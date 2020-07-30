package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

public class DialogSqurehint extends Dialog {

    onClick click;
    CheckBox cb_hint;
    public void setClick(onClick click) {
        this.click = click;
    }

    TextView tv_cancel;
    TextView tv_confirm;
    public DialogSqurehint(@NonNull Context context) {
        super(context, R.style.dialogstyle);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_squre_hint);
        tv_cancel=findViewById(R.id.tv_cancel);
        tv_confirm=findViewById(R.id.tv_confirm);
        cb_hint=findViewById(R.id.cb_hint);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                click.setClickcancel();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                click.setClickenter();
                if(cb_hint.isChecked()){
                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID+Utils.SQUREHINT,true);
                }

            }
        });
    }
    public interface onClick {
        public void setClickenter();
        public void setClickcancel();
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
