package com.jfkj.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.TIMConversationBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.DateTimeUtil;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.widget.EmojiEdittext;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InteractionAdapter extends RecyclerView.Adapter<InteractionAdapter.InteractionHolder> {
    List<TIMConversationBean> timConversationBeanList;
    Context context;
    LayoutInflater layoutInflater;

    public InteractionAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<TIMConversationBean> timConversationBeanList) {
        this.timConversationBeanList = timConversationBeanList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InteractionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new InteractionHolder(layoutInflater.inflate(R.layout.item_interaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InteractionHolder holder, int position) {
        TIMConversation timConversation = timConversationBeanList.get(position).getTimConversation();
        holder.tv_message.setText(timConversation.getLastMsg().getConversation().getLastMsg().toString());

        holder.tv_time.setText(DateTimeUtil.getTimeFormatText(new Date( timConversation.getLastMsg().timestamp())));

        List<String> arrayList = new ArrayList<>();
        arrayList.add(timConversation.getLastMsg().getSender());
        TIMFriendshipManager.getInstance().getUsersProfile(arrayList, true, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                Glide.with(context).load(timUserProfiles.get(0).getFaceUrl()).placeholder(R.drawable.default_head_iv).error(R.drawable.default_head_iv).into(holder.default_head_iv);
                holder.tv_name.setText(timConversation.getLastMsg().getSenderNickname());
            }
        });
        holder.tv_message_number.setText(timConversation.getUnreadMessageNum()+"");
        if (timConversation.getUnreadMessageNum()== 0) {
            holder.tv_message_number.setVisibility(View.GONE);
        } else {
            holder.tv_message_number.setVisibility(View.VISIBLE);
            holder.tv_message_number.setBackgroundResource(R.drawable.shape_nteraction_one);
        }
        holder.tv_message_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setChatName(timConversation.getLastMsg().getSenderNickname());
                chatInfo.setType(TIMConversationType.C2C);

                chatInfo.setChatRoom(false);
                chatInfo.setId(timConversation.getLastMsg().getSender()+"");
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timConversationBeanList != null ? timConversationBeanList.size() : 0;
    }

    class InteractionHolder extends RecyclerView.ViewHolder {
        ImageView default_head_iv;
        TextView tv_name, tv_time, tv_message_number;
        EmojiEdittext tv_message;

        public InteractionHolder(@NonNull View itemView) {
            super(itemView);
            default_head_iv = itemView.findViewById(R.id.iv);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_message_number = itemView.findViewById(R.id.tv_message_number);
        }
    }
}
