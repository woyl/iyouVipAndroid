package com.jfkj.im.Bean;

public class Systemnotificationbean {

    /**
     * body : {"activeUrl":"","avatarImage":"http://iyfile.tiantiancaidian.com/user/picture/20200316/c59a85a9a19dd4f6_s.jpg","subTitle":"测试聊天1在广场发了一个3500.0金币的广场红包，快去抢>>","time":"1585304703269","title":"广场红包"}
     * sendType : 1
     * type : 10
     */

    private BodyBean body;
    private String sendType;
    private String type;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class BodyBean {
        /**
         * activeUrl :
         * avatarImage : http://iyfile.tiantiancaidian.com/user/picture/20200316/c59a85a9a19dd4f6_s.jpg
         * subTitle : 测试聊天1在广场发了一个3500.0金币的广场红包，快去抢>>
         * time : 1585304703269
         * title : 广场红包
         */

        private String activeUrl;
        private String avatarImage;
        private String subTitle;
        private String time;
        private String title;
        private String id="";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActiveUrl() {
            return activeUrl;
        }

        public void setActiveUrl(String activeUrl) {
            this.activeUrl = activeUrl;
        }

        public String getAvatarImage() {
            return avatarImage;
        }

        public void setAvatarImage(String avatarImage) {
            this.avatarImage = avatarImage;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
