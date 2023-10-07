import configs.KafkaConfig

fun main() = AppKafkaConsumer(
    config = KafkaConfig,
    consumerStrategies = listOf(ConsumerStrategyV1())
).run()
