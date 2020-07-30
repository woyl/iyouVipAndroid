package com.jfkj.im.TIM.redpack;

import android.os.Parcel;
import android.os.Parcelable;

public class TestingBean implements Parcelable {
    private String serialNo;

    public TestingBean() {
    }

    protected TestingBean(Parcel in) {
        serialNo = in.readString();
    }

    public static final Creator<TestingBean> CREATOR = new Creator<TestingBean>() {
        @Override
        public TestingBean createFromParcel(Parcel in) {
            return new TestingBean(in);
        }

        @Override
        public TestingBean[] newArray(int size) {
            return new TestingBean[size];
        }
    };

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serialNo);
    }
}
