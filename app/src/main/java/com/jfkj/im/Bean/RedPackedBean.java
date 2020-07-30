package com.jfkj.im.Bean;

public class RedPackedBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"bizType":202,"tradeOrder":"HQ2022020010691343999721406464","groupId":"90572075676336128","redState":1,"bizTypeName":"俱乐部红包","msgId":"20200106164313b74bdc26194b4aa78f8e83572565af88","packetSize":1,"restMoney":"1.00","userId":"76743604952891392","addDate":"2020-01-06 16:43:13","sendWord":"dsfa","money":"1.00","restSize":1,"redId":"91343999717212160","state":1}
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
         * bizType : 202
         * tradeOrder : HQ2022020010691343999721406464
         * groupId : 90572075676336128
         * redState : 1
         * bizTypeName : 俱乐部红包
         * msgId : 20200106164313b74bdc26194b4aa78f8e83572565af88
         * packetSize : 1
         * restMoney : 1.00
         * userId : 76743604952891392
         * addDate : 2020-01-06 16:43:13
         * sendWord : dsfa
         * money : 1.00
         * restSize : 1
         * redId : 91343999717212160
         * state : 1
         */

        private int bizType;
        private String tradeOrder;
        private String groupId;
        private int redState;
        private String bizTypeName;
        private String msgId;
        private int packetSize;
        private String restMoney;
        private String userId;
        private String addDate;
        private String sendWord;
        private String money;
        private int restSize;
        private String redId;
        private int state;

        public int getBizType() {
            return bizType;
        }

        public void setBizType(int bizType) {
            this.bizType = bizType;
        }

        public String getTradeOrder() {
            return tradeOrder;
        }

        public void setTradeOrder(String tradeOrder) {
            this.tradeOrder = tradeOrder;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public int getRedState() {
            return redState;
        }

        public void setRedState(int redState) {
            this.redState = redState;
        }

        public String getBizTypeName() {
            return bizTypeName;
        }

        public void setBizTypeName(String bizTypeName) {
            this.bizTypeName = bizTypeName;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public int getPacketSize() {
            return packetSize;
        }

        public void setPacketSize(int packetSize) {
            this.packetSize = packetSize;
        }

        public String getRestMoney() {
            return restMoney;
        }

        public void setRestMoney(String restMoney) {
            this.restMoney = restMoney;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAddDate() {
            return addDate;
        }

        public void setAddDate(String addDate) {
            this.addDate = addDate;
        }

        public String getSendWord() {
            return sendWord;
        }

        public void setSendWord(String sendWord) {
            this.sendWord = sendWord;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getRestSize() {
            return restSize;
        }

        public void setRestSize(int restSize) {
            this.restSize = restSize;
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
    }
}
