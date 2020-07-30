package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.R;

import java.util.ArrayList;
import java.util.List;

public class DialogUserDetails extends Dialog implements View.OnClickListener {

    private boolean iscancelable;//控制点击dialog外部是否dismiss


    private ButtomBlankDialog.onConfirmDialogListener listener;
    private Context context;

    private  ItemDialogClick itemDialogClick;

    private String name ;


    public DialogUserDetails(ItemDialogClick itemDialogClick,Context context, boolean isCancelable,  String name) {
        super(context, R.style.MyDialog);

        this.itemDialogClick = itemDialogClick;
        this.context = context;
        this.listener = listener;
        this.name = name;

        this.iscancelable = isCancelable;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_user_details);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);


        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(name);
        findViewById(R.id.tv_jubao).setOnClickListener(this);
        findViewById(R.id.tv_lahei).setOnClickListener(this);
        findViewById(R.id.tv_close).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_jubao:
                dismiss();
                itemDialogClick.onItemClick(R.id.tv_jubao);
                break;
            case R.id.tv_lahei:
                dismiss();
                itemDialogClick.onItemClick(R.id.tv_lahei);

                break;
            case R.id.tv_close:
                this.dismiss();
                break;
        }
    }

    public interface ItemDialogClick{
        void onItemClick(int viewId);
    }


}
