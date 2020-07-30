package com.jfkj.im.mvp.discovery;

import android.os.Build;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.model.DiscoveryModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/10
 * </pre>
 */
public class PostingPresenter extends BasePresenter<PostingView> {
    public DiscoveryModel mModel;
    private String content;
    private String location; //位置
    private String fileType;
    private String pathType;
    private String isLocation;  //1不隐藏 2隐藏 位置是否隐藏

    public PostingPresenter(PostingView view) {
        attachView(view);
        mModel = new DiscoveryModel();
    }

    public void setPrams(String fileType, String pathType, String userId, String content, String location, String isLocation) {
        this.fileType = fileType;
        this.pathType = pathType;
        this.content = content;
        this.location = location;
        this.isLocation = isLocation;
    }
    public void upLoadVideo(LocalMedia list) {
        File file = new File(list.getPath());
        Map<String, String> map = new HashMap<>();
        map.put("fileType", fileType);
        map.put("pathType", pathType);
        map.put(Utils.USERID, Utils.APPID);
        RequestBody requestBody = RequestBody.create(MediaType.parse("iamge/jpg"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        addSubscription(mModel.uploadSingleFile(fileType, pathType, body), new HttpErrorMsgObserver<ResponseFileUrl>() {
            @Override
            public void onNext(ResponseFileUrl o) {
                if (o.isSuccess()) {
                    Log.d("@@@", "介是上传视频");
                    Map<String, String> map = new HashMap<>();
                    map.put("content", content);
                    map.put(Utils.LATITUDE, UserInfoManger.getLatitude());
                    map.put(Utils.LONGITUDE, UserInfoManger.getLongitude());
                    map.put("videoPath", o.getData().getFileUrl());
                    String fileUrl = o.getData().getFileUrl();
                    String mp4 = fileUrl.substring(0, fileUrl.length() - 3) + "jpg";
                    map.put("videoImage", mp4);
                    map.put(Utils.OSNAME, Utils.ANDROID);
                    map.put(Utils.CHANNEL, Utils.ANDROID);
                    map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                    posting(map);
                } else {
                    ToastUtils.showShortToast(o.getMessage());
                }
                mvpView.hideLoading();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                mvpView.hideLoading();
            }
        });
    }

    public void upLoadFile(List<LocalMedia> list) {
        mvpView.showLoading();

        if (list.size() > 1) {
            //多文件上传
            Map<String, String> map = new HashMap<>();
            map.put("fileType", fileType);
            map.put("pathType", pathType);
            map.put("userId", UserInfoManger.getUserId());
            map.put(Utils.OSNAME, Utils.ANDROID);
            map.put(Utils.CHANNEL, Utils.ANDROID);
            map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

            List<MultipartBody.Part> parts = new ArrayList<>();
            for (LocalMedia media : list) {



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    media.setCompressPath(media.getAndroidQToPath());
                }


                File file = new File(media.getCompressPath());

                RequestBody requestBody = RequestBody.create(MediaType.parse("iamge/jpg"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                parts.add(body);
            }
            addSubscription(mModel.uploadFiles(map, parts), new HttpErrorMsgObserver<ResponseFileUrl>() {
                @Override
                public void onNext(ResponseFileUrl o) {
                    if (o.isSuccess()) {
                        Log.d("@@@", "多文件成功");
                        Map<String, String> map = new HashMap<>();
                        map.put("content", content);
                        map.put(Utils.LATITUDE, UserInfoManger.getLatitude());
                        map.put(Utils.LONGITUDE, UserInfoManger.getLongitude());
                        map.put("img", o.getData().getFileUrls());
                        map.put(Utils.OSNAME, Utils.ANDROID);
                        map.put(Utils.CHANNEL, Utils.ANDROID);
                        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

                        posting(map);
                    } else {
                        ToastUtils.showShortToast(o.getMessage());
                    }
                    mvpView.hideLoading();
                }
            });
        } else {
            //单一文件上传
            if (list.get(0).getAndroidQToPath() != null) {
                list.get(0).setCompressPath(list.get(0).getAndroidQToPath());
            }
            File file = new File(list.get(0).getCompressPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("iamge/jpg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            addSubscription(mModel.uploadSingleFile(fileType, pathType, body), new HttpErrorMsgObserver<ResponseFileUrl>() {
                @Override
                public void onNext(ResponseFileUrl o) {
                    if (o.isSuccess()) {
                        Log.d("@@@", "介是上传单个照片");
                        Map<String, String> map = new HashMap<>();
                        map.put("content", content);
                        map.put(Utils.LATITUDE, UserInfoManger.getLatitude());
                        map.put(Utils.LONGITUDE, UserInfoManger.getLongitude());
                        map.put("img", o.getData().getFileUrl());
                        map.put(Utils.OSNAME, Utils.ANDROID);
                        map.put(Utils.CHANNEL, Utils.ANDROID);
                        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

                        posting(map);

                    } else {
                        ToastUtils.showShortToast(o.getMessage());
                    }
                    mvpView.hideLoading();
                }
            });
        }
    }

    public void posting(Map<String, String> map) {
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        if(isLocation.equals("1")){
            map.put("location", location);
        }
        map.put("islocation", isLocation);
        addSubscription(mModel.publishDynamic(map), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()) {
                    mvpView.publishSuccess(o);
                } else {
                    ToastUtils.showShortToast(o.getMessage());
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });

    }
}
