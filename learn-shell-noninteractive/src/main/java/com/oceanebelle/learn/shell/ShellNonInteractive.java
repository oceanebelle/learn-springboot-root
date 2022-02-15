package com.oceanebelle.learn.shell;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.stereotype.Component;

@Component
@Order(InteractiveShellApplicationRunner.PRECEDENCE - 100)
@AllArgsConstructor
public class ShellNonInteractive implements ApplicationRunner {

    private final Shell shell;
    private final ConfigurableEnvironment environment;
    private final DefaultResultHandler resultHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.getSourceArgs().length > 0) {
            InteractiveShellApplicationRunner.disable(environment);
            var input = String.join(" ", args.getSourceArgs());
            resultHandler.handleResult(shell.evaluate(() -> input));
            shell.evaluate(() -> "exit");
        }
    }
}
