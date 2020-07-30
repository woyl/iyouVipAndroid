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

public class Dialoglevel extends Dialog implements View.OnClickListener {
    TextView tv_confirm;
    onClick click;
    private String content;

    public void setClick(onClick click) {
        this.click = click;
    }
    Context context;
    public Dialoglevel(@NonNull Context context) {
        super(context,R.style.dialogstyle);
        this.context=context;
    }



    public Dialoglevel(@NonNull Context context,String content) {
        super(context,R.style.dialogstyle);
        this.context=context;
        this.content = content;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_level);
        tv_confirm = findViewById(R.id.tv_confirm);

        TextView tvContent =  findViewById(R.id.tv_content);

        if(!"".equals(content)){
            tvContent.setText(content);
        }

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                click.click(v);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        params.width = (int) (widthPixels * 0.8f);
        getWindow().setAttributes(params);
    }
    public interface onClick {
        public void click(View view);
    }
}
