package com.jfkj.im.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupNewBean implements Parcelable {

    /**
     * groupNotice : OK公告
     * failCount : 0
     * isSuper : 0
     * groupId : 113537685452357632
     * groupHead : http://iyfile.tiantiancaidian.com/static/VipHead.png
     * firstFailDate :
     * sort : 1583591580595
     * ownerId : 113051629334429696
     * addDate : 2020-03-07 22:33:00
     * limitPersonCount : 60
     * personCount : 1
     * isFreeze : 0
     * groupName : 新建南路
     * redPacketNum : 10
     * totalNum : 0
     * sendHour : 20
     */

    private String groupNotice;
    private int failCount;
    private int isSuper;
    private String groupId;
    private String groupHead;
    private String firstFailDate;
    private String sort;
    private String ownerId;
    private String addDate;
    private int limitPersonCount;
    private int personCount;
    private int isFreeze;
    private String groupName;
    private int redPacketNum;
    private int totalNum;
    private int sendHour;

    protected GroupNewBean(Parcel in) {
        groupNotice = in.readString();
        failCount = in.readInt();
        isSuper = in.readInt();
        groupId = in.readString();
        groupHead = in.readString();
        firstFailDate = in.readString();
        sort = in.readString();
        ownerId = in.readString();
        addDate = in.readString();
        limitPersonCount = in.readInt();
        personCount = in.readInt();
        isFreeze = in.readInt();
        groupName = in.readString();
        redPacketNum = in.readInt();
        totalNum = in.readInt();
        sendHour = in.readInt();
    }

    public static final Creator<GroupNewBean> CREATOR = new Creator<GroupNewBean>() {
        @Override
        public GroupNewBean createFromParcel(Parcel in) {
            return new GroupNewBean(in);
        }

        @Override
        public GroupNewBean[] newArray(int size) {
            return new GroupNewBean[size];
        }
    };

    public String getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
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

    public String getGroupHead() {
        return groupHead;
    }

    public void setGroupHead(String groupHead) {
        this.groupHead = groupHead;
    }

    public String getFirstFailDate() {
        return firstFailDate;
    }

    public void setFirstFailDate(String firstFailDate) {
        this.firstFailDate = firstFailDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public int getLimitPersonCount() {
        return limitPersonCount;
    }

    public void setLimitPersonCount(int limitPersonCount) {
        this.limitPersonCount = limitPersonCount;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public int getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(int isFreeze) {
        this.isFreeze = isFreeze;
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

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
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
        dest.writeString(groupNotice);
        dest.writeInt(failCount);
        dest.writeInt(isSuper);
        dest.writeString(groupId);
        dest.writeString(groupHead);
        dest.writeString(firstFailDate);
        dest.writeString(sort);
        dest.writeString(ownerId);
        dest.writeString(addDate);
        dest.writeInt(limitPersonCount);
        dest.writeInt(personCount);
        dest.writeInt(isFreeze);
        dest.writeString(groupName);
        dest.writeInt(redPacketNum);
        dest.writeInt(totalNum);
        dest.writeInt(sendHour);
    }
}
