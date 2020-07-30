package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.ChangePwdPresenter;
import com.jfkj.im.mvp.mine.ChangePwdView;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.TextCheckUtils;
import com.jfkj.im.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  设置新密码页
 * @author :   ys
 * @date :         2019/12/13
 * </pre>
 */
public class ChangeNewPwdActivity extends BaseActivity<ChangePwdPresenter> implements ChangePwdView {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_change_hint)
    TextView mTvChangeHint;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;

    @BindView(R.id.tv_next)
    TextView mTvNext;



    @BindView(R.id.login_hidden_iv)
    ImageView getmIvBack;


    boolean isSee = false;
    boolean isSeeis = false;

    private String newPwd;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public ChangePwdPresenter createPresenter() {
        return new ChangePwdPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        mTvName.setText("设置新的登陆密码");
        mEditPhone.setHint("输入密码");
        mTvNext.setText("确认");
        Bundle bundle = getIntent().getExtras();

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
                    mTvNext.setEnabled(false);
                    mTvNext.setAlpha(0.5f);
                }

                if (s.length() > 0){
                    isSee= true;
                  //  mIvClearInput.setVisibility(View.VISIBLE);
                  // getmIvBack.setImageResource(R.drawable.login__nor_iv);
                }else {
                    isSee= false;
                 //   mIvClearInput.setVisibility(View.GONE);
                  //  getmIvBack.setImageResource(R.drawable.login_hidden_iv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.login_hidden_iv, R.id.iv_back, R.id.tv_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
             case R.id.iv_back:

                finish();
                break;
            case R.id.tv_next:
                if (check()){
                    mvpPresenter.changePwd(newPwd);
                }
                break;
//            case R.id.iv_clear_input:
//                mEditPhone.setText("");
//                mTvNext.setEnabled(false);
//                break;

            case R.id.login_hidden_iv:
                 if(isSee){
                     if(isSeeis){
                         getmIvBack.setImageResource(R.mipmap.login_icon_eye_nor);
                         isSeeis = false;
                         mEditPhone.setTransformationMethod(PasswordTransformationMethod.getInstance());
                     }else{
                         getmIvBack.setImageResource(R.mipmap.login_hidden_iv);
                         isSeeis = true;
                         mEditPhone.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                     }
                 }
                break;
        }
    }

    private boolean check() {
        newPwd = mEditPhone.getText().toString();
        if (!TextCheckUtils.isPassword(newPwd)){
            ToastUtils.showShortToast("请输入6-20位密码");
            return false;
        }
        return true;
    }

    @Override
    public void changeSuccess() {

        JumpUtil.overlay(this, MainActivity.class);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
