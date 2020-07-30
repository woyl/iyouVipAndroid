package com.jfkj.im.mvp.Home;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.model.HomeModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public class NearByPresent extends BasePresenter<NearByView> {

    private HomeModel mHomeModel;

    public NearByPresent(NearByView view){
        attachView(view);
        mHomeModel = new HomeModel();
    }

    public void homeNearBy(String pageNo,String pageSize,String longitude, String latitude,String gender){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);


        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.PAGENO,pageNo);
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.LONGITUDE,longitude);
        map.put(Utils.LATITUDE,latitude);
        map.put(Utils.GENDER,gender);

        addSubscription(apiStores.homeNearby(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    String string = model.string();
                    JSONObject jsonObject=new JSONObject(string);
                    if(jsonObject.getString("code").equals("200")){
                        mvpView.showData( JSON.parseArray(jsonObject.getJSONArray("data").toString(),HomeRecommend.DataBean.class));
                    }else {
                        mvpView.hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {

            }
        });

//        addSubscription(mHomeModel.homeNearBy(age, pageNo, pageSize,longitude,latitude,gender), new HttpErrorMsgObserver<ResponseBody>() {
//            @Override
//            public void onNext(ResponseBody o) {
//                try {
//                    String string = o.string();
//                    JSONObject jsonObject=new JSONObject(string);
//                    if(jsonObject.getString("code").equals("200")){
//                        mvpView.showData( JSON.parseArray(jsonObject.getJSONArray("data").toString(),HomeRecommend.DataBean.class));
//                    }else {
//                        mvpView.hideLoading();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFinished() {
//                super.onFinished();
//              //  mvpView.hideLoading();
//            }
//        });
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
        addSubscription(apiStores.gethomeGiftList(map), new ApiCallback<GiftData>() {
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
