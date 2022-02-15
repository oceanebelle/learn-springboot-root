package com.oceanebelle.learn.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class HelloWorldCommand {
    @ShellMethod("Hello World")
    public String hello() {
        return "Hello World";
    }
}
