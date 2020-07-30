package com.jfkj.im.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jfkj.im.R;
import com.jfkj.im.utils.DisplayUtil;

import java.util.Objects;

public abstract class BaseCustomHeightWithDialogFragment extends DialogFragment {

    private boolean isWidth ;
    private int ori ;
    protected View view;
    protected LayoutInflater inflater;
    private int height,with;


    public BaseCustomHeightWithDialogFragment(int ori,int height,int with) {
        this.ori = ori;
        this.height = height;
        this.with = with;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.inflater = inflater;
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        window.setGravity(ori);
        ViewGroup.LayoutParams layoutParams= window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
//            int width = dm.widthPixels;
        layoutParams.width = DisplayUtil.dip2px(Objects.requireNonNull(getContext()),with);
        layoutParams.height = DisplayUtil.dip2px(getContext(),height);
        window.setWindowAnimations(R.style.popmenu_animation);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogStyle);
        super.onResume();
    }

    @Nullable
    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getDialog().getWindow().setLayout(displayMetrics.widthPixels,getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    protected abstract void initViews();
}
