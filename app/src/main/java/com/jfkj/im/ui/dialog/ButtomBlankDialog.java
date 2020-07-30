package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.jfkj.im.R;

import java.util.ArrayList;
import java.util.List;

public class ButtomBlankDialog extends Dialog implements OnItemSelectedListener, View.OnClickListener {
    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private boolean isBackCanCelable;//
    private List<String> mList = new ArrayList<>();

    private onConfirmDialogListener listener;
    private Context context;
    private WheelView wheelView;
    private String s = "中国农业银行";

    public ButtomBlankDialog(Context context,   boolean isCancelable,boolean isBackCancelable,List<String> list,onConfirmDialogListener listener) {
        super(context, R.style.MyDialog);

        this.context = context;
        this.listener = listener;
        this.mList = list;
        this.iscancelable = isCancelable;
        this.isBackCanCelable=isBackCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.dialog_buttom_blank);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCanCelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);


         findViewById(R.id.tv_close).setOnClickListener(this);
         findViewById(R.id.tv_submit).setOnClickListener(this);

        wheelView = findViewById(R.id.wheelView);
        wheelView.setCyclic(false);
        wheelView.setAdapter(new ArrayWheelAdapter(mList));
        wheelView.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(int index) {
        s = mList.get(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_close:
                this.dismiss();
                break;
            case R.id.tv_submit:
                listener.onStrPick(s);

                break;
        }

    }


    public interface onConfirmDialogListener{
        void onStrPick(String pickStr);
    }
}