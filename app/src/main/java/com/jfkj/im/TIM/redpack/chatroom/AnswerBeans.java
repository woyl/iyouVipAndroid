package com.jfkj.im.TIM.redpack.chatroom;

import java.util.List;

public class AnswerBeans {

    /**
     * questionId : 101
     * answer : [{"answerId":101,"content":"问题[101]的答案[1]","serialNo":1,"questionId":101,"state":1},{"answerId":102,"content":"我改了,问题[101]的答案[2],问题[101]的答案[2],问题[101]的答案[2]","serialNo":2,"questionId":101,"state":1},{"answerId":103,"content":"问题[101]的答案[3]","serialNo":3,"questionId":101,"state":1}]
     * subject : 标题：101
     * sort : 1579238315248
     * state : 1
     * addDate : 2020-01-17 13:18:35
     */

    private int questionId;
    private String subject;
    private String sort;
    private int state;
    private String addDate;
    private boolean isSelect;
    private List<AnswerBean> answer;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    public List<AnswerBean> getAnswer() {
        return answer;
    }

    public void setAnswer(List<AnswerBean> answer) {
        this.answer = answer;
    }

    public static class AnswerBean {
        /**
         * answerId : 101
         * content : 问题[101]的答案[1]
         * serialNo : 1
         * questionId : 101
         * state : 1
         */

        private int answerId;
        private String content;
        private int serialNo;
        private int questionId;
        private int state;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

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
