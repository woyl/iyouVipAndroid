package com.jfkj.im.event;

import com.jfkj.im.entity.UserGroupTaskBean;

public class GroupMyTaskEvent {

    private UserGroupTaskBean userGroupTaskBean;

    public GroupMyTaskEvent(UserGroupTaskBean userGroupTaskBean) {
        this.userGroupTaskBean = userGroupTaskBean;
    }

    public UserGroupTaskBean getUserGroupTaskBean() {
        return userGroupTaskBean;
    }

    public void setUserGroupTaskBean(UserGroupTaskBean userGroupTaskBean) {
        this.userGroupTaskBean = userGroupTaskBean;
    }
}
