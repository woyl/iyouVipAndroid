package com.jfkj.im.entity;

public class UserPraiseBean {


    /**
     * code : 200
     * message : 点赞成功！
     * data : {"praiseid":"117122093254049792"}
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
         * praiseid : 117122093254049792
         */

        private String praiseid;

        public String getPraiseid() {
            return praiseid;
        }

        public void setPraiseid(String praiseid) {
            this.praiseid = praiseid;
        }
    }
}
