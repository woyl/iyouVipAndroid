package com.jfkj.im.TIM.redpack.group;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.event.GroupUnreadMessageNumEvent;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GroupUnreadMessageNumUtils {

    public static void getConversationNumbers(){
        EventBus.getDefault().post(new UpChatRoomEvent(true));
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("msg","...getGroupList......."+code +",,,,,,"+desc);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                long allTotal = 0;
                for (TIMGroupBaseInfo info : timGroupBaseInfos){
                    if(!info.getGroupId().equals(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID))){
                        TIMConversation timConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group,info.getGroupId());
                        if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + info.getGroupId() + Utils.NODISTURB, "closenodisturb") != null) {
                            if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + info.getGroupId() + Utils.NODISTURB, "closenodisturb") != null) {
                                String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + info.getGroupId() + Utils.NODISTURB, "closenodisturb");
                                if (!closenodisturb.equals("closenodisturb")) {
                                    return;
                                }
                            }
                        }
                        allTotal += timConversation.getUnreadMessageNum();
                    }

                    EventBus.getDefault().postSticky(new GroupUnreadMessageNumEvent(allTotal,info.getGroupId()));
                }
                SPUtils.getInstance(App.getAppContext()).put(Utils.GROUP_NO_NUM, allTotal);


            }
        });
    }

    public static void getConversationNumbers(TIMValueCallBack<List<TIMGroupBaseInfo>> callBack){
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("msg","...getGroupList......."+code +",,,,,,"+desc);
                callBack.onError(code, desc);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                callBack.onSuccess(timGroupBaseInfos);
            }
        });
    }

}
