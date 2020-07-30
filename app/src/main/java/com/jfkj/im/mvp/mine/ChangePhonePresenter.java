package com.jfkj.im.mvp.mine;

import com.jfkj.im.App;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
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
 * @date :         2020/1/6
 * </pre>
 */
public class ChangePhonePresenter extends BasePresenter<ChangePhoneView> {
    private MineModel mMineModel;

    public ChangePhonePresenter(ChangePhoneView view){
        attachView(view);
        mMineModel = new MineModel();
    }

    /**
     * 检查手机号
     * @param mobile
     */
    public void checkPhone(String mobile){

        Map<String,String> maps = new HashMap<>();
        maps.put("mobileNo",mobile);
        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.OSNAME, Utils.OSNAME);
        maps.put(Utils.CHANNEL, Utils.CHANNEL);
        maps.put(Utils.DEVICENAME,Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(mMineModel.updateMobileVerification(maps), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()){
                    mvpView.mobileVerification();
                }
                ToastUtils.showShortToast(o.getMessage());
            }
        });
    }

    /**
     * 获取图形验证码
     * @param map
     */
    public void phoneCode(Map<String,String> map){
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(mMineModel.getStaticCode(map), new HttpErrorMsgObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody o) {
                mvpView.showCodeSuccess(o);
            }
        });
    }

    /**
     * 获取短信验证码
     */
    public void getPhoneCode(String mobileNo,String types,String imgYzm,String areaCode,String channel){
        Map<String,String> map = new HashMap<>();
        map.put("mobileNo",mobileNo);
        map.put("types" , types);
        map.put("imgYzm",imgYzm);
        map.put("areaCode",areaCode);

        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL,channel);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(mMineModel.getPhoneCode(map), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()){
                    mvpView.getPhoneCodeSuccess();
                }
                ToastUtils.showShortToast(o.getMessage());
            }
        });
    }

    /**
     * 修改手机号
     */
    public void updateMobile(String mobileNo,String code){
        Map<String,String> map = new HashMap<>();
        map.put("mobileNo",mobileNo);
        map.put("code",code);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(mMineModel.updateMobile(map), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()){
                    mvpView.changeSuccess();
                }
                ToastUtils.showShortToast(o.getMessage());
            }
        });
    }
}
