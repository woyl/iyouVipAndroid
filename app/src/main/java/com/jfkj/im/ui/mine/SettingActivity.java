package com.jfkj.im.ui.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.Setup;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.SettingPresent;
import com.jfkj.im.mvp.mine.SettingView;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.Login_accountpasswordActivity;
import com.jfkj.im.ui.activity.Loginregister_phone_Activity;
import com.jfkj.im.ui.activity.WebActivity;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.OneButtonDialog;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.CacheManger;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.WinxinUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * <pre>
 * Description: 设置
 * @author :   ys
 * @date :         2019/11/20
 * </pre>
 */
public class SettingActivity extends BaseActivity<SettingPresent> implements SettingView {
    @BindView(R.id.tv_login_password)
    TextView tv_login_password;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.iv_online)
    AppCompatImageView mIvOnline;
    @BindView(R.id.layout_online_status)
    LinearLayout mLayoutOnlineStatus;
    @BindView(R.id.line1)
    View mLine1;
    @BindView(R.id.iv_address)
    AppCompatImageView mIvAddress;
    @BindView(R.id.layout_address)
    LinearLayout mLayoutAddress;
//    @BindView(R.id.space)
//    Space mSpace;
    @BindView(R.id.tv_change_phone)
    AppCompatTextView mTvChangePhone;
    @BindView(R.id.line2)
    View mLine2;
    @BindView(R.id.tv_change_pwd)
    AppCompatTextView mTvChangePwd;
    @BindView(R.id.line3)
    View mLine3;
    @BindView(R.id.tv_contact_service)
    AppCompatTextView mTvContactService;
    @BindView(R.id.line4)
    View mLine4;
    @BindView(R.id.tv_clear_cache)
    AppCompatTextView mTvClearCache;
    @BindView(R.id.tv_cache_size)
    AppCompatTextView mTvCacheSize;
    @BindView(R.id.line5)
    View mLine5;
    @BindView(R.id.tv_version)
    AppCompatTextView mTvVersion;
    @BindView(R.id.tv_version_code)
    AppCompatTextView mTvVersionCode;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;
    private int vipLevel;
    private String hideLocation = "-1";
    private String hideOnline = "-1";
    private OneButtonDialog oneButtonDialog;
    private CommonDialog clearDialog = null;
    private CommonDialog exitDialog = null;
    Map<String, String> querymap = new HashMap();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public SettingPresent createPresenter() {
        return new SettingPresent(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, Utils.ANDROID);
        querymap.put(Utils.REQTIME, AppUtils.getReqTime());


        mTvTitle.setText("设置");
        mTvVersionCode.setText(String.valueOf(AppUtils.getVersionName(this)));
        try {
            mTvCacheSize.setText(CacheManger.getCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> headMap = new HashMap<>();
        headMap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        headMap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));

        OkGo.<String>post(ApiStores.base_url + "/my/SetUp")
                .tag(mActivity)
                .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {

                    private String mobile;

                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String string = response.body();
                            OkLogger.e(string);
                            JSONObject jsonObject = new JSONObject(string);
                            if (jsonObject.getString("code").equals("200")) {

                                Setup setup = JSON.parseObject(string, Setup.class);

                                if (setup.getData().getHideOnline() == 0) {
                                    mIvOnline.setBackgroundResource(R.mipmap.switch_off_white);
                                } else {
                                    mIvOnline.setBackgroundResource(R.mipmap.switch_on_green);
                                }

                                if (setup.getData().getHideLocation() == 0) {
                                    mIvAddress.setBackgroundResource(R.mipmap.switch_off_white);
                                } else {
                                    mIvAddress.setBackgroundResource(R.mipmap.switch_on_green);
                                }

                                if (setup.getData().getPassword().equals("1")) {
                                    tv_login_password.setText("");
                                    //  mTvChangePwd.setEnabled(false);
                                } else {
                                    // mTvChangePwd.setEnabled(true);
                                }

                                vipLevel = setup.getData().getVipLevel();
                                hideLocation = setup.getData().getHideLocation() + "";
                                hideOnline = setup.getData().getHideOnline() + "";
                                mobile = setup.getData().getMobile();
                                if (!"".equals(mobile) && mobile.length() >= 10) {
                                    tv_phone.setText(mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length()));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back, R.id.iv_online, R.id.iv_address,
            R.id.tv_change_phone, R.id.tv_change_pwd, R.id.tv_contact_service,
            R.id.tv_clear_cache, R.id.tv_version, R.id.tv_logout, R.id.image_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_online:
                if (Integer.parseInt(UserInfoManger.getUserVipLevel()) < 20) {
                    showTipsDialog("VIP等级达到20级以后，才能解锁隐藏在线状态功能");
                    return;
                }
                if (Integer.parseInt(hideOnline) == 1) {
                    hideOnline = "0";
                } else {
                    hideOnline = "1";
                }
                Map<String, String> map = new HashMap();
                map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                map.put(Utils.OSNAME, Utils.ANDROID);
                map.put(Utils.CHANNEL, Utils.ANDROID);
                map.put(Utils.DEVICENAME, Utils.getdeviceName());
                map.put(Utils.DEVICEID, Utils.ANDROID);
                map.put(Utils.REQTIME, AppUtils.getReqTime());
                mTvTitle.setText("设置");
                map.put("hideOnline", hideOnline);

                OkGo.<String>post(ApiStores.base_url + "/my/SetUpVIP")
                        .tag(mActivity)
                        .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                        .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
//                        .params("hideOnline",hideOnline)
                        .params(map)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    OkLogger.e(response.body());

                                    if (hideOnline.equals("0")) {
                                        mIvOnline.setBackgroundResource(R.mipmap.switch_off_white);
                                    } else {
                                        mIvOnline.setBackgroundResource(R.mipmap.switch_on_green);
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                break;
            case R.id.iv_address:
                if (Integer.parseInt(UserInfoManger.getUserVipLevel()) < 50) {
                    showTipsDialog("VIP等级达到50级以后，才能解锁隐藏地理位置功能");
                    return;
                }
                if (Integer.parseInt(hideLocation) == 1) {
                    hideLocation = "0";
                } else {
                    hideLocation = "1";
                }
                Map<String, String> hideLocationmap = new HashMap();

                hideLocationmap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                hideLocationmap.put(Utils.OSNAME, Utils.ANDROID);
                hideLocationmap.put(Utils.CHANNEL, Utils.ANDROID);
                hideLocationmap.put(Utils.DEVICENAME, Utils.getdeviceName());
                hideLocationmap.put(Utils.DEVICEID, Utils.ANDROID);
                hideLocationmap.put(Utils.REQTIME, AppUtils.getReqTime());

                hideLocationmap.put("hideLocation", hideLocation);

                OkGo.<String>post(ApiStores.base_url + "/my/SetUpVIP")
                        .tag(mActivity)
                        .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                        .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                        .params(hideLocationmap)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    OkLogger.e(response.body());
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    if (jsonObject.getString("code").equals("200")) {


                                        if (hideLocation.equals("0")) {
                                            mIvAddress.setBackgroundResource(R.mipmap.switch_off_white);
                                        } else {
                                            mIvAddress.setBackgroundResource(R.mipmap.switch_on_green);
                                        }


                                    } else {
                                        ToastUtils.showShortToast(jsonObject.getString("message"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                            }
                        });

                break;
            case R.id.tv_change_phone:
                //修改手机号
                JumpUtil.overlay(this, ChangePhoneActivity.class);
                break;
            case R.id.tv_change_pwd:
                JumpUtil.overlay(this, ChangeNewPwdActivity.class);
                break;
            case R.id.tv_contact_service:
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
            case R.id.tv_clear_cache:
                showClearDialog();
                break;
            case R.id.tv_version:

                break;
            case R.id.tv_logout:
                showExitDialog();
                break;
            case R.id.image_wx:
                VipSetGradeDialogFragment vipSetGradeDialogFragment
                        = new VipSetGradeDialogFragment(true, Gravity.CENTER, "关注公众号：【I youAPP】，获取优惠充值奖励", "复制微信公众号");
                vipSetGradeDialogFragment.setRsp(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            //获取剪贴板管理器：
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("Label", "I youAPP");
// 将ClipData内容放到系统剪贴板里。
                            if (cm != null) {
                                cm.setPrimaryClip(mClipData);
                            }
                            WinxinUtils.toWeChat(mActivity);
                        }
                    }
                });
                vipSetGradeDialogFragment.show(getSupportFragmentManager(), "");
                break;
            default:
                break;
        }
    }

    private void showExitDialog() {
        exitDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        exitDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        mvpPresenter.loginOut();
                        break;
                }
            }
        });
        exitDialog.setTitleText("退出登录").setContentText("是否退出登录").show();
    }

    private void showClearDialog() {
        clearDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        clearDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                Log.d("@@@", "这儿要清楚缓存");
                                CacheManger.clearCacheSize(SettingActivity.this);
                                emitter.onComplete();
                            }

                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Object>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Object o) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.showShortToast("清除缓存失败，请重试");
                                    }

                                    @Override
                                    public void onComplete() {
                                        mTvCacheSize.setText("0M");
                                        clearDialog.dismiss();
                                        ToastUtils.showShortToast("缓存清除成功！");
                                    }
                                });

                        break;
                }
            }
        });
        clearDialog.setTitleText("提示")
                .setContentText("确认清除缓存吗？")
                .setCancelBtnText("取消")
                .setConfirmBtnText("确认")
                .setCanceledOnTouchOutside(true);
        clearDialog.show();
    }

    /**
     * 退出登录
     */
    private void logout() {
        SPUtils.getInstance(this).clear();
        Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
        kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(kick_out_intent);

        ApiClient.onDestroy();
        finish();
    }

    /**
     * 检查VIP等级
     */
    private void checkLevel(String type) {
        if ("onLine".equals(type)) {
            if (vipLevel < 20) {
                showTipsDialog("VIP等级达到20级以后，才能解锁隐藏在线状态功能");
            } else {
                mvpPresenter.setUpVip(hideOnline, "");
            }
        } else if ("location".equals(type)) {
            if (vipLevel < 50) {
                showTipsDialog("VIP等级达到50级以后，才能解锁隐藏地理位置功能");
            } else {
                mvpPresenter.setUpVip("", hideLocation);
            }
        }
    }

    private void showTipsDialog(String s) {
        oneButtonDialog = new OneButtonDialog(this, R.style.dialogstyle, new OneButtonDialog.CustomDialogListener() {
            @Override
            public void onClick(View view) {
                oneButtonDialog.dismiss();
            }
        });
        oneButtonDialog.setCanceledOnTouchOutside(true);
        oneButtonDialog.setContent(s).show();
    }

    @Override
    public void showVersion() {

    }


    @Override
    public void loginOutSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            //  toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                exitDialog.dismiss();
                logout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
