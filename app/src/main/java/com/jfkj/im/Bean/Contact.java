package com.jfkj.im.Bean;

import java.io.Serializable;

public class Contact implements Serializable {
    private String mName;
    private int mType;
    private String head;
    private int vipLevel;
    private int gender;
    private int top;
    private int noDisturb;
    private String nickname;
    private int state;
    private Object topTime;
    private long userId;
    private boolean select;
    public String getHead() {
        return head;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(int noDisturb) {
        this.noDisturb = noDisturb;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getTopTime() {
        return topTime;
    }

    public void setTopTime(Object topTime) {
        this.topTime = topTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public Contact(String name, int type) {
        mName = name;
        mType = type;
    }

    public Contact(String mName, int mType, String head, int vipLevel, int gender, int top, int noDisturb, String nickname, int state, Object topTime, long userId) {
        this.mName = mName;
        this.mType = mType;
        this.head = head;
        this.vipLevel = vipLevel;
        this.gender = gender;
        this.top = top;
        this.noDisturb = noDisturb;
        this.nickname = nickname;
        this.state = state;
        this.topTime = topTime;
        this.userId = userId;
    }

    public String getmName() {
        return mName;
    }

    public int getmType() {
        return mType;
    }

}
