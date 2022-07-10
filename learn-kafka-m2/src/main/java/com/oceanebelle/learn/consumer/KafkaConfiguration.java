package com.oceanebelle.learn.consumer;

import com.oceanebelle.learn.kafka.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oceanebelle.learn.kafka.LogMessageFactory.atomicAction;


@Configuration
@EnableKafka
@Log4j2
public class KafkaConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;




    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory(@Value("${app.consumer.concurrency:8}") int concurrency) {

        ConcurrentKafkaListenerContainerFactory<Integer, String> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        containerFactory.setConsumerFactory(consumerFactory());

        containerFactory.setConcurrency(concurrency);

        containerFactory.getContainerProperties().setLogContainerConfig(true);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        containerFactory.getContainerProperties().setSyncCommits(true);
        containerFactory.getContainerProperties().setConsumerRebalanceListener(new AppRebalanceListener());

        var backoff = new FixedBackOff(5000, 0);
        var errorHandler = new SeekToCurrentErrorHandler(new MessageRecoverer(), backoff);
        errorHandler.setAckAfterHandle(true);
        containerFactory.setErrorHandler(errorHandler);

        return containerFactory;
    }



    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        ConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfigs());
        return consumerFactory;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        var consumerProperties = kafkaProperties.buildConsumerProperties();

        var logMsg = LogMessageFactory.startAction("CONFIGURE", "kafkaConsumer");
        consumerProperties.forEach((k, v) -> logMsg.kv(k, v.toString()));
        log.info(logMsg);

        return consumerProperties;
    }


}
