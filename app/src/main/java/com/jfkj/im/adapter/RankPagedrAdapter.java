package com.jfkj.im.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RankPagedrAdapter extends FragmentPagerAdapter {
    LinkedList<String> list;
    LinkedList<Fragment> fragments ;
    public RankPagedrAdapter(@NonNull FragmentManager fm, LinkedList<String> list,LinkedList<Fragment> fragments) {
        super(fm);
        this.list = list;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
    //
//    public RankPagedrAdapter(@NonNull FragmentActivity fragmentActivity, LinkedList<String> list ) {
//        super(fragmentActivity);
//        this.list = list;
//    }
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        Fragment fragment = fragments.get(position);
//        Bundle bundle=new Bundle();
//        bundle.putInt("position",position);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
}
