package com.jfkj.im.entity;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/26
 * </pre>
 */
public class MyGiftBean   {



    /**
     * code : 200
     * message : 查询成功
     * data : [{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":102,"tradeOrder":"DL10220200321118519507416383488","money":19,"balance":3508322,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-21 16:28:59","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/static/VipHead.png","orderState":1,"receiveId":"113079273589440512"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":102,"tradeOrder":"DL10220200320118238085858328576","money":19,"balance":499381,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-20 21:50:43","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200320/9c199567c6e0a7fa_l.jpg","orderState":1,"receiveId":"118212859011399680"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":102,"tradeOrder":"DL10220200320118238021844860928","money":19,"balance":499400,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-20 21:50:28","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200320/9c199567c6e0a7fa_l.jpg","orderState":1,"receiveId":"118212859011399680"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":102,"tradeOrder":"DL10220200320118238018455863296","money":19,"balance":499419,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-20 21:50:27","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200320/9c199567c6e0a7fa_l.jpg","orderState":1,"receiveId":"118212859011399680"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":102,"tradeOrder":"DL10220200320118237980975562752","money":19,"balance":499438,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-20 21:50:18","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200320/9c199567c6e0a7fa_l.jpg","orderState":1,"receiveId":"118212859011399680"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":102,"tradeOrder":"DL10220200320118234673007689728","money":19,"balance":499457,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-20 21:37:09","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200320/9c199567c6e0a7fa_l.jpg","orderState":1,"receiveId":"118212859011399680"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":101,"tradeOrder":"JP10120200320118228329429073920","money":19,"balance":499476,"bizTypeName":"添加好友赠送礼物","userId":"114545907432816640","adddate":"2020-03-20 21:11:57","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/user/picture/20200320/9c199567c6e0a7fa_l.jpg","orderState":2,"receiveId":"118212859011399680"},{"bizType":201,"tradeOrder":"HD20120200320118210292177174528","money":20,"balance":499475,"restSize":2,"redState":"","bizTypeName":"每日红包","redid":"118210292177174528","packetSize":2,"userId":"114545907432816640","adddate":"2020-03-20 20:00:16","orderState":""},{"bizType":201,"tradeOrder":"HD20120200319117847907893510144","money":20,"balance":499375,"restSize":2,"redState":"","bizTypeName":"每日红包","redid":"117847907893510144","packetSize":2,"userId":"114545907432816640","adddate":"2020-03-19 20:00:17","orderState":""},{"bizType":202,"tradeOrder":"HQ20220200319117833885945036800","money":100,"balance":499395,"restSize":1,"redState":"","bizTypeName":"俱乐部红包","redid":"117833885945036800","packetSize":1,"userId":"114545907432816640","adddate":"2020-03-19 19:04:34","orderState":""},{"bizType":201,"tradeOrder":"HD20120200318117485452210208768","money":20,"balance":498796,"restSize":2,"redState":"","bizTypeName":"每日红包","redid":"117485452210208768","packetSize":2,"userId":"114545907432816640","adddate":"2020-03-18 20:00:01","orderState":""},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907915126.png","bizType":102,"tradeOrder":"DL10220200318117465567648153600","money":59,"balance":498816,"bizTypeName":"单聊赠送礼物","userId":"114545907432816640","adddate":"2020-03-18 18:41:00","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/static/VipHead.png","orderState":3,"receiveId":"113079273589440512"},{"bizType":202,"tradeOrder":"HQ20220200317117147896549343232","money":1000,"balance":498875,"restSize":0,"redState":"","bizTypeName":"俱乐部红包","redid":"117147896549343232","packetSize":1,"userId":"114545907432816640","adddate":"2020-03-17 21:38:42","orderState":""},{"bizType":201,"tradeOrder":"HD20120200317117123066076856320","money":10,"balance":499865,"restSize":1,"redState":"","bizTypeName":"每日红包","redid":"117123066076856320","packetSize":1,"userId":"114545907432816640","adddate":"2020-03-17 20:00:02","orderState":""},{"bizType":201,"tradeOrder":"HD20120200316116760678571966464","money":10,"balance":499751,"restSize":1,"redState":"","bizTypeName":"每日红包","redid":"116760678454525952","packetSize":1,"userId":"114545907432816640","adddate":"2020-03-16 20:00:02","orderState":""},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":101,"tradeOrder":"JP10120200316116720140523012096","money":19,"balance":499761,"bizTypeName":"添加好友赠送礼物","userId":"114545907432816640","adddate":"2020-03-16 17:18:57","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/static/VipHead.png","orderState":3,"receiveId":"113476265633251328"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":101,"tradeOrder":"JP10120200316116719462119505920","money":19,"balance":499780,"bizTypeName":"添加好友赠送礼物","userId":"114545907432816640","adddate":"2020-03-16 17:16:15","giftSize":1,"receiveIdHead":"http://iyfile.tiantiancaidian.com/static/VipHead.png","orderState":3,"receiveId":"113098461569744896"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":101,"tradeOrder":"JP10120200316116717370264911872","money":19,"balance":499799,"bizTypeName":"添加好友赠送礼物","userId":"114545907432816640","adddate":"2020-03-16 17:07:56","giftSize":1,"orderState":3,"receiveId":"111716400871112704"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":101,"tradeOrder":"JP10120200316116717296860397568","money":19,"balance":499818,"bizTypeName":"添加好友赠送礼物","userId":"114545907432816640","adddate":"2020-03-16 17:07:39","giftSize":1,"orderState":3,"receiveId":"110898691514171392"},{"getPictureUrl":"http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png","bizType":101,"tradeOrder":"JP10120200316116717151112527872","money":19,"balance":499837,"bizTypeName":"添加好友赠送礼物","userId":"114545907432816640","adddate":"2020-03-16 17:07:04","giftSize":1,"orderState":3,"receiveId":"110896272277372928"}]
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
         * getPictureUrl : http://iyoufile.tiantiancaidian.com/admin/20200113/20200113_1578907763054.png
         * bizType : 102
         * tradeOrder : DL10220200321118519507416383488
         * money : 19.0
         * balance : 3508322.0
         * bizTypeName : 单聊赠送礼物
         * userId : 114545907432816640
         * adddate : 2020-03-21 16:28:59
         * giftSize : 1
         * receiveIdHead : http://iyfile.tiantiancaidian.com/static/VipHead.png
         * orderState : 1
         * receiveId : 113079273589440512
         * restSize : 2
         * redState :
         * redid : 118210292177174528
         * packetSize : 2
         */

