package com.jfkj.im.event;

import com.jfkj.im.entity.MineInfo;

import java.util.List;

public class MineCircleEvent {
    private List<MineInfo.DataBean.CircleArticleBean> isUpdate;

    public MineCircleEvent(List<MineInfo.DataBean.CircleArticleBean> isUpdate) {
        this.isUpdate = isUpdate;
    }

    public List<MineInfo.DataBean.CircleArticleBean> isUpdate() {
        return isUpdate;
    }

    public void setUpdate(List<MineInfo.DataBean.CircleArticleBean> update) {
        isUpdate = update;
    }
}
