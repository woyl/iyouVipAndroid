package com.jfkj.im.event;

public class UpdateMessageEvent {
    private boolean isUpdate;

    public UpdateMessageEvent(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
