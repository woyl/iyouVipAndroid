package com.jfkj.im.TIM.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.Systemnotificationbean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.Gifbean;
import com.jfkj.im.TIM.game.AdventureActivity;
import com.jfkj.im.TIM.modules.chat.ChatLayout;
import com.jfkj.im.TIM.modules.chat.base.BaseInputFragment;
import com.jfkj.im.TIM.modules.chat.layout.input.InputLayout;
import com.jfkj.im.TIM.modules.chat.layout.inputmore.InputMoreActionUnit;
import com.jfkj.im.TIM.modules.chat.layout.message.MessageLayout;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.C2CCueController;
import com.jfkj.im.TIM.redpack.CustomEveryDayRedpackageController;
import com.jfkj.im.TIM.redpack.CustomRedPackController;
import com.jfkj.im.TIM.redpack.CustomSquareAtCollection;
import com.jfkj.im.TIM.redpack.CustomSquareRedEnvelopeCollection;
import com.jfkj.im.TIM.redpack.chatroom.CharacterActivity;
import com.jfkj.im.TIM.redpack.chatroom.CustomAnswerController;
import com.jfkj.im.TIM.redpack.chatroom.CustomAnswerRedPackageController;
import com.jfkj.im.TIM.redpack.chatroom.MyCharacterTextActivity;
import com.jfkj.im.TIM.redpack.ice.CustomIceController;
import com.jfkj.im.TIM.redpack.ice.CustomIceRedPacketController;
import com.jfkj.im.TIM.redpack.party.CustomPartyController;
import com.jfkj.im.TIM.redpack.party.InvitePartyController;
import com.jfkj.im.TIM.redpack.party.InvitePartyStatusController;
import com.jfkj.im.TIM.redpack.tips.CustomTipsController;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.game.CustomGameRedPackageController;
import com.jfkj.im.TIM.redpack.game.CustomGameSubRedPackageController;
import com.jfkj.im.TIM.redpack.game.CustomGameTaskController;
import com.jfkj.im.TIM.redpack.game.SubmissionTaskDialogFragment;
import com.jfkj.im.TIM.utils.DemoLog;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.ui.activity.SendRedPackageActivity;
import com.jfkj.im.ui.dialog.Dialoglevel;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.jfkj.im.TIM.helper.CustomMessage.GIF;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTY;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTYSTATUS;
import static com.jfkj.im.TIM.helper.CustomMessage.JSON_VERSION_1_HELLOTIM;
import static com.jfkj.im.TIM.helper.CustomMessage.JSON_VERSION_3_ANDROID_IOS_TRTC;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_EIGHT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ELEVENT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_NINE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_SIX;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TEN;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.REDENVELOPECOLLECTIONCOMPLETED;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_CUS_TYPE_TWELVE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_FIVE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.SINGLECHATREMINDERMESSAGE;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_ACCEPTED;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_DIALING;


/**
 *
 */
public class ChatLayoutHelper {

    private static final String TAG = ChatLayoutHelper.class.getSimpleName();

    private Activity mContext;
    private int flag;//在这里是我设定了 1单聊的  2是群聊的 4是聊天室
    private boolean c2cChat;
    private FragmentManager fragmentManager;
    Dialoglevel dialoglevel;

