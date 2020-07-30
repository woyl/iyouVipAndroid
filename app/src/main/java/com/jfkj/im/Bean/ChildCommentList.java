package com.jfkj.im.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChildCommentList implements Parcelable {

    /**
     * commentNickName : mmmm
     * commentHead : http://file.vipiyou.com/user/picture/20200428/801003c77219bdc1_l.jpg
     * commentRNickName : coco
     * articleId : 137328020007550976
     * ruserId : 132310047622561792
     * userId : 132264384402685952
     * addDate : 2020-05-12 14:07:39
     * content : 童年
     */

    private String commentNickName;
    private String commentHead;
    private String commentRNickName;
    private String articleId;
    private String ruserId;
    private String userId;
    private String addDate;
    private String content;
    private String contentId;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public static Creator<ChildCommentList> getCREATOR() {
        return CREATOR;
    }

    protected ChildCommentList(Parcel in) {
        commentNickName = in.readString();
        commentHead = in.readString();
        commentRNickName = in.readString();
        articleId = in.readString();
        ruserId = in.readString();
        userId = in.readString();
        addDate = in.readString();
        content = in.readString();
        contentId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentNickName);
        dest.writeString(commentHead);
        dest.writeString(commentRNickName);
        dest.writeString(articleId);
        dest.writeString(ruserId);
        dest.writeString(userId);
        dest.writeString(addDate);
        dest.writeString(content);
        dest.writeString(contentId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChildCommentList> CREATOR = new Creator<ChildCommentList>() {
        @Override
        public ChildCommentList createFromParcel(Parcel in) {
            return new ChildCommentList(in);
        }

        @Override
        public ChildCommentList[] newArray(int size) {
            return new ChildCommentList[size];
        }
    };

    public String getCommentNickName() {
        return commentNickName;
    }

    public void setCommentNickName(String commentNickName) {
        this.commentNickName = commentNickName;
    }

    public String getCommentHead() {
        return commentHead;
    }

    public void setCommentHead(String commentHead) {
        this.commentHead = commentHead;
    }

    public String getCommentRNickName() {
        return commentRNickName;
    }

    public void setCommentRNickName(String commentRNickName) {
        this.commentRNickName = commentRNickName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getRuserId() {
        return ruserId;
    }

    public void setRuserId(String ruserId) {
        this.ruserId = ruserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
