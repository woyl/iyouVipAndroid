package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jfkj.im.R;

public class Groupsetdialog extends Dialog implements View.OnClickListener {

    TextView tv_message;
    TextView tv_cancel;
    TextView tv_enter;

    public Groupsetdialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_groupset);
        tv_message = findViewById(R.id.tv_message);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_enter = findViewById(R.id.tv_enter);
        tv_enter.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }
    public void selMessage(String msg) {
        tv_message.setText(msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
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
}
