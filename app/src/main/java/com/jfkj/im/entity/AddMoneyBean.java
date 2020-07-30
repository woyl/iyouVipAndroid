package com.jfkj.im.entity;

import com.google.gson.annotations.SerializedName;

public class AddMoneyBean {


    /**
     * code : 200
     * message : 订单生成成功
     * data : {"package":"Sign=WXPay","preInfo":"","appid":"wx4daa19e679a154de","sign":"9530EE480D8FEB57F358182F7535A78A","paymentMethod":"1","partnerid":"1588261411","prepayid":"1588261411","noncestr":"L9J0iu3GNat3X2HN","tradeType":"APP","timestamp":"1589265114"}
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
         * package : Sign=WXPay
         * preInfo :
         * appid : wx4daa19e679a154de
         * sign : 9530EE480D8FEB57F358182F7535A78A
         * paymentMethod : 1
         * partnerid : 1588261411
         * prepayid : 1588261411
         * noncestr : L9J0iu3GNat3X2HN
         * tradeType : APP
         * timestamp : 1589265114
         */

        @SerializedName("package")
        private String packageX;
        private String preInfo;
        private String appid;
        private String sign;
        private String paymentMethod;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String tradeType;
        private String timestamp;

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPreInfo() {
            return preInfo;
        }

        public void setPreInfo(String preInfo) {
            this.preInfo = preInfo;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
