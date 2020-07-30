package com.jfkj.im.adapter.dynamic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.modules.chat.GroupChatManagerKit;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.group.ApplyJoinGroupActivity;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MineGroupTaskAdapter extends RecyclerView.Adapter<MineGroupTaskAdapter.MyViewHolder> {

    private Context context;
    private UserGroupTaskBean beans;

    public MineGroupTaskAdapter(Context context, UserGroupTaskBean userGroupTaskBeans) {
        this.context = context;
        this.beans = userGroupTaskBeans;
    }


    public void setData(UserGroupTaskBean userGroupTaskBean){
        this.beans = userGroupTaskBean;
        OkLogger.e("notifyDataSetChanged");
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grouptask, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.getData().size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(beans.getData().get(position).getGroupName());  //俱乐部名称
        //  Glide.with(context).load(beans.getData().get(position).getGroupHead()).transform(new CircleCrop()).into(holder.img_icon);
        holder.tv_gift.setText(" x" + beans.getData().get(position).getRedPacketNum() + "/人");

        if (beans.getData().get(position).getIsSuper() != 0) {
            Glide.with(context).load(R.mipmap.icon_club_vip).into( holder.img_icon);
            holder.iv_heat.setVisibility(View.VISIBLE);
        } else {
            Glide.with(context).load(R.mipmap.icon_club_ordinary).into( holder.img_icon);
            holder.iv_heat.setVisibility(View.GONE);
        }


        UserGroupTaskBean.DataBean dataBean = beans.getData().get(position);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                        for(TIMGroupBaseInfo baseInfo:timGroupBaseInfos){

                            if(baseInfo.getGroupId().equals(dataBean.getGroupId())){
                                Intent intent = new Intent(context, ChatActivity.class);
                                ChatInfo chatInfo = new ChatInfo();
                                chatInfo.setChatName(dataBean.getGroupName());
                                chatInfo.setType(TIMConversationType.Group);
                                chatInfo.setChatRoom(false);
                                chatInfo.setId(dataBean.getGroupId());
                                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                                context.startActivity(intent);
                                return;
                            }
                        }
                        ToastUtils.showShortToast("群不存在");
                        EventBus.getDefault().post(timGroupBaseInfos);
                        return;
                    }
                });

                //跳转到俱乐部聊天
            }
        });

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_icon;
        private final TextView tv_name;
        private final TextView tv_gift;
//        private final ImageView img_vip_group;
        private final RelativeLayout relativeLayout;
        private final ImageView iv_heat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_club_vip);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_gift = itemView.findViewById(R.id.tv_gift);
//            img_vip_group = itemView.findViewById(R.id.img_vip_group);
            relativeLayout = itemView.findViewById(R.id.rl_layout);
            iv_heat = itemView.findViewById(R.id.iv_heat);
        }

    }
}
