package com.jfkj.im.Bean;

import java.util.List;

public class loadUserAnswerList {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"head":"http://iyoufile.198ty.com/user/picture/20200117/ffce4fd6546d8f14_s.jpg","gameId":"104397499690778624","resultId":"104397563377090560","pick":0,"nickName":"~12345678912345","percentage":100,"answerIds":"101,104,107,110,113,116,119,122,125,128,131,134,137,","userId":"76751335294238720"}]}
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
             * head : http://iyoufile.198ty.com/user/picture/20200117/ffce4fd6546d8f14_s.jpg
             * gameId : 104397499690778624
             * resultId : 104397563377090560
             * pick : 0
             * nickName : ~12345678912345
             * percentage : 100
             * answerIds : 101,104,107,110,113,116,119,122,125,128,131,134,137,
             * userId : 76751335294238720
             */

            private String head;
            private String gameId;
            private String resultId;
            private int pick;
            private String nickName;
            private int percentage;
            private String answerIds;
            private String userId;

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
    }
}
