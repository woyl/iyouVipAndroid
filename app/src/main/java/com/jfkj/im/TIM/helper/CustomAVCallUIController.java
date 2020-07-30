package com.jfkj.im.TIM.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.Videochatbean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.ChatLayout;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.group.GroupMessageCenterUtils;
import com.jfkj.im.TIM.redpack.group.GroupUnreadMessageNumUtils;
import com.jfkj.im.TIM.redpack.tips.LoadApplyListUtil;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.DateTimeUtil;
import com.jfkj.im.TIM.utils.DemoLog;
import com.jfkj.im.TIM.utils.TUIKitLog;

import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.ApplyJoinGroupEvent;
import com.jfkj.im.event.GroupUnreadMessageNumEvent;
import com.jfkj.im.event.UpdateMessageEvent;
import com.jfkj.im.event.UserInfoEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.manager.MyActivityManager;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.service.VideoFloatService;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.dialog.PreviewDialog;
import com.jfkj.im.ui.dialog.VideocallDialog;
import com.jfkj.im.ui.dialog.VipUpgradeOneDialogFragment;
import com.jfkj.im.ui.dialog.VipUpgradeTwoDialogFragment;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ServiceUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jfkj.im.TIM.helper.CustomMessage.CLUB_AT_5001;
import static com.jfkj.im.TIM.helper.CustomMessage.JSON_VERSION_3_ANDROID_IOS_TRTC;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_ACCEPTED;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_DIALING;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_HANGUP;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_LINE_BUSY;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_REJECT;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_SPONSOR_CANCEL;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_SPONSOR_TIMEOUT;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_UNKNOWN;


public class CustomAVCallUIController extends TRTCCloudListener {

    private static final String TAG = CustomAVCallUIController.class.getSimpleName();

    private static final int VIDEO_CALL_STATUS_FREE = 1;
    private static final int VIDEO_CALL_STATUS_BUSY = 2;
    private static final int VIDEO_CALL_STATUS_WAITING = 3;
    private int mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;

    private static CustomAVCallUIController mController;

    private long mEnterRoomTime;
    private CustomMessage mOnlineCall;
    private ChatLayout mUISender;
    private TRTCDialog mDialog;
    private TRTCCloud mTRTCCloud;
    PreviewDialog previewDialog;
    VideocallDialog videocallDialog;
    private static final int VIDEO_CALL_OUT_GOING_TIME_OUT = 60 * 1000;
    private static final int VIDEO_CALL_OUT_INCOMING_TIME_OUT = 60 * 1000;
    private Handler mHandler = new Handler();
    private Runnable mVideoCallOutgoingTimeOut = new Runnable() {
        @Override
        public void run() {
            DemoLog.i(TAG, "time out, dismiss outgoing dialog");
            mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
            sendVideoCallAction(VIDEO_CALL_ACTION_SPONSOR_CANCEL, mOnlineCall);
            dismissDialog();
        }
    };

    private Runnable mVideoCallIncomingTimeOut = new Runnable() {
        @Override
        public void run() {
            DemoLog.i(TAG, "time out, dismiss incoming dialog");
            mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
            sendVideoCallAction(VIDEO_CALL_ACTION_SPONSOR_TIMEOUT, mOnlineCall);
            dismissDialog();

        }
    };

    private CustomAVCallUIController() {
        mTRTCCloud = TRTCCloud.sharedInstance(App.getAppContext());
        TRTCListener.getInstance().addTRTCCloudListener(this);
        mTRTCCloud.setListener(TRTCListener.getInstance());
    }

    public static CustomAVCallUIController getInstance() {
        if (mController == null) {
            mController = new CustomAVCallUIController();
        }
        return mController;
    }

