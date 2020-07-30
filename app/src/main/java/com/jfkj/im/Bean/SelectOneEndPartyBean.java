package com.jfkj.im.Bean;

import java.util.List;

public class SelectOneEndPartyBean {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"title":"打麻将","partyId":"119667334968377344"}]
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
         * title : 打麻将
         * partyId : 119667334968377344
         */

        private String title;
        private String partyId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPartyId() {
            return partyId;
        }

        public void setPartyId(String partyId) {
            this.partyId = partyId;
        }
    }
}
