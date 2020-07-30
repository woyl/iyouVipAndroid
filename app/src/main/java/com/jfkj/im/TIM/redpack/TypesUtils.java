package com.jfkj.im.TIM.redpack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import com.jfkj.im.App;
import com.jfkj.im.Bean.TipsStartActivity;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.Gifbean;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.redpack.game.TIMmessageBean;
import com.jfkj.im.TIM.redpack.game.TimeTaskUtils;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.okhttp.LoginException;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.ui.activity.ClubmessageActivity;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.EncodingUtils;
import com.jfkj.im.utils.GlideUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StringUtil;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTY;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTYSTATUS;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_10;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_11;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_8;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_9;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_GIF_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ELEVENT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_SEVEN;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_SIX;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ZERO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.SINGLECHATREMINDERMESSAGE;
import static com.jfkj.im.utils.Utils.START_ACTIVITY_CLUB_INFO_CHAT_FRAGMENT;
import static com.jfkj.im.utils.Utils.START_ACTIVITY_DETAIL_CHAT_FRAGMENT;

public class TypesUtils {


    private static String clubName;





    public static void setTipsType(MessageInfo info, TextView mChatTipsTv) {
        // 自定义的json数据，需要解析成bean实例


        TIMCustomElem elem = (TIMCustomElem) info.getElement();

        CustomMessage customMessage = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
        Gifbean gifbean = JSON.parseObject(new String(elem.getData()), Gifbean.class);
        String userId = TIMManager.getInstance().getLoginUser();

        if (customMessage.getStatus() != null) {
            if (customMessage.getStatus().equals(READ_GIF_CUS_TYPE_ONE)) {
                if (!info.isSelf()) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mChatTipsTv.getLayoutParams();
                    layoutParams.width = (int) (ScreenUtil.getScreenWidth(App.getAppContext()) * 0.4);
                    mChatTipsTv.setText("对方拆开了您的礼物");
                    mChatTipsTv.setVisibility(View.VISIBLE);
                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + gifbean.getBody().getOrderId(), true);
                }
            }
        }
        else if (!TextUtils.isEmpty(customMessage.getTipsType()) && customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_SIX)) {
            switch (customMessage.getTipsType()) {
                case READ_PACKAGE_CUS_TYPE_THREE:  //冒险游戏红包领取完成需做任务
                    if (userId.equals(customMessage.getGameUserId())) {
                        TimeTaskUtils.timeTask(info.getTIMMessage().getConversation().getPeer());
                    }

//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mChatTipsTv.getLayoutParams();
//                layoutParams.width = (int) (ScreenUtil.getScreenWidth(App.getAppContext()) * 0.75);


                    LogUtils.eTag("-------------","" + toHtmlString(customMessage.getText())      + "\t\t " + customMessage.getText().toString());
                    mChatTipsTv.setText(Html.fromHtml(toHtmlString(customMessage.getText())));
                    mChatTipsTv.setVisibility(View.VISIBLE);


                    break;
                case READ_PACKAGE_CUS_TYPE_FOUR:  //冒险游戏任务未按时完成，所有人都来鄙视Ta
                case READ_PACKAGE_CUS_TYPE_FIV:
                    LogUtils.eTag("-------------","" + toHtmlString(customMessage.getText()) + "\t\t\t" + customMessage.getText().toString());
                    mChatTipsTv.setText(Html.fromHtml(toHtmlString(customMessage.getText())));
                    mChatTipsTv.setVisibility(View.VISIBLE);

                    break;
                case READ_PACKAGE_CUS_TYPE_ONE:
                case READ_PACKAGE_CUS_TYPE_TWO:
                    String[] receivedIds = customMessage.getReceiveId();
                    List<String> ids = new ArrayList<>(Arrays.asList(receivedIds));
                    FriendsUtils.getUsersProfile(ids, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                        @Override
                        public void onError(int code, String desc) {
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                            if (!TextUtils.isEmpty(customMessage.getTipsType())) {
                                if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_SIX)
                                        && customMessage.getTipsType().equals(READ_PACKAGE_CUS_TYPE_ONE)) {//俱乐部红包被领取时
                                    if (customMessage.getRedIsDone().equals(READ_PACKAGE_CUS_TYPE_ZERO) //发送者自己
                                            || customMessage.getRedIsDone().equals(READ_PACKAGE_CUS_TYPE_ONE)) {//对与发送者是别
                                        paDuClub(customMessage, timUserProfiles, mChatTipsTv, userId, info, 1);
                                    }
                                } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_SIX)
                                        && customMessage.getTipsType().equals(READ_PACKAGE_CUS_TYPE_TWO)) { //领取冒险游戏红包参与提示
                                    if (customMessage.getRedIsDone().equals(READ_PACKAGE_CUS_TYPE_ZERO)
                                            || customMessage.getRedIsDone().equals(READ_PACKAGE_CUS_TYPE_ONE)
                                    ) {//发送者自己
                                        paDuClub(customMessage, timUserProfiles, mChatTipsTv, userId, info, 2);
                                    }
                                }
                            }
                        }
                    });
                    break;
                case READ_PACKAGE_CUS_TYPE_SIX:
                    paDuNewUserSheHuangTips(customMessage, mChatTipsTv, userId, info);
                    break;
                case READ_PACKAGE_CUS_TYPE_SEVEN:
                case INVITETOPARTY:
                    finishEveryDayTask(customMessage,mChatTipsTv,userId,info,customMessage.getTipsType());
                    break;
            }
        }
        else if (!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(INVITE_CUS_TYPE_THREE) &&  customMessage.getSendType().equals(RED_PAGE_TYPE_CODE_FOUR)) {
            mChatTipsTv.setVisibility(View.GONE);
        }
        else if (!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(READ_PACKAGE_CUS_TYPE_ONE) &&  !TextUtils.isEmpty(customMessage.getType()) && customMessage.getType().equals(READ_PACKAGE_CUS_TYPE_ELEVENT)) {
            String message = "";
            mChatTipsTv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(customMessage.getReceivedId())){
                if (customMessage.getReceivedId().equals(TIMManager.getInstance().getLoginUser())) {
                    message = "对方已领取了你的" + TUIKitConstants.covert3HTMLString("奖励红包");
                    //提交的作业
                    mChatTipsTv.setText(Html.fromHtml(message));
                } else {

                    message = "你领取了" + TUIKitConstants.covert3HTMLString( EncodingUtils.decodeUnicode(customMessage.getReceiveName()) ) + "的奖励红包";
                    mChatTipsTv.setText(Html.fromHtml(message));
                }
            }
        }
        else if (!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(READ_PACKAGE_CUS_TYPE_ONE) &&  !TextUtils.isEmpty(customMessage.getType()) && customMessage.getType().equals(INVITETOPARTYSTATUS)){

        if (!TextUtils.isEmpty(customMessage.getAgreeOrReject()) && customMessage.getAgreeOrReject().equals("1")){
                if (info.getFromUser().equals(TIMManager.getInstance().getLoginUser())){
                    mChatTipsTv.setText("你拒绝了对方的报名申请");
                } else {
                    mChatTipsTv.setText("对方拒绝了你的报名申请");
                }
            }else if (!TextUtils.isEmpty(customMessage.getAgreeOrReject()) && customMessage.getAgreeOrReject().equals("2")){
                if (info.getFromUser().equals(TIMManager.getInstance().getLoginUser())){
                    mChatTipsTv.setText("你同意了对方的报名申请");
                } else {
                    mChatTipsTv.setText("对方同意了你的报名申请");
                }
            }
            mChatTipsTv.setVisibility(View.VISIBLE);
        }
        else if (!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(READ_PACKAGE_CUS_TYPE_ONE) && !TextUtils.isEmpty(customMessage.getType()) && customMessage.getType().equals(SINGLECHATREMINDERMESSAGE)) {
            String message = "";
            mChatTipsTv.setVisibility(View.VISIBLE);
            mChatTipsTv.setText("若在聊天中使用涉黄、暴力、辱骂等不文明用语将依法办理和永久封号");
        }
        else if (!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(READ_PACKAGE_CUS_TYPE_THREE) && !TextUtils.isEmpty(customMessage.getTipsType())){
            switch (customMessage.getTipsType()){


                case PIAZZA_DAILY_TOP_TIPS_TYPE_8:
                    //8(魅力榜)

                    if(!TextUtils.isEmpty(customMessage.getUserId())){
                        SettingTvText(PIAZZA_DAILY_TOP_TIPS_TYPE_8,customMessage.getUserId(),mChatTipsTv,customMessage,info);
                    } else{
                        mChatTipsTv.setVisibility(View.GONE);
                    }


                    break;
                case PIAZZA_DAILY_TOP_TIPS_TYPE_9:


                    if(!TextUtils.isEmpty(customMessage.getUserId())){

                        SettingTvText(PIAZZA_DAILY_TOP_TIPS_TYPE_9,customMessage.getUserId(),mChatTipsTv,customMessage,info);

                    } else{
                        mChatTipsTv.setVisibility(View.GONE);
                    }



                    break;
                case PIAZZA_DAILY_TOP_TIPS_TYPE_10:
                    mChatTipsTv.setVisibility(View.VISIBLE);

                    clubName = customMessage.getClubName();
                    SettingTvText(PIAZZA_DAILY_TOP_TIPS_TYPE_10,customMessage.getSendId(),mChatTipsTv,customMessage,info);

                    break;
                case PIAZZA_DAILY_TOP_TIPS_TYPE_11:

                    //红包领取人ID 不能为空， 红包发送人ID 不能为空，  并且红包发送人id 等于 当前登录用户ID
                    if(!TextUtils.isEmpty(customMessage.getPackageReceivedId()) &&  !TextUtils.isEmpty(customMessage.getSendId()) && customMessage.getSendId().equals(TIMManager.getInstance().getLoginUser())){
                        SettingTvText(PIAZZA_DAILY_TOP_TIPS_TYPE_11,customMessage.getPackageReceivedId(),mChatTipsTv,customMessage,info);

                    }else{
                        mChatTipsTv.setVisibility(View.GONE);
                    }


                    break;
            }
        }
        else{
            mChatTipsTv.setVisibility(View.GONE);
        }


    }


    private static void SettingTvText(String type,String userId,TextView textView,CustomMessage customMessage,MessageInfo messageInfo){

        //先查询本地缓存
        TIMUserProfile profile = TIMFriendshipManager.getInstance().queryUserProfile(userId);

        if(profile == null){
            //本地缓存为空，查询服务器
            List<String> list = new ArrayList<>();
            list.add(userId);
            TIMFriendshipManager.getInstance().getUsersProfile(list, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int code, String desc) {
                    OkLogger.e(code + "\t\t" + desc);
                }
                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    TIMUserProfile timUserProfile = timUserProfiles.get(0);
                    setTVText(type, timUserProfile.getNickName(),textView,customMessage,messageInfo,userId);
                }
            });

        }else{
            //本地缓存不为空
            setTVText(type,profile.getNickName(),textView,customMessage,messageInfo,userId);
        }

    }


    private static void setTVText(String type,String nikeName,  TextView mChatTipsTv,CustomMessage customMessage,MessageInfo messageInfo,String userId){
        mChatTipsTv.setVisibility(View.VISIBLE);
        switch (type){

            case PIAZZA_DAILY_TOP_TIPS_TYPE_11:



                if(TIMManager.getInstance().getLoginUser().equals(userId)){
                    mChatTipsTv.setText(Html.fromHtml(toHtmlString( "你已领取您的\"广场红包\"")));


                }else{   mChatTipsTv.setText(Html.fromHtml(toHtmlString("\"" + nikeName + "\"" +"已领取您的\"广场红包\"")));

                }

                String extStr = SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+ "extra", "广场");

                messageInfo.setExtra(extStr);


                mChatTipsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChatTipsTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventBus.getDefault().post(new TipsStartActivity(START_ACTIVITY_DETAIL_CHAT_FRAGMENT,customMessage.getPackageReceivedId()));
                            }
                        });
                    }
                });
                break;
            case PIAZZA_DAILY_TOP_TIPS_TYPE_10:

