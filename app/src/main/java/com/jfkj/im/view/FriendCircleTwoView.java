package com.jfkj.im.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.utils.media.ScreenUtil;

import java.util.List;

public class FriendCircleTwoView implements View.OnClickListener {


    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;
    private LinearLayout.LayoutParams ll_layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private onItemPositionClickListener mListener;

    public void setOnListener(onItemPositionClickListener listener) {
        this.mListener = listener;
    }

    public void setImageView(RelativeLayout friendsCircleImageLayout, Context mContext, List<String> list) {
        int num = list.size();
        int width = ScreenUtil.getScreenWidth();
        int height = ScreenUtil.getScreenWidth();
        ll_layoutParams.height = height;
        ll_layoutParams.width = width;
        friendsCircleImageLayout.removeAllViews();
        if (num == 5) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_five_picture, null);
            imageView1 = view.findViewById(R.id.img_1);
            imageView2 = view.findViewById(R.id.img_2);
            imageView3 = view.findViewById(R.id.img_3);
            imageView4 = view.findViewById(R.id.img_4);
            imageView5 = view.findViewById(R.id.img_5);
            Glide.with(mContext).load(list.get(0)).into(imageView1);
            Glide.with(mContext).load(list.get(1)).into(imageView2);
            Glide.with(mContext).load(list.get(2)).into(imageView3);
            Glide.with(mContext).load(list.get(3)).into(imageView4);
            Glide.with(mContext).load(list.get(4)).into(imageView5);
            view.setLayoutParams(ll_layoutParams);
            friendsCircleImageLayout.addView(view);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
            imageView4.setOnClickListener(this);
            imageView5.setOnClickListener(this);
        } else if (num == 6) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_six_picture, null);
            imageView1 = view.findViewById(R.id.img_1);
            imageView2 = view.findViewById(R.id.img_2);
            imageView3 = view.findViewById(R.id.img_3);
            imageView4 = view.findViewById(R.id.img_4);
            imageView5 = view.findViewById(R.id.img_5);
            imageView6 = view.findViewById(R.id.img_6);
            Glide.with(mContext).load(list.get(0)).into(imageView1);
            Glide.with(mContext).load(list.get(1)).into(imageView2);
            Glide.with(mContext).load(list.get(2)).into(imageView3);
            Glide.with(mContext).load(list.get(3)).into(imageView4);
            Glide.with(mContext).load(list.get(4)).into(imageView5);
            Glide.with(mContext).load(list.get(5)).into(imageView6);
            view.setLayoutParams(ll_layoutParams);
            friendsCircleImageLayout.addView(view);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
            imageView4.setOnClickListener(this);
            imageView5.setOnClickListener(this);
            imageView6.setOnClickListener(this);
        } else if (num == 7) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_seven_picture, null);
            imageView1 = view.findViewById(R.id.img_1);
            imageView2 = view.findViewById(R.id.img_2);
            imageView3 = view.findViewById(R.id.img_3);
            imageView4 = view.findViewById(R.id.img_4);
            imageView5 = view.findViewById(R.id.img_5);
            imageView6 = view.findViewById(R.id.img_6);
            imageView7 = view.findViewById(R.id.img_7);
            Glide.with(mContext).load(list.get(0)).into(imageView1);
            Glide.with(mContext).load(list.get(1)).into(imageView2);
            Glide.with(mContext).load(list.get(2)).into(imageView3);
            Glide.with(mContext).load(list.get(3)).into(imageView4);
            Glide.with(mContext).load(list.get(4)).into(imageView5);
            Glide.with(mContext).load(list.get(5)).into(imageView6);
            Glide.with(mContext).load(list.get(6)).into(imageView7);
            view.setLayoutParams(ll_layoutParams);
            friendsCircleImageLayout.addView(view);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
            imageView4.setOnClickListener(this);
            imageView5.setOnClickListener(this);
            imageView6.setOnClickListener(this);
            imageView7.setOnClickListener(this);
        } else if (num == 8) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_eight_picture, null);
            imageView1 = view.findViewById(R.id.img_1);
            imageView2 = view.findViewById(R.id.img_2);
            imageView3 = view.findViewById(R.id.img_3);
            imageView4 = view.findViewById(R.id.img_4);
            imageView5 = view.findViewById(R.id.img_5);
            imageView6 = view.findViewById(R.id.img_6);
            imageView7 = view.findViewById(R.id.img_7);
            imageView8 = view.findViewById(R.id.img_8);
            Glide.with(mContext).load(list.get(0)).into(imageView1);
            Glide.with(mContext).load(list.get(1)).into(imageView2);
            Glide.with(mContext).load(list.get(2)).into(imageView3);
            Glide.with(mContext).load(list.get(3)).into(imageView4);
            Glide.with(mContext).load(list.get(4)).into(imageView5);
            Glide.with(mContext).load(list.get(5)).into(imageView6);
            Glide.with(mContext).load(list.get(6)).into(imageView7);
            Glide.with(mContext).load(list.get(7)).into(imageView8);
            view.setLayoutParams(ll_layoutParams);
            friendsCircleImageLayout.addView(view);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
            imageView4.setOnClickListener(this);
            imageView5.setOnClickListener(this);
            imageView6.setOnClickListener(this);
            imageView7.setOnClickListener(this);
            imageView8.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_1:
                mListener.onPositionClick(0);
                break;
            case R.id.img_2:
                mListener.onPositionClick(1);
                break;
            case R.id.img_3:
                mListener.onPositionClick(2);
                break;
            case R.id.img_4:
                mListener.onPositionClick(3);
                break;
            case R.id.img_5:
                mListener.onPositionClick(4);
                break;
            case R.id.img_6:
                mListener.onPositionClick(5);
                break;
            case R.id.img_7:
                mListener.onPositionClick(6);
                break;
            case R.id.img_8:
                mListener.onPositionClick(7);
                break;
        }
    }
}
