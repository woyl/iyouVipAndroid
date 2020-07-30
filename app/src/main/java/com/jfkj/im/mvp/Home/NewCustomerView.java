package com.jfkj.im.mvp.Home;

import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/20
 * </pre>
 */
public interface NewCustomerView extends BaseView {
    void showList(List<HomeRecommend.DataBean> list);
    void showGiftList(List<GiftData.DataBean.ArrayBean> array);
}
