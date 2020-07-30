package com.jfkj.im.Bean;

import java.util.List;

public class VipPrivilegeBean {


    /**
     * code : 200
     * message : 成功
     * data : {"vipLevel":1,"upgradeamount":1000,"vipList":[{"vipLevel":3,"content":"创建俱乐部"},{"vipLevel":6,"content":"自定义冒险游戏"},{"vipLevel":14,"content":"视频聊天"},{"vipLevel":15,"content":"不限好友人数"},{"vipLevel":20,"content":"隐藏在线状态"},{"vipLevel":50,"content":"隐藏地理位置"},{"vipLevel":100,"content":"开启超级俱乐部"}],"upgradeAmountNext":"49927706.30"}
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
         * vipLevel : 1
         * upgradeamount : 1000.0
         * vipList : [{"vipLevel":3,"content":"创建俱乐部"},{"vipLevel":6,"content":"自定义冒险游戏"},{"vipLevel":14,"content":"视频聊天"},{"vipLevel":15,"content":"不限好友人数"},{"vipLevel":20,"content":"隐藏在线状态"},{"vipLevel":50,"content":"隐藏地理位置"},{"vipLevel":100,"content":"开启超级俱乐部"}]
         * upgradeAmountNext : 49927706.30
         */

        private int vipLevel;
        private double upgradeamount;
        private double upgradeAmountNext;
        private List<VipListBean> vipList;

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public double getUpgradeamount() {
            return upgradeamount;
        }

        public void setUpgradeamount(double upgradeamount) {
            this.upgradeamount = upgradeamount;
        }

        public Double getUpgradeAmountNext() {
            return upgradeAmountNext;
        }

        public void setUpgradeAmountNext(Double upgradeAmountNext) {
            this.upgradeAmountNext = upgradeAmountNext;
        }

        public List<VipListBean> getVipList() {
            return vipList;
        }

        public void setVipList(List<VipListBean> vipList) {
            this.vipList = vipList;
        }

        public static class VipListBean {
            /**
             * vipLevel : 3
             * content : 创建俱乐部
             */

            private int vipLevel;
            private String content;

            public int getVipLevel() {
                return vipLevel;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
