package com.jfkj.im.ui.dialog;

import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;

public class SpannableStringBaseDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private String content,cancel,sure;
    private ResponListener<Boolean> responListener;
    private boolean isCenter;
    private SpannableString spannableString;

    public void setResponListener (ResponListener<Boolean> responListener){
        this.responListener = responListener;
    }

    public SpannableStringBaseDialogFragment(boolean isWidth, int ori, SpannableString content, String cancel, String sure, boolean isCenter) {
        super(isWidth, ori);
        this.spannableString = content;
        this.cancel = cancel;
        this.sure = sure;
        this.isCenter = isCenter;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_tip_base_dialog_f,null);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        if (isCenter){
            tv_content.setGravity(Gravity.CENTER);
        }else {
            tv_content.setGravity(Gravity.LEFT);
        }
        tv_content.setText(spannableString);
        tv_cancel.setText(cancel);
        tv_confirm.setText(sure);
        tv_confirm.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                responListener.Rsp(true);
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
}
