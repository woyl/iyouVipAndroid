package com.jfkj.im.ui.dialog;

import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.interfaces.ResponListener;

public class VipSetGradeDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private String content,confirm;
    private ResponListener<Boolean> responListener;
    private Spanned spanned;

    public VipSetGradeDialogFragment(boolean isWidth, int ori,String content,String confirm) {
        super(isWidth, ori);
        this.content = content;
        this.confirm = confirm;
    }

    public VipSetGradeDialogFragment(boolean isWidth, int ori,Spanned spanned,String confirm) {
        super(isWidth, ori);
        this.spanned = spanned;
        this.confirm = confirm;
    }

    public void setRsp(ResponListener<Boolean> responListener){
        this.responListener = responListener;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_vip_set_grade,null);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_confirm.setBackgroundResource(R.drawable.shape_base_btn_black_20dp);
        if(confirm.equals("确认")){
            tv_confirm.setPadding(ScreenUtil.getPxByDp(45),ScreenUtil.getPxByDp(10),ScreenUtil.getPxByDp(45),ScreenUtil.getPxByDp(10));
        } else {
            tv_confirm.setPadding(ScreenUtil.getPxByDp(25),ScreenUtil.getPxByDp(10),ScreenUtil.getPxByDp(25),ScreenUtil.getPxByDp(10));
        }
        if (!TextUtils.isEmpty(content)){
            tv_content.setText(content);
        }
        if (spanned != null){
            tv_content.setText(spanned);
        }

        tv_confirm.setText(confirm);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                responListener.Rsp(true);
                dismiss();
                break;
        }

    }
}
