package com.jfkj.im.TIM.redpack.tips;

import android.util.Log;

import com.jfkj.im.TIM.modules.group.apply.GroupApplyInfo;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyGetParam;
import com.tencent.imsdk.ext.group.TIMGroupPendencyHandledStatus;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.group.TIMGroupPendencyListGetSucc;
import com.tencent.imsdk.ext.group.TIMGroupPendencyMeta;

import java.util.ArrayList;
import java.util.List;

public class LoadApplyListUtil {


    public static void getGroupPendencyList(TIMValueCallBack<List<TIMGroupPendencyItem>> callBack){
        List<TIMGroupPendencyItem> timGroupPendencyItems = new ArrayList<>();
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setTimestamp(0);//首次获取填 0
        param.setNumPerPage(99);

        TIMGroupManager.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int code, String desc) {
                callBack.onError(code, desc);
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                //meta中的nextStartTimestamp如果不为 0,可以先保存起来，
                // 作为获取下一页数据的参数设置到 TIMGroupPendencyGetParam 中
                TIMGroupPendencyMeta meta = timGroupPendencyListGetSucc.getPendencyMeta();
                Log.d("tag", meta.getNextStartTimestamp()
                        + "|" + meta.getReportedTimestamp() + "|" + meta.getUnReadCount());

                List<TIMGroupPendencyItem> pendencyItems = timGroupPendencyListGetSucc.getPendencies();
                for(TIMGroupPendencyItem item : pendencyItems){
                    //对群未决进行相应操作，例如查看/通过/拒绝等
                    if (item.getHandledStatus() == TIMGroupPendencyHandledStatus.NOT_HANDLED){
                        timGroupPendencyItems.add(item);
                    }
                }
                callBack.onSuccess(timGroupPendencyItems);
            }
        });
    }
}
