package com.jfkj.im.mvp.ChatRoute;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class ChatRoutePresenter extends BasePresenter<ChatRouteView> {

    public ChatRoutePresenter(ChatRouteView getImView) {
        attachView(getImView);
    }

    public void getChatRoute() {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

        addSubscription(apiStores.getChatRoute(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.GetImSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.GetImfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
