package com.jfkj.im.entity;

import android.os.Parcel;

import com.jfkj.im.Bean.UserDetailBean;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public class UserDetail extends BaseResponse {

    /**
     * data : {"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","birthday":"1998-08-08","homeVideo":"","hometown":"","gender":1,"constellation":"","signature":"","videoImage":"","weight":"","Height":"","userNickName":"安然测试号_13700000104","userid":76751353593987072}
     */

    private DataBean data;

    protected UserDetail(Parcel in) {
        super(in);
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * birthday : 1998-08-08
         * homeVideo :
         * drinkwine :
         * hometown :
         * occupation :
         * gender : 1
         * signature :
         * hideLocation : 0
         * weight :
         * industry :
         * head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
         * vipLevel : 3
         * constellation : 巨蟹座
         * school :
         * smoking :
         * hideOnline : 0
         * videoImage :
         * isFriend : 2
         * Height :
         * userNickName : 测试号_13700000026
         * likeCuisine :
         */

        private String position;
        private String birthday;
        private String homeVideo;
        private String drinkwine;
        private String hometown;
        private String occupation;
        private int gender;
        private String signature;
        private int hideLocation;
        private String weight;
        private String industry;
        private String head;
        private String vipLevel;
        private String constellation;
        private String school;
        private String smoking;
        private int hideOnline;
        private String videoImage;
        private int isFriend;
        private String Height;
        private String userNickName;
        private String likeCuisine;
        private String age;
        private String educational;
        private String onLine;  // 0在线 1 不在线
        private String activeTime;  //活跃时间
        private String vipCard;
        private List<UserDetailBean.TaskList> taskList;

        public List<UserDetailBean.TaskList> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<UserDetailBean.TaskList> taskList) {
            this.taskList = taskList;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getVipCard() {
            return vipCard;
        }

        public void setVipCard(String vipCard) {
            this.vipCard = vipCard;
        }

        public String getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(String activeTime) {
            this.activeTime = activeTime;
        }

        public String getOnLine() {
            return onLine;
        }

        public void setOnLine(String onLine) {
            this.onLine = onLine;
        }

        public String getEducational() {
            return educational;
        }

        public void setEducational(String educational) {
            this.educational = educational;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public boolean isFriend(){
            return 1 == isFriend;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getHideLocation() {
            return hideLocation;
        }

        public void setHideLocation(int hideLocation) {
            this.hideLocation = hideLocation;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(String vipLevel) {
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

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }

        public String getHeight() {
            return Height;
        }

        public void setHeight(String Height) {
            this.Height = Height;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getLikeCuisine() {
            return likeCuisine;
        }

        public void setLikeCuisine(String likeCuisine) {
            this.likeCuisine = likeCuisine;
        }
    }
}
