package com.jfkj.im.utils;

import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.nc.player.player.VideoViewConfig;
import com.nc.player.player.VideoViewManager;

import java.lang.reflect.Field;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/2
 * </pre>
 */
public class ViewUtils  {

    private ViewUtils() {
    }


    /**
     * 获取当前的播放核心
     */
    public static Object getCurrentPlayerFactory() {
        VideoViewConfig config = VideoViewManager.getConfig();
        Object playerFactory = null;
        try {
            Field mPlayerFactoryField = config.getClass().getDeclaredField("mPlayerFactory");
            mPlayerFactoryField.setAccessible(true);
            playerFactory = mPlayerFactoryField.get(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerFactory;
    }

    /**
     * 将View从父控件中移除
     */
    public static void removeViewFormParent(View v) {
        if (v == null) return;
        ViewParent parent = v.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(v);
        }
    }
}
