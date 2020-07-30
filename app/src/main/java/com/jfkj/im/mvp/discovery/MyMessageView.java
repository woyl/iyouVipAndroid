package com.jfkj.im.mvp.discovery;

import com.jfkj.im.Bean.MyDynamicMessage;
import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/13
 * </pre>
 */
public interface MyMessageView extends BaseView {
    void showMessage(MyDynamicMessage o);
}
