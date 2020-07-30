package com.jfkj.im.TIM.redpack.game;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupEveryday;
import com.jfkj.im.Bean.IceSubmitBean;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.event.TimeTaskEvent;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;

public class TimeTaskUtils {

    public static void timeTask(String id){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID,id);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(Urls.base_url + "/group/hasGroupAdventureTask")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    TimeTaskBean timeTaskBean = JSON.parseObject(jsonObject.getString("data"), TimeTaskBean.class);
                                    EventBus.getDefault().post(new TimeTaskEvent(timeTaskBean));
                                }else {
                                    EventBus.getDefault().post(new TimeTaskEvent(null));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }

    public static void subGameVideo(String redId, TIMValueCallBack<Boolean> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.REDID,redId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(Urls.base_url + "/group/completeAdventureTask")
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
//                                    TimeTaskBean timeTaskBean = JSON.parseObject(jsonObject.getString("data"), TimeTaskBean.class);
//                                    EventBus.getDefault().post(new TimeTaskEvent(timeTaskBean));
                                    callBack.onSuccess(true);
                                }else {
//                                    EventBus.getDefault().post(new TimeTaskEvent(null));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }

    public static void getVideoUrl(File file,String redId,TIMValueCallBack<String>callBack){
        Map<String,String> map = new HashMap<>();
        map.put("fileType","4");
        map.put("pathType","5");
        map.put(Utils.TOKEN,SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        map.put("userId", SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));
        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(Urls.baseupload_url+"/file/uploadFiles")
                .headers(map)
                .addFile("file",file.getName(),file)
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
                                String url_video = jsonObject.getJSONObject("data").getString("fileUrls");
                                callBack.onSuccess(url_video);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void getImagVideoUrl(File file,String type,TIMValueCallBack<String>callBack){
        Map<String,String> map = new HashMap<>();
        map.put("fileType",type);
        map.put("pathType","3");
        map.put(Utils.TOKEN,SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        map.put("userId", SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));
        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(Urls.baseupload_url+"/file/uploadFiles")
                .headers(map)
                .addFile("file",file.getName(),file)
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
                                String url_video = jsonObject.getJSONObject("data").getString("fileUrls");
                                callBack.onSuccess(url_video);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void EverySubmitDayTask(String url,String taskId,String userId,String groupType,String groupId,TIMValueCallBack<IceSubmitBean> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());

        map.put(Utils.URL,url);
        map.put(Utils.TASK_ID,taskId);
        map.put(Utils.USERID,userId);
        map.put(Utils.GROUP_TYPE_TASK,groupType);
        map.put(Utils.GROUPID,groupId);

        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(Urls.base_url + "/task/confirmTask")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    IceSubmitBean giveThumbsUpBean = JSON.parseObject(jsonObject.getString("data"),IceSubmitBean.class);
                                    callBack.onSuccess(giveThumbsUpBean);
                                }else {
                                    callBack.onSuccess(null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }


    public static void EveryDayTask(String groupId,TIMValueCallBack<GroupEveryday> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID,groupId);

        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(Urls.base_url + "/group/findBestGroupTask")
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
                                    GroupEveryday timeTaskBean = JSON.parseObject(jsonObject.getString("data"), GroupEveryday.class);
                                    callBack.onSuccess(timeTaskBean);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }


}
