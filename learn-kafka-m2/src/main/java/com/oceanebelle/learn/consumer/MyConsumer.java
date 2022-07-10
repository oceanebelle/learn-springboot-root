package com.oceanebelle.learn.consumer;

import com.oceanebelle.learn.kafka.LogMessage;
import com.oceanebelle.learn.kafka.LogMessageVisitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.oceanebelle.learn.kafka.LogMessageFactory.startAction;

@Component
@Log4j2
public class MyConsumer {

    private static final String ACTION="RECEIVE_KAFKA";
    private static final String RECEIVE_MESSAGE="receive";

    private final ConcurrentHashMap<Integer, Long> offsets = new ConcurrentHashMap<>(8);


    //@KafkaListener(groupId = "${spring.kakfa.consumer.groupId}", topics="${app.topic}")
    @KafkaListener(groupId = "${spring.kakfa.consumer.groupId}", topics="${app.topic}")
    public void receive(ConsumerRecord<String, String> data) {
        offsets.computeIfPresent(data.partition(), (k, v) -> {
            if ((data.offset() - v) == 1) {
                return data.offset();
            } else throw new IllegalStateException("skipping messages"); });
        offsets.computeIfAbsent(data.partition(), (k) -> data.offset());
        log.info(startAction(ACTION, RECEIVE_MESSAGE).with(LogVisitors.visit(data)));
        try {
            Thread.sleep(10000);
            throw new RuntimeException("A Random ERROR");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
