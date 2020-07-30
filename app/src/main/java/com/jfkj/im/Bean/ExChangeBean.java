package com.jfkj.im.Bean;

import java.util.List;

/**
 * <pre>
 * Description:  兑换
 * @author :   ys
 * @date :         2020/1/16
 * </pre>
 */
public class ExChangeBean{


    /**
     * code : 200
     * message : 成功
     * data : [{"iexchangemoney":100,"imoney":70,"irhz":"2","cexchangeid":"9","osname":"1"},
     * {"iexchangemoney":300,"imoney":210,"irhz":"2","cexchangeid":"10","osname":"1"},
     * {"iexchangemoney":500,"imoney":350,"irhz":"2","cexchangeid":"11","osname":"1"},{"iexchangemoney":1000,"imoney":700,"irhz":"2","cexchangeid":"12","osname":"1"},{"iexchangemoney":3000,"imoney":2100,"irhz":"2","cexchangeid":"13","osname":"1"}]
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
         * iexchangemoney : 100
         * imoney : 70
         * irhz : 2
         * cexchangeid : 9
         * osname : 1
         */

        private int iexchangemoney;
        private int imoney;
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
