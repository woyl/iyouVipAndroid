package com.jfkj.im.Bean;

public class LoginSuccessBean {



    /**
     * code : 200
     * message : 成功
     * data : {"gender":2,"AVChatRoomId":"@TGS# ","userSig":"eJw9js0KgkAURt9ltoXc0bnjKLSoRVSUi7TSdoZjXfqb1MqI3j3RaPmdw4HvzaJ5aOnaUKGZL0EogH7LHrpgPrMtYN0us2NqDGXM5wLAVh53ZWco05eKcmoDzgUK9MAVjq24lOLf077RdbpZLWg7eq2HPIkxv5tgfAqdnUwOgTOrbr3ndTkpTDyNysEvrOjcXOOoEAUolJ8v02AzIQ__","isNewUser":0,"userId":"114545907432816640","token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTQ1NDU5MDc0MzI4MTY2NDAiLCJtb2JpbGVObyI6IjE4ODAwMDAwMDAyIiwiZXhwIjoxNTg2MTQ1NjU2LCJ1c2VySWQiOjExNDU0NTkwNzQzMjgxNjY0MCwiaWF0IjoxNTg1NTQwODU2LCJqdGkiOiIzNDM0NDkwNC1iZjQwLTQ1NTEtOTE3NS02YmMxZWZjN2Q5MTkifQ.uPXnjsRU6udGBUYDPy3zDM3DRBVfCJamkmZ7sDwVf0k","seqRoom":22}
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
         * gender : 2
         * AVChatRoomId : @TGS#3SM5K5KGH
         * userSig : eJw9js0KgkAURt9ltoXc0bnjKLSoRVSUi7TSdoZjXfqb1MqI3j3RaPmdw4HvzaJ5aOnaUKGZL0EogH7LHrpgPrMtYN0us2NqDGXM5wLAVh53ZWco05eKcmoDzgUK9MAVjq24lOLf077RdbpZLWg7eq2HPIkxv5tgfAqdnUwOgTOrbr3ndTkpTDyNysEvrOjcXOOoEAUolJ8v02AzIQ__
         * isNewUser : 0
         * userId : 114545907432816640
         * token : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTQ1NDU5MDc0MzI4MTY2NDAiLCJtb2JpbGVObyI6IjE4ODAwMDAwMDAyIiwiZXhwIjoxNTg2MTQ1NjU2LCJ1c2VySWQiOjExNDU0NTkwNzQzMjgxNjY0MCwiaWF0IjoxNTg1NTQwODU2LCJqdGkiOiIzNDM0NDkwNC1iZjQwLTQ1NTEtOTE3NS02YmMxZWZjN2Q5MTkifQ.uPXnjsRU6udGBUYDPy3zDM3DRBVfCJamkmZ7sDwVf0k
         * seqRoom : 22
         */

        private int gender;
        private String AVChatRoomId;
        private String userSig;
        private int isNewUser;
        private String userId;
        private String token;
        private int seqRoom;

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getAVChatRoomId() {
            return AVChatRoomId;
        }

        public void setAVChatRoomId(String AVChatRoomId) {
            this.AVChatRoomId = AVChatRoomId;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getSeqRoom() {
            return seqRoom;
        }

        public void setSeqRoom(int seqRoom) {
            this.seqRoom = seqRoom;
        }
    }
}
