package com.jfkj.im.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.ChangePhonePresenter;
import com.jfkj.im.mvp.mine.ChangePhoneView;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.SelectAreaActivity;
import com.jfkj.im.ui.dialog.VerificationCodeDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.IDUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.StringChecker;
import com.jfkj.im.utils.TextCheckUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/20
 * </pre>
 */
public class ChangePhoneActivity extends BaseActivity<ChangePhonePresenter> implements ChangePhoneView {
    private static final int SELECT_AREA = 0x1234;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.iv_clear_input)
    ImageView mIvClearInput;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    @BindView(R.id.tv_area)
    TextView mTvArea;
    private String phone;
    private VerificationCodeDialog codeDialog;
    private String imgYzm;
    private String code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone;
    }

    @Override
    public ChangePhonePresenter createPresenter() {
        return new ChangePhonePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        registerListener();

    }

    private void registerListener() {
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( IDUtils.isMobile(s.toString())) {
                    mIvClearInput.setVisibility(View.VISIBLE);
                    mTvNext.setAlpha(1);
                } else {
                    mIvClearInput.setVisibility(View.GONE);
                    mTvNext.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_clear_input, R.id.tv_next, R.id.tv_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_input:
                mEditPhone.setText("");
                break;
            case R.id.tv_next:
                if (checkPhone()) {
                    mvpPresenter.checkPhone(phone);
                    //                    Bundle bundle = new Bundle();
                    //                    bundle.putString("phone",phone);
                    //                    JumpUtil.overlay(this,ChangePhoneYzmActivity.class,bundle);
                }
                break;
            case R.id.tv_area:
                ToastUtils.showShortToast("内测阶段，暂不提供海外用户注册");
//                Log.d("@@@","点啊点啊");
//                Bundle bundle = new Bundle();
//                JumpUtil.startForResult(this, SelectAreaActivity.class, SELECT_AREA, bundle);
                break;
            default:
                break;
        }
    }

    private boolean checkPhone() {
        phone = mEditPhone.getText().toString().trim();
        if (!TextCheckUtils.isPhoneNum(phone)) {
            ToastUtils.showShortToast(StringChecker.checkPhone(phone, this));
            return false;
        }
        return true;
    }

    @Override
    public void mobileVerification() {
        Map<String, String> map = new HashMap<>();
        map.put("mobileNo", phone);

        mvpPresenter.phoneCode(map);
    }

    /**
     * 获取图片验证码成功
     * @param responseBody
     */
    @Override
    public void showCodeSuccess(ResponseBody responseBody) {

        try {
            byte[] bytes = responseBody.bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (codeDialog == null) {
                codeDialog = new VerificationCodeDialog(this, R.style.dialogstyle, new VerificationCodeDialog.onEnterConfirm() {
                    @Override
                    public void getCode(String code) {
                        imgYzm = code;


                        mvpPresenter.getPhoneCode(phone, "3", imgYzm, "86", Utils.CHANNEL_ANDROID);
                    }

                    @Override
                    public void again() {
                        requestAgain();
                    }
                });
                codeDialog.setBitmap(bitmap);
                codeDialog.show();
            } else {
                codeDialog.setBitmap(bitmap);
                codeDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestAgain() {
        Map<String, String> map = new HashMap<>();
        map.put("mobileNo", phone);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        ApiClient.getmRetrofit().create(ApiStores.class)
                .getStaticCode(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new HttpErrorMsgObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        byte[] bytes = new byte[0];
                        try {
                            bytes = responseBody.bytes();
                            Bitmap newBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            codeDialog.setBitmap(newBitmap);
                            codeDialog.setImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getPhoneCodeSuccess() {
        if (codeDialog != null && codeDialog.isShowing()) {
            codeDialog.dismiss();
        }
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        JumpUtil.overlay(ChangePhoneActivity.this, ChangePhoneYzmActivity.class, bundle);
    }

    @Override
    public void changeSuccess() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_AREA) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        code = bundle.getString("code");
                        mTvArea.setText("+" + code);
                    }
                }
            }
        }
    }
}
