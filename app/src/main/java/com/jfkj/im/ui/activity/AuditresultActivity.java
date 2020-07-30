package com.jfkj.im.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

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
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
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

import butterknife.BindView;
import okhttp3.Call;

public class AuditresultActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_repeat)
    Button btn_repeat;
    @BindView(R.id.btn_back_login)
    Button btn_back_login;
    @BindView(R.id.tv_message1)
    TextView tv_message1;


    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.title_right_tv)
    TextView title_right_tv;

    Intent getIntent;
    String examine="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(rl_head);
        getIntent = getIntent();
        btn_repeat.setOnClickListener(this);
        btn_back_login.setOnClickListener(this);

        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_right_tv.setOnClickListener(this);


        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID()+"");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        Map<String, String> mapheaad = new HashMap<>();
        mapheaad.put(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));
        mapheaad.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));

        OkHttpUtils.post()
                .tag(mActivity)
                .url(ApiStores.base_url + "/user/getUserExamine")
                .headers(mapheaad)
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
                            if (jsonObject.getString("code").equals("200")) {
                                tv_message1.setText(jsonObject.getJSONObject("data").getString("reason"));
                                examine=jsonObject.getJSONObject("data").getString("examine");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });



    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_auditresult;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_repeat:
                Intent intent=new Intent(mActivity,UploadpicturesActivity.class);
                intent.putExtra(Utils.TOKEN,getIntent.getStringExtra(Utils.TOKEN));
                startActivity(intent);
                finish();
                break;
            case R.id.btn_back_login:
                startActivity(new Intent(App.getAppContext(), LoginregisternextActivity.class));
                finish();
                break;

            case R.id.title_right_tv:
                Map<String,String> querymap = new HashMap<>();

                querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                querymap.put(Utils.OSNAME, Utils.ANDROID);
                querymap.put(Utils.CHANNEL, Utils.ANDROID);
                querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
                querymap.put(Utils.DEVICEID, Utils.ANDROID);
                querymap.put(Utils.REQTIME, AppUtils.getReqTime());

                OkGo.<String>post(ApiStores.base_url+"/my/customerService")
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

                                Intent uintent = new Intent(getApplicationContext(),WebActivity.class);
                                uintent.putExtra("title", "在线客服");
                                uintent.putExtra("url",   customerServiceBean.getData().getUrl());
                                startActivity(uintent);
                            }
                        });
                break;
        }
    }



    @Override
    protected void onStop() {
        super.onStop();

    }
}
