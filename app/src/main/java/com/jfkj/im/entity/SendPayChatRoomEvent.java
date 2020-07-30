package com.jfkj.im.entity;

public class SendPayChatRoomEvent {
    private boolean isPay;

    public SendPayChatRoomEvent(boolean isPay) {
        this.isPay = isPay;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }
}
