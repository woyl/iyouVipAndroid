package com.jfkj.im.event;

public class PartyRedEvent {
    private boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public PartyRedEvent(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
}
