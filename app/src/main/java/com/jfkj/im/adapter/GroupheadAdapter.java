package com.jfkj.im.adapter;

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
import com.jfkj.im.utils.DecimalFormatUtils;
import com.jfkj.im.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupheadAdapter extends RecyclerView.Adapter<GroupheadAdapter.RichestHolder> {
    List<GroupBean.DataBean.ArrayBean> list;
    Context context;
    LayoutInflater layoutInflater;

    public GroupheadAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<GroupBean.DataBean.ArrayBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RichestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_group_head, parent, false);
        return new RichestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RichestHolder holder, int position) {
        GroupBean.DataBean.ArrayBean arrayBean = list.get(position);
        if(position==0){
            holder.tv_sort.setBackgroundResource(R.drawable.list_icon_one);
        }
        if(position==1){
            holder.tv_sort.setBackgroundResource(R.drawable.list_icon_two);
        }
        if(position==2){
            holder.tv_sort.setBackgroundResource(R.drawable.list_icon_three);
        }
        Glide.with(context).load(arrayBean.getGroupHead()).into(holder.head_circleimageview);
        holder.tv_group_name.setText(arrayBean.getGroupName());
        holder.tv_total_people.setText("×" + arrayBean.getRedPacketNum() + "/人");


        if(arrayBean.getTotalNum() <=9999){
            holder.tv_total.setText(arrayBean.getTotalNum() + "");
        }else{

            double a = (double) arrayBean.getTotalNum()/10000;
            String format = DecimalFormatUtils.doubleNoRounding(a);
            holder.tv_total.setText(format+"W");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(App.getAppContext(), AdditiongroupActivity.class);
//                Intent intent = new Intent(App.getAppContext(), ApplyJoinGroupActivity.class);
                Intent intent = new Intent(App.getAppContext(), GroupInformationActivity.class);
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
        ImageView tv_sort;
        TextView tv_group_name;
        TextView tv_total_people;
        TextView tv_total;
        CircleImageView head_circleimageview;

        public RichestHolder(@NonNull View itemView) {
            super(itemView);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            tv_total_people = itemView.findViewById(R.id.tv_total_people);
            tv_total = itemView.findViewById(R.id.tv_total);
            head_circleimageview = itemView.findViewById(R.id.head_circleimageview);
        }
    }

}