//                mChatTipsTv.setText(Html.fromHtml(toHtmlString("\"" + nikeName+ "\"" + "在"+"\""+clubName+"\""+"名发了一个XXX钻石的红包")));


                String str = "";
                try {
                    double v = Double.parseDouble(customMessage.getDiamondNumber());

                    str =  nikeName + "在" + clubName + "发了一个"+ new Double(v).intValue()  +"钻石的红包";
                }catch (Exception e){
                    str =nikeName + "在" + clubName + "发了一个"+  customMessage.getDiamondNumber()  +"钻石的红包";
                }




                messageInfo.setExtra(SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+"extra","广场"));


                final int end = nikeName.length() + 1;
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                ssb.append(str);

                final int start = 0;
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        //跳用户详情
                        EventBus.getDefault().post(new TipsStartActivity(START_ACTIVITY_DETAIL_CHAT_FRAGMENT,customMessage.getSendId()));
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        //设置文件颜色
                        ds.setColor(Color.parseColor("#FF2B66"));
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }
                },start,nikeName.length(),0);


                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                       EventBus.getDefault().post(new TipsStartActivity(START_ACTIVITY_CLUB_INFO_CHAT_FRAGMENT,customMessage.getClubId()));
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        //设置文件颜色
                        ds.setColor(Color.parseColor("#FF2B66"));
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }
                },end,end+clubName.length(),0);




                mChatTipsTv.setMovementMethod(LinkMovementMethod.getInstance());
                mChatTipsTv.setText(ssb, TextView.BufferType.SPANNABLE);




                break;


            case PIAZZA_DAILY_TOP_TIPS_TYPE_9:

                if(!TextUtils.isEmpty(nikeName)){
                    mChatTipsTv.setText(Html.fromHtml(toHtmlString("\""+nikeName +"\""+"成为了实力榜榜首")));
                }else{
                    mChatTipsTv.setText(customMessage.getText());
                }


                mChatTipsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new TipsStartActivity(START_ACTIVITY_DETAIL_CHAT_FRAGMENT,customMessage.getUserId()));
                    }
                });

                messageInfo.setExtra(SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+"extra","广场"));
                break;

            case PIAZZA_DAILY_TOP_TIPS_TYPE_8:
                if(!TextUtils.isEmpty(nikeName)){
                    mChatTipsTv.setText(Html.fromHtml(toHtmlString("\""+nikeName +"\""+"成为了魅力榜榜首")));
                }else {
                        mChatTipsTv.setText(customMessage.getText());
                }
