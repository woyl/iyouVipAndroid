package com.jfkj.im.TIM.redpack.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

public class MyCharacterTextBeanLeft implements Parcelable {

    /**
     * head : http://iyfile.tiantiancaidian.com/user/picture/20200314/856aca02e1e23d46_s.jpg
     * gameId : 118842512868769792
     * resultId : 118842578035671040
     * pick : 0
     * nickName : YOYO
     * percentage : 31
     * answerIds : 1076,1081,1085,1088,1093,1097,1100,1106,1108,1114,1117,1120,1153,
     * userId : 115886939227422720
     */

    private String head;
    private String gameId;
    private String resultId;
    private int pick;
    private String nickName;
    private int percentage;
    private String answerIds;
    private String userId;

    public MyCharacterTextBeanLeft() {
    }

    protected MyCharacterTextBeanLeft(Parcel in) {
        head = in.readString();
        gameId = in.readString();
        resultId = in.readString();
        pick = in.readInt();
        nickName = in.readString();
        percentage = in.readInt();
        answerIds = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(head);
        dest.writeString(gameId);
        dest.writeString(resultId);
        dest.writeInt(pick);
        dest.writeString(nickName);
        dest.writeInt(percentage);
        dest.writeString(answerIds);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyCharacterTextBeanLeft> CREATOR = new Creator<MyCharacterTextBeanLeft>() {
        @Override
        public MyCharacterTextBeanLeft createFromParcel(Parcel in) {
            return new MyCharacterTextBeanLeft(in);
        }

        @Override
        public MyCharacterTextBeanLeft[] newArray(int size) {
            return new MyCharacterTextBeanLeft[size];
        }
    };

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public int getPick() {
        return pick;
    }

    public void setPick(int pick) {
        this.pick = pick;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(String answerIds) {
        this.answerIds = answerIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
