package com.oceanebelle.learn.hateoas.controller.config;

import com.oceanebelle.learn.logging.LogMessage;
import com.oceanebelle.learn.logging.LogMessageVisitor;
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
