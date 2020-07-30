package com.jfkj.im.mvp.Rank;

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

public class RankPresenter extends BasePresenter<RankView> {

    public RankPresenter(RankView rankView){
        attachView(rankView);
    }
    public void Rank(String type){
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.DEVICENAME,Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        mapHasp.put(Utils.TYPE,type);

        addSubscription(apiStores.getRanking(mapHasp), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {

                    mvpView.RankSuccess(model);
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
}
