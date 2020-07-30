package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.CarryScaleBean;
import com.lzy.okgo.utils.OkLogger;

public class UpgradeDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private CommonDialog.CustomizeDialogListener listener;
    private View mView;
    private TextView tv_level;
    private TextView tv_level_message;

    private AddMoneyBean addMoneyBean;


    public UpgradeDialog(Context context, int theme, CommonDialog.CustomizeDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_upgrade, null   );
    }

    public UpgradeDialog(Context context, CommonDialog.CustomizeDialogListener listener,AddMoneyBean bean) {
        this(context, R.style.dialogstyle, listener);
        this.addMoneyBean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);//设置布局属性占满宽度
        //        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.CENTER;//设置dialog位于底部
        window.setAttributes(params);

        initView();

    }

    private void initView() {

//        tv_level = mView.findViewById(R.id.tv_current_level);
//        tv_level_message = mView.findViewById(R.id.tv_level_message);
//
//        findViewById(R.id.iv_dismiss).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                //listener.onClick(v);
//            }
//        });
//
//        int vipLevel = addMoneyBean.getData().getVipLevel();
//        String showDesc = addMoneyBean.getData().getShowDesc();
//
//        OkLogger.e(vipLevel + "");
//        tv_level.setText("当前等级VIP" + vipLevel );
//
//
//        if(TextUtils.isEmpty(showDesc)){
//            tv_level_message.setText("恭喜升级至VIP" + vipLevel);
//        }else{
//            tv_level_message.setText(addMoneyBean.getData().getShowDesc());
//        }
//



    }





    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
