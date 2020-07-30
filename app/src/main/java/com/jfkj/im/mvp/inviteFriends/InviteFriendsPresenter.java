package com.jfkj.im.mvp.inviteFriends;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.entity.InviteBean;
import com.jfkj.im.entity.InvteFriendBean;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.entity.inviteFriendAddressBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class InviteFriendsPresenter extends BasePresenter<InviteFriendsView> {

    public InviteFriendsPresenter(InviteFriendsView view){
        attachView(view);
    }
    public void InviteFriends(){
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.inviteFriends(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                String string = null;
                try {
                    string = model.string();
                    JSONObject jsonObject = new JSONObject(string);

                    InvteFriendBean userGroupTaskBean = JSON.parseObject(jsonObject.toString(), InvteFriendBean.class);
                    mvpView.InviteFriendsSuccess(userGroupTaskBean);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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



    public void inviteFriendTail(){
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.inviteFriendTail(map), new ApiCallback<InviteBean>() {
            @Override
            public void onSuccess(InviteBean model) {
                mvpView.inviteFriendTail(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }


    public void inviteFriendAddress(){
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.inviteFriendAddress(map), new ApiCallback<inviteFriendAddressBean>() {

            @Override
            public void onSuccess(inviteFriendAddressBean model) {
                mvpView.inviteFriendAddressS(model);
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
