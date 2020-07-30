package com.jfkj.im.Bean;

import java.util.List;

public class InvitationBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"msgId":"20200102095428ba72eb8cd94a4364be90a97f8e9d47ca","userId":"76751334233079808"},{"msgId":"202001020954289f1302c345de46018e1c1e35f2baf736","userId":"76751335294238720"},{"msgId":"20200102095428c96f361a97a34b66a1b941c1bfb513fc","userId":"76751328742735872"}]}
     */

    private String code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ArrayBean> array;

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

        public static class ArrayBean {
            /**
             * msgId : 20200102095428ba72eb8cd94a4364be90a97f8e9d47ca
             * userId : 76751334233079808
             */

            private String msgId;
            private String userId;

            public String getMsgId() {
                return msgId;
            }

            public void setMsgId(String msgId) {
                this.msgId = msgId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
