package com.jfkj.im.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.inputcode.InputcodePresenter;
import com.jfkj.im.mvp.inputcode.InputcodeView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
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
import okhttp3.ResponseBody;

//这个是初始化密码的页面
public class SetpasswordActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;

    @BindView(R.id.input_password_et)
    EditText input_password_et;
    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;
    @BindView(R.id.enter_btn)
    Button enter_btn;



    @BindView(R.id.video_view)
    VideoView videoView;

    Intent getintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        getintent = getIntent();
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_left_tv.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
        input_password_et.addTextChangedListener(this);
        clear_et_iv.setOnClickListener(this);
        clear_et_iv.setVisibility(View.GONE);
        enter_btn.setOnClickListener(this);
//        enter_btn.setAlpha(0.4f);
        enter_btn.setEnabled(false);
        AppManager.getAppManager().addActivity(this);
        video();
    }


    private void video() {
        Uri parse = Uri.parse("android.resource://" + getPackageName() + "/raw/video_brg");

        videoView.setVideoURI(parse);
        videoView.start();

        //  循环播放的监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();mediaPlayer.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoURI(parse);
                videoView.start();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setpassword;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
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
            case R.id.clear_et_iv:
                input_password_et.setText("");
                break;
            case R.id.enter_btn:
                Map<String, String> headmap = new HashMap<>();
                Map<String, String> map = new HashMap<>();
                map.put(Utils.OSNAME, Utils.ANDROID);
                map.put(Utils.CHANNEL, Utils.ANDROID);
                map.put(Utils.APPVERSION, Utils.getVersionCode()+"");
                map.put(Utils.PASSWORD, input_password_et.getText().toString().trim());
                map.put(Utils.REQ_TIME,AppUtils.getReqTime());
                map.put(Utils.DEVICENAME, Utils.getdeviceName());
                map.put(Utils.DEVICEID, "1");
                headmap.put(Utils.TOKEN, getintent.getStringExtra(Utils.TOKEN));
                headmap.put(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
                if (Utils.netWork()) {
                    OkHttpUtils.post()
                            .tag(mActivity)
                            .url(ApiStores.base_url+"/user/updateGender")//updateGender register
                            .headers(headmap)
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
                                        toastShow(jsonObject.getString("message"));
                                        if (jsonObject.getString("code").equals("200")) {
                                            Intent intent = new Intent(App.getAppContext(), SelectSexActivity.class);
                                            intent.putExtra(Utils.TOKEN, getintent.getStringExtra(Utils.TOKEN));
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    toastShow(R.string.nonetwork);
                }

                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (input_password_et.getText().toString().trim().length() > 5) {
//            enter_btn.setAlpha(1.0f);
            enter_btn.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_bt_two_bg));
            enter_btn.setEnabled(true);
            clear_et_iv.setVisibility(View.VISIBLE);
        } else {
//            enter_btn.setAlpha(0.4f);
            enter_btn.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_tv_bg_gray));
            enter_btn.setEnabled(false);
            clear_et_iv.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
