package com.jfkj.im.ui.mine;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.widget.StatusBarHeightView;
import com.lzy.okgo.utils.OkLogger;

import butterknife.BindView;
import butterknife.OnClick;

public class AddUserInfoActivity extends BaseActivity implements TextWatcher {


    private static final int RESULTSCHOOL = 0X11;
    private static final int RESULDESC = 0X12;

    @BindView(R.id.status)
    StatusBarHeightView status;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.edit_input_content)
    EditText editInputContent;
    @BindView(R.id.tv_text_num)
    TextView tvTextNum;
    @BindView(R.id.layout_input_content)
    ConstraintLayout layoutInputContent;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    int textSize;
    private String type;


    @OnClick({R.id.iv_back,R.id.tv_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_confirm:

                Intent intent = new Intent();
                intent.putExtra("dataKey",editInputContent.getText().toString().trim());

                if("desc".equals(type)){
                    setResult(RESULDESC,intent);
                }else{
                    setResult(RESULTSCHOOL,intent);
                }
                finish();
                break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getStringExtra("type");

        if("desc".equals(type)){
            //个人描述
            editInputContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            tvName.setText("添加个人描述");
            editInputContent.setHint("请填写个人描述");
            tvTextNum.setText("0/50");

            textSize = 50;
        }else{
            //院校
            editInputContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            editInputContent.setHint("请填写院校");
            tvName.setText("添加院校");
            textSize = 15;
            tvTextNum.setText("0/15");
        }

        editInputContent.addTextChangedListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_user_info;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        OkLogger.e(s.toString());
        if (s.length() > 0  ) {
            tvConfirm.setClickable(true);
            tvConfirm.setAlpha(1);
            tvTextNum.setText(s.length() + "/" + textSize);
        } else {
            tvConfirm.setClickable(false);
            tvConfirm.setAlpha(0.5f);
            tvTextNum.setText("0/"+ textSize);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
