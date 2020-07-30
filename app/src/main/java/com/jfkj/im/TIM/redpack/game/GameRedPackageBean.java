package com.jfkj.im.TIM.redpack.game;

import android.os.Parcel;
import android.os.Parcelable;

public class GameRedPackageBean implements Parcelable {

    /**
     * gameType : 2
     * bizType : 203
     * tradeOrder : MXYX20320200311114984008500248576
     * groupId : 114613609191047168
     * redState : 1
     * bizTypeName : 冒险游戏
     * msgId : 20200311222010987fea8d463146ed8f02a8f7ce462e2c
     * packetSize : 1
     * restMoney : 23.00
     * userId : 113057798249644032
     * addDate : 2020-03-11 22:20:10
     * sendWord : 我的,哈哈,
     * content : 我的,哈哈,
     * money : 23.00
     * restSize : 1
     * redId : 114984008361836544
     * state : 1
     */

    private int gameType;
    private int bizType;
    private String tradeOrder;
    private String groupId;
    private int redState;
    private String bizTypeName;
    private String msgId;
    private int packetSize;
    private String restMoney;
    private String userId;
    private String addDate;
    private String sendWord;
    private String content;
    private String money;
    private int restSize;
    private String redId;
    private int state;

    public GameRedPackageBean() {
    }

    protected GameRedPackageBean(Parcel in) {
        gameType = in.readInt();
        bizType = in.readInt();
        tradeOrder = in.readString();
        groupId = in.readString();
        redState = in.readInt();
        bizTypeName = in.readString();
        msgId = in.readString();
        packetSize = in.readInt();
        restMoney = in.readString();
        userId = in.readString();
        addDate = in.readString();
        sendWord = in.readString();
        content = in.readString();
        money = in.readString();
        restSize = in.readInt();
        redId = in.readString();
        state = in.readInt();
    }

    public static final Creator<GameRedPackageBean> CREATOR = new Creator<GameRedPackageBean>() {
        @Override
        public GameRedPackageBean createFromParcel(Parcel in) {
            return new GameRedPackageBean(in);
        }

        @Override
        public GameRedPackageBean[] newArray(int size) {
            return new GameRedPackageBean[size];
        }
    };

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getTradeOrder() {
        return tradeOrder;
    }

    public void setTradeOrder(String tradeOrder) {
        this.tradeOrder = tradeOrder;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getRedState() {
        return redState;
    }

    public void setRedState(int redState) {
        this.redState = redState;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public String getRestMoney() {
        return restMoney;
    }

    public void setRestMoney(String restMoney) {
        this.restMoney = restMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getSendWord() {
        return sendWord;
    }

    public void setSendWord(String sendWord) {
        this.sendWord = sendWord;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getRestSize() {
        return restSize;
    }

    public void setRestSize(int restSize) {
        this.restSize = restSize;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameType);
        dest.writeInt(bizType);
        dest.writeString(tradeOrder);
        dest.writeString(groupId);
        dest.writeInt(redState);
        dest.writeString(bizTypeName);
        dest.writeString(msgId);
        dest.writeInt(packetSize);
        dest.writeString(restMoney);
        dest.writeString(userId);
        dest.writeString(addDate);
        dest.writeString(sendWord);
        dest.writeString(content);
        dest.writeString(money);
        dest.writeInt(restSize);
        dest.writeString(redId);
        dest.writeInt(state);
    }
}
