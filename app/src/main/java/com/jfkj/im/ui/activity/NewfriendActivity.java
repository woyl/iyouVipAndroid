package com.jfkj.im.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import com.jfkj.im.App;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.NewfriendBean;
import com.jfkj.im.Bean.chat.Message;
import com.jfkj.im.Bean.chat.MsgBody;
import com.jfkj.im.Bean.chat.MsgType;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.DismantleRedPackageBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.NewfriendAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.JumpRedPackageDetailsEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.handlerApplyMessage.HandlerApplyMessagePresenter;
import com.jfkj.im.mvp.handlerApplyMessage.HandlerApplyMessageView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMFriendResponse;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMPendencyType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.internal.util.LinkedArrayList;
import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTYSTATUS;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.SINGLECHATREMINDERMESSAGE;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_BOTH;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_COME_IN;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_SEND_OUT;

public class NewfriendActivity extends BaseActivity<HandlerApplyMessagePresenter> implements View.OnClickListener, HandlerApplyMessageView {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    NewfriendAdapter newfriendAdapter;
    LinkedList<NewfriendBean> newfriendBeanList;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    @BindView(R.id.newfriend_clear_iv)
    TextView newfriend_clear_iv;
    private HandlerApplyMessagePresenter handlerApplyMessagePresenter;
    private String sendid;
    private String type;
    private Dialog dialog;
    private View inflate;
    private TextView tv_cancel, tv_enter;

    private int position;
    private List<String> userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        newfriendBeanList = new LinkedList<>();
        newfriendAdapter = new NewfriendAdapter(this);
        userIds = new ArrayList<>();
        back_iv.setOnClickListener(this);
        newfriend_clear_iv.setOnClickListener(this);
        recyclerview.setAdapter(newfriendAdapter);
        handlerApplyMessagePresenter = new HandlerApplyMessagePresenter(this);
        initView();
        show();

    }

    public void show() {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
    }

    public void initView() {
        dialog = new Dialog(mActivity, R.style.dialogstyle);
        inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_newfriend, null);
        dialog.setContentView(inflate);
        tv_cancel = inflate.findViewById(R.id.tv_cancel);
        tv_enter = inflate.findViewById(R.id.tv_enter);
        tv_cancel.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("handlerresult");
        registerReceiver(broadcastReceiver, intentFilter);
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.FRIENDMESSAGE, false);
        if (SPUtils.getInstance(this).getString(Utils.APPID + Utils.ADDFRIEND) != null) {
            if (SPUtils.getInstance(this).getString(Utils.APPID + Utils.ADDFRIEND).length() > 0) {
                String addfriend = SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADDFRIEND);
                newfriendBeanList.addAll(JSON.parseArray(addfriend, NewfriendBean.class));
                newfriendAdapter.setList(newfriendBeanList);
            }
        }
        newfriend();
    }

    public void newfriend() {
        TIMFriendPendencyRequest timFriendPendencyRequest = new TIMFriendPendencyRequest();
        timFriendPendencyRequest.setTimPendencyGetType(TIM_PENDENCY_COME_IN);
        TIMFriendshipManager.getInstance().getPendencyList(timFriendPendencyRequest, new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse timFriendPendencyResponse) {
                List<TIMFriendPendencyItem> items = timFriendPendencyResponse.getItems();
                for (TIMFriendPendencyItem timFriendPendencyItem : items) {
                    userIds.add(timFriendPendencyItem.getIdentifier());
                    if (!timFriendPendencyItem.getAddWording().equals("isTempy")) {
                        //1申请 2 拒绝
                        if (timFriendPendencyItem.getType() == 1) {
                            NewfriendBean newfriendBean = new NewfriendBean();
                            newfriendBean.setUser_name(timFriendPendencyItem.getNickname());
                            newfriendBean.setSendid(timFriendPendencyItem.getIdentifier());
                            newfriendBean.setHint_message(timFriendPendencyItem.getAddWording());
                            newfriendBean.setVisitorname("我");
                            newfriendBean.setVisitorid(UserInfoManger.getUserId());
                            newfriendBean.setType("0");
                            newfriendBean.setTime(timFriendPendencyItem.getAddTime());
                            newfriendBeanList.add(newfriendBean);
                            Collections.sort(newfriendBeanList, new Comparator<NewfriendBean>() {
                                @Override
                                public int compare(NewfriendBean o1, NewfriendBean o2) {
                                    Date date1 = new Date(o1.getTime() * 1000);
                                    Date date2 = new Date(o2.getTime() * 1000);
                                    if (date1.before(date2)) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });
                        } else {
                            NewfriendBean newfriendBean = new NewfriendBean();
                            newfriendBean.setUser_name(timFriendPendencyItem.getNickname());
                            newfriendBean.setSendid(timFriendPendencyItem.getIdentifier());
                            newfriendBean.setHint_message(timFriendPendencyItem.getAddWording());
                            newfriendBean.setVisitorname("我");
                            newfriendBean.setVisitorid(UserInfoManger.getUserId());
                            newfriendBean.setType("2");
                            newfriendBean.setTime(timFriendPendencyItem.getAddTime());
                            newfriendBeanList.add(newfriendBean);
                            Collections.sort(newfriendBeanList, new Comparator<NewfriendBean>() {
                                @Override
                                public int compare(NewfriendBean o1, NewfriendBean o2) {
                                    Date date1 = new Date(o1.getTime() * 1000);
                                    Date date2 = new Date(o2.getTime() * 1000);
                                    if (date1.before(date2)) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });
                        }
                    }
                }
                newfriendAdapter.setList(newfriendBeanList);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newfriend;
    }

    @Override
    public HandlerApplyMessagePresenter createPresenter() {
        return handlerApplyMessagePresenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.newfriend_clear_iv:
                dialog.show();
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_enter:

                newfriendBeanList.clear();
                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADDFRIEND, "");
                newfriendAdapter.setList(newfriendBeanList);
                dialog.dismiss();
                toastShow("清除成功");
                deleFriend();
                break;
        }
    }

    private void deleFriend() {
        TIMFriendshipManager.getInstance().deletePendency(TIM_PENDENCY_COME_IN, userIds, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
//                toastShow(s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
            }
        });
