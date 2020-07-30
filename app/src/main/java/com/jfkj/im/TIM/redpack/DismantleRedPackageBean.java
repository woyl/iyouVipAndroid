package com.jfkj.im.TIM.redpack;

import android.os.Parcel;
import android.os.Parcelable;

public class DismantleRedPackageBean implements Parcelable {

    /**
     * redState : 2
     * msgId : 2020030911150753462e6088d047a095a8749222732638
     * receiveId : 114091865141280768
     *
     * "receiveId": "sbbsaa123456879794",领取红包记录ID <string>
     * "msgId": "sbbsaa123456879794",领取红包提醒的消息ID <string>
     * "redState": "1"红包状态 1 正在领取 2 已领取完 3过期退款 <string>
     */

    private int redState;
    private String msgId;
    private String receiveId;

    public DismantleRedPackageBean() {
    }

    protected DismantleRedPackageBean(Parcel in) {
        redState = in.readInt();
        msgId = in.readString();
        receiveId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(redState);
        dest.writeString(msgId);
        dest.writeString(receiveId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DismantleRedPackageBean> CREATOR = new Creator<DismantleRedPackageBean>() {
        @Override
        public DismantleRedPackageBean createFromParcel(Parcel in) {
            return new DismantleRedPackageBean(in);
        }

        @Override
        public DismantleRedPackageBean[] newArray(int size) {
            return new DismantleRedPackageBean[size];
        }
    };

    public int getRedState() {
        return redState;
    }

    public void setRedState(int redState) {
        this.redState = redState;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }
}
