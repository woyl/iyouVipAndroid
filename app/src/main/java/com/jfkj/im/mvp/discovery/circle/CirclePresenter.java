package com.jfkj.im.mvp.discovery.circle;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.jfkj.im.App;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.model.DiscoveryModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.Loginregister_phone_Activity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.TimeUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public class CirclePresenter extends BasePresenter<CircleView> {
    private DiscoveryModel mModel;

    public CirclePresenter(CircleView circleView) {
        attachView(circleView);
        mModel = new DiscoveryModel();
    }

    public void getFindDynamic(String pageNo, String pageSize) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();

        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGENO, pageNo);
        map.put(Utils.PAGESIZE, pageSize);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.getFindDynamic(map), new ApiCallback<CircleBean>() {
            @Override
            public void onSuccess(CircleBean model) {

                if (model.isSuccess()) {
                    mvpView.showDynamic(model);
                }else {
                    ToastUtils.showShortToast(model.getMessage());
                    SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
                    Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                    kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    App.getAppContext().startActivity(kick_out_intent);
                    ApiClient.onDestroy();
                }

            }

            @Override
            public void onFailure(String msg) {

                ToastUtils.showShortToast(msg);
                SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
                Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                App.getAppContext().startActivity(kick_out_intent);
                ApiClient.onDestroy();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();

            }
        });

    }

    public void unread() {
        mvpView.showLoading();
        Map<String, String> querymap = new HashMap();
        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, Utils.ANDROID);

        querymap.put(Utils.REQTIME, AppUtils.getReqTime());
        OkGo.<String>post(ApiStores.base_url + "/find/unreadRecord")
                .headers(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        try {
                            String body = response.body();
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("code").equals("200")) {
                            }
                            try {
                                if (jsonObject.getString("code").equals("11001")) {
                                    SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
                                    Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                                    kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    App.getAppContext().startActivity(kick_out_intent);
                                    ApiClient.onDestroy();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mvpView != null ) {
                            mvpView.hideLoading();
                        }
                    }
                });
    }

    /**
     * 点赞取消点赞
     */
    public void getUserPraise(String articleId, String type, String praiseId, int position, String b, View v) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.ARTICLEID, articleId);
        map.put(Utils.TYPE, type);
        if (praiseId.length() > 0) {
            map.put(Utils.PRAISEID, praiseId);
        }
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.getUserPraise(map), new ApiCallback<PraiseIdBean>() {
            @Override
            public void onSuccess(PraiseIdBean o) {
                v.setEnabled(true);
                mvpView.hideLoading();
                mvpView.showPraise(o, position, b);
            }

            @Override
            public void onFailure(String msg) {
                v.setEnabled(true);
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
                v.setEnabled(true);
            }
        });

    }

    public void setComment(String userId, String articleId, String content, String type, String commentId, int position, String contentId, TIMValueCallBack<Boolean> callBack) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        if (userId.trim().length() > 0) {
            map.put(Utils.USERID, userId);
        }
        map.put(Utils.ARTICLEID, articleId);
        map.put(Utils.CONTENT, content);
        map.put(Utils.TYPE, type);
        if (commentId.trim().length() > 0) {
            map.put(Utils.COMMENTID, commentId);
        }
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);

        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.CONTENT_ID,contentId);
        addSubscription(apiStores.setCommentHashMap(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                if (o.isSuccess()) {
                    mvpView.showSuccess(position);
                    ToastUtils.showShortToast(o.getMessage());
                } else if (o.code.equals("50000")) {
                    callBack.onSuccess(true);
                }
            }

            @Override
            public void onFailure(String msg) {
                callBack.onError(500,msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void addAccessSpace() {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);

        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.addAccessSpace(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    String string = model.string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getString("code").equals("200")) {
                        unread();
                    }
                    if (jsonObject.getString("code").equals("11001")) {
                        SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
                        Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                        kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.getAppContext().startActivity(kick_out_intent);
                        ApiClient.onDestroy();
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

    public void Circlefriend(String pageNo){
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGENO, pageNo);
        map.put(Utils.PAGESIZE, 10+"");


        addSubscription(apiStores.circlefriend(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.CirclefriendSuccess(model);
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
}
