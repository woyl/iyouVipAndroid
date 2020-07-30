package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/30
 * </pre>
 */
public class NoHintDialog extends Dialog implements View.OnClickListener {
    private View mView;
    private Context mContext;
    private CustomizeDialogListener listener;
    private CheckBox checkBox;
    private TextView tvConfirm;

    public NoHintDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public NoHintDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public NoHintDialog(Context context, int theme, CustomizeDialogListener listener) {
        super(context, theme);
        this.mContext = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_no_hint, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);//设置布局属性占满宽度
        //        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.CENTER;//设置dialog位于底部
        window.setAttributes(params);
        initView();
    }

    private void initView() {
        mView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        tvConfirm = mView.findViewById(R.id.tv_confirm);
        checkBox = mView.findViewById(R.id.cb_hint);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    UserInfoManger.saveIfNeedShow(false);
                }else {
                    UserInfoManger.saveIfNeedShow(true);
                }
                listener.onClick(v);
            }
        });

    }

    public NoHintDialog setTitleText(String titleText) {
        TextView dialogTitle = (TextView) mView.findViewById(R.id.tv_title);
        dialogTitle.setText(titleText);
        return this;
    }

    public NoHintDialog setContentText(String content) {
        TextView dialogContent = (TextView) mView.findViewById(R.id.tv_content);
        dialogContent.setText(content);
        return this;
    }

    public NoHintDialog setContentTextHtml(String contentHtml) {
        TextView dialogContent = (TextView) mView.findViewById(R.id.tv_content);
        dialogContent.setText(Html.fromHtml(contentHtml));
        return this;
    }

    public NoHintDialog setConfirmBtnText(String text) {
        TextView dialogConfirm = (TextView) mView.findViewById(R.id.tv_confirm);
        dialogConfirm.setText(text);
        return this;
    }

    public NoHintDialog setCancelBtnText(String text) {
        TextView dialogCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        dialogCancel.setText(text);
        return this;
    }

    public NoHintDialog setTextColor(int viewId, int colotResId) {
        TextView view = mView.findViewById(viewId);
        view.setTextColor(ContextCompat.getColor(mContext, colotResId));
        return this;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }

    public interface CustomizeDialogListener {
        void onClick(View view);
    }
}
