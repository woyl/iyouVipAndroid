package com.jfkj.im.TIM.redpack;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomUtil;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

public class LastMessageUtils {

    public static void getLastMessage(TIMConversationType type, String id, TIMValueCallBack<List<String>> callBack, Context context) {
//        getLocal(type, id, callBack, context);
        getFwLastMessage(type, id, callBack, context);
    }

    private static void getLocal(TIMConversationType type, String id, TIMValueCallBack<List<String>> callBack, Context context) {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(type, id);
//获取此会话的消息
        con.getLocalMessage(1, //获取此会话最近的 10 条消息
                null, //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                new TIMValueCallBack<List<TIMMessage>>() {//回调接口
                    @Override
                    public void onError(int code, String desc) {//获取消息失败
                        //接口返回了错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 含义请参见错误码表
                        Log.e("msg", "get message failed. code: " + code + " errmsg: " + desc);
                        callBack.onError(code, desc);
                    }

                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {//获取消息成功
//                        //遍历取得的消息
//                        for(TIMMessage msg : msgs) {
////                            lastMsg = msg;
//                            //可以通过 timestamp()获得消息的时间戳, isSelf()是否为自己发送的消息
//                            Log.e("msg", "get msg: " + msg.timestamp() + " self: " + msg.isSelf() + " seq: " + msg.getSeq());
//
//                        }
                        if (msgs.size() == 0) {
                            getFwLastMessage(type, id, callBack, context);
                        } else {
                            String[] strings = ChatRoomController.onNewMessage(msgs, context);
                            if (strings.length > 0) {
                                FriendsUtils.getUsersProfileLoaclService(strings[0], new TIMValueCallBack<TIMUserProfile>() {
                                    @Override
                                    public void onError(int code, String desc) {
                                    }

                                    @Override
                                    public void onSuccess(TIMUserProfile timUserProfile) {
                                        if (timUserProfile != null && !TextUtils.isEmpty(timUserProfile.getFaceUrl())) {
                                            if (TextUtils.isEmpty(SPUtils.getInstance(context).getString(Utils.IS_NEW_MESSAGE))) {
                                                SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE, strings[1]);
                                                SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,false);
                                            } else if (TextUtils.equals(SPUtils.getInstance(context).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
                                                SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,false);
                                            } else if (!TextUtils.equals(SPUtils.getInstance(context).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
                                                SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE, strings[1]);
                                                SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,true);
                                            } else {
                                                SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,false);
                                            }
//                                    if (PermissionsCheckUtils.checkFloatPermission(getContext())) {
//                                        Intent intent = new Intent(getActivity(), FloatViewService.class);
//                                        intent.putExtra("img", timUserProfile.getFaceUrl());
//                                        intent.putExtra("text", strings[1]);
//                                        if (TextUtils.isEmpty(SPUtils.getInstance(getActivity()).getString(Utils.IS_NEW_MESSAGE))) {
//                                            SPUtils.getInstance(getActivity()).put(Utils.IS_NEW_MESSAGE, strings[1]);
//                                            intent.putExtra("new", false);
//                                            //启动FloatViewService
//                                        } else if (TextUtils.equals(SPUtils.getInstance(getActivity()).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
//                                            intent.putExtra("new", false);
//                                            //启动FloatViewService
//                                        } else if (!TextUtils.equals(SPUtils.getInstance(getActivity()).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
//                                            intent.putExtra("new", true);
//                                        } else {
//                                            intent.putExtra("new", false);
//                                        }
//                                        Objects.requireNonNull(getActivity()).startService(intent);
//                                    } else {
//                                        Glide.with(mActivity).load(timUserProfile.getFaceUrl()).into(head_iv);
//                                        Glide.with(mActivity).load(timUserProfile.getFaceUrl()).into(head_iv_1);
//                                        tv_message.setText(strings[1]);
//                                        tv_message_1.setText(strings[1]);
//                                        notice_content.startFlipping();
//
                                            List<String> list = new ArrayList<>();
                                            list.add(strings[1]);
                                            list.add(timUserProfile.getFaceUrl());
                                            callBack.onSuccess(list);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }


    public static void getFwLastMessage(TIMConversationType type, String id, TIMValueCallBack<List<String>> callBack, Context context) {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(type, id);
//
//        int loginStatus = TIMManager.getInstance().getLoginStatus();
//        String loginUser = TIMManager.getInstance().getLoginUser();


        //获取此会话的消息
        con.getMessage(1, //获取此会话最近的 1  条消息
                null, //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                new TIMValueCallBack<List<TIMMessage>>()    {//回调接口
                    @Override
                    public void onError(int code, String desc) {//获取消息失败
                        //接口返回了错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 含义请参见错误码表
                        Log.d("msg", "get message failed. code: " + code + " errmsg: " + desc );

                        if(code == 10007){
                            //forbidden   没有权限-群聊未加入成功
                            ChatRoomUtil.applyJoinGroup();
                        }else{
                            callBack.onError(code, desc  );
                        }

                    }

                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {//获取消息成功
                        //遍历取得的消息
//                        for(TIMMessage msg : msgs) {
//                            lastMsg = msg;
//                            //可以通过 timestamp()获得消息的时间戳, isSelf()是否为自己发送的消息
//                            Log.e(tag, "get msg: " + msg.timestamp() + " self: " + msg.isSelf() + " seq: " + msg.msg.seq());
//
//                        }
                        String[] strings = ChatRoomController.onNewMessage(msgs, context);
                        if (strings.length > 0) {

                            FriendsUtils.getUsersProfileLoaclService(strings[0], new TIMValueCallBack<TIMUserProfile>() {
                                @Override
                                public void onError(int code, String desc) {
                                    OkLogger.e(desc);
                                }

                                @Override
                                public void onSuccess(TIMUserProfile timUserProfile) {
                                    if (timUserProfile != null && !TextUtils.isEmpty(timUserProfile.getFaceUrl())) {
                                        if (TextUtils.isEmpty(SPUtils.getInstance(context).getString(Utils.IS_NEW_MESSAGE))) {
                                            SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE, strings[1]);
                                            SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,false);
                                        } else if (TextUtils.equals(SPUtils.getInstance(context).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
                                            SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,false);
                                        } else if (!TextUtils.equals(SPUtils.getInstance(context).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
                                            SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE, strings[1]);
                                            SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,true);
                                        } else {
                                            SPUtils.getInstance(context).put(Utils.IS_NEW_MESSAGE_ROLL,false);
                                        }
//                                    if (PermissionsCheckUtils.checkFloatPermission(getContext())) {
//                                        Intent intent = new Intent(getActivity(), FloatViewService.class);
//                                        intent.putExtra("img", timUserProfile.getFaceUrl());
//                                        intent.putExtra("text", strings[1]);
//                                        if (TextUtils.isEmpty(SPUtils.getInstance(getActivity()).getString(Utils.IS_NEW_MESSAGE))) {
//                                            SPUtils.getInstance(getActivity()).put(Utils.IS_NEW_MESSAGE, strings[1]);
//                                            intent.putExtra("new", false);
//                                            //启动FloatViewService
//                                        } else if (TextUtils.equals(SPUtils.getInstance(getActivity()).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
//                                            intent.putExtra("new", false);
//                                            //启动FloatViewService
//                                        } else if (!TextUtils.equals(SPUtils.getInstance(getActivity()).getString(Utils.IS_NEW_MESSAGE), strings[1])) {
//                                            intent.putExtra("new", true);
//                                        } else {
//                                            intent.putExtra("new", false);
//                                        }
//                                        Objects.requireNonNull(getActivity()).startService(intent);
//                                    } else {
//                                        Glide.with(mActivity).load(timUserProfile.getFaceUrl()).into(head_iv);
//                                        Glide.with(mActivity).load(timUserProfile.getFaceUrl()).into(head_iv_1);
//                                        tv_message.setText(strings[1]);
//                                        tv_message_1.setText(strings[1]);
//                                        notice_content.startFlipping();
//
                                        List<String> list = new ArrayList<>();
                                        list.add(strings[1]);
                                        list.add(timUserProfile.getFaceUrl());
                                        callBack.onSuccess(list);
                                    }else{
                                        List<String> list = new ArrayList<>();
//                                        list.add(strings[1]);
                                        list.add(SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat + "extra","广场"));
//                                        list.add("[提示消息]");
                                        //
                                        list.add(   SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat + "head","广场头像 , 加载失败走onError"));

                                        callBack.onSuccess(list);
                                    }
                                }
                            });
                        }

                    }
                });
    }


    public static void getLocalLastMessage(TIMConversationType type, String id, TIMValueCallBack<List<TIMMessage>> callBack) {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(type, id);

//获取此会话的消息
        con.getMessage(1, //获取此会话最近的 10 条消息
                null, //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                new TIMValueCallBack<List<TIMMessage>>() {//回调接口
                    @Override
                    public void onError(int code, String desc) {//获取消息失败
                        //接口返回了错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 含义请参见错误码表
                        Log.d("msg", "get message failed. code: " + code + " errmsg: " + desc);
                        callBack.onError(code, desc);
                    }

                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {//获取消息成功
                        //遍历取得的消息
//                        for(TIMMessage msg : msgs) {
//                            lastMsg = msg;
//                            //可以通过 timestamp()获得消息的时间戳, isSelf()是否为自己发送的消息
//                            Log.e(tag, "get msg: " + msg.timestamp() + " self: " + msg.isSelf() + " seq: " + msg.msg.seq());
//
//                        }
                        callBack.onSuccess(msgs);

                    }
                });
    }
}
