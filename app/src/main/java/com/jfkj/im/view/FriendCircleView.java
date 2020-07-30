package com.jfkj.im.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jfkj.im.R;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.widget.CenterCropRoundCornerTransform;

import java.util.List;

public class FriendCircleView implements View.OnClickListener {

    private onItemPositionClickListener mListener;
    private RelativeLayout relativeLayout;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8;

    public void setOnListener(onItemPositionClickListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("ResourceType")
    public void setImageView(RelativeLayout friendsCircleImageLayout, Context mContext, List<String> list) {
        int width = ScreenUtil.getScreenWidth();
        int height = ScreenUtil.getScreenWidth();
        RequestOptions options = new RequestOptions().bitmapTransform(new CenterCropRoundCornerTransform(0))
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.place_white)
                .error(R.drawable.place_white)
                .centerCrop();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) friendsCircleImageLayout.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        int num = list.size();
        friendsCircleImageLayout.removeAllViews();
        /**
         * 1
         */
        imageView1 = new ImageView(mContext);
        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp_1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (num == 5) {
            lp_1.width = width / 3 * 2;
            lp_1.height = height / 3 * 2;
            Glide.with(mContext).load(list.get(0)).apply(options).override(width / 3 * 2,height / 3 * 2).into(imageView1);
        } else if (num == 6) {
            lp_1.width = width / 3 * 2;
            lp_1.height = height / 3 * 2;
            Glide.with(mContext).load(list.get(0)).apply(options).override(width / 3 * 2,height / 3 * 2).into(imageView1);
        } else if (num == 7) {
            lp_1.width = width / 4 * 3;
            lp_1.height = height / 4 * 3;
            Glide.with(mContext).load(list.get(0)).apply(options).override(width / 4 * 3,height / 4 * 3).into(imageView1);
        } else if (num == 8) {
            lp_1.width = width / 4 * 3;
            lp_1.height = height / 4 * 3;
            Glide.with(mContext).load(list.get(0)).apply(options).override(width / 4 * 3,height / 4 * 3).into(imageView1);
        }
        imageView1.setLayoutParams(lp_1);
        imageView1.setId(1);
//        imageView1.post(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(mContext).load(list.get(0)).apply(options).into(imageView1);
//            }
//        });

        friendsCircleImageLayout.addView(imageView1);
        /**
         * 2
         */
       imageView2 = new ImageView(mContext);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp_2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (num == 5) {
            lp_2.width = width / 3;
            lp_2.height = height / 3;
            Glide.with(mContext).load(list.get(1)).apply(options).override(width / 3 ,height / 3 ).into(imageView2);
        } else if (num == 6) {
            lp_2.width = width / 3;
            lp_2.height = height / 3;
            Glide.with(mContext).load(list.get(1)).apply(options).override(width / 3 ,height / 3 ).into(imageView2);
        } else if (num == 7) {
            lp_2.width = width / 4;
            lp_2.height = height / 4;
            Glide.with(mContext).load(list.get(1)).apply(options).override(width / 4,height / 4).into(imageView2);
        } else if (num == 8) {
            lp_2.width = width / 4;
            lp_2.height = height / 4;
            Glide.with(mContext).load(list.get(1)).apply(options).override(width / 4,height / 4).into(imageView2);
        }
        lp_2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, imageView1.getId());
        imageView2.setId(2);
        imageView2.setLayoutParams(lp_2);
//        imageView2.post(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(mContext).load(list.get(1)).apply(options).into(imageView2);
//            }
//        });
//        Glide.with(mContext).load(list.get(1)).apply(options).into(imageView2);
        friendsCircleImageLayout.addView(imageView2);
        /**
         * 3
         */
        imageView3 = new ImageView(mContext);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp_3 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (num == 5) {
            lp_3.width = width / 3;
            lp_3.height = height / 3;
            Glide.with(mContext).load(list.get(2)).apply(options).override(width / 3 ,height / 3 ).into(imageView3);
        } else if (num == 6) {
            lp_3.width = width / 3;
            lp_3.height = height / 3;
            Glide.with(mContext).load(list.get(2)).apply(options).override(width / 3 ,height / 3 ).into(imageView3);
        } else if (num == 7) {
            lp_3.width = width / 4;
            lp_3.height = height / 4;
            Glide.with(mContext).load(list.get(2)).apply(options).override(width / 4,height / 4).into(imageView3);
        } else if (num == 8) {
            lp_3.width = width / 4;
            lp_3.height = height / 4;
            Glide.with(mContext).load(list.get(2)).apply(options).override(width / 4,height / 4).into(imageView3);
        }
        lp_3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, imageView1.getId());
        lp_3.addRule(RelativeLayout.BELOW, imageView2.getId());
        imageView3.setId(3);
        imageView3.setLayoutParams(lp_3);
