package com.jfkj.im.Bean;

import java.util.List;

public class CzModeListBean {


    /**
     * code : 10003
     * message : 查询成功
     * data : [{"ilimit":"20","cbankid":"1","iwayid":"1","iway":"支付宝充值1","cimgurl":"http://img4.imgtn.bdimg.com/it/u=3208234695,2734381037&fm=26&gp=0.jpg","cmemo":"蚂蚁金服支付宝充值1","imaxilimit":"3000"},{"ilimit":"0","cbankid":"3","iwayid":"3","iway":"微信充值3","cimgurl":"/static/image/bank/bankcz/zfbpay.png","cmemo":"微信支付充值","imaxilimit":"1000"},{"ilimit":"0","cbankid":"3","iwayid":"2","iway":"微信充值2","cimgurl":"/static/image/bank/bankcz/zfbpay.png","cmemo":"微信支付充值","imaxilimit":"1000"},{"ilimit":"10","cbankid":"1","iwayid":"7","iway":"支付宝充值7","cimgurl":"/static/image/bank/bankcz/ylpay.png","cmemo":"蚂蚁金服支付宝充值","imaxilimit":"3000"}]
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
         * ilimit : 20
         * cbankid : 1
         * iwayid : 1
         * iway : 支付宝充值1
         * cimgurl : http://img4.imgtn.bdimg.com/it/u=3208234695,2734381037&fm=26&gp=0.jpg
         * cmemo : 蚂蚁金服支付宝充值1
         * imaxilimit : 3000
         */

        private String ilimit;
        private String cbankid;
        private String iwayid;
        private String iway;
        private String cimgurl;
        private String cmemo;
        private String imaxilimit;
        public boolean isselected=false;

        public boolean isIsselected() {
            return isselected;
        }

        public void setIsselected(boolean isselected) {
            this.isselected = isselected;
        }

        public String getIlimit() {
            return ilimit;
        }

        public void setIlimit(String ilimit) {
            this.ilimit = ilimit;
        }

        public String getCbankid() {
            return cbankid;
        }

        public void setCbankid(String cbankid) {
            this.cbankid = cbankid;
        }

        public String getIwayid() {
            return iwayid;
        }

        public void setIwayid(String iwayid) {
            this.iwayid = iwayid;
        }

        public String getIway() {
            return iway;
        }

        public void setIway(String iway) {
            this.iway = iway;
        }

        public String getCimgurl() {
            return cimgurl;
        }

        public void setCimgurl(String cimgurl) {
            this.cimgurl = cimgurl;
        }

        public String getCmemo() {
            return cmemo;
        }

        public void setCmemo(String cmemo) {
            this.cmemo = cmemo;
        }

        public String getImaxilimit() {
            return imaxilimit;
        }

        public void setImaxilimit(String imaxilimit) {
            this.imaxilimit = imaxilimit;
        }
    }
}
