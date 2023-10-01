package utils

import configs.KafkaConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

fun KafkaConfig.createKafkaConsumer(): KafkaConsumer<String, String> = KafkaConsumer<String, String>(
    Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHosts)
        put(GROUP_ID_CONFIG, kafkaGroupId)
        put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
    }
)

fun KafkaConfig.createKafkaProducer(): KafkaProducer<String, String> = KafkaProducer<String, String>(
    Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHosts)
        put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }
)
