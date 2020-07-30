package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.SplashAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.ScollLinearLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

public class Login_accountpasswordActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    @BindView(R.id.et_input_phone)
    EditText et_input_phone;
    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head;


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(rl_head);
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_right_tv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        clear_et_iv.setOnClickListener(this);

        et_input_phone.addTextChangedListener(this);
        clear_et_iv.setVisibility(View.GONE);


        mRecyclerView.setAdapter(new SplashAdapter(this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);
    }


//    @Override
//    protected void onStart() {
//        video();
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        customVideoView.stopPlayback();
//        super.onStop();
//    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_accountpassword;
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
                        .execute(new StringCallback() {
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
            case R.id.btn_next:


                String trim = et_input_phone.getText().toString().trim();
                char c = trim.charAt(0);
                String s = String.valueOf(c);

                if(s.equals("1")){


                    Map<String,String> map = new HashMap<>();

                    map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                    map.put(Utils.OSNAME, Utils.ANDROID);
                    map.put(Utils.CHANNEL, Utils.ANDROID);
                    map.put(Utils.DEVICENAME, Utils.getdeviceName());
                    map.put(Utils.DEVICEID, Utils.ANDROID);
                    map.put(Utils.REQTIME,  AppUtils.getReqTime());
                    map.put(Utils.MOBILENO,trim);

                    OkGo.<String>post(ApiStores.base_url+"/user/checkMobile")
                            .tag(mActivity)
                            .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                            .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                            .params(map)

                            .execute(new com.lzy.okgo.callback.StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    String body = response.body();

                                    if(body.contains("10003")){
                                        ToastUtils.showShortToast("该手机号还未注册");

                                    }else{
                                        Intent intent = new Intent(Login_accountpasswordActivity.this, Login_passwordActivity.class);
                                        intent.putExtra(Utils.MOBILENO, et_input_phone.getText().toString().trim());
                                        intent.putExtra(Utils.AREACODE, "86");
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onError(Response<String> response) {

                                }
                            });


                }else{
                    ToastUtils.showShortToast("请输入正确的手机号");
                }

                break;
            case R.id.clear_et_iv:
                et_input_phone.setText("");
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                if(data!=null){
                    if(data.getStringExtra("code")!=null){

                    }
                }
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (et_input_phone.getText().toString().trim().length() == 11) {
            btn_next.setEnabled(true);
            btn_next.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
            btn_next.setAlpha(1.0f);
        } else {
            btn_next.setEnabled(false);
            btn_next.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
            btn_next.setAlpha(0.4f);
        }
        if (et_input_phone.getText().toString().trim().length() > 0) {
            clear_et_iv.setVisibility(View.VISIBLE);
        } else {
            clear_et_iv.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
