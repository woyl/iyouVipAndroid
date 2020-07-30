package com.jfkj.im.mvp.userDetail;

import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/6
 * </pre>
 */
public interface UserDetailView extends BaseView {
    void showDetail(UserDetail detail);

    void showGiftList(List<GiftData.DataBean.ArrayBean> array);

    void  showCircleArticle(CircleBean circleBean);

    void showPraise(PraiseIdBean o,int position);

    void showSuccess();

    void showGroupList(UserDetailClubBean o);

    void Pullblacksuccess();

    void upLoadPic(String url);

    void fails(String error);
}
