package com.jfkj.im.mvp.LoadUserGroupList;

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
//加载俱乐部页面的数据
public class LoadUserGroupListPresenter extends BasePresenter<LoadUserGroupListView> {

    public LoadUserGroupListPresenter(LoadUserGroupListView loadUserGroupListView) {
        attachView(loadUserGroupListView);
    }

    public void getdata(String pageno,String groupName,String type,String sort) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION,Utils.getVersionCode()+"");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.PAGESIZE, 10 + "");
        map.put(Utils.PAGENO, pageno);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID()+"");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
         if(groupName.trim().length()>0){
             map.put(Utils.GROUPNAME, groupName);
         }
        map.put(Utils.TYPE, type);
        if(sort.trim().length()>0){
            map.put(Utils.SORT, sort);
        }


        addSubscription(apiStores.loadRecommendGroupList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.LoadUserGroupSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.LoadUserGroupSuccessfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
