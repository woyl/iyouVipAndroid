package com.jfkj.im.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.SplashAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Loginregisternext.LoginregisternextPresenter;
import com.jfkj.im.mvp.Loginregisternext.LoginregisternextView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.utils.AppManager;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginregisternextActivity extends BaseActivity<LoginregisternextPresenter> implements View.OnClickListener, TextWatcher, LoginregisternextView {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    @BindView(R.id.et_input_phone)
    EditText et_input_phone;
    @BindView(R.id.next_btn)
    TextView next_btn;
    @BindView(R.id.password_login_tv)
    TextView password_login_tv;
    @BindView(R.id.clear_et_iv)
    ImageView clear_et_iv;


    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;



    Dialog codedialog;
    EditText et_code;
    ImageView default_code_iv;
    Button btn_enter;
    LoginregisternextPresenter presenter;
    ImageView dialog_close_iv;
    boolean getLocation = false;

    private String PERMISSION_TIPS = "您尚未开启定位，请去手机设置中开启定位功能。";
    private  LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(rl_head);
        title_left_tv.setBackgroundResource(R.drawable.back_iv);
        title_left_tv.setOnClickListener(this);

        title_right_tv.setBackgroundResource(R.mipmap.customer_service_iv);
        title_right_tv.setOnClickListener(this);
        password_login_tv.setOnClickListener(this);
        clear_et_iv.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        et_input_phone.addTextChangedListener(this);
        AppManager.getAppManager().addActivity(this);

        codedialog = new Dialog(this, R.style.dialogstyle);
        presenter = new LoginregisternextPresenter(this);
        SPUtils.getInstance(mActivity).put(Utils.FIRST, true);

        initView();



        mRecyclerView.setAdapter(new SplashAdapter(this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);



        checkLocation();

    }


    /**
     * 定位
     */
    private void checkLocation() {

        PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permissions) {
                SPUtils.getInstance(mActivity).put(Utils.PERMISSION, true);

                initLocationOption();

                UserInfoManger.saveIsGranted(true);
            }

            @Override
            public void permissionDenied(@NonNull String[] permissions) {
                SPUtils.getInstance(mActivity).put(Utils.PERMISSION, false);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    private void initLocationOption() {
        OkLogger.e("初始化 百度 sdk");
        locationClient = new LocationClient(mActivity);

        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("gcj02");
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationOption.setLocationNotify(true);
        locationOption.setIgnoreKillProcess(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationPoiList(true);
        locationOption.SetIgnoreCacheException(false);
        locationOption.setOpenGps(true);
        locationOption.setIsNeedAltitude(false);
        locationOption.setOpenAutoNotifyMode();
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        locationClient.setLocOption(locationOption);
        locationClient.start();
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
           double latitudeDouble = location.getLatitude();
            //获取经度信息
            String city = location.getCity();

            double longitude = location.getLongitude();
            getLocation = true;


            UserInfoManger.savecity(location.getCity());
            UserInfoManger.saveLongitude(location.getLongitude() + "");
            UserInfoManger.saveLatitude(location.getLatitude() + "");

        }
    }




    public void initView() {
        View dialog_code = LayoutInflater.from(this).inflate(R.layout.dialog_code, null);
        codedialog.setContentView(dialog_code);
        codedialog.setCanceledOnTouchOutside(false);
        Window codedialogWindow = codedialog.getWindow();
        WindowManager.LayoutParams params = codedialogWindow.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        codedialogWindow.setAttributes(params);
        et_code = dialog_code.findViewById(R.id.et_code);
        default_code_iv = dialog_code.findViewById(R.id.default_code_iv);
        btn_enter = dialog_code.findViewById(R.id.btn_enter);
        dialog_close_iv = dialog_code.findViewById(R.id.dialog_close_iv);
        dialog_close_iv.setOnClickListener(this);
        btn_enter.setOnClickListener(this);
        default_code_iv.setOnClickListener(this);

        et_code.addTextChangedListener(this);
//        btn_enter.setAlpha(0.5f);
        btn_enter.setEnabled(false);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loginregisternext;
    }

    @Override
    public LoginregisternextPresenter createPresenter() {
        return presenter;
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
                        .headers(Utils.TOKEN,SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                        .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                        .params(querymap)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                OkLogger.e(response.body());
                                CustomerServiceBean customerServiceBean = GsonUtils.fromJson(response.body(), CustomerServiceBean.class);
                                customerServiceBean.getData().getUrl();

                                Intent uintent = new Intent(mActivity,WebActivity.class);
                                uintent.putExtra("title", "在线客服");
                                uintent.putExtra("url",   customerServiceBean.getData().getUrl());
                                startActivity(uintent);
                            }
                        });

                break;
            case R.id.password_login_tv:

                if(getLocation){
                    startActivity(new Intent(App.getAppContext(), Login_accountpasswordActivity.class));
                }else{
                    checkLocation();
                }

                // finish();
                break;
            case R.id.clear_et_iv:
                et_input_phone.setText("");
                break;
            case R.id.next_btn:

                PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permissions) {

                        openGPSSEtting();
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permissions) {

                        checkLocation();

                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

                break;
            case R.id.default_code_iv:
                presenter.getCode(et_input_phone.getText().toString().trim());
                break;
            case R.id.btn_enter:
                codedialog.dismiss();
                if (Utils.netWork()) {
                    presenter.verification(et_input_phone.getText().toString().trim(), Utils.TYPES_LOGIN, et_code.getText().toString().trim(), "86");
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.dialog_close_iv:
                codedialog.dismiss();
                break;
//            case R.id.phone_head_tv:
//                toastShow("内测阶段，暂不提供海外用户注册");
//                Intent intent=new Intent(mActivity,SelectAreaActivity.class);
//                startActivityForResult(intent,100);
//                break;
        }
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()) {
            if (!et_input_phone.getText().toString().trim().startsWith("1")) {
                toastShow("请输入正确的手机号");
                return;
            }
            if (Utils.netWork()) {//获取验证码  需要网络
                et_code.setText("");
                codedialog.show();
                presenter.getCode(et_input_phone.getText().toString().trim());
            } else {
                toastShow(R.string.nonetwork);
            }
        } else {
            TipsBaseDialogFragment tipsBaseDialogFragment
                    = new TipsBaseDialogFragment(true, Gravity.CENTER, PERMISSION_TIPS,"取消","确定",false);
            tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                @Override
                public void Rsp(Boolean s) {
                    if (s) {
                        //跳转到手机原生设置页面
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 101);
                    }
                }
            });
            tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (data != null) {
                    if (data.getStringExtra("code") != null) {
//                        phone_head_tv.setText(data.getStringExtra("code"));
                    }
                }
                break;
            case 101:
