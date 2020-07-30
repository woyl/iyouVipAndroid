package com.jfkj.im.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.adapter.chat.GiftAdapter;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.ui.activity.ChatFriendActivity;
import com.jfkj.im.ui.home.AddFriendActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SvgaUtils;
import com.jfkj.im.utils.Utils;
import com.opensource.svgaplayer.SVGAImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatGiftAdapter extends RecyclerView.Adapter<ChatGiftAdapter.ViewHolder> {
    static String ids = "93894769281204224,93895587132735488,93895681303248896,93895788715180032,93895903374868480,93896009918578688,120364533444640768,93895470157791232";

    private Activity mContext;
    private List<GiftData.DataBean.ArrayBean> mList = new ArrayList<>();
    private LayoutInflater mInflater;
    private onItemPositionClickListener mPositionClickListener;
    private String userId;
    private String name;


    int clickPosition;



    public ChatGiftAdapter(Activity context, List<GiftData.DataBean.ArrayBean> list, String userId,String name) {
        this.mContext = context;
        this.mList=list;
        this.userId=userId;
        this.name=name;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setListener(onItemPositionClickListener listener) {
        this.mPositionClickListener = listener;
    }

    @NonNull
    @Override
    public ChatGiftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gift, parent, false);
        return new ChatGiftAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGiftAdapter.ViewHolder holder, int position) {
        GiftData.DataBean.ArrayBean data = mList.get(position);

        Glide.with(mContext).load(data.getPictureUrl()).into(holder.mIvGiftIcon);


        holder.mTvGiftName.setText(data.getName());
        holder.mTvGiftPrice.setText(data.getPrice() + "");

        if (data.isSelect()){
            holder.constraint_bg.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shap_gift_item_bg));
            holder.tv_send.setVisibility(View.VISIBLE);
            holder.fl_img_bg.setBackgroundColor(ContextCompat.getColor(mContext,R.color.c222222));

            if(ids.contains(data.getGiftId())){
                holder.mIvGiftIcon.setAlpha(0f);

                SvgaUtils  svgaUtils = new SvgaUtils(mContext, holder.svgaImageView, new SvgaUtils.OnDismissListener() {
                    @Override
                    public void onDissmiss() {
                        holder.mIvGiftIcon.setAlpha(1f);
                    }
                });
                svgaUtils.initAnimator();
                svgaUtils.startAnimator(data.getGiftId());
            } else {


              Animation  mAnimation = AnimationUtils.loadAnimation(App.getAppContext(), R.anim.animation_gift_two);
                holder.mIvGiftIcon.setAnimation(mAnimation);
                mAnimation.start();


//                ScaleAnimation animation_suofang =new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
//                        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
//
//                animation_suofang.setDuration(900);						//执行时间
//                animation_suofang.setRepeatCount(-1);					//重复执行动画
//                animation_suofang.setRepeatMode(Animation.REVERSE);   	//重复 缩小和放大效果
//                holder.mIvGiftIcon.startAnimation(animation_suofang);				//使用View启动动画



//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1f, 1.5f,1f);
//                objectAnimator.setRepeatCount(5);
//                objectAnimator.setDuration(2000);//动画时间
//                objectAnimator.start();//启动动画
//
//
//                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1.5f, 1f,1f);
//                objectAnimator1.setRepeatCount(5);
//                objectAnimator1.setDuration(2000);//动画时间
//                objectAnimator1.start();//启动动画
//
//
//                //简单的放大缩小动画
//                final AnimatorSet animatorSet = new AnimatorSet();
//
//                animatorSet.playTogether(
//                        ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1, 1.5f)
//                                .setDuration(200),
//                        ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1, 1.5f)
//                                .setDuration(200),
//
//                        ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleX", 1.5f, 1)
//                                .setDuration(200),
//                        ObjectAnimator.ofFloat(holder.mIvGiftIcon, "scaleY", 1.5f, 1)
//                                .setDuration(200)
//
//                );
//
//
//
//                animatorSet.start();
            }



        } else {
            holder.constraint_bg.setBackground(null);
            holder.tv_send.setVisibility(View.INVISIBLE);
            holder.fl_img_bg.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0 ; i < mList.size() ; i++){
                    if(mList.get(i).isSelect()){
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
        holder.tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatFriendActivity.class);
                intent.putExtra("GiftData", data);
                intent.putExtra("userId", userId);
                intent.putExtra("userId", userId);
                intent.putExtra("price",data.getPrice()+"");
                intent.putExtra("getPictureUrl", data.getPictureUrl());
                intent.putExtra(Utils.NICKNAME,name);
                mContext.startActivity(intent );
                mPositionClickListener.onPositionClick(position);
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
