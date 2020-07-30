package com.jfkj.im.TIM.redpack.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.ClubmessageBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.UpGradeSuperGroup.UpGradeSuperGroupPresenter;
import com.jfkj.im.mvp.UpGradeSuperGroup.UpGradeSuperGroupView;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class UpGradeSuperGroupActivity extends BaseActivity<UpGradeSuperGroupPresenter> implements UpGradeSuperGroupView {
    @BindView(R.id.tv_upgrade)
    TextView tv_upgrade;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    UpGradeSuperGroupPresenter upGradeSuperGroupPresenter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,true);
        setStaturBar(constraint_head);
        upGradeSuperGroupPresenter=new UpGradeSuperGroupPresenter(this);
          intent = getIntent();
        upGradeSuperGroupPresenter.loadGroupInfo(intent.getStringExtra(Utils.GROUPID));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_up_grade_super_group;
    }

    @Override
    public UpGradeSuperGroupPresenter createPresenter() {
        return upGradeSuperGroupPresenter;
    }

    @OnClick({R.id.iv_title_back,R.id.tv_upgrade})
    void Onclick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_upgrade:
                if (Utils.netWork()) {
                    if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 100) {
                        upGradeSuperGroupPresenter.UpGradeSuperGroup(intent.getStringExtra(Utils.GROUPID),"1");
                    } else {
                        VipSetGradeDialogFragment dialogFragment
                                = new VipSetGradeDialogFragment(true, Gravity.CENTER, "VIP等级达到100级以后，才可解锁超级俱乐部功能。","确认");
                        dialogFragment.setRsp(new ResponListener<Boolean>() {
                            @Override
                            public void Rsp(Boolean s) {
                                if (s) {

                                }
                            }
                        });
                        dialogFragment.show(getSupportFragmentManager(), "");

                    }
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
        }
    }

    @Override
    public void UpGradeSuperGroupSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject=new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if(jsonObject.getString("code").equals("200")){
                tv_upgrade.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGroupInfoSuccess(ResponseBody userDetai) {
        try {
            String string = userDetai.string();
            JSONObject jsonObject=new JSONObject(string);
            if(jsonObject.getString("code").equals("200")){
                ClubmessageBean clubmessageBean = JSON.parseObject(string, ClubmessageBean.class);
                if(clubmessageBean.getData().getIsSuper()==1){
                    tv_upgrade.setVisibility(View.GONE);
                }else {
                    tv_upgrade.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
