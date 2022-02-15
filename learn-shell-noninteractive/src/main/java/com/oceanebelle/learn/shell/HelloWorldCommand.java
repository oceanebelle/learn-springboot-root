package com.oceanebelle.learn.shell;

import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Log4j2
@ShellComponent
public class HelloWorldCommand {

    @NewSpan
    @ShellMethod("Hello World")
    public String hello() {
        log.info(LogMessageFactory.startAction("RUN_COMMAND").method());
        try {
            return "Hello World";
        } finally {
            log.info(LogMessageFactory.endAction("RUN_COMMAND").method());
        }
    }
}
