package com.jfkj.im.ui.party;

import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.crushicetasl.CrushIceSubmitTaskView;
import com.jfkj.im.mvp.crushicetasl.CrushSubmitTaskPresenter;
import com.jfkj.im.view.Ruler;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCrushIceTaskActivity extends BaseActivity<CrushSubmitTaskPresenter> implements TextWatcher, CrushIceSubmitTaskView {

    @BindView(R.id.iv_title_right)
    ImageView iv_title_right;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.ruler)
    Ruler ruler;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.tv_pic_task)
    TextView tv_pic_task;
    @BindView(R.id.tv_video_task)
    TextView tv_video_task;

    String taskType = "1";


    private int start = 100;
    private boolean isPic,isVideo;
    private String taskId,taskContent,taskMoney;

    private CrushSubmitTaskPresenter crushSubmitTaskPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        setAndroidNativeLightStatusBar(this,false);
        taskId = Objects.requireNonNull(getIntent().getExtras()).getString("taskId");
        taskContent = getIntent().getExtras().getString("taskContent");
        taskMoney = getIntent().getExtras().getString("taskMoney");
        taskType = getIntent().getExtras().getString("taskType","");
        iv_title_right.setVisibility(View.GONE);
        tv_title.setText("我的任务");
        et_content.setText(taskContent);
        et_content.setSelection(taskContent.length());
        et_content.addTextChangedListener(this);
//        ruler.setValue(start);

        double value = Double.parseDouble(taskMoney);
        long valueLong = (long) value;
        tv_money.setText(valueLong+"");
        ruler.setValue(valueLong);
        ruler.setOnValueChangeListener(new Ruler.OnValueChangeListener() {
            @Override
            public void onValueChange(long vule) {
                tv_money.setText(String.valueOf(vule));
                showContentBg();
            }
        });



        if (taskType.equals("1")){
            tv_pic_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_ice_select_bg));
            tv_video_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_cle_fivgray_bg));
            tv_pic_task.setTextColor(ContextCompat.getColor(this,R.color.cFF2B66));
            tv_video_task.setTextColor(ContextCompat.getColor(this,R.color.c666666));
        }else {
            tv_pic_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_cle_fivgray_bg));
            tv_video_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_ice_select_bg));
            tv_video_task.setTextColor(ContextCompat.getColor(this,R.color.cFF2B66));
            tv_pic_task.setTextColor(ContextCompat.getColor(this,R.color.c666666));
        }


        isPic = true;
        crushSubmitTaskPresenter = new CrushSubmitTaskPresenter(this);

//        showContentBg();
        tv_submit.setEnabled(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_crush_ice_task;
    }

    @Override
    public CrushSubmitTaskPresenter createPresenter() {
        return crushSubmitTaskPresenter;
    }

    @OnClick({R.id.iv_title_back,R.id.tv_pic_task,R.id.tv_video_task,R.id.tv_submit})
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_pic_task:
                isPic = true;
                isVideo = false;
                selectTask();
                break;
            case R.id.tv_video_task:
                isPic = false;
                isVideo = true;
                selectTask();
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(getContent())){
                    toastShow("请输入任务内容");
                    return;
                }
                String type = "1";
                if (!isPic){
                    type = "2";
                }
                crushSubmitTaskPresenter.submitMyTask(taskId,type,getContent(),getMoney());
                break;
            default:break;
        }
    }

    public String getContent(){
        return et_content.getText().toString().trim();
    }

    public String getMoney(){
        return tv_money.getText().toString();
    }

    private void selectTask() {
        showContentBg();
        if (isPic){
            tv_pic_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_ice_select_bg));
            tv_video_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_cle_fivgray_bg));
            tv_pic_task.setTextColor(ContextCompat.getColor(this,R.color.cFF2B66));
            tv_video_task.setTextColor(ContextCompat.getColor(this,R.color.c666666));
        }else {
            tv_pic_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_cle_fivgray_bg));
            tv_video_task.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_ice_select_bg));
            tv_video_task.setTextColor(ContextCompat.getColor(this,R.color.cFF2B66));
            tv_pic_task.setTextColor(ContextCompat.getColor(this,R.color.c666666));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        showContentBg();
    }

    @SuppressLint("SetTextI18n")
    private void showContentBg() {
        tv_number.setText(et_content.getText().toString().trim().length() + "/20");
        if (et_content.getText().toString().trim().length() == 0) {
            tv_submit.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_tv_three_bg_gray));
            tv_submit.setEnabled(false);
        } else {
            tv_submit.setBackground(ContextCompat.getDrawable(this,R.drawable.shap_bt_two_bg));
            tv_submit.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void submitSuccess(String msg) {
//        toastShow(msg);
        finish();
    }

    @Override
    public void onFails(String error) {
        toastShow(error);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }
}
