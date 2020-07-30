package com.jfkj.im.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jfkj.im.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:   网格 图片
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class GridPictureAdapter extends RecyclerView.Adapter<GridPictureAdapter.ViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    public static final String TAG = "PictureSelector";
    protected OnItemClickListener mItemClickListener;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectMax = 9;
    private List<LocalMedia> list = new ArrayList<>();
    private onAddPicClickListener mOnAddPicClickListener;

    public GridPictureAdapter(Context context, onAddPicClickListener listener) {
        this.mContext = context;
        this.mOnAddPicClickListener = listener;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_grid_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            holder.ivPicture.setBackgroundResource(R.mipmap.icon_add_picture);
            holder.ivDelete.setVisibility(View.INVISIBLE);
            holder.ivPicture.setOnClickListener(v -> {
                mOnAddPicClickListener.onAddPicClick();
            });
        } else {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setOnClickListener(v -> {
                int index = holder.getAdapterPosition();
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, list.size());
                }
            });
        }

        LocalMedia media = list.get(position);
        if (media == null || TextUtils.isEmpty(media.getPath())) {
            return;
        }
        int chooseModel = media.getChooseModel();
        String path;
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            path = media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }

        Glide.with(mContext)
                .load(!media.isCut() && !media.isCompressed() ? Uri.parse(path) : path)
                .centerCrop()
                .placeholder(R.color.color_E5E5E5)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(holder.ivPicture);
        Log.i(TAG, "原图地址::" + media.getPath());

        if (media.isCut()) {
            Log.i(TAG, "裁剪地址::" + media.getCutPath());
        }
        if (media.isCompressed()) {
            Log.i(TAG, "压缩地址::" + media.getCompressPath());
            Log.i(TAG, "压缩后文件大小::" + new File(media.getCompressPath()).length() / 1024 + "k");
        }
        if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
            Log.i(TAG, "Android Q特有地址::" + media.getAndroidQToPath());
        }
        if (media.isOriginal()) {
            Log.i(TAG, "是否开启原图功能::" + true);
            Log.i(TAG, "开启原图功能后地址::" + media.getOriginalPath());
        }

        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                mItemClickListener.onItemClick(adapterPosition, v);
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_choose_picture);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
