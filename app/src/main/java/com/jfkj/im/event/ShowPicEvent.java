package com.jfkj.im.event;

public class ShowPicEvent {
    String path;

    public ShowPicEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
