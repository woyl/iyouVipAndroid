package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.widget.ImageView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Circlefriend.CirclefriendPresenter;
import com.jfkj.im.mvp.Circlefriend.CirclefriendView;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class CirclefriendActivity extends BaseActivity<CirclefriendPresenter> implements CirclefriendView {
    CirclefriendPresenter circlefriendPresenter;
    @BindView(R.id.iv_title_back)
    AppCompatImageView iv_title_back;
    @BindView(R.id.iv_title_right_icon1)
    AppCompatImageView iv_title_right_icon1;
    @BindView(R.id.iv_title_right_icon2)
    AppCompatImageView iv_title_right_icon2;
    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, true);
        circlefriendPresenter = new CirclefriendPresenter(this);
        circlefriendPresenter.Circlefriend(index+"");



    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_circlefriend;
    }

    @Override
    public CirclefriendPresenter createPresenter() {
        return circlefriendPresenter;
    }

    @Override
    public void CirclefriendSuccess(ResponseBody responseBody) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
