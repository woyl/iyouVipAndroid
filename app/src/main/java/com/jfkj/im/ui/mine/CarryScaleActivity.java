package com.jfkj.im.ui.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.entity.CarryScaleBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.mine.CarryScalePresenter;
import com.jfkj.im.mvp.mine.CarryScaleView;
import com.jfkj.im.ui.activity.InviteFriendActivity;
import com.jfkj.im.ui.dialog.CSDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.discovery.CircleActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CarryScaleActivity extends BaseActivity<CarryScalePresenter> implements CarryScaleView {

    private CarryScalePresenter presenter;

    @BindView(R.id.rl_male)
    RelativeLayout rlMale;

    @BindView(R.id.tv_male)
    TextView tvMale;

    @BindView(R.id.rl_personal_video)
    RelativeLayout rlPersonalVideo;

    @BindView(R.id.tv_personal_video)
    TextView tvPersonalVideo;

    @BindView(R.id.rl_female)
    RelativeLayout rlFemale;

    @BindView(R.id.tv_female)
    TextView tvFemale;

    @BindView(R.id.rl_comment_dynamics)
    RelativeLayout rlCommentDynamics;

    @BindView(R.id.tv_comment_dynamics)
    TextView tvCommentDynamics;

    @BindView(R.id.rl_sharedynamics)
    RelativeLayout rlSharedYnamics;

    @BindView(R.id.tv_sharedynamics)
    TextView tvSharedynamics;


    @BindView(R.id.tv_scale)
    TextView tv_scale;

    @BindView(R.id.tv_title)
    TextView tv_title;
    private CarryScaleBean model;




    @OnClick({R.id.tv_male,R.id.tv_personal_video,R.id.tv_female,R.id.tv_comment_dynamics,R.id.tv_sharedynamics,R.id.iv_title_back,R.id.iv_show_dialog})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_male:              //到分享页面
            case R.id.tv_female:
                startActivity(new Intent(mActivity, InviteFriendActivity.class));
                break;
             case R.id.tv_personal_video:           //上传个人视频

                 JumpUtil.overlay(this, UploadVideoActivity.class);
                break;

            case R.id.tv_comment_dynamics:       //iYou圈
            case R.id.tv_sharedynamics:
                JumpUtil.overlay(this, CircleActivity.class);
                break;
            case R.id.iv_title_back:
                finish();
                break;

            case R.id.iv_show_dialog:
                CSDialog dialog = new CSDialog(this, new CommonDialog.CustomizeDialogListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                if(model!=null){
                    dialog.setData(model);
                }

                dialog.show();

                break;

        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        presenter = new CarryScalePresenter(this);

        tv_title.setText("我的结算比例");

    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.getUserCashRate();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carry_scale;
    }

    @Override
    public CarryScalePresenter createPresenter() {
        return presenter;
    }




    /**
     * 获取结算比例
     */
    @Override
    public void onUserCashRate(CarryScaleBean bean) {
        model = bean;
        if(bean.getCode().equals("200")){
            initView(bean);
        }else{
            ToastUtils.showShortToast(bean.getMessage());
        }
    }

    private void initView(CarryScaleBean bean) {
//        //分享动态图片，视频的比例
//        double shareDynamics = bean.getData().getShareDynamics();
//        //个人视频审核通过比例
//        double personalVideo = bean.getData().getPersonalVideo();
//        //邀请一个女性用户
//        double female = bean.getData().getFemale();
//        //评论别人动态比例
//        double commentDynamics = bean.getData().getCommentDynamics();
//        //邀请男性用户并首次充值比例
//        double male = bean.getData().getMale();

//        if(shareDynamics == 1){
//            //rlSharedYnamics.setVisibility(View.GONE);
//            tvSharedynamics.setBackgroundResource(R.color.color_161823);
//            tvSharedynamics.setText("已完成");
//        }

        if(bean.getData().getIsCompleted() == 1){
        //    rlPersonalVideo.setVisibility(View.GONE);
            tvPersonalVideo.setTextColor(ContextCompat.getColor(mActivity,R.color.cFF2B66));
            tvPersonalVideo.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.shape_sign_in_brg));
            tvPersonalVideo.setText("已完成");
        }

//        if(female == 1){
//        //    rlFemale.setVisibility(View.GONE);
//            tvFemale.setBackgroundResource(R.color.color_161823);
//            tvFemale.setText("已完成");
//        }

//        if(commentDynamics == 1 ){
//          //  rlCommentDynamics.setVisibility(View.GONE);
//            tvCommentDynamics.setBackgroundResource(R.color.color_161823);
//            tvCommentDynamics.setText("已完成");
//        }

     //   if(male ==1 ){
         //   rlMale.setVisibility(View.GONE);
    //        tvMale.setBackgroundResource(R.color.color_161823);
    //        tvMale.setText("已完成");
    //    }

        tv_scale.setText(bean.getData().getRate() + "%");

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
