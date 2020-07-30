package com.jfkj.im.mvp.RedPacket;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface RedPacketView extends BaseView {

    public void RedPacketSuccess(ResponseBody responseBody);


    public void sendRedPacked(String s);

    public void sendSquareRedPacketSuccess(ResponseBody responseBody);//生成广场红包
}
