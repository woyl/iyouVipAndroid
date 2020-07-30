package com.jfkj.im.mvp.RedPacket;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class RedPacketPresenter extends BasePresenter<RedPacketView> {

    public RedPacketPresenter(RedPacketView redPacketView){
        attachView(redPacketView);
    }
    public void RedPacket(String groupid){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GROUPID, groupid);

        addSubscription(apiStores.loadGroupInfo(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.RedPacketSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void sendRedPacked(String groupId, String money, String size, String sendword) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.GROUPID, groupId);
        map.put(Utils.MONEY, money);
        map.put(Utils.SIZE, size);
        map.put(Utils.SENDWORD, sendword);

        addSubscription(apiStores.sendRedPacked(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    String string = model.string();
                    mvpView.sendRedPacked(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void sendSquareRedPacket(String money, String size, String sendWord) {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.MONEY, money);
        map.put(Utils.SIZE, size);
        map.put(Utils.SENDWORD, sendWord);

        addSubscription(apiStores.sendSquareRedPacket(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.sendSquareRedPacketSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

}
