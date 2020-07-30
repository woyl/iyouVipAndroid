package com.jfkj.im.adapter;

import android.view.View;

import com.jfkj.im.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public abstract class BaseRecyclerAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mData;
    protected OnItemCLKListener mOnItemCLKListener;

    public BaseRecyclerAdapter(List<T> data){
        if (data == null){
            mData = new ArrayList<>();
        }else {
            mData = data;
        }
    }

    public BaseRecyclerAdapter(){
        mData = new ArrayList<>();
    }

    public void refreshDatas(List<T> data) {
        mData.clear();
        if (CollectionUtils.getSize(data) > 0) {
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }

    public void addData( List<T> newData){
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size(), newData.size());
//        compatibilityDataSizeChanged(newData.size());
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        if (mData == null)
            return null;
        else
            return position < 0 ? null : position >= mData.size() ? null : mData.get(position);
    }

    public void setOnItemCLKListener(OnItemCLKListener onItemCLKListener) {
        this.mOnItemCLKListener = onItemCLKListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemCLKListener {
        void onItemClick(View view, BaseRecyclerAdapter adapter, int position);
    }
}
