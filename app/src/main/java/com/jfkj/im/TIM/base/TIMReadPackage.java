package com.jfkj.im.TIM.base;

import com.tencent.imsdk.TIMElem;

public class TIMReadPackage extends TIMElem {
    private String id;
    private String groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
