package com.jfkj.im.event;

public class UserInfoEvent {
    private boolean isUpdate;

    public UserInfoEvent(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
