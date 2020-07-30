package com.jfkj.im.entity;

public class inviteFriendAddressBean {


    /**
     * code : 200
     * message : 成功
     * data : {"address":"http://iyapi.tiantiancaidian.com/tatah?userid=114545907432816640"}
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
         * address : http://iyapi.tiantiancaidian.com/tatah?userid=114545907432816640
         */

        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
