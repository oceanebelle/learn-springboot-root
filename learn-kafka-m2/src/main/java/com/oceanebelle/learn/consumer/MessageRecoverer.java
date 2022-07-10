package com.oceanebelle.learn.consumer;

import com.oceanebelle.learn.kafka.LogMessageFactory;
import com.oceanebelle.learn.kafka.LogMessageVisitor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.function.BiConsumer;

@Log4j2
public class MessageRecoverer   implements BiConsumer<ConsumerRecord<?, ?>, Exception> {

    @Override
    public void accept(ConsumerRecord<?, ?> consumerRecord, Exception e) {
        log.warn(LogMessageFactory.atomicAction("RECOVER", "recoverMessage")
                .with(pretty(consumerRecord, e)));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static LogMessageVisitor pretty(ConsumerRecord<?, ?> record, Exception ex) {
        return log -> {
            log.kv("topic", record.topic());
            log.kv("partition", record.partition());
            log.kv("offset", record.offset());
            log.kv("x-errorClass", ex.getClass().getName());
            log.kv("x-cause", ex.getCause().getClass().getName());
        };
    }
}
