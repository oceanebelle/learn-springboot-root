package com.oceanebelle.learn.hateoas.logging;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        name = "LogCaptorAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class LogCaptorAppender extends AbstractAppender {
    private List<LogEvent> events = new ArrayList<>();
    private List<String> formattedEvent = new ArrayList<>();

    protected LogCaptorAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @Override
    public void append(LogEvent event) {
        formattedEvent.add(new String(getLayout().toByteArray(event), StandardCharsets.UTF_8));
        events.add(event);
    }

    public int getLogCount() {
        return events.size();
    }

    public void clear() {
        events.clear();
    }

    // Your custom appender needs to declare a factory method
    // annotated with `@PluginFactory`. Log4j will parse the configuration
    // and call this factory method to construct an appender instance with
    // the configured attributes.
    @PluginFactory
    public static LogCaptorAppender createAppender(@PluginAttribute("name") String name,
                                                      @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                      @PluginElement("Filter") final Filter filter, @PluginAttribute("otherAttribute") String otherAttribute) {


        if (name == null) {
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        System.out.println("Initializing appender");
        return new LogCaptorAppender(name, filter, layout, false, null);
    }
}
