package com.jfkj.im.Bean;

import java.util.List;

public class UserDetailBean {


    /**
     * code : 200
     * message : 查询成功
     * data : {"birthday":"2001-03-10","occupation":"产品","gender":2,"hideLocation":0,"signature":"阿里巴巴","industry":"影视娱乐行业","userid":"114545907432816640","head":"http://iyfile.tiantiancaidian.com/user/picture/20200312/1dcac3004aa62e90_s.jpg","vipLevel":50,"constellation":"双鱼座","school":"澳门大学","smoking":"偶尔抽烟","isFriend":2,"Height":153,"userNickName":"成都第一帅","vipCard":0,"likeCuisine":"鲁菜","headState":1,"homeVideo":"http://iyfile.tiantiancaidian.com/user/video/20200311/14924f9fc6fc6960.mp4","drinkwine":"偶尔喝酒","hometown":"河北省石家庄市","educational":"本科","activeTime":"刚刚","weight":38,"onLine":1,"hideOnline":0,"videoImage":"mp4","position":"深圳","videoState":1,"age":20}
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
         * birthday : 2001-03-10
         * occupation : 产品
         * gender : 2
         * hideLocation : 0
         * signature : 阿里巴巴
         * industry : 影视娱乐行业
         * userid : 114545907432816640
         * head : http://iyfile.tiantiancaidian.com/user/picture/20200312/1dcac3004aa62e90_s.jpg
         * vipLevel : 50
         * constellation : 双鱼座
         * school : 澳门大学
         * smoking : 偶尔抽烟
         * isFriend : 2
         * Height : 153
         * userNickName : 成都第一帅
         * vipCard : 0
         * likeCuisine : 鲁菜
         * headState : 1
         * homeVideo : http://iyfile.tiantiancaidian.com/user/video/20200311/14924f9fc6fc6960.mp4
         * drinkwine : 偶尔喝酒
         * hometown : 河北省石家庄市
         * educational : 本科
         * activeTime : 刚刚
         * weight : 38
         * onLine : 1
         * hideOnline : 0
         * videoImage : mp4
         * position : 深圳
         * videoState : 1
         * age : 20
         */

        private String birthday;
        private String occupation;
        private int gender;
        private int hideLocation;
        private String signature;
        private String industry;
        private String userid;
        private String head;
        private int vipLevel;
        private String constellation;
        private String school;
        private String smoking;
        private int isFriend;
        private int Height;
        private String userNickName;
        private int vipCard;
        private String likeCuisine;
        private int headState;
        private String homeVideo;
        private String drinkwine;
        private String hometown;
        private String educational;
        private String activeTime;
        private int weight;
        private int onLine;
        private int hideOnline;
        private String videoImage;
        private String position;
        private int videoState;
        private int age;
        private List<TaskList> taskList;
        private String taskCount;

        public String getTaskCount() {
            return taskCount;
        }

        public void setTaskCount(String taskCount) {
            this.taskCount = taskCount;
        }

        public List<TaskList> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<TaskList> taskList) {
            this.taskList = taskList;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getHideLocation() {
            return hideLocation;
        }

        public void setHideLocation(int hideLocation) {
            this.hideLocation = hideLocation;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getSmoking() {
            return smoking;
        }

        public void setSmoking(String smoking) {
            this.smoking = smoking;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }

        public int getHeight() {
            return Height;
        }

        public void setHeight(int Height) {
            this.Height = Height;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public int getVipCard() {
            return vipCard;
        }

        public void setVipCard(int vipCard) {
            this.vipCard = vipCard;
        }

        public String getLikeCuisine() {
            return likeCuisine;
        }

        public void setLikeCuisine(String likeCuisine) {
            this.likeCuisine = likeCuisine;
        }

        public int getHeadState() {
            return headState;
        }

        public void setHeadState(int headState) {
            this.headState = headState;
        }

        public String getHomeVideo() {
            return homeVideo;
        }

        public void setHomeVideo(String homeVideo) {
            this.homeVideo = homeVideo;
        }

        public String getDrinkwine() {
            return drinkwine;
        }

        public void setDrinkwine(String drinkwine) {
            this.drinkwine = drinkwine;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public String getEducational() {
            return educational;
        }

        public void setEducational(String educational) {
            this.educational = educational;
        }

        public String getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(String activeTime) {
            this.activeTime = activeTime;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getOnLine() {
            return onLine;
        }

        public void setOnLine(int onLine) {
            this.onLine = onLine;
        }

        public int getHideOnline() {
            return hideOnline;
        }

        public void setHideOnline(int hideOnline) {
            this.hideOnline = hideOnline;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getVideoState() {
            return videoState;
        }

        public void setVideoState(int videoState) {
            this.videoState = videoState;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class TaskList{

        /**
         * itaskid : 128194766528708608
         * cuserid : 115886939227422720
         * itasktype : 1
         * ctaskcontent : 分享一张和小动物的合照
         * irewardamount : 100
         * cadddate : 2020-04-17 09:15:01
         * itype : 1
         */

        private String itaskid;
        private String cuserid;
        private String itasktype;
        private String ctaskcontent;
        private Integer irewardamount;
        private String cadddate;
        private String itype;

        public String getItaskid() {
            return itaskid;
        }

        public void setItaskid(String itaskid) {
            this.itaskid = itaskid;
        }

        public String getCuserid() {
            return cuserid;
        }

        public void setCuserid(String cuserid) {
            this.cuserid = cuserid;
        }

        public String getItasktype() {
            return itasktype;
        }

        public void setItasktype(String itasktype) {
            this.itasktype = itasktype;
        }

        public String getCtaskcontent() {
            return ctaskcontent;
        }

        public void setCtaskcontent(String ctaskcontent) {
            this.ctaskcontent = ctaskcontent;
        }

        public String getIrewardamount() {
            return irewardamount  + "";
        }

        public void setIrewardamount(Integer irewardamount) {
            this.irewardamount = irewardamount;
        }

        public String getCadddate() {
            return cadddate;
        }

        public void setCadddate(String cadddate) {
            this.cadddate = cadddate;
        }

        public String getItype() {
            return itype;
        }

        public void setItype(String itype) {
            this.itype = itype;
        }
    }
}
