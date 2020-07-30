package com.jfkj.im.Bean;

public class AllMyPartysRedBean {

    /**
     * error_code : 0
     * data : {"redType":"1"}
     */

    private int error_code;
    private DataBean data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * redType : 1
         */

        private String redType;

        public String getRedType() {
            return redType;
        }

        public void setRedType(String redType) {
            this.redType = redType;
        }
    }
}
