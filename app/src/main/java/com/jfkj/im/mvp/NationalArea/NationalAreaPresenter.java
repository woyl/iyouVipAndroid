package com.jfkj.im.mvp.NationalArea;

import android.content.Context;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class NationalAreaPresenter extends BasePresenter<NationalAreaView> {


    public NationalAreaPresenter(NationalAreaView view) {
        attachView(view);
    }

    public void NationalArea(Context context) {
        OkGo.<String>get(ApiStores.baseJsonUrl+ApiStores.nationalareaurl)
                .tag(context)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mvpView.nationSuccess(response.body());
                    }
                });
    }
}
