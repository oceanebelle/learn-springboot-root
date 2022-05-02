package com.oceanebelle.learn.mongo.controller.config;

import com.oceanebelle.learn.kafka.LogMessage;
import com.oceanebelle.learn.kafka.LogMessageVisitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestId implements LogMessageVisitor {
    private String requestId;

    @Override
    public void visit(LogMessage message) {
        message.kv("requestId", requestId);
    }
}
