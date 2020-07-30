package com.jfkj.im.entity;

import android.os.Parcel;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/11
 * </pre>
 */
public class HomeRecommend extends BaseResponse {

    private List<DataBean> data;

    protected HomeRecommend(Parcel in) {
        super(in);
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String head;
        private String vipLevel;
        private String homeVideo;
        private String hometown;
        private String recommenduid;
        private int gender;
        private String constellation;
        private int isfriend;
        private String nickname;
        private String userid;
        private String age;
        private String vipCard;  //0无，1银卡，2金卡，3黑卡
        private String position;       //位置取这个值

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }


        /**
         * head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
         * recommenduid : 76743604952891392
         * distance : 3245157.0
         * gender : 1
         * constellation :
         * nickname : 普陀一哥
         * userid : 76743604952891392
         * age : 21
         */

        private String distance;


        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public int getIsfriend() {
            return isfriend;
        }

        public void setIsfriend(int isfriend) {
            this.isfriend = isfriend;
        }

        public String getHomeVideo() {
            return homeVideo;
        }

        public void setHomeVideo(String homeVideo) {
            this.homeVideo = homeVideo;
        }

        public String getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(String vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getVipCard() {
            return vipCard;
        }

        public void setVipCard(String vipCard) {
            this.vipCard = vipCard;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getRecommenduid() {
            return recommenduid;
        }

        public void setRecommenduid(String recommenduid) {
            this.recommenduid = recommenduid;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

    }
}
