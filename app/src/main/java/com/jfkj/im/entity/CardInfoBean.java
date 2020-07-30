package com.jfkj.im.entity;

public class CardInfoBean {

    /**
     * code : 200
     * message : 查询成功
     * data : {"money":6000,"bankcard":"**** **** **** **** 4527","bankname":"中国邮政储蓄银行","informationType":1,"realname":"张**"}
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
         * money : 6000
         * bankcard : **** **** **** **** 4527
         * bankname : 中国邮政储蓄银行
         * informationType : 1
         * realname : 张**
         */

        private String money;
        private String bankcard;
        private String bankname;
        private int informationType;
        private String realname;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getBankcard() {
            return bankcard;
        }

        public void setBankcard(String bankcard) {
            this.bankcard = bankcard;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public int getInformationType() {
            return informationType;
        }

        public void setInformationType(int informationType) {
            this.informationType = informationType;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }
    }
}
