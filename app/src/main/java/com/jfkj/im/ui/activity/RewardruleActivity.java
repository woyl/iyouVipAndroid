package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.SpecialBaseActivity;

import butterknife.BindView;

public class RewardruleActivity extends BaseActivity {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fullScreen(mActivity);
//        }
        title_left_tv.setBackgroundResource(R.mipmap.iv_back_black);
        title_center_tv.setText("奖励规则");
        title_center_tv.setTextSize(17);
        title_left_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rewardrule;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }
}
