package com.oceanebelle.learn.logging;

import java.util.Objects;

public class LogMessageFactory {

    public static LogMessage startAction(String action) {
        Objects.requireNonNull(action);
        return new LogMessage().action(action).state(LogMessage.State.START.name());
    }

    public static LogMessage endAction(String action) {
        Objects.requireNonNull(action);
        return new LogMessage().action(action).state(LogMessage.State.COMPLETE.name());
    }

    public static LogMessage failAction(String action) {
        Objects.requireNonNull(action);
        return new LogMessage().action(action).state(LogMessage.State.FAIL.name());
    }

    public static LogMessage startAction(String action, String method) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(method);
        return new LogMessage().action(action).state(LogMessage.State.START.name()).method(method);
    }

    public static LogMessage endAction(String action, String method) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(method);
        return new LogMessage().action(action).state(LogMessage.State.COMPLETE.name()).method(method);
    }

    public static LogMessage failAction(String action, String method) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(method);
        return new LogMessage().action(action).state(LogMessage.State.FAIL.name()).method(method);
    }

    public static LogMessage startAccess(String method) {
        return new LogMessage().action("ACCESS").state(LogMessage.State.START.name()).method(method);
    }

    public static LogMessage endAccess(String method) {
        return new LogMessage().action("ACCESS").state(LogMessage.State.COMPLETE.name()).method(method);
    }

    public static LogMessage failAccess(String method) {
        return new LogMessage().action("ACCESS").state(LogMessage.State.FAIL.name()).method(method);
    }

}
