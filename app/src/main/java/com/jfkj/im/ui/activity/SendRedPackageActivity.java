package com.jfkj.im.ui.activity;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.HeadUrlNameUtils;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.RedPackageUtil;
import com.jfkj.im.TIM.redpack.SendRedPackageBean;
import com.jfkj.im.TIM.redpack.chatroom.AnswerRedpackage;
import com.jfkj.im.TIM.redpack.game.GameRedPackageBean;
import com.jfkj.im.TIM.redpack.game.GameUtils;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.SendRedPackageSqure.SendRedPackagePresenter;
import com.jfkj.im.mvp.SendRedPackageSqure.SendRedPackageView;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_CUS_TYPE_TWELVE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;
//大冒险1  真心话2  普通红包

public class SendRedPackageActivity extends BaseActivity<SendRedPackagePresenter> implements TextWatcher, SendRedPackageView {
    SendRedPackagePresenter sendRedPackagePresenter;
    @BindView(R.id.tv_diamonds)
    EditText et_diamonds;
    @BindView(R.id.tv_group_number)
    TextView tv_group_number;
    @BindView(R.id.tv_number)
    EditText et_number;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_send_red_pack)
    TextView tv_send_red_pack;
    @BindView(R.id.tv_tips)
    TextView tv_tips;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.tv_1)
    TextView tv_1;

    private TIMConversation conversation;
    private ChatInfo chatInfo;
    private String type;
    private String gameContent;

    private String nickName;
    private String urlHead;
    private int chatType;
    private ChargeDialog mChargeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniviews();
        getData();
    }

    private void getData() {
        nickName = HeadUrlNameUtils.getHeadName()[0];
        urlHead = HeadUrlNameUtils.getHeadName()[1];
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("recharge");
        sendRedPackagePresenter = new SendRedPackagePresenter(this);
        registerReceiver(broadcastReceiver, intentFilter);
        GroupInfoSetUtils.getGroupMembers(chatInfo.getId(), new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String desc) {
                toastShow(desc);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                tv_group_number.setText("(本群共" + timGroupMemberInfos.size() + "人)");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void iniviews() {
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head);
        type = getIntent().getStringExtra("type");

        if (TextUtils.equals("red", type)) {
            tv_send_red_pack.setText("发红包");
            chatType = getIntent().getIntExtra("chatType", 0);
            if (chatType == 4) {//广场
                et_content.setHint("祝大家财源广进!");
                tv_group_number.setVisibility(View.INVISIBLE);
            } else if (chatType == 2) {
                et_content.setHint("恭喜发财，大吉大利！");
            }
        } else {
            gameContent = getIntent().getStringExtra("content");
//            String content = null;
//            if (gameContent != null) {
//                content = gameContent.replace(" ","");
//            }
//            List<String> list = null;
//            if (content != null) {
//                list = Arrays.asList(content.split(","));
//            }
//            if (list != null) {
//                StringBuilder sub = new StringBuilder();
//                for (String s : list){
//                    sub.append(s).append("\n");
//                }
//                if (TextUtils.equals("1",type)){
//                    et_content.setText("【大冒险】\n"+sub.toString());
//                }else {
//                    et_content.setText("【真心话】\n"+sub.toString());
//                }
//                et_content.setEnabled(false);
//            }
            if (TextUtils.equals("1", type)) {
                et_content.setText("【大冒险】\n" + gameContent);
            } else {
                et_content.setText("【真心话】\n" + gameContent);
            }
            et_content.setEnabled(false);

            tv_1.setText("设置游戏红包奖励");
            tv_send_red_pack.setText("发起冒险");

        }
        chatInfo = (ChatInfo) getIntent().getSerializableExtra("data");
        assert chatInfo != null;
        Log.i("msg", "........-----id-----------" + chatInfo.getId());
        Log.i("msg", ".......loginUser..........." + TIMManager.getInstance().getLoginUser());
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatInfo.getId());
        tv_send_red_pack.setEnabled(false);
        tv_send_red_pack.addTextChangedListener(this);
        et_diamonds.addTextChangedListener(this);
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTvbG();
            }
        });
        et_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_tips.setVisibility(View.GONE);
                }
            }
        });
        et_diamonds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_tips.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setTvbG() {
        if (!TextUtils.isEmpty(getMoney()) && !TextUtils.isEmpty(getNumber())) {
            tv_send_red_pack.setEnabled(true);

            tv_send_red_pack.setAlpha(1f);
//            tv_send_red_pack.setTextColor(ContextCompat.getColor(mActivity,R.color.black));
//            tv_send_red_pack.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_tv_three_bg_gray_s));
        } else {
            tv_send_red_pack.setEnabled(false);
            tv_send_red_pack.setAlpha(0.5f);

//            tv_send_red_pack.setTextColor(ContextCompat.getColor(mActivity,R.color.white));
//            tv_send_red_pack.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_tv_three_bg_gray));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_red_package;
    }

    @Override
    public SendRedPackagePresenter createPresenter() {
        return sendRedPackagePresenter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        if (s.toString().length() > 0 && Long.parseLong(s.toString()) < 10) {
//            tv_tips.setVisibility(View.VISIBLE);
//        } else {
//            tv_tips.setVisibility(View.GONE);
//        }
        setTvbG();
    }

    public String getMoney() {
        return et_diamonds.getText().toString();
    }

    public String getNumber() {
        return et_number.getText().toString();
    }

    public String getContent() {
        return et_content.getText().toString();
    }

    @OnClick({R.id.tv_send_red_pack, R.id.iv_title_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_send_red_pack:


//                 long diamondsNum =   Long.parseLong(et_diamonds.getText().toString());
//                 long num =   Long.parseLong(et_number.getText().toString());
//                //50    <=  3500/70
//                 if((diamondsNum / 70) > num  ){
//                     tv_tips.setText("红包个数不能超过" + (diamondsNum / 70) + "个" );
//                     tv_tips.setVisibility(View.VISIBLE);
//                 }else{
                    if (TextUtils.equals("red", type)) {
                        //普通红包
                        sendRedPack(type);
                    } else {
                        //游戏红包
                        sendGamePack();
                    }
//                 }

                break;
        }
    }

    private void sendGamePack() {
        progressDialog.show();
        if (TextUtils.isEmpty(getContent())) {
            et_content.setText("游戏红包！");
        }
        new GameUtils(chatInfo.getId())
                .genAdventureGameRedPack(this, chatInfo.getId(), getMoney(), getNumber(), gameContent, type, new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                        GameRedPackageBean gameRedPackageBean = (GameRedPackageBean) data;
                        TIMMessage message = buildGameRedPack(gameRedPackageBean);
                        TIMMessageOfflinePushSettings settings = new TIMMessageOfflinePushSettings();
                        settings.setEnabled(true);

                        settings.setDescr(nickName + "在俱乐部发了一份" + getMoney() + "钻石的冒险游戏，点击立即参与");
                        message.setOfflinePushSettings(settings);
                        //将 elem 添加到消息
                        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
                            @Override
                            public void onError(int code, String desc) {
                                progressDialog.dismiss();
                                Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                                Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                            }

                            @Override
                            public void onSuccess(TIMMessage timMessage) {
                                progressDialog.dismiss();
                                Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        progressDialog.dismiss();
                        toastShow(errMsg);
                    }
                }, new TIMValueCallBack<Boolean>() {
                    @Override
                    public void onError(int code, String desc) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            showPayDialog();
                        }

                    }
                }, new TIMValueCallBack<String>() {
                    @Override
                    public void onError(int code, String desc) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s) {
                        progressDialog.dismiss();
                        tv_tips.setText( s );
                        tv_tips.setVisibility(View.VISIBLE);
                    }
                });
    }

    private TIMMessage buildGameRedPack(GameRedPackageBean gameRedPackageBean) {
        Gson gson = new Gson();

        RedPackageCustom redPackageCustom = new RedPackageCustom();
        redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_TWO);
        redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setGroupId(gameRedPackageBean.getGroupId());
        redPackageCustom.setAvatarUrl(urlHead);
        redPackageCustom.setSendName(nickName);
        redPackageCustom.setSendType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setSendId(gameRedPackageBean.getUserId());
        redPackageCustom.setVIP(UserInfoManger.getUserVipLevel().trim());
        redPackageCustom.setRedId(gameRedPackageBean.getRedId());
        redPackageCustom.setRedDesc(gameRedPackageBean.getSendWord());
        redPackageCustom.setTipsType("0");
