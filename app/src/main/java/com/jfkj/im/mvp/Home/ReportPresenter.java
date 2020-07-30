package com.jfkj.im.mvp.Home;

import android.util.Log;

import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/28
 * </pre>
 */
public class ReportPresenter extends BasePresenter<ReportView> {
    private HomeModel mHomeModel;
    private String userId;
    private String type;
    public ReportPresenter(ReportView reportView){
        attachView(reportView);
        mHomeModel = new HomeModel();
    }

    public void setInfo(String userId,String type){
        this.userId = userId;
        this.type = type;
    }

    public void upLoadFile(List<LocalMedia> list){
        File file = new File(list.get(0).getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("iamge/jpg"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),requestBody);
        addSubscription(mHomeModel.uploadSingleFile("1","5", body), new HttpErrorMsgObserver<ResponseFileUrl>() {
            @Override
            public void onNext(ResponseFileUrl o) {
                if (o.isSuccess()){
                    Log.d("@@@","userid=" + userId + "type:" + type );
                    toUserReport(userId,type,o.getData().getFileUrl());
                }else {
                    ToastUtils.showShortToast(o.getMessage());
                }
            }
        });
    }

    public void toUserReport(String userId,String type,String image){
        mvpView.showLoading();

        Map<String,String> maps = new HashMap<>();
        maps.put("userId",userId);
        maps.put("type",type);
        maps.put("img",image);

        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.OSNAME, Utils.ANDROID);
        maps.put(Utils.CHANNEL, Utils.ANDROID);
        maps.put(Utils.DEVICENAME, Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(mHomeModel.toUserReport(maps), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()){
                    mvpView.hideLoading();
                    ToastUtils.showShortToast("举报成功！");
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                mvpView.hideLoading();
            }
        });
    }
}
