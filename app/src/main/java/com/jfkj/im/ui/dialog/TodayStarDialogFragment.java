package com.jfkj.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.view.RoundImageView;

import java.util.List;
import java.util.Objects;

public class TodayStarDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private List<TodayStarBean> todayStarBeans;

    public TodayStarDialogFragment(boolean isWidth, int ori, List<TodayStarBean> t) {
        super(isWidth, ori);
        this.todayStarBeans = t;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_f_today_star,null);
        RoundImageView img_head = view.findViewById(R.id.img_head);
        ImageView img_cancel = view.findViewById(R.id.img_cancel);
        FrameLayout ll_content = view.findViewById(R.id.ll_content);
        TextView tv_type = view.findViewById(R.id.tv_type);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_money = view.findViewById(R.id.tv_money);

        ConstraintLayout mlayout = view.findViewById(R.id.cl_layout);



        ll_content.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = ll_content.getLayoutParams();
                layoutParams.height = (int) (ScreenUtil.getScreenHeight() * 0.7);
                layoutParams.width = (int) (ScreenUtil.getScreenWidth() * 0.8);
            }
        });

        if (todayStarBeans.get(0).getGender().equals("1") && UserInfoManger.getGender().equals(todayStarBeans.get(0).getGender())){
            tv_type.setText("今日魅力之星");
            tv_name.setText(todayStarBeans.get(1).getNickName());
            tv_money.setText(todayStarBeans.get(1).getMoney());
            Glide.with(getContext()).load(todayStarBeans.get(1).getHead()).into(img_head);
            mlayout.setBackgroundResource(R.mipmap.today_star_a);
        }else {
            tv_type.setText("今日实力之星");
            tv_name.setText(todayStarBeans.get(0).getNickName());
            tv_money.setText(todayStarBeans.get(0).getMoney());
            Glide.with(getContext()).load(todayStarBeans.get(0).getHead()).into(img_head);
            mlayout.setBackgroundResource(R.mipmap.today_star_b);
        }



        img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (todayStarBeans.get(0).getGender().equals("1") && UserInfoManger.getGender().equals(todayStarBeans.get(0).getGender())){
                    //用户详情就不要跳转了 避免套娃
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", todayStarBeans.get(1).getUserId());
                    JumpUtil.overlay(getActivity(), UserDetailActivity.class, bundle);
                }else {
                    //用户详情就不要跳转了 避免套娃
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", todayStarBeans.get(0).getUserId());
                    JumpUtil.overlay(getActivity(), UserDetailActivity.class, bundle);
                }
                diss();

            }
        });


        img_cancel.setOnClickListener(this);
    }


    public void diss(){
        this.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_cancel:
                dismiss();
                break;
        }
    }
}
