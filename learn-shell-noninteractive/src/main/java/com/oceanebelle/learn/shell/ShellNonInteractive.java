package com.oceanebelle.learn.shell;

import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.stereotype.Component;

@Component
@Order(InteractiveShellApplicationRunner.PRECEDENCE - 100)
@AllArgsConstructor
@Log4j2
public class ShellNonInteractive implements ApplicationRunner {

    private final Shell shell;
    private final ConfigurableEnvironment environment;
    private final DefaultResultHandler resultHandler;

    @NewSpan
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.getSourceArgs().length > 0) {
            InteractiveShellApplicationRunner.disable(environment);
            var input = String.join(" ", args.getSourceArgs());
            log.info(LogMessageFactory.startAction("NON_INTERACTIVE").kv("command", input));
            resultHandler.handleResult(shell.evaluate(() -> input));
            shell.evaluate(() -> "exit");
            log.info(LogMessageFactory.endAction("NON_INTERACTIVE").kv("command", input));
        }
    }
}
