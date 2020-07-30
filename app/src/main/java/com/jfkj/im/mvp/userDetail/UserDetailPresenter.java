package com.jfkj.im.mvp.userDetail;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.model.HomeModel;
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
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/6
 * </pre>
 */
public class UserDetailPresenter extends BasePresenter<UserDetailView> {

    private HomeModel mModel;

    public UserDetailPresenter(UserDetailView view) {
        attachView(view);
        mModel = new HomeModel();
    }


    public void getUserCircleArticle(String longitude, String latitude, String userId, Context context,int pageNo) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();

        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);

        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.LONGITUDE, longitude);
        map.put(Utils.LATITUDE, latitude);
        map.put(Utils.USERID, userId);
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.PAGENO,pageNo + "");
        map.put(Utils.PAGESIZE,"10");

//        SPUtils.getInstance(App.getAppContext()).put(Utils.SIGN,SignUtils.getInstance().getSignToken(map));
        addSubscription(apiStores.getUserCircleArticle(map), new ApiCallback<CircleBean>() {
            @Override
            public void onSuccess(CircleBean model) {
                mvpView.hideLoading();
                if (model.isSuccess()) {
                    mvpView.showCircleArticle(model);
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

    /**
     * 礼物列表
     */
    public void getGiftList() {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        addSubscription(apiStores.getGiftList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    String string = model.string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getString("code").equals("200")) {
                        GiftData giftData = JSON.parseObject(string, GiftData.class);
                        mvpView.showGiftList(giftData.getData().getArray());
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

    /**
     * 拉黑
     */
    public void blackList(String userId, String type) {
        addSubscription(mModel.blackList(userId, type), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse response) {
                if (response.isSuccess()) {
                    if(type.equals("1")){
                        List<String> list = new ArrayList<>();
                        list.add(userId);
                        TIMFriendshipManager.getInstance().addBlackList(list, new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int code, String desc) {

                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                Intent intent = new Intent("removefriend");
                                intent.putExtra("removefriend", userId);
                                App.getAppContext().sendBroadcast(intent);
                                mvpView.Pullblacksuccess();
                            }
                        });
                        TIMFriendshipManager.getInstance().deleteBlackList(list, new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int code, String desc) {

                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                Intent intent = new Intent("removefriend");
                                intent.putExtra("removefriend", userId);
                                App.getAppContext().sendBroadcast(intent);
                            }
                        });
                    }
                    ToastUtils.showShortToast(response.getMessage());
                }
            }
        });
    }

    /**
     * 点赞取消点赞
     */
    public void getUserPraise(Context context, String articleId, String type, String praiseId, int position) {

      //  Toast.makeText(context, "praiseId = " + praiseId , Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.DEVICEID, AppUtils.getIMEI(context));
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put("articleId", articleId);
        map.put("type", type);
        if (!TextUtils.isEmpty(praiseId)) {
            map.put("praiseId", praiseId);
        }
        addSubscription(mModel.getUserPraise(map), new HttpErrorMsgObserver<PraiseIdBean>() {
            @Override
            public void onNext(PraiseIdBean o) {
                if (o.isSuccess()) {
                    mvpView.showPraise(o, position);
                }

                ToastUtils.showShortToast(o.getMessage());

            }
        });
    }

    public void getUserDetail(Context context, String userid) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.DEVICENAME, AppUtils.getBRAND());
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.USERID, userid);
//       SPUtils.getInstance(App.getAppContext()).put(Utils.SIGN, SignUtils.getInstance().getSignToken(map));
        addSubscription(apiStores.getUserDetail(map), new ApiCallback<UserDetail>() {
            @Override
            public void onSuccess(UserDetail model) {
                if (model.isSuccess()) {
                    mvpView.showDetail(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.fails(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void setComment(String userId, String articleId, String content, String type, String commentId,String contentId) {
//        addSubscription(mModel.setComment(userId, articleId, content, type, commentId), new HttpErrorMsgObserver<BaseResponse>() {
//            @Override
//            public void onNext(BaseResponse o) {
//                if (o.isSuccess()) {
//                    mvpView.showSuccess();
//                    ToastUtils.showShortToast(o.getMessage());
//                } else {
//                    ToastUtils.showShortToast(o.getMessage());
//                }
//            }
//        });
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.USERID, userId);
        map.put(Utils.ARTICLEID, articleId);
        map.put(Utils.CONTENT, content);
        map.put(Utils.TYPE, type);
        map.put(Utils.COMMENTID, commentId);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("contentId",contentId);


        addSubscription(apiStores.setCommentHashMap(map), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()) {
                    mvpView.showSuccess();
                    ToastUtils.showShortToast(o.getMessage());
                } else {
                    ToastUtils.showShortToast(o.getMessage());
                }
            }
        });
    }

    public void getUserGroup(String userId, Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, AppUtils.getBRAND());
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.USERID, userId);

        addSubscription(apiStores.getUserDetailClub(map), new HttpErrorMsgObserver<UserDetailClubBean>() {
            @Override
            public void onNext(UserDetailClubBean o) {
                if (o.isSuccess()) {
                    mvpView.showGroupList(o);
                }
            }
        });
    }

    //单个文件采用多个文件上传接口（这里只能上传图片）
    public void uploadFiles(String fileType, String pathType, File file, String signedFile) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("fileType", fileType);
        map.put("pathType", pathType);
        map.put("userId", UserInfoManger.getUserId());
        map.put("signedFile",signedFile);
//        List<MultipartBody.Part> parts = new ArrayList<>();
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//        parts.add(body);
//        addSubscription(apiStores.uploadFiles(map, parts), new ApiCallback<ResponseBody>() {
//            @Override
//            public void onSuccess(ResponseBody model) {
//                try {
//                    mvpView.upLoadPic(model.string());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mvpView.getCrushFails(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                mvpView.hideLoading();
//            }
//        });
        map.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
//        map.put("userId", SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));
        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.baseupload_url+"/file/uploadFiles")
                .headers(map)
                .addFile("file",file.getName(),file)
                .build()
                //上传视频 timeout
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        OkLogger.e(e.toString());
                        mvpView.hideLoading();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mvpView.hideLoading();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            ToastUtils.showShortToast(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("200")) {
                                String url_video = jsonObject.getJSONObject("data").getString("fileUrls");
                                mvpView.upLoadPic(url_video);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
