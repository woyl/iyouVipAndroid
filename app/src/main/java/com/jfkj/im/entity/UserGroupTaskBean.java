package com.jfkj.im.entity;


import java.util.List;

/**
 * 我的俱乐部列表
 */
public class UserGroupTaskBean  {



    /**
     * code : 200
     * message : 查询成功
     * data : [{"groupName":"4745","redPacketNum":10,"isSuper":0,"groupId":"113376619091066880","groupHead":"https://b-ssl.duitang.com/uploads/item/201802/20/20180220165946_RiGPS.thumb.700_0.jpeg","ownerId":"113361629546151936"},{"groupName":"cc","redPacketNum":10,"isSuper":0,"groupId":"113363751092224000","groupHead":"https://b-ssl.duitang.com/uploads/item/201802/20/20180220165946_RiGPS.thumb.700_0.jpeg","ownerId":"113361629546151936"},{"groupName":"mmm","redPacketNum":10,"isSuper":0,"groupId":"113363286925377536","groupHead":"https://b-ssl.duitang.com/uploads/item/201802/20/20180220165946_RiGPS.thumb.700_0.jpeg","ownerId":"113361629546151936"}]
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
         * groupName : 4745
         * redPacketNum : 10
         * isSuper : 0
         * groupId : 113376619091066880
         * groupHead : https://b-ssl.duitang.com/uploads/item/201802/20/20180220165946_RiGPS.thumb.700_0.jpeg
         * ownerId : 113361629546151936
         */

        private String groupName;
        private int redPacketNum;
        private int isSuper;
        private String groupId;
        private String groupHead;
        private String ownerId;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getRedPacketNum() {
            return redPacketNum;
        }

        public void setRedPacketNum(int redPacketNum) {
            this.redPacketNum = redPacketNum;
        }

        public int getIsSuper() {
            return isSuper;
        }

        public void setIsSuper(int isSuper) {
            this.isSuper = isSuper;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupHead() {
            return groupHead;
        }

        public void setGroupHead(String groupHead) {
            this.groupHead = groupHead;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }
    }
}
