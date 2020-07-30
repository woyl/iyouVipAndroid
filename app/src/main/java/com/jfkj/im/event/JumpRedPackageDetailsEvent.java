package com.jfkj.im.event;

public class JumpRedPackageDetailsEvent {
    private String redId;
    private String id;
    private String state;

    public JumpRedPackageDetailsEvent(String redId, String id, String state) {
        this.redId = redId;
        this.id = id;
        this.state = state;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
