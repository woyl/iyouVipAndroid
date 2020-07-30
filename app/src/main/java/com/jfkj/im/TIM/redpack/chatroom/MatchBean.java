package com.jfkj.im.TIM.redpack.chatroom;

import java.util.List;

public class MatchBean {

    /**
     * head : http://iyfile.tiantiancaidian.com/user/picture/20200314/856aca02e1e23d46_s.jpg
     * answer : [{"answerId":1076,"content":"吃东西","serialNo":0,"questionId":1075,"state":1},{"answerId":1081,"content":"而非地方","serialNo":0,"questionId":1079,"state":1},{"answerId":1085,"content":"个人风格弱覆盖","serialNo":0,"questionId":1083,"state":1},{"answerId":1088,"content":"混凝土你和他","serialNo":0,"questionId":1087,"state":1},{"answerId":1093,"content":"避风港避风港吧","serialNo":0,"questionId":1091,"state":1},{"answerId":1097,"content":"二个人个","serialNo":0,"questionId":1095,"state":1},{"answerId":1100,"content":"的故事中国官方","serialNo":0,"questionId":1099,"state":1},{"answerId":1106,"content":"给v额v俄国人","serialNo":0,"questionId":1103,"state":1},{"answerId":1108,"content":"而非人格人格","serialNo":0,"questionId":1107,"state":1},{"answerId":1114,"content":"二个人个人","serialNo":0,"questionId":1111,"state":1},{"answerId":1117,"content":"而非个人个人","serialNo":0,"questionId":1115,"state":1},{"answerId":1120,"content":"分分分","serialNo":0,"questionId":1119,"state":1},{"answerId":1153,"content":"的v阿凡达v","serialNo":0,"questionId":1151,"state":1}]
     * nickName : YOYO
     * percentage : 31
     * userId : 115886939227422720
     */

    private String head;
    private String nickName;
    private int percentage;
    private String userId;
    private List<AnswerBean> answer;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AnswerBean> getAnswer() {
        return answer;
    }

    public void setAnswer(List<AnswerBean> answer) {
        this.answer = answer;
    }

    public static class AnswerBean {
        /**
         * answerId : 1076
         * content : 吃东西
         * serialNo : 0
         * questionId : 1075
         * state : 1
         */

        private int answerId;
        private String content;
        private int serialNo;
        private int questionId;
        private int state;

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
