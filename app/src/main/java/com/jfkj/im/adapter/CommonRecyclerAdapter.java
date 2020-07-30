package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的RecyclerView基类
 *
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected Context mContext;
    protected List<T> mData;
    private int layoutId;
    private View mView;

    /**
     * 追加数据
     * */
    public void addData(List<T> data){
        if (data != null){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 清空并设置数据
     * */
    public void setData(List<T> data){
        mData.clear();
        addData(data);
    }

    /**
     *
     * */
    public void setItemData(List<T> data,int position){
        mData.clear();
        if (data != null){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

   /**
    * 清空数据
    */
    public void ClearData(){
        mData.clear();
        notifyDataSetChanged();
    }
    /**
     * 刷新数据
     *
     * @param aList
     */
    public void refreshData(List<T> aList) {
        if (aList != null) {
            mData=aList;
        }
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<T> getData() {
        return mData;
    }

    public void removeData(int positon){
        this.getData().remove(positon);
        notifyItemRemoved(positon);
        notifyItemRangeChanged(0,getItemCount());
    }
    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data == null ? (List<T>) new ArrayList<>() : data;
        this.layoutId = layoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(layoutId, parent, false);
        return new CommonRecyclerHolder(mContext,mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonRecyclerHolder){
            CommonRecyclerHolder commonHolder = (CommonRecyclerHolder) holder;
            commonHolder.setListPosition(position);
            convert(commonHolder, mData.size()>0?mData.get(position):null,position);
        }
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size() : 0;
    }

    public abstract void convert(CommonRecyclerHolder holder, T t,int position);
}
