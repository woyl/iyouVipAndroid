package com.jfkj.im.mvp.UpGradeSuperGroup;


import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.http.PUT;

public class UpGradeSuperGroupPresenter extends BasePresenter<UpGradeSuperGroupView> {

    public UpGradeSuperGroupPresenter(UpGradeSuperGroupView view) {
        attachView(view);
    }

    public void UpGradeSuperGroup(String groupid,String isSuper) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.APP_CHANNEL);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupid);
        if (isSuper.trim().length() > 0) {
            map.put(Utils.ISSUPER, isSuper);
        }
        addSubscription(apiStores.updateGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.UpGradeSuperGroupSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
    public void loadGroupInfo(String groupid){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.APP_CHANNEL);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupid);

        addSubscription(apiStores.loadGroupInfo(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loadGroupInfoSuccess(model);
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
