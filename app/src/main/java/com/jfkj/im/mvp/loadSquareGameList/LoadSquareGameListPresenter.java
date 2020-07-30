package com.jfkj.im.mvp.loadSquareGameList;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class LoadSquareGameListPresenter extends BasePresenter<LoadSquareGameListView> {

    public LoadSquareGameListPresenter(LoadSquareGameListView loadSquareGameListView){
        attachView(loadSquareGameListView);
    }
    public void  LoadSquareGameList(String sort){
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGESIZE, 10+"");

        if (sort.trim().length() > 0) {
            map.put(Utils.SORT, sort);
        }

        addSubscription(apiStores.loadSquareGameList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                  mvpView.LoadSquareGameListSuccess(model);
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
