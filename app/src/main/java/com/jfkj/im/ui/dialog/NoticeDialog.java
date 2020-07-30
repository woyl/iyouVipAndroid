package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jfkj.im.R;

public class NoticeDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private CommonDialog.CustomizeDialogListener listener;
    private View mView;
    private boolean mOneButton = false;



    public NoticeDialog(Context context, int theme, CommonDialog.CustomizeDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
    }

    public NoticeDialog(Context context, CommonDialog.CustomizeDialogListener listener) {
        this(context, R.style.dialogstyle, listener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);//设置布局属性占满宽度
        //        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.CENTER;//设置dialog位于底部
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {

     //  mView.findViewById(R.id.tv_submit).setOnClickListener(this);


        mView.findViewById(R.id.tv_submit).setOnClickListener(this);
        mView.findViewById(R.id.tv_cancel).setOnClickListener(this);



    }



    public NoticeDialog setContentText(String content) {
      //  TextView dialogContent = (TextView) mView.findViewById(R.id.tv_message);
      //  dialogContent.setText(content);
        return this;
    }



    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
