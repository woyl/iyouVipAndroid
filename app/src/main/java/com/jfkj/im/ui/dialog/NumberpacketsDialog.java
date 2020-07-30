package com.jfkj.im.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.jfkj.im.R;

public class NumberpacketsDialog extends Dialog implements View.OnClickListener, TextWatcher {
    EditText et_number;
    TextView tv_cancel, tv_recharge, tv_hint;
    Ontemclick ontemclick;
    public NumberpacketsDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    public void setOntemclick(Ontemclick ontemclick) {
        this.ontemclick = ontemclick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_numberpackets);
        et_number = findViewById(R.id.et_number);
        tv_hint = findViewById(R.id.tv_hint);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_recharge = findViewById(R.id.tv_recharge);
        tv_cancel.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        tv_recharge.setEnabled(false);
        et_number.addTextChangedListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_recharge:
                if (TextUtils.isEmpty(et_number.getText().toString().trim())) {
                    Toast.makeText(getContext(), "请输入红包数量", Toast.LENGTH_SHORT).show();
                    return;
                }


                 ontemclick.onclick(et_number.getText().toString().trim());
                dismiss();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())){
            long num = Long.parseLong(s.toString().trim());
            if (num <= 9) {
                tv_hint.setText("红包数量最少为10");
                tv_hint.setVisibility(View.VISIBLE);
                tv_recharge.setEnabled(false);
                tv_recharge.setAlpha(0.5f);
            } else if (num > 100000){
                tv_hint.setText("红包数量最多为100000");
                tv_hint.setVisibility(View.VISIBLE);
                tv_recharge.setEnabled(false);
//                tv_recharge.setAlpha(0.5f);
            } else {
                tv_hint.setVisibility(View.INVISIBLE);
                tv_recharge.setEnabled(true);
                tv_recharge.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shap_bt_two_black_bg));
//                tv_recharge.setAlpha(1f);
            }
        }
    }

    public interface Ontemclick{
        public void  onclick(String s);
    }
}
