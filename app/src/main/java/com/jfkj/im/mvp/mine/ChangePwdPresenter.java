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
 * @date :         2019/12/13
 * </pre>
 */
public class ChangePwdPresenter extends BasePresenter<ChangePwdView> {
    private MineModel mModel;

    public ChangePwdPresenter(ChangePwdView view){
        attachView(view);
        mModel = new MineModel();
    }

    public void changePwd(String pwd){
        Map<String,String> map = new HashMap<>();

        map.put("password",pwd);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(mModel.updateMyPassword(map), new HttpErrorMsgObserver<BaseResponse>() {
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
