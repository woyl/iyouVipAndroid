package com.jfkj.im.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
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

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.Bean.LoginSuccessBean;
import com.jfkj.im.Bean.VerificationParameterBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomUtil;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.SplashAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Loginpassword.LoginpasswordPresenter;
import com.jfkj.im.mvp.Loginpassword.LoginpasswordView;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.LottieDialog;
import com.jfkj.im.ui.mine.InsertUserinfoActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.ScollLinearLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class Login_passwordActivity extends BaseActivity<LoginpasswordPresenter> implements TextWatcher, View.OnClickListener, LoginpasswordView {
    @BindView(R.id.et_input_password)
    EditText et_input_password;
    @BindView(R.id.btn_enter)
    Button btn_enter;
    @BindView(R.id.fotgetpassword_et)
    TextView fotgetpassword_et;
    @BindView(R.id.login_hidden_iv)
    ImageView login_hidden_iv;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    boolean mbDisplayFlg = true;
    Intent getIntent;
    LoginpasswordPresenter loginpasswordPresenter;
    Dialog codedialog;//这里是获取图形验证码的对话框
    ImageView dialog_close_iv;
    EditText et_code;
    Button diglog_btn_enter;
    ImageView default_code_iv;
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.title_right_tv)
    TextView title_right_tv;


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private LottieDialog loadingDialog;
    private int loginNUm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(rl_head);
        btn_enter.setOnClickListener(this);
        btn_enter.setEnabled(false);
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_left_tv.setOnClickListener(this);
        login_hidden_iv.setOnClickListener(this);
        fotgetpassword_et.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        et_input_password.addTextChangedListener(this);

        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_right_tv.setOnClickListener(this);


        et_input_password.setFilters(new InputFilter[]{filter});
        loginpasswordPresenter = new LoginpasswordPresenter(this);
        getIntent = getIntent();
        codedialog = new Dialog(this, R.style.dialogstyle);
        View dialog_code = LayoutInflater.from(this).inflate(R.layout.dialog_code, null);
        codedialog.setContentView(dialog_code);
        codedialog.setCanceledOnTouchOutside(false);
        diglog_btn_enter = dialog_code.findViewById(R.id.btn_enter);
        diglog_btn_enter.addTextChangedListener(this);
        Window codedialogWindow = codedialog.getWindow();
        WindowManager.LayoutParams params = codedialogWindow.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        codedialogWindow.setAttributes(params);
        dialog_close_iv = dialog_code.findViewById(R.id.dialog_close_iv);
        default_code_iv = dialog_code.findViewById(R.id.default_code_iv);
        default_code_iv.setOnClickListener(this);
        et_code = dialog_code.findViewById(R.id.et_code);
        et_code.addTextChangedListener(this);
        dialog_close_iv.setOnClickListener(this);
        diglog_btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginpasswordPresenter.verification(getIntent.getStringExtra(Utils.MOBILENO), Utils.TYPES_FORGETPASSWORD, et_code.getText().toString().trim(), getIntent.getStringExtra(Utils.AREACODE));
            }
        });


        mRecyclerView.setAdapter(new SplashAdapter(this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);
    }




    @Override
    protected void onStop() {
        super.onStop();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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

            case R.id.btn_enter:

                if (Utils.netWork()) {
                    Log.d("@@@", "UserInfoManger.getRegistrationID()" + UserInfoManger.getRegistrationID());
                    ApiClient.onDestroy();

                    loginpasswordPresenter.login(getIntent.getStringExtra(Utils.MOBILENO), et_input_password.getText().toString().trim(),UserInfoManger.getLongitude(),UserInfoManger.getLatitude(),UserInfoManger.getcity());

                } else {
                    toastShow(R.string.nonetwork);
                }

                break;
            case R.id.login_hidden_iv:
                if (mbDisplayFlg) {
                    et_input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    login_hidden_iv.setImageResource(R.drawable.login__nor_iv);
                } else {
                    et_input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    login_hidden_iv.setImageResource(R.mipmap.login_hidden_iv);
                }
                mbDisplayFlg = !mbDisplayFlg;
//                fotgetpassword_et.postInvalidate();
                // 切换后将EditText光标置于末尾
//                CharSequence charSequence = fotgetpassword_et.getText();
//                if (charSequence instanceof Spannable) {
//                    Spannable spanText = (Spannable) charSequence;
//                    Selection.setSelection(spanText, charSequence.length());
//                }
                break;
            case R.id.fotgetpassword_et:

                if (Utils.netWork()) {
                    codedialog.show();
                    et_code.setText("");
                    loginpasswordPresenter.getCode(getIntent.getStringExtra(Utils.MOBILENO));
                } else {
                    toastShow(R.string.nonetwork);
                }

                break;
            case R.id.iv_clear:
                et_input_password.setText("");
                break;
            case R.id.default_code_iv:
                loginpasswordPresenter.getCode(getIntent.getStringExtra(Utils.MOBILENO));
                break;
            case R.id.dialog_close_iv:
                codedialog.dismiss();
                break;
            case R.id.title_left_tv:
                finish();
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_password;
    }

    @Override
    public LoginpasswordPresenter createPresenter() {
        return loginpasswordPresenter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (et_input_password.getText().toString().trim().length() > 5 && et_input_password.getText().toString().trim().length() < 21) {
            btn_enter.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
            btn_enter.setEnabled(true);
            btn_enter.setAlpha(1.0f);
        } else {
            btn_enter.setEnabled(false);
            btn_enter.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
            btn_enter.setAlpha(0.5f);
        }
        if (et_input_password.getText().toString().trim().length() != 0) {
            iv_clear.setVisibility(View.VISIBLE);
           // login_hidden_iv.setVisibility(View.VISIBLE);
        } else {
           // login_hidden_iv.setVisibility(View.GONE);
            iv_clear.setVisibility(View.GONE);
        }
        if (et_code.getText().toString().trim().length() == 4) {
            diglog_btn_enter.setEnabled(true);
            diglog_btn_enter.setAlpha(1.0f);
      //      diglog_btn_enter.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_black_20dp));
        } else {
            diglog_btn_enter.setEnabled(false);
            diglog_btn_enter.setAlpha(0.5f);
        //    diglog_btn_enter.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_black_20dp));

        }


        if (charSequence.toString().contains(" ")) {
            String[] str = charSequence.toString().split(" ");
            String str1 = "";
            for (int j = 0; j < str.length; j++) {
                str1 += str[j];
            }
            et_input_password.setText(str1);
            et_input_password.setSelection(i);

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void loginSuccess(String responseBody) {
        try {

            JSONObject jsonObject = new JSONObject(responseBody);

            if (jsonObject.getString("code").equals("200")) {

                LoginSuccessBean loginSuccessBean = JSON.parseObject(responseBody, LoginSuccessBean.class);
                UserInfoManger.saveGender( loginSuccessBean.getData().getGender() + "");
              //  toastShow(jsonObject.getString("message"));
                LoginBean loginBean = JSON.parseObject(responseBody, LoginBean.class);
                UserInfoManger.saveToken(jsonObject.getJSONObject("data").getString("token"));
                loginpasswordPresenter.getUserInfo(jsonObject.getJSONObject("data").getString("token"), loginBean);

                SPUtils.getInstance(mActivity).put(Utils.IS_NEW_USER,loginBean.getData().getIsNewUser() + "");

            }else{
                ToastUtils.showShortToast(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginfail(String s) {

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
    public void getCodeSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                Intent fot_intent = new Intent(Login_passwordActivity.this, ForgetInputcodeActivity.class);
                fot_intent.putExtra(Utils.MOBILENO, getIntent.getStringExtra(Utils.MOBILENO));
                fot_intent.putExtra(Utils.AREACODE, getIntent.getStringExtra(Utils.AREACODE));
                startActivity(fot_intent);
                finish();
                codedialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verificationParameterSuccess(String s, String token, LoginBean loginBean) {


        /*
        "code": "200",
        "message": "查询成功",
        -"data": {
        "gender": "0未设置性别,1男 2女",
        "examine": "0未提交审核  1 审核通过  2审核中  3审核未通过",
        "vipLevel": "等级",
        "vipCard": "0无，1银卡，2金卡，3黑卡",
        "optState": "0男性未选好友，1注册已选好友",
        "userId": "用户id"
         */
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("200")) {
                SPUtils.getInstance(getApplication()).put(Utils.USERID, loginBean.getData().getUserid());
                SPUtils.getInstance(getApplication()).put(Utils.USER_SIG, loginBean.getData().getUserSig());
                SPUtils.getInstance(getApplication()).put(Utils.TOKEN, loginBean.getData().getToken());
                SPUtils.getInstance(getApplication()).put(Utils.AV_CHAT_ROOM_ID, loginBean.getData().getAVChatRoomId());
                UserInfoManger.savaUserInfo(jsonObject.getString("data"));
                LogTencent(s,loginBean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LogTencent(String s, LoginBean loginBean) {
        TUIKit.login(SPUtils.getInstance(mActivity).getString(Utils.USERID), SPUtils.getInstance(mActivity).getString(Utils.USER_SIG), new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                SPUtils.getInstance(mActivity).put(Constants.AUTO_LOGIN, true);

                go(s,loginBean);
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                loginNUm ++;
                switch (errCode){
                    case 6014:
                        toastShow("请重新登录");
                        break;
                    case 6026:
                        toastShow("请重新登录，登录错误");
                        break;
                    case 6206:
                        toastShow("UserSig 过期");
                        break;
                    case 6208:
                        toastShow("其他终端登录同一个帐号，引起已登录的帐号被踢，需重新登录");
                        break;
                    case 7501:
                        toastShow("登录正在执行中");
                        break;
                    case 7502:
                        toastShow("请重新登录，登录失败");
                        break;
                    case 7503:
                        toastShow("初始化失败，内部错误");
                        break;
                    case 7504:
                        toastShow("未初始化，内部错误");
                        break;
                    case 7505:
                        toastShow("包格式错误，内部错误");
                        break;
                    case 7506:
                        toastShow("解密失败，内部错误");
                        break;
                    case 7507:
                        toastShow("请求失败，内部错误");
                        break;
                    case 7508:
                        toastShow("请求超时，内部错误");
                        break;
                    case 70014:
                        toastShow(" UserSig失效");
                        break;
                }
                if (loginNUm <= 5 ){
                    LogTencent(s,loginBean);
                }
            }
        });
    }

    private void go(String s, LoginBean loginBean) {
        VerificationParameterBean.DataBean data = JSON.parseObject(s, VerificationParameterBean.class).getData();

        if(data.getExamine() == 1){
            String isNewUser = SPUtils.getInstance(mActivity).getString(Utils.IS_NEW_USER);

            if(isNewUser.equals("1")  && loginBean.getData().getGender() == 2 ){
                Intent intent = new Intent(App.getAppContext(), InsertUserinfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                ApiClient.onDestroy();
            }else{
                //审核通过 , 并且不是第一次登录
                SPUtils.getInstance(mActivity).put(Utils.SEQROOM, loginBean.getData().getSeqRoom());
                SPUtils.getInstance(mActivity).put(Utils.CIRCLEID, loginBean.getData().getCircleRoomId());
                Intent intent = new Intent(App.getAppContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ApiClient.onDestroy();
            }


        }else if(data.getExamine() ==0){


            if(data.getGender() == 1){
                //男性
                if(data.getOptState() == 0){
                    //男性未选择好友
                    Intent  intent = new Intent(App.getAppContext(), ChooseLikePersonActivity.class);
                    startActivity(intent);
                }else{
                    //已选择好友, 是否购买会员
                    if(data.getVipCard() == 0){
                        //进入购买会员卡页面
                        startActivity(new Intent(this,BuyMemberActivity.class));
                    }else if(data.getVipCard() == 1){
                        //银卡 ,进入审核流程
                        if(data.getExamine() == 0){
                            //未提交审核
                            Intent uploadpictures_code = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                            uploadpictures_code.putExtra(Utils.TOKEN, UserInfoManger.getToken());
                            startActivity(uploadpictures_code);
                        }else  if(data.getExamine() ==2 ){
                            //审核中
                            Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }else{
                            //审核失败
                            Intent intent = new Intent(App.getAppContext(), AuditresultActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }else if (data.getVipCard() == 2  || data.getVipCard() ==3){
                        //黑卡 金卡用户 直接进入首页
                        SPUtils.getInstance(getApplication()).put(Utils.SEQROOM, loginBean.getData().getSeqRoom());
                        SPUtils.getInstance(getApplication()).put(Utils.CIRCLEID, loginBean.getData().getCircleRoomId());
                        Intent intent = new Intent(App.getAppContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        ApiClient.onDestroy();
                    }
                }
            }else if(data.getGender() ==2){
                //女性
                if(data.getExamine() == 0){
                    //未提交审核
                    Intent uploadpictures_code = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                    startActivity(uploadpictures_code);
                }else  if(data.getExamine() ==2 ){
                    //审核中
                    Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    //审核失败
                    Intent intent = new Intent(App.getAppContext(), AuditresultActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }else{
                //未选择性别
                Intent intent_code = new Intent(App.getAppContext(), SelectSexActivity.class);
                startActivity(intent_code);
                return;
            }
        }else  if(data.getExamine() ==2 ){
            //审核中
            Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }else if(data.getExamine() ==3 ){
            //审核失败
            Intent intent = new Intent(App.getAppContext(), AuditresultActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void showLoading() {
        loadingDialog = new LottieDialog(this);
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }


    private InputFilter filter=new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(source.equals(" ")||source.toString().contentEquals("\n"))return "";
            else return null;
        }
    };





}
