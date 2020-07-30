package com.jfkj.im.mvp.RedPacketDetails;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class RedPacketDetailsPresenter extends BasePresenter<RedPacketDetailsView> {

    public   RedPacketDetailsPresenter(RedPacketDetailsView view){
        attachView(view);
    }
    //加载红包记录详情
    public void loadRedPacketDetails(String redId){
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());



        map.put(Utils.REDID, redId);

        addSubscription(apiStores.loadRedPacketDetails(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.loadRedPacketDetails(model.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    //加载红包领取记录列表
    public void loadRedPacketReceiveList(String redId,String sort){
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
            map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.REDID, redId);
        map.put(Utils.PAGESIZE,10+"");
        if(sort.trim().length()>0){
            map.put(Utils.SORT,sort);
        }

        addSubscription(apiStores.loadRedPacketReceiveList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.loadRedPacketReceiveList(model.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
