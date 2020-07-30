package com.jfkj.im.Bean;

import java.util.List;

public class MineCharacterttestBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"gameId":"104120804580655104","sort":"1581346421239","gameState":2,"addDate":"2020-02-10 22:53:41"},{"gameId":"104118225796726784","sort":"1581345806409","gameState":2,"addDate":"2020-02-10 22:43:26"},{"gameId":"104114824010661888","sort":"1581344995360","gameState":2,"addDate":"2020-02-10 22:29:55"},{"gameId":"104111875091988480","sort":"1581344292283","gameState":2,"addDate":"2020-02-10 22:18:12"},{"gameId":"104107142809780224","sort":"1581343164018","gameState":2,"addDate":"2020-02-10 21:59:24"},{"gameId":"104101391865348096","sort":"1581341792886","gameState":2,"addDate":"2020-02-10 21:36:32"},{"gameId":"104098756923883520","sort":"1581341164667","gameState":2,"addDate":"2020-02-10 21:26:04"},{"gameId":"104098752524058624","sort":"1581341163619","gameState":2,"addDate":"2020-02-10 21:26:03"},{"gameId":"104098741182660608","sort":"1581341160915","gameState":2,"addDate":"2020-02-10 21:26:00"},{"gameId":"104003013697470464","sort":"1581318337704","gameState":2,"addDate":"2020-02-10 15:05:37"}]}
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
             * gameId : 104120804580655104
             * sort : 1581346421239
             * gameState : 2
             * addDate : 2020-02-10 22:53:41
             */

            private String gameId;
            private String sort;
            private int gameState;
            private String addDate;

            public String getGameId() {
                return gameId;
            }

            public void setGameId(String gameId) {
                this.gameId = gameId;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public int getGameState() {
                return gameState;
            }

            public void setGameState(int gameState) {
                this.gameState = gameState;
            }

            public String getAddDate() {
                return addDate;
            }

            public void setAddDate(String addDate) {
                this.addDate = addDate;
            }
        }
    }
}
