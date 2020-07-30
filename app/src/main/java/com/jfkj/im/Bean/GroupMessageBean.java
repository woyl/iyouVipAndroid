package com.jfkj.im.Bean;

public class GroupMessageBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"owner":"","user":{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","nickName":"测试号_13700000093","userId":"76751335294238720"},"group":{"groupName":"vivo","redPacketNum":10,"groupNotice":"Yyyy","totalNum":1,"groupId":"81822554741932032","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","ownerId":"76743604952891392","addDate":"2019-12-11 10:08:24","limitPersonCount":60,"personCount":1}}
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
         * owner :
         * user : {"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","nickName":"测试号_13700000093","userId":"76751335294238720"}
         * group : {"groupName":"vivo","redPacketNum":10,"groupNotice":"Yyyy","totalNum":1,"groupId":"81822554741932032","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","ownerId":"76743604952891392","addDate":"2019-12-11 10:08:24","limitPersonCount":60,"personCount":1}
         */

        private String owner;
        private UserBean user;
        private GroupBean group;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public static class UserBean {

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
             * groupName : vivo
             * redPacketNum : 10
             * groupNotice : Yyyy
             * totalNum : 1
             * groupId : 81822554741932032
             * groupHead : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
             * ownerId : 76743604952891392
             * addDate : 2019-12-11 10:08:24
             * limitPersonCount : 60
             * personCount : 1
             */

            private String groupName;
            private int redPacketNum;
            private String groupNotice;
            private int totalNum;
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

            public int getTotalNum() {
                return totalNum;
            }

            public void setTotalNum(int totalNum) {
                this.totalNum = totalNum;
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
