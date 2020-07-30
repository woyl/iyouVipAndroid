package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jfkj.im.R;

public class ReceiveDialog extends Dialog {
    private Context mContext;
      Button btn;
    public ReceiveDialog(@NonNull Context context) {
        super(context,R.style.dialogstyle);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_receive);
        btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(mContext,"领取礼物成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels *0.65); // 设置宽度
        params.height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.4); // 设置宽度
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }
}
