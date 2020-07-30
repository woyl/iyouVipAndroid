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

import androidx.annotation.NonNull;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/6
 * </pre>
 */
public class OneButtonDialog extends Dialog {
    private View view;
    private Context mContext;
    private TextView tvConfirm;
    private CustomDialogListener mListener;

    public OneButtonDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public interface CustomDialogListener {
        void onClick(View view);
    }

    public OneButtonDialog(@NonNull Context context, int themeResId,CustomDialogListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_one_button,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);

        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v);
            }
        });

    }

    public OneButtonDialog setTitle(String title){
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        return this;
    }

    public OneButtonDialog setContent(String content){
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(content);
        return this;
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);//设置布局属性占满宽度
        //        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.CENTER;//设置dialog位置
        window.setAttributes(params);
    }
}
