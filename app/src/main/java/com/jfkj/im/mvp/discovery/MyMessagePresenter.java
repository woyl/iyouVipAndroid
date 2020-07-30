package com.jfkj.im.mvp.discovery;

import com.jfkj.im.App;
import com.jfkj.im.Bean.MyDynamicMessage;
import com.jfkj.im.model.DiscoveryModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/13
 * </pre>
 */
public class MyMessagePresenter extends BasePresenter<MyMessageView> {
    public DiscoveryModel mModel;

    public MyMessagePresenter(MyMessageView view){
        attachView(view);
        mModel = new DiscoveryModel();
    }

    public void getDynamicMessage(String pageNo,String pageSize){

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.PAGENO, pageNo);
        map.put(Utils.PAGESIZE, pageSize);

        addSubscription(apiStores.getFindDynamicMessage(map), new ApiCallback<MyDynamicMessage>() {
            @Override
            public void onSuccess(MyDynamicMessage o) {
                if (o.isSuccess()){
                    mvpView.showMessage(o);
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
}
