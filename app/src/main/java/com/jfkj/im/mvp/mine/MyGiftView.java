package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/26
 * </pre>
 */
public interface MyGiftView extends BaseView {

    void showGift(MyGiftBean bean);

    void details();
}
