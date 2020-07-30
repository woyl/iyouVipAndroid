package com.jfkj.im.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.mine.InsertUserinfoActivity;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.CustomVideoView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Call;

public class AuditingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
//    @BindView(R.id.tv_message)
//    TextView tv_message;
    @BindView(R.id.btn_back_login)
    Button btn_back_login;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head;



    private Timer timer;
    private TimerTask timerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity,false);
        setStaturBar(rl_head);
        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_right_tv.setOnClickListener(this);
        btn_back_login.setOnClickListener(this);


        SPUtils.getInstance(mActivity).put(Utils.TOKEN,SPUtils.getInstance(mActivity).getString(Utils.TOKEN + "input", UserInfoManger.getToken()));

    }


    @Override
    protected void onResume() {
        super.onResume();

//        timer = new Timer();
//
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                getUserInfo();
//            }
//        };


       // timer.schedule(timerTask, 20000, 20000);

        getUserInfo();
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(timer!=null){
            timerTask.cancel();
            timerTask = null;

            timer.cancel();
            timer = null;
        }

    }



    private void getUserInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());

        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/user/getUserInfo")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    UserInfoBean      userInfoBean = JSON.parseObject(jsonObject.getString("data"), UserInfoBean.class);

                                    if(userInfoBean.getExamine() == 1){

                                        if(userInfoBean.getGender() ==2 ){
                                            Intent intent = new Intent(App.getAppContext(), InsertUserinfoActivity.class);

                                            startActivity(intent);

                                        }else{
                                            //审核通过
                                            Intent intent = new Intent(App.getAppContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            ApiClient.onDestroy();
                                        }

                                    }else if(userInfoBean.getExamine() == 3){
                                        //未通过
                                       Intent intent = new Intent(App.getAppContext(), AuditresultActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra(Utils.TOKEN,SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN) );
                                        intent.putExtra(Utils.SEX, "2");//性别
                                        startActivity(intent);
                                        ApiClient.onDestroy();

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            JumpUtil.overlay(this, Loginregister_phone_Activity.class);
            toastShow(R.string.nonetwork);
            finish();
        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_auditing;
    }
    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_tv:
                Map<String, String> querymap = new HashMap<>();

                querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                querymap.put(Utils.OSNAME, Utils.ANDROID);
                querymap.put(Utils.CHANNEL, Utils.ANDROID);
                querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
                querymap.put(Utils.DEVICEID, Utils.ANDROID);
                querymap.put(Utils.REQTIME, AppUtils.getReqTime());

                OkGo.<String>post(ApiStores.base_url + "/my/customerService")
                        .tag(mActivity)
                        .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                        .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                        .params(querymap)
                        .execute(new com.lzy.okgo.callback.StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                OkLogger.e(response.body());
                                CustomerServiceBean customerServiceBean = GsonUtils.fromJson(response.body(), CustomerServiceBean.class);
                                customerServiceBean.getData().getUrl();

                                Intent uintent = new Intent(getApplicationContext(), WebActivity.class);
                                uintent.putExtra("title", "在线客服");
                                uintent.putExtra("url", customerServiceBean.getData().getUrl());
                                startActivity(uintent);
                            }
                        });
                break;
            case R.id.btn_back_login:
                Intent intent=new Intent(App.getAppContext(), LoginregisternextActivity.class);
                SPUtils.getInstance(mActivity).put(Utils.TOKEN,"");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(App.getAppContext(), LoginregisternextActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(App.getAppContext(), LoginregisternextActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
