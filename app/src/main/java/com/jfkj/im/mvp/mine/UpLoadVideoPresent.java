package com.jfkj.im.mvp.mine;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.jfkj.im.App;
import com.jfkj.im.TIM.utils.FileUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.utils.OkLogger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/16
 * </pre>
 */
public class UpLoadVideoPresent extends BasePresenter<UploadView> {
    private MineModel mModel;

    public UpLoadVideoPresent(UploadView view){
        attachView(view);
        mModel = new MineModel();
    }

    public void upLoadFile(LocalMedia media){
        Map<String,String> map = new HashMap<>();
        map.put("fileType","4");
        map.put("pathType","1");
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        File file = new File(media.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        addSubscription(mModel.uploadSingleFile(map, part), new HttpErrorMsgObserver<ResponseFileUrl>() {
            @Override
            public void onNext(ResponseFileUrl o) {

                if (o.isSuccess()){
                    UserInfoManger.saveUserVideo(o.getData().getFileUrl());

                    uploadVideo(o.getData().getFileUrl());
                }else {

                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                //上传失败
                mvpView.hideLoading();
            }
        });

    }



    public void delVideo(String url){
        mvpView.showLoading();
        Map<String,String>  map=new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("homeVideo",url);
        addSubscription(apiStores.uploadVideo(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                mvpView.hideLoading();
                if (o.isSuccess()){
                    UserInfoManger.saveUserVideo(url);
                    ToastUtils.showShortToast(o.getMessage());
                }else {
                    ToastUtils.showShortToast(o.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e("" + msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }



    public void uploadVideo(String url){
        OkLogger.e("视屏路径" + url);
        mvpView.showLoading();
        Map<String,String>  map=new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("homeVideo",url);
        addSubscription(apiStores.uploadVideo(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                mvpView.hideLoading();

                if (o.isSuccess()){
                    ToastUtils.showShortToast("视频上传成功");
                    UserInfoManger.saveUserVideo(url);
                    mvpView.showUploadSuccess();
                    ToastUtils.showShortToast(o.getMessage());
                }else {
                    ToastUtils.showShortToast(o.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e("" + msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }




}
