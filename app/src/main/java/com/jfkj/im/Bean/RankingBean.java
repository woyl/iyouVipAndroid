package com.jfkj.im.Bean;

import java.util.List;

public class RankingBean {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"head":"http://iyfile.tiantiancaidian.com/static/VipHead.png","statday":"2020-03-11","money":57,"nickname":"发送大放送的","type":2,"userid":"113050500244570112"},{"head":"http://iyfile.tiantiancaidian.com/static/VipHead.png","statday":"2020-03-11","money":57,"nickname":"高呼","type":2,"userid":"113056866917023744"},{"head":"http://iyfile.tiantiancaidian.com/static/VipHead.png","statday":"2020-03-11","money":38,"nickname":"呜呜好","type":2,"userid":"113052245188280320"},{"head":"http://iyfile.tiantiancaidian.com/static/VipHead.png","statday":"2020-03-11","money":38,"nickname":"测试2","type":2,"userid":"113079273589440512"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * head : http://iyfile.tiantiancaidian.com/static/VipHead.png
         * statday : 2020-03-11
         * money : 57.0
         * nickname : 发送大放送的
         * type : 2
         * userid : 113050500244570112
         */

        private String head;
        private String statday;
        private int money;
        private String nickname;
        private int type;
        private String userid;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getStatday() {
            return statday;
        }

        public void setStatday(String statday) {
            this.statday = statday;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
