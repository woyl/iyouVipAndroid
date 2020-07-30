package com.jfkj.im.TIM.redpack.group;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupInfoBean implements Parcelable {

    /**
     * groupOwnerName : 我的
     * groupOwnerId : 113051629334429696
     * groupNotice : 哦哦哦
     * city : 眉山市
     * PersonCount : 1
     * isSuper : 0
     * groupId : 114616609468973056
     * groupOwnerHead : http://iyfile.tiantiancaidian.com/static/VipHead.png
     * limitPersonCount : 60
     * isJoin : 0
     * isFreeze : 0
     * vipLevel : 1
     * groupName : 哦哦
     * redPacketNum : 10
     * totalNum : 0
     * isOwner : 1
     * sendHour : 20
     */

    private String groupOwnerName;
    private String groupOwnerId;
    private String groupNotice;
    private String city;
    private int PersonCount;
    private int isSuper;
    private String groupId;
    private String groupOwnerHead;
    private int limitPersonCount;
    private int isJoin;
    private int isFreeze;
    private String vipLevel;
    private String groupName;
    private int redPacketNum;
    private long totalNum;
    private int isOwner;
    private int sendHour;
    private String hide;

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public GroupInfoBean() {
    }

    protected GroupInfoBean(Parcel in) {
        groupOwnerName = in.readString();
        groupOwnerId = in.readString();
        groupNotice = in.readString();
        city = in.readString();
        PersonCount = in.readInt();
        isSuper = in.readInt();
        groupId = in.readString();
        groupOwnerHead = in.readString();
        limitPersonCount = in.readInt();
        isJoin = in.readInt();
        isFreeze = in.readInt();
        vipLevel = in.readString();
        groupName = in.readString();
        redPacketNum = in.readInt();
        totalNum = in.readInt();
        isOwner = in.readInt();
        sendHour = in.readInt();
    }

    public static final Creator<GroupInfoBean> CREATOR = new Creator<GroupInfoBean>() {
        @Override
        public GroupInfoBean createFromParcel(Parcel in) {
            return new GroupInfoBean(in);
        }

        @Override
        public GroupInfoBean[] newArray(int size) {
            return new GroupInfoBean[size];
        }
    };

    public String getGroupOwnerName() {
        return groupOwnerName;
    }

    public void setGroupOwnerName(String groupOwnerName) {
        this.groupOwnerName = groupOwnerName;
    }

    public String getGroupOwnerId() {
        return groupOwnerId;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public String getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPersonCount() {
        return PersonCount;
    }

    public void setPersonCount(int PersonCount) {
        this.PersonCount = PersonCount;
    }

    public int getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(int isSuper) {
        this.isSuper = isSuper;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupOwnerHead() {
        return groupOwnerHead;
    }

    public void setGroupOwnerHead(String groupOwnerHead) {
        this.groupOwnerHead = groupOwnerHead;
    }

    public int getLimitPersonCount() {
        return limitPersonCount;
    }

    public void setLimitPersonCount(int limitPersonCount) {
        this.limitPersonCount = limitPersonCount;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public int getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(int isFreeze) {
        this.isFreeze = isFreeze;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getRedPacketNum() {
        return redPacketNum;
    }

    public void setRedPacketNum(int redPacketNum) {
        this.redPacketNum = redPacketNum;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public int getSendHour() {
        return sendHour;
    }

    public void setSendHour(int sendHour) {
        this.sendHour = sendHour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupOwnerName);
        dest.writeString(groupOwnerId);
        dest.writeString(groupNotice);
        dest.writeString(city);
        dest.writeInt(PersonCount);
        dest.writeInt(isSuper);
        dest.writeString(groupId);
        dest.writeString(groupOwnerHead);
        dest.writeInt(limitPersonCount);
        dest.writeInt(isJoin);
        dest.writeInt(isFreeze);
        dest.writeString(vipLevel);
        dest.writeString(groupName);
        dest.writeInt(redPacketNum);
        dest.writeLong(totalNum);
        dest.writeInt(isOwner);
        dest.writeInt(sendHour);
    }
}
