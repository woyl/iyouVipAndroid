package com.jfkj.im.Bean;

import android.os.Parcel;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.jfkj.im.entity.BaseResponse;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/3/24
 * </pre>
 */
public class AppVersion extends BaseResponse {


    /**
     * data : {"appVersion":"20200322","downloadurl":"http://iyfile.tiantiancaidian.com/versionUpdate/20200323.apk","message":"1版本升级dsx\n 2;更新交友","isCoerce":0}
     */

    private DataBean data;

    protected AppVersion(Parcel in) {
        super(in);
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appVersion : 20200322
         * downloadurl : http://iyfile.tiantiancaidian.com/versionUpdate/20200323.apk
         * message : 1版本升级dsx
         2;更新交友
         * isCoerce : 0
         */

        private String appVersion;
        private String downloadurl;
        @SerializedName("message")
        private String messageX;
        private String isCoerce;


        public boolean isForceUpdate(){
            return !TextUtils.isEmpty(isCoerce) && "1".equals(isCoerce);
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }

        public String getMessageX() {
            return messageX;
        }

        public void setMessageX(String messageX) {
            this.messageX = messageX;
        }

        public String getIsCoerce() {
            return isCoerce;
        }

        public void setIsCoerce(String isCoerce) {
            this.isCoerce = isCoerce;
        }
    }
}
