package com.jfkj.im.entity;

import android.os.Parcel;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/10
 * </pre>
 */
public class AddFriend extends BaseResponse {
    /**
     * data : {"msgId":"2019120211075078056be9c7b94fe5ae8de60cae5fe0a5"}
     */

    private DataBean data=null;

    protected AddFriend(Parcel in) {
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
         * msgId : 2019120211075078056be9c7b94fe5ae8de60cae5fe0a5
         */

        private String msgId=null;

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }
    }
}
