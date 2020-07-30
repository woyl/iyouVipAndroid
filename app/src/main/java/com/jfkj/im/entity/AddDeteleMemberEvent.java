package com.jfkj.im.entity;

public class AddDeteleMemberEvent {
    private boolean isSend;

    public AddDeteleMemberEvent(boolean isSend) {
        this.isSend = isSend;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
