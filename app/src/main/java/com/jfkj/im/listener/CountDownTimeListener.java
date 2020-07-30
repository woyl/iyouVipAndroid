package com.jfkj.im.listener;

/**
 * <pre>
 * Description: 计时监听接口
 * @author :   ys
 * @date :         2019/5/23
 * </pre>
 */
public interface CountDownTimeListener {

    /**
     * 计时完成
     */
    void onTimeFinish();

    /**
     * 计时过程
     * @param millisUntilFinished
     */
    void onTimeTick(long millisUntilFinished);
}
