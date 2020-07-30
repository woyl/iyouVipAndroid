package com.jfkj.im.Bean;

public class VideocallBean {


    /**
     * roomID : 3274597346
     * requestUser : 116640184111595520
     * videoState : 0
     * version : 2
     */

    private long roomID;
    private String requestUser;
    private int videoState;
    private int version;
    String sendType;

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public int getVideoState() {
        return videoState;
    }

    public void setVideoState(int videoState) {
        this.videoState = videoState;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
