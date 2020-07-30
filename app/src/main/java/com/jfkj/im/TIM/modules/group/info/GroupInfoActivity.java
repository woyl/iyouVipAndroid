package com.jfkj.im.TIM.modules.group.info;


import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;

import butterknife.BindView;


public class GroupInfoActivity extends BaseActivity {
    @BindView(R.id.group_manager_base)
    LinearLayout group_manager_base;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(group_manager_base);
        GroupInfoFragment fragment = new GroupInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.group_manager_base, fragment).commitAllowingStateLoss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.group_info_activity;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void finish() {
        super.finish();
        setResult(1001);
    }
}
