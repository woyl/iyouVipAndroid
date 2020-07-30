package com.jfkj.im.event;

public class SeventyEvent {
    private boolean isShow;

    public SeventyEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
