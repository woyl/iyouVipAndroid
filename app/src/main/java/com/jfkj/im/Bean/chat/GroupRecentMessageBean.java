package com.jfkj.im.Bean.chat;


import java.io.Serializable;

//最近消息
public class GroupRecentMessageBean implements Serializable {
    String head_url;
    String message;
    String  name;
    String groupid;

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    @Override
    public String toString() {
        return "GroupRecentMessageBean{" +
                "head_url='" + head_url + '\'' +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", groupid='" + groupid + '\'' +
                '}';
    }
}
