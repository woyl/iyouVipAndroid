package com.jfkj.im.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SendGifBean implements Parcelable {

    /**
     * 礼物订单ID
     */
    String orderId;

    /**
     * 交易记录编号
     */
    String tradeOrderNo;

    /**
     * 礼物Id
     */
    String giftId;

    /**
     * 礼物数量
     */
    String giftSize;
    /**
     * 总金额
     */
    String money;
    /**
     * 提示内容
     */
    String tips;
    /**
     * 礼物图片
     */
    String pictureUrl;
    /**
     * 礼物名称
     */
    String name;

    public SendGifBean() {
    }

    public SendGifBean(Parcel in) {
        orderId = in.readString();
        tradeOrderNo = in.readString();
        giftId = in.readString();
        giftSize = in.readString();
        money = in.readString();
        tips = in.readString();
        pictureUrl = in.readString();
        name = in.readString();
    }

    public static final Creator<SendGifBean> CREATOR = new Creator<SendGifBean>() {
        @Override
        public SendGifBean createFromParcel(Parcel in) {
            return new SendGifBean(in);
        }

        @Override
        public SendGifBean[] newArray(int size) {
            return new SendGifBean[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeOrderNo() {
        return tradeOrderNo;
    }

    public void setTradeOrderNo(String tradeOrderNo) {
        this.tradeOrderNo = tradeOrderNo;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftSize() {
        return giftSize;
    }

    public void setGiftSize(String giftSize) {
        this.giftSize = giftSize;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(tradeOrderNo);
        dest.writeString(giftId);
        dest.writeString(giftSize);
        dest.writeString(money);
        dest.writeString(tips);
        dest.writeString(pictureUrl);
        dest.writeString(name);
    }
}
