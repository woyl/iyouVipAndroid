package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jfkj.im.Bean.Selecttime;
import com.jfkj.im.R;
import com.jfkj.im.adapter.ScrollPickerAdapter;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.view.ScrollPickerView;

import java.util.ArrayList;
import java.util.List;



public class SelecttimeDialog extends Dialog implements View.OnClickListener {

    TextView tv_cancel;
    TextView tv_enter;
    ScrollPickerView scrollPickerView;
    SelectResult selectResult;

    public void setSelectResult(SelectResult selectResult) {
        this.selectResult = selectResult;
    }

    public SelecttimeDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selecttime);
        scrollPickerView = findViewById(R.id.pickerview);
        scrollPickerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_enter = findViewById(R.id.tv_enter);
        tv_cancel.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        initData();
    }
    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
             list.add("0" + i + ":00") ;
            }
            if (i >=10) {
                list.add(i + ":00");
            }
        }
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder =
                new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(getContext())
                        .setDataList(list)
                        .selectedItemOffset(0)
                        .visibleItemNumber(3)
                        .setDivideLineColor("#E5E5E5")
                        .setItemViewProvider(null)
                        .setOnClickListener(new ScrollPickerAdapter.OnClickListener() {
                            @Override
                            public void onSelectedItemClicked(View v) {

                            }
                        });
        ScrollPickerAdapter mScrollPickerAdapter = builder.build();
        scrollPickerView.setAdapter(mScrollPickerAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_enter:
               selectResult.selecttime(SPUtils.getInstance(getContext()).getString("isSelected"));
                break;
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
    public interface  SelectResult{
        public void selecttime(String s);
    }
}
