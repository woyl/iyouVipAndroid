package com.jfkj.im.mvp.Minecharacter;

import com.jfkj.im.App;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.ResponseBody;

public class MinecharacterPresenter extends BasePresenter<MinecharacterView> {

    public MinecharacterPresenter(MinecharacterView minecharacterView) {
        attachView(minecharacterView);
    }

    public void Minecharacter(String gameId) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGESIZE, 13 + "");
        map.put(Utils.GAMEID, gameId);
        map.put(Utils.PAGENO, 1 + "");

        Map<String, String> headmap = new HashMap<>();
        headmap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        headmap.put(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));

        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.base_url + "/square/loadUserAnswerList")
                .headers(headmap)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mvpView.MinecharacterSuccess(response);
                    }
                });
//        addSubscription(apiStores.loadUserAnswerList(map), new ApiCallback<ResponseBody>() {
//            @Override
//            public void onSuccess(ResponseBody model) {
//                try {
//                    mvpView.MinecharacterSuccess(model.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
    }

    public void loadSquareGameQuestion(String gameId) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GAMEID, gameId);

        addSubscription(apiStores.loadSquareGameQuestion(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loadSquareGameQuestionSuccess(model);
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
