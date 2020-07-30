package com.jfkj.im.Bean;

public class AddFriendBean {



    /**
     * code : 200
     * message : 操作成功
     * data : {"giftId":"93891594675421184","money":"20","orderId":"121848213484470272","msgId":"2020033020560463dd3194596e4c649dac8000b9926586","tradeOrderNo":"JP10120200330121848213643853824","userId":"119893769985327104","giftSize":1,"content":"我是测试聊天1，很高兴遇见你，交个朋友吧"}
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
         * giftId : 93891594675421184
         * money : 20
         * orderId : 121848213484470272
         * msgId : 2020033020560463dd3194596e4c649dac8000b9926586
         * tradeOrderNo : JP10120200330121848213643853824
         * userId : 119893769985327104
         * giftSize : 1
         * content : 我是测试聊天1，很高兴遇见你，交个朋友吧
         */

        private String giftId;
        private String money;
        private String orderId;
        private String msgId;
        private String tradeOrderNo;
        private String userId;
        private int giftSize;
        private String content;

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getGiftSize() {
            return giftSize;
        }

        public void setGiftSize(int giftSize) {
            this.giftSize = giftSize;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
