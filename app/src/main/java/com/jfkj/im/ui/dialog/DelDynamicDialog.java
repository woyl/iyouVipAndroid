package com.jfkj.im.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.R;

public class DelDynamicDialog  extends BottomSheetDialog {
    private Context context ;

    private OnDelClickLister onDelClickLister;

    public DelDynamicDialog(@NonNull Context context,OnDelClickLister onDelClickLister) {
        super(context);
        this.context = context;
        this.onDelClickLister = onDelClickLister;
        createView();
    }

    private void createView() {

        View bottomView = LayoutInflater.from(context).inflate(R.layout.layout_del_dynamic_dialog, null);

        bottomView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dis();
            }
        });

        bottomView.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除

                dis();
                onDelClickLister.onClickDel(v);
            }
        });


        setContentView(bottomView);
    }


    public void dis(){
        this.dismiss();
    }



    public interface OnDelClickLister{
        void onClickDel(View v);
    }

}
