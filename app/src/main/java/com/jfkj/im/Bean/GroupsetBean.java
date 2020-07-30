package com.jfkj.im.Bean;

public class GroupsetBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"groupName":"Yyyg","redPacketNum":10,"groupNotice":"Fffjj","isOwner":1,"isSuper":0,"groupId":"84044210679971840","noDisturb":0,"sendHour":0,"groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png"}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * groupName : Yyyg
         * redPacketNum : 10
         * groupNotice : Fffjj
         * isOwner : 1
         * isSuper : 0
         * groupId : 84044210679971840
         * noDisturb : 0
         * sendHour : 0
         * groupHead : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
         */

        private String groupName;
        private int redPacketNum;
        private String groupNotice;
        private int isOwner;
        private int isSuper;
        private String groupId;
        private int noDisturb;
        private int sendHour;
        private String groupHead;

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

        public String getGroupNotice() {
            return groupNotice;
        }

        public void setGroupNotice(String groupNotice) {
            this.groupNotice = groupNotice;
        }

        public int getIsOwner() {
            return isOwner;
        }

        public void setIsOwner(int isOwner) {
            this.isOwner = isOwner;
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

        public int getNoDisturb() {
            return noDisturb;
        }

        public void setNoDisturb(int noDisturb) {
            this.noDisturb = noDisturb;
        }

        public int getSendHour() {
            return sendHour;
        }

        public void setSendHour(int sendHour) {
            this.sendHour = sendHour;
        }

        public String getGroupHead() {
            return groupHead;
        }

        public void setGroupHead(String groupHead) {
            this.groupHead = groupHead;
        }
    }
}
