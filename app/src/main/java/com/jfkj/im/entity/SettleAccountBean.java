package com.jfkj.im.entity;

public class SettleAccountBean {



    /**
     * code : 200
     * message : 查询成功
     * data : {"money":6000,"rate":30,"binding":1,"sum":0,"informationType":1}
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
         * rate : 30
         * binding : 1
         * sum : 0
         * informationType : 1
         */

        private String money;
        private String rate;
        private int binding;
        private int sum;
        private int informationType;


        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public int getBinding() {
            return binding;
        }

        public void setBinding(int binding) {
            this.binding = binding;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public int getInformationType() {
            return informationType;
        }

        public void setInformationType(int informationType) {
            this.informationType = informationType;
        }
    }
}