    public ChatLayoutHelper(Activity context, int flag, FragmentManager fragmentManager) {
        mContext = context;
        this.flag = flag;
        this.fragmentManager = fragmentManager;
        if (flag == 1) {
            c2cChat = false;
        } else {
            c2cChat = true;
        }
        dialoglevel = new Dialoglevel(mContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void customizeChatLayout(final ChatLayout layout, boolean isUpdate){

        CustomAVCallUIController.getInstance().setUISender(layout, mContext);

//        //====== NoticeLayout使用范例 ======//
//        NoticeLayout noticeLayout = layout.getNoticeLayout();
//        noticeLayout.alwaysShow(true);
//        noticeLayout.getContent().setText("现在插播一条广告");
//        noticeLayout.getContentExtra().setText("参看有奖");
//        noticeLayout.setOnNoticeClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.toastShortMessage("赏白银五千两");
//            }
//        });
//
        //====== MessageLayout使用范例 ======//
        MessageLayout messageLayout = layout.getMessageLayout();
//        ////// 设置聊天背景 //////
//        messageLayout.setBackground(new ColorDrawable(0xFFEFE5D4));
//        ////// 设置头像 //////
//        // 设置默认头像，默认与朋友与自己的头像相同
        messageLayout.setAvatar(R.mipmap.icon_faxian_zhanweitu);
//        // 设置头像圆角
        messageLayout.setAvatarRadius(50);
//        // 设置头像大小
        messageLayout.setAvatarSize(new int[]{35, 35});
//
//        ////// 设置昵称样式（对方与自己的样式保持一致）//////
//        messageLayout.setNameFontSize(12);
//        messageLayout.setNameFontColor(0xFF8B5A2B);
//
//        ////// 设置气泡 ///////
//        // 设置自己聊天气泡的背景
//        messageLayout.setRightBubble(ContextCompat.getDrawable(mContext, R.drawable.shape_chat_main));
        // 设置朋友聊天气泡的背景
//        messageLayout.setLeftBubble(ContextCompat.getDrawable(mContext,R.drawable.shape_chat_visitor));

//        ////// 设置聊天内容 //////
//        // 设置聊天内容字体字体大小，朋友和自己用一种字体大小
//        messageLayout.setChatContextFontSize(15);
//        // 设置自己聊天内容字体颜色
//        messageLayout.setRightChatContentFontColor(R.color.color_ffffff);
        // 设置朋友聊天内容字体颜色
//        messageLayout.setLeftChatContentFontColor(R.color.black);
//
//        ////// 设置聊天时间 //////
//        // 设置聊天时间线的背景
//        messageLayout.setChatTimeBubble(new ColorDrawable(0xFFE4E7EB));

//        // 设置聊天时间的字体大小
//        messageLayout.setChatTimeFontSize(12);
//        // 设置聊天时间的字体颜色
//        messageLayout.setChatTimeFontColor(0xFF7E848C);
//
//        ////// 设置聊天的提示信息 //////
//        // 设置提示的背景
//        messageLayout.setTipsMessageBubble(ContextCompat.getDrawable(mContext, R.drawable.shap_tips_fiv_bg));
//        // 设置提示的字体大小
//        messageLayout.setTipsMessageFontSize(11);
//        // 设置提示的字体颜色
//        messageLayout.setTipsMessageFontColor(ContextCompat.getColor(mContext, R.color.line_btn));
//
        // 设置自定义的消息渲染时的回调
        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw(), isUpdate);
//
//        // 新增一个PopMenuAction
//        PopMenuAction action = new PopMenuAction();
//        action.setActionName("test");
//        action.setActionClickListener(new PopActionClickListener() {
//            @Override
//            public void onActionClick(int position, Object data) {
//                ToastUtil.toastShortMessage("新增一个pop action");
//            }
//        });
//        messageLayout.addPopAction(action);
//
//        final MessageLayout.OnItemClickListener l = messageLayout.getOnItemClickListener();
//        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
//            @Override
//            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
//                l.onMessageLongClick(view, position, messageInfo);
//                ToastUtil.toastShortMessage("demo中自定义长按item");
//            }
//
//            @Override
//            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                l.onUserIconClick(view, position, messageInfo);
//                ToastUtil.toastShortMessage("demo中自定义点击头像");
//            }
//        });


        //====== InputLayout使用范例 ======//
        InputLayout inputLayout = layout.getInputLayout();
        inputLayout.setGroup(c2cChat);

        inputLayout.getInputText().setHintTextColor(mContext.getColor(R.color.white));
        inputLayout.getInputText().setTextColor(mContext.getColor(R.color.white));

//        // TODO 隐藏音频输入的入口，可以打开下面代码测试
//        inputLayout.disableAudioInput(true);
//        // TODO 隐藏表情输入的入口，可以打开下面代码测试
//        inputLayout.disableEmojiInput(true);
//        // TODO 隐藏更多功能的入口，可以打开下面代码测试
//        inputLayout.disableMoreInput(true);
//        // TODO 可以用自定义的事件来替换更多功能的入口，可以打开下面代码测试
//        inputLayout.replaceMoreInput(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.toastShortMessage("自定义的更多功能按钮事件");
//                MessageInfo info = MessageInfoUtil.buildTextMessage("自定义的消息");
//                layout.sendMessage(info, false);
//            }
//        });
//        // TODO 可以用自定义的fragment来替换更多功能，可以打开下面代码测试
//        inputLayout.replaceMoreInput(new CustomInputFragment().setChatLayout(layout));
//
//        // TODO 可以disable更多面板上的各个功能，可以打开下面代码测试
//        inputLayout.disableCaptureAction(true);
//        inputLayout.disableSendFileAction(true);
//        inputLayout.disableSendPhotoAction(true);
//        inputLayout.disableVideoRecordAction(true);
        if (flag == 4) {
            //在广场页面隐藏发送的某些功能
            inputLayout.disableCaptureAction(true);
            inputLayout.disableSendPhotoAction(true);
            inputLayout.disableVideoRecordAction(true);
        }
        // TODO 可以自己增加一些功能，可以打开下面代码测试
//        // 这里增加一个视频通话
//        InputMoreActionUnit videoCall = new InputMoreActionUnit();
//        videoCall.setIconResId(R.mipmap.chatting_btn_video_gray);
//        videoCall.setTitleId(R.string.video_call);
//        videoCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Integer.parseInt(UserInfoManger.getUserVipLevel()) >= 14) {
//                    CustomAVCallUIController.getInstance().createVideoCallRequest(mContext);
//                } else {
//                    dialoglevel.show();
//                    dialoglevel.setClick(new Dialoglevel.onClick() {
//                        @Override
//                        public void click(View view) {
//                            dialoglevel.dismiss();
//                        }
//                    });
//                }
//
//            }
//        });
//        if (flag == 1) {
//            inputLayout.addAction(videoCall);
//        }


        // 增加一个欢迎提示富文本
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.custom);
        unit.setTitleId(R.string.test_custom_action);
        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                CustomMessage customMessage = new CustomMessage();
                String data = gson.toJson(customMessage);
                MessageInfo info = MessageInfoUtil.buildCustomMessage(data, 2);
                layout.sendMessage(info, false);
            }
        });
        //     inputLayout.addAction(unit);

        InputMoreActionUnit read = new InputMoreActionUnit();
        read.setIconResId(R.mipmap.chatting_btn_sendmoney_red);
        read.setTitleId(R.string.read_package);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SendRedPackageActivity.class)
                        .putExtra("data", layout.getChatInfo())
                        .putExtra("type", "red")
                        .putExtra("chatType", flag));
            }
        });
        if (flag == 2 || flag == 4) {
            inputLayout.addAction(read);
        }

        InputMoreActionUnit game = new InputMoreActionUnit();
        game.setIconResId(R.mipmap.chatting_btn_game_nor);
        game.setTitleId(R.string.game);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, AdventureActivity.class).putExtra("data", layout.getChatInfo()));
            }
        });
        if (flag == 2) {
            inputLayout.addAction(game);
        }

        InputMoreActionUnit characterText = new InputMoreActionUnit();
        characterText.setIconResId(R.mipmap.chatting_btn_test_nor);
        characterText.setTitleId(R.string.character_text);
        characterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CharacterActivity.class)
                        .putExtra("data", layout.getChatInfo())
                        .putExtra("type", "1"));
            }
        });
        if (flag == 4) {
            inputLayout.addAction(characterText);
        }
    }

    public static class CustomInputFragment extends BaseInputFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View baseView = inflater.inflate(R.layout.test_chat_input_custom_fragment, container, false);
            Button btn1 = baseView.findViewById(R.id.test_send_message_btn1);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.toastShortMessage("自定义的按钮1");
                    if (getChatLayout() != null) {
                        Gson gson = new Gson();
                        CustomMessage customMessage = new CustomMessage();
                        String data = gson.toJson(customMessage);
                        MessageInfo info = MessageInfoUtil.buildCustomMessage(data, 2);
                        getChatLayout().sendMessage(info, false);
                    }
                }
            });
            Button btn2 = baseView.findViewById(R.id.test_send_message_btn2);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.toastShortMessage("自定义的按钮2");
                    if (getChatLayout() != null) {
                        Gson gson = new Gson();
                        CustomMessage customMessage = new CustomMessage();
                        String data = gson.toJson(customMessage);
                        MessageInfo info = MessageInfoUtil.buildCustomMessage(data, 2);
                        getChatLayout().sendMessage(info, false);
                    }
                }
            });
            return baseView;
        }

    }

    public class CustomMessageDraw implements IOnCustomMessageDrawListener {

        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param parent 自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info   消息的具体信息
         */
        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info) {

            // 获取到自定义消息的json数据
            if (!(info.getElement() instanceof TIMCustomElem)) {
                return;
            }

            TIMCustomElem elem = (TIMCustomElem) info.getElement();
            // 自定义的json数据，需要解析成bean实例
            CustomMessage data = null;
            try {
                Log.d("--->getData1", Utils.APPID + new String(elem.getData()));
                data = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
                if (data.getPackageCustom() == null) {
                    setData(data, info.isSelf());
                } else {
                    setData(data, info.isSelf());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (data != null) {
                if (data.version == JSON_VERSION_1_HELLOTIM) {
                    try {
                        JSONObject rootjsonObject = new JSONObject(new String(elem.getData()));
                        if (rootjsonObject.getString("sendType").equals("3")) {
                            if (rootjsonObject.getString("type").equals("5") || rootjsonObject.getString("type").equals("6") || rootjsonObject.getString("type").equals("7")) {
                                boolean remove = info.getTIMMessage().remove();

                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(data.getSendType())) {
                        switch (data.getSendType()) {
                            case READ_PACKAGE_MSG_TYPE_ONE:
                                switch (data.getType()) {
                                    case READ_PACKAGE_CUS_TYPE_TEN:
                                        CustomIceRedPacketController.onDraw(parent, data, info);
                                        break;
                                    case READ_PACKAGE_CUS_TYPE_NINE:
                                        CustomIceController.onDraw(mContext, parent, data, info);
                                        break;
                                    case INVITETOPARTY:
                                        //
                                        InvitePartyController.onDraw(parent, data, info, mContext);
                                        break;
                                    case INVITETOPARTYSTATUS:
                                        InvitePartyStatusController.onDraw(parent, data, info);
                                        break;
//                                    case SINGLECHATREMINDERMESSAGE:
////                                        OkLogger.e("---------------999--------------");
//                                        C2CCueController.onDraw(parent,data,info);
//                                        break;
                                }
                                break;
                            case READ_PACKAGE_MSG_TYPE_TWO:
                                if (!TextUtils.isEmpty(data.getPackageCustom().getCusType())) {
                                    switch (data.getPackageCustom().getCusType()) {
                                        case READ_PACKAGE_CUS_TYPE_ONE:
                                            CustomRedPackController.onDraw(parent, data, info);
                                            break;
                                        case READ_PACKAGE_CUS_TYPE_TWO:
                                            CustomGameRedPackageController.onDraw(parent, data, info);
                                            break;
                                        case READ_PACKAGE_CUS_TYPE_THREE:
                                            CustomGameTaskController.onDraw(mContext, parent, data, info);
                                            break;
                                        case READ_PACKAGE_CUS_TYPE_FOUR:
                                            CustomGameSubRedPackageController.onDraw(parent, data, info);
                                            break;
                                        case READ_PACKAGE_CUS_TYPE_FIV:
                                            CustomEveryDayRedpackageController.onDraw(parent, data, info);
                                            break;
                                        case READ_PACKAGE_CUS_TYPE_SIX:
                                            CustomTipsController.onDraw(parent, data, info);
                                            break;
                                    }
                                }
                                break;
                            case RED_PACKAGE_MST_TYPE_THREE:
                                if (!TextUtils.isEmpty(data.getCusType())) {
                                    if (RED_PACKAGE_CUS_TYPE_TWELVE.equals(data.getCusType())) {
                                        CustomAnswerRedPackageController.onDraw(parent, data, info);
                                    }

                                } else if (data.getType().equals(READ_PACKAGE_CUS_TYPE_EIGHT)) {
                                    CustomPartyController.onDraw(parent, data, info, mContext);
                                }else if (data.getType().equals(REDENVELOPECOLLECTIONCOMPLETED)){
                                    //红包已经被领取完成
                                    CustomSquareRedEnvelopeCollection.onDraw(parent, data, info, mContext);
                                } else {
                                    CustomAnswerController.onDraw(parent, data, info);
                                }
                                break;

                            case RED_PACKAGE_MST_TYPE_FIVE:

                                switch (data.getType()){
                                    case "5001":
                                        CustomSquareAtCollection.onDraw(parent, data, info, mContext);
                                        break;
                                }

                                break;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(new String(elem.getData()));
                            if (jsonObject.getString("sendType").equals("1")) {
                                if (jsonObject.getString("type").equals("0") || jsonObject.getString("type").equals("4")) {
                                    CustomHelloTIMUIController.onDraw(mContext, parent, data, new String(elem.getData()), info.isSelf(), info);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                } else if (data.version == JSON_VERSION_3_ANDROID_IOS_TRTC) {
                    if (data.videoState == VIDEO_CALL_ACTION_DIALING || data.videoState == VIDEO_CALL_ACTION_ACCEPTED) {
                        TIMMessage timMessage = info.getTIMMessage();
                        timMessage.remove();
                    } else {
                        CustomAVCallUIController.getInstance().onDraw(parent, data, info, new String(elem.getData()));
                    }
                } else {
                    //这个是苹果和安卓视频通话运行的代码//这个对方的
                    // CustomHelloTIMUIController.onDraw(mContext, parent, data, new String(elem.getData()), info.isSelf(), info);
                    //  CustomAVCallUIController.getInstance().onDraw(parent, data, info);
                }
            }
        }
    }


    private void setData(CustomMessage data, boolean isSelf) {
        RedPackageCustom redPackageCustom = new RedPackageCustom();
        if (!TextUtils.isEmpty(data.getSendType())) {
            if (data.getSendType().equals(READ_PACKAGE_MSG_TYPE_TWO)) {
                redPackageCustom.setCusType(data.getCusType());
                redPackageCustom.setSendId(data.getSendId());
                redPackageCustom.setAvatarUrl(data.getAvatarUrl());
                redPackageCustom.setSendName(data.getSendName());
                redPackageCustom.setVIP(data.getVIP());
                redPackageCustom.setSendType(data.getSendType());
                redPackageCustom.setRedId(data.getRedId());
                redPackageCustom.setRedDesc(data.getRedDesc());
                redPackageCustom.setTaskImage(data.getTaskImage());
                redPackageCustom.setTaskUrl(data.getTaskUrl());
                redPackageCustom.setTipsType(data.getTipsType());
                redPackageCustom.setText(data.getText());
                redPackageCustom.setReceiveId(data.getReceiveId());
                redPackageCustom.setRedIsDone(data.getRedIsDone());
                redPackageCustom.setRedSendId(data.getRedSendId());
            } else if (data.getSendType().equals(RED_PACKAGE_MST_TYPE_THREE)) { //性格测试
                if (data.getBody() != null) {
                    if (data.getBody().getMsg() != null) {
                        redPackageCustom.setOrderId(data.getBody().getMsg().getCgameid());
                        redPackageCustom.setCadddate(data.getBody().getCadddate());
                        redPackageCustom.setType(data.getType());
                    } else {
                        redPackageCustom.setOrderId(data.getBody().getOrderId());
                        redPackageCustom.setType(data.getType());
                        redPackageCustom.setCadddate(data.getBody().getCadddate());
                    }
                }
            }
        } else if (data.getPackageCustom() != null) {
            if (data.getPackageCustom().getSendType().equals(READ_PACKAGE_MSG_TYPE_TWO)) {
                data.setCusType(data.getPackageCustom().getCusType());
                data.setSendId(data.getPackageCustom().getSendId());
                data.setAvatarUrl(data.getPackageCustom().getAvatarUrl());
                data.setSendName(data.getPackageCustom().getSendName());
                data.setVIP(data.getPackageCustom().getVIP());
                data.setSendType(data.getPackageCustom().getSendType());
                data.setRedId(data.getPackageCustom().getRedId());
                data.setRedDesc(data.getPackageCustom().getRedDesc());
                data.setTaskImage(data.getPackageCustom().getTaskImage());
                data.setTaskUrl(data.getPackageCustom().getTaskUrl());
                data.setTipsType(data.getPackageCustom().getTipsType());
                data.setText(data.getPackageCustom().getText());
                data.setReceiveId(data.getPackageCustom().getReceiveId());
                data.setRedIsDone(data.getPackageCustom().getRedIsDone());
                data.setRedSendId(data.getPackageCustom().getRedSendId());

                redPackageCustom.setCusType(data.getPackageCustom().getCusType());
                redPackageCustom.setSendId(data.getPackageCustom().getSendId());
                redPackageCustom.setAvatarUrl(data.getPackageCustom().getAvatarUrl());
                redPackageCustom.setSendName(data.getPackageCustom().getSendName());
                redPackageCustom.setVIP(data.getPackageCustom().getVIP());
                redPackageCustom.setSendType(data.getPackageCustom().getSendType());
                redPackageCustom.setRedId(data.getPackageCustom().getRedId());
                redPackageCustom.setRedDesc(data.getPackageCustom().getRedDesc());
                redPackageCustom.setTaskImage(data.getPackageCustom().getTaskImage());
                redPackageCustom.setTaskUrl(data.getPackageCustom().getTaskUrl());
                redPackageCustom.setTipsType(data.getPackageCustom().getTipsType());
                redPackageCustom.setText(data.getPackageCustom().getText());
                redPackageCustom.setReceiveId(data.getPackageCustom().getReceiveId());
                redPackageCustom.setRedIsDone(data.getPackageCustom().getRedIsDone());
                redPackageCustom.setRedSendId(data.getPackageCustom().getRedSendId());
            }
        }

        data.setPackageCustom(redPackageCustom);
    }
}


