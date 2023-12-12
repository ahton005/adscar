import configs.KafkaConfig
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging.logger
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import utils.createKafkaConsumer
import utils.createKafkaProducer
import java.time.Duration.ofSeconds
import java.util.UUID.randomUUID

private val log = logger {}

class AppKafkaConsumer(
    consumerStrategies: List<ConsumerStrategy>,
    private val config: KafkaConfig,
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) {
    private val process = atomic(true)

    private val topicsAndStrategyByInputTopic: Map<String, TopicsAndStrategy> = consumerStrategies.associate {
        val topics = it.topics(config)
        topics.input to TopicsAndStrategy(topics.input, topics.output, it)
    }

    fun run() = runBlocking {
        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(IO) {
                    consumer.poll(ofSeconds(1))
                }
                if (!records.isEmpty) {
                    log.info { "Receive ${records.count()} messages" }
                }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        val resp = config.controllerHelper(
                            { strategy.deserialize(record.value()) },
                            { strategy.serialize(this) },
                            this::class,
                            "Kafka"
                        )
                        sendResponse(resp, outputTopic)
                    } catch (ex: Exception) {
                        log.error(ex) { ex.message }
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
            log.error(ex) { ex.message }
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                log.error(ex) { ex.message }
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(json: String, outputTopic: String) {
        val resRecord = ProducerRecord(
            outputTopic,
            randomUUID().toString(),
            json
        )
        producer.send(resRecord)
        log.info { "Sending message with id ${resRecord.key()} to $outputTopic:\n$json" }
    }

    fun stop() {
        process.value = false
    }
}

private data class TopicsAndStrategy(val inputTopic: String, val outputTopic: String, val strategy: ConsumerStrategy)
