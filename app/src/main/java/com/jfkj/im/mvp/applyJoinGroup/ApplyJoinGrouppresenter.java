package com.jfkj.im.mvp.applyJoinGroup;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class ApplyJoinGrouppresenter extends BasePresenter<ApplyJoinGroupView> {

    //这个是加群的数据请求
    public ApplyJoinGrouppresenter(ApplyJoinGroupView applyJoinGroupView) {
        attachView(applyJoinGroupView);
    }

    public void getdata(String groupid, String content) {
//applyJoinGroup
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GROUPID, groupid);
        map.put("content", content);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.applyJoinGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.getApplyJoinGroupSuucess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getApplyJoinGroupfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
