package com.jfkj.im.TIM.redpack.group;

import android.util.Log;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupPendencyGetParam;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.group.TIMGroupPendencyListGetSucc;
import com.tencent.imsdk.ext.group.TIMGroupPendencyMeta;

import java.util.List;

public class GroupMessageCenterUtils {
    public static long getGroupMessageCenter(){
        final long[] all = {0};
        TIMGroupPendencyGetParam pendencyGetParam = new TIMGroupPendencyGetParam();
        pendencyGetParam.setTimestamp(0);
        pendencyGetParam.setNumPerPage(10);
        TIMGroupManager.getInstance().getGroupPendencyList(pendencyGetParam, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                //meta中的nextStartTimestamp如果不为 0,可以先保存起来，
                // 作为获取下一页数据的参数设置到 TIMGroupPendencyGetParam 中
                TIMGroupPendencyMeta meta = timGroupPendencyListGetSucc.getPendencyMeta();
                Log.e("msg", meta.getNextStartTimestamp()
                        + "|" + meta.getReportedTimestamp() + "|" + meta.getUnReadCount());

                List<TIMGroupPendencyItem> pendencyItems = timGroupPendencyListGetSucc.getPendencies();
                all[0] = pendencyItems.size();
            }
        });
        return all[0];
    }

    //获取当前未读消息数量

    public static long getUnreadMessageNum(TIMConversationType type, String peer) {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(type, peer);
//获取会话未读数
        long num = con.getUnreadMessageNum();
        return num;
    }
}
