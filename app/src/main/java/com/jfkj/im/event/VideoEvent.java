package com.jfkj.im.event;

public class VideoEvent {

    public VideoEvent(boolean isStart) {
        this.isStart = isStart;
    }

    private boolean isStart;

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}
