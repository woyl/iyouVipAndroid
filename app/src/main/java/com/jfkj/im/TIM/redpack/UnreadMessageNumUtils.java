package com.jfkj.im.TIM.redpack;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.TIM.redpack.group.GroupUnreadMessageNumUtils;
import com.jfkj.im.event.GroupUnreadMessageNumEvent;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UnreadMessageNumUtils {

    //已读上报
    public static void setReadMessage(TIMConversationType type, String peer) {
        //对群聊会话已读上报
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                type,      //会话类型：群组
                peer);                       //群组 Id
        //将此会话的 lastMsg 代表的消息及这个消息之前的所有消息标记为已读
        LastMessageUtils.getLocalLastMessage(type, peer, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                for (TIMMessage msg : timMessages) {
//                  lastMsg = msg;
                    //可以通过 timestamp()获得消息的时间戳, isSelf()是否为自己发送的消息
                    Log.e("msg", "get msg: " + msg.timestamp() + " self: " + msg.isSelf() + " seq: " + msg.getSeq());
                    conversation.setReadMessage(msg, new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {
                            Log.e("msg", "setReadMessage failed, code: " + code + "|desc: " + desc);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d("msg", "setReadMessage succ");
                            GroupUnreadMessageNumUtils.getConversationNumbers(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                                @Override
                                public void onError(int code, String desc) {

                                }

                                @Override
                                public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                                    long unNum = 0;
                                    for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                                        unNum += getUnreadMessageNum(type, timGroupBaseInfo.getGroupId());
                                        if (timGroupBaseInfo.getGroupId().equals(Utils.CIRCLEROOMID)) {
                                            return;
                                        }
                                        if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + timGroupBaseInfo.getGroupId() + Utils.NODISTURB, "closenodisturb") != null) {
                                            if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + timGroupBaseInfo.getGroupId() + Utils.NODISTURB, "closenodisturb") != null) {
                                                String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + timGroupBaseInfo.getGroupId() + Utils.NODISTURB, "closenodisturb");
                                                if (!closenodisturb.equals("closenodisturb")) {
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    EventBus.getDefault().postSticky(new GroupUnreadMessageNumEvent(unNum, peer));
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    //获取当前未读消息数量

    private static long getUnreadMessageNum(TIMConversationType type, String peer) {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(type, peer);
//获取会话未读数
        long num = con.getUnreadMessageNum();
        return num;
    }
}
