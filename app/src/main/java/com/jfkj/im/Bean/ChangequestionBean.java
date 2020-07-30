package com.jfkj.im.Bean;

import java.util.List;

public class ChangequestionBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"questionId":118,"answer":[{"answerId":152,"content":"问题[118]的答案[1]","serialNo":1,"questionId":118,"state":1},{"answerId":153,"content":"问题[118]的答案[2]","serialNo":2,"questionId":118,"state":1},{"answerId":154,"content":"问题[118]的答案[3]","serialNo":3,"questionId":118,"state":1}],"subject":"标题：118","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"}
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
         * questionId : 118
         * answer : [{"answerId":152,"content":"问题[118]的答案[1]","serialNo":1,"questionId":118,"state":1},{"answerId":153,"content":"问题[118]的答案[2]","serialNo":2,"questionId":118,"state":1},{"answerId":154,"content":"问题[118]的答案[3]","serialNo":3,"questionId":118,"state":1}]
         * subject : 标题：118
         * sort : 1579238315248
         * state : 1
         * addDate : 2020-01-17 13:18:35
         */

        private int questionId;
        private String subject;
        private String sort;
        private int state;
        private String addDate;
        private List<AnswerBean> answer;

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
             * answerId : 152
             * content : 问题[118]的答案[1]
             * serialNo : 1
             * questionId : 118
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
