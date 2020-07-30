package com.jfkj.im.ui;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.R;
import com.jfkj.im.adapter.RankPagedrAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.ui.fragment.Rankfragment;
import com.jfkj.im.utils.TablayoutViewpagerUtils;

import java.util.LinkedList;

import butterknife.BindView;

/*@tt*/
public class RankActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back_iv)
    AppCompatImageView back_iv;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
//    @BindView(R.id.slid_tab)
//    SlidingTabLayout slid_tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;

    RankPagedrAdapter rankPagedrAdapter;
    LinkedList<String> list;
    LinkedList<Fragment> fragments;

    private boolean isSelect = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back_iv.setOnClickListener(this);
        fragments=new LinkedList<>();
        list=new LinkedList<>();
        list.add("魅力榜");
        list.add("实力榜");

        ll_bg.post(new Runnable() {
            @Override
            public void run() {
                int w = ll_bg.getWidth();
                int h = ll_bg.getHeight();
                fragments.add(new Rankfragment(w,h));
                fragments.add(new Rankfragment(w,h));
                rankPagedrAdapter=new RankPagedrAdapter(getSupportFragmentManager(),list,fragments);
                viewpager.setAdapter(rankPagedrAdapter);
//                slid_tab.setViewPager(viewpager);
                tablayout.setupWithViewPager(viewpager);
                viewpager.setCurrentItem(getIntent().getIntExtra("postion",0));
            }
        });

        new TablayoutViewpagerUtils().setViewpagerWithTablayoutBg(mActivity,tablayout,viewpager,ll_bg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rank;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
