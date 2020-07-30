package com.jfkj.im.mvp.mine;

import com.google.protobuf.Api;
import com.jfkj.im.App;
import com.jfkj.im.entity.ChargeRecordBean;
import com.jfkj.im.model.MineModel;
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
 * @date :         2019/12/24
 * </pre>
 */
public class ChargeRecordPresenter extends BasePresenter<ChargeRecordView> {
    private MineModel mModel;

    public ChargeRecordPresenter(ChargeRecordView view) {
        attachView(view);
        mModel = new MineModel();
    }



    public void  rechargeRecord(String pageSize , String pageNo){
        Map<String,String>  map = new HashMap();
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.PAGENO,pageNo);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.rechargeRecord(map), new ApiCallback<ChargeRecordBean>() {
            @Override
            public void onSuccess(ChargeRecordBean model) {
                mvpView.showRecord(model);
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
