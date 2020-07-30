package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.SpecialBaseActivity;
import com.jfkj.im.utils.Utils;

import butterknife.BindView;


public class WebActivity extends BaseActivity {
    @BindView(R.id.title_left_tv)
    TextView title_left;
    @BindView(R.id.title_center_tv)
    TextView title_center;
    @BindView(R.id.webView)
    WebView webView;
    Intent intent;
    String url;
    String title;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        title_left.setBackgroundResource(R.mipmap.iv_back_black);
        intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        title_center.setText(title);
        title_center.setTextColor(ContextCompat.getColor(mActivity,R.color.white));
        if(Utils.netWork()){
            webView.loadUrl(url);
            webView.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }else {
            tv.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullScreen(mActivity);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }
}
