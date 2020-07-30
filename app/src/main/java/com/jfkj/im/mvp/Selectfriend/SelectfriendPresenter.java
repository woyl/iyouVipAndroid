package com.jfkj.im.mvp.Selectfriend;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class SelectfriendPresenter extends BasePresenter<SelectfriendView> {

    public SelectfriendPresenter(SelectfriendView selectfriendView) {
        attachView(selectfriendView);
    }

    //加载好友列表
    public void selectfriend() {
        TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
              //  mvpView.Selectfriendsuccess(timFriends);
            }
        });

    }

    //删除好友
    public void deleteFriend(String userIds) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.USERIDS, userIds);
        addSubscription(apiStores.deleteFriend(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.deletefriendsuccess(model,userIds);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.deletefriendfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    //  群主删除群成员
    public void delGroupMember(String userIds, String groupid) {
        mvpView.showLoading();

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.USERIDS, userIds);
        map.put(Utils.GROUPID, groupid);
        addSubscription(apiStores.delGroupMember(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.delGroupMembersuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.deletefriendfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    //要请好友进群
    public void inviteJoinGroup(String userIds, String groupid) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.USERIDS, userIds);
        map.put(Utils.GROUPID, groupid);
        addSubscription(apiStores.inviteJoinGroup(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.invitesuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.invitefail(msg);
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
        map.put(Utils.PAGESIZE, 10 + "");
//       if (sort.trim().length() > 0) {
//           map.put(Utils.SORT, sort);
//       }
        addSubscription(apiStores.loadGroupMemberList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

                mvpView.loadGroupMemberListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

                mvpView.loadGroupMemberListfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
