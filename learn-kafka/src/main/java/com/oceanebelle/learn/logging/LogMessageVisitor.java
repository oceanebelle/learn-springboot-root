package com.oceanebelle.learn.logging;

/**
 * Hook to objects to collect logging context
 */
public interface LogMessageVisitor {
    void visit(LogMessage message);
}
