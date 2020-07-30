package com.jfkj.im.mvp.discovery;

import com.jfkj.im.entity.RankListBean;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/3
 * </pre>
 */
public interface CharmView extends BaseView {
    void showRankList(List<RankListBean> listBeans);

    void showContributeList();

    void hideList();
}
