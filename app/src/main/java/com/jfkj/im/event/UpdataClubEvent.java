package com.jfkj.im.event;

public class UpdataClubEvent {
    private boolean isUp;

    public UpdataClubEvent(boolean isUp) {
        this.isUp = isUp;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }
}
