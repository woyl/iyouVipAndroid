package com.jfkj.im;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class GroupMessagecenterBean implements MultiItemEntity {
    private int itemType = 0;
    WaitpassageBean waitpassageBean;

    public WaitpassageBean getWaitpassageBean() {
        return waitpassageBean;
    }

    public void setWaitpassageBean(WaitpassageBean waitpassageBean) {
        this.waitpassageBean = waitpassageBean;
    }

    public static class WaitpassageBean {
        private String head ="";
        private String vipLevel ="";
        private String nickname ="";
        private String groupName ="";
        private String sendId ="";
        private String receiveId ="";
        private String groupId = "";
        private String message = "";
        private String toMsgId = "";
        private String needAck = "";
        private String msgId = "";
        private String state = "";
        private String invite_name="";

        private boolean isReply=false;
        public String getInvite_name() {
            return invite_name;
        }

        public void setInvite_name(String invite_name) {
            this.invite_name = invite_name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getNeedAck() {
            return needAck;
        }

        public void setNeedAck(String needAck) {
            this.needAck = needAck;
        }

        public String getToMsgId() {
            return toMsgId;
        }

        public void setToMsgId(String toMsgId) {
            this.toMsgId = toMsgId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(String vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSendId() {
            return sendId;
        }

        public void setSendId(String sendId) {
            this.sendId = sendId;
        }

        public String getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(String receiveId) {
            this.receiveId = receiveId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
