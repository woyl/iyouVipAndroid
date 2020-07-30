package com.jfkj.im.TIM.modules.group.info;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.component.picture.imageEngine.impl.GlideEngine;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.group.member.GroupMemberInfo;
import com.jfkj.im.TIM.modules.group.member.IGroupMemberRouter;
import com.jfkj.im.TIM.redpack.group.GroupAddOrDelMeberActivity;
import com.jfkj.im.TIM.redpack.group.GroupMemberInfoBean;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GroupInfoAdapter extends BaseAdapter {

    private static final int ADD_TYPE = -100;
    private static final int DEL_TYPE = -101;
    private static final int OWNER_PRIVATE_MAX_LIMIT = 10;  //讨论组,owner可以添加成员和删除成员，
    private static final int OWNER_PUBLIC_MAX_LIMIT = 11;   //公开群,owner不可以添加成员，但是可以删除成员
    private static final int OWNER_CHATROOM_MAX_LIMIT = 11; //聊天室,owner不可以添加成员，但是可以删除成员
    private static final int NORMAL_PRIVATE_MAX_LIMIT = 11; //讨论组,普通人可以添加成员
    private static final int NORMAL_PUBLIC_MAX_LIMIT = 12;  //公开群,普通人没有权限添加成员和删除成员
    private static final int NORMAL_CHATROOM_MAX_LIMIT = 12; //聊天室,普通人没有权限添加成员和删除成员

    private List<GroupMemberInfo> mGroupMembers = new ArrayList<>();
    private IGroupMemberRouter mTailListener;
    private GroupInfo mGroupInfo;
    private Context context;
    private String groupId;

    public GroupInfoAdapter(Context context) {
        this.context = context;
    }

    public void setManagerCallBack(IGroupMemberRouter listener) {
        mTailListener = listener;
    }

    @Override
    public int getCount() {
        return mGroupMembers.size();
    }

    @Override
    public GroupMemberInfo getItem(int i) {
        return mGroupMembers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        MyViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(TUIKit.getAppContext()).inflate(R.layout.group_member_adpater, viewGroup, false);
            holder = new MyViewHolder();
            holder.memberIcon = view.findViewById(R.id.group_member_icon);
            holder.memberName = view.findViewById(R.id.group_member_name);
            holder.tv_group_z = view.findViewById(R.id.tv_group_z);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }
        final GroupMemberInfo info = getItem(i);
//        if (!TextUtils.isEmpty(info.getAccount())) {
//            holder.memberName.setText(info.getAccount());
//        } else {
//            holder.memberName.setText("");
//        }
        if (info.getIsOwner() == 1){
            holder.tv_group_z.setVisibility(View.VISIBLE);
        }else {
            holder.tv_group_z.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(info.getName())){
            holder.memberName.setText(info.getName());
        }else {
            holder.memberName.setText("");
        }
        view.setOnClickListener(null);
//        holder.memberIcon.setBackground(null);
        if (info.getMemberType() == ADD_TYPE) {
//            holder.memberIcon.setImageResource(R.drawable.add_group_member);
//            holder.memberIcon.setBackgroundResource(R.drawable.bottom_action_border);
            holder.memberIcon.setImageResource(R.mipmap.club_icon_groupadd_gray);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, GroupAddOrDelMeberActivity.class)
                            .putExtra("add", true)
                            .putExtra("groupId", groupId));
//                    if (mTailListener != null) {
//                        mTailListener.forwardAddMember(mGroupInfo);
//                    }
                }
            });
        } else if (info.getMemberType() == DEL_TYPE) {
//            holder.memberIcon.setImageResource(R.drawable.del_group_member);
//            holder.memberIcon.setBackgroundResource(R.drawable.bottom_action_border);
            holder.memberIcon.setImageResource(R.mipmap.club_icon_groupdelete_gray);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, GroupAddOrDelMeberActivity.class)
                            .putExtra("add", false)
                            .putExtra("groupId", groupId));
