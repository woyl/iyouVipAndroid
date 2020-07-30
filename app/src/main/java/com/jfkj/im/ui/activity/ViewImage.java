package com.jfkj.im.ui.activity;



import android.os.Bundle;
import android.util.DisplayMetrics;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
/**
 *  图片查看器
 */
public class ViewImage extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_image;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }


}
