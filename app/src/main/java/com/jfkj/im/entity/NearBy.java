package com.jfkj.im.entity;

import android.os.Parcel;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public class NearBy extends BaseResponse {

    private List<NearByData> data;

    protected NearBy(Parcel in) {
        super(in);
    }

    public List<NearByData> getData() {
        return data;
    }

    public void setData(List<NearByData> data) {
        this.data = data;
    }


    public static class NearByData {
        /**
         * head : http://iyoufile.198ty.com//1/null/null_1574848351479.png
         * recommenduid : 76866014024957950
         * distance : 9826400
         * gender : 2
         * constellation :
         * nickname : 111
         * userid : 78610126684160000
         * age :
         */

        private String head;
        private String recommenduid;
        private String distance;
        private int gender;
        private String constellation;
        private String nickname;
        private String userid;
        private String age;

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
