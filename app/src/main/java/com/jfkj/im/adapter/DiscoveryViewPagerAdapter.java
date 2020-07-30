package com.jfkj.im.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class DiscoveryViewPagerAdapter extends PagerAdapter {


    public DiscoveryViewPagerAdapter() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
