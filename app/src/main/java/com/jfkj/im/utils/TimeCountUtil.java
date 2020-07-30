package com.jfkj.im.utils;

import android.os.CountDownTimer;

import com.jfkj.im.listener.CountDownTimeListener;


/**
 * <pre>
 * Description:  倒计时
 * @author :   ys
 * @date :         2019/5/23
 * </pre>
 */
public class TimeCountUtil extends CountDownTimer {

    private CountDownTimeListener mListener = null;

    public TimeCountUtil(long millisInFuture, long countDownInterval, CountDownTimeListener listener) {
        super(millisInFuture, countDownInterval);
        this.mListener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mListener.onTimeTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        mListener.onTimeFinish();
    }
}
