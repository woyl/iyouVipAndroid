package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jfkj.im.R;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.listener.onItemPositionClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/12
 * </pre>
 */
public class NearByAdapter1 extends RecyclerView.Adapter<NearByAdapter1.ViewHolder> {

    private Context mContext;
    private List<HomeRecommend.DataBean> mList = new ArrayList<>();
    private onItemPositionClickListener mListener;

    public NearByAdapter1(Context context,List<HomeRecommend.DataBean> list,onItemPositionClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_near_by, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeRecommend.DataBean item = mList.get(position);
        holder.mTvUserName.setText(item.getNickname());
        Glide.with(mContext)
                .load(R.drawable.pic_bg_user)
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //缓存转换后的资源
                .transform(new CenterCrop(),new RoundedCorners(20))
                .into(holder.mIvIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositionClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_user_name)
        AppCompatTextView mTvUserName;
        @BindView(R.id.tv_user_level)
        AppCompatTextView mTvUserLevel;
        @BindView(R.id.iv_user_sex)
        AppCompatImageView mIvUserSex;
        @BindView(R.id.tv_user_age)
        AppCompatTextView mTvUserAge;
        @BindView(R.id.tv_user_address)
        AppCompatTextView mTvUserAddress;
        @BindView(R.id.tv_user_constellation)
        AppCompatTextView mTvUserConstellation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
