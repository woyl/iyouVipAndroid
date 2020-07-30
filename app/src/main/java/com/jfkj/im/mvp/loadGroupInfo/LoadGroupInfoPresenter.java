package com.jfkj.im.mvp.loadGroupInfo;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;

import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class LoadGroupInfoPresenter extends BasePresenter<LoadGroupInfoView> {

    public LoadGroupInfoPresenter(LoadGroupInfoView loadGroupInfoView){
        attachView(loadGroupInfoView);
    }

    public LoadGroupInfoPresenter() {
    }

    public void LoadGroupInfo(String groupId){

        mvpView.showLoading();
        Map<String, String> map = new HashMap();
        map.put(Utils.OSNAME,Utils.ANDROID);
        map.put(Utils.CHANNEL,Utils.ANDROID);
        map.put(Utils.APPVERSION,Utils.getVersionCode()+"");
        map.put(Utils.GROUPID,groupId);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.loadGroupInfo(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.LoadGroupInfoSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.LoadGroupInfofail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void loadGroupMemberList(String groupid, String sort) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GROUPID, groupid);
        map.put(Utils.PAGESIZE,10+"");
        if (sort.trim().length() > 0) {
            map.put(Utils.SORT, sort);
        }
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME,AppUtils.getReqTime());

        addSubscription(apiStores.loadGroupMemberList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loadmenberSucess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.loadmenberfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


    public void loadGroupMemberInfo(String groupId){
        mvpView.showLoading();
        Map<String, String> map = new HashMap();
        map.put(Utils.OSNAME,Utils.ANDROID);
        map.put(Utils.CHANNEL,Utils.ANDROID);
        map.put(Utils.APPVERSION,Utils.getVersionCode()+"");
        map.put(Utils.GROUPID,groupId);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME,AppUtils.getReqTime());
        addSubscription(apiStores.loadGroupMemberInfo(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loadGroupMemberInfo(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.LoadGroupInfofail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


   public void updateGroup(String groupid,String top,String noDisturb,String groupname,String groupNotice,String redpacketnum,String sendHour,String isSuper){
       mvpView.showLoading();
       Map<String, String> map = new HashMap<>();
       map.put(Utils.OSNAME, Utils.ANDROID);
       map.put(Utils.CHANNEL, Utils.ANDROID);
       map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
       map.put(Utils.DEVICENAME, Utils.getdeviceName());
       map.put(Utils.DEVICEID, Utils.ANDROID);
       map.put(Utils.REQTIME, AppUtils.getReqTime());
       map.put(Utils.GROUPID, groupid);
       if (top.trim().length() > 0) {
           map.put(Utils.TOP, top);
       }
       if (noDisturb.trim().length() > 0) {
           map.put(Utils.NODISTURB, noDisturb);
       }
       if (groupname.trim().length() > 0) {
           map.put(Utils.GROUPNAME, groupname);
       }
       if (groupNotice.trim().length() > 0) {
           map.put(Utils.GROUPNOTICE, groupNotice);
       }
       if (redpacketnum.trim().length() > 0) {
           map.put(Utils.REDPACKETNUM, redpacketnum);
       }
       if (sendHour.trim().length() > 0) {
           map.put(Utils.SENDHOUR, sendHour);
       }
       if (isSuper.trim().length() > 0) {
           map.put(Utils.ISSUPER, isSuper);
       }

        addSubscription(apiStores.updateGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.updateGroupSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.updateGroupfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void quitGroup(String groupid){
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupid);

        addSubscription(apiStores.quitGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                 mvpView.quitGroupSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
