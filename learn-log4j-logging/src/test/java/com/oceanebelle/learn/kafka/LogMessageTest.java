package com.oceanebelle.learn.kafka;

import com.oceanebelle.learn.kafka.plugin.LogCaptorAppender;
import com.oceanebelle.learn.kafka.test.LogHelper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

@Log4j2
public class LogMessageTest {
    @Test
    public void whenKVisUsed_rejectIfReservedKey() {
        Assertions.assertThatThrownBy(() -> new LogMessage().kv("action", "")).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new LogMessage().kv("method", "")).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new LogMessage().kv("state", "")).isInstanceOf(IllegalArgumentException.class);
    }

    @BeforeEach
    public void setup() {
        LogHelper.captureLogFor(LogMessageTest.class);
    }

    @Test
    public void testElapsed2() {
        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        Assertions.assertThat(logCaptor.getLogCount()).isEqualTo(0);
    }

    @Test
    public void testElapsed() {

        // starts testSTARTmethod
        log.info(LogMessageFactory.startAction("TEST", "method").message("test"));

        // starts testSTART
        log.info(LogMessageFactory.startAction("TEST").message("test"));

        // computes testSTARTmethod, end state calculates tookMs
        log.info(LogMessageFactory.completeAction("TEST", "method").message("original")); // with took

        // duplicate log will not calculate tookMs
        log.info(LogMessageFactory.completeAction("TEST", "method").message("duplicate")); // without took

        // log message using visitor
        log.info(LogMessageFactory.startAction("TEST").with(TestObject.builder().name("aName").build()));

        // log map object
        var map = new HashMap<String, String>();
        map.put("key", "value");
        log.info(LogMessageFactory.startAction("TEST").with(map));

        // log a boolean
        log.info(LogMessageFactory.startAction("TEST").kv("aBoolean", true));

        // log numbers
        log.info(LogMessageFactory.startAction("TEST").kv("int", 1).kv("long", 2L).kv("decimal", 1.0));

        // log a collection
        log.info(LogMessageFactory.startAction("TEST").kv("coll", Arrays.asList("first", "second")));


        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        Assertions.assertThat(logCaptor.getLogCount()).isEqualTo(9);

        // Capture and verify log
        Assertions.assertThat(logCaptor.getLog(0)).matches("action=TEST method=method state=START message=test");
        Assertions.assertThat(logCaptor.getLog(1)).matches("action=TEST state=START message=test");
        Assertions.assertThat(logCaptor.getLog(2)).matches("action=TEST method=method state=COMPLETE message=original tookMs=\\d+");
        Assertions.assertThat(logCaptor.getLog(3)).matches("action=TEST method=method state=COMPLETE message=duplicate");
        Assertions.assertThat(logCaptor.getLog(4)).matches("action=TEST state=START name=aName");
        Assertions.assertThat(logCaptor.getLog(5)).matches("action=TEST state=START key=value");
        Assertions.assertThat(logCaptor.getLog(6)).matches("action=TEST state=START aBoolean=true");
        Assertions.assertThat(logCaptor.getLog(7)).matches("action=TEST state=START decimal=1.0 int=1 long=2");
        Assertions.assertThat(logCaptor.getLog(8)).matches("action=TEST state=START coll=\\(first,second\\)");


    }

    @Data
    @Builder
    static class TestObject implements LogMessageVisitor {

        private String name;

        @Override
        public void visit(LogMessage message) {
            message.kv("name", name);
        }
    }
}
