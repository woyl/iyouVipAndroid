package com.jfkj.im.entity;

public class UpdatePhotoBean
{


    /**
     * code : 200
     * message : 提交成功
     * data : {"headUrl":"http://iyfile.tiantiancaidian.com/user/picture/20200313/c8988702ff03681c_s.jpg","headState":1}
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
         * headUrl : http://iyfile.tiantiancaidian.com/user/picture/20200313/c8988702ff03681c_s.jpg
         * headState : 1
         */

        private String headUrl;
        private int headState;

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getHeadState() {
            return headState;
        }

        public void setHeadState(int headState) {
            this.headState = headState;
        }
    }
}
