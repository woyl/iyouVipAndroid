package com.jfkj.im.mvp.Home;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AccountBalance;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMFriendStatus;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.observers.DisposableObserver;
import okhttp3.Call;
import okhttp3.ResponseBody;

public class HomePresenter extends BasePresenter<HomeView> {
    public HomePresenter(HomeView homeView) {
        attachView(homeView);
    }

    public void getVersionAndroid(Context context) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.getdeviceName());
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.getVersionAndroid(map), new DisposableObserver<AppVersion>() {
            @Override
            public void onNext(AppVersion version) {
                if (version.isSuccess() && version != null) {
                    mvpView.showUpdateDialog(version);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //获得礼物列表
    public void getGiftList() {
        mvpView.showLoading();
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.getGiftListResponseBody(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.getgiftlist(model.string());

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void pickFemaleAdd() {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        Map<String, String> headmap = new HashMap();

        headmap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        headmap.put(Utils.CONTENTYPE, "application/x-www-form-urlencoded");
        OkHttpUtils.post()
                .url(ApiStores.base_url + "/friend/pickFemaleAdd")
                .headers(headmap)
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
                                // SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.UpSex,"");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获得礼物列表
    public void getGif() {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.gethomeGiftList(map), new ApiCallback<GiftData>() {
            @Override
            public void onSuccess(GiftData model) {
                try {
                    mvpView.showGiftList(model.getData().getArray());
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


    //提交城市定位
    public void getCity(String hometown) {
//        mvpView.showLoading();
//        Map<String, String> map = new HashMap<>();
//        map.put(Utils.HOMETOWN, hometown);
//        map.put(Utils.OSNAME, Utils.ANDROID);
//        map.put(Utils.CHANNEL, Utils.ANDROID);
//        map.put(Utils.DEVICENAME, Utils.getdeviceName());
//        map.put(Utils.DEVICEID, Utils.ANDROID);
//        map.put(Utils.REQTIME, AppUtils.getReqTime());
//        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
//
//        addSubscription(apiStores.editorInfo(map), new ApiCallback<BaseResponse>() {
//            @Override
//            public void onSuccess(BaseResponse model) {
//
//            }
//
//            @Override
//            public void onFailure(String msg) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
    }

    public void genUserSig() {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

        addSubscription(apiStores.genUserSig(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.UserSigSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.UserSigfail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void getUserDetail(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.USERID, userId);
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        Map<String, String> headMap = new HashMap<>();
        headMap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        headMap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.base_url + "/home/getUsertail")
                .headers(headMap)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        OkLogger.e("json1 --- >" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {
                                UserDetailBean userDetail = JSON.parseObject(response, UserDetailBean.class);
                                UserDetailBean.DataBean data = userDetail.getData();
                                UserInfoManger.saveUserHeadUrl(data.getHead());
                                UserInfoManger.saveWeight(data.getWeight() + "");
                                UserInfoManger.saveHeight(data.getHeight() + "");
                                UserInfoManger.saveNickName(data.getUserNickName());
                                UserInfoManger.saveGender(data.getGender() + "");
                                UserInfoManger.saveDescribe(data.getSignature());
                                UserInfoManger.saveHometown(data.getHometown());
                                UserInfoManger.saveEducation(data.getEducational());
                                UserInfoManger.saveBirthday(data.getBirthday());
                                UserInfoManger.saveUserVipLevel(data.getVipLevel() + "");
                                UserInfoManger.saveConstellation(data.getConstellation());
                                UserInfoManger.saveAge(data.getAge() + "");
                                UserInfoManger.saveUserId(data.getUserid());
                                UserInfoManger.savaSchool(data.getSchool());
                                UserInfoManger.savaindustry(data.getIndustry());
                                UserInfoManger.savaSmokingy(data.getSmoking());
                                UserInfoManger.savaDrinkwine(data.getDrinkwine());
                                UserInfoManger.savaLikeCuisine(data.getLikeCuisine());
                                UserInfoManger.savaOccupation(data.getOccupation());
                                UserInfoManger.saveUserHeadState(data.getHeadState() + "");
                                UserInfoManger.savaVipCard(data.getVipCard() + "");

                                HashMap<String, Object> hashMap = new HashMap<>();
                                if (data.getHead() != null) {
                                    hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, data.getHead() + "");
                                }
                                if (data.getUserNickName() != null) {
                                    hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, data.getUserNickName() + "");
                                }
                                if (data.getSignature() != null) {
                                    hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_SELFSIGNATURE, data.getSignature() + "");
                                }
                                if (data.getHometown() != null) {
                                    hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_LOCATION, data.getHometown() + "");
                                }
                                hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_ALLOWTYPE, TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM);

//                                ToastUtils.showShortToast("同步VIP等级 " + data.getVipLevel());
//                                OkLogger.e("同步腾讯 VIP 等级 = "  + data.getVipLevel() );
                                hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_LEVEL, data.getVipLevel());       //同步VIP 等级
                                TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, new TIMCallBack() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onSuccess() {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.base_url + "/home/getUsertails")
                .headers(headMap)
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
                                UserDetailBean userDetail = JSON.parseObject(response, UserDetailBean.class);
                                UserInfoManger.savaMineUserHeadUrl(userDetail.getData().getHead());
                                String detail = userDetail.getData().getHeadState() + "";
                                UserInfoManger.saveMineUserHeadState(userDetail.getData().getHeadState() + "");
                                UserInfoManger.saveUserVideo(userDetail.getData().getHomeVideo());
                                UserInfoManger.saveTaskCount(userDetail.getData().getTaskCount());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void accountBalance() {
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");

        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.accountBalance(mapHasp), new HttpErrorMsgObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                if (accountBalance.getCode().equals("200")) {
                    UserInfoManger.saveUserBalance(accountBalance.getData().getJewel());
                    UserInfoManger.saveUserCost(accountBalance.getData().getJewelCost() + "");
                    UserInfoManger.saveUserGift(accountBalance.getData().getGoldCoin() + "");
                    UserInfoManger.saveUpgradeAmount(accountBalance.getData().getUpgradeAmount() + "");
                    UserInfoManger.savaAccumulatedGifts(accountBalance.getData().getGoldIncome() + "");
                }
            }
        });
    }

    public void selectAdvertisement() {
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        mapHasp.put(Utils.APIPKG, Utils.ANDROID);
        mapHasp.put(Utils.TYPE, 0 + "");

        addSubscription(apiStores.selectAdvertisement(mapHasp), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    SPUtils.getInstance(App.getAppContext()).put(Utils.SELECTADVERTISEMENT, model.string());
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

    /**
     * 退出登录
     */
    public void loginOut() {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.loginOut(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loginOutSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }


    public void getRanking(String type) {
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());

        mapHasp.put(Utils.TYPE, type);
        addSubscription(apiStores.getRanking(mapHasp), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    SPUtils.getInstance(App.getAppContext()).put(Utils.GETRANKING + type, model.string());
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

//    public void addRecommendfriend(List<String> useridList){
//
//        for(String s:useridList){
//            TIMFriendRequest timFriendRequest = new TIMFriendRequest(s);
//            TIMFriendshipManager.getInstance().addFriend(timFriendRequest, new TIMValueCallBack<TIMFriendResult>() {
//                @Override
//                public void onError(int code, String desc) {
//
//                }
//
//                @Override
//                public void onSuccess(TIMFriendResult timFriendResult) {
//                    switch (timFriendResult.getResultCode()) {
//                        case TIMFriendStatus.TIM_FRIEND_STATUS_SUCC:
//                            ToastUtil.toastShortMessage("成功");
//                            break;
//                        case TIMFriendStatus.TIM_FRIEND_PARAM_INVALID:
//                            if (TextUtils.equals(timFriendResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
//                                ToastUtil.toastShortMessage("对方已是您的好友");
//                                break;
//                            }
//                        case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_SELF_FRIEND_FULL:
//                            ToastUtil.toastShortMessage("您的好友数已达系统上限");
//                            break;
//                        case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_THEIR_FRIEND_FULL:
//                            ToastUtil.toastShortMessage("对方的好友数已达系统上限");
//                            break;
//                        case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
//                            ToastUtil.toastShortMessage("你已被对方拉黑");
//                            break;
//                        case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
//                            ToastUtil.toastShortMessage("对方已禁止加好友");
//                            break;
//                        case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
//                            ToastUtil.toastShortMessage("您已被被对方设置为黑名单");
//                            break;
//                        case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_PENDING:
//                            ToastUtil.toastShortMessage("好友申请已发送");
//                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.UpSex,"");
//                            break;
//                        default:
//                            ToastUtil.toastLongMessage(timFriendResult.getResultCode() + " " + timFriendResult.getResultInfo());
//                            break;
//                    }
//                }
//            });
//        }
//    }

    /**
     * 获取今日之星
     */

    public void getTodayStar() {
        mvpView.showLoading();
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");

        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.getTodayStar(mapHasp), new ApiCallback<BaseBeans<TodayStarBean>>() {
            @Override
            public void onSuccess(BaseBeans<TodayStarBean> model) {
                if (model.getCode().equals("200")) {
                    mvpView.getTodayStar(model.getData());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
