package com.oceanebelle.learn.logging.test;

import com.oceanebelle.learn.logging.plugin.LogCaptorAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;


public class LogHelper {

    public static final String TEST_APPENDER = "test-appender";

    public static void captureLogFor(Class<?>... clz) {
        final Configuration config = getLogConfiguration();

        var appender = getLogCaptorAppender(config);
        appender.clear();

        for(Class<?> c : clz) {
            var clzLogger = (Logger) config.getLoggerContext().getLogger(c);
            config.addLoggerAppender(clzLogger, appender);
        }
    }

    public static LogCaptorAppender getLogCaptor() {
        return getLogConfiguration().getAppender(TEST_APPENDER);
    }

    private static LogCaptorAppender getLogCaptorAppender(Configuration config) {
        var appender = (LogCaptorAppender) config.getAppender(TEST_APPENDER);
        if ( appender == null) {
            var newAppender = LogCaptorAppender.createAppender(TEST_APPENDER, null, null, null);
            newAppender.start();
            config.addAppender(newAppender);
            appender = newAppender;
        }
        return appender;
    }

    private static Configuration getLogConfiguration() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        return ctx.getConfiguration();
    }
}
