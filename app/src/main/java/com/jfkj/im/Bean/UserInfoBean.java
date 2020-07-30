package com.jfkj.im.Bean;

public class UserInfoBean {

    /**
     * vipLevel : 0
     * gender : 1
     * examine : 0
     * optState : 0
     * vipCard : 0
     * userId : 110842049208647680
     * 比如。gender 为0 则说明先要填写性别，昵称
     * gender 为1 男性，vipCard =0且 optState=0则到男性用户选择好友页面。
     * gender 为1 男性，vipCard =0且 optState=1 到购买会员页面
     * gender 为1 男性，vipCard =1 银卡且 examine=0 提交审核
     * gender 为1 男性，vipCard =1 银卡且 examine=1 首页
     */

    private int vipLevel;
    private int gender;
    private int examine;
    private int optState;
    private int vipCard;
    private String userId;

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getExamine() {
        return examine;
    }

    public void setExamine(int examine) {
        this.examine = examine;
    }

    public int getOptState() {
        return optState;
    }

    public void setOptState(int optState) {
        this.optState = optState;
    }

    public int getVipCard() {
        return vipCard;
    }

    public void setVipCard(int vipCard) {
        this.vipCard = vipCard;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
