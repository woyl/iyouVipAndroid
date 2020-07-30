package com.jfkj.im.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.GuideEvent;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.LoginOutDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.tools.JumpUtils;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.cache.Sp;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 * Description:  引导页
 * @author :   ys
 * @date :         2019/12/10
 * </pre>
 */
public class GuideActivity extends BaseActivity implements CountDownTimeListener, EasyPermissions.PermissionCallbacks {
    //
    private TimeCountUtil mCountUtil = null;
    private static final int PERMISSION_READ_WRITE = 124;
    private static final String[] permissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String PERMISSION_TIPS = "为保证软件正常使用请开启电话和读写权限";
    private Context context;
    private boolean isFrist;
    private UserInfoBean userInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Log.d("@@@", "走了重新启动");
        setAndroidNativeLightStatusBar(mActivity, false);
        EventBus.getDefault().post(new GuideEvent(true));
        Log.e("msg",".......GuideActivity......onCreate().........");
    }

//    @Subscribe
//    public void startActivity(String event){
//
//    }
//



    @Override
    public void onBackPressed() {

    }

    /**
     * 获取渠道名
     * @return 如果没有获取成功，那么返回值为空
     */
    public  String getChannelName() {

        String channelName = null;
        try {
            PackageManager packageManager = this.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo( this.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 读写权限的申请
     */
    @AfterPermissionGranted(PERMISSION_READ_WRITE)
    private void requestPermission() {
        if (hasPermission()) {


            if (!TextUtils.isEmpty(SPUtils.getInstance(mActivity).getString(Utils.TOKEN))) {
                getUserInfo();
            } else {
                mCountUtil.start();
            }
        } else {
            EasyPermissions.requestPermissions(this, PERMISSION_TIPS, PERMISSION_READ_WRITE, permissions);
        }
    }

    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限请求")
                    .setRationale(PERMISSION_TIPS)
                    .build()
                    .show();
        }
    }


    private void showExitDialog() {
        LoginOutDialog exitDialog = new LoginOutDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_submit:
                        SPUtils.getInstance(context).put(Utils.TOKEN, "");
                        Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                        kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(kick_out_intent);
                        if (Utils.channel != null) {
                            Utils.channel.close();
                        }
                        ApiClient.onDestroy();
                        finish();
                        break;
                }
            }
        });
        exitDialog.setContentText("您的登录状态已过期，请重新登录").show();
    }

    private void getUserInfo() {
        UserInfoManger.savaUserInfo("");

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
                            JumpUtil.overlay(context, Loginregister_phone_Activity.class);
                            finish();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    UserInfoManger.savaUserInfo(jsonObject.getString("data"));
                                    userInfoBean = JSON.parseObject(jsonObject.getString("data"), UserInfoBean.class);

                                    SPUtils.getInstance(context).put(Utils.USERID,userInfoBean.getUserId());
                                }else if (jsonObject.getString("code").equals("11001")){
                                    //用户登录已经过期
                                    showExitDialog();

                                }
                                mCountUtil.start();
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

    /**
     * 比如。gender 为0 则说明先要填写性别，昵称
     * gender 为1 男性，vipCard =0且 optState=0则到男性用户选择好友页面。
     * gender 为1 男性，vipCard =0且 optState=1 到购买会员页面
     * gender 为1 男性，vipCard =1 银卡且 examine=0 提交审核
     * gender 为1 男性，vipCard =1 银卡且 examine=0 提交审核
     * gender 为1 男性，vipCard =1 银卡且 examine=1 首页
     * 审核状态examine  0未提交审核 1 审核通过 2审核中 3审核未通过
     * 1银卡，2金卡，3黑卡  0没有购买过
     * gender 为2 女性
     */
    public void jumpToActivity(UserInfoBean data) {



        if(data.getExamine() == 1){
            //审核通过
            Intent intent = new Intent(App.getAppContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ApiClient.onDestroy();
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
    public void onTimeFinish() {
        if (userInfoBean != null) {
            jumpToActivity(userInfoBean);
        } else {
            JumpUtil.overlay(context, Loginregister_phone_Activity.class);
            finish();
        }

    }

    @Override
    public void onTimeTick(long millisUntilFinished) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountUtil != null) {
            mCountUtil.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("@@@", "guideActivity" + "onReusme");

//        if ("initSuccess".equals(event)){
            Log.e("msg",".......GuideActivity......startActivity().........");
            if (mCountUtil == null) {
                mCountUtil = new TimeCountUtil(1000, 1000, this);
            }
            requestPermission();

            //申请读写权限

            String channelName = getChannelName();


            SPUtils.getInstance(this).put(Utils.DEVICENAMETYOE,channelName);
//        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("@@@", "guideActivity" + "onStart");
    }
}
