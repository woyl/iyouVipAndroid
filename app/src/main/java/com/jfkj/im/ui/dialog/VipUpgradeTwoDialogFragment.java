package com.jfkj.im.ui.dialog;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.utils.media.ScreenUtil;

public class VipUpgradeTwoDialogFragment extends BaseDialogFragment{
    private String content,btContent;

    private ResponListener<Boolean> responListener;

    public void setResponListener(ResponListener<Boolean> responListener) {
        this.responListener = responListener;
    }

    public VipUpgradeTwoDialogFragment(boolean isWidth, int ori,String content,String btContent) {
        super(isWidth, ori);
        this.content = content;
        this.btContent = btContent;
    }


    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_vip_two,null);
        AppCompatImageView image = view.findViewById(R.id.image);
        AppCompatTextView tv_content = view.findViewById(R.id.tv_content);
        AppCompatImageView img_cancel = view.findViewById(R.id.img_cancel);
        AppCompatTextView tv_bt = view.findViewById(R.id.tv_bt);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        int w = ScreenUtil.getScreenWidth();
        int h = ScreenUtil.getScreenHeight();
        layoutParams.height = (int) (h * 0.35);
        layoutParams.width = (int) (w * 0.7);

        tv_content.setText(content);
        tv_bt.setText(btContent);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responListener.Rsp(true);
                dismiss();
            }
        });
    }
}
