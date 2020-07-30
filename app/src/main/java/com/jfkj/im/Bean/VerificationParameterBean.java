package com.jfkj.im.Bean;

public class VerificationParameterBean {


    /**
     * code : 200
     * message : 查询成功
     * data : {"yzHead":0,"yzGender":0,"yzBirthday":0,"examine":0,"yzNickName":0,"yzPassword":0}
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
        private int vipLevel;
        private int gender;
        private int examine;
        private int optState;
        private int vipCard;
        private String userId;


        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getExamine() {
            return examine;
        }

        public void setExamine(int examine) {
            this.examine = examine;
        }

        public int getOptState() {
            return optState;
        }

        public void setOptState(int optState) {
            this.optState = optState;
        }

        public int getVipCard() {
            return vipCard;
        }

        public void setVipCard(int vipCard) {
            this.vipCard = vipCard;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