//                openGPSSEtting();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //跳转到手机原生设置页面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 101);
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
            next_btn.setAlpha(1.0f);
            next_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
            next_btn.setEnabled(true);
//            next_btn.setTextColor(Color.parseColor("#333333"));
        } else {
            next_btn.setAlpha(0.4f);
            next_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
            next_btn.setEnabled(false);
//            next_btn.setTextColor(Color.parseColor("#66333333"));
        }
        if (et_input_phone.getText().toString().trim().length() > 0) {
            clear_et_iv.setVisibility(View.VISIBLE);
        } else {
            clear_et_iv.setVisibility(View.INVISIBLE);
        }
        if (et_code.getText().toString().trim().length() == 4) {
            btn_enter.setAlpha(1.0f);
            btn_enter.setEnabled(true);
            btn_enter.setTextColor(Color.parseColor("#ffffff"));

           // btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_black_20dp));
        } else {
            btn_enter.setAlpha(0.49f);
            btn_enter.setEnabled(false);
            btn_enter.setTextColor(Color.parseColor("#7Dffffff"));
           // btn_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_black_20dp));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void getCodeSuccess(ResponseBody responseBody) {
        progressDialog.dismiss();
        try {
            byte[] bytes = responseBody.bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            default_code_iv.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getCodefail(String s) {

    }

    @Override
    public void loginregister(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);

            if (jsonObject.getString("code").equals("200")) {
                Intent intent = new Intent(App.getAppContext(), InputcodeActivity.class);
                intent.putExtra(Utils.MOBILENO, et_input_phone.getText().toString().trim());
                intent.putExtra(Utils.AREACODE,  "86");
                startActivity(intent);
                codedialog.dismiss();
                //   finish();
            }else{
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
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }
}
