package com.jfkj.im.mvp.Home;

import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class RecommendPresenter extends BasePresenter<RecommendView> {

    private HomeModel mHomeModel;
    public RecommendPresenter(RecommendView view){
        attachView(view);
        mHomeModel = new HomeModel();
    }

    public void getHomeRecommend(  String pageNo, String pageSize, String gender){

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        if(pageNo.length()>0){
            map.put(Utils.PAGENO,pageNo);
        }
        if(pageSize.length()>0){
            map.put(Utils.PAGESIZE, pageSize);
        }
        if(gender.length()>0){
            map.put(Utils.GENDER, gender);
        }
        addSubscription(apiStores.homeRecommend(map), new ApiCallback<HomeRecommend>() {
            @Override
            public void onSuccess(HomeRecommend model) {

                mvpView.showHomeRecommend(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }

    /**
     * 礼物列表
     */
    public void getGiftList(){

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.getGiftList(map), new ApiCallback<GiftData>() {
            @Override
            public void onSuccess(GiftData model) {
                mvpView.showGiftList(model.getData().getArray());
            }
            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }
}
