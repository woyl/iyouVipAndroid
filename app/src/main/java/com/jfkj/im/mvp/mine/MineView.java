package com.jfkj.im.mvp.mine;

import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public interface MineView extends BaseView {

    void showMineInfo(MineInfo info);

    void showDelete(int position);

    void showPraise(String  id, int position);

    void showSuccess();

    //显示我的俱乐部
    void showUserGroupTask(UserGroupTaskBean userGroupTaskBean);

    //查询余额成功
    void onSuccessBalance();

    void onFindOnNew(List<CommentBean> commentBean);

    //获取用户信息
    void getUserInfo(UserDetailBean userDetailBean);
}
