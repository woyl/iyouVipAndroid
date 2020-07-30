package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class LoginAccountActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    @BindView(R.id.et_input_phone)
    EditText et_input_phone;
    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;
    @BindView(R.id.btn_next)
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_left_tv.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
        et_input_phone.addTextChangedListener(this);
        clear_et_iv.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        clear_et_iv.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_account;
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
                querymap.put(Utils.DEVICENAME,Utils.getdeviceName());
                querymap.put(Utils.DEVICEID, Utils.ANDROID);
                querymap.put(Utils.REQTIME, AppUtils.getReqTime());

                OkGo.<String>post(ApiStores.base_url + "/my/customerService")
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

                                Intent uintent = new Intent(getApplicationContext(), WebActivity.class);
                                uintent.putExtra("title", "在线客服");
                                uintent.putExtra("url", customerServiceBean.getData().getUrl());
                                startActivity(uintent);
                            }
                        });
                break;
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.clear_et_iv:
                et_input_phone.setText("");
                break;
            case R.id.btn_next:
                if(!et_input_phone.getText().toString().trim().startsWith("1")){
                    toastShow("手机号码输入错误");
                    return;
                }
                Intent intent_phone=new Intent(App.getAppContext(),Login_passwordActivity.class);
                intent_phone.putExtra(Utils.MOBILENO,et_input_phone.getText().toString().trim());
                startActivity(intent_phone);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       if(et_input_phone.getText().toString().trim().length()>0){
           clear_et_iv.setVisibility(View.VISIBLE);
       }else {
           clear_et_iv.setVisibility(View.GONE);
       }
        if(et_input_phone.getText().toString().trim().length()==11){
            btn_next.setEnabled(true);
            btn_next.setAlpha(1.0f);
        }else {
            btn_next.setEnabled(false);
            btn_next.setAlpha(0.5f);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
