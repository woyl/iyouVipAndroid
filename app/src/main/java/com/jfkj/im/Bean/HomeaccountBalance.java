package com.jfkj.im.Bean;

public class HomeaccountBalance {


    /**
     * code : 200
     * message : 成功
     * data : {"jewelincome":0,"jewel":7.0001937E7,"jewelCost":-1937,"upgradeAmount":400}
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
         * jewelincome : 0.0
         * jewel : 7.0001937E7
         * jewelCost : -1937.0
         * upgradeAmount : 400.0
         */

        private double jewelincome;
        private double jewel;
        private double jewelCost;
        private double upgradeAmount;

        public double getJewelincome() {
            return jewelincome;
        }

        public void setJewelincome(double jewelincome) {
            this.jewelincome = jewelincome;
        }

        public double getJewel() {
            return jewel;
        }

        public void setJewel(double jewel) {
            this.jewel = jewel;
        }

        public double getJewelCost() {
            return jewelCost;
        }

        public void setJewelCost(double jewelCost) {
            this.jewelCost = jewelCost;
        }

        public double getUpgradeAmount() {
            return upgradeAmount;
        }

        public void setUpgradeAmount(double upgradeAmount) {
            this.upgradeAmount = upgradeAmount;
        }
    }
}
