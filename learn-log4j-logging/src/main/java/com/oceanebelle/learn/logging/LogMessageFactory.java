package com.oceanebelle.learn.logging;

public class LogMessageFactory {
    public static LogMessage startAccess(String method) {
        return new LogMessage().action("ACCESS").state(LogMessage.State.START.name()).method(method);
    }

    public static LogMessage endAccess(String method) {
        return new LogMessage().action("ACCESS").state(LogMessage.State.OK.name()).method(method);
    }

    public static LogMessage failAccess(String method) {
        return new LogMessage().action("ACCESS").state(LogMessage.State.FAIL.name()).method(method);
    }

}
