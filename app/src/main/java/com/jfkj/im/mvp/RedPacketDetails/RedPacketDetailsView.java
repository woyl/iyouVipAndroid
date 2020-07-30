package com.jfkj.im.mvp.RedPacketDetails;

import com.jfkj.im.mvp.BaseView;

public interface RedPacketDetailsView extends BaseView {

    public void loadRedPacketDetails(String s);//加载红包记录详情
    public void loadRedPacketReceiveList(String s);//加载红包领取记录列表
}
