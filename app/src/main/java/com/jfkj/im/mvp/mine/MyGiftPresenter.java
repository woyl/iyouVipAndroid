package com.jfkj.im.mvp.mine;

import com.jfkj.im.App;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/26
 * </pre>
 */
public class MyGiftPresenter extends BasePresenter<MyGiftView> {
    private MineModel mModel;

    public MyGiftPresenter(MyGiftView view){
        attachView(view);
        mModel = new MineModel();
    }

    public void getGiftList(String pageSize,String pageNo,String type){
        Map<String,String> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("type",type);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(mModel.myGift(map), new ApiCallback<MyGiftBean>() {
            @Override
            public void onSuccess(MyGiftBean model) {
                if (model.getCode().equals("200")){
                    mvpView.showGift(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
                mvpView.details();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
