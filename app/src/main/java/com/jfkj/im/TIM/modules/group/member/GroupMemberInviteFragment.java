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


public class GroupMemberInviteFragment extends BaseTimFragment {

    private GroupMemberInviteLayout mInviteLayout;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInviteLayout = view.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_fragment_invite_members, container, false);
        return view;
    }

    private void init() {
        mInviteLayout.setDataSource((GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO));
        mInviteLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
    }
}
