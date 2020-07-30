package com.jfkj.im.mvp.Home;

import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public interface NearByView extends BaseView {
    void showData(List<HomeRecommend.DataBean> list);

    void showGiftList(List<GiftData.DataBean.ArrayBean> array);
}
