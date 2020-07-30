package com.jfkj.im.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;



/**
 * 通用的RecyclerView的Holder
 */

public class CommonRecyclerHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private int position;
    private SparseArray<View> mViews;
    private Context context;
    public CommonRecyclerHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        this.mConvertView = itemView;
        this.mViews = new SparseArray<>();
    }


    /**
     * 得到item上的控件
     *
     * @param viewId 控件的id
     * @return 对应的控件
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (null==view) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;

    }

    public CommonRecyclerHolder setText(@IdRes int textViewId, String text) {
        TextView tv = getView(textViewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        } else {
            tv.setText("");
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public CommonRecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public CommonRecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public CommonRecyclerHolder setImageByUrl(int viewId, String url) {
//        ImageLoadUtils.showImageView(context, R.drawable.ic_stub, url,(ImageView)getView(viewId));
        return this;
    }


    /**
     * 获取 item 对象
     */
    public View getConvertView() {
        return mConvertView;
    }

    public void setListPosition(int position) {
        this.position = position;
    }

    /**
     * 获取当前item 位置
     */
    public int getListPosition() {
        return position;
    }


//    public CommonRecyclerHolder setOnClickListener(ListenerWithPosition.OnClickWithPositionListener clickListener, @IdRes int... viewIds) {
//        ListenerWithPosition listener = new ListenerWithPosition(position, this);
//        listener.setOnClickListener(clickListener);
//        for (int id : viewIds) {
//            View v = getView(id);
//            v.setOnClickListener(listener);
//        }
//        return this;
//    }
}
