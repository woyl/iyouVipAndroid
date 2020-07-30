package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.Bean.VipData;
import com.jfkj.im.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/15
 * </pre>
 */
public class VipAdapter extends RecyclerView.Adapter<VipAdapter.ViewHolder> {


    private int level; //VIP等级

    private List<VipData> mVipData = new ArrayList<>();
    private Context mContext;

    public VipAdapter(Context context,List<VipData> list,int level){
        this.mContext = context;
        this.mVipData = list;
        this.level = level;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_level, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableDark());
        holder.mTvVipLevel.setText(mVipData.get(position).getVipLevel());
        holder.mTvVipPermission.setText(mVipData.get(position).getVipPermission());

        holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.slide_bar_hint_color));
        holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.slide_bar_hint_color));



        if(level<3){

        }else if(level >= 3 && level<6){
            if(position <= 0){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        }else if(level >=6 && level < 14){
            if(position <= 1){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        }
        else if(level>=14 && level<15){
            if(position <= 2){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        }else if(level>=15 && level<20){
            if(position <= 3){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }

        }else if(level>=20 && level<50){
            if(position <= 4){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        }else if(level>=50 && level<70){
            if(position <= 5){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        } else if (level >=70 &&  level<100){
            if(position <= 6){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        }else if (level >=100 &&  level<1000){
            if(position <= 7){
                holder.mIvVipIcon.setImageResource(mVipData.get(position).getmDrawableLight());

                holder.mTvVipLevel.setTextColor(mContext.getResources().getColor(R.color.cDBB3B0));
                holder.mTvVipPermission.setTextColor(mContext.getResources().getColor(R.color.c333333));
            }
        }



    }

    @Override
    public int getItemCount() {
        return mVipData == null ? 0 : mVipData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvVipIcon;
        TextView mTvVipLevel;
        TextView mTvVipPermission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvVipIcon = itemView.findViewById(R.id.iv_vip_icon);
            mTvVipLevel = itemView.findViewById(R.id.tv_vip_level);
            mTvVipPermission = itemView.findViewById(R.id.tv_vip_permission);
        }
    }
}
