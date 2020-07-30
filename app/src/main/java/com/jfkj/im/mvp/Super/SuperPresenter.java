package com.jfkj.im.mvp.Super;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class SuperPresenter extends BasePresenter<SuperView> {

    public SuperPresenter(SuperView superView) {
        attachView(superView);
    }

    public void Super(String groupid,  String isSuper) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GROUPID, groupid);
        if (isSuper.trim().length() > 0) {
            map.put(Utils.ISSUPER, isSuper);
        }

        addSubscription(apiStores.updateGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.SuperSuccess(model);
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
