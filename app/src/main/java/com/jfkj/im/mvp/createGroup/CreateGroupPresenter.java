package com.jfkj.im.mvp.createGroup;

import com.jfkj.im.App;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class CreateGroupPresenter extends BasePresenter<CreateGroupView> {

    public CreateGroupPresenter(CreateGroupView createGroupView) {
        attachView(createGroupView);
    }

    public void creategroup(String groupName, String notice, String redNum, String sendHour) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID + "");
        map.put(Utils.GROUPNAME, groupName);
        map.put(Utils.NOTICE, notice);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID()+"");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put("redNum", redNum);
        map.put("sendHour", sendHour);

        addSubscription(apiStores.createGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.CreateGroupSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.CreateGroupfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void updateGroup( String groupid,String sendHour) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID + "");
        map.put(Utils.GROUPID, groupid);
        map.put("sendHour", sendHour);

        addSubscription(apiStores.updateGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.updateGroupSuccess(model.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.CreateGroupfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
