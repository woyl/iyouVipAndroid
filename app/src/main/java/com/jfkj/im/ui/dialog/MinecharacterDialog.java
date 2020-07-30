package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jfkj.im.R;

public class MinecharacterDialog extends Dialog implements View.OnClickListener {
    TextView tv_cancel, tv_confirm;
    onClick click;

    public void setClick(onClick click) {
        this.click = click;
    }

    public MinecharacterDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_minecharacter);
        tv_cancel=findViewById(R.id.tv_cancel);
        tv_confirm=findViewById(R.id.tv_confirm);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                click.onclickListener(v);
                break;
        }
    }

    public interface onClick {
        public void onclickListener(View view);
    }
}
