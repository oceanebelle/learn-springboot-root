package com.oceanebelle.learn.hateoas.logging;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LogMessage {

    enum State {
        START,
        OK,
        FAIL,
        NOOP
    }

    @Getter
    enum ReservedKeys {
        ACTION("action", 1),
        STATE("state", 2),
        METHOD("method", 3);

        private String key;
        private int sortKey;

        ReservedKeys(String key, int sortKey) {
            this.key = key;
            this.sortKey = sortKey;
        }

        private static Set<String> keys;
        static {
            keys = Arrays.stream(ReservedKeys.values()).map(ReservedKeys::getKey).collect(Collectors.toSet());
        }

        public static boolean isReserved(String other) {
            return keys.contains(other);
        }

        public static Integer getSortKeyByKey(String other) {
            return (keys.contains(other)) ? ReservedKeys.valueOf(other.toUpperCase()).getSortKey() : -1;
        }
    }

    private static ThreadLocal<Map<String, Instant>> startTimeMap = ThreadLocal.withInitial(HashMap::new);

    private Optional<String> action = Optional.empty();
    private Optional<String> method = Optional.empty();
    private Optional<String> state = Optional.empty();

    private String cachedLogMessage;

    private Map<String, String> contextMap = new HashMap<>();

    public LogMessage action(String action) {
        this.action = Optional.ofNullable(action);
        contextMap.put("action", action);
        return this;
    }

    public LogMessage state(String state) {
        this.state = Optional.ofNullable(state);
        contextMap.put("state", state);
        return this;
    }

    public LogMessage method(String method) {
        this.method = Optional.ofNullable(method);
        contextMap.put("method", method);
        return this;
    }

    private void ensureCustomKey(String key) {
        if (ReservedKeys.isReserved(key)) {
            throw new IllegalArgumentException("Cannot use custom key for reserved keys: " + key);
        }
    }

    public LogMessage kv(String key, String value) {
        Objects.requireNonNull(key);
        ensureCustomKey(key);
        contextMap.put(key, value);
        return this;
    }

    private Instant getNow() {
        return Instant.now(Clock.systemUTC());
    }

    private void initClock() {
        var startTime = getNow();
        if (isStartLogging()) {
            startTimeMap.get().put(startLogKey(), startTime);
        }
    }

    private boolean isStartLogging() {
        return state.filter(s -> s.equalsIgnoreCase(State.START.name())).isPresent();
    }

    private String startLogKey() {
        StringBuilder builder = new StringBuilder();
        action.ifPresent(a -> builder.append(a.toLowerCase()));
        builder.append(State.START);
        method.ifPresent(m -> builder.append(m.toLowerCase()));
        return builder.toString();
    }

    private Optional<Duration> updateElapsed() {
        var now = getNow();
        var key = startLogKey();

        if (!isStartLogging() && startTimeMap.get().containsKey(key)) {
            var start = startTimeMap.get().get(key);
            var duration = Optional.of(Duration.between(start, now));
            // the elapsed calculation will only be fetched one time and cleared
            startTimeMap.get().remove(key);
            return duration;
        }

        return Optional.empty();
    }

    static class ActionComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            var isReserved1 = ReservedKeys.isReserved(o1);
            var isReserved2 = ReservedKeys.isReserved(o2);

            if (isReserved1 && isReserved2) {
                return o1.compareTo(o2);
            } else if (isReserved1) {
                return -1 * ReservedKeys.getSortKeyByKey(o1);
            } else if (isReserved2) {
                return (ReservedKeys.getSortKeyByKey(o2));
            } else {
                return o1.compareTo(o2);
            }
        }
    }

    private static final ActionComparator ACTION_COMPARATOR = new ActionComparator();

    private String escape(String original) {
        return (original.contains(" ")) ? "\"" + original + "\"" : original;
    }

    @Override
    public String toString() {

        if (cachedLogMessage == null) {

            initClock();
            var duration = updateElapsed();

            StringBuilder sb = new StringBuilder();
            sb.append(contextMap.entrySet().stream()
                    .filter(e -> !Strings.isBlank(e.getValue())) // skip empty pairs
                    .sorted(Comparator.comparing(Map.Entry::getKey, ACTION_COMPARATOR))
                    .map(e -> String.join("=", e.getKey(), escape(e.getValue())))
                    .collect(Collectors.joining(" ")));

            duration.ifPresent(value -> sb.append(" tookMs=").append(value.toMillis()));

            cachedLogMessage = sb.toString();
        }

        return cachedLogMessage;

    }


}
