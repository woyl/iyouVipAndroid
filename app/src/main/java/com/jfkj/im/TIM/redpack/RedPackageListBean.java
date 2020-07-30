package com.jfkj.im.TIM.redpack;

public class RedPackageListBean {
    /**
     * bizType : 202
     * sendId : 76751314603737088
     * groupId : 76751317325840384
     * bizTypeName : 俱乐部红包
     * sort : 1574913415349
     * addDate : 2019-11-28 11:56:55
     * userId : 76751314603737088
     * vipLevel : 3
     * userHead : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
     * money : 3
     * bestLuck : 0
     * redId : 77099438023376896
     * state : 1
     * userNickName : 测试号_13700000083
     * recId : 77138822198198272
     */
    /**
     * {
     * "bizType": 202,
     * "sendId": "76751314603737088",//发送红包用户ID <string>
     * "groupId": "76751317325840384",//俱乐部ID <string>
     * "bizTypeName": "俱乐部红包",//... <string>
     * "sort": "1574913415349",//排序字段 <string>
     * "addDate": "2019-11-28 11:56:55",//领取时间 <string>
     * "userId": "76751314603737088",//领取红包用户ID <string>
     * "vipLevel": 3,//领取用户VIP等级 <number>
     * "userHead": "http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png",//用户头像 <string>
     * "money": 3,//领取金额 <number>
     * "bestLuck": 0,//是否手气最佳 <number>
     * "redId": "77099438023376896",//红包ID <string>
     * "state": 1,//1 正常 0 领取失败 <number>
     * "userNickName": "测试号_13700000083",//用户昵称 <string>
     * "recId": "77138822198198272"//领取记录ID <string>
     * }
     * */

    private int bizType;
    private String sendId;
    private String groupId;
    private String bizTypeName;
    private String sort;
    private String addDate;
    private String userId;
    private int vipLevel;
    private String userHead;
    private int money;
    private int bestLuck;
    private String redId;
    private int state;
    private String userNickName;
    private String recId;

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
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

    public int getBestLuck() {
        return bestLuck;
    }

    public void setBestLuck(int bestLuck) {
        this.bestLuck = bestLuck;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

}
