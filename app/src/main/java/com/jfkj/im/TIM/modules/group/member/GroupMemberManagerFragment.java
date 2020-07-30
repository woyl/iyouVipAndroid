package com.jfkj.im.TIM.modules.group.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.jfkj.im.R;
import com.jfkj.im.TIM.base.BaseTimFragment;
import com.jfkj.im.TIM.modules.group.info.GroupInfo;
import com.jfkj.im.TIM.utils.TUIKitConstants;

/**
 * 群成员管理
 */
public class GroupMemberManagerFragment extends BaseTimFragment {

    private GroupMemberManagerLayout mMemberLayout;
    private GroupInfo mGroupInfo;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_fragment_members, container, false);
        mMemberLayout = view.findViewById(R.id.group_member_grid_layout);
        init();
        return view;
    }

    private void init() {
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        mMemberLayout.setDataSource(mGroupInfo);
        mMemberLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mMemberLayout.setRouter(new IGroupMemberRouter() {

            @Override
            public void forwardListMember(GroupInfo info) {

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
}
