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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.R;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.entity.MyObtainGiftBean;
import com.jfkj.im.ui.activity.RedPacketDetailsActivity;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.TimeUtil;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * Description:  获得的礼物适配器
 * @author :   ys
 * @date :         2019/12/27
 * </pre>
 */
public class ObtainGiftListAdapter extends RecyclerView.Adapter<ObtainGiftListAdapter.ViewHolder> {
    private Context mContext;
    private List<MyObtainGiftBean.DataBean> mGiftList = new ArrayList<>();
    private Drawable drawable;


    public ObtainGiftListAdapter(Context context, List<MyObtainGiftBean.DataBean> list) {
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
        MyObtainGiftBean.DataBean bean = mGiftList.get(position);
        // 送出的礼物


        try {


            //1.判断是 红包还是 礼物
            switch (bean.getBizType()) {
                case "101":
                case "102":
                case "103":
                    //101,"添加好友赠送礼物"     102,"单聊赠送礼物"        103,"评论动态赠送礼物"

                    Glide.with(mContext).load(bean.getRsenderidHead()).transform(new CircleCrop()).into(holder.mIvUserHead);
                    holder.mTvName.setText(bean.getRsenderNickName());
                    try {
                        holder.mTvState.setText(TimeUtil.getInstance().getTimeFormatText(bean.getAdddate()));
                    } catch (Exception e) {
                        holder.mTvState.setText(bean.getAdddate());
                    }
                    Glide.with(mContext).load(bean.getGetPictureUrl()).transform(new CircleCrop()).into(holder.mIvGift);

                    if (bean.getBizType().equals("103")) {
                        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_diamond_yellow);
                        if (drawable != null) {
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        }
                        holder.mTvGiftNum.setCompoundDrawables(null, null, drawable, null);
                        holder.mTvGiftNum.setText(bean.getMoney() + "");
                        holder.mIvGift.setVisibility(View.GONE);
                    } else {
                        holder.mTvGiftNum.setText("X" + bean.getGiftSize());
                        holder.mIvGift.setVisibility(View.VISIBLE);
                        holder.mTvGiftNum.setCompoundDrawables(null, null, null, null);
                    }

                    // 订单状态 1 正常  2 已领取 3 已退款
                    if (bean.getOrderState().equals("3")) {
                        holder.mTvDescribe.setText("(过期已退回)");
                    } else {
                        holder.mTvDescribe.setText("");
                    }
                    holder.mLayoutGift.setVisibility(View.VISIBLE);
                    holder.mTvTime.setVisibility(View.GONE);

                    break;
                case "205":
                case "601":
                    holder.mTvName.setText(bean.getBizTypeName());
                    holder.mLayoutGift.setVisibility(View.VISIBLE);
                    holder.mTvDescribe.setVisibility(View.GONE);
                    holder.mTvTime.setVisibility(View.GONE);
                    holder.mIvGift.setVisibility(View.GONE);

                    if (bean.getBizType().equals("205")) {
                        Glide.with(mContext).load(R.drawable.gift_icon_test).transform(new CircleCrop()).into(holder.mIvUserHead);
                    } else {
                        Glide.with(mContext).load(R.mipmap.gift_icon_send_play).transform(new CircleCrop()).into(holder.mIvUserHead);
                    }

                    drawable = mContext.getResources().getDrawable(R.mipmap.icon_coin_yellow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                    holder.mTvGiftNum.setText(bean.getMoney() + " ");
                    holder.mTvGiftNum.setCompoundDrawables(null, null, drawable, null);
                    holder.mTvState.setText(TimeUtil.getInstance().getTimeFormatText(bean.getAdddate()));

                    break;
                case "201":
                case "202":
                case "203":
                case "204":
                case "206":
                case "207":
                    //201,"每日红包  202,"俱乐部红包    203 "冒险游戏    204 广场红包      205 性格测试   206破冰任务 207俱乐部日常任务
                    holder.mLayoutGift.setVisibility(View.GONE);
                    holder.mTvDescribe.setVisibility(View.VISIBLE);
                    holder.mTvTime.setVisibility(View.VISIBLE);
                    holder.mIvGift.setVisibility(View.GONE);

                    try {
                        holder.mTvTime.setText(TimeUtil.getInstance().getTimeFormatText(bean.getAdddate()));
                    } catch (Exception e) {
                        holder.mTvTime.setText(bean.getAdddate());
                    }
                    try {
                        holder.mTvTime.setText(DataFormatUtils.getTodayDateTimes(bean.getAdddate()));
                    } catch (Exception e) {
                        holder.mTvTime.setText(bean.getAdddate());
                    }

                    if ("205".equals(bean.getBizType())) {
                        // 性格测试
                        Glide.with(mContext).load(R.drawable.gift_icon_test).transform(new CircleCrop()).into(holder.mIvUserHead);
                    } else if ("203".equals(bean.getBizType())) {
                        //冒险游戏
                        Glide.with(mContext).load(R.drawable.gift_icon_game).transform(new CircleCrop()).into(holder.mIvUserHead);
                    } else if ("206".equals(bean.getBizType())) {
                        //破冰任务
                        Glide.with(mContext).load(R.mipmap.gift_icon_send_red).transform(new CircleCrop()).into(holder.mIvUserHead);
                    } else {
                        Glide.with(mContext).load(R.drawable.gift_icon_send_red).transform(new CircleCrop()).into(holder.mIvUserHead);
                    }

                    if (bean.getOrderState().equals("3")) {
                        holder.mTvDescribe.setText("(过期部分已退回)");

                    } else {
                        holder.mTvDescribe.setText("");
                    }


                    if (!bean.getOrderState().equals("3")) {
                        holder.mTvState.setText("已领取");
                    }

                    holder.mTvName.setText(bean.getBizTypeName());


                    break;
            }
        } catch (Exception e) {
            OkLogger.e(e.toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getBizType().equals("201")
                        || bean.getBizType().equals("202")
                        || bean.getBizType().equals("203")
                        || bean.getBizType().equals("204")
                        || bean.getBizType().equals("206")
                        || bean.getBizType().equals("207")) {
                    Intent intent = new Intent(mContext, RedPacketDetailsActivity.class);
                    intent.putExtra(Utils.GROUPID, bean.getRedid());
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
