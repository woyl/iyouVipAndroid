package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.jfkj.im.Bean.ChildCommentList;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class CommentBean  implements Parcelable {


    public CommentBean() {
    }

    /**
     * commentNickName : 789
     * commentRNickName : 789
     * articleId : 87012985960267776
     * ruserId : 81217675350638592
     * userId : 81217675350638592
     * addDate : 2019-12-26T20:07:32
     * content : ghhh？？？？？？
     */

    private String commentNickName;
    private String commentRNickName;
    private String articleId;
    private String ruserId;
    private String userId;
    private String addDate;
    private String content;
    private String commentHead;
    private String contentId;

    private List<ChildCommentList> childCommentList;


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public static Creator<CommentBean> getCREATOR() {
        return CREATOR;
    }

    protected CommentBean(Parcel in) {
        commentNickName = in.readString();
        commentRNickName = in.readString();
        articleId = in.readString();
        ruserId = in.readString();
        userId = in.readString();
        addDate = in.readString();
        content = in.readString();
        commentHead = in.readString();
        contentId = in.readString();
        childCommentList = in.createTypedArrayList(ChildCommentList.CREATOR);
        contentId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentNickName);
        dest.writeString(commentRNickName);
        dest.writeString(articleId);
        dest.writeString(ruserId);
        dest.writeString(userId);
        dest.writeString(addDate);
        dest.writeString(content);
        dest.writeString(commentHead);
        dest.writeString(contentId);
        dest.writeTypedList(childCommentList);
        dest.writeString(contentId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
        @Override
        public CommentBean createFromParcel(Parcel in) {
            return new CommentBean(in);
        }

        @Override
        public CommentBean[] newArray(int size) {
            return new CommentBean[size];
        }
    };

    public String getCommentNickName() {
        return commentNickName;
    }

    public void setCommentNickName(String commentNickName) {
        this.commentNickName = commentNickName;
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

    public String getCommentHead() {
        return commentHead;
    }

    public void setCommentHead(String commentHead) {
        this.commentHead = commentHead;
    }

    public List<ChildCommentList> getChildCommentList() {
        return childCommentList;
    }

    public void setChildCommentList(List<ChildCommentList> childCommentList) {
        this.childCommentList = childCommentList;
    }
}
