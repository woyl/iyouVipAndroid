package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.SelectpacketsBean;
import com.jfkj.im.R;

import java.util.List;

public class SelectpacketsAdapter extends RecyclerView.Adapter<SelectpacketsAdapter.Selectpackets> {
    List<SelectpacketsBean> list;

    public void setList(List<SelectpacketsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Selectpackets onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Selectpackets(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selectpackets, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Selectpackets holder, int position) {
        SelectpacketsBean selectpacketsBean = list.get(position);
        holder.textView.setText("×" + selectpacketsBean.getPackets() + "/人");
        if (selectpacketsBean.isFlag()) {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_selectpackets_select);
            holder.textView.setTextColor(Color.parseColor("#FF2B66"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#111111"));
            holder.textView.setTextColor(Color.parseColor("#ffffff"));
        }





        if (list.get(position).getPackets().trim().length() > 0) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_custom.setVisibility(View.GONE);

            if(list.size()-1 == position){
                holder.iv_custom.setVisibility(View.VISIBLE);
            }

        } else {
          //  holder.iv_custom.setVisibility(View.VISIBLE);
            holder.iv.setVisibility(View.GONE);
            holder.textView.setText("自定义数量");
            holder.textView.setTextColor(Color.parseColor("#BBBBBB"));
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Selectpackets extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView iv;
        LinearLayout linearLayout;
        private final ImageView iv_custom;

        public Selectpackets(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
            linearLayout = itemView.findViewById(R.id.ly_bg);
            iv_custom = itemView.findViewById(R.id.iv_custom);
        }
    }
}
