package com.jfkj.im.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.GsonUtils;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.Bean.VerificationParameterBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.SplashAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.inputcode.InputcodePresenter;
import com.jfkj.im.mvp.inputcode.InputcodeView;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.ui.mine.InsertUserinfoActivity;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.ScollLinearLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class InputcodeActivity extends BaseActivity<InputcodePresenter> implements View.OnClickListener, TextWatcher, InputcodeView {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.tv_second)
    TextView tv_second;
    @BindView(R.id.btn_input_enter)
    Button btn_enter;
    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;
    Intent getIntent;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    MyCountDownTimer myCountDownTimer;
    InputcodePresenter inputcodePresenter;
    TelephonyManager telephonyManager;
    Dialog codedialog;
    EditText et_code;
    ImageView default_code_iv;
    Button diglog_btn_enter;
    ImageView dialog_close_iv;
    private Double latitudeDouble;
    private String city;
    private Double longitude;
    private String locality = "深圳";
    private Location location;
    private int loginNUm = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(rl_head);
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_left_tv.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
        btn_enter.setOnClickListener(this);
        tv_second.setOnClickListener(this);
        clear_et_iv.setOnClickListener(this);
        et_input.addTextChangedListener(this);
        getIntent = getIntent();
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        myCountDownTimer.start();
        tv_code.setText(getIntent.getStringExtra(Utils.MOBILENO));
        clear_et_iv.setVisibility(View.INVISIBLE);
//        btn_enter.setAlpha(0.4f);
        btn_enter.setEnabled(false);
        inputcodePresenter = new InputcodePresenter(this);
