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

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/31
 * </pre>
 */
public class PhoneAndPwdPresenter extends BasePresenter<PhoneAndPwdView> {
    public MineModel mModel;

    public PhoneAndPwdPresenter(PhoneAndPwdView view){
        attachView(view);
        mModel = new MineModel();
    }

    public void updatePasswordVerification(String password){
        Map<String,String> map = new HashMap<>();
        map.put("password",password);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(mModel.updatePasswordVerification(map), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                if (o.isSuccess()){
                    mvpView.verifySuccess();
                }else {
                    ToastUtils.showShortToast("原密码输入错误");
                }
            }
        });
    }
}
