package com.jfkj.im.Bean;

import java.util.List;

public class GroupBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"groupNotice":"nnn","failCount":0,"isSuper":0,"groupId":"86619743972360192",
     * "groupHead":"http://iyoufile.198ty.com/group/20191224/f017185deb5f6873.jpg","firstFailDate":"",
     * "sort":"1577173843235","ownerId":"76751334233079808","topTime":0,"addDate":"2019-12-24 15:50:43",
     * "limitPersonCount":60,"personCount":2,"isFreeze":0,"groupName":"bbb","redPacketNum":10,
     * "top":0,"isOwner":0,"totalNum":1,"noDisturb":0,"sendHour":0,"receiveNum":0},
     * {"groupNotice":"Fffjj","failCount":0,"isSuper":0,"groupId":"84044210679971840","groupHead":"http://iyoufile.198ty.com/group/20191225/0d60978bfcabc434.jpg","firstFailDate":"","sort":"1576559788247","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-17 13:16:28","limitPersonCount":60,"personCount":2,"isFreeze":0,"groupName":"Yyyg","redPacketNum":10,"top":0,"isOwner":1,"totalNum":2,"noDisturb":1,"sendHour":0,"receiveNum":0},{"groupNotice":"Ggg","failCount":0,"isSuper":0,"groupId":"82898599498874880","groupHead":"http://iyoufile.198ty.com/group/20191217/3c968ef4659337ec.jpg","firstFailDate":"","sort":"1576286653257","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-14 09:24:13","limitPersonCount":60,"personCount":4,"isFreeze":0,"groupName":"Fffd","redPacketNum":10,"top":0,"isOwner":1,"totalNum":3,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Hhhh","failCount":0,"isSuper":0,"groupId":"81830636029083648","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","firstFailDate":"","sort":"1576032030924","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 10:40:30","limitPersonCount":60,"personCount":1,"isFreeze":0,"groupName":"Jjxxs","redPacketNum":10,"top":0,"isOwner":1,"totalNum":1,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Vvv","failCount":0,"isSuper":0,"groupId":"81829981809934336","groupHead":"http://iyoufile.198ty.com/group/20191227/a4105ca633e305c2.jpg","firstFailDate":"","sort":"1576031874946","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 10:37:54","limitPersonCount":60,"personCount":2,"isFreeze":0,"groupName":"Tds","redPacketNum":10,"top":0,"isOwner":1,"totalNum":2,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Toff","failCount":0,"isSuper":0,"groupId":"81826818952396800","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","firstFailDate":"","sort":"1576031120862","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 10:25:20","limitPersonCount":60,"personCount":1,"isFreeze":0,"groupName":"Gggggg","redPacketNum":10,"top":0,"isOwner":1,"totalNum":1,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Ttt","failCount":0,"isSuper":0,"groupId":"81824977476780032","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","firstFailDate":"","sort":"1576030681820","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 10:18:01","limitPersonCount":60,"personCount":1,"isFreeze":0,"groupName":"Vvv","redPacketNum":10,"top":0,"isOwner":1,"totalNum":1,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Vvv","failCount":0,"isSuper":0,"groupId":"81824046060273664","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","firstFailDate":"","sort":"1576030459753","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 10:14:19","limitPersonCount":60,"personCount":1,"isFreeze":0,"groupName":"Bbc","redPacketNum":10,"top":0,"isOwner":1,"totalNum":1,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Yyyy","failCount":0,"isSuper":0,"groupId":"81822554741932032","groupHead":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","firstFailDate":"","sort":"1576030104195","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 10:08:24","limitPersonCount":60,"personCount":1,"isFreeze":0,"groupName":"vivo","redPacketNum":10,"top":0,"isOwner":1,"totalNum":1,"noDisturb":0,"sendHour":0,"receiveNum":0},{"groupNotice":"Vvvvoo","failCount":0,"isSuper":0,"groupId":"81812510277238784","groupHead":"http://iyoufile.198ty.com/group/20191227/c3fc7fe8f28a2287.jpg","firstFailDate":"","sort":"1576027709408","ownerId":"76743604952891392","topTime":0,"addDate":"2019-12-11 09:28:29","limitPersonCount":60,"personCount":2,"isFreeze":0,"groupName":"Ggg","redPacketNum":10,"top":0,"isOwner":1,"totalNum":2,"noDisturb":0,"sendHour":0,"receiveNum":0}]}
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
        private List<ArrayBean> array;

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

        public static class ArrayBean {
            /**
             * groupNotice : nnn
             * failCount : 0
             * isSuper : 0
             * groupId : 86619743972360192
             * groupHead : http://iyoufile.198ty.com/group/20191224/f017185deb5f6873.jpg
             * firstFailDate :
             * sort : 1577173843235
             * ownerId : 76751334233079808
             * topTime : 0
             * addDate : 2019-12-24 15:50:43
             * limitPersonCount : 60
             * personCount : 2
             * isFreeze : 0
             * groupName : bbb
             * redPacketNum : 10
             * top : 0
             * isOwner : 0
             * totalNum : 1
             * noDisturb : 0
             * sendHour : 0
             * receiveNum : 0
             */

            private String groupNotice;
            private int failCount;
            private int isSuper;
            private long groupId;
            private String groupHead;
            private String firstFailDate;
            private String sort;
            private String ownerId;
            private int topTime;
            private String addDate;
            private int limitPersonCount;
            private int personCount;
            private int isFreeze;
            private String groupName;
            private int redPacketNum;
            private int top;
            private int isOwner;
            private int totalNum;
            private int noDisturb;
            private int sendHour;
            private int receiveNum;

            public String getGroupNotice() {
                return groupNotice;
            }

            public void setGroupNotice(String groupNotice) {
                this.groupNotice = groupNotice;
            }

            public int getFailCount() {
                return failCount;
            }

            public void setFailCount(int failCount) {
                this.failCount = failCount;
            }

            public int getIsSuper() {
                return isSuper;
            }

            public void setIsSuper(int isSuper) {
                this.isSuper = isSuper;
            }

            public long getGroupId() {
                return groupId;
            }

            public void setGroupId(long groupId) {
                this.groupId = groupId;
            }

            public String getGroupHead() {
                return groupHead;
            }

            public void setGroupHead(String groupHead) {
                this.groupHead = groupHead;
            }

            public String getFirstFailDate() {
                return firstFailDate;
            }

            public void setFirstFailDate(String firstFailDate) {
                this.firstFailDate = firstFailDate;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getOwnerId() {
                return ownerId;
            }

            public void setOwnerId(String ownerId) {
                this.ownerId = ownerId;
            }

            public int getTopTime() {
                return topTime;
            }

            public void setTopTime(int topTime) {
                this.topTime = topTime;
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

            public int getIsFreeze() {
                return isFreeze;
            }

            public void setIsFreeze(int isFreeze) {
                this.isFreeze = isFreeze;
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

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getIsOwner() {
                return isOwner;
            }

            public void setIsOwner(int isOwner) {
                this.isOwner = isOwner;
            }

            public int getTotalNum() {
                return totalNum;
            }

            public void setTotalNum(int totalNum) {
                this.totalNum = totalNum;
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

            public int getReceiveNum() {
                return receiveNum;
            }

            public void setReceiveNum(int receiveNum) {
                this.receiveNum = receiveNum;
            }
        }
    }
}
