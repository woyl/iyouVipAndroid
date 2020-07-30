package com.jfkj.im.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NewfriendBean {

    private String head_url = "";
    private String user_name = "";
    private String hint_message = "";
    private String type = "0";
    private String sendid = "";
    private String orderId = "";
    private String tradeOrderNo = "";
    private String giftsize = "";
    private String giftidimg = "";
    private String visitorname;
    private String number;
    private String visitorid;
    private String state;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVisitorid() {
        return visitorid;
    }

    public void setVisitorid(String visitorid) {
        this.visitorid = visitorid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVisitorname() {
        return visitorname;
    }

    public void setVisitorname(String visitorname) {
        this.visitorname = visitorname;
    }

    public String getGiftidimg() {
        return giftidimg;
    }

    public void setGiftidimg(String giftidimg) {
        this.giftidimg = giftidimg;
    }


    public String getGiftsize() {
        return giftsize;
    }

    public void setGiftsize(String giftsize) {
        this.giftsize = giftsize;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHint_message() {
        return hint_message;
    }

    public void setHint_message(String hint_message) {
        this.hint_message = hint_message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

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
}
