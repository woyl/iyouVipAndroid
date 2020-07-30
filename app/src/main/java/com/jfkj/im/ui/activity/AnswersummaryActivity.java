package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jfkj.im.R;
import com.jfkj.im.mvp.Answersummary.AnswersummaryPresenter;
import com.jfkj.im.mvp.Answersummary.AnswersummaryView;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class AnswersummaryActivity extends BaseActivity<AnswersummaryPresenter> implements AnswersummaryView, View.OnClickListener {
    AnswersummaryPresenter answersummaryPresenter;
    @BindView(R.id.close_iv)
    AppCompatImageView close_iv;
    @BindView(R.id.et_money)
    AppCompatEditText et_money;
    @BindView(R.id.btn_start_test)
    AppCompatButton btn_start_test;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        close_iv.setOnClickListener(this);
        btn_start_test.setOnClickListener(this);
        intent = getIntent();
        answersummaryPresenter = new AnswersummaryPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.btn_start_test:
                if (TextUtils.isEmpty(et_money.getText().toString().trim())) {
                    toastShow("请输入金额");
                    return;
                }
                if (Long.parseLong(et_money.getText().toString()) < 500) {
                    toastShow("金额必须大于500");
                    return;
                }
                if (Utils.netWork()) {
                    answersummaryPresenter.sendSquareGame(intent.getStringExtra(Utils.QUESTIONIDS), intent.getStringExtra(Utils.ANSWERIDS), et_money.getText().toString().trim());
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_answersummary;
    }

    @Override
    public AnswersummaryPresenter createPresenter() {
        return answersummaryPresenter;
    }

    @Override
    public void sendSquareGameSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if(jsonObject.getString("code").equals("200")){
                Intent intent = new Intent("sendSquareGameSuccess");
                intent.putExtra(Utils.CGAMEID, jsonObject.getJSONObject("data").getString("cgameid"));
                intent.putExtra(Utils.TIME, jsonObject.getJSONObject("data").getString("cadddate"));
                sendBroadcast(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


}
