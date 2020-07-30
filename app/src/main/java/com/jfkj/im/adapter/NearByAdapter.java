package com.jfkj.im.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jfkj.im.R;
import com.jfkj.im.entity.NearBy;

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
 * @date :         2019/11/25
 * </pre>
 */
public class NearByAdapter extends BaseRecyclerAdapter<NearBy.NearByData, NearByAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData();
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
            ButterKnife.bind(this,itemView);
        }

        public ViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_near_by, parent, false));
        }

        public void setData() {
            NearBy.NearByData item = getItem(getAdapterPosition());
            mTvUserName.setText(item.getNickname());
            Glide.with(itemView.getContext())
                    .load(R.drawable.pic_bg_user)
                    .transition(withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //缓存转换后的资源
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .into(mIvIcon);
        }
    }

}
