package com.jfkj.im.mvp.Home;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.ArticleDynamicBean;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.UserPraiseBean;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/20
 * </pre>
 */
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailView> {
    private HomeModel mHomeModel;

    public DynamicDetailPresenter(DynamicDetailView view) {
        attachView(view);
        mHomeModel = new HomeModel();
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



    public void setComment(String userId, String articleId, String content, String type, String commentId, String contentId, TIMValueCallBack<Boolean> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.USERID, userId);
        map.put(Utils.ARTICLEID, articleId);
        map.put(Utils.CONTENT, content);
        map.put(Utils.TYPE, type);
        map.put(Utils.COMMENTID, commentId);
        map.put(Utils.CONTENT_ID,contentId);
        addSubscription(apiStores.setCommentHashMap(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                if (o.isSuccess()) {
                    mvpView.showSuccess();
                } else if (o.code.equals("50000")) {
                    callBack.onSuccess(true);
                } else {
                    ToastUtils.showShortToast(o.getMessage());
                }

            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
                callBack.onError(0,msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 点赞取消点赞
     */
    public void getUserPraise(String articleId, String type, String praiseId, View view) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.ARTICLEID, articleId);
        map.put(Utils.TYPE, type);
        if (praiseId != null) {
            map.put(Utils.PRAISEID, praiseId);
        }
//        Map<String, String> headmap = new HashMap<>();
//        headmap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
//        headmap.put(Utils.SIGN, SignUtils.getInstance().getSignToken(map));
//        SPUtils.getInstance(App.getAppContext()).put(Utils.SIGN, SignUtils.getInstance().getSignToken(map));


        addSubscription(apiStores.getUserPraise2(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody o) {

                try {
                    String string = o.string();
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        UserPraiseBean userGroupTaskBean = JSON.parseObject(jsonObject.toString(), UserPraiseBean.class);
                        mvpView.showPraise(userGroupTaskBean,view);
                        view.setEnabled(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // mvpView.showPraise(null, position);
            }

            @Override
            public void onFailure(String msg) {
                view.setEnabled(true);
            }

            @Override
            public void onFinish() {
                view.setEnabled(true);
            }
        });


//        addSubscription(apiStores.getUserPraise(map), new ApiCallback<PraiseIdBean>() {
//            @Override
//            public void onSuccess(PraiseIdBean o) {
//                if (o.isSuccess()){
//                    mvpView.showPraise(o);
//                    ToastUtils.showShortToast(o.getMessage());
//                }else {
//                    ToastUtils.showShortToast(o.getMessage());
//                }
//
//            }
//            @Override
//            public void onFailure(String msg) {
//
//            }
//            @Override
//            public void onFinish() {
//                mvpView.hideLoading();
//            }
//        });

    }

    public void getUserCircleArticle(String longitude, String latitude, String userId) {
        mvpView.showLoading();
        addSubscription(mHomeModel.getUserCircleArticle(longitude, latitude, userId), new HttpErrorMsgObserver<CircleBean>() {
            @Override
            public void onNext(CircleBean o) {
                mvpView.hideLoading();
                if (o.isSuccess()) {
                    mvpView.refreshData(o);
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                mvpView.hideLoading();
            }
        });
    }

    public void getArticleDynamic(String articleId, String ruserid, boolean isRefreshComment) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.ARTICLEID,articleId);
        map.put(Utils.RUSERID,ruserid);
        addSubscription(apiStores.getArticleDynamic(map), new ApiCallback<ArticleDynamicBean>() {
            @Override
            public void onSuccess(ArticleDynamicBean o) {
                if (o.isSuccess()){
                    if (isRefreshComment){
                        mvpView.refreshComment(o);
                    }else {
                        mvpView.showView(o);
                    }
                }else {
                    ToastUtils.showShortToast(o.getMessage());
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

}
