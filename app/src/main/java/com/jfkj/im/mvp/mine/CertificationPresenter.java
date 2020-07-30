package com.jfkj.im.mvp.mine;


import com.jfkj.im.App;
import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 实名认证
 */
public class CertificationPresenter  extends BasePresenter<CertificationView> {
    private MineModel mModel;


    public CertificationPresenter( CertificationView  certificationView ) {
        attachView(certificationView);
        this.mModel = new MineModel();
    }





    /**
     * 上传文件
     * @param outputFile
     * @param userid
     * @param signedFile
     */
    public void uploadUserHead(File outputFile , String userid, String signedFile){

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
                    //文件上传成功
                    OkLogger.e("文件上传成功");
                    mvpView.hideLoading();
                    mvpView.upFileSuccess(o.getData().getFileUrl());

                } else {
                    mvpView.hideLoading();
                }
            }
        });
    }


    /**
     *  实名认证
     *
     * @param idCard  身份证号
     * @param cardPhoto1 正面
     * @param realName  真实姓名
     * @param cardPhoto2    反面
     */
    public void authentication(String idCard,String cardPhoto1 , String realName , String cardPhoto2){
        Map<String,String> maps = new HashMap<>();
        maps.put( "idCard", idCard);
        maps.put( "cardPhoto1", cardPhoto1);
        maps.put( "realName", realName);
        maps.put( "cardPhoto2", cardPhoto2);
        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.OSNAME, Utils.ANDROID);
        maps.put(Utils.CHANNEL, Utils.ANDROID);
        maps.put(Utils.DEVICENAME, Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.authentication(maps), new ApiCallback<BindSettleAccountBean>() {

            @Override
            public void onSuccess(BindSettleAccountBean model) {
                //綁定成功
                mvpView.bindSuccess(model);
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
