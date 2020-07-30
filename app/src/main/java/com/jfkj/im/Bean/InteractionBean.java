package com.jfkj.im.Bean;



public class InteractionBean  {
    String name="";
    String message="";
    String headurl="";
    String time="";
    String number="0";
    long id=0;
    String top="";
    String noDisturb="";

    long groupId=0L;
    String msgIds="";
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(String noDisturb) {
        this.noDisturb = noDisturb;
    }

    public InteractionBean() {
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(String msgIds) {
        this.msgIds = msgIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
