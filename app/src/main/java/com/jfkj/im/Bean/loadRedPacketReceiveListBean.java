package com.jfkj.im.Bean;

import java.util.List;

public class loadRedPacketReceiveListBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"bizType":202,"sendId":"76743604952891392","groupId":"90572075676336128","bizTypeName":"俱乐部红包","sort":"1578451429501","addDate":"2020-01-08 10:43:49","userId":"76743604952891392","vipLevel":3,"userHead":"http://iyoufile.198ty.com/user/picture/20191227/7520d10027bb58ae_s.jpg","money":1,"bestLuck":1,"redId":"91978302532747264","state":1,"userNickName":"ggege","recId":"91978329158189056"}]}
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
             * bizType : 202
             * sendId : 76743604952891392
             * groupId : 90572075676336128
             * bizTypeName : 俱乐部红包
             * sort : 1578451429501
             * addDate : 2020-01-08 10:43:49
             * userId : 76743604952891392
             * vipLevel : 3
             * userHead : http://iyoufile.198ty.com/user/picture/20191227/7520d10027bb58ae_s.jpg
             * money : 1.0
             * bestLuck : 1
             * redId : 91978302532747264
             * state : 1
             * userNickName : ggege
             * recId : 91978329158189056
             */

            private int bizType;
            private String sendId;
            private String groupId;
            private String bizTypeName;
            private String sort;
            private String addDate;
            private String userId;
            private int vipLevel;
            private String userHead;
            private Integer money;
            private int bestLuck;
            private String redId;
            private int state;
            private String userNickName;
            private String recId;

            public int getBizType() {
                return bizType;
            }

            public void setBizType(int bizType) {
                this.bizType = bizType;
            }

            public String getSendId() {
                return sendId;
            }

            public void setSendId(String sendId) {
                this.sendId = sendId;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public String getBizTypeName() {
                return bizTypeName;
            }

            public void setBizTypeName(String bizTypeName) {
                this.bizTypeName = bizTypeName;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getAddDate() {
                return addDate;
            }

            public void setAddDate(String addDate) {
                this.addDate = addDate;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getVipLevel() {
                return vipLevel;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            public String getUserHead() {
                return userHead;
            }

            public void setUserHead(String userHead) {
                this.userHead = userHead;
            }

            public Integer getMoney() {
                return money;
            }

            public void setMoney(Integer money) {
                this.money = money;
            }

            public int getBestLuck() {
                return bestLuck;
            }

            public void setBestLuck(int bestLuck) {
                this.bestLuck = bestLuck;
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

            public String getUserNickName() {
                return userNickName;
            }

            public void setUserNickName(String userNickName) {
                this.userNickName = userNickName;
            }

            public String getRecId() {
                return recId;
            }

            public void setRecId(String recId) {
                this.recId = recId;
            }
        }
    }
}
