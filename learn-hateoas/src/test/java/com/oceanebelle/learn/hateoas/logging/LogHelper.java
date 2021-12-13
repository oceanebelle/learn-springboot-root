package com.oceanebelle.learn.hateoas.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public class LogHelper {

    public static void resetLogCaptor() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

//        final Layout layout = PatternLayout.createDefaultLayout(config);
//
//        if (config.getAppender("test-appender") == null) {
//            Appender appender = new LogCaptorAppender("test-appender", null, layout, false, null);
//            appender.start();
//            config.addAppender(appender);
//
//
//            AppenderRef ref = AppenderRef.createAppenderRef("test-appender", null, null);
//            AppenderRef[] refs = new AppenderRef[] {ref};
//
//            LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.DEBUG, clz.getTypeName(),
//                    "true", refs, null, config, null );
//            loggerConfig.addAppender(appender, null, null);
//            config.addLogger(clz.getTypeName(), loggerConfig);
//            ctx.updateLoggers();
//
//        } else {
            ((LogCaptorAppender) config.getAppender("test")).clear();
//        }
    }

    public static LogCaptorAppender getLogCaptor() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        return config.getAppender("test");
    }
}
