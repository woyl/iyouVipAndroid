package com.jfkj.im.mvp.mine;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/13
 * </pre>
 */
public class EditUserInfoPresenter extends BasePresenter<EditUserInfoView> {
    private MineModel mModel;

    public EditUserInfoPresenter(EditUserInfoView view){
        attachView(view);
        mModel = new MineModel();
    }

    /**
     * 上传文件
     * @param outputFile
     * @param userid
     * @param signedFile
     */
    public void uploadUserHead(File outputFile , String userid,String signedFile){

        mvpView.showLoading();
        Map<String,String> map = new HashMap<>();
        map.put("fileType","1");
        map.put("pathType","1");
        map.put("userId", userid);
        map.put("signedFile",signedFile);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), outputFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", outputFile.getName(), requestBody);
        addSubscription(mModel.uploadSingleFile(map, part), new HttpErrorMsgObserver<ResponseFileUrl>() {
            @Override
            public void onNext(ResponseFileUrl o) {
                OkLogger.e("---- on next " + o.getData().getFileUrl());
                if (o.isSuccess()){
                    ToastUtils.showShortToast("提交成功，审核通过后立即生效");
                    upLoad(o.getData().getFileUrl());
                }else {
                    mvpView.hideLoading();
                }
            }
        });
    }


    /**
     *  设置信息
     * @param map
     * @param type
     */
    public void editInfo(Map<String,String> map,String type){

        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());



        addSubscription(apiStores.editorInfo(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse o) {
                if (o.isSuccess()){
                    mvpView.showEditSuccess(o,type);
                }else {
                    ToastUtils.showShortToast(o.getMessage());
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
     * 上传头像
     * @param fileUrl
     */
    public void upLoad(String fileUrl) {
        Map<String, String> map = new HashMap<>();
        map.put("portrait", fileUrl);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.portraitExamine(map), new ApiCallback<ResponseBody>() {

            @Override
            public void onSuccess(ResponseBody model) {


                UserInfoManger.savaMineUserHeadUrl(fileUrl);
                UserInfoManger.saveMineUserHeadState("0");
                mvpView.showEditSuccess(null,"user_head");
//                    mvpView.showEditSuccess(baseResponse,"user_head");
                mvpView.hideLoading();
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
