package com.jfkj.im.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;

import com.jfkj.im.R;
import com.jfkj.im.entity.CarryScaleBean;
import com.jfkj.im.utils.ToastUtils;

public class CSDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private CommonDialog.CustomizeDialogListener listener;
    private View mView;
    private boolean mOneButton = false;
    private TextView tv_male,tv_male_line;
    private TextView tv_personal_video,tv_personal_video_line;
    private TextView tv_female,tv_female_line;
    private TextView tv_comment_dynamics,tv_comment_dynamics_line;
    private TextView share_dynamics,share_dynamics_line;
    private TextView tv_baserate,tv_baserate_line;


    public CSDialog(Context context, int theme, CommonDialog.CustomizeDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_cs, null);
    }

    public CSDialog(Context context, CommonDialog.CustomizeDialogListener listener) {
        this(context, R.style.dialogstyle, listener);
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

    }

    private void initView() {
        tv_male = mView.findViewById(R.id.tv_male);
        tv_male_line = mView.findViewById(R.id.tv_male_line);
        tv_personal_video=  mView.findViewById(R.id.tv_personal_video);
        tv_personal_video_line = mView.findViewById(R.id.tv_personal_video_line);
        tv_female = mView.findViewById(R.id.tv_female);
        tv_female_line = mView.findViewById(R.id.tv_female_line);
        tv_comment_dynamics = mView.findViewById(R.id.tv_comment_dynamics);
        tv_comment_dynamics_line = mView.findViewById(R.id.tv_comment_dynamics_line);
        share_dynamics = mView.findViewById(R.id.share_dynamics);
        share_dynamics_line = mView.findViewById(R.id.share_dynamics_line);

        tv_baserate = mView.findViewById(R.id.tv_baserate);
        mView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void setData(CarryScaleBean bean){
        initView();

        tv_baserate.setText(bean.getData().getBaseRate()  + "%"); //基础比例

        if(bean.getData().getShareDynamics() !=0){
            share_dynamics.setText("+"+bean.getData().getShareDynamics() + "%");
            share_dynamics.setVisibility(View.VISIBLE);
        }else{
//            share_dynamics.setText("-");
            share_dynamics_line.setVisibility(View.VISIBLE);
        }

        if(bean.getData().getPersonalVideo() != 0 ){
            tv_personal_video.setText("+" + bean.getData().getPersonalVideo() + "%");
            tv_personal_video.setVisibility(View.VISIBLE);
        }else{
//            tv_personal_video.setText("-");
            tv_personal_video_line.setVisibility(View.VISIBLE);
        }


        if(bean.getData().getFemale() !=0 ){
            tv_female.setText("+" + bean.getData().getFemale() + "%");
            tv_female.setVisibility(View.VISIBLE);
        }else{
//            tv_female.setText("-");
            tv_female_line.setVisibility(View.VISIBLE);
        }


        if (bean.getData().getCommentDynamics() != 0){
            tv_comment_dynamics.setText("+" + bean.getData().getCommentDynamics() + "%");
            tv_comment_dynamics.setVisibility(View.VISIBLE);
        }
        else{
//            tv_comment_dynamics.setText("-");
            tv_comment_dynamics_line.setVisibility(View.VISIBLE);
        }

        if(bean.getData().getMale() != 0  ){
            tv_male.setText("+" + bean.getData().getMale() + "%");
            tv_male.setVisibility(View.VISIBLE);
        }else{
//            tv_male.setText("-");
            tv_male_line.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
