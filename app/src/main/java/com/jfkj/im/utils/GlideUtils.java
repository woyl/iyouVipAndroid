package com.jfkj.im.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jfkj.im.R;
import com.jfkj.im.view.RoundBitmapTransformation;


public class GlideUtils {

 	public static void loadChatImage(final Context mContext, String imgUrl, final ImageView imageView) {



	final RequestOptions options = new RequestOptions()
				.placeholder(R.drawable.chatting_pic_loading_failure)// 正在加载中的图片
				.error(R.drawable.chatting_pic_loading_failure); // 加载失败的图片

		Glide.with(mContext)
				.load(imgUrl) // 图片地址
				.apply(options)
				.into(imageView);
 	}



 	public static void loadintoRight(Context context,String imgurl,ImageView imageView){
		RequestOptions mTransitionOptions = RequestOptions.bitmapTransform(
				new RoundBitmapTransformation(20, 4, 20, 20));
		Glide.with(context).load(imgurl).apply(mTransitionOptions).into(imageView);
	}
	public static void loadintoleft(Context context,String imgurl,ImageView imageView){
		RequestOptions mTransitionOptions = RequestOptions.bitmapTransform(
				new RoundBitmapTransformation(4, 20, 20, 20));
		Glide.with(context).load(imgurl).apply(mTransitionOptions).into(imageView);
	}
}
