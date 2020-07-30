package com.jfkj.im.ui.dialog;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.utils.media.ScreenUtil;

import java.util.Objects;

public class VipUpgradeOneDialogFragment extends BaseDialogFragment {

    private String content;

    private ResponListener<Boolean> responListener;

    public void setResponListener(ResponListener<Boolean> responListener) {
        this.responListener = responListener;
    }

    public VipUpgradeOneDialogFragment(boolean isWidth, int ori, String content) {
        super(isWidth, ori);
        this.content = content;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_vip_one,null);
        AppCompatImageView image = view.findViewById(R.id.image);
        AppCompatTextView tv_content = view.findViewById(R.id.tv_content);
        AppCompatImageView img_cancel = view.findViewById(R.id.img_cancel);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        int w = ScreenUtil.getScreenWidth();
        int h = ScreenUtil.getScreenHeight();
        layoutParams.height = (int) (h * 0.26);
        layoutParams.width = (int) (w * 0.7);

        tv_content.setText(content);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responListener.Rsp(true);
                dismiss();
            }
        });
    }
}
