package com.jfkj.im.Bean;

public class ChatFriendBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"money":59,"orderId":"92083982992277504","msgId":"202001081743390335d2ac771c462da23606296d3ced2d","tradeOrderNo":"DL1022020010892083982996471808"}
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
         * money : 59.0
         * orderId : 92083982992277504
         * msgId : 202001081743390335d2ac771c462da23606296d3ced2d
         * tradeOrderNo : DL1022020010892083982996471808
         */

        private double money;
        private String orderId;
        private String msgId;
        private String tradeOrderNo;

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getTradeOrderNo() {
            return tradeOrderNo;
        }

        public void setTradeOrderNo(String tradeOrderNo) {
            this.tradeOrderNo = tradeOrderNo;
        }
    }
}
