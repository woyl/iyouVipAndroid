package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.group.GroupInformationActivity;
import com.jfkj.im.utils.Utils;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.RichestHolder> {
    List<GroupBean.DataBean.ArrayBean> list;
    Activity context;
    LayoutInflater layoutInflater;

    String conStr;

    int type = -1;

    public GroupAdapter(Activity context,String conStr,int type) {
        this.context = context;

        this.conStr = conStr;

        this.type = type;

        layoutInflater = LayoutInflater.from(context);
    }


    public GroupAdapter(Activity context,String conStr) {
        this.context = context;

        this.conStr = conStr;

        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<GroupBean.DataBean.ArrayBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RichestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(conStr.equals("SearchActivity")){
            view =  layoutInflater.inflate(R.layout.item_group_two, parent, false);
        }else{
            view =  layoutInflater.inflate(R.layout.item_group, parent, false);
        }
        return new RichestHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RichestHolder holder, int position) {
        GroupBean.DataBean.ArrayBean arrayBean = list.get(position);

        if(type == -1){
            holder.tv_sort.setText(position +4+ "");
        }else{
            holder.tv_sort.setVisibility(View.GONE);
        }
        Glide.with(context).load(arrayBean.getGroupHead()).into(holder.head_circleimageview);
        holder.tv_group_name.setText(arrayBean.getGroupName());
        holder.tv_total_people.setText("×" + arrayBean.getRedPacketNum() + "/人");
        holder.tv_total.setText(arrayBean.getTotalNum() + "");
        if (arrayBean.getIsSuper() == 1){
            holder.img_is_super.setVisibility(View.VISIBLE);
        }else {
            holder.img_is_super.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(App.getAppContext(), AdditiongroupActivity.class);
                Intent intent = new Intent(App.getAppContext(), GroupInformationActivity.class);
//                Intent intent = new Intent(App.getAppContext(), ApplyJoinGroupActivity.class);
                intent.putExtra(Utils.GROUPID, arrayBean.getGroupId()+"");
                intent.putExtra("type",false);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class RichestHolder extends RecyclerView.ViewHolder {
        TextView tv_sort;
        TextView tv_group_name;
        TextView tv_total_people;
        TextView tv_total;
        CircleImageView head_circleimageview;
        ImageView img_is_super;

        public RichestHolder(@NonNull View itemView) {
            super(itemView);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            tv_total_people = itemView.findViewById(R.id.tv_total_people);
            tv_total = itemView.findViewById(R.id.tv_total);
            head_circleimageview = itemView.findViewById(R.id.head_circleimageview);
            img_is_super= itemView.findViewById(R.id.img_is_super);
        }
    }

}
