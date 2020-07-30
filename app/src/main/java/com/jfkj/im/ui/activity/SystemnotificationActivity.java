package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.w3c.dom.Text;

import butterknife.BindView;

public class SystemnotificationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    SwipeRecyclerView swipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_left_tv.setBackgroundResource(R.drawable.back_black_iv);
        title_left_tv.setOnClickListener(this);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_systemnotification;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {

    }
}
