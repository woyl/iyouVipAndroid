package com.jfkj.im.TIM.redpack;

public class RedPackageHeadBean {


    /**
     * redType : 1
     * userHead : http://iyfile.tiantiancaidian.com/static/VipHead.png
     * money : 13
     * isReceive : 1
     * userNickName : 我的
     * packetSize : 1
     * receiveSize : 1
     * state : 2
     * sendWord : 米家
     * receiveMoney : 13
     * {
     * "code": "200",... <string>
     * "message": "操作成功",... <string>
     * -"data": {...<object>
     * "redType": 1,红包类型 1俱乐部红包，2每日红包 <number>
     * "userHead": "http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png",发送人头像 <string>
     * "money": 43,总金额 <number>
     * "isReceive": 0,是否领取 <number>
     * "userNickName": "测试号_13700000083",发送人昵称 <string>
     * "packetSize": 12,红包总大小 <number>
     * "receiveSize": 12,领取数量 <number>
     * "state": 2,1 正在领取 2 已领取完 3过期退款 <number>
     * "sendWord": "恭喜发财，大吉大利",红包描述 <string>
     * "receiveMoney": 0你领取红包金额 <number>
     * }
     * }
     */

    private int redType;
    private String userHead;
    private int money;
    private int isReceive;
    private String userNickName;
    private int packetSize;
    private int receiveSize;
    private int state;
    private String sendWord;
    private int receiveMoney;

    public int getRedType() {
        return redType;
    }

    public void setRedType(int redType) {
        this.redType = redType;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public int getReceiveSize() {
        return receiveSize;
    }

    public void setReceiveSize(int receiveSize) {
        this.receiveSize = receiveSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSendWord() {
        return sendWord;
    }

    public void setSendWord(String sendWord) {
        this.sendWord = sendWord;
    }

    public int getReceiveMoney() {
        return receiveMoney;
    }

    public void setReceiveMoney(int receiveMoney) {
        this.receiveMoney = receiveMoney;
    }
}
