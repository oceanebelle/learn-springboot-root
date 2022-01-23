package com.oceanebelle.learn.logging;

import com.oceanebelle.learn.logging.plugin.LogCaptorAppender;
import com.oceanebelle.learn.logging.test.LogHelper;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Log4j2
public class LogHelperTest {

    @BeforeEach
    public void setup() {
        LogHelper.captureLogFor(LogHelperTest.class);
    }

    @Test
    public void testElapsed2() {
        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        Assertions.assertThat(logCaptor.getLogCount()).isEqualTo(0);
    }

    @Test
    public void testElapsed() {

        log.info("first log");

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        Assertions.assertThat(logCaptor.getLogCount()).isEqualTo(1);

    }
}
