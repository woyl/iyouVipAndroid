package com.jfkj.im.mvp.discovery;

import com.jfkj.im.entity.RankListBean;
import com.jfkj.im.model.DiscoveryModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;

import java.util.List;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/3
 * </pre>
 */
public class CharmPresenter extends BasePresenter<CharmView> {
    private DiscoveryModel mModel;

    public CharmPresenter(CharmView view) {
        attachView(view);
        mModel = new DiscoveryModel();
    }

    public void getRankList(String type) {
        addSubscription(mModel.getRankList(type), new HttpErrorMsgObserver<List<RankListBean>>() {
            @Override
            public void onNext(List<RankListBean> rankListBeans) {
                if (rankListBeans.size() > 0){
                    mvpView.showRankList(rankListBeans);
                }else {
                    mvpView.hideList();
                }
            }
        });
    }
}