//        imageView3.post(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(mContext).load(list.get(2)).apply(options).into(imageView3);
//            }
//        });
//        Glide.with(mContext).load(list.get(2)).apply(options).into(imageView3);
        friendsCircleImageLayout.addView(imageView3);
        /**
         * 4
         */
        imageView4 = new ImageView(mContext);
        imageView4.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp_4 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (num == 5) {
            lp_4.width = width / 2;
            lp_4.height = height / 3;
            lp_4.addRule(RelativeLayout.BELOW, imageView1.getId());
            Glide.with(mContext).load(list.get(3)).apply(options).override(width /  2,height / 3 ).into(imageView4);
        } else if (num == 6) {
            lp_4.width = width / 3;
            lp_4.height = height / 3;
            lp_4.addRule(RelativeLayout.BELOW, imageView1.getId());
            Glide.with(mContext).load(list.get(3)).apply(options).override(width / 3 ,height / 3 ).into(imageView4);
        } else if (num == 7) {
            lp_4.width = width / 4;
            lp_4.height = height / 4;
            lp_4.addRule(RelativeLayout.BELOW, imageView3.getId());
            lp_4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, imageView1.getId());
            Glide.with(mContext).load(list.get(3)).apply(options).override(width / 4,height / 4).into(imageView4);
        } else if (num == 8) {
            lp_4.width = width / 4;
            lp_4.height = height / 4;
            lp_4.addRule(RelativeLayout.BELOW, imageView3.getId());
            lp_4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, imageView1.getId());
            Glide.with(mContext).load(list.get(3)).apply(options).override(width / 4,height / 4).into(imageView4);
        }
        imageView4.setId(4);
        imageView4.setLayoutParams(lp_4);
//        imageView4.post(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(mContext).load(list.get(3)).apply(options).into(imageView4);
//            }
//        });
//        Glide.with(mContext).load(list.get(3)).apply(options).into(imageView4);
        friendsCircleImageLayout.addView(imageView4);
        /**
         * 5
         */
        imageView5 = new ImageView(mContext);
        imageView5.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp_5 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (num == 5) {//
            lp_5.width = width / 2;
            lp_5.height = height / 3;
            lp_5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, imageView4.getId());
            lp_5.addRule(RelativeLayout.BELOW, imageView1.getId());
            Glide.with(mContext).load(list.get(4)).apply(options).override(width /2,height / 3 ).into(imageView5);
        } else if (num == 6) {
            lp_5.width = width / 3;
            lp_5.height = height / 3;
            lp_5.addRule(RelativeLayout.RIGHT_OF, imageView4.getId());
            lp_5.addRule(RelativeLayout.BELOW, imageView1.getId());
            Glide.with(mContext).load(list.get(4)).apply(options).override(width / 3 ,height / 3 ).into(imageView5);
        } else if (num == 7) {//
            lp_5.width = width / 4 * 3 / 2;
            lp_5.height = height / 4;
            lp_5.addRule(RelativeLayout.BELOW, imageView1.getId());
            Glide.with(mContext).load(list.get(4)).apply(options).override(width / 4 * 3 / 2,height / 4).into(imageView5);
        } else if (num == 8) {//
            lp_5.width = width / 4;
            lp_5.height = height / 4;
            lp_5.addRule(RelativeLayout.BELOW, imageView1.getId());
            Glide.with(mContext).load(list.get(4)).apply(options).override(width / 4,height / 4).into(imageView5);
        }
        imageView5.setId(5);
        imageView5.setLayoutParams(lp_5);
