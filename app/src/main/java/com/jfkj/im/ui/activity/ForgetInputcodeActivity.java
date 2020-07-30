package com.jfkj.im.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.SplashAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.inputcode.InputcodePresenter;
import com.jfkj.im.mvp.inputcode.InputcodeView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.CustomVideoView;
import com.jfkj.im.view.ScollLinearLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.ResponseBody;

//这个页面是忘记密码 输入的验证码
public class ForgetInputcodeActivity extends BaseActivity<InputcodePresenter> implements View.OnClickListener, TextWatcher, InputcodeView {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    @BindView(R.id.tv_send_code)
    TextView tv_code;
    @BindView(R.id.input_password_et)
    EditText input_password_et;
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.tv_second)
    TextView tv_second;
    @BindView(R.id.btn_forgetcode_enter)
    Button btn_forgetcode_enter;
    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.iv_show_password)
    ImageView iv_show_password;


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;


    Intent getIntent;
    MyCountDownTimer myCountDownTimer;
    InputcodePresenter inputcodePresenter;
    Dialog codedialog;
    EditText et_code;
    ImageView default_code_iv;

    ImageView dialog_close_iv;
    Button diglog_btn_enter;


    boolean isSee = false;
    boolean isSeeis = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        setStaturBar(rl_head);
        title_left_tv.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
        setEditTextInputSpace(input_password_et);

        btn_forgetcode_enter.setOnClickListener(this);
        tv_second.setOnClickListener(this);
        clear_et_iv.setOnClickListener(this);
        iv_show_password.setOnClickListener(this);
        et_input.addTextChangedListener(this);
        input_password_et.addTextChangedListener(this);


        getIntent = getIntent();
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        tv_code.setText("验证码已发送至+ " + getIntent.getStringExtra(Utils.MOBILENO) + "；密码需要6-20位");
        clear_et_iv.setVisibility(View.INVISIBLE);
        inputcodePresenter = new InputcodePresenter(this);
        tv_second.setEnabled(true);
        initView();

        mRecyclerView.setAdapter(new SplashAdapter(this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);
    }

    public void initView() {
        codedialog = new Dialog(this, R.style.dialogstyle);
        View dialog_code = LayoutInflater.from(this).inflate(R.layout.dialog_code, null);
        codedialog.setContentView(dialog_code);
        codedialog.setCanceledOnTouchOutside(false);
        Window codedialogWindow = codedialog.getWindow();
        WindowManager.LayoutParams params = codedialogWindow.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        codedialogWindow.setAttributes(params);
        dialog_close_iv = dialog_code.findViewById(R.id.dialog_close_iv);
        et_code = dialog_code.findViewById(R.id.et_code);
        default_code_iv = dialog_code.findViewById(R.id.default_code_iv);
        diglog_btn_enter = dialog_code.findViewById(R.id.btn_enter);
        diglog_btn_enter.setOnClickListener(this);
        default_code_iv.setOnClickListener(this);
        dialog_close_iv.setOnClickListener(this);
        et_code.addTextChangedListener(this);
//        diglog_btn_enter.setAlpha(0.5f);
        diglog_btn_enter.setEnabled(false);
        //  codedialog.show();
        //      inputcodePresenter.getCode(getIntent.getStringExtra(Utils.MOBILENO));
        myCountDownTimer.start();





    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fortgetinputcode;
    }

    @Override
    public InputcodePresenter createPresenter() {
        return inputcodePresenter;
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
            case R.id.btn_forgetcode_enter:
                if (Utils.netWork()) {
                    inputcodePresenter.updatePassword(getIntent.getStringExtra(Utils.MOBILENO), input_password_et.getText().toString(), et_input.getText().toString().trim());
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.tv_second://重新发送
                if (Utils.netWork()) {
                    codedialog.show();
                    et_code.setText("");
                    inputcodePresenter.getCode(getIntent.getStringExtra(Utils.MOBILENO));

                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.clear_et_iv:
                et_input.setText("");
                break;
            case R.id.btn_enter://这个是点击对话框获取验证码的部分
                codedialog.dismiss();
                if (Utils.netWork()) {
                    inputcodePresenter.verification(getIntent.getStringExtra(Utils.MOBILENO), Utils.TYPES_FORGETPASSWORD, et_code.getText().toString().trim(), getIntent.getStringExtra(Utils.AREACODE));
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.dialog_close_iv:
                codedialog.dismiss();
                break;

            case R.id.iv_show_password:
                if (isSee) {
                    if (isSeeis) {
                        iv_show_password.setImageResource(R.drawable.login__nor_iv);
                        isSeeis = false;
                        input_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        iv_show_password.setImageResource(R.mipmap.login_hidden_iv);
                        isSeeis = true;
                        input_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
        if (et_input.getText().toString().trim().length() == 6 && input_password_et.getText().toString().trim().length() > 5) {
            btn_forgetcode_enter.setAlpha(1.0f);
            btn_forgetcode_enter.setEnabled(true);
            btn_forgetcode_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
        } else {
            btn_forgetcode_enter.setAlpha(0.4f);
            btn_forgetcode_enter.setEnabled(false);
            btn_forgetcode_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
        }
        if (et_input.getText().toString().trim().length() > 0) {
            clear_et_iv.setVisibility(View.VISIBLE);
        } else {
            clear_et_iv.setVisibility(View.INVISIBLE);
        }
        if (et_code.getText().toString().trim().length() == 4) {
            diglog_btn_enter.setAlpha(1.0f);
            diglog_btn_enter.setEnabled(true);
//            diglog_btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shap_bt_two_bg));
        } else {
            diglog_btn_enter.setAlpha(0.5f);
            diglog_btn_enter.setEnabled(false);
//            diglog_btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shap_tv_bg_gray));
        }


        if (et_input.getText().toString().trim().length() > 0) {
            isSee = true;
            //  mIvClearInput.setVisibility(View.VISIBLE);
            // getmIvBack.setImageResource(R.drawable.login__nor_iv);
        } else {
            isSee = false;
            //   mIvClearInput.setVisibility(View.GONE);
            //  getmIvBack.setImageResource(R.drawable.login_hidden_iv);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public void loginregister(String string) {
        try {

            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
//                Intent intent_code = new Intent(App.getAppContext(), SetnewpasswordActivity.class);
//                intent_code.putExtra(Utils.MOBILENO, getIntent.getStringExtra(Utils.MOBILENO));
//                startActivity(intent_code);
//                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loginregister(Response<String> responseBody) {

    }

    @Override
    public void getloginregisterFail(String s) {

    }

    @Override
    public void getBitmaSuccess(ResponseBody responseBody) {
        try {
            byte[] bytes = responseBody.bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            default_code_iv.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBitmapfail(String s) {

    }

    @Override
    public void getCodeSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                myCountDownTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCodefail(String fail) {
        toastShow(R.string.nonetwork);
    }

    @Override
    public void updatePassword(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                Intent intent = new Intent(mActivity, Login_accountpasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verificationParameterSuccess(String s, String token, LoginBean loginBean) {

    }

    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_second.setText(millisUntilFinished / 1000 + "s");
            tv_second.setEnabled(false);
        }

        @Override
        public void onFinish() {
            tv_second.setText("重新获取验证码");
            tv_second.setEnabled(true);
        }
    }
}
