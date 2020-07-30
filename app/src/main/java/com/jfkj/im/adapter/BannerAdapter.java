package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.VipListBean;
import com.jfkj.im.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private VipListBean data;

    private Context context;

    private float itemWidth = 1.0f;
    private float ratio = 0.5f;  // 宽高比


    private DisplayMetrics displayMetrics;




    public void setData(VipListBean data){
        this.data = data;
    }

    public BannerAdapter(VipListBean data, Context context){
        super();
        this.data = data;
        this.context = context;
        displayMetrics = context.getResources().getDisplayMetrics();
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        final BannerViewHolder holder = new BannerViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "onclick :" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();


            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BannerViewHolder holder, final int position) {
//        holder.textView.setBackgroundColor(Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
//        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
//        layoutParams.width = (int) (displayMetrics.widthPixels * itemWidth);
//        layoutParams.height = (int) (layoutParams.width * ratio);
        if (data.getData().get(position).getVipType()  == 1) {
            holder.tv_vip.setText("银卡");
            holder.tv_jf.setText("可获得" + data.getData().get(position).getImoney());
            holder.tv_money.setText(data.getData().get(position).getIexchangemoney() + "");
            Glide.with(context).load(R.mipmap.vipcard_bg_1).into(holder.imageView);
            Drawable drawable = ContextCompat.getDrawable(context,R.mipmap.icon_yue);
            if (drawable != null) {
                drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            holder.tv_jf.setCompoundDrawables(null,null,drawable,null);

            holder.ll_1.setVisibility(View.VISIBLE);
            holder.ll_2.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.mipmap.vip_icon_picture_need).into(holder.img_1);
            Glide.with(context).load(R.mipmap.vip_icon_video_need).into(holder.img_2);
            holder.tv_1.setText("需要提供\n" +
                    "真实照片");
            holder.tv_2.setText("需要进行\n" +
                    "视频认证");
        } else if (data.getData().get(position).getVipType() == 2) {
            holder.tv_vip.setText("金卡");
            holder.tv_jf.setText("可获得" + data.getData().get(position).getImoney());
            holder.tv_money.setText(data.getData().get(position).getIexchangemoney() + "");
            holder.tv_vip.setTextColor(ContextCompat.getColor(context,R.color.c85612B));
            holder.tv_jf.setTextColor(ContextCompat.getColor(context,R.color.c85612B));
            holder.tv_money.setTextColor(ContextCompat.getColor(context,R.color.c85612B));
            holder.tv_money_type.setTextColor(ContextCompat.getColor(context,R.color.c85612B));
            Glide.with(context).load(R.mipmap.vipcard_bg_2).into(holder.imageView);
            Drawable drawable = ContextCompat.getDrawable(context,R.mipmap.icon_yue);
            if (drawable != null) {
                drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            holder.tv_jf.setCompoundDrawables(null,null,drawable,null);

            holder.ll_1.setVisibility(View.VISIBLE);
            holder.ll_2.setVisibility(View.VISIBLE);
            holder.ll_3.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.mipmap.vip_icon_picture_not).into(holder.img_1);
            Glide.with(context).load(R.mipmap.vip_icon_video_not).into(holder.img_2);
            Glide.with(context).load(R.mipmap.vip_icon_id_secret).into(holder.img_3);
            holder.tv_1.setText("不需要\n" +
                    "上传照片");
            holder.tv_2.setText("不需要\n" +
                    "视频认证");
            holder.tv_3.setText("头像保密\n" +
                    "身份保密");
        } else if(data.getData().get(position).getVipType() == 3){
            holder.tv_vip.setText("黑卡");
            holder.tv_jf.setText("可获得" + data.getData().get(position).getImoney());
            holder.tv_money.setText(data.getData().get(position).getIexchangemoney() + "");
            holder.tv_vip.setTextColor(ContextCompat.getColor(context,R.color.cEED5A9));
            holder.tv_jf.setTextColor(ContextCompat.getColor(context,R.color.cEED5A9));
            holder.tv_money.setTextColor(ContextCompat.getColor(context,R.color.cEED5A9));
            holder.tv_money_type.setTextColor(ContextCompat.getColor(context,R.color.cEED5A9));
            Glide.with(context).load(R.mipmap.vipcard_bg_3).into(holder.imageView);
            Drawable drawable = ContextCompat.getDrawable(context,R.mipmap.icon_yue);
            if (drawable != null) {
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            holder.tv_jf.setCompoundDrawables(null,null,drawable,null);
//            FrameLayout.LayoutParams rl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            int scriWidth = ScreenUtil.getScreenWidth(context);
//            rl.width = (int) (scriWidth * 0.85); // 设置图片的宽度
//            rl.height = (int) (rl.width * 0.52);// 设置图片的高度
//            Log.e("msg", "....rl.width......" + rl.width);
//            Log.e("msg", "....rl.height......" + rl.height);
//            holder.imageView.setLayoutParams(rl);
            holder.ll_1.setVisibility(View.VISIBLE);
            holder.ll_2.setVisibility(View.VISIBLE);
            holder.ll_3.setVisibility(View.VISIBLE);
          //  holder.ll_4.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.mipmap.vip_icon_picture_not).into(holder.img_1);
            Glide.with(context).load(R.mipmap.vip_icon_video_not).into(holder.img_2);
            Glide.with(context).load(R.mipmap.vip_icon_id_secret).into(holder.img_3);
     //       Glide.with(context).load(R.mipmap.vip_icon__ferrari_blackcrad).into(holder.img_4);
            holder.tv_1.setText("不需要\n" +
                    "上传照片");
            holder.tv_2.setText("不需要\n" +
                    "视频认证");
            holder.tv_3.setText("头像保密\n" +
                    "身份保密");

        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.getData().size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, img_1, img_2, img_3, img_4;
        TextView tv_vip, tv_jf, tv_money, tv_1, tv_2, tv_3,tv_money_type;
        LinearLayout ll_1, ll_2, ll_3, ll_4;
        FrameLayout fl_1;

        public BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pic);
            tv_vip = itemView.findViewById(R.id.tv_vip);
            tv_jf = itemView.findViewById(R.id.tv_jf);
            tv_money = itemView.findViewById(R.id.tv_money);
            fl_1 = itemView.findViewById(R.id.fl_1);
            img_1 = itemView.findViewById(R.id.img_1);
            img_2 = itemView.findViewById(R.id.img_2);
            img_3 = itemView.findViewById(R.id.img_3);
            img_4 = itemView.findViewById(R.id.img_4);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
//            tv_4 = itemView.findViewById(R.id.tv_4);
            ll_1 = itemView.findViewById(R.id.ll_1);
            ll_2 = itemView.findViewById(R.id.ll_2);
            ll_3 = itemView.findViewById(R.id.ll_3);
            ll_4 = itemView.findViewById(R.id.ll_4);
            tv_money_type = itemView.findViewById(R.id.tv_money_type);
        }

    }

    public float getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(float itemWidth) {
        this.itemWidth = itemWidth;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }



}