//        imageView5.post(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(mContext).load(list.get(4)).apply(options).into(imageView5);
//            }
//        });
//        Glide.with(mContext).load(list.get(4)).apply(options).into(imageView5);
        friendsCircleImageLayout.addView(imageView5);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        /**
         * 6
         */
        if (num > 5) {
            imageView6 = new ImageView(mContext);
            imageView6.setScaleType(ImageView.ScaleType.FIT_XY);
            RelativeLayout.LayoutParams lp_6 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (num == 6) {
                lp_6.width = width / 3;
                lp_6.height = height / 3;
                lp_6.addRule(RelativeLayout.RIGHT_OF, imageView5.getId());
                lp_6.addRule(RelativeLayout.BELOW, imageView1.getId());
                Glide.with(mContext).load(list.get(5)).apply(options).override(width / 3,height / 3).into(imageView6);
            } else if (num == 7) {
                lp_6.width = width / 4 * 3 / 2;
                lp_6.height = height / 4;
                lp_6.addRule(RelativeLayout.RIGHT_OF, imageView5.getId());
                lp_6.addRule(RelativeLayout.BELOW, imageView1.getId());
                Glide.with(mContext).load(list.get(5)).apply(options).override(width / 4*3/2,height / 4).into(imageView6);
            } else if (num == 8) {
                lp_6.width = width / 4;
                lp_6.height = height / 4;
                lp_6.addRule(RelativeLayout.RIGHT_OF, imageView5.getId());
                lp_6.addRule(RelativeLayout.BELOW, imageView1.getId());
                Glide.with(mContext).load(list.get(5)).apply(options).override(width / 4,height / 4).into(imageView6);
            }

            imageView6.setId(6);
            imageView6.setLayoutParams(lp_6);
            imageView6.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext).load(list.get(5)).apply(options).into(imageView6);
                }
            });
//            Glide.with(mContext).load(list.get(5)).apply(options).into(imageView6);
            friendsCircleImageLayout.addView(imageView6);
            imageView6.setOnClickListener(this);

            if (num > 6){
                /**
                 * 7
                 */
               imageView7 = new ImageView(mContext);
                imageView7.setScaleType(ImageView.ScaleType.FIT_XY);
                RelativeLayout.LayoutParams lp_7 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (num == 7) {
                    lp_7.width = width / 4;
                    lp_7.height = height / 4;
                    lp_7.addRule(RelativeLayout.RIGHT_OF, imageView6.getId());
                    lp_7.addRule(RelativeLayout.BELOW, imageView1.getId());
                    Glide.with(mContext).load(list.get(6)).apply(options).override(width / 4,height / 4).into(imageView7);
                } else if (num == 8) {
                    lp_7.width = width / 4;
                    lp_7.height = height / 4;
                    lp_7.addRule(RelativeLayout.RIGHT_OF, imageView6.getId());
                    lp_7.addRule(RelativeLayout.BELOW, imageView1.getId());
                    Glide.with(mContext).load(list.get(6)).apply(options).override(width / 4,height / 4).into(imageView7);
                }
                imageView7.setId(7);
                imageView7.setLayoutParams(lp_7);
//                imageView7.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.with(mContext).load(list.get(6)).apply(options).into(imageView7);
//                    }
//                });
//                Glide.with(mContext).load(list.get(6)).apply(options).into(imageView7);
                friendsCircleImageLayout.addView(imageView7);
                imageView7.setOnClickListener(this);
                if (num > 7){
                    /**
                     * 8
                     */
                    imageView8 = new ImageView(mContext);
                    imageView8.setScaleType(ImageView.ScaleType.FIT_XY);
                    RelativeLayout.LayoutParams lp_8 = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp_8.width = width / 4;
                    lp_8.height = height / 4;
                    Glide.with(mContext).load(list.get(7)).apply(options).override(width / 4,height / 4).into(imageView8);
                    lp_8.addRule(RelativeLayout.RIGHT_OF, imageView7.getId());
                    lp_8.addRule(RelativeLayout.BELOW, imageView1.getId());
                    imageView8.setId(8);
                    imageView8.setLayoutParams(lp_8);
//                    imageView8.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.with(mContext).load(list.get(7)).apply(options).into(imageView8);
//                        }
//                    });
//                    Glide.with(mContext).load(list.get(7)).apply(options).into(imageView8);
                    friendsCircleImageLayout.addView(imageView8);
                    imageView8.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 1:
                mListener.onPositionClick(0);
                break;
            case 2:
                mListener.onPositionClick(1);
                break;
            case 3:
                mListener.onPositionClick(2);
                break;
            case 4:
                mListener.onPositionClick(3);
                break;
            case 5:
                mListener.onPositionClick(4);
                break;
            case 6:
                mListener.onPositionClick(5);
                break;
            case 7:
                mListener.onPositionClick(6);
                break;
            case 8:
                mListener.onPositionClick(7);
                break;
        }
    }
}
