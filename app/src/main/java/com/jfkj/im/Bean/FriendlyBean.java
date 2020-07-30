package com.jfkj.im.Bean;

import java.util.List;

public class FriendlyBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"isStop":0,"removeIds":["76751324493905920","76751330353348608","76751332815405056","76751339610177536","76751331745857536","76751342420361216","76751328742735872","76751326259707904","76751334233079808"],"clientVersion":"1582279203181","friendArray":[{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","vipLevel":3,"gender":1,"top":0,"noDisturb":1,"nickname":"测试号_13700000011","state":1,"topTime":"","userId":"76749489477517312"},{"head":"http://iyoufile.198ty.com//1/538593/538593_1577330147788.png","vipLevel":4,"gender":1,"top":0,"noDisturb":0,"nickname":"12_\\<\u20ac%#$&@758?","state":1,"topTime":"","userId":"87275393270284288"},{"head":"http://iyoufile.198ty.com/user/picture/20200117/ffce4fd6546d8f14_s.jpg","vipLevel":16,"gender":1,"top":0,"noDisturb":0,"nickname":"~12345678912345","state":1,"topTime":"1582273936872","userId":"76751335294238720"}]}
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
         * isStop : 0
         * removeIds : ["76751324493905920","76751330353348608","76751332815405056","76751339610177536","76751331745857536","76751342420361216","76751328742735872","76751326259707904","76751334233079808"]
         * clientVersion : 1582279203181
         * friendArray : [{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","vipLevel":3,"gender":1,"top":0,"noDisturb":1,"nickname":"测试号_13700000011","state":1,"topTime":"","userId":"76749489477517312"},{"head":"http://iyoufile.198ty.com//1/538593/538593_1577330147788.png","vipLevel":4,"gender":1,"top":0,"noDisturb":0,"nickname":"12_\\<\u20ac%#$&@758?","state":1,"topTime":"","userId":"87275393270284288"},{"head":"http://iyoufile.198ty.com/user/picture/20200117/ffce4fd6546d8f14_s.jpg","vipLevel":16,"gender":1,"top":0,"noDisturb":0,"nickname":"~12345678912345","state":1,"topTime":"1582273936872","userId":"76751335294238720"}]
         */

        private int isStop;
        private String clientVersion;
        private List<String> removeIds;
        private List<FriendArrayBean> friendArray;

        public int getIsStop() {
            return isStop;
        }

        public void setIsStop(int isStop) {
            this.isStop = isStop;
        }

        public String getClientVersion() {
            return clientVersion;
        }

        public void setClientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
        }

        public List<String> getRemoveIds() {
            return removeIds;
        }

        public void setRemoveIds(List<String> removeIds) {
            this.removeIds = removeIds;
        }

        public List<FriendArrayBean> getFriendArray() {
            return friendArray;
        }

        public void setFriendArray(List<FriendArrayBean> friendArray) {
            this.friendArray = friendArray;
        }

        public static class FriendArrayBean {
            /**
             * head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
             * vipLevel : 3
             * gender : 1
             * top : 0
             * noDisturb : 1
             * nickname : 测试号_13700000011
             * state : 1
             * topTime :
             * userId : 76749489477517312
             */

            private String head;
            private int vipLevel;
            private int gender;
            private int top;
            private int noDisturb;
            private String nickname;
            private int state;
            private String topTime;
            private String userId;

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

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getNoDisturb() {
                return noDisturb;
            }

            public void setNoDisturb(int noDisturb) {
                this.noDisturb = noDisturb;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getTopTime() {
                return topTime;
            }

            public void setTopTime(String topTime) {
                this.topTime = topTime;
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
