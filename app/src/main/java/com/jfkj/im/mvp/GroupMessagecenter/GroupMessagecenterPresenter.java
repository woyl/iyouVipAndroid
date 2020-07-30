package com.jfkj.im.mvp.GroupMessagecenter;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class GroupMessagecenterPresenter extends BasePresenter<GroupMessagecenterView> {

    public GroupMessagecenterPresenter(GroupMessagecenterView view) {
        attachView(view);
    }

    public void loadmessagecenter(String userId, String groupId, String msgId, String type) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.USERID, userId);
        map.put(Utils.GROUPID, groupId);
        map.put(Utils.MSGID, msgId);
        map.put(Utils.TYPE, type);
        addSubscription(apiStores.handlerInviteGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.handlerInviteGroupSuccess(model,userId);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.handlerInviteGroupfail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
    public void handlerJoinGroup(String applyid,String groupid,String msgid,String type){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.APPLYID,applyid);
        map.put(Utils.GROUPID,groupid);
        map.put(Utils.MSGID,msgid);
        map.put(Utils.TYPE,type);
        addSubscription(apiStores.handlerJoinGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.handlerJoinGroupSuccess(model.string(),applyid);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String msg) {
                mvpView.handlerJoinGroupfail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public void replyJoinGroupMessageSuccess(String applyid,String groupid,String msgid,String content){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.APPLYID,applyid);

        map.put(Utils.GROUPID,groupid);
        map.put(Utils.MSGID,msgid);
        map.put(Utils.CONTENT,content);
        addSubscription(apiStores.replyJoinGroupMessage(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.replyJoinGroupMessageSuccess(model.string(),applyid);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String msg) {
                mvpView.handlerJoinGroupfail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}

