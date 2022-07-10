package com.oceanebelle.learn.consumer;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oceanebelle.learn.kafka.LogMessageFactory.atomicAction;

@Log4j2
public class AppRebalanceListener implements ConsumerAwareRebalanceListener {

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        log.error(atomicAction("REBALANCE", "revoked")
                .with("partitions", partitions
                        .stream().map(AppRebalanceListener::pretty)
                        .collect(Collectors.toList())));
    }

    @Override
    public void onPartitionsLost(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        log.error(atomicAction("REBALANCE", "lost")
                        .kv("groupId", consumer.groupMetadata().groupId())
                        .kv("memberId", consumer.groupMetadata().memberId())
                .with("partitions", partitions
                        .stream().map(AppRebalanceListener::pretty)
                        .collect(Collectors.toList())));
    }

    @Override
    public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        log.info(atomicAction("REBALANCE", "assigned")
                .kv("groupId", consumer.groupMetadata().groupId())
                .kv("groupInstanceId", consumer.groupMetadata().groupInstanceId().orElse(null))
                .kv("memberId", consumer.groupMetadata().memberId())
                        .with("offsets", consumer.committed(new HashSet<>(partitions))
                                .entrySet().stream().map(AppRebalanceListener::prettyOffset)
                                .collect(Collectors.toList()))
                .with("partitions", partitions
                        .stream().map(AppRebalanceListener::pretty)
                        .collect(Collectors.toList())));
    }

    private static String pretty(TopicPartition partition) {
        return String.join("-", partition.topic(), String.valueOf(partition.partition()));
    }

    private static String prettyOffset(Map.Entry<TopicPartition, OffsetAndMetadata> entry) {
        if (entry.getValue() == null) {
            return "";
        } else {
            return String.format("%s-%d:%d", entry.getKey().topic(), entry.getKey().partition(),
                    entry.getValue().offset());
        }
    }
}
