package com.jfkj.im.adapter.fragment;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.jfkj.im.ui.fragment.MyPartyFragment;
import com.jfkj.im.ui.fragment.PartyRegisterFragment;


public class TabPartyAdapter extends FragmentStateAdapter {

    private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

    public static final int PAGE_HOME = 0;
    public static final int PAGE_CLASS = 1;

    public TabPartyAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentSparseArray.put(PAGE_HOME, PartyRegisterFragment.getInstance());
        fragmentSparseArray.put(PAGE_CLASS, MyPartyFragment.getInstance());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentSparseArray.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentSparseArray.size();
    }
}
