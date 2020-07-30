package com.jfkj.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/18
 * </pre>
 */
public class BaseResponse implements Parcelable {

    public String code;
    public String message;

    public boolean isSuccess(){
        return "200".equals(code);
    }

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };

    public BaseResponse() {
    }

    protected BaseResponse(Parcel in) {
        code = in.readString();
        message = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(message);
    }
}
