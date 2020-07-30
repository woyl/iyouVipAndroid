package com.jfkj.im.Bean;

import java.util.List;

public class DeletemembersBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","isOwner":1,"nickName":"测试号_13700000001","groupId":"82898599498874880","sort":"1576286653261","userId":"76743604952891392","memberId":"82898599515652096"},{"head":"http://iyoufile.198ty.com//1/311104/311104_1575885829224.png","isOwner":0,"nickName":"789","groupId":"82898599498874880","sort":"1576547579939","userId":"81217675350638592","memberId":"83993005278887936"},{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","isOwner":0,"nickName":"鸠摩智测试号_13700000097","groupId":"82898599498874880","sort":"1576547661711","userId":"76751343502491648","memberId":"83993348205182976"},{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","isOwner":0,"nickName":"测试号_13700000092","groupId":"82898599498874880","sort":"1576553418602","userId":"76751334233079808","memberId":"83993629676535808"}]}
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
             * head : http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png
             * isOwner : 1
             * nickName : 测试号_13700000001
             * groupId : 82898599498874880
             * sort : 1576286653261
             * userId : 76743604952891392
             * memberId : 82898599515652096
             */
            private int vipLevel;

            public int getVipLevel() {
                return vipLevel;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            private String head;
            private int isOwner;
            private String nickName;
            private String groupId;
            private String sort;
            private String userId;
            private String memberId;

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public int getIsOwner() {
                return isOwner;
            }

            public void setIsOwner(int isOwner) {
                this.isOwner = isOwner;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }
        }
    }
}
