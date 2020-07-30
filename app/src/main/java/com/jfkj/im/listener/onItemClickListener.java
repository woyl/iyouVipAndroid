package com.jfkj.im.listener;

import android.view.View;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public interface onItemClickListener {
    void onPositionClick(int position, View v);

    void onPositionClick(int position, View v,boolean b);
}
