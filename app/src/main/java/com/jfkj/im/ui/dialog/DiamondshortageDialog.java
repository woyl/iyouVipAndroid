package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jfkj.im.R;

public class DiamondshortageDialog extends Dialog implements View.OnClickListener {
  TextView tv_cancel;
  TextView tv_recharge;

    public DiamondshortageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diamondshortage);
        tv_cancel=findViewById(R.id.tv_cancel);
        tv_recharge=findViewById(R.id.tv_recharge);
        tv_cancel.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                   dismiss();
                break;
            case R.id.tv_recharge:

                break;
        }
    }
}
