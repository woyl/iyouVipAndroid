package com.jfkj.im.TIM.redpack.group;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.modules.group.info.GroupInfoPresenter;
import com.jfkj.im.TIM.redpack.chatroom.CharacterActivity;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.DecimalFormatUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupInformationActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.cir_head)
    ImageView cir_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_notice)
    TextView tv_notice;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.recycler_m)
    RecyclerView recycler_m;
    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.tv_vip_level)
    TextView tv_vip_level;
    @BindView(R.id.tv_m_1)
    TextView tv_m_1;

    /**隐藏成员*/
    @BindView(R.id.fl_hide_member)
    FrameLayout fl_hide_member;

    //群组信息
    private GroupInfoBean groupInfoBean;
    private CommonRecyclerAdapter<GroupMemberInfoBean> adapter;
    private boolean type;
    private String groupId;
    private List<GroupMemberInfoBean> groupMemberInfoBeans = new ArrayList<>();

    private GroupInfoPresenter mPresenter;
    private boolean isGroupOwer;
    private Context context;
    private MyHandler myHandler;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniView();
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageAtTime(1,200);
    }

    public static class MyHandler extends Handler {
        private WeakReference<GroupInformationActivity> weakReference;
        private MyHandler(GroupInformationActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            GroupInformationActivity groupInformationActivity = weakReference.get();
            if (msg.what == 1) {
                groupInformationActivity.setData();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setData() {
//        progressDialog.show();
        mPresenter.loadGroupInfo(groupId, this, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                getGroupMember((GroupInfoBean) data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
//                progressDialog.dismiss();
            }
        });
    }

    private void getGroupMember(GroupInfoBean groupInfoBean1) {
        if(groupInfoBean == null){
            groupInfoBean=groupInfoBean1;
        }
        GroupInfoSetUtils.getGroupMembers(groupId, "", new TIMValueCallBack<List<GroupMemberInfoBean>>() {
            @Override
            public void onError(int code, String desc) {
//                progressDialog.dismiss();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(List<GroupMemberInfoBean> groupMemberInfoBeans) {
//                progressDialog.dismiss();
                setLayout(groupInfoBean1,groupMemberInfoBeans);
            }
        });

        if (TextUtils.isEmpty(groupInfoBean1.getHide()) || groupInfoBean1.getHide().equals("0")){
            tv_m_1.setVisibility(View.VISIBLE);
            recycler_m.setVisibility(View.VISIBLE);
        } else {
            fl_hide_member.setVisibility(View.VISIBLE);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void setLayout(GroupInfoBean groupInfoBean,List<GroupMemberInfoBean> groupMemberInfoBeans) {
        tv_title.setText(groupInfoBean.getGroupName());
        tv_title.setTextColor(getColor(R.color.white));
        Glide.with(this).load(groupInfoBean.getGroupOwnerHead()).into(cir_head);
        tv_name.setText(groupInfoBean.getGroupOwnerName());
        tv_city.setText(groupInfoBean.getCity());
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_diamond_yellow);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_money.setCompoundDrawables(drawable, null, null, null);
        tv_money.setText("x" + groupInfoBean.getRedPacketNum() + "/人");
        tv_notice.setText(groupInfoBean.getGroupNotice());
        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_diamond_yellow);
        drawable1.setBounds(1, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_total.setCompoundDrawables(null, null, drawable1, null);
        String totals = "";
        if (groupInfoBean.getTotalNum() > 9999) {
            double a = (double) groupInfoBean.getTotalNum()/10000;
            String format = DecimalFormatUtils.doubleNoRounding(a);
            totals = format + "w";
        } else {
            totals = String.valueOf(groupInfoBean.getTotalNum());
        }
        tv_total.setText(totals);
        tv_vip_level.setText("VIP"+groupInfoBean.getVipLevel());
        if(groupInfoBean.getIsOwner()==1){
            type=true;
            tv_add.setText("邀请好友加入");

        }
        if(groupInfoBean.getIsJoin()==1){
            tv_add.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(groupInfoBean.getHide()) || groupInfoBean.getHide().equals("0")){
            tv_m_1.setVisibility(View.VISIBLE);
            recycler_m.setVisibility(View.VISIBLE);
        } else {
            fl_hide_member.setVisibility(View.VISIBLE);
        }

        cir_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("userId", groupInfoBean.getGroupOwnerId());
                startActivity(intent);
            }
        });

        if (groupInfoBean.getIsOwner() == 1 && groupInfoBean.getGroupOwnerId().equals(TIMManager.getInstance().getLoginUser())){
            tv_add.setText("邀请好友加入");
            tv_add.setVisibility(View.VISIBLE);
        }else {
            if (type){
                tv_add.setVisibility(View.GONE);
            } else {
                for (GroupMemberInfoBean memberInfoBean : groupMemberInfoBeans){
                    if (!TextUtils.isEmpty(memberInfoBean.getUserId())){
                        if (!memberInfoBean.getUserId().equals(TIMManager.getInstance().getLoginUser())){
                            tv_add.setText("申请加入");
                            tv_add.setVisibility(View.VISIBLE);
                        }else if (memberInfoBean.getUserId().equals(TIMManager.getInstance().getLoginUser())){
                            tv_add.setVisibility(View.GONE);
                            break;
                        }
                    }
                }
            }
        }



        adapter = new CommonRecyclerAdapter<GroupMemberInfoBean>(this, groupMemberInfoBeans, R.layout.group_member_adpater) {
            @Override
            public void convert(CommonRecyclerHolder holder, GroupMemberInfoBean groupMemberInfoBean, int position) {
                ImageView group_member_icon = holder.getView(R.id.group_member_icon);
                TextView group_member_name = holder.getView(R.id.group_member_name);
                Glide.with(mContext).load(groupMemberInfoBean.getHead()).into(group_member_icon);
                group_member_name.setText(groupMemberInfoBean.getNickName());
                TextView tv_group_z = holder.getView(R.id.tv_group_z);
                if (groupMemberInfoBean.getIsOwner() == 1){
                    tv_group_z.setVisibility(View.VISIBLE);
                }else {
                    tv_group_z.setVisibility(View.GONE);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        intent.putExtra("userId", groupMemberInfoBean.getUserId());
                        startActivity(intent);
                    }
                });
            }
        };

        recycler_m.setAdapter(adapter);
    }

    private void iniView() {
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        recycler_m.setLayoutManager(new GridLayoutManager(this, 5));
        type = getIntent().getBooleanExtra("type",false);
        groupId = getIntent().getStringExtra(Utils.GROUPID);
        mPresenter = new GroupInfoPresenter();
        context = this;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void receiData(GroupInforEven groupInforEven) {
        groupInfoBean = groupInforEven.getGroupInfoBean();
        groupMemberInfoBeans.addAll(groupInforEven.getGroupMemberInfoBeans());
        isGroupOwer = groupInforEven.isType();
//        groupId = groupInforEven.getGroupId();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_information;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.iv_title_back, R.id.tv_add})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_add:
                if (type) {
//                    EventBus.getDefault().postSticky(groupInfoBean);
                    startActivity(new Intent(this, GroupAddOrDelMeberActivity.class)
                            .putExtra("add", true)
                            .putExtra("groupId", groupId));
                } else {
                    startActivity(new Intent(this, ApplyJoinGroupActivity.class)
                    .putExtra("groupInfo",groupInfoBean));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
