package com.jfkj.im.event;

public class GameStateEvent {
    private boolean isUpdate;

    public GameStateEvent(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
