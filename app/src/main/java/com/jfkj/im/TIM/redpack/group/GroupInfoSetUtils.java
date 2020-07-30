package com.jfkj.im.TIM.redpack.group;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.base.IUIKitCallsBack;
import com.jfkj.im.TIM.modules.group.member.GroupMemberInfo;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.ui.activity.Loginregister_phone_Activity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class GroupInfoSetUtils {

    public static void setGroupInfo(Context mContext ,String groupId,String redNum,String sendHour,TIMValueCallBack<Boolean> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID,groupId);
        map.put(Utils.REDPACKETNUM,redNum);
        map.put(Utils.SENDHOUR, sendHour);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mContext)
                    .url(Urls.base_url + "/group/updateGroup")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(mContext.getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError(id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                ToastUtils.showLongToast(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    callBack.onSuccess(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showLongToast(R.string.nonetwork);
        }
    }


    public static void setGroupInfoHide(Context mContext ,String groupId,String hide,TIMValueCallBack<Boolean> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID,groupId);
        map.put(Utils.HIDE,hide);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mContext)
                    .url(Urls.base_url + "/group/updateGroup")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(mContext.getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError(id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    callBack.onSuccess(true);
                                } else {
                                    ToastUtils.showLongToast(jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showLongToast(R.string.nonetwork);
        }
    }

    public static void loadGroupMemberList(Context mContext,  String groupId, String pageSize, String sort, IUIKitCallsBack<GroupMemberInfoBean> callBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID,groupId);
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.SORT, sort);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mContext)
                    .url(Urls.base_url + "/group/loadGroupMemberList")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(mContext.getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError("",id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                ToastUtils.showLongToast(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                                    List<GroupMemberInfoBean> groupMemberInfoBeans = JSON.parseArray(jsonObject1.getString("array"),GroupMemberInfoBean.class);
                                    callBack.onSuccess(groupMemberInfoBeans);
                                }
                                if(jsonObject.getString("code").equals("11001")){
                                    SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
                                    Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                                    kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    App.getAppContext().startActivity(kick_out_intent);
                                    ApiClient.onDestroy();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showLongToast(R.string.nonetwork);
        }
    }

    public static void getGroupMembers(String groupId,TIMValueCallBack<List<TIMGroupMemberInfo>> callBack){
        TIMGroupManager.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String desc) {
                callBack.onError(code,desc);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                callBack.onSuccess(timGroupMemberInfos);
            }
        });
    }


    public static void getGroupMembers(String groupid, String sort,TIMValueCallBack<List<GroupMemberInfoBean>> callBack){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GROUPID, groupid);
        map.put(Utils.PAGESIZE,"200");
        if (sort.trim().length() > 0) {
            map.put(Utils.SORT, sort);
        }
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME,AppUtils.getReqTime());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(Urls.base_url + "/group/loadGroupMemberList")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError(id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                                    List<GroupMemberInfoBean> groupMemberInfoBeans = JSON.parseArray(jsonObject1.getString("array"),GroupMemberInfoBean.class);
                                    callBack.onSuccess(groupMemberInfoBeans);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showLongToast(R.string.nonetwork);
        }
    }

    //获取本地缓存的群组信息
    public static TIMGroupDetailInfo queryGroupInfo(String groupId){
        return TIMGroupManager.getInstance().queryGroupInfo(groupId);
    }

    ////获取服务器群组信息
    public static void getGroupInfo(List<String> ids,TIMValueCallBack<List<TIMGroupDetailInfoResult>> callBack) {
        //获取服务器群组信息
        TIMGroupManager.getInstance().getGroupInfo(
                ids, //需要获取信息的群组 ID 列表
                new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
                    @Override
                    public void onError(int code, String desc) {
                        callBack.onError(code, desc);
                    }

                    @Override
                    public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                        callBack.onSuccess(timGroupDetailInfoResults);
                    }
                });       //回调
    }

    public static void getGroupMembersInfo(String groupId, List<String> memberIds, TIMValueCallBack<TIMGroupMemberInfo> callBack) {
        TIMGroupManager.getInstance().getGroupMembersInfo(groupId, memberIds, new TIMValueCallBack<List<TIMGroupMemberInfo>> (){

            @Override
            public void onError(int i, String s) {
                callBack.onError(i, s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                for (TIMGroupMemberInfo info:timGroupMemberInfos){
                    callBack.onSuccess(info);
                }
            }
        });
    }
}
