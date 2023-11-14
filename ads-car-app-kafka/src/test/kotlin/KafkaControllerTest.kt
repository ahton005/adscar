package ru.otus.otuskotlin.marketplace.app.kafka

import AppKafkaConsumer
import ConsumerStrategyV1
import configs.KafkaConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy.EARLIEST
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.zyablov.otus.otuskotlin.adscar.api.v1.apiV1RequestSerialize
import ru.zyablov.otus.otuskotlin.adscar.api.v1.apiV1ResponseDeserialize
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateOrUpdateObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebug
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode.STUB
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugStubs.SUCCESS
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility.OWNER_ONLY
import kotlin.test.assertEquals

private const val PARTITION = 0

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = KafkaConfig
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(
            config = config,
            consumerStrategies = listOf(ConsumerStrategyV1()),
            consumer = consumer,
            producer = producer
        )

        consumer.schedulePollTask {
            consumer.rebalance(listOf(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        AdCreateRequest(
                            requestId = "1",
                            adCreate = AdCreateOrUpdateObject(
                                title = "Продается автомобиль",
                                description = "some testing ad to check them all",
                                visibility = OWNER_ONLY
                            ),
                            debug = AdRequestDebug(
                                mode = STUB,
                                stub = SUCCESS
                            )
                        )
                    )
                )
            )
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    1L,
                    "test-1",
                    apiV1RequestSerialize(
                        AdCreateRequest(
                            requestId = "2",
                            adCreate = AdCreateOrUpdateObject(
                                title = "Продается автомобиль",
                                description = "some testing ad to check them all",
                                visibility = OWNER_ONLY
                            ),
                            debug = AdRequestDebug(
                                mode = STUB,
                                stub = SUCCESS
                            )
                        )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<AdCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("1", result.requestId)
        assertEquals("Продается автомобиль", result.ad?.title)

        val message1 = producer.history()[1]
        val result1 = apiV1ResponseDeserialize<AdCreateResponse>(message1.value())
        assertEquals(outputTopic, message1.topic())
        assertEquals("2", result1.requestId)
        assertEquals("Продается автомобиль", result1.ad?.title)
    }
}
