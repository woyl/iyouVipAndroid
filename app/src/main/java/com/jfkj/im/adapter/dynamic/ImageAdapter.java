package com.jfkj.im.adapter.dynamic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.view.CustomRoundAngleImageView;
import com.jfkj.im.view.GlideRoundTransform;
import com.jfkj.im.view.MyScaleImageView;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.utils.OkLogger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<LocalMedia> mList = new ArrayList<>();
    private Context mContext;
    private onItemPositionClickListener mListener;
    private int num;


    public ImageAdapter(Context context, List<LocalMedia> strings, int num) {
        this.mContext = context;
        this.mList = strings;
        this.num = num;
    }


    public void setOnListener(onItemPositionClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalMedia s = mList.get(position);
        int width = ScreenUtil.getScreenWidth(mContext);
//        int height = ScreenUtil.getScreenHeight(mContext);
        //            RequestOptions options = new RequestOptions().bitmapTransform(new CenterCropRoundCornerTransform(10)).placeholder(R.color.color_f5f6fa).error(R.color.color_f5f6fa);
        ViewHolder viewHolder = holder;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.mIvImage.getLayoutParams();
//        RequestOptions options = new RequestOptions().bitmapTransform(new CenterCropRoundCornerTransform(0)).placeholder(R.drawable.place_white).error(R.drawable.place_white);
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        if (num == 1) {

            holder.img_one.test(s.getWidth(), s.getHeight(), mContext);
            Glide.with(mContext).load(s.getPath()).into(holder.img_one);


        } else if (num == 2) {
            holder.mIvImage.setVisibility(View.VISIBLE);

            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            int imgWidth = width - ScreenUtil.getPxByDp(55) * 2 -  ScreenUtil.getPxByDp(10)*2;
            int imgHeight = width - ScreenUtil.getPxByDp(55) * 2 -  ScreenUtil.getPxByDp(10)*2;
            layoutParams.height = imgHeight / 2;
            layoutParams.width = imgWidth / 2;
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(imgWidth / 2, imgHeight / 2).centerCrop();

            Glide.with(mContext).load(s.getPath())
//                    .apply(options)
                    .transform(new CenterCrop(), new GlideRoundTransform(3))
                    .into(viewHolder.mIvImage);
        } else {
            holder.mIvImage.setVisibility(View.VISIBLE);
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            int imgWidth = width - ScreenUtil.getPxByDp(55) * 2 -  ScreenUtil.getPxByDp(10)*2;
            int imgHeight = width - ScreenUtil.getPxByDp(55) * 2 - ScreenUtil.getPxByDp(10)*2;
            layoutParams.height = imgHeight / 3;
            layoutParams.width = imgWidth / 3;
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(imgWidth / 3, imgHeight / 3).centerCrop();

            Glide.with(mContext).load(s.getPath())
//                    .apply(options)
                    .transform(new CenterCrop(), new GlideRoundTransform(3))
                    .into(viewHolder.mIvImage);
        }
        OkLogger.e("______________________" + s.getPath());
        viewHolder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onPositionClick(holder.getAdapterPosition());
            }
        });

        viewHolder.img_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositionClick(holder.getAdapterPosition());
            }
        });
//
//        loadImageView(mContext,s.getPath(),holder.mIvImage);

    }


//    private void loadImageView(Context context, String path, ImageView imageView) {
////        //我这里是先获取屏幕的宽高，然后把屏幕的宽设为imageView的宽。
////        int width = mContext.getResources().getDisplayMetrics().widthPixels;
////        int height = mContext.getResources().getDisplayMetrics().heightPixels;
////
////        ViewGroup.LayoutParams params = imageView.getLayoutParams();
////        params.width = width;
////        params.height = height;
////        imageView.setLayoutParams(params);
////
////        Glide.with(mContext).load(path).listener(new RequestListener<Drawable>() {
////            @Override
////            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                return false;
////            }
////
////            @Override
////            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                //首先设置imageView的ScaleType属性为ScaleType.FIT_XY，让图片不按比例缩放，把图片塞满整个View。
////                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
////                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
////                }
////                // 得到当前imageView的宽度（我设置的是屏幕宽度），获取到imageView与图片宽的比例，然后通过这个比例去设置imageView的高
////                ViewGroup.LayoutParams params = imageView.getLayoutParams();
////                int vw = (imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight()) / num;
////                float scale = (float) vw / (float) resource.getIntrinsicWidth();
////                int vh = Math.round(resource.getIntrinsicHeight() * scale);
////                params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
////                imageView.setLayoutParams(params);
////                return false;
////            }
////        }).placeholder(R.drawable.place_white).error(R.drawable.place_white).into(imageView);
////    }

    @Override
    public int getItemCount() {
        Log.d("@@@", "mListSize()=====" + mList.size());
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CustomRoundAngleImageView mIvImage;
        MyScaleImageView img_one;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
            img_one = itemView.findViewById(R.id.img_one);
        }
    }
}
