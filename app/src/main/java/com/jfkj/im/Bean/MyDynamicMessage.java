package com.jfkj.im.Bean;

import android.os.Parcel;

import com.jfkj.im.entity.BaseResponse;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/13
 * </pre>
 */
public class MyDynamicMessage extends BaseResponse {

    private List<DataBean> data;

    protected MyDynamicMessage(Parcel in) {
        super(in);
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * head : http://iyoufile.198ty.com//1/632047/632047_1577757373283.png
         * articleImages : http://iyoufile.198ty.com/friend/picture/20200114/f8adbbfc4b0334a8_s.jpg
         * articleid : 94134696941846528
         * commentid : 0
         * ruserid : 0
         * ruserId : 0
         * userNickName : 苍风一目连
         * type : 0
         * userid : 89067290909343744
         * content :
         * adddate : 38分钟前
         */

        private String heads;
        private String articleImages;
        private String articleId;
        private String commentid;
        private String ruserId;
        private String type;  //0 点赞类型  1 评论类型
        private String userid;
        private String content;
        private String adddate;
        private String ruserNickName;

        private String head;

        private long commentId;

        private String userNickName;

        private String userId;

        public String getHead() {
            return head;
        }

        public String getHeads() {
            return heads;
        }

        public void setHeads(String heads) {
            this.heads = heads;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public long getCommentId() {
            return commentId;
        }

        public void setCommentId(long commentId) {
            this.commentId = commentId;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }


        public String getRuserNickName() {
            return ruserNickName;
        }

        public void setRuserNickName(String ruserNickName) {
            this.ruserNickName = ruserNickName;
        }

        public String getArticleImages() {
            return articleImages;
        }

        public void setArticleImages(String articleImages) {
            this.articleImages = articleImages;
        }

        public String getCommentid() {
            return commentid;
        }

        public void setCommentid(String commentid) {
            this.commentid = commentid;
        }

        public String getRuserId() {
            return ruserId;
        }

        public void setRuserId(String ruserId) {
            this.ruserId = ruserId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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
    }
}
