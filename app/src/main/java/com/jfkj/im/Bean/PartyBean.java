package com.jfkj.im.Bean;

public class PartyBean {

    /**
     * maxNumber : 5
     * cadddate : 2020-04-05 12:11:36
     * place : B茶馆
     * title : 打麻将
     * userId : 119667334968377344
     * istate : 0
     * partyId : 119667334968377344
     */

    private String maxNumber;
    private String cadddate = "";
    private String place;
    private String title;
    private String userId;
    private String istate;
    private String partyId;
    private String pagePhoto;
    private String redType = "";


    public String getPagePhoto() {
        return pagePhoto;
    }

    public void setPagePhoto(String pagePhoto) {
        this.pagePhoto = pagePhoto;
    }

    public String getRedType() {
        return redType;
    }

    public void setRedType(String redType) {
        this.redType = redType;
    }

    public String getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(String maxNumber) {
        this.maxNumber = maxNumber;
    }

    public String getCadddate() {
        return cadddate;
    }

    public void setCadddate(String cadddate) {
        this.cadddate = cadddate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIstate() {
        return istate;
    }

    public void setIstate(String istate) {
        this.istate = istate;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
