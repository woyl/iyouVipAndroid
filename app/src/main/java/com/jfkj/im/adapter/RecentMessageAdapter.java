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
import com.google.gson.Gson;
import com.jfkj.im.Bean.GroupBean;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.chat.GroupRecentMessageBean;
import com.jfkj.im.R;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.widget.EmojiEdittext;



import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentMessageAdapter extends RecyclerView.Adapter<RecentMessageAdapter.ClubHolder> {

    List<InteractionBean> list;
    Context context;
    LayoutInflater layoutInflater;
    OnClick onClick;

    public RecentMessageAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setList(List<InteractionBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecentMessageAdapter.ClubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_club, parent, false);
        return new RecentMessageAdapter.ClubHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentMessageAdapter.ClubHolder holder, int position) {
        InteractionBean groupRecentMessageBean = list.get(position);
        holder.tv_name.setText(groupRecentMessageBean.getName());
        holder.tv_message.setText(groupRecentMessageBean.getMessage());
        if (!TextUtils.isEmpty(groupRecentMessageBean.getTime())) {
            holder.tv_time.setText(Utils.getTimeString(Long.parseLong(groupRecentMessageBean.getTime())));
        }
        Glide.with(context).load(groupRecentMessageBean.getHeadurl()).into(holder.default_head_iv);
        if (groupRecentMessageBean.getNumber().equals("0")) {
            holder.tv_message_number.setVisibility(View.GONE);
        } else {
            holder.tv_message_number.setText(groupRecentMessageBean.getNumber());
            holder.tv_message_number.setVisibility(View.VISIBLE);
        }
        if (SPUtils.getInstance(context).getBoolean(Utils.APPID + groupRecentMessageBean.getGroupId() + Utils.NODISTURB, false)) {
            holder.iv_nodisturb.setVisibility(View.VISIBLE);
        } else {
            holder.iv_nodisturb.setVisibility(View.GONE);
        }
        holder.tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InteractionBean interactionBean = list.get(position);
                Intent intent = new Intent(context, ChatActivity.class);
                Gson gson = new Gson();
                intent.putExtra(Utils.RECEIVEID, interactionBean.getGroupId() + "");
                intent.putExtra(Utils.NICKNAME, interactionBean.getName() + "");
                intent.putExtra(Utils.HEADIMGURL, interactionBean.getHeadurl() + "");
                SPUtils.getInstance(context).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, gson.toJson(list));
                intent.putExtra(Utils.ISPRIVATEFRIEND, false);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    class ClubHolder extends RecyclerView.ViewHolder {
        CircleImageView default_head_iv;
        TextView tv_name;
        EmojiEdittext tv_message;
        TextView tv_time;
        TextView tv_message_number;
        ImageView iv_nodisturb;

        public ClubHolder(@NonNull View itemView) {
            super(itemView);
            default_head_iv = itemView.findViewById(R.id.iv);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_nodisturb = itemView.findViewById(R.id.iv_nodisturb);
            tv_message_number = itemView.findViewById(R.id.tv_message_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.OnClickIterm(view, getAdapterPosition());
                }
            });
        }
    }

    public interface OnClick {
        public void OnClickIterm(View view, int position);
    }
}
