package com.jfkj.im.Bean;

import java.util.List;

public class MatchingdegreeBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"head":"http://iyoufile.198ty.com/user/picture/20200117/ffce4fd6546d8f14_s.jpg","answer":[{"answerId":101,"content":"问题[101]的答案[1]","serialNo":1,"questionId":101,"state":1},{"answerId":104,"content":"问题[102]的答案[1]","serialNo":1,"questionId":102,"state":1},{"answerId":107,"content":"问题[103]的答案[1]","serialNo":1,"questionId":103,"state":1},{"answerId":110,"content":"问题[104]的答案[1]","serialNo":1,"questionId":104,"state":1},{"answerId":113,"content":"问题[105]的答案[1]","serialNo":1,"questionId":105,"state":1},{"answerId":116,"content":"问题[106]的答案[1]","serialNo":1,"questionId":106,"state":1},{"answerId":119,"content":"问题[107]的答案[1]","serialNo":1,"questionId":107,"state":1},{"answerId":122,"content":"问题[108]的答案[1]","serialNo":1,"questionId":108,"state":1},{"answerId":125,"content":"问题[109]的答案[1]","serialNo":1,"questionId":109,"state":1},{"answerId":128,"content":"问题[110]的答案[1]","serialNo":1,"questionId":110,"state":1},{"answerId":131,"content":"问题[111]的答案[1]","serialNo":1,"questionId":111,"state":1},{"answerId":134,"content":"问题[112]的答案[1]","serialNo":1,"questionId":112,"state":1},{"answerId":137,"content":"问题[113]的答案[1]","serialNo":1,"questionId":113,"state":1}],"nickName":"~12345678912345","percentage":100,"userId":"76751335294238720"}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * head : http://iyoufile.198ty.com/user/picture/20200117/ffce4fd6546d8f14_s.jpg
         * answer : [{"answerId":101,"content":"问题[101]的答案[1]","serialNo":1,"questionId":101,"state":1},{"answerId":104,"content":"问题[102]的答案[1]","serialNo":1,"questionId":102,"state":1},{"answerId":107,"content":"问题[103]的答案[1]","serialNo":1,"questionId":103,"state":1},{"answerId":110,"content":"问题[104]的答案[1]","serialNo":1,"questionId":104,"state":1},{"answerId":113,"content":"问题[105]的答案[1]","serialNo":1,"questionId":105,"state":1},{"answerId":116,"content":"问题[106]的答案[1]","serialNo":1,"questionId":106,"state":1},{"answerId":119,"content":"问题[107]的答案[1]","serialNo":1,"questionId":107,"state":1},{"answerId":122,"content":"问题[108]的答案[1]","serialNo":1,"questionId":108,"state":1},{"answerId":125,"content":"问题[109]的答案[1]","serialNo":1,"questionId":109,"state":1},{"answerId":128,"content":"问题[110]的答案[1]","serialNo":1,"questionId":110,"state":1},{"answerId":131,"content":"问题[111]的答案[1]","serialNo":1,"questionId":111,"state":1},{"answerId":134,"content":"问题[112]的答案[1]","serialNo":1,"questionId":112,"state":1},{"answerId":137,"content":"问题[113]的答案[1]","serialNo":1,"questionId":113,"state":1}]
         * nickName : ~12345678912345
         * percentage : 100
         * userId : 76751335294238720
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
}
