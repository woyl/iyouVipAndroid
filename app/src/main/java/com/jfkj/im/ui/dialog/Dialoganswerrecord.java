package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jfkj.im.R;

public class Dialoganswerrecord extends Dialog implements View.OnClickListener {
    TextView tv_confirm;
    TextView tv_cancel;
    OnClick onClick;

    public Dialoganswerrecord(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_answer_record);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                onClick.onclick(v);
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public interface OnClick {
        public void onclick(View v);
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
}
