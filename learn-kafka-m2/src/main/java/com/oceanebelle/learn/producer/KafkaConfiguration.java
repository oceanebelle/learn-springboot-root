package com.oceanebelle.learn.producer;

import com.oceanebelle.learn.kafka.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@Configuration
@EnableScheduling
@Log4j2
public class KafkaConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;


    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate() {
        return new KafkaTemplate<Integer, String>(producerFactory());
    }

    private Map<String, Object> producerConfigs() {
        var producerProperties = kafkaProperties.buildProducerProperties();

        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        var logMsg = LogMessageFactory.startAction("CONFIGURE", "kafkaProducer");
        producerProperties.forEach((k, v) -> logMsg.kv(k, v.toString()));

        log.info(logMsg);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return producerProperties;
    }

    private ProducerFactory<Integer, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
}
