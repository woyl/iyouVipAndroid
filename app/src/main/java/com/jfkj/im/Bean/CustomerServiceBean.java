package com.jfkj.im.Bean;

public class CustomerServiceBean {


    /**
     * code : 200
     * message : 查询成功
     * data : {"url":"https://v1.live800.com/live800/chatClient/chatbox.jsp?companyID=840156&configID=43851&jid=3261337167&s=1"}
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
         * url : https://v1.live800.com/live800/chatClient/chatbox.jsp?companyID=840156&configID=43851&jid=3261337167&s=1
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
