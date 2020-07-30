package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.CirclefriendBean;

import java.util.List;

public class CirclefriendAdapter extends RecyclerView.Adapter<CirclefriendAdapter.CirclefriendViewHolder> {
    List<CirclefriendBean.DataBean> list;
    Context context;
    LayoutInflater layoutInflater;

    public CirclefriendAdapter(Context context){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public CirclefriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CirclefriendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CirclefriendViewHolder extends RecyclerView.ViewHolder {

        public CirclefriendViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
