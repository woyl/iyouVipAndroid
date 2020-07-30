package com.jfkj.im.mvp.party;

import android.os.Build;
import android.util.Log;

import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.model.DiscoveryModel;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.utils.OkLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PartyComplaintsPresenter extends BasePresenter<PartyComplaintsView> {
      private DiscoveryModel mHomeModel;


    public PartyComplaintsPresenter(PartyComplaintsView partyDetailsView) {
        attachView(partyDetailsView);
        mHomeModel = new DiscoveryModel();
    }

    String filePath = "";

    public void upLoadFile(List<LocalMedia> list,String partyId,String reason,String userId){
        mvpView.showLoading();



            //多文件上传
            Map<String, String> map = new HashMap<>();
            map.put("fileType", "1");
            map.put("pathType", "5");
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
            addSubscription(mHomeModel.uploadFiles(map, parts), new HttpErrorMsgObserver<ResponseFileUrl>() {
                @Override
                public void onNext(ResponseFileUrl o) {
                    if (o.isSuccess()) {

                        for (int i =0 ; i < list.size() ; i++){
                            filePath += list.get(i)+",";
                        }
                        String substring = filePath.substring(0, filePath.length() - 1);
                        submitComplaint(substring,partyId,reason,userId);

                    } else {
                        ToastUtils.showShortToast(o.getMessage());
                    }
                    mvpView.hideLoading();
                }
            });



    }

    public void submitComplaint(String photo,String partyId, String reason,String userId){
        Map maps = new HashMap();
        maps.put(Utils.OSNAME, Utils.ANDROID);
        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.CHANNEL, Utils.ANDROID);
        maps.put(Utils.DEVICENAME,Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());
        maps.put("photo",photo);
        maps.put("partyId",partyId);
        maps.put("reason",reason);
        maps.put("userId",userId);


        addSubscription(apiStores.submitComplaint(maps), new ApiCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean model) {
                mvpView.complaints(model);
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
