package com.jfkj.im.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SlideDownRecyclerview extends RecyclerView {

    private float mPosY;    //手指按下时Y轴的值
    private float mCurPosY; //手指移动时Y轴的值
    private boolean isTop = true;//判断是否到达顶部
    private boolean isStop = false; //第一次进入RecyclerView时默认就是顶部，所以需要进行判断
    private CloseView callBack;

    //滑动到顶部时如果继续下滑则回调此方法
    public interface CloseView {
        void colseCallBack();
    }


    public void setCloseViewListener(CloseView callBack){
        this.callBack = callBack;
    }


    public SlideDownRecyclerview(Context context) {
        super(context);
    }

    public SlideDownRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideDownRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurPosY - mPosY > 0 && (Math.abs(mCurPosY - mPosY) > 10)) {
                    //向下滑動
                    if (isTop) {
                        callBack.colseCallBack();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (isStop) {
            if (canScrollVertically(-1)) {
                isTop = false;
            } else {
                //滑动到顶部
                isTop = true;
            }
        }
        isStop = true;
    }

}
