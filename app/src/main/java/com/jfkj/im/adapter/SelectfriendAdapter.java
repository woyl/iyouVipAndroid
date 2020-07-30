package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: xp
 * @date: 2017/7/19
 */

public class SelectfriendAdapter extends RecyclerView.Adapter<SelectfriendAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SortModel> mData;
    private Context mContext;

    private ResponListener<Boolean> responListener;

    public void setResponListener(ResponListener<Boolean> responListener){
        this.responListener = responListener;
    }

    public SelectfriendAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setmData(List<SortModel> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public SelectfriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_group_member, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final SelectfriendAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (section == 0) return;
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }
        holder.tvName.setText(this.mData.get(position).getName());
        holder.tv_level.setText("VIP"+mData.get(position).getVipLevel());
        holder.select_iv.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(mData.get(position).getHead_url()).into(holder.default_head_iv);



        if (mData.get(position).isIsselect()) {
            holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_sel);
        } else {
            holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_unsel);
        }
        if (mData.get(position).isGroupMember()){
            holder.select_iv.setBackgroundResource(R.mipmap.club_radio_btn_sel);
            holder.itemView.setEnabled(false);
        }else {
            holder.itemView.setEnabled(true);
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
                responListener.Rsp(true);
                notifyItemChanged(position+1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
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
        return  !TextUtils.isEmpty(mData.get(position).getLetters()) ? mData.get(position).getLetters().charAt(0) : 0;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            if (TextUtils.isEmpty(sortStr)){
                break;
            }
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
