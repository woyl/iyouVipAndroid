package com.jfkj.im.videoPlay;

import android.content.Context;
import android.util.AttributeSet;

import com.nc.player.controller.BaseVideoController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/2
 * </pre>
 */
public class TikTokController extends BaseVideoController {
    public TikTokController(@NonNull Context context) {
        super(context);
    }

    public TikTokController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TikTokController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public boolean showNetWarning() {
        //不显示移动网络播放警告
        return false;
    }
}
