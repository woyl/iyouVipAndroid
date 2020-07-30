package com.jfkj.im.mvp.crushicetasl;

import android.content.Context;

import com.jfkj.im.App;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.CrushIceTaskHallBean;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class CrushIceTaskHallPresenter extends BasePresenter<CrushIceTaskView> {

    public CrushIceTaskHallPresenter(CrushIceTaskView crushIceTaskView) {
        attachView(crushIceTaskView);
    }

    public void getData(String pageNo,String pageSize){
        mvpView.showLoading();

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.PAGENO, pageNo);
        map.put(Utils.PAGESIZE, pageSize);
        addSubscription(apiStores.crushIceTask(map), new ApiCallback<BaseBeans<CrushIceTaskHallBean>>() {
            @Override
            public void onSuccess(BaseBeans<CrushIceTaskHallBean> model) {
                mvpView.getCrushIce(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getCrushFails(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }

    //单个文件采用多个文件上传接口（这里只能上传图片）
    public void uploadFiles(String fileType, String pathType, File file,String signedFile) {
        if(fileType.equals("1")){
            mvpView.showLoading();
        }

        Map<String, String> map = new HashMap<>();
        map.put("fileType", fileType);
        map.put("pathType", pathType);
        map.put("userId", UserInfoManger.getUserId());
      //  map.put("signedFile",signedFile);
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
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mvpView.getCrushFails(""+id);
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

    public void addFriend(String userId){
        mvpView.showLoading();

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.APPLYID,userId);

        addSubscription(apiStores.addFrend(map),new ApiCallback< BaseBean>(){

            @Override
            public void onSuccess(BaseBean model) {
                mvpView.addFriend();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getCrushFails(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
