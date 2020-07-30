package com.jfkj.im.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;

import javax.inject.Inject;

public class PublicComCancelDialog extends BaseDialogFragment implements View.OnClickListener {

    private String rightContent,title;
    private CharSequence content;
    private ResponListener<Boolean> responListener;
    private boolean isCenter;

    public PublicComCancelDialog(boolean isWidth, int ori,String title,CharSequence content,String rightContent,boolean isCenter) {
        super(isWidth, ori);
        this.content = content;
        this.title = title;
        this.rightContent = rightContent;
        this.isCenter = isCenter;
    }
    public void setRsp(ResponListener<Boolean> responListener){
        this.responListener = responListener;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_public_com_cancel,null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_title.setText(title);
        tv_confirm.setText(rightContent);
        if (isCenter){
            tv_content.setGravity(Gravity.CENTER);
        }else {
            tv_content.setGravity(Gravity.LEFT);
        }
        tv_content.setText(content);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                Intent intent=new Intent("recharge");
                App.getAppContext().sendBroadcast(intent);
                if(responListener!=null){
                    responListener.Rsp(true);
                }
                dismiss();
                break;
        }
    }
}
