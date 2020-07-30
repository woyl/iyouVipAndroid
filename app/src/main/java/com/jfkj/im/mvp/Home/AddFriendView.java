package com.jfkj.im.mvp.Home;

import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public interface AddFriendView extends BaseView {
    void addSuccess(String userid);
     void userdetail(String s);

     void addFriendSuccess();

}
