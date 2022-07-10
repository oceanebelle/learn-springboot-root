package com.oceanebelle.learn.producer;

import com.oceanebelle.learn.kafka.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.oceanebelle.learn.kafka.LogMessageFactory.*;

@Component
@Log4j2
public class MyProducer {

    private static final String ACTION="SEND_KAFKA";
    private static final String SEND_MESSAGE="sendMessage";
    private static final String TOPIC="topic";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${app.topic}")
    private String topic;

    @Scheduled(fixedRate = 1000)
    @NewSpan
    public void sendMessage() {
        log.info(startAction(ACTION, SEND_MESSAGE).kv("topic", topic));
        try {
            kafkaTemplate.send(topic, "hello-" + Instant.now().getNano()).get(1, TimeUnit.SECONDS);
            log.info(successAction(ACTION, SEND_MESSAGE).kv(TOPIC, topic));
        } catch (ExecutionException e) {
            log.error(failAction(ACTION, SEND_MESSAGE).kv(TOPIC, topic), e);
        } catch (TimeoutException | InterruptedException e) {
            log.error(failAction(ACTION, SEND_MESSAGE).kv(TOPIC, topic), e);
        }
    }
}
