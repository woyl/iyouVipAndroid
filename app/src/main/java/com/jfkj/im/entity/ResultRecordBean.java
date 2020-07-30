package com.jfkj.im.entity;

import java.util.List;

public class ResultRecordBean {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"cashid":"1","money":"1","success":"1","cashdate":"1"},{"cashid":"1","money":"1","success":"1","cashdate":"1"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cashid : 1
         * money : 1
         * success : 1
         * cashdate : 1
         */

        private String cashid;
        private String money;
        private String success;
        private String cashdate;

        public String getCashid() {
            return cashid;
        }

        public void setCashid(String cashid) {
            this.cashid = cashid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getCashdate() {
            return cashdate;
        }

        public void setCashdate(String cashdate) {
            this.cashdate = cashdate;
        }
    }
}
