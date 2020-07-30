package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jfkj.im.R;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/22
 * </pre>
 */
public class FilterDialog extends Dialog implements View.OnClickListener {
    private LinearLayout tvMale;
    private LinearLayout tvFemale;
    private LinearLayout tvAll;
    private Context mContext;
    private onListItem mListItem;

    public FilterDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public void onListItem(onListItem listItem){
        this.mListItem = listItem;
    }

    public FilterDialog(Context context, int themeResId) {
        super(context, R.style.dialogstyle);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter);
        tvMale = findViewById(R.id.layout_male);
        tvFemale = findViewById(R.id.layout_female);
        tvAll = findViewById(R.id.layout_all);
        tvMale.setOnClickListener(this);
        tvFemale.setOnClickListener(this);
        tvAll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_male:
                mListItem.onItem(0);
                dismiss();
                break;
            case R.id.layout_female:
                mListItem.onItem(1);
                dismiss();
                break;
            case R.id.layout_all:
                mListItem.onItem(2);
                dismiss();
                break;
            default:
        }
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

    public interface onListItem{
        void  onItem(int i);
    }
}
