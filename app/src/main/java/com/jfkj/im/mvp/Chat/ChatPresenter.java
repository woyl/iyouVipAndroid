package com.jfkj.im.mvp.Chat;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.jfkj.im.App;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.PUT;

public class ChatPresenter extends BasePresenter<ChatView> {

    public ChatPresenter(ChatView chatView) {
        attachView(chatView);
    }


    public void getUserDetail(String userid){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.USERID, userid);

        addSubscription(apiStores.getUserDetail(map), new ApiCallback<UserDetail>() {
            @Override
            public void onSuccess(UserDetail model) {
                 mvpView.getUser(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
    public void loadGroupInfo(String groupid){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.GROUPID, groupid);

        addSubscription(apiStores.loadGroupInfo(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loadGroupInfoSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
    public void uploadImage(String type, String mobileno, MultipartBody.Part file) {
        addSubscription(apiStores.uploadImage(type, mobileno, file), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

                try {
                    mvpView.uploadImageSuccess(model.string());
                } catch (IOException e) {
                    e.printStackTrace();
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

    public void uploadImage(Context context,String fileType, String pathType, File file) {
        OkGo.<String>post(ApiStores.baseupload_url + "/file/uploadSingleFile")
                .tag(context)
                .headers("fileType", fileType)
                .headers("pathType", pathType)
                .params("file", file)
                .isMultipart(true)
                .execute(new com.lzy.okgo.callback.StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        mvpView.uploadImageSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }


    public void paySpeakOrder(String s) {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);


        addSubscription(apiStores.paySpeakOrder(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.paySpeakOrder(model, s);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }



    //单个文件采用多个文件上传接口（这里只能上传图片）
    public void uploadFiles(Context context, String fileType, String pathType, File file) {
        Map<String, String> map = new HashMap<>();
        map.put("fileType", fileType);
        map.put("pathType", pathType);
        map.put("userId", UserInfoManger.getUserId());
        List<MultipartBody.Part> parts = new ArrayList<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("iamge/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        parts.add(body);
        addSubscription(apiStores.uploadFiles(map, parts), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.uploadVideoscreenshotsSuccess(model.string());
                } catch (Exception e) {
                    e.printStackTrace();
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

    public void uploadfile(Context context, String fileType, String pathType, File file, String time, long mediasize) {
        OkGo.<String>post(ApiStores.baseupload_url + "/file/uploadSingleFile")
                .tag(context)
                .headers("fileType", fileType)
                .headers("pathType", pathType)
                .params("file", file)
                .isMultipart(true)
                .execute(new com.lzy.okgo.callback.StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mvpView.uploadaudioSuccess(response.body(), time, mediasize);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        mvpView.uploadaudiofail(response.body());
                    }
                });
    }

    public void uploadViewdeofile(Context context, String fileType, String pathType, File file, String time, long mediasize, String mediapicture) {
        OkGo.<String>post(ApiStores.baseupload_url + "/file/uploadSingleFile")
                .tag(context)
                .headers("fileType", fileType)
                .headers("pathType", pathType)
                .params("file", file)
                .isMultipart(true)
                .execute(new com.lzy.okgo.callback.StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        mvpView.uploadViewdeoSuccess(response.body(), time, mediasize, mediapicture);
                    }

                    @Override
                    public void onError(Response<String> response) {

                        mvpView.uploadaudiofail(response.body());
                    }
                });
    }

    //  context.getExternalCacheDir()+ "/","tata_receive_voice_"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+".mp3")
    public void downloadfile(Context context, String fileurl, String time, String receive_head_url, String sendid,String name) {
        File filex;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filex = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis()+".mp3");
        } else {
            filex = new File(context.getFilesDir(), System.currentTimeMillis()+".mp3");
        }
        RequestParams requestParams = new RequestParams(fileurl);
        requestParams.setSaveFilePath(filex.getAbsolutePath());
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {

                mvpView.downfileSuccess(result, Long.parseLong(time), receive_head_url, sendid,  name);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });

    }

    public void downvideofile(Context context, String fileurl, long time, String receive_head_url, String sendid) {
        File filex;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filex = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis()+".mp4");
        } else {
            filex = new File(context.getFilesDir(), System.currentTimeMillis()+".mp4");
        }
        RequestParams requestParams = new RequestParams(fileurl);
        requestParams.setSaveFilePath(filex.getAbsolutePath());
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                mvpView.downVideoSuccess(result, time, receive_head_url, sendid);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });

    }


    //抢红包检测
    public void checkRedPacket(String redId,int position,String   RedId) {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.REDID, redId);

        addSubscription(apiStores.checkRedPacket(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.checkRedPacket(model.string(),  position,RedId);
                } catch (Exception e) {
                    e.printStackTrace();
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

    //拆红包
    public void receiveRedPacket(String redId, String serialNo) {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.REDID, redId);
        map.put(Utils.SERIALNO, serialNo);

        addSubscription(apiStores.receiveRedPacket(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.receiveRedPacket(model.string(),redId);
                } catch (Exception e) {
                    e.printStackTrace();
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


    //领取礼物
    public void receiveFriendGift(String userid, String orderId, String tradeOrderNo, String giftid) {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.USERID, userid);
        map.put(Utils.ORDERID, orderId);
        map.put(Utils.TRADEORDERNO, tradeOrderNo);

        addSubscription(apiStores.receiveFriendGift(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.receiveFriendGift(model.string(), giftid, tradeOrderNo);
                } catch (IOException e) {
                    e.printStackTrace();
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
    public void getSquareTips( ){
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
          addSubscription(apiStores.getSquareTips(map), new ApiCallback<ResponseBody>() {
              @Override
              public void onSuccess(ResponseBody model) {
                  mvpView.getSquareTipsSuccess(model);
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
