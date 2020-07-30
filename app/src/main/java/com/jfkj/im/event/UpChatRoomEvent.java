package com.jfkj.im.event;

public class UpChatRoomEvent {
    private boolean upDateMessage;

    public UpChatRoomEvent(boolean upDateMessage) {
        this.upDateMessage = upDateMessage;
    }

    public boolean isUpDateMessage() {
        return upDateMessage;
    }

    public void setUpDateMessage(boolean upDateMessage) {
        this.upDateMessage = upDateMessage;
    }
}