//                messageInfo.setExtra(customMessage.getText());

                messageInfo.setExtra(SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+"extra","广场"));

                mChatTipsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new TipsStartActivity(START_ACTIVITY_DETAIL_CHAT_FRAGMENT,customMessage.getUserId()));
                    }
                });

                break;
        }
    }


    //12 "是可以咯"完成了俱乐部的"日常任务"
    private static void finishEveryDayTask(CustomMessage customMessage, TextView mChatTipsTv, String userId, MessageInfo info,String type) {
        if (TextUtils.equals(customMessage.getGameUserId(),userId)){
            String content = null;
            if (type.equals(READ_PACKAGE_CUS_TYPE_SEVEN)){
                content = "我发红包代替完成了俱乐部的<font color ='#FF2B66'>日常任务</font>";
            } else  {
                content = "我完成了俱乐部的<font color ='#FF2B66'>日常任务</font>";
            }
            mChatTipsTv.setText(Html.fromHtml(content));
        } else {
            ArrayList<String> content = StringUtil.ReplaceDoubleMarks(customMessage.getText());
            StringBuilder stringBuilder = new StringBuilder();
            int count = 0;
            for (String con :content) {
                count ++;
                stringBuilder.append("<font color ='#FF2B66'>").append(con).append("</font>");
                if (content.size() != count){
                    if (type.equals(READ_PACKAGE_CUS_TYPE_SEVEN)){
                        stringBuilder.append("发红包代替完成了俱乐部的");
                    } else {
                        stringBuilder.append("完成了俱乐部的");
                    }
                }
            }
            mChatTipsTv.setText(Html.fromHtml(stringBuilder.toString()));
        }
        mChatTipsTv.setVisibility(View.VISIBLE);
    }

    private static void paDuNewUserSheHuangTips(CustomMessage customMessage, TextView mChatTipsTv, String userId, MessageInfo info) {
        String[] receivedIds = customMessage.getReceiveId();
        if (receivedIds.length > 0) {
            if (!info.isSelf()) {
                if (receivedIds[0].equals(userId)) {
                    mChatTipsTv.setVisibility(View.VISIBLE);
                    mChatTipsTv.setText(customMessage.getText());
                } else {
                    mChatTipsTv.setVisibility(View.GONE);
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private static void paDuClub(CustomMessage customMessage, List<TIMUserProfile> timUserProfiles,
                                 TextView mChatTipsTv, String userId, MessageInfo info, int type) {
        if (!info.isSelf()) {
            for (TIMUserProfile timUser : timUserProfiles
            ) {
                if (customMessage.getRedSendId().equals(userId) && customMessage.getSendId().equals(timUser.getIdentifier())) {//发送人
                    if (type == 1) {
                        if (customMessage.getRedSendId().equals(customMessage.getSendId())) {
                            String content = "你领取了自己的<font color ='#FF2B66'>俱乐部红包</font>";
                            mChatTipsTv.setText(Html.fromHtml(content));
                        } else {
                            String colors = "'" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "'领取了你的"+TUIKitConstants.covert3HTMLString("俱乐部红包");
                            mChatTipsTv.setText(Html.fromHtml(colors));
                        }
                    } else {
                        if (customMessage.getRedSendId().equals(customMessage.getSendId())) {
                            mChatTipsTv.setText("你参与了自己的冒险游戏");
                        } else {
                            String colors = "" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "参与了你的冒险游戏";
                            mChatTipsTv.setText(Html.fromHtml(colors));
                        }
                    }
                    mChatTipsTv.setVisibility(View.VISIBLE);
                    break;
                } else {//别人
                    if (customMessage.getSendId().equals(userId)) {
                        if (type == 1) {
                            if (!customMessage.getSendId().equals(timUser.getIdentifier())) {
                                String colors = "你领取了'" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "'的"+TUIKitConstants.covert3HTMLString("俱乐部红包");
                                mChatTipsTv.setText(Html.fromHtml(colors));
                                break;
                            }
                        } else {
                            if (!customMessage.getSendId().equals(timUser.getIdentifier())) {
                                String colors = "你参与了" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "的冒险游戏";
                                mChatTipsTv.setText(Html.fromHtml(colors));
                                break;
                            }
                        }
                        mChatTipsTv.setVisibility(View.VISIBLE);
                    } else {
                        mChatTipsTv.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        }
    }

    public static void showTips(CustomMessage customMessage, List<TIMUserProfile> timUserProfiles, String userId, MessageInfo info, int type) {
        if (!info.isSelf()) {
            for (TIMUserProfile timUser : timUserProfiles
            ) {
                if (customMessage.getRedSendId().equals(userId) && customMessage.getSendId().equals(timUser.getIdentifier())) {//发送人
                    if (type == 1) {
                        if (customMessage.getRedSendId().equals(customMessage.getSendId())) {
                            info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                            info.setExtra("你领取了自己的"+TUIKitConstants.covert3HTMLString("俱乐部红包"));
                        } else {
                            String colors = "'" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "'领取了你的"+TUIKitConstants.covert3HTMLString("俱乐部红包");
                            info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                            info.setExtra(timUser.getNickName() + "领取了你的俱乐部红包");
                        }
                    } else {
                        if (customMessage.getRedSendId().equals(customMessage.getSendId())) {
                            info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                            info.setExtra("你参与了自己的冒险游戏");
                        } else {
                            String colors = "" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "参与了你的冒险游戏";
                            info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                            info.setExtra(timUser.getNickName() + "参与了你的冒险游戏");
                        }
                    }
                    break;
                } else {//别人
                    if (customMessage.getSendId().equals(userId)) {
                        if (type == 1) {
                            if (!customMessage.getSendId().equals(timUser.getIdentifier())) {
                                String colors = "你领取了'" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "'的"+TUIKitConstants.covert3HTMLString("俱乐部红包");
                                info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                info.setExtra("你领取了" + timUser.getNickName() + "的"+TUIKitConstants.covert3HTMLString("俱乐部红包"));
                                break;
                            }
                        } else {
                            if (!customMessage.getSendId().equals(timUser.getIdentifier())) {
                                String colors = "你参与了" + TUIKitConstants.covert3HTMLString(timUser.getNickName()) + "的冒险游戏";
                                info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                info.setExtra("你参与了" + timUser.getNickName() + "的冒险游戏");
                                break;
                            }
                        }

                    } else {
                        info.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                        info.setExtra("[红包]");
                        break;
                    }
                }
            }
        }
    }





    public  static   String toHtmlString(String str){
        String htmlStr = "<font color=\"#BBBBBB\">";
        int numMark = 0;

        for (int i = 0 ;i<str.length();i++){

            if(str.charAt(i) == '\"'){
                numMark++;
                if(numMark%2==0){
                    htmlStr += "</font>";
                    htmlStr += "<font color=\"#BBBBBB\">";
                }else{
                    htmlStr += "</font>";
                    htmlStr += "<font color=\"#FF2B66\">";
                }

            }else{
                htmlStr +=str.charAt(i);
            }

            if(i  == str.length()-1){
                //最后一位
                htmlStr += "</font>";
            }
        }

        return htmlStr;
    }



}

