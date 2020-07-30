package com.jfkj.im.entity;

import android.os.Parcel;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/16
 * </pre>
 */
public class ResponseFileUrl extends BaseResponse {
    /**
     * data : {"fileUrl":"iyoufile.198ty.com/chat/voice/20191213/6faa2c4cabb7f69c.mp3"}
     */

    private DataBean data;

    protected ResponseFileUrl(Parcel in) {
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
         * fileUrl : iyoufile.198ty.com/chat/voice/20191213/6faa2c4cabb7f69c.mp3
         */

        private String fileUrl;
        private String fileUrls;

        public String getFileUrls() {
            return fileUrls;
        }

        public void setFileUrls(String fileUrls) {
            this.fileUrls = fileUrls;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }


    }

}
