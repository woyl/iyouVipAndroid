package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomUtil;
import com.jfkj.im.retrofit.ApiClient;

import androidx.annotation.NonNull;


public class ExitDialog extends Dialog {
    TextView tv_cancel,tv_enter;
    private Context mContext;
    public ExitDialog(@NonNull Context context) {
        super(context,R.style.dialogstyle);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit);
        tv_cancel=findViewById(R.id.tv_cancel);
        tv_enter=findViewById(R.id.tv_enter);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoomUtil.quitGroup();
                ApiClient.onDestroy();
                System.exit(0);
            }
        });
    }
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);

    }
}
