package com.jfkj.im.entity;

public class CarryScaleBean {

    /**
     * code : 200
     * message : 操作成功
     * data : {"expiryDate":1,"baseRate":30,"rate":31,"shareDynamics":1,"beginTime":"2020-03-16T21:00:00","endTime":"2020-03-17T21:00:00","personalVideo":0,"female":0,"addState":1,"commentDynamics":0,"userId":"114545907432816640","male":0}
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
         * expiryDate : 1
         * baseRate : 30.0
         * rate : 31.0
         * shareDynamics : 1.0
         * beginTime : 2020-03-16T21:00:00
         * endTime : 2020-03-17T21:00:00
         * personalVideo : 0.0
         * female : 0.0
         * addState : 1
         * commentDynamics : 0.0
         * userId : 114545907432816640
         * male : 0.0
         */

        private int expiryDate;
        private double baseRate;
        private double rate;
        private double shareDynamics;
        private String beginTime;
        private String endTime;
        private double personalVideo;
        private double female;
        private int addState;
        private double commentDynamics;
        private String userId;
        private double male;
        private int isCompleted;


        public int getIsCompleted() {
            return isCompleted;
        }

        public void setIsCompleted(int isCompleted) {
            this.isCompleted = isCompleted;
        }

        public int getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(int expiryDate) {
            this.expiryDate = expiryDate;
        }

        public double getBaseRate() {
            return baseRate;
        }

        public void setBaseRate(double baseRate) {
            this.baseRate = baseRate;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getShareDynamics() {
            return shareDynamics;
        }

        public void setShareDynamics(double shareDynamics) {
            this.shareDynamics = shareDynamics;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getPersonalVideo() {
            return personalVideo;
        }

        public void setPersonalVideo(double personalVideo) {
            this.personalVideo = personalVideo;
        }

        public double getFemale() {
            return female;
        }

        public void setFemale(double female) {
            this.female = female;
        }

        public int getAddState() {
            return addState;
        }

        public void setAddState(int addState) {
            this.addState = addState;
        }

        public double getCommentDynamics() {
            return commentDynamics;
        }

        public void setCommentDynamics(double commentDynamics) {
            this.commentDynamics = commentDynamics;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getMale() {
            return male;
        }

        public void setMale(double male) {
            this.male = male;
        }
    }
}
