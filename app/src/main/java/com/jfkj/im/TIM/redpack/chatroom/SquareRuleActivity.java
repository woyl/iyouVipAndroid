package com.jfkj.im.TIM.redpack.chatroom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class SquareRuleActivity extends BaseActivity {
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        tv_title.setText("广场规则");
        tv_title.setTextColor(getColor(R.color.white));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_square_rule;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.iv_title_back)
    void onclick(){
        finish();
    }
}
