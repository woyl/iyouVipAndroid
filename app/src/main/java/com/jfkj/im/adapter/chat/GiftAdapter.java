package com.jfkj.im.adapter.chat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.ui.home.AddFriendActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SvgaUtils;
import com.opensource.svgaplayer.SVGAImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {
    static String ids = "93894769281204224,93895587132735488,93895681303248896,93895788715180032,93895903374868480,93896009918578688,120364533444640768,93895470157791232";

    private int clickPosition;
    private Context mContext;
    private List<GiftData.DataBean.ArrayBean> mList = new ArrayList<>();
    private LayoutInflater mInflater;
    private onItemPositionClickListener mPositionClickListener;
    private String userId;
    String name;

    public GiftAdapter(Context context, List<GiftData.DataBean.ArrayBean> list, String userId) {
        this.mContext = context;
        this.mList = list;
        this.userId = userId;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setListener(onItemPositionClickListener listener) {
        this.mPositionClickListener = listener;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiftData.DataBean.ArrayBean data = mList.get(position);
        Glide.with(mContext).load(data.getPictureUrl()).into(holder.mIvGiftIcon);

        if (data.isSelect()) {
            holder.constraint_bg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shap_gift_item_bg));
            holder.tv_send.setVisibility(View.VISIBLE);
            holder.fl_img_bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.c222222));

            if (ids.contains(data.getGiftId())) {
                holder.mIvGiftIcon.setAlpha(0f);
                SvgaUtils svgaUtils = new SvgaUtils(mContext, holder.svgaImageView, new SvgaUtils.OnDismissListener() {
                    @Override
                    public void onDissmiss() {
                        holder.mIvGiftIcon.setAlpha(1f);
                    }
                });
                svgaUtils.initAnimator();
                svgaUtils.startAnimator(data.getGiftId());
            } else {
                Animation mAnimation = AnimationUtils.loadAnimation(App.getAppContext(), R.anim.animation_gift_two);
                holder.mIvGiftIcon.setAnimation(mAnimation);
                mAnimation.start();


//                //简单的放大缩小动画
//                final AnimatorSet animatorSet = new AnimatorSet();
//
//
//                ObjectAnimator animtion1=ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1, 1.5f);
//
//                animtion1.setRepeatMode(ValueAnimator.REVERSE);
//
//                ObjectAnimator animtion2=ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1, 1.5f);
//                animtion2.setRepeatMode(ValueAnimator.REVERSE);
//
//                ObjectAnimator animtion3=ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1.5f, 1);
//
//                animtion3.setRepeatMode(ValueAnimator.REVERSE);
//                ObjectAnimator animtion4=ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1.5f, 1);
//                animtion4.setRepeatMode(ValueAnimator.REVERSE);
//
//
//
////                ObjectAnimator objectAnimator =    ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1, 1.5f);
////                objectAnimator .setDuration(100),
////
////
////                        ObjectAnimator objectAnimator1 =    ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1, 1.5f);
////                objectAnimator1.setDuration(100),
////
////                        ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1.5f, 1)
////                                .setDuration(100),
////                        ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1.5f, 1)
////                                .setDuration(100)
//
//
//                animatorSet.playTogether(
//                    animtion1,animtion2,animtion3,animtion4
//                );
//
//                animatorSet.start();
            }


        } else {
            holder.constraint_bg.setBackground(null);
            holder.tv_send.setVisibility(View.INVISIBLE);
            holder.fl_img_bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
        }


        holder.mTvGiftName.setText(data.getName());
        holder.mTvGiftPrice.setText(data.getPrice() + "");


        /**
         *
         */
        holder.tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                //                bundle.putParcelable("GiftData",data);
                bundle.putDouble("price", data.getPrice());
                bundle.putString("pictureUrl", data.getPictureUrl());
                bundle.putString("giftId", data.getGiftId());
                bundle.putString("name", name);
                //                bundle.putString("giftId",data.getGiftId());
                JumpUtil.overlay(mContext, AddFriendActivity.class, bundle);
                mPositionClickListener.onPositionClick(position);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isSelect()) {
                        mList.get(i).setSelect(false);
                        clickPosition = i;
                    }
                }


//                for (GiftData.DataBean.ArrayBean arrayBean:
//                        mList) {
//                    arrayBean.setSelect(false);
//                }


                data.setSelect(true);

                //刷新当前
                notifyItemChanged(position);
                notifyItemChanged(clickPosition);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView mIvGiftIcon;
        AppCompatTextView mTvGiftName;
        AppCompatTextView mTvGiftPrice;
        ConstraintLayout constraint_bg;
        AppCompatTextView tv_send;
        FrameLayout fl_img_bg;
        private final SVGAImageView svgaImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvGiftIcon = itemView.findViewById(R.id.iv_gift_icon);
            mTvGiftName = itemView.findViewById(R.id.tv_gift_name);
            mTvGiftPrice = itemView.findViewById(R.id.tv_gift_price);
            constraint_bg = itemView.findViewById(R.id.constraint_bg);
            tv_send = itemView.findViewById(R.id.tv_send);
            fl_img_bg = itemView.findViewById(R.id.fl_img_bg);
            svgaImageView = itemView.findViewById(R.id.svg_img);
        }
    }
}
