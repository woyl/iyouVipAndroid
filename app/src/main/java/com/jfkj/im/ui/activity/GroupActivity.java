package com.jfkj.im.ui.activity;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.adapter.GroupAdapter;
import com.jfkj.im.adapter.Groupviewpager;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.SpecialBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupActivity extends SpecialBaseActivity implements View.OnClickListener {
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Groupviewpager groupviewpager;
    @BindView(R.id.search_iv)
    ImageView search_iv;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;
    @BindView(R.id.img_head)
    ImageView img_head;

    GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStaturBar(img_head);
        List<String> list = new ArrayList<>();
        list.add("最富有");
        list.add("最新");
        groupviewpager = new Groupviewpager(getSupportFragmentManager(), 0, list);
        viewpager.setAdapter(groupviewpager);
        tabLayout.setupWithViewPager(viewpager);
        search_iv.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullScreen(mActivity);
        }
        setAndroidNativeLightStatusBar(this, false);
//        setStaturBar(rl_head);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(tabLayout.getTabTextColors());
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(tabLayout.getTabTextColors());
                textView.setTypeface(Typeface.DEFAULT);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_club;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv:
                Intent Intent_search = new Intent(App.getAppContext(), SearchActivity.class);
                startActivity(Intent_search);
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    public void setStaturBar(View view) {
//        ConstraintLayout.LayoutParams layoutParams
//                = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0, ScreenUtil.getStatusBarHeight(),0,0);
//        view.setLayoutParams(layoutParams);
        view.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);

    }
}
