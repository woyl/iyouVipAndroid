package com.jfkj.im.Bean;

import java.util.List;

public class TestQuestionBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"questionId":101,"answer":[{"answerId":101,"content":"问题[101]的答案[1]","serialNo":1,"questionId":101,"state":1},{"answerId":102,"content":"问题[101]的答案[2]","serialNo":2,"questionId":101,"state":1},{"answerId":103,"content":"问题[101]的答案[3]","serialNo":3,"questionId":101,"state":1}],"subject":"标题：101","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":102,"answer":[{"answerId":104,"content":"问题[102]的答案[1]","serialNo":1,"questionId":102,"state":1},{"answerId":105,"content":"问题[102]的答案[2]","serialNo":2,"questionId":102,"state":1},{"answerId":106,"content":"问题[102]的答案[3]","serialNo":3,"questionId":102,"state":1}],"subject":"标题：102","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":103,"answer":[{"answerId":107,"content":"问题[103]的答案[1]","serialNo":1,"questionId":103,"state":1},{"answerId":108,"content":"问题[103]的答案[2]","serialNo":2,"questionId":103,"state":1},{"answerId":109,"content":"问题[103]的答案[3]","serialNo":3,"questionId":103,"state":1}],"subject":"标题：103","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":104,"answer":[{"answerId":110,"content":"问题[104]的答案[1]","serialNo":1,"questionId":104,"state":1},{"answerId":111,"content":"问题[104]的答案[2]","serialNo":2,"questionId":104,"state":1},{"answerId":112,"content":"问题[104]的答案[3]","serialNo":3,"questionId":104,"state":1}],"subject":"标题：104","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":105,"answer":[{"answerId":113,"content":"问题[105]的答案[1]","serialNo":1,"questionId":105,"state":1},{"answerId":114,"content":"问题[105]的答案[2]","serialNo":2,"questionId":105,"state":1},{"answerId":115,"content":"问题[105]的答案[3]","serialNo":3,"questionId":105,"state":1}],"subject":"标题：105","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":106,"answer":[{"answerId":116,"content":"问题[106]的答案[1]","serialNo":1,"questionId":106,"state":1},{"answerId":117,"content":"问题[106]的答案[2]","serialNo":2,"questionId":106,"state":1},{"answerId":118,"content":"问题[106]的答案[3]","serialNo":3,"questionId":106,"state":1}],"subject":"标题：106","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":107,"answer":[{"answerId":119,"content":"问题[107]的答案[1]","serialNo":1,"questionId":107,"state":1},{"answerId":120,"content":"问题[107]的答案[2]","serialNo":2,"questionId":107,"state":1},{"answerId":121,"content":"问题[107]的答案[3]","serialNo":3,"questionId":107,"state":1}],"subject":"标题：107","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":108,"answer":[{"answerId":122,"content":"问题[108]的答案[1]","serialNo":1,"questionId":108,"state":1},{"answerId":123,"content":"问题[108]的答案[2]","serialNo":2,"questionId":108,"state":1},{"answerId":124,"content":"问题[108]的答案[3]","serialNo":3,"questionId":108,"state":1}],"subject":"标题：108","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":109,"answer":[{"answerId":125,"content":"问题[109]的答案[1]","serialNo":1,"questionId":109,"state":1},{"answerId":126,"content":"问题[109]的答案[2]","serialNo":2,"questionId":109,"state":1},{"answerId":127,"content":"问题[109]的答案[3]","serialNo":3,"questionId":109,"state":1}],"subject":"标题：109","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":110,"answer":[{"answerId":128,"content":"问题[110]的答案[1]","serialNo":1,"questionId":110,"state":1},{"answerId":129,"content":"问题[110]的答案[2]","serialNo":2,"questionId":110,"state":1},{"answerId":130,"content":"问题[110]的答案[3]","serialNo":3,"questionId":110,"state":1}],"subject":"标题：110","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":111,"answer":[{"answerId":131,"content":"问题[111]的答案[1]","serialNo":1,"questionId":111,"state":1},{"answerId":132,"content":"问题[111]的答案[2]","serialNo":2,"questionId":111,"state":1},{"answerId":133,"content":"问题[111]的答案[3]","serialNo":3,"questionId":111,"state":1}],"subject":"标题：111","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":112,"answer":[{"answerId":134,"content":"问题[112]的答案[1]","serialNo":1,"questionId":112,"state":1},{"answerId":135,"content":"问题[112]的答案[2]","serialNo":2,"questionId":112,"state":1},{"answerId":136,"content":"问题[112]的答案[3]","serialNo":3,"questionId":112,"state":1}],"subject":"标题：112","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"},{"questionId":113,"answer":[{"answerId":137,"content":"问题[113]的答案[1]","serialNo":1,"questionId":113,"state":1},{"answerId":138,"content":"问题[113]的答案[2]","serialNo":2,"questionId":113,"state":1},{"answerId":139,"content":"问题[113]的答案[3]","serialNo":3,"questionId":113,"state":1}],"subject":"标题：113","sort":"1579238315248","state":1,"addDate":"2020-01-17 13:18:35"}]}
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
        private List<ArrayBean> array;

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

        public static class ArrayBean {
            /**
             * questionId : 101
             * answer : [{"answerId":101,"content":"问题[101]的答案[1]","serialNo":1,"questionId":101,"state":1},{"answerId":102,"content":"问题[101]的答案[2]","serialNo":2,"questionId":101,"state":1},{"answerId":103,"content":"问题[101]的答案[3]","serialNo":3,"questionId":101,"state":1}]
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
                private boolean isselect=false;

                public boolean isIsselect() {
                    return isselect;
                }

                public void setIsselect(boolean isselect) {
                    this.isselect = isselect;
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
    }
}
