package com.jfkj.im.TIM.redpack;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class RedPackageCustom implements MultiItemEntity {
    private String sendId;//发送者id
    private String avatarUrl;//信息发送者头像 url
    private String sendName;//信息发送者昵称
    private String sendContent;
    private String groupId;
    private String groupName;
    private String VIP;//发送者VIP等级
    private String msgType;//消息类型，1单聊，2群组，这里固定为2，辅助判断
    private long date;
    private boolean isReceive;
    /**
     * 1.群红包
     * 2.冒险游戏红包
     * 3.冒险游戏提交的任务
     * 4.半透明tips提示：
     * */
    private String cusType;
    private String redId;
    private String redDesc;
    private String taskUrl;
    private String taskImage;

    /**
     * （1.群红包领取，2.群红包领完，3.领取冒险游戏红包参与提示，
     * 4.冒险游戏红包领取完成需做任务，5.冒险游戏任务未提交，
     * 6.俱乐部聊天冻结，7.新用户加入俱乐部涉黄暴力提示（入群后自动发送一条，仅发送者自己可看到))
     * */
    private String tipsType;
    private String text;
    private String[] receiveId;
    private String receiveName;
    private String receiveHeadUrl;
    private String receiveContent;
    private String redSendId;
    private String redIsDone;

    /**
     * intHandledStatus=0  intOperationType=0 intPendencyType=0没同意，因为没有拒绝
     * intHandledStatus=2  intOperationType=1 intPendencyType=0同意了，
     * intHandledStatus=2  intOperationType=0 intPendencyType=0 群组拒绝了
     * */
    private long intOperationType;
    private long intHandledStatus;
    private long intPendencyType;
    private String sendType;
    private String gameUserId;

    private String orderId;

    //广场测试
    private String type;
    private String cadddate;

    public String getCadddate() {
        return cadddate;
    }

    public void setCadddate(String cadddate) {
        this.cadddate = cadddate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getVIP() {
        return VIP;
    }

    public void setVIP(String VIP) {
        this.VIP = VIP;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getTipsType() {
        return tipsType;
    }

    public void setTipsType(String tipsTtype) {
        this.tipsType = tipsTtype;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getItemType() {
        return Integer.parseInt(cusType);
    }

    public String[] getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String[] receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveHeadUrl() {
        return receiveHeadUrl;
    }

    public void setReceiveHeadUrl(String receiveHeadUrl) {
        this.receiveHeadUrl = receiveHeadUrl;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public void setReceive(boolean receive) {
        isReceive = receive;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getReceiveContent() {
        return receiveContent;
    }

    public void setReceiveContent(String receiveContent) {
        this.receiveContent = receiveContent;
    }

    public long getIntOperationType() {
        return intOperationType;
    }

    public void setIntOperationType(long intOperationType) {
        this.intOperationType = intOperationType;
    }

    public long getIntHandledStatus() {
        return intHandledStatus;
    }

    public void setIntHandledStatus(long intHandledStatus) {
        this.intHandledStatus = intHandledStatus;
    }

    public long getIntPendencyType() {
        return intPendencyType;
    }

    public void setIntPendencyType(long intPendencyType) {
        this.intPendencyType = intPendencyType;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public String getRedDesc() {
        return redDesc;
    }

    public void setRedDesc(String redDesc) {
        this.redDesc = redDesc;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRedSendId() {
        return redSendId;
    }

    public void setRedSendId(String redSendId) {
        this.redSendId = redSendId;
    }

    public String getRedIsDone() {
        return redIsDone;
    }

    public void setRedIsDone(String redIsDone) {
        this.redIsDone = redIsDone;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(String gameUserId) {
        this.gameUserId = gameUserId;
    }
}
