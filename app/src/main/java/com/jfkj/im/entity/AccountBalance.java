package com.jfkj.im.entity;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/27
 * </pre>
 */
public class AccountBalance   {

    /**
     * code : 200
     * message : 成功
     * data : {"jewel":"499395","goldCoin":"100","goldIncome":1000,"jewelCost":1235,"upgradeAmount":42000}
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
         * jewel : 499395
         * goldCoin : 100
         * goldIncome : 1000
         * jewelCost : 1235
         * upgradeAmount : 42000
         */

        private String jewel;
        private String goldCoin;
        private String goldIncome;
        private String jewelCost;
        private String upgradeAmount;

        public String getJewel() {
            return jewel;
        }

        public void setJewel(String jewel) {
            this.jewel = jewel;
        }

        public String getGoldCoin() {

            //goldCoin=32467.330000000001746229827404022216796875
                if(goldCoin.contains(".")){

                    try {
                        String[] split = goldCoin.split("\\.");
                        return split[0];
                    }catch (Exception e){
                        return "";
                    }
                }else{
                    return goldCoin;
                }
        }

        public void setGoldCoin(String goldCoin) {
            this.goldCoin = goldCoin;
        }

        public String getGoldIncome() {

            try {
                return new Double(Double.parseDouble(goldIncome)).intValue() +"";
            }catch (Exception e){
                return goldIncome;
            }
        }

        public void setGoldIncome(String goldIncome) {
            this.goldIncome = goldIncome;
        }

        public String getJewelCost() {
            try {
                return new Double(Double.parseDouble(jewelCost)).intValue() +"";
            }catch (Exception e){
                return jewelCost;
            }
        }

        public void setJewelCost(String jewelCost) {
            this.jewelCost = jewelCost;
        }

        public String getUpgradeAmount() {
            return upgradeAmount;
        }

        public void setUpgradeAmount(String upgradeAmount) {
            this.upgradeAmount = upgradeAmount;
        }
    }
}