//                    if (mTailListener != null) {
//                        mTailListener.forwardDeleteMember(mGroupInfo);
//                    }
                }
            });
        }else {
            holder.memberIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setId(info.getAccount());
                    chatInfo.setChatRoom(false);
                    Intent intent = new Intent(context, UserDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", info.getAccount());
                    context.startActivity(intent);
                }
            });
            if (!TextUtils.isEmpty(info.getIconUrl())) {
                GlideEngine.loadImage(holder.memberIcon, info.getIconUrl(), null);
            }
        }
        return view;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setDataSource(GroupInfo info, List<GroupMemberInfoBean> groupMemberInfoBean) {
        mGroupInfo = null;
        mGroupInfo = info;
        mGroupMembers.clear();
        List<GroupMemberInfo> members = info.getMemberDetails();
        if (members != null) {
            int shootMemberCount = 0;
            if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_PRIVATE)) {
                if (info.isOwner()) {
                    shootMemberCount = members.size() > OWNER_PRIVATE_MAX_LIMIT ? OWNER_PRIVATE_MAX_LIMIT : members.size();
                } else {
                    shootMemberCount = members.size() > NORMAL_PRIVATE_MAX_LIMIT ? NORMAL_PRIVATE_MAX_LIMIT : members.size();
                }
            } else if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_PUBLIC)) {
                if (info.isOwner()) {
                    shootMemberCount = members.size() > OWNER_PUBLIC_MAX_LIMIT ? OWNER_PUBLIC_MAX_LIMIT : members.size();
                } else {
                    shootMemberCount = members.size() > NORMAL_PUBLIC_MAX_LIMIT ? NORMAL_PUBLIC_MAX_LIMIT : members.size();
                }
            } else if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_CHAT_ROOM)) {
                if (info.isOwner()) {
                    shootMemberCount = members.size() > OWNER_CHATROOM_MAX_LIMIT ? OWNER_CHATROOM_MAX_LIMIT : members.size();
                } else {
                    shootMemberCount = members.size() > NORMAL_CHATROOM_MAX_LIMIT ? NORMAL_CHATROOM_MAX_LIMIT : members.size();
                }
            }
            for (int i = 0; i < members.size(); i++) {
                for (int j = 0; j < groupMemberInfoBean.size(); j++) {
                    if (TextUtils.equals(members.get(i).getAccount(), groupMemberInfoBean.get(j).getUserId())) {
                        members.get(i).setIconUrl(groupMemberInfoBean.get(j).getHead());
                        members.get(i).setName(groupMemberInfoBean.get(j).getNickName());
                        members.get(i).setAccount(groupMemberInfoBean.get(j).getUserId());
                        members.get(i).setIsOwner(groupMemberInfoBean.get(j).getIsOwner());
                        mGroupMembers.add(members.get(i));
                    }
                }
            }
//                for (int i = 0; i < shootMemberCount; i++) {
//                    mGroupMembers.add(members.get(i));
//                }
//            if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_PRIVATE)) {
//                // 公开群/聊天室 只有APP管理员可以邀请他人入群
//                GroupMemberInfo add = new GroupMemberInfo();
//                add.setMemberType(ADD_TYPE);
//                mGroupMembers.add(add);
//            }
//            GroupMemberInfo self = null;
//            for (int i = 0; i < mGroupMembers.size(); i++) {
//                GroupMemberInfo memberInfo = mGroupMembers.get(i);
//                if (memberInfo.getIsOwner() == 1) {
//                    GroupMemberInfo add = new GroupMemberInfo();
//                    add.setMemberType(ADD_TYPE);
//                    mGroupMembers.add(add);
//                    GroupMemberInfo del = new GroupMemberInfo();
//                    del.setMemberType(DEL_TYPE);
//                    mGroupMembers.add(del);
//                }
//                else {
//                    GroupMemberInfo del = new GroupMemberInfo();
//                    del.setMemberType(DEL_TYPE);
//                    mGroupMembers.add(del);
//                }

//                if (TextUtils.equals(memberInfo.getAccount(), TIMManager.getInstance().getLoginUser())) {
//                    self = memberInfo;
//                    break;
//                }
//            }
//            if (info.isOwner() || (self != null && self.getMemberType() == TIMGroupMemberRoleType.ROLE_TYPE_ADMIN)) {
//                GroupMemberInfo del = new GroupMemberInfo();
//                del.setMemberType(DEL_TYPE);
//                mGroupMembers.add(del);
//            }
            BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(mGroupMembers);
                }
            });

            if (info.isOwner()) {
                GroupMemberInfo add = new GroupMemberInfo();
                add.setMemberType(ADD_TYPE);
                mGroupMembers.add(add);
                GroupMemberInfo del = new GroupMemberInfo();
                del.setMemberType(DEL_TYPE);
                mGroupMembers.add(del);
            }

            notifyDataSetChanged();

        }


    }

    private class MyViewHolder {
        private ImageView memberIcon;
        private TextView memberName;
        private TextView tv_group_z;
    }


}
