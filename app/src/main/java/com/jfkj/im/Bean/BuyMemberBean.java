package com.jfkj.im.Bean;

public class BuyMemberBean {

    /**
     * code : 200
     * message : 充值订单回调完成
     * data : {"vipLevel":50,"showDesc":"隐藏地理位置功能已解锁","isShow":0}
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
         * vipLevel : 50
         * showDesc : 隐藏地理位置功能已解锁
         * isShow : 0
         */

        private int vipLevel;
        private String showDesc;
        private int isShow;

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getShowDesc() {
            return showDesc;
        }

        public void setShowDesc(String showDesc) {
            this.showDesc = showDesc;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }
}