        private String receiveNickName;
        private String getPictureUrl;
        private String bizType;
        private String tradeOrder;
        private int money;
        private String balance;
        private String bizTypeName;
        private String userId;
        private String adddate;
        private String giftSize;
        private String receiveIdHead;
        private String orderState;
        private String receiveId;
        private String restSize;
        private String redState;
        private String redid;
        private String packetSize;


        public String getReceiveNickName() {
            return receiveNickName;
        }

        public void setReceiveNickName(String receiveNickName) {
            this.receiveNickName = receiveNickName;
        }

        public String getGetPictureUrl() {
            return getPictureUrl;
        }

        public void setGetPictureUrl(String getPictureUrl) {
            this.getPictureUrl = getPictureUrl;
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

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
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

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }

        public String getGiftSize() {
            return giftSize;
        }

        public void setGiftSize(String giftSize) {
            this.giftSize = giftSize;
        }

        public String getReceiveIdHead() {
            return receiveIdHead;
        }

        public void setReceiveIdHead(String receiveIdHead) {
            this.receiveIdHead = receiveIdHead;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public String getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(String receiveId) {
            this.receiveId = receiveId;
        }

        public String getRestSize() {
            return restSize;
        }

        public void setRestSize(String restSize) {
            this.restSize = restSize;
        }

        public String getRedState() {
            return redState;
        }

        public void setRedState(String redState) {
            this.redState = redState;
        }

        public String getRedid() {
            return redid;
        }

        public void setRedid(String redid) {
            this.redid = redid;
        }

        public String getPacketSize() {
            return packetSize;
        }

        public void setPacketSize(String packetSize) {
            this.packetSize = packetSize;
        }
    }
}
