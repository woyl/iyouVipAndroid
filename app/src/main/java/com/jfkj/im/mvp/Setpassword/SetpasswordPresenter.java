package com.jfkj.im.mvp.Setpassword;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class SetpasswordPresenter extends BasePresenter<SetpasswordView> {

    public SetpasswordPresenter(SetpasswordView view){
        attachView(view);
    }
    public void Setpassword(String password){
        Map<String,String>  map=new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL);
        map.put(Utils.APPVERSION,Utils.getVersionCode() + "");
        map.put(Utils.PASSWORD,password);

        addSubscription(apiStores.register(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

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
