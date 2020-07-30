package com.jfkj.im.mvp.Chat;

import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.mvp.BaseView;

import java.io.File;

import okhttp3.ResponseBody;

public interface ChatView extends BaseView {
    public void uploadImageSuccess(String responseBody);//上传图片

    public void uploadImagefail(String s);
    public void getUser(UserDetail userDetai);
    public void loadGroupInfoSuccess(ResponseBody userDetai);
    public void uploadaudioSuccess(String responseBody, String time, long mediasize);

    public void uploadaudiofail(String responseBody);

    public void downfileSuccess(File file, long time, String receive_head_url, String sendid,String name);//下载文件


    public void downVideoSuccess(File file, long time, String receive_head_url, String sendid);//下载文件

    public void downfilefail(String s);

   // public void sendRedPacked(String s);


    public void checkRedPacket(String s,int position,String RedId);//抢红包检测

    public void receiveRedPacket(String s,String redId);//拆红包





    public void receiveFriendGift(String s, String giftid, String tradeorderno);//领取礼物


    //视频
    public void uploadViewdeoSuccess(String s, String time, long mediasize, String mediapicture);

    //视频
    public void uploadVideoscreenshotsSuccess(String s);

    public void paySpeakOrder(ResponseBody s,String message);//支付钻石


//    public void sendSquareRedPacketSuccess(ResponseBody responseBody);//生成广场红包



    public void getSquareTipsSuccess(ResponseBody responseBody);
}
