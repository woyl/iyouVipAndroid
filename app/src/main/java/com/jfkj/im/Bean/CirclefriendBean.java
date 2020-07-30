package com.jfkj.im.Bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.jfkj.im.entity.CommentBean;

import java.util.ArrayList;
import java.util.List;

public class CirclefriendBean  implements Parcelable  {
    public String code;
    public String message;

    private List<DataBean> data;


    protected CirclefriendBean(Parcel in) {
        code = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<CirclefriendBean> CREATOR = new Creator<CirclefriendBean>() {
        @Override
        public CirclefriendBean createFromParcel(Parcel in) {
            return new CirclefriendBean(in);
        }

        @Override
        public CirclefriendBean[] newArray(int size) {
            return new CirclefriendBean[size];
        }
    };

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(code);
        dest.writeString(message);
        dest.writeTypedList(data);
    }


    public static class DataBean implements Parcelable {

        /**
         * videoPath :
         * commenCount : 0
         * Head : http://iyoufile.198ty.com//1/311104/311104_1575885829224.png
         * images : http://iyoufile.198ty.com/friend/picture/20191225/e7294dbcab41d7f4_s.jpg
         * commenlist : []
         * articleId : 87012985960267776
         * videoImage :
         * praisecount : 0
         * videoTime : 0
         * NickName : 789
         * content : 仿佛个
         * adddate : 2019-12-25T17:53:19
         */

        private String videoPath;
        private String commenCount;
        private String Head;
        private String images="";
        private String articleId;
        private String videoImage;
        private String praisecount;
        private String videoTime;
        private String NickName;
        private String content;
        private String adddate;
        private List<CommentBean> commenlist;
        private String location;
        private String cuserid;
        private String isPraise;
        private String praiseId;  //取消朋友圈点赞要传的

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getCommenCount() {
            return commenCount;
        }

        public void setCommenCount(String commenCount) {
            this.commenCount = commenCount;
        }

        public String getHead() {
            return Head;
        }

        public void setHead(String head) {
            Head = head;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public String getPraisecount() {
            return praisecount;
        }

        public void setPraisecount(String praisecount) {
            this.praisecount = praisecount;
        }

        public String getVideoTime() {
            return videoTime;
        }

        public void setVideoTime(String videoTime) {
            this.videoTime = videoTime;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }

        public List<CommentBean> getCommenlist() {
            return commenlist;
        }

        public void setCommenlist(List<CommentBean> commenlist) {
            this.commenlist = commenlist;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCuserid() {
            return cuserid;
        }

        public void setCuserid(String cuserid) {
            this.cuserid = cuserid;
        }

        public String getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(String isPraise) {
            this.isPraise = isPraise;
        }

        public String getPraiseId() {
            return praiseId;
        }

        public void setPraiseId(String praiseId) {
            this.praiseId = praiseId;
        }


        protected DataBean(Parcel in) {
            videoPath = in.readString();
            commenCount = in.readString();
            Head = in.readString();
            images = in.readString();
            articleId = in.readString();
            videoImage = in.readString();
            praisecount = in.readString();
            videoTime = in.readString();
            NickName = in.readString();
            content = in.readString();
            adddate = in.readString();
            commenlist = in.createTypedArrayList(CommentBean.CREATOR);
            location = in.readString();
            cuserid = in.readString();
            isPraise = in.readString();
            praiseId = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(videoPath);
            dest.writeString(commenCount);
            dest.writeString(Head);
            dest.writeString(images);
            dest.writeString(articleId);
            dest.writeString(videoImage);
            dest.writeString(praisecount);
            dest.writeString(videoTime);
            dest.writeString(NickName);
            dest.writeString(content);
            dest.writeString(adddate);
            dest.writeTypedList(commenlist);
            dest.writeString(location);
            dest.writeString(cuserid);
            dest.writeString(isPraise);
            dest.writeString(praiseId);
        }
    }

}
