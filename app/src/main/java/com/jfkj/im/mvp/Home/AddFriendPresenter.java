package com.jfkj.im.mvp.Home;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.AddFriendBean;
import com.jfkj.im.Bean.GiftchatBean;
import com.jfkj.im.Bean.NewfriendBean;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AccountBalance;
import com.jfkj.im.entity.AddFriend;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.ui.dialog.Dialoglevel;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMFriendStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class AddFriendPresenter extends BasePresenter<AddFriendView> {
    private HomeModel mHomeModel;

    private Context context;

    public AddFriendPresenter(AddFriendView view) {
        attachView(view);
        mHomeModel = new HomeModel();
    }


    public void addFriend(Context context, String osName, String appVersion, String channel, String userId, String giftId, String size, String content) {
        mvpView.showLoading();
        this.context = context;
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.USERID, userId);
        map.put(Utils.GIFTID, giftId);
        map.put(Utils.SIZE, size);
        map.put(Utils.CONTENT, content);
        addSubscription(apiStores.applyAddFriend(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

                try {
                    String string = model.string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getString("code").equals("200")) {
                        AddFriendBean addFriendBean = JSON.parseObject(string, AddFriendBean.class);
                        GiftchatBean giftchatBean = JSON.parseObject(SPUtils.getInstance(App.getAppContext()).getString(Utils.GETGIFTLIST), GiftchatBean.class);
                        List<GiftchatBean.DataBean.ArrayBean> array = giftchatBean.getData().getArray();
                        NewfriendBean newfriendBean = new NewfriendBean();
                        for (int i = 0; i < array.size(); i++) {
                            if (giftId.equals(array.get(i).getGiftId())) {
                                newfriendBean.setGiftidimg(array.get(i).getPictureUrl());
                            }
                        }
                        List<NewfriendBean> newfriendBeanList = new ArrayList<>();
                        newfriendBean.setHint_message(content);
                        newfriendBean.setType("4");
                        newfriendBean.setState("等待通过");
                        newfriendBean.setGiftsize(size);
                        newfriendBean.setHead_url(UserInfoManger.getUserHeadUrl());
                        newfriendBean.setUser_name(UserInfoManger.getNickName());
                        newfriendBean.setVisitorname(osName);
                        newfriendBean.setVisitorid(userId);
                        newfriendBeanList.add(newfriendBean);

                        if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.ADDFRIEND) != null) {
                            if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.ADDFRIEND).toString().length() > 0) {
                                newfriendBeanList.addAll(JSON.parseArray(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.ADDFRIEND), NewfriendBean.class));
                            }
                        }

                        SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.ADDFRIEND, JSON.toJSONString(newfriendBeanList));


                        ExecutorServiceUtils.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                TIMFriendRequest timFriendRequest = new TIMFriendRequest(userId);

                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("gift", giftId + "_" + size);
                                    jsonObject.put("des", content);
                                    timFriendRequest.setAddWording(jsonObject.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                TIMFriendshipManager.getInstance().addFriend(timFriendRequest, new TIMValueCallBack<TIMFriendResult>() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        ToastUtil.toastShortMessage(code + desc);
                                    }

                                    @Override
                                    public void onSuccess(TIMFriendResult timFriendResult) {

                                        switch (timFriendResult.getResultCode()) {
                                            case TIMFriendStatus.TIM_FRIEND_STATUS_SUCC:
//                                                ToastUtil.toastShortMessage("成功");

                                                mvpView.addFriendSuccess();

                                                break;
                                            case TIMFriendStatus.TIM_FRIEND_PARAM_INVALID:
                                                if (TextUtils.equals(timFriendResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
                                                    ToastUtil.toastShortMessage("对方已是您的好友");
                                                    break;
                                                }
                                            case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_SELF_FRIEND_FULL:


                                                Dialoglevel dialoglevel = new Dialoglevel(context, "您已达到当前添加好友上限，请升级VIP等级。");
                                                dialoglevel.setClick(new Dialoglevel.onClick() {
                                                    @Override
                                                    public void click(View view) {

                                                    }
                                                });
                                                dialoglevel.show();


                                                ToastUtil.toastShortMessage("您的好友数已达系统上限");


                                                break;
                                            case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_THEIR_FRIEND_FULL:
                                                ToastUtil.toastShortMessage("对方的好友数已达系统上限");
                                                break;
                                            case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                                                ToastUtil.toastShortMessage("你已被对方拉黑");
                                                break;
                                            case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                                                ToastUtil.toastShortMessage("对方已禁止加好友");
                                                break;
                                            case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                                                ToastUtil.toastShortMessage("您已被被对方设置为黑名单");
                                                break;
                                            case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_PENDING:
                                                ToastUtil.toastShortMessage("好友申请已发送");
                                                break;
                                            default:
                                                ToastUtil.toastLongMessage(timFriendResult.getResultCode() + " " + timFriendResult.getResultInfo());
                                                break;
                                        }
                                    }
                                });
                            }
                        });


                    } else {

                        ToastUtil.toastShortMessage(jsonObject.getString("message"));

                    }
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

    public void userdetail(String userid) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.USERID, userid);
        addSubscription(apiStores.getTRTCUserDetail(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.userdetail(model.string());
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
}
