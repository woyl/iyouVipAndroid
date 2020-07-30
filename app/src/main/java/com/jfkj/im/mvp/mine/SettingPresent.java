package com.jfkj.im.mvp.mine;

import android.content.Context;

import com.jfkj.im.App;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/20
 * </pre>
 */
public class SettingPresent extends BasePresenter<SettingView> {
    private Context mContext;
    private MineModel mModel;

    public SettingPresent(SettingView view) {
        attachView(view);
        mModel = new MineModel();
    }


    public void setUpVip(String hideOnline,String hideLocation) {
        Map<String, String> map = new HashMap();


        if(hideLocation.trim().length()>0){
            map.put("hideOnline",hideOnline);
        }
        if(hideLocation.trim().length()>0){
            map.put("hideLocation",hideLocation);
        }


        addSubscription(apiStores.sethide(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse model) {
                if (model.isSuccess()){
                    // mvpView.changeStates();
                }else {
                    ToastUtils.showShortToast(model.getMessage());
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
    public void loginOut() {
        Map<String, String> map = new HashMap();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME,AppUtils.getReqTime());
        addSubscription(apiStores.loginOut(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loginOutSuccess(model);
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
