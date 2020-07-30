package com.jfkj.im.TIM.modules.group.member;

import com.tencent.imsdk.TIMGroupMemberInfo;

import java.io.Serializable;

public class GroupMemberInfo implements Serializable,Comparable<GroupMemberInfo> {

    private String iconUrl;
    private String account;
    private String signature;
    private String location;
    private String birthday;
    private boolean isTopChat;
    private boolean isFriend;
    private long joinTime;
    private long tinyId;
    private int memberType;
    private TIMGroupMemberInfo detail;

    private String name;
    private int isOwner;

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isTopChat() {
        return isTopChat;
    }

    public void setTopChat(boolean topChat) {
        isTopChat = topChat;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public long getTinyId() {
        return tinyId;
    }

    public void setTinyId(long tinyId) {
        this.tinyId = tinyId;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public TIMGroupMemberInfo getDetail() {
        return detail;
    }

    public void setDetail(TIMGroupMemberInfo detail) {
        this.detail = detail;
    }

    public GroupMemberInfo covertTIMGroupMemberInfo(TIMGroupMemberInfo info) {
        setAccount(info.getUser());
        setTinyId(info.getTinyId());
        setJoinTime(info.getJoinTime());
        setMemberType(info.getRole());
        return this;
    }

    @Override
    public int compareTo(GroupMemberInfo o) {
        return  o.getIsOwner() - this.isOwner;
    }
}