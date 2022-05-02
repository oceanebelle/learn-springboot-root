package com.oceanebelle.learn.kafka;

/**
 * Hook to objects to collect logging context
 */
public interface LogMessageVisitor {
    void visit(LogMessage message);
}
