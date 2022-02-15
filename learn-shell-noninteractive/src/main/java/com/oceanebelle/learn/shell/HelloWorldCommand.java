package com.oceanebelle.learn.shell;

import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Log4j2
@ShellComponent
public class HelloWorldCommand {
    @ShellMethod("Hello World")
    public String hello() {
        log.info(LogMessageFactory.startAction("SHELL_COMMAND").method());
        return "Hello World";
    }
}
