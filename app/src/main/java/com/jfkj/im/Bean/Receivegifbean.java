package com.jfkj.im.Bean;

public class Receivegifbean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"giftId":"93892373318598656","money":"19.00","orderId":"117079640467701760","msgId":"202003171707289c2114dba0324b0d9065722c855fde60","giftSize":"1","tips":"对方领取了您的礼物"}
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
         * giftId : 93892373318598656
         * money : 19.00
         * orderId : 117079640467701760
         * msgId : 202003171707289c2114dba0324b0d9065722c855fde60
         * giftSize : 1
         * tips : 对方领取了您的礼物
         */

        private String giftId;
        private String money;
        private String orderId;
        private String msgId;
        private String giftSize;
        private String tips;

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

        public String getGiftSize() {
            return giftSize;
        }

        public void setGiftSize(String giftSize) {
            this.giftSize = giftSize;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }
}
