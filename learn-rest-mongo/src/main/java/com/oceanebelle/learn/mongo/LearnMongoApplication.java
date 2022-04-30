package com.oceanebelle.learn.mongo;

import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@Log4j2
@SpringBootApplication
public class LearnMongoApplication {
    private static final String APP = "APP";

    public static void main(String[] args) {
        log.info(LogMessageFactory.startAction(APP).kv("message", "Starting up application."));
        SpringApplication.run(LearnMongoApplication.class, args);
    }

    @PreDestroy
    public void destroy() {
        log.info("Config Location: {}", LoggerContext.getContext(false).getConfiguration().getConfigurationSource());

        log.info(LogMessageFactory.endAction(APP).kv("message", "Shutting down application."));
    }
}
