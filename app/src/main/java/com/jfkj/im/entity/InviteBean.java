package com.jfkj.im.entity;

public class InviteBean {


    /**
     * code : 200
     * message : 成功
     * data : {"diamonds":0,"inviteSize":0,"QRmobileNo":"18800000002"}
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


    @Override
    public String toString() {
        return "InviteBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * diamonds : 0.0
         * inviteSize : 0
         * QRmobileNo : 18800000002
         */


        private int diamonds;
        private String inviteSize;
        private String QRmobileNo;

        public int getDiamonds() {
            return diamonds;
        }

        public void setDiamonds(int diamonds) {
            this.diamonds = diamonds;
        }

        public String getInviteSize() {
            return inviteSize;
        }

        public void setInviteSize(String inviteSize) {
            this.inviteSize = inviteSize;
        }

        public String getQRmobileNo() {
            return QRmobileNo;
        }

        public void setQRmobileNo(String QRmobileNo) {
            this.QRmobileNo = QRmobileNo;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "diamonds=" + diamonds +
                    ", inviteSize='" + inviteSize + '\'' +
                    ", QRmobileNo='" + QRmobileNo + '\'' +
                    '}';
        }
    }
}
