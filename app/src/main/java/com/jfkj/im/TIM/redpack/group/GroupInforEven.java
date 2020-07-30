package com.jfkj.im.TIM.redpack.group;

import java.util.ArrayList;

public class GroupInforEven {
    private ArrayList<GroupMemberInfoBean> groupMemberInfoBeans;
    private boolean type;
    private GroupInfoBean groupInfoBean;;
    private String groupId;

    public GroupInforEven(ArrayList<GroupMemberInfoBean> groupMemberInfoBeans, boolean type, GroupInfoBean groupInfoBean,String groupId) {
        this.groupMemberInfoBeans = groupMemberInfoBeans;
        this.type = type;
        this.groupInfoBean = groupInfoBean;
        this.groupId = groupId;
    }

    public ArrayList<GroupMemberInfoBean> getGroupMemberInfoBeans() {
        return groupMemberInfoBeans;
    }

    public void setGroupMemberInfoBeans(ArrayList<GroupMemberInfoBean> groupMemberInfoBeans) {
        this.groupMemberInfoBeans = groupMemberInfoBeans;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public GroupInfoBean getGroupInfoBean() {
        return groupInfoBean;
    }

    public void setGroupInfoBean(GroupInfoBean groupInfoBean) {
        this.groupInfoBean = groupInfoBean;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
