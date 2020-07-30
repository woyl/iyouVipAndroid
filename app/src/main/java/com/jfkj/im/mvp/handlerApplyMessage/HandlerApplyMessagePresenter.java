package com.jfkj.im.mvp.handlerApplyMessage;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class HandlerApplyMessagePresenter extends BasePresenter<HandlerApplyMessageView> {

    public HandlerApplyMessagePresenter(HandlerApplyMessageView view){
        attachView(view);
    }

    public void handlerApplyMessage(String applyId,String orderId,String tradeOrderNo,String type){

        Map<String,String>  map=new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.APPLYID,applyId);
        map.put(Utils.ORDERID,orderId);
        map.put(Utils.TRADEORDERNO,tradeOrderNo);
        map.put(Utils.TYPE,type);
        addSubscription(apiStores.handlerApplyMessage(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

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