//        customMessage.setPackageCustom(redPackageCustom);
        String data = gson.toJson(redPackageCustom);
        MessageInfo info = MessageInfoUtil.buildRedPackCustomMessage(data);
        return info.getTIMMessage();
    }

    private void sendRedPack(String type) {
        progressDialog.show();
        if (TextUtils.equals("red", type)) {
            if (TextUtils.isEmpty(getContent())) {
                if (chatType == 4) {
                    //祝大家财源广进!
                    et_content.setText("祝大家财源广进！");
                } else if (chatType == 2) {
                    et_content.setText("恭喜发财，大吉大利！");
                }
            }
            if (chatType == 4) {//聊天室红包
                sendChatRoomRedPack();

            } else if (chatType == 2) {//普通红包
                sendRedPackFind();

            }
        }
    }

    private void sendChatRoomRedPack() {
        progressDialog.show();
        new RedPackageUtil().sendChatRoomRedPack(chatInfo.getId(), getMoney(), getNumber(), getContent(),
                new TIMValueCallBack<AnswerRedpackage>() {
                    @Override
                    public void onError(int code, String desc) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(AnswerRedpackage answerRedpackage) {
                        if (answerRedpackage != null) {
                            TIMMessage message = buildChatRoomRedPack(answerRedpackage);
                            //将 elem 添加到消息
                            conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
                                @Override
                                public void onError(int code, String desc) {
                                    progressDialog.dismiss();
                                    Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                                    Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                                }

                                @Override
                                public void onSuccess(TIMMessage timMessage) {
                                    progressDialog.dismiss();
                                    Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                                    finish();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                }, new TIMValueCallBack<Boolean>() {
                    @Override
                    public void onError(int code, String desc) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            showPayDialog();
                        }

                    }
                }, new TIMValueCallBack<String>() {
                    @Override
                    public void onError(int code, String desc) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s) {
                        progressDialog.dismiss();
                        tv_tips.setText( s );
                        tv_tips.setVisibility(View.VISIBLE);
                    }
                });
    }

    //广场红包
    private TIMMessage buildChatRoomRedPack(AnswerRedpackage answerRedpackage) {
        Gson gson = new Gson();
        RedPackageCustom redPackageCustom = new RedPackageCustom();
        redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setSendId(answerRedpackage.getUserId());
        redPackageCustom.setCusType(RED_PACKAGE_CUS_TYPE_TWELVE);

        redPackageCustom.setAvatarUrl(urlHead);
        redPackageCustom.setSendName(nickName);
        redPackageCustom.setSendType(RED_PACKAGE_MST_TYPE_THREE);
        redPackageCustom.setVIP(UserInfoManger.getUserVipLevel().trim());
        redPackageCustom.setRedId(answerRedpackage.getRedId());
        redPackageCustom.setRedDesc(answerRedpackage.getSendWord());
        redPackageCustom.setTipsType("");
        String data = gson.toJson(redPackageCustom);
        MessageInfo info = MessageInfoUtil.buildRedPackCustomMessage(data);
        return info.getTIMMessage();
    }

    /**
     * if (TextUtils.equals("red", type)) {
     * tv_tips.setText("单个红包不能低于10钻石；红包个数不能超过" + timGroupMemberInfos.size() + "个");
     * } else if (TextUtils.equals("task", type)) {
     * tv_tips.setText("单个红包不能低于10钻石；红包个数不能超过" + timGroupMemberInfos.size() + "个");
     * } else {
     * tv_tips.setText("单个红包不能低于50钻石；红包个数不能超过" + timGroupMemberInfos.size() + "个");
     * }
     */

    private void sendRedPackFind() {
        progressDialog.show();
        new RedPackageUtil().sendRedPackage(chatInfo.getId(), getMoney(), getNumber(), getContent(), new TIMValueCallBack<SendRedPackageBean>() {
            @Override
            public void onError(int code, String desc) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(SendRedPackageBean sendRedPackageBean) {
                if (sendRedPackageBean != null) {
                    TIMMessage message = buildRedPack(sendRedPackageBean);
//                    String extContent = "ext content";
                    TIMMessageOfflinePushSettings settings = new TIMMessageOfflinePushSettings();
                    settings.setEnabled(true);
//                    settings.setExt(extContent.getBytes());

                    // 设置 iOS 和 Android 通知栏消息的标题和内容。如果想要两个平台通知栏展示的标题和内容不同，可以通过 AndroidSettings 和 IOSSettings 分别设置。
//                    settings.setTitle("I'm title");
                    settings.setDescr(nickName + "在俱乐部发了一个" + getMoney() + "钻石的俱乐部红包，快去抢＞＞");
// 设置离线推送扩展信息
//                    JSONObject object = new JSONObject();
//                    try {
//                        object.put("level", 15);
//                        object.put("task", "TASK15");
//                        settings.setExt(object.toString().getBytes("utf-8"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }

// 设置在 Android 设备上收到消息时的离线配置
//                    TIMMessageOfflinePushSettings.AndroidSettings androidSettings = new TIMMessageOfflinePushSettings.AndroidSettings();
// IM SDK 2.5.3之前的构造方式
// TIMMessageOfflinePushSettings.AndroidSettings androidSettings = settings.new AndroidSettings();
// 设置 Android 通知栏消息的标题和内容
// androidSettings.setTitle("I'm title for android");
// androidSettings.setDesc("I'm desc for android");
// 设置 Android 设备收到消息时的提示音，声音文件需要放置到 raw 文件夹
//                    androidSettings.setSound(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.hualala));
//                    settings.setAndroidSettings(androidSettings);

//设置在 iOS 设备上收到消息时的离线配置
//                    TIMMessageOfflinePushSettings.IOSSettings iosSettings = new TIMMessageOfflinePushSettings.IOSSettings();
//IM SDK 2.5.3之前的构造方式
//TIMMessageOfflinePushSettings.IOSSettings iosSettings = settings.new IOSSettings();
// 设置 iOS 通知栏消息的标题和内容
// iosSettings.setTitle("I'm title for iOS");
// iosSettings.setDesc("I'm desc for iOS");

// 开启 Badge 计数
//                    iosSettings.setBadgeEnabled(true);
// 设置 iOS 收到消息时没有提示音且不振动（IM SDK 2.5.3新增特性）
//iosSettings.setSound(TIMMessageOfflinePushSettings.IOSSettings.NO_SOUND_NO_VIBRATION);
// 设置 iOS 设备收到离线消息时的提示音
//                    iosSettings.setSound("/path/to/sound/file");

                    message.setOfflinePushSettings(settings);
                    //        //将 elem 添加到消息
                    conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int code, String desc) {
                            progressDialog.dismiss();
                            Log.i("msg", ",,,,,,code,,,,,,,,," + code);
                            Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            progressDialog.dismiss();
                            Log.i("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                            finish();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                }
            }
        }, new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int code, String desc) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                progressDialog.dismiss();
                if (aBoolean) {
                    showPayDialog();
                }

            }
        }, new TIMValueCallBack<String>() {
            @Override
            public void onError(int code, String desc) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                tv_tips.setText( s );
                tv_tips.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showPayDialog() {
        PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                "您的余额不足，请及时充值！", "立即充值", true);
        dialog.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
                    ChargeDialog mChargeDialog = new ChargeDialog(mActivity, mActivity);
                    mChargeDialog.show();
                }
            }
        });
        if (getFragmentManager() != null) {
            dialog.show(getSupportFragmentManager(), "");
        }
    }

    //俱乐部和任务红包类型
    public TIMMessage buildRedPack(SendRedPackageBean bean) {
        Gson gson = new Gson();
        RedPackageCustom redPackageCustom = new RedPackageCustom();
        redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setSendId(bean.getUserId());
        if (TextUtils.equals("red", type)) {
            redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_ONE);
        } else if (TextUtils.equals("task", type)) {
            redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_FOUR);
        }

        redPackageCustom.setAvatarUrl(urlHead);
        redPackageCustom.setSendName(nickName);
        redPackageCustom.setSendType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setVIP(UserInfoManger.getUserVipLevel().trim());
        redPackageCustom.setRedId(bean.getRedId());
        redPackageCustom.setRedDesc(bean.getSendWord());
        redPackageCustom.setTipsType("");
        String data = gson.toJson(redPackageCustom);
        MessageInfo info = MessageInfoUtil.buildRedPackCustomMessage(data);
        return info.getTIMMessage();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "recharge":
                    sendRedPackagePresenter.getExchange();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void showList(ExChangeBean bean) {
        mChargeDialog = new ChargeDialog(this, this);
        mChargeDialog.show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
