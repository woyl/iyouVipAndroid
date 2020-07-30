package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jfkj.im.R;

import androidx.annotation.NonNull;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/28
 * </pre>
 */
public class BlackListDialog extends Dialog implements View.OnClickListener {
    TextView tv_cancel;
    TextView tv_enter;
    private OnItemClick onItemClick;

    public void BlackListDialog(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public BlackListDialog(@NonNull Context context) {
        super(context,R.style.dialogstyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectsex);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_enter = findViewById(R.id.tv_enter);
        tv_cancel.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_enter:
                onItemClick.setOnItem();
                break;
        }
    }

    public interface OnItemClick{
        public void setOnItem();
    }


}
