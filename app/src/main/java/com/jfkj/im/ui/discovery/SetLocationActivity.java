package com.jfkj.im.ui.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.ToastUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  自定义位置信息
 * @author :   ys
 * @date :         2019/12/24
 * </pre>
 */
public class SetLocationActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.et_location)
    EditText mEtLocation;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    private String location;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_location;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        mEtLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mTvNext.setEnabled(true);
                    mTvNext.setAlpha(1.0f);
                } else {
                    mTvNext.setEnabled(false);
                    mTvNext.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (check()){
                    Intent intent = new Intent();
                    intent.putExtra("location",location);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }

    private boolean check() {
        location = mEtLocation.getText().toString().trim();
        if (TextUtils.isEmpty(location)){
            ToastUtils.showShortToast("请输入您所在的位置");
            return false;
        }
        return true;
    }
}
