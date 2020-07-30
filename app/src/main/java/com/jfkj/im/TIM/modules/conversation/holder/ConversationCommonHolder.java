package com.jfkj.im.TIM.modules.conversation.holder;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.Systemnotificationbean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.contact.ContactItemBean;
import com.jfkj.im.TIM.modules.conversation.base.ConversationIconView;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.modules.group.info.GroupInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.DateTimeUtil;
import com.jfkj.im.TIM.utils.SharedPreferenceUtils;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.friendship.TIMFriend;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ConversationCommonHolder extends ConversationBaseHolder {

    public ConversationIconView conversationIconView;
    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected TextView tv_level;
    protected ImageView img_super;
    public  ImageView iv_nodisturb;
    public ConversationCommonHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
        tv_level = rootView.findViewById(R.id.tv_level);
        img_super = rootView.findViewById(R.id.img_super);
        iv_nodisturb = rootView.findViewById(R.id.iv_nodisturb);
    }




    @SuppressLint("SetTextI18n")
    public void layoutViews(ConversationInfo conversation, int position) {

        if(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID +conversation.getId()+Utils.NODISTURB, "closenodisturb")!=null){
            if(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID +conversation.getId()+Utils.NODISTURB, "closenodisturb").length()>0){
                String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + conversation.getId() + Utils.NODISTURB, "closenodisturb");
                if(closenodisturb.equals("closenodisturb")){
                    iv_nodisturb.setVisibility(View.GONE);
                    conversation.setUnRead(conversation.getUnRead());

                }else {
                    iv_nodisturb.setVisibility(View.VISIBLE);
                    conversation.setUnRead(0);
                }
            }
        }


        if (!conversation.isGroup()) {
            tv_level.setVisibility(View.VISIBLE);
            if (TIMFriendshipManager.getInstance().queryUserProfile(conversation.getId()) != null) {
                tv_level.setText("VIP" + TIMFriendshipManager.getInstance().queryUserProfile(conversation.getId()).getLevel() + "");
            }
            if (conversation.getId().equals(Utils.SYSTEMID)) {
                tv_level.setVisibility(View.GONE);

                try {
                    List<Object> iconUrlList = conversation.getIconUrlList();
                    iconUrlList.set(0, ApiStores.base_url + "/static/system_iv_new.png");
                }catch (Exception e){

                }


            }
        } else {
            tv_level.setVisibility(View.GONE);
        }

        MessageInfo lastMsg = conversation.getLastMessage();
        if (lastMsg != null && lastMsg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
            if (lastMsg.isSelf()) {
                lastMsg.setExtra("您撤回了一条消息");
            } else if (lastMsg.isGroup()) {
                String message = TUIKitConstants.covert2HTMLString(
                        TextUtils.isEmpty(lastMsg.getGroupNameCard())
                                ? lastMsg.getFromUser()
                                : lastMsg.getGroupNameCard());
                lastMsg.setExtra(message + "撤回了一条消息");
            } else {
                lastMsg.setExtra("对方撤回了一条消息");
            }
        }

//        if (conversation.isTop()) {
//            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.c1E1E1E));
//        } else {
//            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.c1E1E1E));
//        }

        TIMGroupDetailInfo detailInfo = TIMGroupManager.getInstance().queryGroupInfo(conversation.getId());

        if (detailInfo != null) {
            if ( detailInfo.getCustom()!= null && detailInfo.getCustom().get("isSuper") != null) {
                String isSuper = new String(detailInfo.getCustom().get("isSuper"));
                if (isSuper.equals("1")) {
                    img_super.setVisibility(View.VISIBLE);
                } else {
                    img_super.setVisibility(View.GONE);
                }
            }
        }


        titleText.setText(conversation.getTitle());
        messageText.setText("");
        timelineText.setText("");
        if (lastMsg != null) {
            if (lastMsg.getExtra() != null) {

                //1.判断是否是群聊, 在判断是否有人@我。
                if(lastMsg.isGroup()  && !"".equals(SPUtils.getInstance(App.getAppContext()).getString(lastMsg.getTIMMessage().getConversation().getPeer(),""))){
                     messageText.setText(Html.fromHtml("[有人@我] " + lastMsg.getExtra().toString()));
                }else{
                    messageText.setText(Html.fromHtml(lastMsg.getExtra().toString()));
                }



                messageText.setTextColor(rootView.getResources().getColor(R.color.list_bottom_text_bg));
            }
            timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(lastMsg.getMsgTime() * 1000)));
            if (lastMsg.getFromUser().equals(Utils.SYSTEMID)) {
                List<Systemnotificationbean> linkedList = new ArrayList<>();
                if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID) != null) {
                    if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID).length() > 0 && SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID).length() != 4) {
                        linkedList.addAll(JSON.parseArray(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID), Systemnotificationbean.class));
                    }
                }
                TIMElem ele = lastMsg.getElement();
                TIMElemType eleType = ele.getType();
                if (eleType != TIMElemType.SNSTips) {
                    if (linkedList.size() > 0) {
                        if (linkedList.get(0).getBody() != null ) {
                            messageText.setText(linkedList.get(0).getBody().getSubTitle());
                        }
                        messageText.setText("点击查看系统通知");
                    }
                }
            }




        }
        if (conversation.getUnRead() > 0 ) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99) {
                unreadText.setText("99+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            unreadText.setVisibility(View.GONE);
        }

        conversationIconView.setRadius(mAdapter.getItemAvatarRadius());
        if (mAdapter.getItemDateTextSize() != 0) {
            timelineText.setTextSize(mAdapter.getItemDateTextSize());
        }
        if (mAdapter.getItemBottomTextSize() != 0) {
            messageText.setTextSize(mAdapter.getItemBottomTextSize());
        }
        if (mAdapter.getItemTopTextSize() != 0) {
            titleText.setTextSize(mAdapter.getItemTopTextSize());
        }
        if (!mAdapter.hasItemUnreadDot()) {
            unreadText.setVisibility(View.GONE);
        }

        if (conversation.getIconUrlList() != null) {
            conversationIconView.setConversation(conversation);
        }

        //// 由子类设置指定消息类型的views
        layoutVariableViews(conversation, position);
    }

    public void layoutVariableViews(ConversationInfo conversationInfo, int position) {

    }
}
