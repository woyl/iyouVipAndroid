package com.jfkj.im.TIM.bean;



public class Gifbean {

    /**
     * status : 0
     * body : {"orderId":"117081293556154368","gifimgurl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","tradeOrderNo":"DL10220200317117081293681983488","giftId":"93892373318598656"}
     * type : 0
     * sendType : 1
     */
    private String status;
    private  BodyBean body;
    private String type;
    private String sendType;
   private String money;
   private boolean receive;
   private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public  BodyBean getBody() {
        return body;
    }

    public void setBody( BodyBean body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    //    public boolean isReceive() {
//
//
//    }
//
//    public void setReceive(boolean b) {
//    }

    public static class BodyBean {
        /**
         * orderId : 117081293556154368
         * gifimgurl : http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png
         * tradeOrderNo : DL10220200317117081293681983488
         * giftId : 93892373318598656
         */
        private String orderId;
        private String gifimgurl;
        private String tradeOrderNo;
        private String giftId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getGifimgurl() {
            return gifimgurl;
        }

        public void setGifimgurl(String gifimgurl) {
            this.gifimgurl = gifimgurl;
        }

        public String getTradeOrderNo() {
            return tradeOrderNo;
        }

        public void setTradeOrderNo(String tradeOrderNo) {
            this.tradeOrderNo = tradeOrderNo;
        }

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }
    }


}
