package com.jfkj.im.entity;

/**
 * <pre>
 * Description:  设置一些状态值
 * @author :   ys
 * @date :         2019/12/6
 * </pre>
 */
public class Setup {

    /**
     * code : 200
     * message : 成功
     * data : {"HideOnline":0,"VipLevel":70,"HideLocation":0}
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
         * HideOnline : 0
         * VipLevel : 70
         * HideLocation : 0
         */

        private int HideOnline;
        private int VipLevel;
        private int HideLocation;
        private String Mobile;
        private String Password;

        public String getMobile(){

            return Mobile;
        }

        public void setMobile(String mobile){
            this.Mobile = mobile;
        }


        public String getPassword(){
            return Password;
        }

        public void setPassword(String password){
            this.Password = password;
        }



        public int getHideOnline() {
            return HideOnline;
        }

        public void setHideOnline(int HideOnline) {
            this.HideOnline = HideOnline;
        }

        public int getVipLevel() {
            return VipLevel;
        }

        public void setVipLevel(int VipLevel) {
            this.VipLevel = VipLevel;
        }

        public int getHideLocation() {
            return HideLocation;
        }

        public void setHideLocation(int HideLocation) {
            this.HideLocation = HideLocation;
        }
    }
}
