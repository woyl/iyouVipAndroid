package com.jfkj.im.mvp.Home;

import android.view.View;

import com.jfkj.im.ArticleDynamicBean;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.UserPraiseBean;
import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/20
 * </pre>
 */
public interface DynamicDetailView extends BaseView {

    void showPraise(UserPraiseBean bean, View view);

    void showSuccess();

    void refreshData(CircleBean o);

    void refreshComment(ArticleDynamicBean bean);

    void showView(ArticleDynamicBean o);

        void showDelete(int position);
}
