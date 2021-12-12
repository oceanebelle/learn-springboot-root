package com.oceanebelle.learn.hateoas.logging;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Log4j2
public class LogMessageTest {
    @Test
    public void whenKVisUsed_rejectIfReservedKey() {
        Assertions.assertThatThrownBy(() -> new LogMessage().kv("action", "")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testElapsed() {
        // starts testSTARTmethod
        LogMessage start = new LogMessage();
        start.action("TEST").state("START").kv("aValue","test").method("method");
        log.info(start);

        // starts testSTART
        LogMessage mid = new LogMessage();
        mid.action("TEST").state("START").kv("aValue","test");
        log.info(mid);

        // computes testSTARTmethod
        LogMessage end = new LogMessage();
        end.action("TEST").state("X").method("method").kv("message","the end");
        log.info(end); // with took

        LogMessage dup = new LogMessage();
        dup.action("TEST").state("X").method("method").kv("message","the end").kv("duplicated", "true");
        log.info(dup); // without took

    }
}
