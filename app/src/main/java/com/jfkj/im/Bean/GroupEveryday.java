package com.jfkj.im.Bean;

public class GroupEveryday {
    private String taskcontent;
    private String itaskid;
    private String itasktype;
    private String surplustime;
    private String submitType;//0 未提交，1提交

    public String getSubmitType() {
        return submitType;
    }

    public void setSubmitType(String submitType) {
        this.submitType = submitType;
    }

    public String getTaskcontent() {
        return taskcontent;
    }

    public void setTaskcontent(String taskcontent) {
        this.taskcontent = taskcontent;
    }

    public String getItaskid() {
        return itaskid;
    }

    public void setItaskid(String itaskid) {
        this.itaskid = itaskid;
    }

    public String getItasktype() {
        return itasktype;
    }

    public void setItasktype(String itasktype) {
        this.itasktype = itasktype;
    }

    public String getSurplustime() {
        return surplustime;
    }

    public void setSurplustime(String surplustime) {
        this.surplustime = surplustime;
    }
}
