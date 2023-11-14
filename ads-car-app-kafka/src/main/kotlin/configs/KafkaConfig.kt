package configs

import AdProcessor
import IAppSettings
import MkplCorSettings

private const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
private const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
private const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
private const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
private const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
private const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

object KafkaConfig : IAppSettings {
    val kafkaHosts: List<String> = (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*")
    val kafkaGroupId: String = System.getenv(KAFKA_GROUP_ID_VAR) ?: "marketplace"
    val kafkaTopicInV1: String = System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "marketplace-in-v1"
    val kafkaTopicOutV1: String = System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "marketplace-out-v1"
    val kafkaTopicInV2: String = System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "marketplace-in-v2"
    val kafkaTopicOutV2: String = System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "marketplace-out-v2"
    override val corSettings: MkplCorSettings = MkplCorSettings()
    override val processor: AdProcessor = AdProcessor(corSettings)
}
