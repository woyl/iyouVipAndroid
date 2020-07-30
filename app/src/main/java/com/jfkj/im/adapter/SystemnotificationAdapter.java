package com.jfkj.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.jfkj.im.Bean.Systemnotificationbean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.DateTimeUtil;
import com.jfkj.im.utils.GlideUtils;
import com.jfkj.im.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SystemnotificationAdapter extends RecyclerView.Adapter<SystemnotificationAdapter.SystemnotificationHolder> {
    List<Systemnotificationbean> linkedList;
    Context context;
    LayoutInflater layoutInflater;
    public SystemnotificationAdapter(Context context){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    public void setLinkedList(List<Systemnotificationbean> linkedList) {
        this.linkedList = linkedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SystemnotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_systemnotification, parent, false);
        return new SystemnotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemnotificationHolder holder, int position) {
        Systemnotificationbean.BodyBean body = linkedList.get(position).getBody();
        if (body != null && !TextUtils.isEmpty((body.getAvatarImage()))){
            holder.tv_name.setText(body.getTitle());
            holder.tv_message.setText(body.getSubTitle());
            switch (linkedList.get(position).getType()) {
                case "10":
                    Glide.with(context).load(R.drawable.system_notification_iv).into(holder.default_head_iv);
                    break;
                case "7":
                case "8":
                case "9":
                default:
                    Glide.with(context).load(body.getAvatarImage()).placeholder(R.mipmap.system_iv).into(holder.default_head_iv);
                    break;
            }
            holder.tv_name.setText(body.getTitle());
            holder.tv_time.setText(DateTimeUtil.getTimeFormatText(new Date(Long.parseLong(body.getTime()))));
        } else {
            if (body != null &&!TextUtils.isEmpty(body.getSubTitle())){
                if (body.getSubTitle().contains("VIP")){
                    Glide.with(context).load(R.mipmap.system_iv).placeholder(R.mipmap.system_iv).into(holder.default_head_iv);
                    holder.tv_name.setText(body.getTitle());
                    holder.tv_message.setText(body.getSubTitle());
                    holder.tv_time.setText(DateTimeUtil.getTimeFormatText(new Date(Long.parseLong(body.getTime()))));
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return linkedList!=null?linkedList.size():0;
    }

    class SystemnotificationHolder extends RecyclerView.ViewHolder {
        CircleImageView default_head_iv;
        TextView tv_name;
        TextView tv_time;
        TextView tv_message;

        public SystemnotificationHolder(@NonNull View itemView) {
            super(itemView);
            default_head_iv = itemView.findViewById(R.id.default_head_iv);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_message = itemView.findViewById(R.id.tv_message);

        }
    }
}
