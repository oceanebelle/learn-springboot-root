package com.oceanebelle.learn.shell;

import com.oceanebelle.learn.kafka.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class LearnShellApplication {
    private static final String APP = "APP";

    public static void main(String[] args) {
        log.info(LogMessageFactory.startAction(APP).kv("message", "Starting up application."));
        SpringApplication.run(LearnShellApplication.class, args);
    }
}
