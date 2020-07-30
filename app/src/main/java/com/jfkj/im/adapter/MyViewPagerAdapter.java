package com.jfkj.im.adapter;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/5/27
 * </pre>
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = null;
    private List<String> titleList;



    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> list) {
        super(fm);
        this.fragmentList = fragments;
        this.titleList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
