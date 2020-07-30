package com.jfkj.im.Bean;

public class PartyInfoBean {


    /**
     * code : 200
     * message : 成功
     * data : {"head":"http://iyfile.tiantiancaidian.com/static/VipHead.png","vipLevel":11,"diamonds":10000,"maxNumber":5,"nickName":"🐚🐚🐚","introduce":"新增的聚会信息","signCount":0,"cadddate":"2020-04-05 12:11:36","place":"B茶馆","title":"打麻将","welfare":1,"userId":"119667334968377344","type":1}
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
         * head : http://iyfile.tiantiancaidian.com/static/VipHead.png
         * vipLevel : 11
         * diamonds : 10000
         * maxNumber : 5
         * nickName : 🐚🐚🐚
         * introduce : 新增的聚会信息
         * signCount : 0
         * cadddate : 2020-04-05 12:11:36
         * place : B茶馆
         * title : 打麻将
         * welfare : 1
         * userId : 119667334968377344
         * type : 1
         */

        private String head;
        private int vipLevel;
        private int diamonds;
        private int maxNumber;
        private String nickName;
        private String introduce;
        private int signCount;
        private String cadddate;
        private String place;
        private String title;
        private int welfare;
        private String userId;
        private int type;
        private String detailsphoto;
        private String stringDate;


        public String getStringDate() {
            return stringDate;
        }

        public void setStringDate(String stringDate) {
            this.stringDate = stringDate;
        }

        public String getDetailsphoto() {

            return detailsphoto;
        }

        public void setDetailsphoto(String detailsphoto) {
            this.detailsphoto = detailsphoto;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public int getDiamonds() {
            return diamonds;
        }

        public void setDiamonds(int diamonds) {
            this.diamonds = diamonds;
        }

        public int getMaxNumber() {
            return maxNumber;
        }

        public void setMaxNumber(int maxNumber) {
            this.maxNumber = maxNumber;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public int getSignCount() {
            return signCount;
        }

        public void setSignCount(int signCount) {
            this.signCount = signCount;
        }

        public String getCadddate() {
            return cadddate;
        }

        public void setCadddate(String cadddate) {
            this.cadddate = cadddate;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getWelfare() {
            return welfare;
        }

        public void setWelfare(int welfare) {
            this.welfare = welfare;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
