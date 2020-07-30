package com.jfkj.im.Bean;

import java.util.List;

public class SelectadvertisementBean {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"adverturl":"22342342342","top":0,"advertremark":"","title":"234234","advertid":"107913930869178368","url":"http://iyoufile.tiantiancaidian.com/admin/20200221/20200221_1582250767618.png"}]
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
         * adverturl : 22342342342
         * top : 0
         * advertremark :
         * title : 234234
         * advertid : 107913930869178368
         * url : http://iyoufile.tiantiancaidian.com/admin/20200221/20200221_1582250767618.png
         */

        private String adverturl;
        private int top;
        private String advertremark;
        private String title;
        private String advertid;
        private String url;

        public String getAdverturl() {
            return adverturl;
        }

        public void setAdverturl(String adverturl) {
            this.adverturl = adverturl;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public String getAdvertremark() {
            return advertremark;
        }

        public void setAdvertremark(String advertremark) {
            this.advertremark = advertremark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAdvertid() {
            return advertid;
        }

        public void setAdvertid(String advertid) {
            this.advertid = advertid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
