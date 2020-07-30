package com.jfkj.im.Bean;

public class ClubmessageBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"groupOwnerName":"ggege","groupOwnerId":"76743604952891392","groupNotice":"111","city":"","PersonCount":2,"isSuper":0,"groupId":"90572075676336128","groupOwnerHead":"http://iyoufile.198ty.com/user/picture/20200109/4c906454b273e36e_s.jpg","isJoin":1,"vipLevel":3,"groupName":"111","redPacketNum":10,"totalNum":1,"isOwner":1}
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
         * groupOwnerName : ggege
         * groupOwnerId : 76743604952891392
         * groupNotice : 111
         * city :
         * PersonCount : 2
         * isSuper : 0
         * groupId : 90572075676336128
         * groupOwnerHead : http://iyoufile.198ty.com/user/picture/20200109/4c906454b273e36e_s.jpg
         * isJoin : 1
         * vipLevel : 3
         * groupName : 111
         * redPacketNum : 10
         * totalNum : 1
         * isOwner : 1
         */

        private String groupOwnerName;
        private String groupOwnerId;
        private String groupNotice;
        private String city;
        private int PersonCount;
        private int isSuper;
        private String groupId;
        private String groupOwnerHead;
        private int isJoin;
        private int vipLevel;
        private String groupName;
        private int redPacketNum;
        private int totalNum;
        private int isOwner;

        public String getGroupOwnerName() {
            return groupOwnerName;
        }

        public void setGroupOwnerName(String groupOwnerName) {
            this.groupOwnerName = groupOwnerName;
        }

        public String getGroupOwnerId() {
            return groupOwnerId;
        }

        public void setGroupOwnerId(String groupOwnerId) {
            this.groupOwnerId = groupOwnerId;
        }

        public String getGroupNotice() {
            return groupNotice;
        }

        public void setGroupNotice(String groupNotice) {
            this.groupNotice = groupNotice;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getPersonCount() {
            return PersonCount;
        }

        public void setPersonCount(int PersonCount) {
            this.PersonCount = PersonCount;
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

        public String getGroupOwnerHead() {
            return groupOwnerHead;
        }

        public void setGroupOwnerHead(String groupOwnerHead) {
            this.groupOwnerHead = groupOwnerHead;
        }

        public int getIsJoin() {
            return isJoin;
        }

        public void setIsJoin(int isJoin) {
            this.isJoin = isJoin;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
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

        public int getIsOwner() {
            return isOwner;
        }

        public void setIsOwner(int isOwner) {
            this.isOwner = isOwner;
        }
    }
}
