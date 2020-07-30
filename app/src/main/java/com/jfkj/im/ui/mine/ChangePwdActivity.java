package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.PhoneAndPwdPresenter;
import com.jfkj.im.mvp.mine.PhoneAndPwdView;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/13
 * </pre>
 */
public class ChangePwdActivity extends BaseActivity<PhoneAndPwdPresenter> implements PhoneAndPwdView {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_change_hint)
    TextView mTvChangeHint;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.iv_clear_input)
    ImageView mIvClearInput;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    private String oldPwd;
    private boolean verify = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public PhoneAndPwdPresenter createPresenter() {
        return new PhoneAndPwdPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,true);
        mTvName.setText("输入原密码");
        mTvNext.setEnabled(false);
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    mTvNext.setAlpha(1);
                    mTvNext.setEnabled(true);
                }else {
                    mTvNext.setAlpha(0.5f);
                    mTvNext.setEnabled(false);
                }

                if (s.length() > 0){
                    mIvClearInput.setVisibility(View.VISIBLE);
                }else {
                    mIvClearInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxView.clicks(mTvNext).throttleFirst(1, TimeUnit.SECONDS)
                .safeSubscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (check()) {
                            mvpPresenter.updatePasswordVerification(oldPwd);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.iv_back,R.id.iv_clear_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //            case R.id.tv_next:
            //                if (check()){
            //                    mvpPresenter.updatePasswordVerification(oldPwd);
            //                }
            //                break;
            case R.id.iv_clear_input:
                mEditPhone.setText("");
            default:
                break;
        }
    }

    private boolean check() {
        oldPwd = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd) || oldPwd.length() < 6 || oldPwd.length() > 20) {
            ToastUtils.showShortToast("旧密码格式不正确");
            return false;
        }

        return true;
    }

    @Override
    public void verifySuccess() {
        Bundle bundle = new Bundle();
        bundle.putString("oldPwd", oldPwd);
        JumpUtil.overlay(ChangePwdActivity.this, ChangeNewPwdActivity.class, bundle);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
