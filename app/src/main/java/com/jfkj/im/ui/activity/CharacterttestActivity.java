package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.jfkj.im.Bean.TestQuestionBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.CharacterttestAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.replacequestion.ReplaceQuestionPresenter;
import com.jfkj.im.mvp.replacequestion.ReplaceQuestionView;
import com.jfkj.im.ui.dialog.Dialoganswerrecord;
import com.jfkj.im.ui.dialog.VipDialog;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class CharacterttestActivity extends BaseActivity<ReplaceQuestionPresenter> implements View.OnClickListener, ReplaceQuestionView, OnItemClickListener {
    @BindView(R.id.close_iv)
    AppCompatImageView close_iv;
    @BindView(R.id.tv_position1)
    AppCompatTextView tv_position1;
    @BindView(R.id.tv_question)
    AppCompatTextView tv_question;
    @BindView(R.id.tv_start_test)
    AppCompatTextView tv_start_test;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    @BindView(R.id.btn_enter_test)
    AppCompatButton btn_enter_test;
    ReplaceQuestionPresenter replaceQuestionPresenter;
    List<TestQuestionBean.DataBean.ArrayBean> arrayBeans;
    CharacterttestAdapter characterttestAdapter;
    VipDialog vipDialog;
    Dialoganswerrecord dialoganswerrecord;
    List<String> questionIdlist;
    StringBuffer stringBuffer;
    StringBuffer answerIdsstringBuffer;
    boolean TEST;
    String cgameid = "";
    Intent intent;

    Dialog dialog;
    View inflate;
    TextView tv_cancel, tv_enter;
    boolean isend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        TEST = intent.getBooleanExtra(Utils.TEST, false);
        if (intent.getStringExtra(Utils.CGAMEID) != null) {
            cgameid = intent.getStringExtra(Utils.CGAMEID);//
        }
        dialog = new Dialog(mActivity, R.style.dialogstyle);
        inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_characterttest, null);
        dialog.setContentView(inflate);
        show();
        tv_cancel = inflate.findViewById(R.id.tv_cancel);
        tv_enter = inflate.findViewById(R.id.tv_enter);
        tv_cancel.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        replaceQuestionPresenter = new ReplaceQuestionPresenter(this);
        close_iv.setOnClickListener(this);
        btn_enter_test.setOnClickListener(this);
        arrayBeans = new ArrayList<>();
        swiperecyclerview.setLayoutManager(new LinearLayoutManager(this));
        characterttestAdapter = new CharacterttestAdapter(this);
        swiperecyclerview.setOnItemClickListener(this);
        swiperecyclerview.setAdapter(characterttestAdapter);
        tv_start_test.setOnClickListener(this);
        replaceQuestionPresenter.getTestQuestion();
        vipDialog = new VipDialog(this);
        vipDialog.setCanceledOnTouchOutside(false);
        questionIdlist = new ArrayList<>();
        stringBuffer = new StringBuffer();
        answerIdsstringBuffer = new StringBuffer();
        dialoganswerrecord = new Dialoganswerrecord(this);
        dialoganswerrecord.setOnClick(new Dialoganswerrecord.OnClick() {
            @Override
            public void onclick(View v) {
                dialoganswerrecord.dismiss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_characterttest;
    }

    public void show() {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        WindowManager.LayoutParams answerparams = dialog.getWindow().getAttributes();
        answerparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        answerparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        answerparams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(answerparams);
    }

    @Override
    public ReplaceQuestionPresenter createPresenter() {
        return replaceQuestionPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                dialog.show();
                break;

            case R.id.tv_start_test:
                if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 6) {
                    if (Utils.netWork()) {
                        stringBuffer.delete(0, stringBuffer.length());
                        for (int i = 0; i < arrayBeans.size(); i++) {
                            stringBuffer.append(arrayBeans.get(i).getQuestionId() + ",");
                        }
                        if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 6) {
                            replaceQuestionPresenter.ReplaceQuestion(stringBuffer.toString());
                        } else {
                            vipDialog.show();
                        }
                    } else {
                        toastShow(R.string.nonetwork);
                    }
                } else {
                    vipDialog.show();
                }
                break;
            case R.id.btn_enter_test:
                replaceQuestionPresenter.submitUserAnswer(cgameid, answerIdsstringBuffer.toString());
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_enter:
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void ReplaceQuestionSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                TestQuestionBean.DataBean.ArrayBean arrayBean = JSON.parseObject(jsonObject.getJSONObject("data").toString(), TestQuestionBean.DataBean.ArrayBean.class);
                int answerposition = Integer.parseInt(tv_position1.getText().toString().trim()) - 1;
                arrayBeans.get(answerposition).setAnswer(arrayBean.getAnswer());
                arrayBeans.get(answerposition).setAddDate(arrayBean.getAddDate());
                arrayBeans.get(answerposition).setQuestionId(arrayBean.getQuestionId());
                arrayBeans.get(answerposition).setSort(arrayBean.getSort());
                arrayBeans.get(answerposition).setState(arrayBean.getState());
                arrayBeans.get(answerposition).setSubject(arrayBean.getSubject());
                characterttestAdapter.setArrayBeans(arrayBeans.get(answerposition).getAnswer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getTestQuestionSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                arrayBeans.addAll(JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("array").toString(), TestQuestionBean.DataBean.ArrayBean.class));
                characterttestAdapter.setArrayBeans(arrayBeans.get(0).getAnswer());
                tv_position1.setText("1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submitUserAnswerSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(View view, int adapterPosition) {

        RxView.clicks(view)
                .throttleFirst(700, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object s) throws Exception {
                        if (!isend) {

                            int answerposition = Integer.parseInt(tv_position1.getText().toString().trim()) - 1;
                            List<TestQuestionBean.DataBean.ArrayBean.AnswerBean> answer = arrayBeans.get(answerposition).getAnswer();
                            for (int i = 0; i < answer.size(); i++) {
                                arrayBeans.get(answerposition).getAnswer().get(i).setIsselect(false);
                            }
                            arrayBeans.get(answerposition).getAnswer().get(adapterPosition).setIsselect(true);

                            answerIdsstringBuffer.append(arrayBeans.get(answerposition).getAnswer().get(adapterPosition).getAnswerId() + ",");
                            characterttestAdapter.notifyDataSetChanged();
                            //上面是响应变换
                            swiperecyclerview.postDelayed(new Runnable() {//这里点击 延迟300ms
                                @Override
                                public void run() {

                                    if (Integer.parseInt(tv_position1.getText().toString().trim()) >= 13) {
                                        if (tv_position1.getText().toString().trim().equals("13")) {
                                            isend = true;
                                            stringBuffer.delete(0, stringBuffer.length());
                                            for (int i = 0; i < arrayBeans.size(); i++) {
                                                stringBuffer.append(arrayBeans.get(i).getQuestionId() + ",");
                                            }
                                            if (TEST) {
                                                btn_enter_test.setVisibility(View.VISIBLE);
                                                tv_start_test.setVisibility(View.GONE);

                                            } else {
                                                Intent intent = new Intent(mActivity, AnswersummaryActivity.class);
                                                intent.putExtra(Utils.ANSWERIDS, answerIdsstringBuffer.toString());
                                                intent.putExtra(Utils.QUESTIONIDS, stringBuffer.toString());
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    } else {
                                        characterttestAdapter.setArrayBeans(arrayBeans.get(Integer.parseInt(tv_position1.getText().toString().trim())).getAnswer());
                                        tv_question.setText(arrayBeans.get(Integer.parseInt(tv_position1.getText().toString().trim())).getSubject());
                                        tv_position1.setText(Integer.parseInt(tv_position1.getText().toString().trim()) + 1 + "");
                                    }
                                }
                            }, 300);
                        }
                    }
                });

    }
}
