package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.R;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.ui.activity.RedPacketDetailsActivity;
import com.jfkj.im.utils.TimeUtil;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/27
 * </pre>
 */
public class GiftListAdapter extends RecyclerView.Adapter<GiftListAdapter.ViewHolder> {
    private Context mContext;
    private List<MyGiftBean.DataBean> mGiftList = new ArrayList<>();
    private Drawable drawable;


    public GiftListAdapter(Context context, List<MyGiftBean.DataBean> list) {
        this.mContext = context;
        this.mGiftList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gift_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 送出的礼物
        try {
            switch (mGiftList.get(position).getBizType()) {
                case "101"://101,"添加好友赠送礼物"     102,"单聊赠送礼物"        103,"评论动态赠送礼物"
                case "102"://101,"添加好友赠送礼物"     102,"单聊赠送礼物"        103,"评论动态赠送礼物"
                    Glide.with(mContext).load(mGiftList.get(position).getReceiveIdHead()).transform(new CircleCrop()).into(holder.mIvUserHead);
                    holder.mTvName.setText(mGiftList.get(position).getReceiveNickName());
                    try {
                        holder.mTvState.setText(TimeUtil.getInstance().getTimeFormatText(mGiftList.get(position).getAdddate()));
                    } catch (Exception e) {
                        holder.mTvState.setText(mGiftList.get(position).getAdddate());
                    }
                    Glide.with(mContext).load(mGiftList.get(position).getGetPictureUrl()).transform(new CircleCrop()).into(holder.mIvGift);
                    holder.mIvGift.setVisibility(View.VISIBLE);
                    holder.mTvGiftNum.setCompoundDrawables(null, null, null, null);
                    holder.mTvGiftNum.setText("X" + mGiftList.get(position).getGiftSize());
                    holder.mLayoutGift.setVisibility(View.VISIBLE);
                    holder.mTvTime.setVisibility(View.GONE);
                    break;
                case "103"://101,"添加好友赠送礼物"     102,"单聊赠送礼物"        103,"评论动态赠送礼物"
                    holder.mLayoutGift.setVisibility(View.VISIBLE);
                    holder.mTvTime.setVisibility(View.GONE);
                    Glide.with(mContext).load(mGiftList.get(position).getReceiveIdHead()).transform(new CircleCrop()).into(holder.mIvUserHead);
                    holder.mTvName.setText(mGiftList.get(position).getReceiveNickName());
                    drawable = mContext.getResources().getDrawable(R.mipmap.icon_diamond_yellow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.mTvGiftNum.setCompoundDrawables(null, null, drawable, null);
                    holder.mTvGiftNum.setText(mGiftList.get(position).getMoney() + " ");
                    holder.mTvState.setText(TimeUtil.getInstance().getTimeFormatText(mGiftList.get(position).getAdddate()));
                    holder.mIvGift.setVisibility(View.GONE);
                    break;
                case "205":
                case "601":
                    holder.mTvName.setText(mGiftList.get(position).getBizTypeName());
                    holder.mLayoutGift.setVisibility(View.VISIBLE);
                    holder.mTvTime.setVisibility(View.GONE);
                    if (mGiftList.get(position).getBizType().equals("205")) {
                        Glide.with(mContext).load(R.drawable.gift_icon_test).transform(new CircleCrop()).into(holder.mIvUserHead);
                    } else {
                        Glide.with(mContext).load(R.mipmap.gift_icon_send_play).transform(new CircleCrop()).into(holder.mIvUserHead);
                    }
                    drawable = mContext.getResources().getDrawable(R.mipmap.icon_diamond_yellow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.mTvGiftNum.setText(mGiftList.get(position).getMoney() + " ");
                    holder.mTvGiftNum.setCompoundDrawables(null, null, drawable, null);
                    holder.mTvState.setText(TimeUtil.getInstance().getTimeFormatText(mGiftList.get(position).getAdddate()));
                    holder.mIvGift.setVisibility(View.GONE);
                    break;
                case "201":
                case "202":
                case "203":
                case "204":
                case "206":
                case "207":
                    //201,"每日红包  202,"俱乐部红包    203 "冒险游戏    204 广场红包      205 性格测试  206破冰任务 207日常任务
                    holder.mLayoutGift.setVisibility(View.GONE);
                    holder.mTvTime.setVisibility(View.VISIBLE);
                    try {
                        holder.mTvTime.setText(TimeUtil.getInstance().getTimeFormatText(mGiftList.get(position).getAdddate()));
                    } catch (Exception e) {
                        holder.mTvTime.setText(mGiftList.get(position).getAdddate());
                    }
                    if ("203".equals(mGiftList.get(position).getBizType())
                            || "204".equals(mGiftList.get(position).getBizType())
                            || "201".equals(mGiftList.get(position).getBizType())
                            || "202".equals(mGiftList.get(position).getBizType())) {
                        //剩余红包等于0
                        if ("0".equals(mGiftList.get(position).getRestSize())) {
                            //剩余红包数量等于0
                            holder.mTvState.setText("全部被抢光");
                        } else {
                            holder.mTvState.setText("领取" + (Integer.parseInt(mGiftList.get(position).getPacketSize()) - Integer.parseInt(mGiftList.get(position).getRestSize()) + "/" + mGiftList.get(position).getPacketSize()));
                        }

                        if ("203".equals(mGiftList.get(position).getBizType())) {
                            //冒险游戏
                            Glide.with(mContext).load(R.drawable.gift_icon_game).transform(new CircleCrop()).into(holder.mIvUserHead);
                        } else {
                            Glide.with(mContext).load(R.drawable.gift_icon_send_red).transform(new CircleCrop()).into(holder.mIvUserHead);
                        }

                    } else if ("206".equals(mGiftList.get(position).getBizType())) {
                        if ("0".equals(mGiftList.get(position).getRestSize())) {
                            holder.mTvState.setText("已领取");
                        } else {
                            holder.mTvState.setText("未领取");
                        }
                        //破冰任务
                        Glide.with(mContext).load(R.mipmap.gift_icon_send_red).transform(new CircleCrop()).into(holder.mIvUserHead);
                    } else {
                        Glide.with(mContext).load(R.drawable.gift_icon_send_red).transform(new CircleCrop()).into(holder.mIvUserHead);
                    }
                    holder.mTvGiftNum.setCompoundDrawables(null, null, null, null);
                    holder.mTvName.setText(mGiftList.get(position).getBizTypeName());
                    holder.mIvGift.setVisibility(View.GONE);
                    break;
            }

            holder.mTvDescribe.setText("(过期部分已退回)");
            // 订单状态 1 正常  2 已领取 3 已退款
            if (mGiftList.get(position).getOrderState().equals("3")) {
                holder.mTvDescribe.setVisibility(View.VISIBLE);
            } else {
                holder.mTvDescribe.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            OkLogger.e(e.toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGiftList.get(position).getBizType().equals("201")
                        || mGiftList.get(position).getBizType().equals("202")
                        || mGiftList.get(position).getBizType().equals("203")
                        || mGiftList.get(position).getBizType().equals("204")
                        || mGiftList.get(position).getBizType().equals("206")
                        || mGiftList.get(position).getBizType().equals("207")) {
                    Intent intent = new Intent(mContext, RedPacketDetailsActivity.class);
                    intent.putExtra(Utils.GROUPID, mGiftList.get(position).getRedid());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGiftList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user_head)
        ImageView mIvUserHead;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_describe)
        TextView mTvDescribe;
        @BindView(R.id.iv_gift)
        ImageView mIvGift;
        @BindView(R.id.tv_gift_num)
        TextView mTvGiftNum;
        @BindView(R.id.layout_gift)
        LinearLayout mLayoutGift;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.cl_start)
        ConstraintLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
