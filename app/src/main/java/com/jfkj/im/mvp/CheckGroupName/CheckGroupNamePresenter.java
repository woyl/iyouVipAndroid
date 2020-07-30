package com.jfkj.im.mvp.CheckGroupName;

import com.jfkj.im.App;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class CheckGroupNamePresenter extends BasePresenter<CheckGroupNameView> {

    public CheckGroupNamePresenter(CheckGroupNameView checkGroupNameView) {
        attachView(checkGroupNameView);
    }

    public void CheckGroupName(String groupName) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID + "");
        map.put(Utils.GROUPNAME, groupName);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID()+"");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());

        addSubscription(apiStores.checkGroupName(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.CheckGroupNameSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.CheckGroupNamefail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
