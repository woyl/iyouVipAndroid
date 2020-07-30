package com.jfkj.im.event;

import com.jfkj.im.Bean.GroupBean;

public class GroupInfoEvent {
    private GroupBean.DataBean.ArrayBean arrayBean;

    public GroupInfoEvent(GroupBean.DataBean.ArrayBean arrayBean) {
        this.arrayBean = arrayBean;
    }

    public GroupBean.DataBean.ArrayBean getArrayBean() {
        return arrayBean;
    }

    public void setArrayBean(GroupBean.DataBean.ArrayBean arrayBean) {
        this.arrayBean = arrayBean;
    }
}
