package com.jfkj.im.mvp.Club;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.chat.GroupRecentMessageBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class ClubPresenter extends BasePresenter<ClubView> {

    public ClubPresenter(ClubView clubView) {
        attachView(clubView);
    }

    public void getdata(String sort) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGESIZE, 10 + "");
        if (sort.trim().length() > 0) {
            map.put(Utils.SORT, sort);
        }

        addSubscription(apiStores.loadUserGroupList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loadClubSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.loadClubfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void messagerecord(Activity mActivity ) {
        if (SPUtils.getInstance(mActivity).getString(Utils.APPID+Utils.RECENTGROUPMESSAGE) != null) {
            if (SPUtils.getInstance(mActivity).getString(Utils.APPID+Utils.RECENTGROUPMESSAGE).trim().length() > 0) {

                mvpView.messagerecord(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID+Utils.RECENTGROUPMESSAGE).trim(), InteractionBean.class));
            }
        }
    }
}


