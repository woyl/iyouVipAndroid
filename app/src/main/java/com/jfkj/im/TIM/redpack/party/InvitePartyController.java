package com.jfkj.im.TIM.redpack.party;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.Bean.IsFriendBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.event.UpdateMessageEvent;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMCheckFriendResult;
import com.tencent.imsdk.friendship.TIMFriendCheckInfo;
import com.tencent.imsdk.friendship.TIMFriendRelationType;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTYSTATUS;

/**
 * 邀请聚会
 */
public class InvitePartyController {

    private static TextView tvReject;
    private static TextView tvInvite;
    private static LinearLayout layout;
    private static TextView tvResult;
    private static TextView tvMessage;
    private static TextView tvMine;
    private static ImageView ivHead;

    @SuppressLint("SetTextI18n")
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info, Context mContext) {
        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.item_invite_party, null, false);
            parent.addMessageContentView(view);

            layout = view.findViewById(R.id.ll_layout);

            tvResult = view.findViewById(R.id.tv_result);

            tvMessage = view.findViewById(R.id.tv_message);

            tvMine = view.findViewById(R.id.tv_mine);
            ivHead = view.findViewById(R.id.img_1);


            String content = "我报名参加了你发起的见面聚会-" + TUIKitConstants.covert3HTMLString(data.getJoinTime());
            tvMessage.setText(Html.fromHtml(content));
            Glide.with(mContext).load(data.getPagePhoto()).into(ivHead);
            layout.setVisibility(View.GONE);
            tvResult.setVisibility(View.GONE);
            tvMine.setVisibility(View.VISIBLE);

        } else {

            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_chat_left_invite, null, false);
            parent.addMessageContentView(view);

            layout = view.findViewById(R.id.ll_layout);
            tvResult = view.findViewById(R.id.tv_result);
            tvMessage = view.findViewById(R.id.tv_message);
            tvMine = view.findViewById(R.id.tv_mine);
            tvReject = view.findViewById(R.id.tv_reject);
            tvInvite = view.findViewById(R.id.tv_invite);
            ivHead = view.findViewById(R.id.img_1);

            tvMine.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            tvResult.setVisibility(View.GONE);


            Glide.with(mContext).load(data.getPagePhoto()).into(ivHead);
            String content = "我报名参加了你发起的见面聚会-" + TUIKitConstants.covert3HTMLString(data.getJoinTime());
            tvMessage.setText(Html.fromHtml(content));

            if (!SPUtils.getInstance(App.getAppContext()).getString(data.getPartyId() + "" + Utils.APPID + info.getFromUser(), "").equals("")) {
                //有数据
                String string = SPUtils.getInstance(App.getAppContext()).getString(data.getPartyId() + "" + Utils.APPID + info.getFromUser());
                switch (string) {
                    case "1":
                        layout.setVisibility(View.GONE);
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("已拒绝");
                        break;
                    case "2":
                        layout.setVisibility(View.GONE);
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("已邀约");
                        break;
                    case "3":
                        layout.setVisibility(View.GONE);
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("已过期");
                        break;
                    case "4":
                        layout.setVisibility(View.GONE);
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("聚会报名人数已满");
                        break;

                }
            }

            tvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmInvitation("1", data.getSenderId(), data.getPartyId(), data, info);
                }
            });

            tvInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TIMFriendCheckInfo checkinfo = new TIMFriendCheckInfo();
                    List<String> list = new ArrayList<>();
                    list.add(data.getSenderId());
                    checkinfo.setUsers(list);

                    //判断是否是好友
                    TIMFriendshipManager.getInstance().checkFriends(checkinfo, new TIMValueCallBack<List<TIMCheckFriendResult>>() {
                        @Override
                        public void onError(int code, String desc) {

                        }

                        @Override
                        public void onSuccess(List<TIMCheckFriendResult> timCheckFriendResults) {
                            if (timCheckFriendResults.get(0).getResultType() == TIMFriendRelationType.TIM_FRIEND_RELATION_TYPE_NONE) {
                                //不是好友
                                EventBus.getDefault().post(new IsFriendBean(data.getSenderId()));
                            }
                        }
                    });

                    confirmInvitation("2", data.getSenderId(), data.getPartyId(), data, info);
                }
            });
        }
    }

    private static void sendMess(CustomMessage data, MessageInfo info, String s) {
        MessageInfo messageInfo = buildSystemMessageInfo(s);
        //receiverId
        TIMManager.getInstance().getConversation(TIMConversationType.C2C, data.getSenderId()).sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                OkLogger.e(code + "\t\t " + desc);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                //保存点击状态
                SPUtils.getInstance(App.getAppContext()).put(data.getPartyId() + "" + Utils.APPID + info.getFromUser(), s);
                EventBus.getDefault().post(new UpdateMessageEvent(true));
            }
        });

    }


    /**
     * @param type    邀约类型 1:拒绝 2:接受
     * @param userid  接受邀约的用户Id
     * @param partyId 聚会Id
     */
    public static void confirmInvitation(String type, String userid, String partyId, CustomMessage data, MessageInfo info) {

        Map<String, String> querymap = new HashMap<>();

        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, Utils.ANDROID);
        querymap.put(Utils.REQTIME, AppUtils.getReqTime());
        querymap.put("type", type);
        querymap.put("userId", userid);
        querymap.put("partyId", partyId);

        OkGo.<String>post(ApiStores.base_url + "/party/confirmInvitation")
                .tag(App.getAppContext())
                .headers(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkLogger.e(response.body());
                        //0：未操作 1：拒绝过 2:同意过 3:已过期
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            JSONObject object = new JSONObject(jsonObject.getString("data"));
                            String type = object.getString("clickType");
                            if (type.equals("3")) {
                                SPUtils.getInstance(App.getAppContext()).put(data.getPartyId() + "" + Utils.APPID + info.getFromUser(), "3");
                                EventBus.getDefault().post(new UpdateMessageEvent(true));
                            }
                            if (jsonObject.getString("code").equals("200")) {
                                if (type.equals("1")) {
                                    layout.setVisibility(View.GONE);
                                    tvResult.setVisibility(View.VISIBLE);
                                    tvResult.setText("已拒绝");
                                    sendMess(data,info,"1");
                                } else if (type.equals("2")) {
                                    layout.setVisibility(View.GONE);
                                    tvResult.setVisibility(View.VISIBLE);
                                    tvResult.setText("已邀约");
                                    sendMess(data, info, "2");
                                }
                            } else if (jsonObject.getString("code").equals("30001")) {




                                layout.setVisibility(View.GONE);
                                tvResult.setVisibility(View.VISIBLE);
                                tvResult.setText(jsonObject.getString("message"));
                                EventBus.getDefault().post(new UpdateMessageEvent(true));
                                SPUtils.getInstance(App.getAppContext()).put(data.getPartyId() + "" + Utils.APPID + info.getFromUser(), "4");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShortToast("聚会报名人数已满");
                        }
                    }
                });
    }

    /**
     * @param type 1:已经拒绝 2:已经同意
     * @return
     */
    public static MessageInfo buildSystemMessageInfo(String type) {
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();

        JSONObject object = new JSONObject();

        try {
            object.put("sendType", "1");
            object.put("type", INVITETOPARTYSTATUS);
            object.put("agreeOrReject", type);

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
}
