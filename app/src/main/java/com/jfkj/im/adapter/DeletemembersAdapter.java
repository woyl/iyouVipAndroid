package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeletemembersAdapter  extends RecyclerView.Adapter<SelectfriendAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SortModel> mData;
    private Context mContext;
    public DeletemembersAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setmData(List<SortModel> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public SelectfriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_deletemembers, parent, false);
        SelectfriendAdapter.ViewHolder viewHolder = new SelectfriendAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SelectfriendAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }
        holder.tvName.setText(this.mData.get(position).getName());
        holder.tv_level.setText(mData.get(position).getVipLevel()+"");
        holder.select_iv.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(mData.get(position).getHead_url()).into(holder.default_head_iv);
        if (mData.get(position).isIsselect()) {
            holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_sel);
        } else {
            holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_unsel);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).isIsselect()) {
                    mData.get(position).setIsselect(false);
                } else {
                    mData.get(position).setIsselect(true);
                }
                if (mData.get(position).isIsselect()) {
                    holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_sel);
                } else {
                    holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_unsel);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName,tv_level;
        ImageView select_iv;
        CircleImageView default_head_iv;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tag);
            tvName = itemView.findViewById(R.id.name);
            select_iv = itemView.findViewById(R.id.select_iv);
            tv_level = itemView.findViewById(R.id.tv_level);
            default_head_iv = itemView.findViewById(R.id.default_head_iv);
        }
    }

    /**
     * 提供给Activity刷新数据
     *
     * @param list
     */
    public void updateList(List<SortModel> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
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
