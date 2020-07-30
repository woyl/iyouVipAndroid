package com.jfkj.im.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.R;
import com.jfkj.im.tab.TabLayoutMediator;

public class TablayoutViewpagerUtils {

    public void setViewpagerWithTablayout(Context context, @NonNull TabLayout tabLayout, @NonNull ViewPager2 viewPager,String content1,String content2) {
        new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(content1);
                    setTextSize(true,tab,context);
                } else {
                    tab.setText(content2);
                    setTextSize(false,tab,context);
                }
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTextSize(true,tab,context);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTextSize(false,tab,context);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void setViewpagerWithTablayout(Context context, @NonNull TabLayout tabLayout, @NonNull ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTextSize(true,tabLayout.getTabAt(position),context);
                } else {
                    setTextSize(false,tabLayout.getTabAt(position),context);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTextSize(true,tab,context);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTextSize(false,tab,context);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void setViewpagerWithTablayoutBg(Context context, @NonNull TabLayout tabLayout, @NonNull ViewPager viewPager, LinearLayout ll_bg) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTextSize(true,tabLayout.getTabAt(position),context);
                    ll_bg.setBackground(ContextCompat.getDrawable(context,R.mipmap.no_bg));
                } else {
                    setTextSize(false,tabLayout.getTabAt(position),context);
                    ll_bg.setBackground(ContextCompat.getDrawable(context,R.mipmap.meili_bg));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTextSize(true,tab,context);
                if (tab.getPosition() == 0) {
                    ll_bg.setBackground(ContextCompat.getDrawable(context,R.mipmap.no_bg));
                } else {
                    ll_bg.setBackground(ContextCompat.getDrawable(context,R.mipmap.meili_bg));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTextSize(false,tab,context);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setTextSize(boolean is,TabLayout.Tab tab,Context mActivity){
        if (is){
            View view = tab.getCustomView();
            if (view == null){
                tab.setCustomView(R.layout.tab_layout);
            }
            TextView textView = tab.getCustomView().findViewById(R.id.tv_content);
            textView.setText(tab.getText());
            textView.setTextSize(16);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        } else {
            View view = tab.getCustomView();
            if (view == null){
                tab.setCustomView(R.layout.tab_layout);
            }
            TextView textView = tab.getCustomView().findViewById(R.id.tv_content);
            textView.setText(tab.getText());
            textView.setTextSize(16);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.line_btn));
        }
    }
}
