package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/23
 * </pre>
 */
public class BaseBean<T> implements Parcelable {
    public String code;
    public String message;
    public T data;

    protected BaseBean(Parcel in) {
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel in) {
            return new BaseBean(in);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
