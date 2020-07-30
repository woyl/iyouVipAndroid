package com.jfkj.im.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jfkj.im.ui.fragment.ClubFragment;
import com.jfkj.im.ui.fragment.Groupfragment;

import java.util.List;

public class Groupviewpager extends FragmentStatePagerAdapter {
    List<String> list;

    public Groupviewpager(@NonNull FragmentManager fm, int behavior, List<String> list) {
        super(fm, behavior);
        this.list=list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("position",position+1+"");
        return Groupfragment.getInstance(bundle);
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
}
