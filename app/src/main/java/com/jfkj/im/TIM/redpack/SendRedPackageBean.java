package com.jfkj.im.TIM.redpack;

import android.os.Parcel;
import android.os.Parcelable;

public class SendRedPackageBean implements Parcelable {

    /**
     * bizType : 202
     * tradeOrder : HQ20220200308113862755903209472
     * groupId : 113828577002258432
     * redState : 1
     * bizTypeName : 俱乐部红包
     * msgId : 202003082004436c078ed4695f417ea76acefbdbd89a2b
     * packetSize : 1
     * restMoney : 13.00
     * userId : 113051629334429696
     * addDate : 2020-03-08 20:04:43
     * sendWord : 恭喜发财，大吉大利！
     * money : 13.00
     * restSize : 1
     * redId : 113862755752214528
     * state : 1
     */
    /**
     * {
     * "code": "200",... <string>
     * "message": "操作成功",... <string>
     * -"data": {...<object>
     * "bizType": 202,交易编码 <number>
     * "tradeOrder": "HQ2022020010691337679475703808",红包订单 <string>
     * "groupId": "76749494825254912",群ID <string>
     * "redState": 1,红包状态 1 正常领取 2 已领取完 3过期退款 <number>
     * "bizTypeName": "俱乐部红包",交易类型名称 <string>
     * "msgId": "20200106161806bb1edadc8dfe4eb29581d0ba8da7b2fc",消息ID <string>
     * "packetSize": 10,总大小 <number>
     * "restMoney": "100.00",剩余红包金额 <string>
     * "userId": "76749491763412992",发送人ID <string>
     * "addDate": "2020-01-06 16:18:06",时间 <string>
     * "sendWord": "快来抢红包啊",备注 <string>
     * "money": "100.00",总金额 <string>
     * "restSize": 10,剩余红包大小 <number>
     * "redId": "91337679471509504",红包ID <string>
     * "state": 1订单状态 0 未支付 1 已支付 <number>
     * }
     * */

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
    private String money;
    private int restSize;
    private String redId;
    private int state;

    public SendRedPackageBean() {
    }

    protected SendRedPackageBean(Parcel in) {
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
        money = in.readString();
        restSize = in.readInt();
        redId = in.readString();
        state = in.readInt();
    }

    public static final Creator<SendRedPackageBean> CREATOR = new Creator<SendRedPackageBean>() {
        @Override
        public SendRedPackageBean createFromParcel(Parcel in) {
            return new SendRedPackageBean(in);
        }

        @Override
        public SendRedPackageBean[] newArray(int size) {
            return new SendRedPackageBean[size];
        }
    };

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
        dest.writeString(money);
        dest.writeInt(restSize);
        dest.writeString(redId);
        dest.writeInt(state);
    }
}
