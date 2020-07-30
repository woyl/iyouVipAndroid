package com.jfkj.im.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/2
 * </pre>
 */
public class ViewPagerLayoutManger extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    private PagerSnapHelper mSnapHelper;
    private Context mContext;
    private int mDrift;//位移，用来判断移动方向
    private OnViewPagerListener mListener;

    public ViewPagerLayoutManger (Context context,int orientation){
        this(context,orientation,false);
    }

    public ViewPagerLayoutManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    {
        mSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        mSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View viewIdle = mSnapHelper.findSnapView(this);
            int positionIdle = getPosition(viewIdle);
            if (mListener != null && getChildCount() == 1) {
                mListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1);
            }
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }


    /**
     * 监听水平方向的相对偏移量
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (mListener !=  null && getChildCount() == 1){
             mListener.onInitComplete();
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        if (mDrift >= 0){
            if (mListener != null){
                mListener.onPageRelease(true,getPosition(view));
            }
        }else {
            if (mListener != null) {
                mListener.onPageRelease(false,getPosition(view));
            }
        }
    }

    /**
     * 设置监听
     */
    public void setOnViewPagerListener(OnViewPagerListener listener){
        this.mListener = listener;
    }

    public interface  OnViewPagerListener{
        /*初始化完成*/
        void onInitComplete();

        /*释放的监听*/
        void onPageRelease(boolean isNext, int position);

        /*选中的监听以及判断是否滑动到底部*/
        void onPageSelected(int position, boolean isBottom);
    }
}
