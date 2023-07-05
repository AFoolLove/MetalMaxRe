package me.afoolslove.metalmaxre.log.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelStringListAppender extends AppenderBase<ILoggingEvent> {
    Layout<ILoggingEvent> layout;
    private List<Map.Entry<Level, String>> list = new ArrayList<>();

    public void start() {
        list.clear();

        if (layout == null || !layout.isStarted()) {
            return;
        }
        super.start();
    }

    public void stop() {
        super.stop();
    }

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        String res = layout.doLayout(eventObject);
        list.add(Map.entry(eventObject.getLevel(), res));
    }

    public List<Map.Entry<Level, String>> getList() {
        return list;
    }
}
