package com.oceanebelle.learn.mongo.repository;


import com.oceanebelle.learn.kafka.LogMessage;
import com.oceanebelle.learn.kafka.LogMessageVisitor;


public interface UserRepository extends LogMessageVisitor {
    @Override
    default void visit(LogMessage message) {
        message.kv("entity", Object.class.getSimpleName());
    }
}
