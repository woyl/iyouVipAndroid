package com.jfkj.im.adapter.dynamic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jfkj.im.R;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.widget.CenterCropRoundCornerTransform;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class DynamicImageAdapter extends RecyclerView.Adapter<DynamicImageAdapter.ViewHolder> {

    private List<LocalMedia> mList = new ArrayList<>();
    private Context mContext;
    private onItemPositionClickListener mListener;

    public DynamicImageAdapter(Context context, List<LocalMedia> strings) {
        this.mContext = context;
        this.mList = strings;
    }

    public void setOnListener(onItemPositionClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_image, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalMedia s = mList.get(position);
        Log.d("@@@","SSSSSSSS" + s.getPath());

        RequestOptions options = new RequestOptions().bitmapTransform(new CenterCropRoundCornerTransform(10)).placeholder(R.drawable.add_user_iv).error(R.drawable.add_user_iv);
        Glide.with(mContext).load(s.getPath()).apply(options).into(holder.mIvImage);
        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositionClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("@@@","mListSize()=====" + mList.size());
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        RelativeLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
