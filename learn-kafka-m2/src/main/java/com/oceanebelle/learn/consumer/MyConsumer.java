package com.oceanebelle.learn.consumer;

import com.oceanebelle.learn.kafka.LogMessage;
import com.oceanebelle.learn.kafka.LogMessageVisitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.oceanebelle.learn.kafka.LogMessageFactory.startAction;

@Component
@Log4j2
public class MyConsumer {

    private static final String ACTION="RECEIVE_KAFKA";
    private static final String RECEIVE_MESSAGE="receive";
    private static final String TOPIC="topic";
    private static final String PARTITION="partition";
    private static final String OFFSET="offset";

    @KafkaListener(groupId = "${spring.kakfa.consumer.groupId}", topics="${app.topic}")
    public void receive(ConsumerRecord<String, String> data) {
        var recordVisitor = new RecordVisitor(data);
        log.info(startAction(ACTION, RECEIVE_MESSAGE).with(recordVisitor));
    }

    @AllArgsConstructor
    static class RecordVisitor implements LogMessageVisitor {

        private ConsumerRecord<String, String> record;

        @Override
        public void visit(LogMessage logMessage) {
            logMessage.kv(TOPIC, record.topic());
            logMessage.kv(PARTITION, record.partition());
            logMessage.kv(OFFSET, record.offset());
            logMessage.kv("size", record.serializedValueSize());
            logMessage.kv("ts", record.timestamp());
        }
    }
}