//        TIMFriendshipManager.getInstance().deletePendency(TIM_PENDENCY_SEND_OUT, userIds, new TIMValueCallBack<List<TIMFriendResult>>() {
//            @Override
//            public void onError(int i, String s) {
//                toastShow(s);
//            }
//
//            @Override
//            public void onSuccess(List<TIMFriendResult> timFriendResults) {
//            }
//        });

    }


    /**
     * @param
     * @return
     */
    public static MessageInfo buildSystemMessageInfo() {
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();

        JSONObject object = new JSONObject();

        try {
            object.put("sendType", "1");
            object.put("type", SINGLECHATREMINDERMESSAGE);
            object.put("content", "若在聊天中使用涉黄、暴力、辱骂等不文明用语将依法办理和永久封号");


        } catch (Exception e) {
            e.printStackTrace();
        }
        ele.setData(object.toString().getBytes());
        messageInfo.setMsgTime(System.currentTimeMillis() / 1000);
        TIMMsg.addElement(ele);
        messageInfo.setElement(ele);
        messageInfo.setSelf(true);
        messageInfo.setTIMMessage(TIMMsg);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
        return messageInfo;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case "handlerresult":
                    if (Utils.netWork()) {
                        sendid = intent.getStringExtra(Utils.SENDID);
                        type = intent.getStringExtra(Utils.TYPE);
                        intent.getStringExtra(Utils.SENDID);
                        intent.getStringExtra(Utils.ORDERID);
                        intent.getStringExtra(Utils.TRADEORDERNO);
                        intent.getStringExtra(Utils.TYPE);
                        intent.getStringExtra(Utils.CONTENT);
                        intent.getStringExtra(Utils.HEAD_URL);
                        intent.getStringExtra(Utils.NICKNAME);
                        position = intent.getIntExtra(Utils.POSITION, 0);

                        if (type.equals("1")) {//这里是同意
                            TIMFriendResponse response = new TIMFriendResponse();
                            response.setIdentifier(intent.getStringExtra(Utils.SENDID));
                            response.setResponseType(TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE_AND_ADD);
                            TIMFriendshipManager.getInstance().doResponse(response, new TIMValueCallBack<TIMFriendResult>() {
                                @Override
                                public void onError(int code, String desc) {

                                }

                                @Override
                                public void onSuccess(TIMFriendResult timFriendResult) {

                                    MessageInfo messageInfo = buildSystemMessageInfo();

                                    TIMManager.getInstance().getConversation(TIMConversationType.C2C, sendid).sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                                        @Override
                                        public void onError(int code, String desc) {
                                            OkLogger.e(code + "");
                                        }

                                        @Override
                                        public void onSuccess(TIMMessage timMessage) {
                                            OkLogger.e(timMessage + "");
                                            handlerApplyMessagePresenter.handlerApplyMessage(intent.getStringExtra(Utils.SENDID), intent.getStringExtra(Utils.ORDERID),
                                                    intent.getStringExtra(Utils.TRADEORDERNO), "1");

                                            for (int i = 0; i < newfriendBeanList.size(); i++) {
                                                if (newfriendBeanList.get(i).getSendid().equals(intent.getStringExtra(Utils.SENDID)) && newfriendBeanList.get(i).getVisitorid().equals(intent.getStringExtra(Utils.VISITORID)) && newfriendBeanList.get(i).getType().equals("0") && intent.getIntExtra(Utils.POSITION, -1) == i) {
                                                    newfriendBeanList.get(i).setType("1");
                                                    newfriendAdapter.notifyItemChanged(position);
                                                }
                                            }


                                            OkLogger.e("" + JSON.toJSONString(newfriendBeanList));

                                            SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADDFRIEND, JSON.toJSONString(newfriendBeanList));

                                        }
                                    });


                                }
                            });
                        } else {
                            TIMFriendResponse response = new TIMFriendResponse();
                            response.setIdentifier(intent.getStringExtra(Utils.SENDID));
                            response.setResponseType(TIMFriendResponse.TIM_FRIEND_RESPONSE_REJECT);
                            TIMFriendshipManager.getInstance().doResponse(response, new TIMValueCallBack<TIMFriendResult>() {
                                @Override
                                public void onError(int code, String desc) {

                                }

                                @Override
                                public void onSuccess(TIMFriendResult timFriendResult) {
                                    handlerApplyMessagePresenter.handlerApplyMessage(intent.getStringExtra(Utils.SENDID), intent.getStringExtra(Utils.ORDERID),
                                            intent.getStringExtra(Utils.TRADEORDERNO), "2");

                                    for (int i = 0; i < newfriendBeanList.size(); i++) {
                                        if (newfriendBeanList.get(i).getSendid().equals(intent.getStringExtra(Utils.SENDID)) && newfriendBeanList.get(i).getVisitorid().equals(intent.getStringExtra(Utils.VISITORID)) && newfriendBeanList.get(i).getType().equals("0") && intent.getIntExtra(Utils.POSITION, -1) == i) {
                                            newfriendBeanList.get(i).setType("2");
                                            newfriendAdapter.notifyItemChanged(position);
                                        }
                                    }

                                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADDFRIEND, JSON.toJSONString(newfriendBeanList));
                                }
                            });
                        }
                    } else {
                        toastShow(R.string.nonetwork);//这里是拒绝
                    }

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
    public void handlerApplyMessage(String string) {

    }
}
