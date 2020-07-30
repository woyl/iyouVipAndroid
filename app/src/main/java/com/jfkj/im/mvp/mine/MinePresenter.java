package com.jfkj.im.mvp.mine;


import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jfkj.im.App;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AccountBalance;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.CommentBeanX;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.entity.UserPraiseBean;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.SoftKeyBoardListener;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.ResponseBody;


/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class MinePresenter extends BasePresenter<MineView> {
    private MineModel mModel;

    public MinePresenter(MineView view) {
        attachView(view);
        mModel = new MineModel();
    }


    /**
     * 我的动态
     * @param pageSize 10
     * @param pageNo    1
     */
    public void getMineInfo(String pageSize, String pageNo) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.PAGENO,pageNo);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.homePage(map), new ApiCallback<MineInfo>() {
            @Override
            public void onSuccess(MineInfo model) {

                OkLogger.e("-----" + model.getCode());
                if(model.isSuccess()){
                    mvpView.showMineInfo(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e("获取用户信息失败");
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }



    public void getAccountBalance(){
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.accountBalance(mapHasp), new HttpErrorMsgObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                if (accountBalance.getCode().equals("200")) {

                    //jewel 账户钻石余额
                    UserInfoManger.saveUserBalance(accountBalance.getData().getJewel());
                    //消费钻石总额
                    UserInfoManger.saveUserCost(accountBalance.getData().getJewelCost() + "");
                    //goldIncome
                    UserInfoManger.saveUserGift(accountBalance.getData().getGoldCoin() + "");
                    //upgradeAmount
                    UserInfoManger.saveUpgradeAmount(accountBalance.getData().getUpgradeAmount() + "");

                    UserInfoManger.savaAccumulatedGifts(accountBalance.getData().getGoldIncome() + "");
                    mvpView.onSuccessBalance();
                }
            }
        });
    }

    public void deleteDynamic(String articleId,int position){
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        mapHasp.put("articleId",articleId);

        addSubscription(apiStores.deleteDynamic(mapHasp), new ApiCallback<BaseResponse>() {
          @Override
          public void onSuccess(BaseResponse model) {
              if (model.isSuccess()){
                  mvpView.showDelete(position);
              }
              ToastUtils.showShortToast(model.getMessage());
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
     * 点赞取消点赞
     */
    public void getUserPraise(String articleId, String type, String praiseId, int position, View v){



        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        mapHasp.put(Utils.ARTICLEID, articleId);
        mapHasp.put(Utils.TYPE, type);

        if(praiseId!=null){
            mapHasp.put(Utils.PRAISEID, praiseId);
        }

        addSubscription(apiStores.getUserPraise2(mapHasp), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody o) {

                try {
                   String string = o.string();
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        UserPraiseBean userGroupTaskBean = JSON.parseObject(jsonObject.toString(), UserPraiseBean.class);
                        mvpView.showPraise(userGroupTaskBean.getData().getPraiseid(),position);
                        v.setEnabled(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(String msg) {
                v.setEnabled(true);
            }
            @Override
            public void onFinish() {
                v.setEnabled(true);
            }
        });
    }


    public void findAllNewComment(String articleId){
        Map<String,String > mapHasp = new HashMap<>();
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        mapHasp.put(Utils.ARTICLEID, articleId);

        addSubscription(apiStores.findAllNewComment(mapHasp), new ApiCallback<CommentBeanX>() {
            @Override
            public void onSuccess(CommentBeanX model) {



//                try {
//                    JSONObject jsonObject  =  new JSONObject(model.string() );
//
//
//                    OkLogger.e("----------------" + jsonObject.getJSONArray("data").toString());
//
//
//                    try {
//
//                        List<CommentBeanX> data1 = JSON.parseArray(jsonObject.getJSONArray("data").toString(), CommentBeanX.class);
                        mvpView.onFindOnNew(model.getData());
//
//                    }catch (Exception e){
//                        OkLogger.e(e.toString());
//                    }
//
//
////                    List<CommentBean> data = JSON.parseArray(jsonObject.getJSONArray("data").toString(), CommentBean.class);
//
//                } catch (JSONException | IOException e) {
//                    OkLogger.e(e.toString());
//                }

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

    public void setComment(String userId, String articleId, String content, String type, String contentId, TIMValueCallBack<Boolean> callBack) {

        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());
        if(userId.trim().length()>0){
            mapHasp.put(Utils.USERID, userId);
        }
        mapHasp.put(Utils.ARTICLEID, articleId);
        mapHasp.put(Utils.CONTENT, content);
        mapHasp.put(Utils.TYPE, type);

        mapHasp.put("contentId",contentId);

        addSubscription(apiStores.setCommentHashMap(mapHasp), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                if (o.isSuccess()) {
                    mvpView.showSuccess();
                    ToastUtils.showShortToast(o.getMessage());
                }else if (o.code.equals("50000")) {
                    callBack.onSuccess(true);
                } else {
                    ToastUtils.showShortToast(o.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
                callBack.onError(500,msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public void getGroupTask(String userid){
        //公共参数
        Map<String,String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.USERID,userid);

            addSubscription(apiStores.getUserGroupTask(map), new ApiCallback<ResponseBody>() {


                @Override
                public void onSuccess(ResponseBody model) {
                    OkLogger.e("-----------------" +model.toString());

                    String string = null;
                    try {
                        string = model.string();
                        JSONObject jsonObject = new JSONObject(string);

                        UserGroupTaskBean userGroupTaskBean = JSON.parseObject(jsonObject.toString(), UserGroupTaskBean.class);
                        mvpView.showUserGroupTask(userGroupTaskBean);
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

    /**
     * 获取用户信息
     */

    public void getUserDetails() {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.USERID, UserInfoManger.getUserId());
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
                        OkLogger.e("mine json2 --- >" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {
                                UserDetailBean userDetail = JSON.parseObject(response, UserDetailBean.class);
                                mvpView.getUserInfo(userDetail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


}