    public void onCreate() {
        mTRTCCloud = TRTCCloud.sharedInstance(App.getAppContext());
        mTRTCCloud.setListener(this);
    }

    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {
        DemoLog.i(TAG, "trtc onError");
        mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
        sendVideoCallAction(VIDEO_CALL_ACTION_HANGUP, mOnlineCall);
        Toast.makeText(App.getAppContext(), "通话异常: " + errMsg + "[" + errCode + "]", Toast.LENGTH_LONG).show();
        if (mTRTCCloud != null) {
            mTRTCCloud.exitRoom();
        }
    }

    @Override
    public void onEnterRoom(long elapsed) {
        DemoLog.i(TAG, "onEnterRoom " + elapsed);
        Toast.makeText(App.getAppContext(), "开始通话", Toast.LENGTH_SHORT).show();
//        mEnterRoomTime = System.currentTimeMillis();
        mEnterRoomTime = elapsed;
        SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.CALLVIDEO, mEnterRoomTime);

    }

    @Override
    public void onExitRoom(int reason) {
        DemoLog.i(TAG, "onExitRoom " + reason);
        Toast.makeText(App.getAppContext(), "结束通话", Toast.LENGTH_SHORT).show();
        mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
    }

    public void setUISender(ChatLayout layout, Context context) {
        DemoLog.i(TAG, "setUISender: " + layout);
        mUISender = layout;
        if (mCurrentVideoCallStatus == VIDEO_CALL_STATUS_WAITING) {
            boolean success = showIncomingDialingDialog(context);
            if (success) {
                videocallDialog.setUser(mUISender.getChatInfo().getId());
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_BUSY;
            } else {
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                sendVideoCallAction(VIDEO_CALL_ACTION_REJECT, mOnlineCall);
                Toast.makeText(App.getAppContext(), "发起通话失败，没有弹出对话框权限", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onDraw(ICustomMessageViewGroup parent, CustomMessage data, MessageInfo info, String resultdata) {

        if (mUISender.isGroup && mUISender.isChatRoom){
            return;
        }

        if (data.videoState == VIDEO_CALL_ACTION_DIALING || data.videoState == VIDEO_CALL_ACTION_ACCEPTED || data.videoState == VIDEO_CALL_ACTION_LINE_BUSY) {
            info.remove();
            return;
        }
        TextView textView;
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.test_custom_message_av_layout1, null, false);
        parent.addMessageContentView(view);
        if (info.isSelf()) {
            view.setBackgroundResource(R.drawable.shape_chat_main);
        } else {
            view.setBackgroundResource(R.drawable.shap_chat_video_bg);
        }
        if (data == null) {
            DemoLog.i(TAG, "onCalling null data");
            return;
        }
        textView = view.findViewById(R.id.test_custom_message_tv);
        if (info.isSelf()) {
            textView.setTextColor(Color.parseColor("#ffffff"));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                Drawable drawable = App.getAppContext().getDrawable(R.drawable.chatting_icon_call_white);
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(drawable, null, null, null);
            }
        } else {
            textView.setTextColor(Color.parseColor("#000000"));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = App.getAppContext().getDrawable(R.drawable.chatting_icon_call_black);
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(drawable, null, null, null);
            }
        }

        String callingAction = "";

        switch (data.videoState) {
            // 新接一个电话
            case VIDEO_CALL_ACTION_DIALING:
                callingAction = "请求通话";
                break;
            case VIDEO_CALL_ACTION_SPONSOR_CANCEL:
                callingAction = "取消通话";
                break;
            case VIDEO_CALL_ACTION_REJECT:
                if (info.isSelf()) {
                    callingAction = "已拒绝";
                } else {
                    callingAction = "对方已取消";
                }

                break;
            case VIDEO_CALL_ACTION_SPONSOR_TIMEOUT:
                if (!info.isSelf()) {
                    callingAction = "对方未接听";
                    view.setAlpha(0.8f);
                } else {
                    callingAction = "无应答";
                }
                break;
            case VIDEO_CALL_ACTION_ACCEPTED:
                callingAction = "开始通话";
                break;
            case VIDEO_CALL_ACTION_HANGUP:


                System.currentTimeMillis();
                if (info.getMessage() != null) {
                    String message = info.getMessage();

                }
                SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.CALLVIDEO, mEnterRoomTime);
                callingAction = "通话时长 " + DateTimeUtil.secToTime(data.duration);

                break;
            case VIDEO_CALL_ACTION_LINE_BUSY:
                callingAction = "正在通话中";
                break;
            default:
                DemoLog.e(TAG, "unknown data.action: " + data.videoState);
                callingAction = "不能识别的通话指令";
                break;
        }
        textView.setText(callingAction);
    }

    public void createVideoCallRequest(Context context) {
        // 显示通话UI  在这里发起视频通话
        boolean success = showOutgoingDialingDialog(context);
        if (success) {
            mCurrentVideoCallStatus = VIDEO_CALL_STATUS_BUSY;
            assembleOnlineCall(null);
            sendVideoCallAction(VIDEO_CALL_ACTION_DIALING, mOnlineCall);
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(mVideoCallOutgoingTimeOut, VIDEO_CALL_OUT_GOING_TIME_OUT);
        } else {
            Toast.makeText(App.getAppContext(), "发起通话失败，没有弹出对话框权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void hangup() {
        DemoLog.i(TAG, "hangup");
        mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
        sendVideoCallAction(VIDEO_CALL_ACTION_HANGUP, mOnlineCall);
    }

    private void enterRoom() {

        final Intent intent = new Intent(App.getAppContext(), TRTCActivity.class);
        // intent.putExtra(TRTCActivity.KEY_ROOM_ID, mOnlineCall.requestUser);
        intent.putExtra(TRTCActivity.KEY_ROOM_ID, mOnlineCall.roomID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getAppContext().startActivity(intent);
    }

    public void sendVideoCallAction(int action, CustomMessage roomInfo) {
        DemoLog.i(TAG, "sendVideoCallAction action: " + action
                + " call_id: " + roomInfo.requestUser
                + " room_id: " + roomInfo.roomID
                + " partner: " + roomInfo.getPartner());
        Videochatbean videochatbean = new Videochatbean();
        videochatbean.setVersion(JSON_VERSION_3_ANDROID_IOS_TRTC);
        videochatbean.setRequestUser(roomInfo.requestUser);
        videochatbean.setSendType("1");
        videochatbean.setRoomID(roomInfo.roomID);
        videochatbean.setVideoState(action);
        if (action == VIDEO_CALL_ACTION_HANGUP) {
            videochatbean.duration = (System.currentTimeMillis() - mEnterRoomTime + 500) / 1000;
        }
        Gson gson = new Gson();
        CustomMessage message = new CustomMessage();
        message.version = JSON_VERSION_3_ANDROID_IOS_TRTC;
        message.requestUser = roomInfo.requestUser;
        message.roomID = Utils.ROOMID;
        message.videoState = action;
        message.setSendType("1");
        String data = gson.toJson(videochatbean);

//        message.invited_list = roomInfo.invited_list;
        if (action == VIDEO_CALL_ACTION_HANGUP) {
            message.duration = (int) (System.currentTimeMillis() - mEnterRoomTime + 500) / 1000;
        }
        //String data = gson.toJson(message);
        MessageInfo info = MessageInfoUtil.buildCustomMessage(data, 1);
        if (TextUtils.equals(mOnlineCall.getPartner(), roomInfo.getPartner())) {
//            mUISender.sendMessage(info, false);
            TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, roomInfo.getPartner());
            con.sendMessage(info.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onError(int code, String desc) {
                    DemoLog.i(TAG, "sendMessage fail:" + code + "=" + desc);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    TUIKitLog.i(TAG, "sendMessage onSuccess");
                    EventBus.getDefault().post(new UpdateMessageEvent(false));
                }
            });
        } else {

            TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, roomInfo.getPartner());
            con.sendMessage(info.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onError(int code, String desc) {
                    DemoLog.i(TAG, "sendMessage fail:" + code + "=" + desc);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    TUIKitLog.i(TAG, "sendMessage onSuccess");
                }
            });
        }

        stopService();

    }

    private void stopService() {
        if (ServiceUtils.isServiceRunning(App.getAppContext(),"com.jfkj.im.service.VideoFloatService")){
            Intent intent = new Intent(App.getAppContext(), VideoFloatService.class);
            //终止FloatViewService
            App.getAppContext().stopService(intent);
        }
    }


    private String createCallID() {
//        final String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        sb.append(GenerateTestUserSig.SDKAPPID).append("-").append(TIMManager.getInstance().getLoginUser()).append("-");
//        for (int i = 0; i < 32; i++) {
//            int index = random.nextInt(CHARS.length());
//            sb.append(CHARS.charAt(index));
//        }
        return TIMManager.getInstance().getLoginUser().toString();
    }

    private void assembleOnlineCall(CustomMessage roomInfo) {
        mOnlineCall = new CustomMessage();

        if (roomInfo == null) {
            mOnlineCall.requestUser = createCallID();
            mOnlineCall.roomID = Utils.ROOMID;
            mOnlineCall.invited_list = new String[]{mUISender.getChatInfo().getId()};
            mOnlineCall.setPartner(mUISender.getChatInfo().getId());
        } else {
            mOnlineCall.requestUser = roomInfo.requestUser;
            mOnlineCall.roomID = roomInfo.roomID;
            mOnlineCall.invited_list = roomInfo.invited_list;
            mOnlineCall.setPartner(roomInfo.getPartner());

        }
    }

    public void onNewMessage(List<TIMMessage> msgs) {
        Log.v("--->onNewMessage", msgs.size() + msgs.toString());

        CustomMessage data = CustomMessage.convert2VideoCallData(msgs);
        if (msgs.size() == 1) {
            TIMMessage timMessage = msgs.get(0);
            MessageInfo messageInfo = MessageInfoUtil.TIMMessage2MessageInfo(timMessage, true).get(0);
            Log.v("--->onNewMessagegetData", "MSG_TYPE_CUSTOM=" + MessageInfo.MSG_TYPE_CUSTOM + ",getMsgType=" + messageInfo.getMsgType());
            if (messageInfo.getMsgType() == 256) {
                TIMCustomElem elem = (TIMCustomElem) messageInfo.getElement();
                commentarycommentary(new String(elem.getData()));
                if (messageInfo.getFromUser().equals(Utils.SYSTEMID)) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(elem.getData()));

                        if (jsonObject.getString("sendType").equals("1") && jsonObject.getString("type").equals(CustomMessage.CLUB_DELETE_THIRTEEN)) {
                            JSONObject object = new JSONObject(jsonObject.getString("body"));
                            String content = object.getString("subTitle");
//                        EventBus.getDefault().postSticky(new ShowVipUpgradeDialogFragmentEvent(content));
                            showDialog(content);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (messageInfo.getFromUser().equals(Utils.SYSTEMID)) {
                    try {
                        TIMCustomElem elem = (TIMCustomElem) messageInfo.getElement();
                        JSONObject jsonObject = new JSONObject(new String(elem.getData()));
                        if (jsonObject.getString("sendType").equals("1") && jsonObject.getString("type").equals(CustomMessage.CLUB_DELETE_THIRTEEN)) {
                            JSONObject object = new JSONObject(jsonObject.getString("body"));
                            String content = object.getString("subTitle");
//                        EventBus.getDefault().postSticky(new ShowVipUpgradeDialogFragmentEvent(content));
                            showDialog(content);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(messageInfo.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM){

                        TIMMessage timMessage1 = messageInfo.getTIMMessage();
                        if(timMessage1!=null){
                            OkLogger.e(timMessage1.getElementCount() + "***********************");
                            for (int i = 0;i<timMessage1.getElementCount();i++){
                                TIMElem element = timMessage1.getElement(i);


                                try {
                                    TIMCustomElem elem = (TIMCustomElem) element;
                                    JSONObject  jsonObject = new JSONObject(new String(elem.getData()));

                                    if(!TextUtils.isEmpty(jsonObject.getString("sendType")) &&  jsonObject.getString("sendType").equals("5" )&& jsonObject.getString("type").equals(CLUB_AT_5001)){
                                        CustomMessage customMessage = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
                                        List<String> userList =   customMessage.getUserList();

                                        if (userList.contains(TIMManager.getInstance().getLoginUser())) {
                                            SPUtils.getInstance(App.getAppContext()).put(messageInfo.getTIMMessage().getConversation().getPeer(), messageInfo.getTIMMessage().getConversation().getPeer());
                                        }

                                    }
                                }catch (Exception e){
                                    OkLogger.e(e.toString());
                                }


                            }
                        }





                }



            }
        }
        boolean b = (data != null);
        if (data != null) {
            onNewComingCall(data);
        }
        try {
            for (TIMMessage msg : msgs) {
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                if (type == TIMConversationType.System) {
                    Log.i("msg", ",,,,,,,,type,,,,,,,,,," + type);
                    for (int i = 0; i < msg.getElementCount(); i++) {
                        TIMElem t = msg.getElement(i);
                        LoadApplyListUtil.getGroupPendencyList(new TIMValueCallBack<List<TIMGroupPendencyItem>>() {
                            @Override
                            public void onError(int code, String desc) {
                                ToastUtils.showShortToast(desc);
                            }

                            @Override
                            public void onSuccess(List<TIMGroupPendencyItem> timGroupPendencyItems) {
                                if (timGroupPendencyItems.size() > 0) {
                                    EventBus.getDefault().postSticky(new ApplyJoinGroupEvent(true));

                                    SPUtils.getInstance(App.getAppContext()).put(UserInfoManger.getUserId() + "GROUP_JOIN_USER_PROCESSING","" + timGroupPendencyItems.size());


                                    GroupUnreadMessageNumUtils.getConversationNumbers(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                                        @Override
                                        public void onError(int code, String desc) {

                                        }

                                        @Override
                                        public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                                            long unNum = 0;
                                            for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                                                if (timGroupBaseInfo.getGroupId().equals(Urls.i_you_circle) || timGroupBaseInfo.getGroupId().equals(Urls.square_chat)) {
                                                    continue;
                                                }
                                                unNum += GroupMessageCenterUtils.getUnreadMessageNum(type, timGroupBaseInfo.getGroupId());
                                                if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + timGroupBaseInfo.getGroupId() + Utils.NODISTURB, "closenodisturb") != null) {
                                                    if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + timGroupBaseInfo.getGroupId() + Utils.NODISTURB, "closenodisturb") != null) {
                                                        String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + timGroupBaseInfo.getGroupId() + Utils.NODISTURB, "closenodisturb");
                                                        if (!closenodisturb.equals("closenodisturb")) {
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                            EventBus.getDefault().postSticky(new GroupUnreadMessageNumEvent(unNum, ""));
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void showDialog(String contentOld) {
        if (!TextUtils.isEmpty(contentOld)) {
            Log.e(TAG, "showDialog: ------------contentOld " + contentOld );
            String old = contentOld;
            String content = "";
            String vip = "";
            if (old.contains("VIP3") || old.contains("VIP6") || old.contains("VIP14") || old.contains("VIP20") || old.contains("VIP50") || old.contains("VIP100")) {
                //content = old.substring(12, old.indexOf(","));
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(old);
                content = m.replaceAll("").trim();
                vip = "恭喜升级至VIP" + content;
            } else  {
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(old);
                content = m.replaceAll("").trim();
                vip = "恭喜升级至VIP" + content;
            }

            Log.e("msg", "..........content............." + content);

            String bt = "";
            if (TextUtils.equals(content, "3")) {
                bt = "创建俱乐部功能已解锁";
            } else if (TextUtils.equals(content, "6")) {
                bt = "自定义冒险游戏已解锁";
            } else if (TextUtils.equals(content, "14")) {
                bt = "视频聊天功能已解锁";
            } else if (TextUtils.equals(content, "20")) {
                bt = "隐藏在线状态功能已解锁";
            } else if (TextUtils.equals(content, "50")) {
                bt = "隐藏地理位置功能已解锁";
            } else if (TextUtils.equals(content, "100")) {
                bt = "超级俱乐部功能已解锁";
            }
            if (TextUtils.equals(content, "3") || TextUtils.equals(content, "6") || TextUtils.equals(content, "14") || TextUtils.equals(content, "20") || TextUtils.equals(content, "50") || TextUtils.equals(content, "100")) {
                if (UserInfoManger.getGender().equals("2") && old.contains("VIP3")) {//"0未设置性别,1男 2女",
                    VipUpgradeOneDialogFragment vipUpgradeOneDialogFragment = new VipUpgradeOneDialogFragment(true, Gravity.CENTER, vip);
                    vipUpgradeOneDialogFragment.setResponListener(new ResponListener<Boolean>() {
                        @Override
                        public void Rsp(Boolean s) {
                            if (s) {
                                upDate();
                            }
                        }
                    });
                    vipUpgradeOneDialogFragment.show(MyActivityManager.getInstance().getCurrentActivity().getSupportFragmentManager(), "");
                } else {
                    VipUpgradeTwoDialogFragment vipUpgradeTwoDialogFragment = new VipUpgradeTwoDialogFragment(true, Gravity.CENTER, vip, bt);
                    vipUpgradeTwoDialogFragment.setResponListener(new ResponListener<Boolean>() {
                        @Override
                        public void Rsp(Boolean s) {
                            if (s) {
                                upDate();
                            }
                        }
                    });
                    vipUpgradeTwoDialogFragment.show(MyActivityManager.getInstance().getCurrentActivity().getSupportFragmentManager(), "");
                }
            } else {
                VipUpgradeOneDialogFragment vipUpgradeOneDialogFragment = new VipUpgradeOneDialogFragment(true, Gravity.CENTER, vip);
                vipUpgradeOneDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            upDate();
                        }
                    }
                });
                vipUpgradeOneDialogFragment.show(MyActivityManager.getInstance().getCurrentActivity().getSupportFragmentManager(), "");
            }



        }
    }

    public void upDate(){
        EventBus.getDefault().post(new UserInfoEvent(true));
    }

    public static void commentarycommentary(String msg) {

        try {
            JSONObject jsonObject = new JSONObject(msg);
            if (jsonObject.getString("sendType").equals("4")) {
                Log.d("--->Utils.APPIDmsg", Utils.APPID + msg);

                if (jsonObject.getJSONObject("body").getString("userid").equals(Utils.APPID)) {

                    if (jsonObject.getString("type").equals("5") || jsonObject.getString("type").equals("6") || jsonObject.getString("type").equals("7")) {
                        Intent intent = new Intent("praise");
                        App.getAppContext().sendBroadcast(intent);
                    }else if (jsonObject.getString("type").equals("100")){
//                        Intent intent = new Intent("unlikeMessage");
//                        App.getAppContext().sendBroadcast(intent);

                        EventBus.getDefault().post("unlikeMessage");

                    }
                }

                if (jsonObject.getString("type").equals("7")) {
                    Intent intent = new Intent("dynamic");
                    App.getAppContext().sendBroadcast(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onNewMessage(List<TIMMessage> msgs, Context context) {
        List<RedPackageCustom> messageList = new ArrayList<>();
        CustomMessage customMessage = null;
        int num = SPUtils.getInstance(context).getInt("numCenter");
        if (msgs != null) {
            for (TIMMessage msg : msgs) {
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                if (type != TIMConversationType.C2C) {
                    continue;
                }
                List<MessageInfo> list = MessageInfoUtil.TIMMessage2MessageInfo(msg, false);
                if (list == null || list.size() == 0) {
                    return;
                }
                for (MessageInfo info : list) {

                    if (info.getMsgType() != MessageInfo.MSG_TYPE_CUSTOM) {
                        continue;
                    }
                    // 获取到自定义消息的json数据
                    if (!(info.getElement() instanceof TIMCustomElem)) {
                        continue;
                    }
                    TIMCustomElem elem = (TIMCustomElem) info.getElement();
                    // 自定义的json数据，需要解析成bean实例
                    try {
                        customMessage = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
                        if (customMessage.getPackageCustom() != null && customMessage.getPackageCustom().getCusType().equals("9")) {
                            messageList.add(customMessage.getPackageCustom());
                            if (num == -1) {
                                num = 0;
                                SPUtils.getInstance(context).put("numCenter", num);
                                String json = new Gson().toJson(messageList);
                                SPUtils.getInstance(context).put("data" + SPUtils.getInstance(context).getInt("numCenter"), json);
                            } else {
                                List<RedPackageCustom> redPackageCustoms = new ArrayList<>();
                                for (int i = 0; i <= num; i++) {
                                    redPackageCustoms.addAll(JSON.parseArray(SPUtils.getInstance(context).getString("data" + i), RedPackageCustom.class));
                                }
                                for (int i = 0; i < redPackageCustoms.size(); i++) {
                                    for (int j = 0; j < messageList.size(); j++) {
                                        if (!TextUtils.equals(redPackageCustoms.get(i).getGroupId(), messageList.get(j).getGroupId())
                                                && !TextUtils.equals(redPackageCustoms.get(i).getSendId(), messageList.get(j).getSendId())) {
                                            num++;
                                            SPUtils.getInstance(context).put("numCenter", num);
                                            String json = new Gson().toJson(messageList);
                                            SPUtils.getInstance(context).put("data" + SPUtils.getInstance(context).getInt("numCenter"), json);
                                        }
                                    }
                                }
                            }
                        }


                    } catch (Exception e) {
                        DemoLog.e(TAG, "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
                    }
                }
            }
        }
    }

    private void onNewComingCall(CustomMessage message) {

        Log.d("--->onNewComingCall", "onNewComingCall current state: " + mCurrentVideoCallStatus
                + " requestUser action: " + message.videoState
                + " coming requestUser: " + message.requestUser
                + " coming room_id: " + message.roomID
                + " current room_id: " + (mOnlineCall == null ? null : mOnlineCall.roomID)
                + " coming sendType: " + message.sendType
                + " coming version: " + message.version
        );

        switch (message.videoState) {
            case VIDEO_CALL_ACTION_DIALING:// 0
                if (mCurrentVideoCallStatus == VIDEO_CALL_STATUS_FREE) {
                    mCurrentVideoCallStatus = VIDEO_CALL_STATUS_WAITING;
                    startC2CConversation(message);
                    assembleOnlineCall(message);
                } else {
                    sendVideoCallAction(VIDEO_CALL_ACTION_LINE_BUSY, message);
                }
                break;
            case VIDEO_CALL_ACTION_SPONSOR_CANCEL:
                if (mCurrentVideoCallStatus != VIDEO_CALL_STATUS_FREE && TextUtils.equals(message.requestUser, mOnlineCall.requestUser)) {
                    dismissDialog();
                    mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                }
                break;
            case VIDEO_CALL_ACTION_REJECT:// 2
                if (mCurrentVideoCallStatus != VIDEO_CALL_STATUS_FREE && TextUtils.equals(message.requestUser, mOnlineCall.requestUser)) {
                    dismissDialog();
                    mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                }
                break;
            case VIDEO_CALL_ACTION_SPONSOR_TIMEOUT:// 3
                if (mCurrentVideoCallStatus != VIDEO_CALL_STATUS_FREE && TextUtils.equals(message.requestUser, mOnlineCall.requestUser)) {
                    dismissDialog();
                    mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                }
                break;
            case VIDEO_CALL_ACTION_ACCEPTED:// 4
                if (mCurrentVideoCallStatus != VIDEO_CALL_STATUS_FREE && TextUtils.equals(message.requestUser, mOnlineCall.requestUser)) {
                    dismissDialog();
                }
                assembleOnlineCall(message);
                enterRoom();
                break;
            case VIDEO_CALL_ACTION_HANGUP:// 5
                dismissDialog();
                mTRTCCloud.exitRoom();
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                break;
            case VIDEO_CALL_ACTION_LINE_BUSY:// 6
                if (mCurrentVideoCallStatus == VIDEO_CALL_STATUS_BUSY && TextUtils.equals(message.requestUser, mOnlineCall.requestUser)) {
                    dismissDialog();
                }
                break;
            case VIDEO_CALL_ACTION_UNKNOWN:// -1

                break;
            default:
                DemoLog.e(TAG, "unknown data.action: " + message.videoState);
                break;
        }
        stopService();
    }


    private void startC2CConversation(CustomMessage message) {
        // 小米手机需要在安全中心里面把demo的"后台弹出权限"打开，才能当应用退到后台时弹出通话请求对话框。
        DemoLog.i(TAG, "startC2CConversation " + message.getPartner());

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.C2C);
        chatInfo.setId(message.getPartner());

        chatInfo.setChatName(message.getSendName());
        chatInfo.setChatRoom(false);
        Intent intent = new Intent(App.getAppContext(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getAppContext().startActivity(intent);
    }

    private boolean showIncomingDialingDialog(Context context) {

        dismissDialog();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mVideoCallIncomingTimeOut, VIDEO_CALL_OUT_INCOMING_TIME_OUT);
        mDialog = new TRTCDialog(App.getAppContext());
        mDialog.setTitle("来电话了");
        mDialog.setPositiveButton("接听", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoLog.i(TAG, "VIDEO_CALL_ACTION_ACCEPTED");
                mHandler.removeCallbacksAndMessages(null);
                sendVideoCallAction(VIDEO_CALL_ACTION_ACCEPTED, mOnlineCall);
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_BUSY;
                enterRoom();
            }
        });
        mDialog.setNegativeButton("拒绝", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoLog.i(TAG, "VIDEO_CALL_ACTION_REJECT");
                mHandler.removeCallbacksAndMessages(null);
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                sendVideoCallAction(VIDEO_CALL_ACTION_REJECT, mOnlineCall);
            }
        });

        videocallDialog = new VideocallDialog(context);
        videocallDialog.setOnClick(new VideocallDialog.OnClick() {
            @Override
            public void onclickrefuse() {
                mHandler.removeCallbacksAndMessages(null);
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_FREE;
                sendVideoCallAction(VIDEO_CALL_ACTION_REJECT, mOnlineCall);
            }

            @Override
            public void onclickaccject() {
                mHandler.removeCallbacksAndMessages(null);
                sendVideoCallAction(VIDEO_CALL_ACTION_ACCEPTED, mOnlineCall);
                mCurrentVideoCallStatus = VIDEO_CALL_STATUS_BUSY;
                enterRoom();
            }
        });


        return videocallDialog.showSystemDialog();
    }

    private boolean showOutgoingDialingDialog(Context context) {
        dismissDialog();
        mDialog = new TRTCDialog(context);
        mDialog.setTitle("等待对方接听");
        previewDialog = new PreviewDialog(context);
        previewDialog.setOnClick(new PreviewDialog.OnClick() {
            @Override
            public void onclickcancel() {
                mHandler.removeCallbacksAndMessages(null);
                sendVideoCallAction(VIDEO_CALL_ACTION_SPONSOR_CANCEL, mOnlineCall);
            }
        });
        return previewDialog.showSystemDialog();
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (videocallDialog != null) {
            videocallDialog.dismiss();
        }
        if (previewDialog != null) {
            previewDialog.dismiss();
        }
        mHandler.removeCallbacksAndMessages(null);

    }
}
