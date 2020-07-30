package com.jfkj.im.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class GroupMessCenterLitepal extends LitePalSupport {

    @Column(unique = true)
    private String groupId;

    @Column(nullable = false)
    private String applyStr;

    /**0申请 1。拒绝 2 同意*/
    @Column(nullable = false)
    private String state;

    private String refuseStr;

    @Column(nullable = false)
    private long addTime;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userHeadUrl;

    @Column(nullable = false)
    private String handlerId;

    @Column(nullable = false)
    private String handlerName;

    @Column(nullable = false)
    private String handlerHeadUrl;

    @Column(nullable = false,defaultValue = "1")
    private String vipGrade;

    @Column(nullable = false)
    private String groupName;

    public String getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(String vipGrade) {
        this.vipGrade = vipGrade;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getApplyStr() {
        return applyStr;
    }

    public void setApplyStr(String applyStr) {
        this.applyStr = applyStr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRefuseStr() {
        return refuseStr;
    }

    public void setRefuseStr(String refuseStr) {
        this.refuseStr = refuseStr;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getHandlerHeadUrl() {
        return handlerHeadUrl;
    }

    public void setHandlerHeadUrl(String handlerHeadUrl) {
        this.handlerHeadUrl = handlerHeadUrl;
    }
}
