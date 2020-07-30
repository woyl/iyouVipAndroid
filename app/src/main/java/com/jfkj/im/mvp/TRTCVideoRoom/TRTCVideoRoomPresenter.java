package com.jfkj.im.mvp.TRTCVideoRoom;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class TRTCVideoRoomPresenter extends BasePresenter<TRTCVideoRoomView> {

    public TRTCVideoRoomPresenter (TRTCVideoRoomView videoRoomView){
        attachView(videoRoomView);
    }
    public void TRTCVideoRoom(String userid){
        Map<String,String> map=new HashMap<>();
        map.put(Utils.USERID,userid);
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

        addSubscription(apiStores.getTRTCUserDetail(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.TRTCVideoRoomSuccess(model.string());
                } catch (IOException e) {
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
}
