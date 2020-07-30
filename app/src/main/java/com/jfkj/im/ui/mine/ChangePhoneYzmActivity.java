package com.jfkj.im.ui.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.ChatLayoutHelper;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.ChangePhonePresenter;
import com.jfkj.im.mvp.mine.ChangePhoneView;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.VerificationCodeDialog;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * <pre>
 * Description: 手机验证码
 * @author :   ys
 * @date :         2019/11/20
 * </pre>
 */
public class ChangePhoneYzmActivity extends BaseActivity<ChangePhonePresenter> implements ChangePhoneView, CountDownTimeListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    AppCompatTextView mTvPhone;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.tv_send_yzm)
    TextView mTvSendYzm;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.layout_yzm)
    ConstraintLayout mLayoutYzm;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    @BindView(R.id.iv_clear_input)
    ImageView mIvClear;
    private String phone;
    private TimeCountUtil mTimeCountUtil;
    private String code;
    private VerificationCodeDialog codeDialog;
    private String imgYzm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_yzm;
    }

    @Override
    public ChangePhonePresenter createPresenter() {
        return new ChangePhonePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            mTvPhone.setText("验证码已发送至+" + phone);
        }
        mTvSendYzm.setEnabled(false);
        mEditCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mIvClear.setVisibility(View.VISIBLE);
                } else {
                    mIvClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTimeCountUtil = new TimeCountUtil(60000, 1000, this);
        mTimeCountUtil.start();
    }

    @OnClick({R.id.tv_send_yzm, R.id.tv_next, R.id.iv_back, R.id.iv_clear_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_yzm:
                //重新发送验证码
                Map<String, String> map = new HashMap<>();
                map.put("mobileNo", phone);
                mvpPresenter.phoneCode(map);
                break;
            case R.id.tv_next:
                if (check()) {
                    mvpPresenter.updateMobile(phone, code);
                }
                break;
            case R.id.iv_clear_input:
                mEditCode.setText("");
                break;
        }
    }

    private boolean check() {
        code = mEditCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShortToast("亲输入验证码");
            return false;
        }
        return true;
    }

    @Override
    public void onTimeFinish() {
        mTimeCountUtil.cancel();
        mTvSendYzm.setEnabled(true);
        mTvSendYzm.setText("重新发送");
        mTvSendYzm.setTextColor(ContextCompat.getColor(mActivity,R.color.cFF2B66));
    }

    @Override
    public void onTimeTick(long millisUntilFinished) {
        mTvSendYzm.setEnabled(false);
        mTvSendYzm.setText((millisUntilFinished / 1000) + "S");
    }

    @Override
    public void mobileVerification() {

    }

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
    }

    @Override
    public void changeSuccess() {
        JumpUtil.overlay(this, SettingActivity.class);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCountUtil != null) {
            mTimeCountUtil.cancel();
            mTimeCountUtil = null;
        }
    }
}
