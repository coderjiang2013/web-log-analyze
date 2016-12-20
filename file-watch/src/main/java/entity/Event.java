package entity;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Created by CoderJiang on 2016/12/14.
 */
public class Event {

    private String appendContent;

    private WatchEvent watchEvent;

    private Path path;

    public Event(String modifyContent, Path path, WatchEvent<?> watchEvent) {
        this.appendContent = modifyContent;
        this.path = path;
        this.watchEvent = watchEvent;
    }

    public String getAppendContent() {
        return appendContent;
    }

    public void setAppendContent(String appendContent) {
        this.appendContent = appendContent;
    }

    public WatchEvent getWatchEvent() {
        return watchEvent;
    }

    public void setWatchEvent(WatchEvent watchEvent) {
        this.watchEvent = watchEvent;
    }
}
