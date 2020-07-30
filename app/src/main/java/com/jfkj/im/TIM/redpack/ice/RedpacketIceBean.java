package com.jfkj.im.TIM.redpack.ice;

public class RedpacketIceBean {

    /**
     * result : true
     * money : 500
     * redState : 2
     * tradeOrderNo : PB60120200421129669255171014656
     * error : RED_PACKET_OPEN_FAIL
     * remainSize : 0
     * sendWord : 谢谢你完成我的游戏点击打开
     * remainMoney : 0
     * receiveId : 129669255171014656
     */

    private boolean result;
    private String money;
    private String redState;
    private String tradeOrderNo;
    private String error;
    private String remainSize;
    private String sendWord;
    private String remainMoney;
    private String receiveId;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRedState() {
        return redState;
    }

    public void setRedState(String redState) {
        this.redState = redState;
    }

    public String getTradeOrderNo() {
        return tradeOrderNo;
    }

    public void setTradeOrderNo(String tradeOrderNo) {
        this.tradeOrderNo = tradeOrderNo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRemainSize() {
        return remainSize;
    }

    public void setRemainSize(String remainSize) {
        this.remainSize = remainSize;
    }

    public String getSendWord() {
        return sendWord;
    }

    public void setSendWord(String sendWord) {
        this.sendWord = sendWord;
    }

    public String getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(String remainMoney) {
        this.remainMoney = remainMoney;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }
}
