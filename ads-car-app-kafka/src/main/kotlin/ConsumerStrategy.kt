import configs.KafkaConfig
import ru.zyablov.otus.otuskotlin.adscar.api.v1.apiV1RequestDeserialize
import ru.zyablov.otus.otuskotlin.adscar.api.v1.apiV1ResponseSerialize
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IRequest
import ru.zyablov.otus.otuskotlin.adscar.common.InnerContext
import ru.zyablov.otus.otuskotlin.adscar.mappers.v1.toInnerContext
import ru.zyablov.otus.otuskotlin.adscar.mappers.v1.toTransport

interface ConsumerStrategy {
    fun topics(config: KafkaConfig): InputOutputTopics
    fun serialize(source: InnerContext): String
    fun deserialize(value: String): InnerContext
}

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: KafkaConfig) = InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)

    override fun serialize(source: InnerContext) = apiV1ResponseSerialize(source.toTransport())

    override fun deserialize(value: String) = apiV1RequestDeserialize<IRequest>(value).toInnerContext()
}
