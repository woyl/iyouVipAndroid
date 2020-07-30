package com.jfkj.im.Bean;

public class LoginBean {


    /**
     * code : 200
     * message : 成功
     * data : {"gender":1,"examine":1,"EXPIRETIME":"604800","SDKAPPID":"1400290969","userid":"76743604952891392","token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3Njc0MzYwNDk1Mjg5MTM5MiIsImdlbmRlciI6MSwibW9iaWxlTm8iOiIxMzcwMDAwMDAwMSIsImV4cCI6MTU4MzI0OTE5OSwidXNlcklkIjo3Njc0MzYwNDk1Mjg5MTM5MiwiaWF0IjoxNTgyNjQ0Mzk5LCJqdGkiOiI5OTRkZDIyMy0wYmE1LTQ0N2YtYmM4Ni00MDZlZDY4OGZhZTYifQ.FGUsWXbOOjIhC9IOPAahEi9W829YZJKAMUhtn6Brys8"}
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
         * gender : 1
         * examine : 1
         * EXPIRETIME : 604800
         * SDKAPPID : 1400290969
         * userid : 76743604952891392
         * token : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3Njc0MzYwNDk1Mjg5MTM5MiIsImdlbmRlciI6MSwibW9iaWxlTm8iOiIxMzcwMDAwMDAwMSIsImV4cCI6MTU4MzI0OTE5OSwidXNlcklkIjo3Njc0MzYwNDk1Mjg5MTM5MiwiaWF0IjoxNTgyNjQ0Mzk5LCJqdGkiOiI5OTRkZDIyMy0wYmE1LTQ0N2YtYmM4Ni00MDZlZDY4OGZhZTYifQ.FGUsWXbOOjIhC9IOPAahEi9W829YZJKAMUhtn6Brys8
         */
        private String circleRoomId;
        private int gender;
        private int examine;
        private String EXPIRETIME;
        private String SDKAPPID;
        private String userid;
        private String token;
        private String userSig;
        private int isNewUser;
        private int isRegister;
        private int seqRoom;
        private String AVChatRoomId;






        public String getCircleRoomId() {
            return circleRoomId;
        }

        public void setCircleRoomId(String circleRoomId) {
            this.circleRoomId = circleRoomId;
        }

        public int getSeqRoom() {
            return seqRoom;
        }

        public void setSeqRoom(int seqRoom) {
            this.seqRoom = seqRoom;
        }

        public String getUserSig() {
            return userSig;
        }

        public void setUserSig(String userSig) {
            this.userSig = userSig;
        }

        public int getIsNewUser() {
            return isNewUser;
        }

        public void setIsNewUser(int isNewUser) {
            this.isNewUser = isNewUser;
        }

        public int getIsRegister() {
            return isRegister;
        }

        public void setIsRegister(int isRegister) {
            this.isRegister = isRegister;
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

        public String getEXPIRETIME() {
            return EXPIRETIME;
        }

        public void setEXPIRETIME(String EXPIRETIME) {
            this.EXPIRETIME = EXPIRETIME;
        }

        public String getSDKAPPID() {
            return SDKAPPID;
        }

        public void setSDKAPPID(String SDKAPPID) {
            this.SDKAPPID = SDKAPPID;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAVChatRoomId() {
            return AVChatRoomId;
        }

        public void setAVChatRoomId(String AVChatRoomId) {
            this.AVChatRoomId = AVChatRoomId;
        }
    }
}
