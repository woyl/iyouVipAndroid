package com.jfkj.im.TIM.modules.group.info;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.jfkj.im.R;
import com.jfkj.im.TIM.base.BaseTimFragment;
import com.jfkj.im.TIM.modules.group.member.GroupMemberDeleteFragment;
import com.jfkj.im.TIM.modules.group.member.GroupMemberInviteFragment;
import com.jfkj.im.TIM.modules.group.member.GroupMemberManagerFragment;
import com.jfkj.im.TIM.modules.group.member.IGroupMemberRouter;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.entity.AddDeteleMemberEvent;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.event.SeventyEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class GroupInfoFragment extends BaseTimFragment {

    private GroupInfoLayout mGroupInfoLayout;
     View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.group_info_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mGroupInfoLayout = view.findViewById(R.id.group_info_layout);
        if (getFragmentManager() != null) {
            mGroupInfoLayout.setFragment(getFragmentManager());
        }
        mGroupInfoLayout.setGroupId(getArguments().getString(TUIKitConstants.Group.GROUP_ID));
        mGroupInfoLayout.setRouter(new IGroupMemberRouter() {
            @Override
            public void forwardListMember(GroupInfo info) {
                GroupMemberManagerFragment fragment = new GroupMemberManagerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardAddMember(GroupInfo info) {
                GroupMemberInviteFragment fragment = new GroupMemberInviteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardDeleteMember(GroupInfo info) {
                GroupMemberDeleteFragment fragment = new GroupMemberDeleteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void upDate(AddDeteleMemberEvent event){
        if (event.isSend()){
            BackgroundTasks.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGroupInfoLayout.setGroupId(getArguments().getString(TUIKitConstants.Group.GROUP_ID));
                }
            },1000);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upSeventy(SeventyEvent event){
        if (event.isShow()){
            VipSetGradeDialogFragment dialogFragment =
                    new VipSetGradeDialogFragment(true, Gravity.CENTER, "VIP等级达到70级以后，才可解锁隐藏俱乐部成员状态功能。","确认");
            dialogFragment.setRsp(new ResponListener<Boolean>() {
                @Override
                public void Rsp(Boolean s) {
                    if (s) {

                    }
                }
            });
            if (getFragmentManager() != null) {
                dialogFragment.show(getFragmentManager(), "");
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
