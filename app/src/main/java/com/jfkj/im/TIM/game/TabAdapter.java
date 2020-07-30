package com.jfkj.im.TIM.game;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jfkj.im.TIM.redpack.chatroom.AnswerSelfBean;
import com.jfkj.im.TIM.redpack.chatroom.MyCharacterTextBeanLeft;
import com.jfkj.im.TIM.redpack.chatroom.MyCharacterTextFragment;
import com.jfkj.im.TIM.redpack.chatroom.MyCharavterUserTextFragment;

import java.util.List;

public class TabAdapter extends FragmentStateAdapter {
    private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

    public static final int PAGE_HOME = 0;
    public static final int PAGE_CLASS = 1;

    public TabAdapter(@NonNull FragmentActivity fragmentActivity, String gameId,List<AnswerSelfBean> answerSelfBeans) {
        super(fragmentActivity);
        fragmentSparseArray.put(PAGE_HOME, MyCharacterTextFragment.getInstance(gameId,answerSelfBeans));
        fragmentSparseArray.put(PAGE_CLASS, MyCharavterUserTextFragment.getInstance(gameId,answerSelfBeans));
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
