package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class RedPacketDialog extends Dialog implements View.OnClickListener {

    CircleImageView default_head_iv;
    TextView tv_name;
    TextView tv_hint;
    ImageView iv_close;
    ImageView iv_open;
    String redId;
    String serialNo;

    public RedPacketDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_open:
                if (Utils.netWork()) {
                    Intent intent = new Intent("pick_redpacket");
                    intent.putExtra(Utils.REDID, redId);
                    intent.putExtra(Utils.SERIALNO, serialNo);
                    App.getAppContext().sendBroadcast(intent);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.nonetwork, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_redpacket);
        default_head_iv = findViewById(R.id.default_head_iv);
        tv_name = findViewById(R.id.tv_name);
        tv_hint = findViewById(R.id.tv_hint);
        iv_close = findViewById(R.id.iv_close);
        iv_open = findViewById(R.id.iv_open);
        iv_open.setOnClickListener(this);
        iv_close.setOnClickListener(this);

    }

    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    public void setMessage(String redId, String url, String name, String hint, String serialno) {
        Glide.with(getContext()).load(url).into(default_head_iv);
        tv_name.setText(name);
        tv_hint.setText(hint);
        this.redId = redId;
        this.serialNo = serialno;
    }
}
