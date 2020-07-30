package com.jfkj.im.entity;

import java.util.List;

/**
 * <pre>
 * Description:  累计获得的礼物
 * @author :   ys
 * @date :         2019/12/26
 * </pre>
 */
public class MyObtainGiftBean {

    /**
     * code : 200
     * message : 查询成功
     * data : [{"sendIdHead":"http://iyfile.tiantiancaidian.com/static/VipHead.png","bizType":203,"tradeOrder":"MXYX20320200327120627125131804672","sendId":"119667968023068672","groupId":"119683584528351232","bizTypeName":"冒险游戏","userId":"114545907432816640","orderState":"","money":500,"balance":1160,"redid":"120627095561961472","state":1,"adddate":"2020-03-27 12:03:54"},{"getPictureUrl":"http://iyfile.tiantiancaidian.com/admin/20200325/20200325_1585124014267.png","bizType":101,"tradeOrder":"JP10120200327120626657433354240","money":20,"balance":660,"bizTypeName":"添加好友赠送礼物","rsenderId":"119667968023068672","rsenderidHead":"http://iyfile.tiantiancaidian.com/static/VipHead.png","userId":"114545907432816640","adddate":"2020-03-27 12:02:12","giftSize":1,"orderState":2},{"sendIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200326/8134f5c53af400a1_l.jpg","bizType":202,"tradeOrder":"HQ20220200322118831387032813568","sendId":"114545907432816640","groupId":"116153394716082176","bizTypeName":"俱乐部红包","userId":"114545907432816640","orderState":"","money":50,"balance":1260,"redid":"118831361187512320","state":1,"adddate":"2020-03-22 13:08:17"},{"sendIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200326/8134f5c53af400a1_l.jpg","bizType":202,"tradeOrder":"HQ20220200322118779796120535040","sendId":"114545907432816640","groupId":"116153394716082176","bizTypeName":"俱乐部红包","userId":"114545907432816640","orderState":"","money":1000,"balance":1210,"redid":"118779770707247104","state":1,"adddate":"2020-03-22 09:43:17"},{"sendIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200326/8134f5c53af400a1_l.jpg","bizType":203,"tradeOrder":"MXYX20320200322118778725105008640","sendId":"114545907432816640","groupId":"116153394716082176","bizTypeName":"冒险游戏","userId":"114545907432816640","orderState":"","money":100,"balance":210,"redid":"118778699804966912","state":1,"adddate":"2020-03-22 09:39:01"},{"sendIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200326/8134f5c53af400a1_l.jpg","bizType":202,"tradeOrder":"HQ20220200321118614152137801728","sendId":"114545907432816640","groupId":"116153394716082176","bizTypeName":"俱乐部红包","userId":"114545907432816640","orderState":"","money":10,"balance":110,"redid":"118614124639944704","state":1,"adddate":"2020-03-21 22:45:04"},{"sendIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200326/8134f5c53af400a1_l.jpg","bizType":202,"tradeOrder":"HQ20220200317117147944897085440","sendId":"114545907432816640","groupId":"116153394716082176","bizTypeName":"俱乐部红包","userId":"114545907432816640","orderState":"","money":1000,"balance":1000,"redid":"117147896549343232","state":1,"adddate":"2020-03-17 21:38:53"}]
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
         * sendIdHead : http://iyfile.tiantiancaidian.com/static/VipHead.png
         * bizType : 203
         * tradeOrder : MXYX20320200327120627125131804672
         * sendId : 119667968023068672
         * groupId : 119683584528351232
         * bizTypeName : 冒险游戏
         * userId : 114545907432816640
         * orderState :
         * money : 500.0
         * balance : 1160.0
         * redid : 120627095561961472
         * state : 1
         * adddate : 2020-03-27 12:03:54
         * getPictureUrl : http://iyfile.tiantiancaidian.com/admin/20200325/20200325_1585124014267.png
         * rsenderId : 119667968023068672
         * rsenderidHead : http://iyfile.tiantiancaidian.com/static/VipHead.png
         * giftSize : 1
         */

        private String sendIdHead;
        private String bizType;
        private String tradeOrder;
        private String sendId;
        private String groupId;
        private String bizTypeName;
        private String userId;
        private String orderState;
        private Integer money;
        private Integer balance;
        private String redid;
        private int state;
        private String adddate;
        private String getPictureUrl;
        private String rsenderId;
        private String rsenderidHead;
        private int giftSize;

        private String rsenderNickName ;

        public String getRsenderNickName() {

            return rsenderNickName;
        }

        public void setRsenderNickName(String rsenderNickName) {
            this.rsenderNickName = rsenderNickName;
        }

        public String getSendIdHead() {
            return sendIdHead;
        }

        public void setSendIdHead(String sendIdHead) {
            this.sendIdHead = sendIdHead;
        }

        public String getBizType() {
            return bizType;
        }

        public void setBizType(String bizType) {
            this.bizType = bizType;
        }

        public String getTradeOrder() {
            return tradeOrder;
        }

        public void setTradeOrder(String tradeOrder) {
            this.tradeOrder = tradeOrder;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public Integer getMoney() {
            return money;
        }

        public void setMoney(Integer money) {
            this.money = money;
        }

        public Integer getBalance() {
            return balance;
        }

        public void setBalance(Integer balance) {
            this.balance = balance;
        }

        public String getRedid() {
            return redid;
        }

        public void setRedid(String redid) {
            this.redid = redid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }

        public String getGetPictureUrl() {
            return getPictureUrl;
        }

        public void setGetPictureUrl(String getPictureUrl) {
            this.getPictureUrl = getPictureUrl;
        }

        public String getRsenderId() {
            return rsenderId;
        }

        public void setRsenderId(String rsenderId) {
            this.rsenderId = rsenderId;
        }

        public String getRsenderidHead() {
            return rsenderidHead;
        }

        public void setRsenderidHead(String rsenderidHead) {
            this.rsenderidHead = rsenderidHead;
        }

        public int getGiftSize() {
            return giftSize;
        }

        public void setGiftSize(int giftSize) {
            this.giftSize = giftSize;
        }
    }
}
