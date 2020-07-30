package com.jfkj.im.entity;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/24
 * </pre>
 */
public class ChargeRecordBean  {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"money":280,"balance":499661,"rmb":28,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 13:25:08"},{"money":500,"balance":500161,"rmb":50,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 13:25:48"},{"money":500,"balance":500661,"rmb":50,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 13:25:50"},{"money":1000,"balance":501661,"rmb":100,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:13:43"},{"money":1000000,"balance":1501661,"rmb":100000,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:16:01"},{"money":1000000,"balance":2501661,"rmb":100000,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:23:18"},{"money":1000,"balance":2502661,"rmb":100,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:24:39"},{"money":1000,"balance":2503661,"rmb":100,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:25:05"},{"money":1000000,"balance":3503661,"rmb":100000,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:27:20"},{"money":280,"balance":3503941,"rmb":28,"bizTypeName":"用户充值","userId":"","adddate":"2020-03-21 14:28:58"}]
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
         * money : 280.0
         * balance : 499661.0
         * rmb : 28.0
         * bizTypeName : 用户充值
         * userId :
         * adddate : 2020-03-21 13:25:08
         */

        private String money;
        private long balance;
        private String rmb;
        private String bizTypeName;
        private String userId;
        private String adddate;


        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        public String getRmb() {
            return rmb;
        }

        public void setRmb(String rmb) {
            this.rmb = rmb;
        }

        public String getBizTypeName() {
            return bizTypeName;
        }

        public void setBizTypeName(String bizTypeName) {
            this.bizTypeName = bizTypeName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }
    }
}
