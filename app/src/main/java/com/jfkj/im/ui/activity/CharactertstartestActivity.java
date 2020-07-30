package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.Utils;

import butterknife.BindView;

public class CharactertstartestActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_start_test)
    AppCompatButton btn_start_test;
    @BindView(R.id.close_iv)
    AppCompatImageView close_iv;
    @BindView(R.id.tv_start_test)
    AppCompatTextView tv_start_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_start_test.setOnClickListener(this);
        close_iv.setOnClickListener(this);
        tv_start_test.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_charactertest;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_test:
                startActivity(new Intent(mActivity, CharacterttestActivity.class));
                finish();
                break;
            case R.id.close_iv:
                finish();
                break;
            case R.id.tv_start_test:
                if(Utils.netWork()){
                    startActivity(new Intent(mActivity,MineCharacterttestActivity.class));
                }else {
                    toastShow(R.string.nonetwork);
                }
                break;
        }
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }
}
