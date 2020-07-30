package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;

public class DetailspacketsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailspackets;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }
}
