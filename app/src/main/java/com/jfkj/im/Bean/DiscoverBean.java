package com.jfkj.im.Bean;

import java.util.List;

public class DiscoverBean {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"adverturl":"www.baidu.com","top":0,"advertremark":"","title":"测试","advertid":"118563405723729920","url":"http://iyoufile.198ty.com/admin/20200317/20200317_1584426282234.png"}]
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
         * adverturl : www.baidu.com
         * top : 0
         * advertremark :
         * title : 测试
         * advertid : 118563405723729920
         * url : http://iyoufile.198ty.com/admin/20200317/20200317_1584426282234.png
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
