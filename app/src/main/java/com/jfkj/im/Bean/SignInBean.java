package com.jfkj.im.Bean;

import java.util.List;

public class SignInBean {


    /**
     * code : 200
     * message : 查询成功
     * data : {"isEnd":0,"userList":[{"head":"http://iyfile.tiantiancaidian.com/user/picture/20200320/68b5e3aeed6fa710_l.jpg","vipLevel":1,"note":"我会准时到","nickName":"雪花女神龙","userId":114853707593678848,"istate":0}]}
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
        /**
         * isEnd : 0
         * userList : [{"head":"http://iyfile.tiantiancaidian.com/user/picture/20200320/68b5e3aeed6fa710_l.jpg","vipLevel":1,"note":"我会准时到","nickName":"雪花女神龙","userId":114853707593678848,"istate":0}]
         */

        private int isEnd;
        private List<UserListBean> userList;

        public int getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(int isEnd) {
            this.isEnd = isEnd;
        }

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class UserListBean {
            /**
             * head : http://iyfile.tiantiancaidian.com/user/picture/20200320/68b5e3aeed6fa710_l.jpg
             * vipLevel : 1
             * note : 我会准时到
             * nickName : 雪花女神龙
             * userId : 114853707593678848
             * istate : 0
             */

            private String head;
            private int vipLevel;
            private String note;
            private String nickName;
            private String userId;
            private int istate;

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public int getVipLevel() {
                return vipLevel;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getIstate() {
                return istate;
            }

            public void setIstate(int istate) {
                this.istate = istate;
            }
        }
    }
}
