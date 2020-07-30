package com.jfkj.im.ui.party;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.SignInBean;
import com.jfkj.im.R;

import com.jfkj.im.adapter.SignInAdapter;
import com.jfkj.im.event.PartyRedEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.party.SignInPresenter;
import com.jfkj.im.mvp.party.SignInView;
import com.jfkj.im.ui.dialog.CommonCenterDialog;
import com.jfkj.im.ui.dialog.LottieDialog;
import com.jfkj.im.ui.dialog.SuccessDialog;

import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;

public class SignInActivity extends BaseActivity<SignInPresenter> implements SignInView, SwipeRecyclerView.LoadMoreListener, SignInAdapter.OnItemClick {

    @BindView(R.id.recycler)
    SwipeRecyclerView  recyclerView;

    @BindView(R.id.iv_title_back)
    AppCompatImageView imageView;


    private int pageNo = 1;

    private int pageSize = 10;

    private SignInPresenter  signInPresenter;
    private String partyId;

    private SignInBean signInBean = new SignInBean();
    private LinearLayoutManager linearLayoutManager;
    private SignInAdapter adapter;
    private CommonCenterDialog tipsDialog;

    private int position;
    private LottieDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        signInPresenter = new SignInPresenter(this);

        Intent intent = getIntent();
        partyId = intent.getStringExtra("partyId");

       // Glide.with(this).load(R.drawable.nav_icon_back_black).into(imageView);


        adapter = new SignInAdapter(this,signInBean ,this);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.useDefaultLoadMore();
        recyclerView.loadMoreFinish(false, true);
        recyclerView.setLoadMoreListener(this);
        recyclerView.setAutoLoadMore(false);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNo = 1;
        this.signInBean = null;
        signInBean = new SignInBean();
        signInPresenter.inviteAllUsers(pageNo,pageSize, partyId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    public SignInPresenter createPresenter() {
        return signInPresenter;
    }


    @Override
    public void getSuccess(SignInBean signInBean) {



        if(signInBean.getData().getUserList().size() > 0){

            if(this.signInBean.getData()==null){
                this.signInBean  = signInBean;
            }else{
                this.signInBean.getData().getUserList().addAll(signInBean.getData().getUserList());
            }

            adapter.setDate(this.signInBean);
            recyclerView.loadMoreFinish(false, true);
        }else{
            recyclerView.loadMoreFinish(false, false);
        }
    }

    @Override
    public void getError() {
    }

    /**
     * 确认到场
     */
    @Override
    public void confirmArrivalSuccess(BaseBean baseBean) {
        SuccessDialog successDialog = new SuccessDialog(this, R.style.dialogstyle);
        successDialog.show();
        // 刷新数据
        signInBean.getData().getUserList().get(position).setIstate(1);
        adapter.setItem(signInBean,position);

        EventBus.getDefault().post(new PartyRedEvent(true));

    }

    @Override
    public void showLoading() {
        loadingDialog = new LottieDialog(this);
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        signInPresenter.inviteAllUsers(pageNo,pageSize, partyId);
    }

    @Override
    public void confirmPresence(String userId, int type,int position) {
        if(type == 1){
            this.position = position;
            //温馨提示
            showNoMoneyDialog(userId);

        }else{
            //投诉
//            JumpUtil.overlay(this,PartyComplaintsActivity.class);

            EventBus.getDefault().post(new PartyRedEvent(true));

            Intent intent = new Intent(this, PartyComplaintsActivity.class);
            intent.putExtra("partyId",partyId);
            intent.putExtra("userId",userId);

            startActivity(intent);

        }
    }


    private void showNoMoneyDialog(String  userId) {
        tipsDialog = new CommonCenterDialog(this, R.style.dialogstyle, new CommonCenterDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_confirm) {
                    tipsDialog.dismiss();
                    signInPresenter.confirmArrival(userId,partyId);
                    EventBus.getDefault().post(new PartyRedEvent(true));

                } else if (view.getId() == R.id.tv_cancel) {
                    tipsDialog.dismiss();

                }
            }
        });
        tipsDialog.setTitleText("温馨提示").setContentText("是否确认应邀用户到场？").setConfirmBtnText("确认").setCancelBtnText("取消").show();

        tipsDialog.show();
    }


}