//        inputcodePresenter.getPhonecode(getIntent.getStringExtra(VideoUtils.MOBILENO), VideoUtils.TYPES_REGISTER,);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tv_second.setEnabled(true);
        AppManager.getAppManager().addActivity(this);
        initView();


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
//            //开启定位权限,200是标识码
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//        } else {
//            initLocationOption();
//            Toast.makeText(mActivity, "已开启动态权限", Toast.LENGTH_SHORT).show();
//        }


        //getLocation();



        mRecyclerView.setAdapter(new SplashAdapter(this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);


    }





    public void initView() {
        codedialog = new Dialog(this, R.style.dialogstyle);
        View dialog_code = LayoutInflater.from(this).inflate(R.layout.dialog_code, null);
        codedialog.setContentView(dialog_code);
        Window codedialogWindow = codedialog.getWindow();
        WindowManager.LayoutParams params = codedialogWindow.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        codedialogWindow.setAttributes(params);
        et_code = dialog_code.findViewById(R.id.et_code);
        default_code_iv = dialog_code.findViewById(R.id.default_code_iv);
        dialog_close_iv = dialog_code.findViewById(R.id.dialog_close_iv);
        diglog_btn_enter = dialog_code.findViewById(R.id.btn_enter);
        diglog_btn_enter.setOnClickListener(this);
        default_code_iv.setOnClickListener(this);
        dialog_close_iv.setOnClickListener(this);
        et_code.addTextChangedListener(this);
//        diglog_btn_enter.setAlpha(0.5f);
        diglog_btn_enter.setEnabled(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inputcode;
    }

    @Override
    public InputcodePresenter createPresenter() {
        return inputcodePresenter;
    }


    //入口是getLocation

    /**
     * 定位：权限判断
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        //检查定位权限
        ArrayList<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

//        //判断
//        if (permissions.size() == 0) {//有权限，直接获取定位
//            getLocationLL();
//        } else {//没有权限，获取定位权限
//            requestPermissions(permissions.toArray(new String[permissions.size()]), 2);
//        }
    }


    /**
     * 定位：权限监听
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2://定位
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "未同意获取定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.title_right_tv:
                //客服

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
            case R.id.btn_input_enter:
                if (Utils.netWork()) {
                    ApiClient.onDestroy();

                    PermissionsUtil.requestPermission(getApplication(), new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permissions) {




                            inputcodePresenter.phoneLoginYZM(getIntent.getStringExtra(Utils.MOBILENO), Utils.TYPES_REGISTER, et_input.getText().toString().trim(), UserInfoManger.getLongitude() + "", UserInfoManger.getLatitude() + "", UserInfoManger.getcity());

                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permissions) {
                            ToastUtils.showShortToast("没有位置权限");
                        }
                    }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);


                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.tv_second:
                if (Utils.netWork()) {
                    //   inputcodePresenter.getPhonecode(getIntent.getStringExtra(VideoUtils.MOBILENO), VideoUtils.TYPES_REGISTER,);
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
            case R.id.default_code_iv:
                inputcodePresenter.getCode(getIntent.getStringExtra(Utils.MOBILENO));
                break;
            case R.id.btn_enter:
                codedialog.dismiss();
                if (Utils.netWork()) {
                    inputcodePresenter.verification(getIntent.getStringExtra(Utils.MOBILENO), Utils.TYPES_LOGIN, et_code.getText().toString().trim(), getIntent.getStringExtra(Utils.AREACODE));
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.dialog_close_iv:
                codedialog.dismiss();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (et_input.getText().toString().trim().length() == 6) {
            btn_enter.setAlpha(1.0f);
            btn_enter.setEnabled(true);
//            btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
        } else {
            btn_enter.setAlpha(0.4f);
            btn_enter.setEnabled(false);
//            btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shap_tv_bg_gray));
        }
        if (et_input.getText().toString().trim().length() > 0) {
            clear_et_iv.setVisibility(View.VISIBLE);
        } else {
            clear_et_iv.setVisibility(View.INVISIBLE);
        }
        if (et_code.getText().toString().trim().length() == 4) {
         //   diglog_btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
            diglog_btn_enter.setAlpha(1.0f);
            diglog_btn_enter.setEnabled(true);
        } else {
            diglog_btn_enter.setAlpha(0.5f);
          //  diglog_btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shap_tv_bg_gray));
            diglog_btn_enter.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void loginregister(Response<String> body) {
        try {

            String string = body.body();

            JSONObject jsonObject = new JSONObject(string);

            if (jsonObject.getString("code").equals("200")) {
                toastShow(jsonObject.getString("message"));
                LoginBean loginBean = JSON.parseObject(string, LoginBean.class);
                UserInfoManger.savaUserInfo("");

                SPUtils.getInstance(mActivity).put(Utils.USERID, loginBean.getData().getUserid());
                SPUtils.getInstance(mActivity).put(Utils.USER_SIG, loginBean.getData().getUserSig());
                SPUtils.getInstance(mActivity).put(Utils.TOKEN, loginBean.getData().getToken());
                SPUtils.getInstance(mActivity).put(Utils.AV_CHAT_ROOM_ID, loginBean.getData().getAVChatRoomId());


//                UserInfoManger.saveGender(loginBean.getData().getGender() + "");

                SPUtils.getInstance(mActivity).put(Utils.IS_NEW_USER,loginBean.getData().getIsNewUser() + "");


                SPUtils.getInstance(mActivity).put(Utils.TOKEN + "input", loginBean.getData().getToken());
                UserInfoManger.saveToken(loginBean.getData().getToken());

                inputcodePresenter.getUserInfo(jsonObject.getJSONObject("data").getString("token"), loginBean);
            } else {
                toastShow(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        } catch (Exception e) {
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

            if (jsonObject.getString("code").equals("200")) {
                myCountDownTimer.start();
            } else {
                toastShow(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCodefail(String fail) {

    }

    @Override
    public void updatePassword(ResponseBody responseBody) {

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
                UserInfoManger.savaUserInfo(jsonObject.getString("data"));
                UserInfoBean userInfoBean = JSON.parseObject(jsonObject.getString("data"), UserInfoBean.class);

                SPUtils.getInstance(mActivity).put(Utils.USERID, loginBean.getData().getUserid());
                SPUtils.getInstance(mActivity).put(Utils.USER_SIG, loginBean.getData().getUserSig());
                SPUtils.getInstance(mActivity).put(Utils.TOKEN, loginBean.getData().getToken());
                SPUtils.getInstance(mActivity).put(Utils.AV_CHAT_ROOM_ID, Urls.square_chat);
                UserInfoManger.saveToken(loginBean.getData().getToken());
                UserInfoManger.saveGender(userInfoBean.getGender() + "");
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
                loginNUm++;
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
                        toastShow("请求超时，内部错误")   ;
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

    private void go(String s,LoginBean loginBean) {
        VerificationParameterBean.DataBean data = JSON.parseObject(s, VerificationParameterBean.class).getData();

        if(data.getExamine() == 1){
            String isNewUser = SPUtils.getInstance(mActivity).getString(Utils.IS_NEW_USER);

            if(isNewUser.equals("1") && loginBean.getData().getGender() == 2){
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



        }
        else if(data.getExamine() ==0){


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
                        SPUtils.getInstance(mActivity).put(Utils.SEQROOM, loginBean.getData().getSeqRoom());
                        SPUtils.getInstance(mActivity).put(Utils.CIRCLEID, loginBean.getData().getCircleRoomId());
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
            tv_second.setText("重新发送");
            tv_second.setEnabled(true);
        }
    }
}
