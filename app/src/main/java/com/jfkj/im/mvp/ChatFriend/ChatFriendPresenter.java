package com.jfkj.im.mvp.ChatFriend;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class ChatFriendPresenter extends BasePresenter<ChatFriendView> {

    public ChatFriendPresenter(ChatFriendView chatFriendView) {
        attachView(chatFriendView);
    }

    public void chatfriendpresenter(String userId,String giftId, String size) {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.GIFTID, giftId);
        map.put(Utils.SIZE, size);
        map.put(Utils.USERID, userId);




        addSubscription(apiStores.giveFriendGift(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {

                    mvpView.ChatFriend(model.string());
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
  public  void loadUserVerifyInfo(String userid){
      Map<String, String> map = new HashMap();
      map.put(Utils.USERID, userid);
      map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
      map.put(Utils.OSNAME, Utils.ANDROID);
      map.put(Utils.CHANNEL, Utils.ANDROID);
      map.put(Utils.DEVICENAME,Utils.getdeviceName());
      map.put(Utils.DEVICEID, Utils.ANDROID);
      map.put(Utils.REQTIME, AppUtils.getReqTime());


        addSubscription(apiStores.getchatUserDetail(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.loadUserVerifyInfoSuccess(model.string());
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
