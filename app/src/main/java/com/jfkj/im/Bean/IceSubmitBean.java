package com.jfkj.im.Bean;

public class IceSubmitBean {

    /**
     * taskCount : 1
     * task : {"icollectionid":"130117925679005696","itaskid":"128194618239090690","cuserid":"117341190306791424","itype":2,"cadddate":"2020-04-22 16:36:57","url":"http://file.vipiyou.com/user/video/20200422/d2e4dafb8665dca4.mp4"}
     * imageUrl : http://file.vipiyou.com/user/video/20200422/d2e4dafb8665dca4.jpg
     */

    private String taskCount;
    private TaskBean task;
    private String imageUrl;

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public TaskBean getTask() {
        return task;
    }

    public void setTask(TaskBean task) {
        this.task = task;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static class TaskBean {
        /**
         * icollectionid : 130117925679005696
         * itaskid : 128194618239090690
         * cuserid : 117341190306791424
         * itype : 2
         * cadddate : 2020-04-22 16:36:57
         * url : http://file.vipiyou.com/user/video/20200422/d2e4dafb8665dca4.mp4
         * handle = 1
         * systemtype 1
         * groupid =
         */

        private String icollectionid;
        private String itaskid;
        private String cuserid;
        private String itype;
        private String cadddate;
        private String url;
        private String taskCount;
        private String imageUrl;

        private String groupid;
        private String handle;
        private String systemtype;

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public String getSystemtype() {
            return systemtype;
        }

        public void setSystemtype(String systemtype) {
            this.systemtype = systemtype;
        }

        public String getTaskCount() {
            return taskCount;
        }

        public void setTaskCount(String taskCount) {
            this.taskCount = taskCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getIcollectionid() {
            return icollectionid;
        }

        public void setIcollectionid(String icollectionid) {
            this.icollectionid = icollectionid;
        }

        public String getItaskid() {
            return itaskid;
        }

        public void setItaskid(String itaskid) {
            this.itaskid = itaskid;
        }

        public String getCuserid() {
            return cuserid;
        }

        public void setCuserid(String cuserid) {
            this.cuserid = cuserid;
        }

        public String getItype() {
            return itype;
        }

        public void setItype(String itype) {
            this.itype = itype;
        }

        public String getCadddate() {
            return cadddate;
        }

        public void setCadddate(String cadddate) {
            this.cadddate = cadddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
