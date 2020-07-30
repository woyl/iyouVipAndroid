package com.jfkj.im.entity;

import java.util.List;

public class InvteFriendBean {


    /**
     * code : 200
     * message : 成功
     * data : [{"exchangeid":"114545907","inviteSize":1,"QRmobileNo":"18800000002","isgive":1,"mobileNo":"15511112222","userid":"114545907432816640","adddate":"2020-03-12 13:39:21"},{"exchangeid":"114545907","inviteSize":1,"QRmobileNo":"18800000002","isgive":1,"mobileNo":"15511112222","userid":"114545907432816640","adddate":"2020-03-12 13:39:21"}]
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
         * exchangeid : 114545907
         * inviteSize : 1
         * QRmobileNo : 18800000002
         * isgive : 1
         * mobileNo : 15511112222
         * userid : 114545907432816640
         * adddate : 2020-03-12 13:39:21
         */

        private String exchangeid;
        private int inviteSize;
        private String QRmobileNo;
        private int isgive;
        private String mobileNo;
        private String userid;
        private String adddate;

        public String getExchangeid() {
            return exchangeid;
        }

        public void setExchangeid(String exchangeid) {
            this.exchangeid = exchangeid;
        }

        public int getInviteSize() {
            return inviteSize;
        }

        public void setInviteSize(int inviteSize) {
            this.inviteSize = inviteSize;
        }

        public String getQRmobileNo() {
            return QRmobileNo;
        }

        public void setQRmobileNo(String QRmobileNo) {
            this.QRmobileNo = QRmobileNo;
        }

        public int getIsgive() {
            return isgive;
        }

        public void setIsgive(int isgive) {
            this.isgive = isgive;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }
    }
}
