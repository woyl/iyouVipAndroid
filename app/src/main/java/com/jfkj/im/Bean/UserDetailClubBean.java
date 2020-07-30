package com.jfkj.im.Bean;

import android.os.Parcel;

import com.jfkj.im.entity.BaseResponse;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/3/17
 * </pre>
 */
public class UserDetailClubBean extends BaseResponse {

    private List<UserDetailClubBean.DataBean> data;

    protected UserDetailClubBean(Parcel in) {
        super(in);
    }

    public List<UserDetailClubBean.DataBean> getData() {
        return data;
    }

    public void setData(List<UserDetailClubBean.DataBean> data) {
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
        private String redPacketNum;
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

        public String getRedPacketNum() {
            return redPacketNum;
        }

        public void setRedPacketNum(String redPacketNum) {
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
