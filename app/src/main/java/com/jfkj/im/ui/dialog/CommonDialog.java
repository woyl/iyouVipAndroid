package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jfkj.im.R;

import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;


/**
 * @author Administrator
 */
public class CommonDialog extends Dialog implements OnClickListener {

    private Context context;
    private CustomizeDialogListener listener;
    private View mView;
    private boolean mOneButton = false;

    public interface CustomizeDialogListener {
        void onClick(View view);
    }

    public CommonDialog( Context context) {
        super(context);
        this.context = context;
    }

    public CommonDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public CommonDialog(Context context, int theme, CustomizeDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
    }

    public CommonDialog(Context context, CustomizeDialogListener listener) {
        this(context, R.style.dialogstyle, listener);
    }

    /**
     * 原有功能扩展
     *
     * @param context   上下文
     * @param oneButton 显示一个按钮
     * @param listener
     */
    public CommonDialog(Context context, boolean oneButton, CustomizeDialogListener listener) {
        super(context, R.style.dialogstyle);
        this.context = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        mOneButton = oneButton;
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
        initView();
    }

    private void initView() {
        mView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mView.findViewById(R.id.tv_confirm).setOnClickListener(this);
        if (mOneButton) {
            hideView(R.id.tv_cancel);
        }
    }

    public CommonDialog setTitleText(String titleText) {
        TextView dialogTitle = (TextView) mView.findViewById(R.id.tv_title);
        dialogTitle.setText(titleText);
        return this;
    }

    public CommonDialog setContentText(String content) {
        TextView dialogContent = (TextView) mView.findViewById(R.id.tv_content);
        dialogContent.setText(Html.fromHtml(content));
        return this;
    }

    public CommonDialog setYesBtn(String str){
        TextView tv_submit =  mView.findViewById(R.id.tv_confirm);

        tv_submit.setText(str);
        return this;
    }


    public CommonDialog setContentTextHtml(String contentHtml) {
        TextView dialogContent = (TextView) mView.findViewById(R.id.tv_content);
        dialogContent.setText(Html.fromHtml(contentHtml));
        return this;
    }

    public CommonDialog hideView(int viewId) {
        View view = mView.findViewById(viewId);
        view.setVisibility(View.GONE);
        return this;
    }

    public CommonDialog setConfirmBtnText(String text) {
        TextView dialogConfirm = (TextView) mView.findViewById(R.id.tv_confirm);
        dialogConfirm.setText(text);
        return this;
    }

    public CommonDialog setCancelBtnText(String text) {
        TextView dialogCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        dialogCancel.setText(text);
        return this;
    }

    public CommonDialog setTextColor(int viewId, int colotResId) {
        TextView view = mView.findViewById(viewId);
        view.setTextColor(ContextCompat.getColor(context, colotResId));
        return this;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
