package com.jfkj.im.Bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class GroupcenterBean implements MultiItemEntity {
    private WaitpassageBean waitpassageBean;
    private int itemType = 0;

    public GroupcenterBean() {
    }

    public GroupcenterBean(WaitpassageBean waitpassageBean) {
        this.waitpassageBean = waitpassageBean;
    }

    public WaitpassageBean getWaitpassageBean() {
        return waitpassageBean;
    }

    public void setWaitpassageBean(WaitpassageBean waitpassageBean) {
        this.waitpassageBean = waitpassageBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public static class WaitpassageBean {
        private String masterhead = "";
        private String master = "";
        private String visitor = "";
        private String groupName = "";
        private String masterId = "";
        private String visitorId = "";
        private String groupId = "";
        private String message = "";
        private String state = "";

        private String toMsgId = "";
        private String needAck = "";
        private String msgId = "";

        private String chat1="";
        private String chat2="";
        private String chat3="";

        public String getChat1() {
            return chat1;
        }

        public void setChat1(String chat1) {
            this.chat1 = chat1;
        }

        public String getChat2() {
            return chat2;
        }

        public void setChat2(String chat2) {
            this.chat2 = chat2;
        }

        public String getChat3() {
            return chat3;
        }

        public void setChat3(String chat3) {
            this.chat3 = chat3;
        }

        public WaitpassageBean(String masterhead, String master, String visitor, String groupName, String masterId, String visitorId, String groupId, String message, String state, String toMsgId, String needAck, String msgId) {
            this.masterhead = masterhead;
            this.master = master;
            this.visitor = visitor;
            this.groupName = groupName;
            this.masterId = masterId;
            this.visitorId = visitorId;
            this.groupId = groupId;
            this.message = message;
            this.state = state;
            this.toMsgId = toMsgId;
            this.needAck = needAck;
            this.msgId = msgId;
        }

        public WaitpassageBean() {
        }

        public String getToMsgId() {
            return toMsgId;
        }

        public void setToMsgId(String toMsgId) {
            this.toMsgId = toMsgId;
        }

        public String getNeedAck() {
            return needAck;
        }

        public void setNeedAck(String needAck) {
            this.needAck = needAck;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMasterhead() {
            return masterhead;
        }

        public void setMasterhead(String masterhead) {
            this.masterhead = masterhead;
        }

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public String getVisitor() {
            return visitor;
        }

        public void setVisitor(String visitor) {
            this.visitor = visitor;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getMasterId() {
            return masterId;
        }

        public void setMasterId(String masterId) {
            this.masterId = masterId;
        }

        public String getVisitorId() {
            return visitorId;
        }

        public void setVisitorId(String visitorId) {
            this.visitorId = visitorId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }


}
