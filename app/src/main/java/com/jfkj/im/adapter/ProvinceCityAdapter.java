package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jfkj.im.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProvinceCityAdapter extends RecyclerView.Adapter<ProvinceCityAdapter.ProvincecityViewHolder> {

    List<String> list;
    Context context;
    LayoutInflater layoutInflater;

    public ProvinceCityAdapter(Context context) {
        this.context = context;
        layoutInflater= LayoutInflater.from(context);
    }
     public void setList( List<String> list){
        this.list=list;
        notifyDataSetChanged();
     }
    @NonNull
    @Override
    public ProvincecityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProvincecityViewHolder(layoutInflater.inflate(R.layout.item_select_province_city,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProvincecityViewHolder provincecityViewHolder, int i) {
        provincecityViewHolder.textView.setText(list.get(i));
    }
    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    class ProvincecityViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ProvincecityViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
        }
    }
}
