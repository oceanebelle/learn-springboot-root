package com.oceanebelle.learn.consumer;

import com.oceanebelle.learn.kafka.LogMessageVisitor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public final class LogVisitors {
    private static final String TOPIC="topic";
    private static final String PARTITION="partition";
    private static final String OFFSET="offset";

    public static LogMessageVisitor visit(ConsumerRecord<?, ?> record) {
        return logMessage -> {
            logMessage.kv(TOPIC, record.topic());
            logMessage.kv(PARTITION, record.partition());
            logMessage.kv(OFFSET, record.offset());
            logMessage.kv("size", record.serializedValueSize());
            logMessage.kv("ts", record.timestamp());
        };
    }
}
