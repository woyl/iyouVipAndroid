package com.jfkj.im.Bean;

public class AddGroupBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"msgId":"20191223154525f44d27f81d3a41b3ab6fbb50e1eee303","content":"1555","group":{"groupName":"测试群：13700000043","redPacketNum":10,"groupNotice":"测试群公告","groupId":"76751247041888256","sendHour":8,"groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","ownerId":"76751245318029312","addDate":"2019-11-27 10:16:50","limitPersonCount":60,"personCount":22}}
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
         * msgId : 20191223154525f44d27f81d3a41b3ab6fbb50e1eee303
         * content : 1555
         * group : {"groupName":"测试群：13700000043","redPacketNum":10,"groupNotice":"测试群公告","groupId":"76751247041888256","sendHour":8,"groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","ownerId":"76751245318029312","addDate":"2019-11-27 10:16:50","limitPersonCount":60,"personCount":22}
         */

        private String msgId;
        private String content;
        private GroupBean group;

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public static class GroupBean {
            /**
             * groupName : 测试群：13700000043
             * redPacketNum : 10
             * groupNotice : 测试群公告
             * groupId : 76751247041888256
             * sendHour : 8
             * groupHead : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
             * ownerId : 76751245318029312
             * addDate : 2019-11-27 10:16:50
             * limitPersonCount : 60
             * personCount : 22
             */

            private String groupName;
            private int redPacketNum;
            private String groupNotice;
            private String groupId;
            private int sendHour;
            private String groupHead;
            private String ownerId;
            private String addDate;
            private int limitPersonCount;
            private int personCount;

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

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
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
        }
    }
}
