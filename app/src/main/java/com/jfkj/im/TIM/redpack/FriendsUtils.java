package com.jfkj.im.TIM.redpack;

import com.jfkj.im.App;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMCheckFriendResult;
import com.tencent.imsdk.friendship.TIMFriendCheckInfo;
import com.tencent.imsdk.friendship.TIMFriendCheckType;
import com.tencent.imsdk.friendship.TIMFriendRelationType;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class FriendsUtils {

    /**
     * 获取指定用户资料
     * 参数:
     * identifiers - 用户的identifier列表
     * forceUpdate - 强制从后台拉取
     * cb - 回调
     */
    public static void getUsersProfile(List<String> friends, boolean foceUpdate, TIMValueCallBack<List<TIMUserProfile>> callBack) {

        TIMFriendshipManager.getInstance().getUsersProfile(friends, foceUpdate, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
                callBack.onError(code, desc);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                callBack.onSuccess(timUserProfiles);
            }
        });
    }

    public static void getUsersProfileLoaclService(String id, TIMValueCallBack<TIMUserProfile> callBack) {
        //获取本地缓存的用户资料
        TIMUserProfile userProfile = TIMFriendshipManager.getInstance().queryUserProfile(id);
        if (userProfile == null) {
            List<String> strings = new ArrayList<>();
            strings.add(id);
            getUsersProfile(strings, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int code, String desc) {
                    callBack.onError(code, desc);
                }
                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    if (timUserProfiles.size() > 0) {
                        callBack.onSuccess(timUserProfiles.get(0));
                    }
                }
            });
        } else {
            callBack.onSuccess(userProfile);
        }
    }


    /**
     * 获取本地用户资料名字
     */
    public static String queryUsersProfile(String id) {
        //获取本地缓存的用户资料
        TIMUserProfile userProfile = TIMFriendshipManager.getInstance().queryUserProfile(id);
        if (userProfile != null) {
            return userProfile.getNickName();
        } else {
            return "";
        }
    }

    /**
     * 获取本地用户资料
     */
    public static TIMUserProfile queryUsersProfileUser(String id) {
        //获取本地缓存的用户资料
        return TIMFriendshipManager.getInstance().queryUserProfile(id);
    }


    public static void querySelfProfile(TIMValueCallBack<TIMUserProfile> callBack) {
        TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().querySelfProfile();
        if (timUserProfile == null) {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int code, String desc) {
                    callBack.onError(code, desc);
                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    callBack.onSuccess(timUserProfile);
                }
            });
        } else {
            callBack.onSuccess(timUserProfile);
        }
    }

    /**
     * 检查好友关系
     *
     *
     * *  不是好友
     * *
     * TIM_FRIEND_RELATION_TYPE_NONE = 0;
     * <p>
     * 对方在我的好友列表中
     * TIM_FRIEND_RELATION_TYPE_MY_UNI = 1;
     * 我在对方的好友列表中
     * TIM_FRIEND_RELATION_TYPE_OTHER_UNI = 2;
     * 互为好友
     * TIM_FRIEND_RELATION_TYPE_BOTH_WAY = 3;
     */
    public static void checkFriend(String id,TIMValueCallBack<Boolean> callBack) {
        List<String> strings = new ArrayList<>();
        strings.add(id);
        TIMFriendCheckInfo checkInfo = new TIMFriendCheckInfo();
        checkInfo.setUsers(strings);
        checkInfo.setCheckType(TIMFriendCheckType.TIM_FRIEND_CHECK_TYPE_BIDIRECTION);
        TIMFriendshipManager.getInstance().checkFriends(checkInfo, new TIMValueCallBack<List<TIMCheckFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                callBack.onError(i,s);
            }

            @Override
            public void onSuccess(List<TIMCheckFriendResult> timCheckFriendResults) {
                if (timCheckFriendResults != null && timCheckFriendResults.size() > 0 ){
                    for (TIMCheckFriendResult timCheckFriend: timCheckFriendResults){
                        if (timCheckFriend.getResultType() == TIMFriendRelationType.TIM_FRIEND_RELATION_TYPE_BOTH_WAY){
                            callBack.onSuccess(true);
                        } else {
                            callBack.onSuccess(false);
                        }
                    }
                }
            }
        });
    }

    public static void addFriend(String id, TIMValueCallBack<Boolean> callBack){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.APPLYID,id);

        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.base_url+"/friend/addTaskFriend")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(id,"");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            ToastUtils.showShortToast(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("200")) {
                               callBack.onSuccess(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
