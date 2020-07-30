package com.jfkj.im.TIM.redpack.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

public class AnswerSelfBean implements Parcelable {

    /**
     * questionId : 1075
     * answer : {"answerId":1078,"content":"运动","serialNo":0,"questionId":1075,"state":1}
     * subject : 解压的方式是
     * sort : 0
     * state : 1
     * addDate : 2020-03-21 20:01:43
     */

    private int questionId;
    private AnswerBean answer;
    private String subject;
    private String sort;
    private int state;
    private String addDate;

    public AnswerSelfBean() {
    }

    protected AnswerSelfBean(Parcel in) {
        questionId = in.readInt();
        subject = in.readString();
        sort = in.readString();
        state = in.readInt();
        addDate = in.readString();
    }

    public static final Creator<AnswerSelfBean> CREATOR = new Creator<AnswerSelfBean>() {
        @Override
        public AnswerSelfBean createFromParcel(Parcel in) {
            return new AnswerSelfBean(in);
        }

        @Override
        public AnswerSelfBean[] newArray(int size) {
            return new AnswerSelfBean[size];
        }
    };

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(questionId);
        dest.writeString(subject);
        dest.writeString(sort);
        dest.writeInt(state);
        dest.writeString(addDate);
    }

    public static class AnswerBean implements Parcelable{
        /**
         * answerId : 1078
         * content : 运动
         * serialNo : 0
         * questionId : 1075
         * state : 1
         */

        private int answerId;
        private String content;
        private int serialNo;
        private int questionId;
        private int state;

        public AnswerBean() {
        }

        protected AnswerBean(Parcel in) {
            answerId = in.readInt();
            content = in.readString();
            serialNo = in.readInt();
            questionId = in.readInt();
            state = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(answerId);
            dest.writeString(content);
            dest.writeInt(serialNo);
            dest.writeInt(questionId);
            dest.writeInt(state);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<AnswerBean> CREATOR = new Creator<AnswerBean>() {
            @Override
            public AnswerBean createFromParcel(Parcel in) {
                return new AnswerBean(in);
            }

            @Override
            public AnswerBean[] newArray(int size) {
                return new AnswerBean[size];
            }
        };

        public int getAnswerId() {
            return answerId;
        }

        public void setAnswerId(int answerId) {
            this.answerId = answerId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(int serialNo) {
            this.serialNo = serialNo;
        }

        public int getQuestionId() {
            return questionId;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
