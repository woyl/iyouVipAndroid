package com.jfkj.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.MineCharacterttestBean;
import com.jfkj.im.R;

import java.util.List;

public class MineCharacterttestAdapter extends RecyclerView.Adapter<MineCharacterttestAdapter.MineCharacterttestaolder> {
    List<MineCharacterttestBean.DataBean.ArrayBean> list;
    Context context;
    LayoutInflater layoutInflater;

    public MineCharacterttestAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<MineCharacterttestBean.DataBean.ArrayBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MineCharacterttestaolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_minecharacterttest, parent, false);
        return new MineCharacterttestaolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MineCharacterttestaolder holder, int position) {
        MineCharacterttestBean.DataBean.ArrayBean arrayBean = list.get(position);
        holder.tv_time.setText(arrayBean.getAddDate());
        if(arrayBean.getGameState()==1){
            holder.tv_state.setBackgroundResource(R.drawable.shape_character_img);
            holder.tv_state.setText("进行中");
            holder.tv_state.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.tv_state.setBackgroundResource(R.drawable.shape_character_ed);
            holder.tv_state.setTextColor(Color.parseColor("#666666"));
            holder.tv_state.setText("已结束");
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class MineCharacterttestaolder extends RecyclerView.ViewHolder {
        TextView tv_state;
        AppCompatTextView tv_time;

        public MineCharacterttestaolder(@NonNull View itemView) {
            super(itemView);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
