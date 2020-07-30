package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;

import butterknife.BindView;

public class RuleActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.tv_content)
    TextView tvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_left_tv.setBackgroundResource(R.drawable.back_black_iv);
        title_center_tv.setText("广场规则");
        title_center_tv.setTextColor(Color.parseColor("#000000"));
        title_left_tv.setOnClickListener(this);


        tvContent.setText("  I you的用户：\n" +
                "\n" +
                "        为了维护良好的社区氛围，在广场聊天时请注意以下聊天规范：\n" +
                "\n" +
                "        1.和平交流讨论，不得人身攻击。\n" +
                "        2.不得发表煽动国家、地区、名族、宗教间仇恨、歧视等相关言论。\n" +
                "        3.此群为I you用户群，禁止交流其他社交产品。\n" +
                "        4.禁止发布任何形式的广告及非I you广场官方的链接，任何形式带群号或者二维码的宣传；\n" +
                "        5.不散布传播淫秽、色情、暴力、赌博等内容。\n" +
                "        6.严禁诋毁、辱骂工作人员，或一任何形式冒充工作人员。\n" +
                "\n" +
                "        ");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rule;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_tv:
                finish();
                break;
        }
    }
}
