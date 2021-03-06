package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.EditUserInfoPresenter;
import com.jfkj.im.mvp.mine.EditUserInfoView;
import com.jfkj.im.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  设置星座
 * @author :   ys
 * @date :         2020/1/6
 * </pre>
 */
public class SetStarActivity extends BaseActivity<EditUserInfoPresenter> implements EditUserInfoView {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.et_star)
    EditText mEtStar;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    private String star;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_star;
    }

    @Override
    public EditUserInfoPresenter createPresenter() {
        return new EditUserInfoPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,true);
        mEtStar.addTextChangedListener(new TextWatcher() {
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

    @Override
    public void showEditSuccess(BaseResponse baseResponse, String type) {
        setResult(RESULT_OK);
//        UserInfoManger.saveStar(star);
        finish();
        ToastUtils.showShortToast(baseResponse.getMessage());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R.id.iv_back, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (check()){
                    star = mEtStar.getText().toString().trim();
                    Map<String,String> map = new HashMap<>();
                    map.put("star",star);
                    mvpPresenter.editInfo(map,"USER_STAR");
                }
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(mEtStar.getText().toString().trim())){
            ToastUtils.showShortToast("请输入星座名称");
            return false;
        }
        return true;
    }
}
