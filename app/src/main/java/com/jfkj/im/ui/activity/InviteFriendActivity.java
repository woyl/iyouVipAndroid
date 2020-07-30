package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.PrivateConstants;
import com.jfkj.im.adapter.InviteFriendAdapter;
import com.jfkj.im.adapter.ShareAdapter;
import com.jfkj.im.entity.InviteBean;
import com.jfkj.im.entity.InvteFriendBean;
import com.jfkj.im.entity.inviteFriendAddressBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.SpecialBaseActivity;
import com.jfkj.im.mvp.inviteFriends.InviteFriendsPresenter;
import com.jfkj.im.mvp.inviteFriends.InviteFriendsView;
import com.jfkj.im.ui.dialog.Sharedialog;
import com.jfkj.im.utils.QrCodeUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.WxShareUtils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * 分享
 */

public class InviteFriendActivity extends BaseActivity<InviteFriendsPresenter> implements View.OnClickListener, InviteFriendsView, ShareAdapter.OnShareItemClick {
    InviteFriendsPresenter inviteFriendsPresenter;
    InviteFriendAdapter inviteFriendAdapter;
    Sharedialog sharedialog;

    @BindView(R.id.iv_title_back)
    ImageView img_back;

    @BindView(R.id.rv_invite_list)
    RecyclerView rv_invite_list;

    @BindView(R.id.tv_add_friend)
    TextView tv_add_friend;

    @BindView(R.id.ll_null)
    LinearLayout ll_null;


    @BindView(R.id.tv_num_money)
    TextView tvMoney;

    @BindView(R.id.tv_num)
    TextView tv_num;

    private String qrString = "";
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fullScreen(mActivity);
//        }

        sharedialog=new Sharedialog(this,this);
        // iv_reward_rule.setOnClickListener(this);
        //invite_friend_btn.setOnClickListener(this);
        rv_invite_list.setLayoutManager(new LinearLayoutManager(this));
        inviteFriendsPresenter=new InviteFriendsPresenter(this);
        inviteFriendsPresenter.InviteFriends();
        inviteFriendsPresenter.inviteFriendAddress();
        inviteFriendsPresenter.inviteFriendTail();


        Resources res = getResources();
        bmp = BitmapFactory.decodeResource(res, R.mipmap.system_iv);

        OkLogger.e(bmp + "");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bmp != null){
            bmp = null;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public InviteFriendsPresenter createPresenter() {
        return inviteFriendsPresenter;
    }



    @OnClick({R.id.tv_next,R.id.iv_title_back,R.id.tv_add_friend})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_next:
                //奖励规则
                Intent intent=new Intent(mActivity,RewardruleActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_title_back:
                finish();
                break;

            case R.id.tv_add_friend:

                sharedialog.setQrString(qrString);
                sharedialog.show();
                break;

        }

    }
//    @Override
//    public void onClick(View v) {
//           switch (v.getId()){
////               case R.id.iv_reward_rule:
////                   Intent intent=new Intent(mActivity,RewardruleActivity.class);
////                   startActivity(intent);
////                   break;
////               case R.id.invite_friend_btn:
////                   sharedialog.show();
////                   break;
//           }
//    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void InviteFriendsSuccess(InvteFriendBean bean) {
        if("200".equals(bean.getCode())){
            if(bean.getData().size()>0){
                ll_null.setVisibility(View.GONE);
                rv_invite_list.setVisibility(View.VISIBLE);
            }else{
                ll_null.setVisibility(View.VISIBLE);
                rv_invite_list.setVisibility(View.GONE);
            }
        }
        inviteFriendAdapter=new InviteFriendAdapter(this,bean);
        rv_invite_list.setAdapter(inviteFriendAdapter);
    }

    @Override
    public void inviteFriendAddressS(inviteFriendAddressBean inviteFriendAddressBean) {
        qrString = inviteFriendAddressBean.getData().getAddress();
        OkLogger.e("+++++++++++" + qrString);
        // sharedialog.setQrString(qrString);
    }

    @Override
    public void inviteFriendTail(InviteBean bean) {
        OkLogger.e(bean.toString());
        tv_num.setText(bean.getData().getInviteSize());
       tvMoney.setText(bean.getData().getDiamonds() + "");
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                // SendMessageToWX.Req.WXSceneSession是分享到好友会话
                // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
                WxShareUtils.shareWeb(this, PrivateConstants.WeChatAppID,qrString,"I you 高分玩家聚集地","超高颜值、高效约会、私密交友",bmp, SendMessageToWX.Req.WXSceneSession);
                break;
            case 1:
                WxShareUtils.shareWeb(this, PrivateConstants.WeChatAppID,qrString,"I you 高分玩家聚集地","超高颜值、高效约会、私密交友",bmp, SendMessageToWX.Req.WXSceneTimeline);
                break;
        }

    }
}
