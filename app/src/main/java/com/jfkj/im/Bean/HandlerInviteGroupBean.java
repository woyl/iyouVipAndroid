package com.jfkj.im.Bean;

public class HandlerInviteGroupBean {

    /**
     * code : 200
     * message : 操作成功
     * data : {"owner":{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","nickName":"测试号_13700000093","userId":"76751335294238720"},"group":{"groupName":".222222","redPacketNum":10,"groupNotice":".222","groupId":"89418003229310976","groupHead":"http://iyoufile.198ty.com/group/20200102/e0aa7410d1d7c766.jpg","ownerId":"76751335294238720","addDate":"2020-01-01 09:10:00","limitPersonCount":60,"personCount":2}}
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
         * owner : {"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","nickName":"测试号_13700000093","userId":"76751335294238720"}
         * group : {"groupName":".222222","redPacketNum":10,"groupNotice":".222","groupId":"89418003229310976","groupHead":"http://iyoufile.198ty.com/group/20200102/e0aa7410d1d7c766.jpg","ownerId":"76751335294238720","addDate":"2020-01-01 09:10:00","limitPersonCount":60,"personCount":2}
         */

        private OwnerBean owner;
        private GroupBean group;

        public OwnerBean getOwner() {
            return owner;
        }

        public void setOwner(OwnerBean owner) {
            this.owner = owner;
        }

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public static class OwnerBean {
            /**
             * head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
             * nickName : 测试号_13700000093
             * userId : 76751335294238720
             */

            private String head;
            private String nickName;
            private String userId;

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

        public static class GroupBean {
            /**
             * groupName : .222222
             * redPacketNum : 10
             * groupNotice : .222
             * groupId : 89418003229310976
             * groupHead : http://iyoufile.198ty.com/group/20200102/e0aa7410d1d7c766.jpg
             * ownerId : 76751335294238720
             * addDate : 2020-01-01 09:10:00
             * limitPersonCount : 60
             * personCount : 2
             */

            private String groupName;
            private int redPacketNum;
            private String groupNotice;
            private String groupId;
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
