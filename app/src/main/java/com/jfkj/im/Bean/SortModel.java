package com.jfkj.im.Bean;

public class SortModel {

    private String name;
    private String letters;//显示拼音的首字母
    private long userid;
    private boolean isselect = false;
    private boolean isGroupMember = false;
    private String head_url;
    private int noDisturb;
    private int top;
    private int vipLevel;

    public boolean isGroupMember() {
        return isGroupMember;
    }

    public void setGroupMember(boolean groupMember) {
        isGroupMember = groupMember;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(int noDisturb) {
        this.noDisturb = noDisturb;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public boolean isIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        //        俱乐部邀请好友闪退
        return letters == null ? "Z":letters.length()>=1?letters:"Z";
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }


}
