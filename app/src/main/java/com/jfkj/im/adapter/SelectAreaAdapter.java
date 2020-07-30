package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;

import java.util.List;

public class SelectAreaAdapter extends RecyclerView.Adapter<SelectAreaAdapter.SelectAreaViewHolder> {

    private LayoutInflater mInflater;
    private List<SortModel> mData;
    private Context mContext;

    public SelectAreaAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    public void setmData(List<SortModel> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SelectAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selectarea, parent, false);
        return new SelectAreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAreaViewHolder holder, int position) {
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }
        SortModel sortModel = mData.get(position);
        holder.name.setText(sortModel.getName());
        holder.number.setText(sortModel.getUserid()+"");
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class SelectAreaViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        TextView tvTag;

        public SelectAreaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            tvTag = itemView.findViewById(R.id.tag);
        }
    }

    //    public SelectAreaAdapter(@Nullable List<SortModel> data) {
//        super(R.layout.item_selectarea, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, SortModel item) {
//        TextView name = helper.getView(R.id.name);
//        TextView number = helper.getView(R.id.number);
//        name.setText(item.getName());
//        number.setText(item.getUserid()+"");
//    }
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
