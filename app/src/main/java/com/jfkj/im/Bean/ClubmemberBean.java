package com.jfkj.im.Bean;

import java.util.List;

public class ClubmemberBean {


    /**
     * code : 200
     * message : 操作成功
     * data : {"array":[{"head":"http://iyoufile.198ty.com//1/76436110686945280/76436110686945280_1574745931370.png","isOwner":1,"nickName":"测试号_13700000001","groupId":"84044210679971840","sort":"1576559788250","userId":"76743604952891392","memberId":"84044210692554752"}]}
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
             * groupId : 84044210679971840
             * sort : 1576559788250
             * userId : 76743604952891392
             * memberId : 84044210692554752
             */

            private String head;
            private int isOwner;
            private String nickName;
            private String groupId;
            private String sort;
            private String userId;
            private String memberId;
            private String adddelete;
            private int  vipLevel;

            public int getVipLevel() {
                return vipLevel;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            public String getAdddelete() {
                return adddelete;
            }

            public void setAdddelete(String adddelete) {
                this.adddelete = adddelete;
            }

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
