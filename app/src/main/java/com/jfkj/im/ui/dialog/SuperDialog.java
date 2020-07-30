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

public class SuperDialog extends Dialog {
    onClick click;

    public void setClick(onClick click) {
        this.click = click;
    }

    TextView tv_enter;
    public SuperDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_super);
        tv_enter=findViewById(R.id.tv_enter);
        tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onclick(v);
            }
        });
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
    public interface onClick{
        public void onclick(View view);
    }
}
