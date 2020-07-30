package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
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
public class ReportDialog extends Dialog implements View.OnClickListener {

    private View mView;
    private TextView mTvReport;
    private TextView mTvBlacklist;
    private TextView mTvCancel;
    private itemDialogClick mDialogClick;
    private Context mContext;

    public ReportDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_report, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        mTvReport = mView.findViewById(R.id.tv_report);
        mTvBlacklist = mView.findViewById(R.id.tv_blacklist);
        mTvCancel = mView.findViewById(R.id.tv_cancel);
        mTvReport.setOnClickListener(this);
        mTvBlacklist.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    public  ReportDialog setFirst(String text){
        TextView tvReport = mView.findViewById(R.id.tv_report);
        tvReport.setText(text);
        return this;
    }

    public  ReportDialog setSecond(String text){
        TextView tvSecond = mView.findViewById(R.id.tv_blacklist);
        tvSecond.setText(text);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_report:
                mDialogClick.onItemClick(R.id.tv_report);
                break;
            case R.id.tv_blacklist:
                mDialogClick.onItemClick(R.id.tv_blacklist);
                break;
            case R.id.tv_cancel:
                dismiss();
//                mDialogClick.onItemClick(R.id.tv_cancel);
                break;
            default:
                break;
        }
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
    public void setOnItemDialogClick(itemDialogClick click){
        this.mDialogClick = click;
    }
    public interface itemDialogClick{
        void onItemClick(int viewId);
    }
}
