package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.jfkj.im.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/4
 * </pre>
 */
public class PickDialog extends Dialog implements View.OnClickListener, OnItemSelectedListener {
    private Context mContext;
    private TextView tvTitle;
    private TextView tvCancel;
    private TextView tvConfirm;
    private WheelView wheelView;
    private String title;
    private List<String> mList = new ArrayList<>();
    private String pickStr;
    private onConfirmDialogListener listener;

    public PickDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }
    public PickDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public PickDialog(@NonNull Context context, int themeResId, String title, List<String> dataList,onConfirmDialogListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.mList = dataList;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pick_view);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);//设置布局属性占满宽度
//        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.CENTER;//设置dialog位于底部
        window.setAttributes(params);

        tvTitle = findViewById(R.id.tv_title);
        wheelView = findViewById(R.id.wheelView);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);

        tvTitle.setText(title);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        wheelView.setCyclic(false);
        wheelView.setAdapter(new ArrayWheelAdapter(mList));
        wheelView.setOnItemSelectedListener(this);
    }

    public PickDialog setTitle(String title) {
        TextView textView = findViewById(R.id.tv_title);
        textView.setText(title);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                dismiss();
                if (TextUtils.isEmpty(pickStr)){
                    pickStr = mList.get(0);
                }
                listener.onStrPick(pickStr);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(int index) {
        pickStr = mList.get(index);
    }

    public interface onConfirmDialogListener{
        void onStrPick(String pickStr);
    }


}
