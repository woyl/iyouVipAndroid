package com.jfkj.im.Bean;

public class LoadRedPacketDetailsBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"redType":1,"userHead":"http://iyoufile.198ty.com/user/picture/20191227/7520d10027bb58ae_s.jpg","money":1,"isReceive":1,"userNickName":"ggege","packetSize":1,"receiveSize":1,"state":2,"sendWord":"SFADS","receiveMoney":1}
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
         * redType : 1
         * userHead : http://iyoufile.198ty.com/user/picture/20191227/7520d10027bb58ae_s.jpg
         * money : 1.0
         * isReceive : 1
         * userNickName : ggege
         * packetSize : 1
         * receiveSize : 1
         * state : 2
         * sendWord : SFADS
         * receiveMoney : 1.0
         */

        private int redType;
        private String userHead;
        private Integer money;
        private int isReceive;
        private String userNickName;
        private int packetSize;
        private int receiveSize;
        private int state;
        private String sendWord;
        private double receiveMoney;

        public int getRedType() {
            return redType;
        }

        public void setRedType(int redType) {
            this.redType = redType;
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

        public int getIsReceive() {
            return isReceive;
        }

        public void setIsReceive(int isReceive) {
            this.isReceive = isReceive;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public int getPacketSize() {
            return packetSize;
        }

        public void setPacketSize(int packetSize) {
            this.packetSize = packetSize;
        }

        public int getReceiveSize() {
            return receiveSize;
        }

        public void setReceiveSize(int receiveSize) {
            this.receiveSize = receiveSize;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getSendWord() {
            return sendWord;
        }

        public void setSendWord(String sendWord) {
            this.sendWord = sendWord;
        }

        public double getReceiveMoney() {
            return receiveMoney;
        }

        public void setReceiveMoney(double receiveMoney) {
            this.receiveMoney = receiveMoney;
        }
    }
}
