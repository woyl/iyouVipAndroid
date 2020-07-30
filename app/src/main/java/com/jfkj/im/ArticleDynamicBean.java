package com.jfkj.im;

import android.os.Parcel;

import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.CommentBean;

import java.util.List;

/**
 * <pre>
 * Description:  该tata圈动态
 * @author :   ys
 * @date :         2020/1/13
 * </pre>
 */
public class ArticleDynamicBean extends BaseResponse {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    protected ArticleDynamicBean(Parcel in) {
        super(in);
    }

    public static class DataBean {
        /**
         * commenCount : 0
         * Head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
         * images :
         * commenlist : []
         * articleId : 94641700198285312
         * praisecount : 2
         * NickName : 测试号_13700000073
         * content : 别点别评，测试用
         * videoPath : http://iyoufile.198ty.com/friend/video/20200115/36c7cb6a9cb901d3.mp4
         * videoImage : http://iyoufile.198ty.com/friend/video/20200115/36c7cb6a9cb901d3.jpg
         * isPraise : 0
         * isMore : 0
         * location : 广东深圳宝安区
         * videoTime : 0
         * cuserid : 76751300263411712
         * adddate : 14小时前
         */

        private int commenCount;
        private String Head;
        private String images;
        private String articleId;
        private String praisecount;
        private String NickName;
        private String content;
        private String videoPath;
        private String videoImage;
        private int isPraise;
        private String isMore;
        private String location;
        private String videoTime;
        private String cuserid;
        private String adddate;
        private String praiseId;
        private List<CommentBean> commenlist;
        private List<CircleBean.DataBean.PicImagesBean> picImages;
        private CircleBean.DataBean.VideoImagesBean videoImages;

        private String commenDiamonds;

        public String getCommenDiamonds() {
            return commenDiamonds;
        }

        public void setCommenDiamonds(String commenDiamonds) {
            this.commenDiamonds = commenDiamonds;
        }

        public List<CircleBean.DataBean.PicImagesBean> getPicImages() {
            return picImages;
        }

        public void setPicImages(List<CircleBean.DataBean.PicImagesBean> picImages) {
            this.picImages = picImages;
        }

        public CircleBean.DataBean.VideoImagesBean getVideoImages() {
            return videoImages;
        }

        public void setVideoImages(CircleBean.DataBean.VideoImagesBean videoImages) {
            this.videoImages = videoImages;
        }

        public int getCommenCount() {
            return commenCount;
        }

        public void setCommenCount(int commenCount) {
            this.commenCount = commenCount;
        }

        public String getHead() {
            return Head;
        }

        public void setHead(String Head) {
            this.Head = Head;
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

        public String getPraiseId() {
            return praiseId;
        }

        public void setPraiseId(String praiseId) {
            this.praiseId = praiseId;
        }

        public String getPraisecount() {
            return praisecount;
        }

        public void setPraisecount(String praisecount) {
            this.praisecount = praisecount;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public int getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(int isPraise) {
            this.isPraise = isPraise;
        }

        public String getIsMore() {
            return isMore;
        }

        public void setIsMore(String isMore) {
            this.isMore = isMore;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getVideoTime() {
            return videoTime;
        }

        public void setVideoTime(String videoTime) {
            this.videoTime = videoTime;
        }

        public String getCuserid() {
            return cuserid;
        }

        public void setCuserid(String cuserid) {
            this.cuserid = cuserid;
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
    }
}
