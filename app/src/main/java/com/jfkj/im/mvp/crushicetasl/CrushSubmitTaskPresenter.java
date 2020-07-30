package com.jfkj.im.mvp.crushicetasl;

import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class CrushSubmitTaskPresenter extends BasePresenter<CrushIceSubmitTaskView> {

    public CrushSubmitTaskPresenter(CrushIceSubmitTaskView crushIceSubmitTaskView) {
        attachView(crushIceSubmitTaskView);
    }


    public void submitMyTask(String taskId,String type,String ctaskcontent,String irewardamount){
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.TASK_ID, taskId);
        map.put(Utils.TYPE, type);
        map.put(Utils.TASK_CONTENT,ctaskcontent);
        map.put(Utils.I_REWARD_AMOUNT,irewardamount);
        addSubscription(apiStores.submitTask(map), new ApiCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean model) {
                ToastUtils.showLongToast(model.getMessage());
                if (model.getCode().equals("200")){
                    mvpView.submitSuccess(model.getMessage());
                }

            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFails(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
