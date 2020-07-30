package com.jfkj.im.mvp.Home;

import com.jfkj.im.App;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/20
 * </pre>
 */
public class NewCustomerPresenter extends BasePresenter<NewCustomerView> {
    private HomeModel mHomeModel;
    public NewCustomerPresenter(NewCustomerView view){
        attachView(view);
        mHomeModel = new HomeModel();
    }

    public void homeNewPeople(String pageNo, String pageSize, String gender){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.PAGENO,pageNo);
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.GENDER,gender);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.homeNewPeople(map), new ApiCallback<HomeRecommend>() {
            @Override
            public void onSuccess(HomeRecommend model) {
                try {
                    if(model.getCode().equals("200")){
                        mvpView.showList(model.getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

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
                try {

                    if(model.isSuccess()){

                        mvpView.showGiftList(model.getData().getArray());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
