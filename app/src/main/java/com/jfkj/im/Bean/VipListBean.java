package com.jfkj.im.Bean;

import java.util.List;

public class VipListBean {


    /**
     * code : 200
     * message : 成功
     * data : [{"iexchangemoney":498,"imoney":4980,"vipType":1,"irhz":"3","cexchangeid":"4","osname":"1"},{"iexchangemoney":1998,"imoney":19980,"vipType":2,"irhz":"3","cexchangeid":"5","osname":"1"},{"iexchangemoney":2998,"imoney":29980,"vipType":3,"irhz":"3","cexchangeid":"6","osname":"1"}]
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
         * iexchangemoney : 498.0
         * imoney : 4980.0
         * vipType : 1
         * irhz : 3
         * cexchangeid : 4
         * osname : 1
         */

        private int iexchangemoney;
        private int imoney;
        private int vipType;
        private String irhz;
        private String cexchangeid;
        private String osname;

        public int getIexchangemoney() {
            return iexchangemoney;
        }

        public void setIexchangemoney(int iexchangemoney) {
            this.iexchangemoney = iexchangemoney;
        }

        public int getImoney() {
            return imoney;
        }

        public void setImoney(int imoney) {
            this.imoney = imoney;
        }

        public int getVipType() {
            return vipType;
        }

        public void setVipType(int vipType) {
            this.vipType = vipType;
        }

        public String getIrhz() {
            return irhz;
        }

        public void setIrhz(String irhz) {
            this.irhz = irhz;
        }

        public String getCexchangeid() {
            return cexchangeid;
        }

        public void setCexchangeid(String cexchangeid) {
            this.cexchangeid = cexchangeid;
        }

        public String getOsname() {
            return osname;
        }

        public void setOsname(String osname) {
            this.osname = osname;
        }
    }
}
