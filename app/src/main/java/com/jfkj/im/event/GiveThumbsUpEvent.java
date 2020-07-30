package com.jfkj.im.event;

public class GiveThumbsUpEvent {
    private boolean isGive;
    private String taskId;
    private String userId;
    private String money;
    private String taskContent;
    private String sendId;




    public GiveThumbsUpEvent(boolean isGive, String taskId, String userId,String money,String taskContent,String sendId) {
        this.isGive = isGive;
        this.taskId = taskId;
        this.userId = userId;
        this.money = money;
        this.taskContent = taskContent;
        this.sendId = sendId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isGive() {
        return isGive;
    }

    public void setGive(boolean give) {
        isGive = give;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }
}
