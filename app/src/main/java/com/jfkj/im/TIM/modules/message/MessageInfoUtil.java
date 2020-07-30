package com.jfkj.im.TIM.modules.message;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.Systemnotificationbean;
import com.jfkj.im.TIM.bean.Gifbean;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.utils.DateTimeUtil;
import com.jfkj.im.TIM.utils.FileUtil;
import com.jfkj.im.TIM.utils.ImageUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.EncodingUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMGroupTipsElemGroupInfo;
import com.tencent.imsdk.TIMGroupTipsElemMemberInfo;
import com.tencent.imsdk.TIMGroupTipsGroupInfoType;
import com.tencent.imsdk.TIMGroupTipsType;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMSnapshot;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.TIMVideo;
import com.tencent.imsdk.TIMVideoElem;
import com.tencent.openqq.protocol.imsdk.head;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.jfkj.im.TIM.helper.CustomMessage.CLUB_DELETE_THIRTEEN;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTY;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITETOPARTYSTATUS;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_10;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_11;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_8;
import static com.jfkj.im.TIM.helper.CustomMessage.PIAZZA_DAILY_TOP_TIPS_TYPE_9;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_GIF_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_GIF_CUS_TYPE_ZERO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_EIGHT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ELEVENT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_NINE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_SEVEN;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_SIX;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TEN;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ZERO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.REDENVELOPECOLLECTIONCOMPLETED;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_CUS_TYPE_TWELVE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.SINGLECHATREMINDERMESSAGE;


public class MessageInfoUtil {

    public static final String GROUP_CREATE = "group_create";
    public static final String GROUP_DELETE = "group_delete";
    private static final String TAG = MessageInfoUtil.class.getSimpleName();
    private static CustomMessage customMessage;

    /**
     * 创建一条文本消息
     *
     * @param message 消息内容
     * @param list    At的UserID
     * @return
     */
    public static MessageInfo buildTextMessage(String message, List<String> list,String context) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extKey", "ext content");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (list != null && list.size() > 0) {

                try {
                    TIMCustomElem customElem = new TIMCustomElem();

                    com.alibaba.fastjson.JSONObject remindProto = new com.alibaba.fastjson.JSONObject();
                    // notiye 1，提醒部分对象（mentinedTarget）。2，提醒全部。其他不提醒
                    remindProto.put("notiye", "1");
                    JSONArray array = JSONArray.parseArray(com.alibaba.fastjson.JSONObject.toJSON(list).toString());
                    //@ 的用户iD 集合
                    remindProto.put("users", array);
                    remindProto.put("sendType", "5");
                    remindProto.put("type", "5001");
                    remindProto.put("text",context);


                    OkLogger.e("jsonArray" + array);
                    OkLogger.e("json = " + remindProto.toString());
                    customElem.setData(remindProto.toString().getBytes("utf-8"));
                    TIMMsg.addElement(customElem);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
        }else{
            TIMTextElem ele = new TIMTextElem();
            ele.setText(message);
            TIMMsg.addElement(ele);
            info.setElement(ele);
        }

        info.setExtra(message);
        info.setMsgTime(System.currentTimeMillis() / 1000);

        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setMsgType(MessageInfo.MSG_TYPE_TEXT);
        return info;
    }


    public static MessageInfo buildCommemt(String data) {//评论
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.addElement(ele);
        messageInfo.setMsgTime(System.currentTimeMillis() / 1000);
        messageInfo.setElement(ele);
        messageInfo.setSelf(true);
        messageInfo.setTIMMessage(TIMMsg);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        return messageInfo;
    }


    //这里构建系统自定义消息
    public static MessageInfo buildSystemMessageInfo(String sendType, String type, String avatarImage, String title, String subTitle, String time) {//ping
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        JSONObject rootjsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            rootjsonObject.put(Utils.SENDTYPE, sendType);
            rootjsonObject.put(Utils.TYPE, type);
            jsonObject.put("avatarImage", avatarImage);
            jsonObject.put("title", title);
            jsonObject.put("subTitle", subTitle);
            jsonObject.put("time", time);
            rootjsonObject.put(Utils.BODY, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ele.setData(rootjsonObject.toString().getBytes());
        messageInfo.setMsgTime(System.currentTimeMillis() / 1000);
        TIMMsg.addElement(ele);
        messageInfo.setElement(ele);
        messageInfo.setSelf(true);
        messageInfo.setTIMMessage(TIMMsg);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        return messageInfo;
    }


    public static MessageInfo buildPraise(String type, String sendType, String userid) {//ping
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        JSONObject rootjsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            rootjsonObject.put(Utils.SENDTYPE, sendType);
            rootjsonObject.put(Utils.TYPE, type);
            jsonObject.put("userid", userid);
            jsonObject.put("timeId", DataFormatUtils.getTodayDateTime());

            rootjsonObject.put(Utils.BODY, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ele.setData(rootjsonObject.toString().getBytes());
        messageInfo.setMsgTime(System.currentTimeMillis() / 1000);
        TIMMsg.addElement(ele);
        messageInfo.setElement(ele);
        messageInfo.setSelf(true);
        messageInfo.setTIMMessage(TIMMsg);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        return messageInfo;
    }

    /**
     * 创建一条自定义表情的消息
     *
     * @param groupId  自定义表情所在的表情组id
     * @param faceName 表情的名称
     * @return
     */
    public static MessageInfo buildCustomFaceMessage(int groupId, String faceName) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMFaceElem ele = new TIMFaceElem();
        ele.setIndex(groupId);
        ele.setData(faceName.getBytes());
        TIMMsg.addElement(ele);
        info.setExtra("[自定义表情]");
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM_FACE);
        return info;
    }

    /**
     * 创建一条图片消息
     *
     * @param uri        图片URI
     * @param compressed 是否压缩
     * @return
     */
    public static MessageInfo buildImageMessage(final Uri uri, boolean compressed) {

        final MessageInfo info = new MessageInfo();
        final TIMImageElem ele = new TIMImageElem();
        //  info.setDataUri(uri);
        ele.setPath(uri.getPath());
        info.setDataPath(uri.getPath());
        info.setImgWidth(300);
        info.setImgHeight(600);
        TIMMessage TIMMsg = new TIMMessage();
        TIMMsg.setSender(TIMManager.getInstance().getLoginUser());
        TIMMsg.setTimestamp(System.currentTimeMillis());
        if (!compressed) {
            ele.setLevel(0);
        }
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setExtra("[图片]");
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setMsgType(MessageInfo.MSG_TYPE_IMAGE);
        return info;
    }


    /**
     * 创建一条视频消息
     *
     * @param imgPath   视频缩略图路径
     * @param videoPath 视频路径
     * @param width     视频的宽
     * @param height    视频的高
     * @param duration  视频的时长
     * @return
     */
    public static MessageInfo buildVideoMessage(String imgPath, String videoPath, int width, int height, long duration) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMVideoElem ele = new TIMVideoElem();

        TIMVideo video = new TIMVideo();
        video.setDuaration(duration / 1000);
        video.setType("mp4");
        TIMSnapshot snapshot = new TIMSnapshot();

        snapshot.setWidth(width);
        snapshot.setHeight(height);
        ele.setSnapshot(snapshot);
        ele.setVideo(video);
        ele.setSnapshotPath(imgPath);
        ele.setVideoPath(videoPath);

        TIMMsg.addElement(ele);
        Uri videoUri = Uri.fromFile(new File(videoPath));
        info.setSelf(true);
        info.setImgWidth(width);
        info.setImgHeight(height);
        info.setDataPath(imgPath);
        info.setDataUri(videoUri);
        info.setTIMMessage(TIMMsg);
        info.setExtra("[视频]");
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setMsgType(MessageInfo.MSG_TYPE_VIDEO);
        return info;
    }

    /**
     * 创建一条音频消息
     *
     * @param recordPath 音频路径
     * @param duration   音频的时长
     * @return
     */
    public static MessageInfo buildAudioMessage(String recordPath, int duration) {
        MessageInfo info = new MessageInfo();
        info.setDataPath(recordPath);
        TIMMessage TIMMsg = new TIMMessage();
        TIMMsg.setSender(TIMManager.getInstance().getLoginUser());
        TIMMsg.setTimestamp(System.currentTimeMillis() / 1000);
        TIMSoundElem ele = new TIMSoundElem();
        ele.setDuration(duration / 1000);
        ele.setPath(recordPath);
        TIMMsg.addElement(ele);

        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setExtra("[语音]");
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setMsgType(MessageInfo.MSG_TYPE_AUDIO);
        return info;
    }

    /**
     * 创建一条文件消息
     *
     * @param fileUri 文件路径
     * @return
     */
    public static MessageInfo buildFileMessage(Uri fileUri) {
        String filePath = FileUtil.getPathFromUri(fileUri);
        File file = new File(filePath);
        if (file.exists()) {
            MessageInfo info = new MessageInfo();
            info.setDataPath(filePath);
            TIMMessage TIMMsg = new TIMMessage();
            TIMFileElem ele = new TIMFileElem();
            TIMMsg.setSender(TIMManager.getInstance().getLoginUser());
            TIMMsg.setTimestamp(System.currentTimeMillis() / 1000);
            ele.setPath(filePath);
            ele.setFileName(file.getName());
            TIMMsg.addElement(ele);
            info.setSelf(true);
            info.setTIMMessage(TIMMsg);
            info.setExtra("[文件]");
            info.setMsgTime(System.currentTimeMillis() / 1000);
            info.setElement(ele);
            info.setFromUser(TIMManager.getInstance().getLoginUser());
            info.setMsgType(MessageInfo.MSG_TYPE_FILE);
            return info;
        }
        return null;
    }


    /**
     * 创建一条自定义消息
     *
     * @param data 自定义消息内容，可以是任何内容
     * @return
     */
    public static MessageInfo buildCustomMessage(String data, int type) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        if (type == 1) {
            ele.setExt("视频通话".getBytes());
        }
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
//        info.setGroup(false);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        return info;
    }

    public static MessageInfo buildVideocallMessage(String data) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_VIDEO);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        return info;
    }


    /**
     * 创建一条red自定义消息
     *
     * @param data 自定义消息内容，可以是任何内容
     * @return
     */
    public static MessageInfo buildRedPackCustomMessage(String data) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.setCustomInt(0);
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setExtra("[红包]");
        info.setGroup(true);
        return info;
    }

    /**
     * 邀请
     */
    public static MessageInfo buildInviteCustomMessage(String data) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setExtra("[邀请]");
        info.setGroup(true);
        return info;
    }

    /**
     * 邀请
     */
    public static MessageInfo buildTaskCustomMessage(String data) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setExtra("[任务]");
        info.setGroup(true);
        return info;
    }

    /**
     * 性格测试
     */
    public static MessageInfo buildCharacterCustomMessage(String data) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setExtra("[性格测试]");
        info.setGroup(true);
        return info;
    }

    /**
     * 创建一条red自定义消息
     *
     * @param data 自定义消息内容，可以是任何内容
     * @return
     */
    public static MessageInfo buildIceRedPackCustomMessage(String data) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.setCustomInt(0);
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setExtra("[红包]");
        info.setGroup(false);
        return info;
    }

    public static MessageInfo buildgifMessage(String gifid, String gifimgurl, String orderId, String tradeOrderNo, String type, String status, String sendType, String seq, String rand, String timestamp) {
        MessageInfo info = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setExt("礼物".getBytes());
        Gifbean gifbean = new Gifbean();
//        Gifbean.BodyBean bodyBean = new Gifbean.BodyBean();
//        gifbean.setStatus("0");
//        bodyBean.setGiftId(gifid);
//        bodyBean.setGifimgurl(gifimgurl);
//        bodyBean.setOrderId(orderId);

        //

        JSONObject jsonObject = new JSONObject();
        JSONObject rootjsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.ORDERID, orderId);
            jsonObject.put(Utils.GIFIMGURL, gifimgurl);
            jsonObject.put(Utils.TRADEORDERNO, tradeOrderNo);
            jsonObject.put(Utils.GIFTID, gifid);
            rootjsonObject.put(Utils.STATUS, status);
            if (seq.length() > 0) {
                jsonObject.put(Utils.SEQ, seq);
                jsonObject.put(Utils.RAND, rand);
                jsonObject.put(Utils.TIMESTAMP, timestamp);
            }
            rootjsonObject.put(Utils.TYPE, type);
            rootjsonObject.put(Utils.SENDTYPE, sendType);
            rootjsonObject.put(Utils.BODY, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }


        ele.setData(rootjsonObject.toString().getBytes());
        TIMMsg.addElement(ele);
        info.setSelf(true);
        info.setTIMMessage(TIMMsg);

        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setElement(ele);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setExtra("[礼物]");

        info.setFromUser(TIMManager.getInstance().getLoginUser());
        info.setGroup(false);
        return info;
    }

    /**
     * 创建一条群消息自定义内容
     *
     * @param action  群消息类型，比如建群等
     * @param message 消息内容
     * @return
     */
    public static TIMMessage buildGroupCustomMessage(String action, String message) {
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(action.getBytes());
        ele.setExt(message.getBytes());
        TIMMsg.addElement(ele);
        return TIMMsg;
    }


    /**
     * 创建一条群消息自定义内容
     *
     * @param data 群消息类型，比如建群等
     * @param
     * @return
     */
    public static TIMMessage buildGroupCustomBean(String data) {
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        ele.setData(data.getBytes());
        TIMMsg.addElement(ele);
        return TIMMsg;
    }

    /**
     * 把SDK的消息bean列表转化为TUIKit的消息bean列表
     *
     * @param timMessages SDK的群消息bean列表
     * @param isGroup     是否是群消息
     * @return
     */
    public static List<MessageInfo> TIMMessages2MessageInfos(List<TIMMessage> timMessages, boolean isGroup) {
        if (timMessages == null) {
            return null;
        }
        List<MessageInfo> messageInfos = new ArrayList<>();
        for (int i = 0; i < timMessages.size(); i++) {
            TIMMessage timMessage = timMessages.get(i);
            List<MessageInfo> info = TIMMessage2MessageInfo(timMessage, isGroup);
            if (info != null) {
                messageInfos.addAll(info);
            }
        }
        return messageInfos;
    }

    /**
     * 把SDK的消息bean转化为TUIKit的消息bean
     *
     * @param timMessage SDK的群消息bean
     * @param isGroup    是否是群消息
     * @return
     */
    public static List<MessageInfo> TIMMessage2MessageInfo(TIMMessage timMessage, boolean isGroup) {
        if (timMessage == null || timMessage.status() == TIMMessageStatus.HasDeleted || timMessage.getElementCount() == 0) {
            return null;
        }
        List<MessageInfo> list = new ArrayList<>();
        for (int i = 0; i < timMessage.getElementCount(); i++) {
            final MessageInfo msgInfo = new MessageInfo();
            if (ele2MessageInfo(msgInfo, timMessage, timMessage.getElement(i), isGroup) != null) {
                list.add(msgInfo);
            }
            break;
        }
        return list;
    }


    /**
     * 把SDK的消息bean转化为TUIKit的消息bean
     *
     * @param timMessage SDK的群消息bean
     * @param isGroup    是否是群消息
     * @return
     */
    public static List<MessageInfo> TIMMessage2MessageInfo2(TIMMessage timMessage, boolean isGroup) {
        if (timMessage == null || timMessage.status() == TIMMessageStatus.HasDeleted || timMessage.getElementCount() == 0) {
            return null;
        }
        List<MessageInfo> list = new ArrayList<>();
        for (int i = 0; i < timMessage.getElementCount(); i++) {
            final MessageInfo msgInfo = new MessageInfo();
            if (ele2MessageInfo(msgInfo, timMessage, timMessage.getElement(i), isGroup) != null) {
                list.add(msgInfo);
            }
        }
        return list;
    }


    public static boolean isTyping(TIMMessage timMessage) {
        // 如果有任意一个element是正在输入，则认为这条消息是正在输入。除非测试，正常不可能发这种消息。
        for (int i = 0; i < timMessage.getElementCount(); i++) {
            if (timMessage.getElement(i).getType() == TIMElemType.Custom) {
                TIMCustomElem customElem = (TIMCustomElem) timMessage.getElement(i);
                if (isTyping(customElem.getData())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isTyping(byte[] data) {
        try {
            String str = new String(data, "UTF-8");
            MessageTyping typing = new Gson().fromJson(str, MessageTyping.class);
            if (typing != null
                    && typing.userAction == MessageTyping.TYPE_TYPING
                    && TextUtils.equals(typing.actionParam, MessageTyping.EDIT_START)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            TUIKitLog.e(TAG, "parse json error");
        }
        return false;
    }


    private static MessageInfo ele2MessageInfo(final MessageInfo msgInfo, TIMMessage timMessage, TIMElem ele, boolean isGroup) {

        if (msgInfo == null
                || timMessage == null
                || timMessage.status() == TIMMessageStatus.HasDeleted
                || timMessage.getElementCount() == 0
                || ele == null
                || ele.getType() == TIMElemType.Invalid) {
            TUIKitLog.e(TAG, "ele2MessageInfo parameters error");
            return null;
        }

        String sender = timMessage.getSender();
        msgInfo.setTIMMessage(timMessage);
        msgInfo.setElement(ele);
        msgInfo.setGroup(isGroup);
        msgInfo.setId(timMessage.getMsgId());
        msgInfo.setUniqueId(timMessage.getMsgUniqueId());
        msgInfo.setPeerRead(timMessage.isPeerReaded());
        msgInfo.setFromUser(sender);
        if (isGroup) {
            TIMGroupMemberInfo memberInfo = timMessage.getSenderGroupMemberProfile();
            if (memberInfo != null && !TextUtils.isEmpty(memberInfo.getNameCard())) {
                msgInfo.setGroupNameCard(memberInfo.getNameCard());
            }
        }
        msgInfo.setMsgTime(timMessage.timestamp());
        msgInfo.setSelf(sender.equals(TIMManager.getInstance().getLoginUser()));

        TIMElemType type = ele.getType();
        if (type == TIMElemType.Custom) {
            TIMCustomElem customElem = (TIMCustomElem) ele;
            String data = new String(customElem.getData());
            Log.e("msg", "....................." + data);
            if (data.equals(GROUP_CREATE)) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_CREATE);
                /**获取自己的信息*/
//                FriendsUtils.querySelfProfile(new TIMValueCallBack<TIMUserProfile>() {
//                    @Override
//                    public void onError(int code, String desc) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(TIMUserProfile timUserProfile) {
//                        String message = TUIKitConstants.covert2HTMLString(
////                        TextUtils.isEmpty(msgInfo.getGroupNameCard())
////                                ? msgInfo.getFromUser()
////                                : msgInfo.getGroupNameCard()) + "创建群组";
//
//                        TextUtils.isEmpty(msgInfo.getGroupNameCard())
//                                ? timUserProfile.getNickName()
//                                : msgInfo.getGroupNameCard()) + "创建群组";
//                        msgInfo.setExtra(message);
//                    }
//                });
                FriendsUtils.getUsersProfileLoaclService(msgInfo.getFromUser(), new TIMValueCallBack<TIMUserProfile>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(TIMUserProfile timUserProfile) {
                        if (timUserProfile != null) {
                            String message = TUIKitConstants.covert2HTMLString(
////                        TextUtils.isEmpty(msgInfo.getGroupNameCard())
////                                ? msgInfo.getFromUser()
////                                : msgInfo.getGroupNameCard()) + "创建群组";
                                    TextUtils.isEmpty(msgInfo.getGroupNameCard())
                                            ? timUserProfile.getNickName()
                                            : msgInfo.getGroupNameCard()) + "创建群组";
                            msgInfo.setExtra(message);
                        }
                    }
                });
            } else if (data.equals(GROUP_DELETE)) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_DELETE);
                msgInfo.setExtra(new String(customElem.getExt()));
            } else {
                if (isTyping(customElem.getData())) {
                    // 忽略正在输入，它不能作为真正的消息展示
                    return null;
                }

                /**
                 *
                 * */
                // 自定义的json数据，需要解析成bean实例

                try {
                    Log.v("--->customElem", new String(customElem.getData()));
                    JSONObject jsonObject = new JSONObject(new String(customElem.getData()));
                    if (jsonObject.getString("sendType").equals("1")) {
                        if (jsonObject.optString("type").equals("7")
                                || jsonObject.optString("type").equals("8")
                                || jsonObject.optString("type").equals("9")
                                || jsonObject.optString("type").equals("10")
                                || jsonObject.optString("type").equals("11")
                                || jsonObject.optString("type").equals(CLUB_DELETE_THIRTEEN)) {
                            if (sender.equals(Utils.SYSTEMID)) {
                                List<Systemnotificationbean> linkedList = new ArrayList<>();
//                                if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID) != null) {
//                                    if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID).length() > 0) {
//                                        linkedList.addAll(JSON.parseArray(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID), Systemnotificationbean.class));
//                                    }
//                                }

                                if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID) != null) {
                                    if (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID).length() > 0) {
                                        if (jsonObject.getString("type").equals("9") || !TextUtils.isEmpty(jsonObject.getString("body"))) {
                                            linkedList.addAll(JSON.parseArray(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID), Systemnotificationbean.class));
                                        } else if (jsonObject.getString("type").equals("7") || !TextUtils.isEmpty(jsonObject.getString("body"))) {
                                            linkedList.addAll(JSON.parseArray(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.SYSTEMID), Systemnotificationbean.class));
                                        }
                                    }
                                }

                                Systemnotificationbean systemnotificationbean = JSON.parseObject(new String(customElem.getData()), Systemnotificationbean.class);
                                if (jsonObject.getString("type").equals("7")
                                        || jsonObject.getString("type").equals("8")
                                        || jsonObject.getString("type").equals("10")
                                        || jsonObject.getString("type").equals(CLUB_DELETE_THIRTEEN)) {
                                    systemnotificationbean.getBody().setId(Utils.SYSTEMID);
                                }
                                linkedList.add(0, systemnotificationbean);
                                Log.v("--->linkedListSYSTEMID", JSON.toJSONString(linkedList));
                                for (int i = 0; i < linkedList.size() - 1; i++) {
                                    for (int j = linkedList.size() - 1; j > i; j--) {
                                        if (linkedList.get(j).getBody() != null && !TextUtils.isEmpty(linkedList.get(j).getBody().getTime())) {
                                            if (linkedList.get(j).getBody().getTime().equals(linkedList.get(i).getBody().getTime())) {
                                                linkedList.remove(j);
                                            }

                                        }
                                    }
                                }
                                SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.SYSTEMID, JSON.toJSONString(linkedList));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    customMessage = new Gson().fromJson(new String(customElem.getData()), CustomMessage.class);
                }catch (Exception e){
                    return null;
                }
                if (customMessage!=null &&!TextUtils.isEmpty(customMessage.getCusType())) {
                    if (!TextUtils.isEmpty(customMessage.getTipsType()) && customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_SIX)) {
                        switch (customMessage.getTipsType()) {
                            case READ_PACKAGE_CUS_TYPE_ONE: //俱乐部红包被领取时
                            case READ_PACKAGE_CUS_TYPE_TWO: //领取冒险游戏红包参与提示
//                                showRedTips(customMessage,msgInfo);
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                msgInfo.setExtra("提示");
                                break;
                            case READ_PACKAGE_CUS_TYPE_THREE:
                            case READ_PACKAGE_CUS_TYPE_SIX:
                            case READ_PACKAGE_CUS_TYPE_FOUR:
                            case READ_PACKAGE_CUS_TYPE_FIV:
                            case READ_PACKAGE_CUS_TYPE_SEVEN://红包代替任务
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                msgInfo.setExtra(customMessage.getText());
                                break;
                            case INVITETOPARTY: //完成俱乐部日常任务
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                msgInfo.setExtra("[每日任务]");
                                break;
                            default:
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                                msgInfo.setExtra("提示");
                                break;
                        }
                    } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_ONE)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[红包]");
                    } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_TWO)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[游戏红包]");
                    } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_THREE)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[任务]");
                    } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_FOUR)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[游戏红包]");
                    } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_FIV)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[每日红包]");
                    } else if (customMessage.getCusType().equals(READ_PACKAGE_CUS_TYPE_SIX)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[红包]");
                    } else if (customMessage.getCusType().equals(RED_PACKAGE_CUS_TYPE_TWELVE)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[红包]");
                    }
                }
                else if (customMessage!=null && !TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(RED_PACKAGE_MST_TYPE_THREE)) {
                    if (!TextUtils.isEmpty(customMessage.getType())) {
                        switch (customMessage.getType()) {
                            case READ_PACKAGE_CUS_TYPE_ONE:
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                                msgInfo.setExtra("[性格测试]");
                                break;
                            case READ_PACKAGE_CUS_TYPE_EIGHT:
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                                msgInfo.setExtra("[见面聚会]");
                                break;
                            case REDENVELOPECOLLECTIONCOMPLETED:
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                                String extraStr = SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+ "extra","广场");
                                msgInfo.setExtra(extraStr);
                                break;

//                            default:
//                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
//                                msgInfo.setExtra("[性格测试]");
//                                break;
                        }
                    } else if (!TextUtils.isEmpty(customMessage.getTipsType()) && customMessage.getTipsType().equals(PIAZZA_DAILY_TOP_TIPS_TYPE_11)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_RED_ENVELOPE);
                        String extraStr = SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat + "extra","广场");
                        msgInfo.setExtra(extraStr);
                    } else if (!TextUtils.isEmpty(customMessage.getTipsType()) && customMessage.getTipsType().equals(PIAZZA_DAILY_TOP_TIPS_TYPE_10)) {
                        msgInfo.setMsgType(MessageInfo.MSG_CLUB_RED_ENVELOPES);
                        String extraStr = SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat + "extra","广场");
                        msgInfo.setExtra(extraStr);
                    } else if (!TextUtils.isEmpty(customMessage.getTipsType()) && customMessage.getTipsType().equals(PIAZZA_DAILY_TOP_TIPS_TYPE_8)) {
                        msgInfo.setMsgType(MessageInfo.MSG_PIAZZA_CHARM_LIST);
                        String extraStr = SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+ "extra","广场");
                        msgInfo.setExtra(extraStr);
                    } else if (!TextUtils.isEmpty(customMessage.getTipsType()) && customMessage.getTipsType().equals(PIAZZA_DAILY_TOP_TIPS_TYPE_9)) {
                        msgInfo.setMsgType(MessageInfo.MSG_PIAZZA_STRENGTH_LIST);
                        String extraStr = SPUtils.getInstance(App.getAppContext()).getString(Urls.square_chat+ "extra","广场");
                        msgInfo.setExtra(extraStr);
                    }
                }
                else if (customMessage!=null &&!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(RED_PAGE_TYPE_CODE_FOUR)) {
                    msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                    msgInfo.setExtra("");
                }
                else if (customMessage!=null && customMessage.getStatus() != null) {//这里是礼物的提示
                    if (customMessage.getStatus().equals(READ_GIF_CUS_TYPE_ONE) && customMessage.getType().equals(READ_PACKAGE_CUS_TYPE_FOUR)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                        if (msgInfo.isSelf()) {
                            msgInfo.setExtra("礼物");
                        } else {
                            msgInfo.setExtra("对方拆开了您的礼物");
                        }
                    } else if (customMessage.getStatus().equals(READ_GIF_CUS_TYPE_ZERO)) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("礼物");
                    }
                } else if (customMessage!=null &&customMessage.getPackageCustom() != null) {
                    if (!TextUtils.isEmpty(customMessage.getPackageCustom().getCusType())) {
                        if (customMessage.getPackageCustom().getCusType().equals(READ_PACKAGE_CUS_TYPE_THREE)) {
                            msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                            msgInfo.setExtra("[任务]");
                        }
                    }
                }
                else if (customMessage!=null &&!TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals(READ_PACKAGE_MSG_TYPE_ONE) && !TextUtils.isEmpty(customMessage.getType())) {
                    switch (customMessage.getType()) {
                        case READ_PACKAGE_CUS_TYPE_ELEVENT:
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + customMessage.getRedId(), RED_PAGE_TYPE_CODE_FOUR);
                            msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + customMessage.getRedId(), READ_PACKAGE_CUS_TYPE_SIX);
                            if (!TextUtils.isEmpty(customMessage.getReceivedId()) && customMessage.getReceivedId().equals(TIMManager.getInstance().getLoginUser())) {
                                //提交的作业
                                msgInfo.setExtra("对方已领取了你的奖励红包");
                            } else {
                                msgInfo.setExtra("你领取了" + EncodingUtils.decodeUnicode(customMessage.getReceiveName()) + "的奖励红包");
                            }
                            break;
                        case READ_PACKAGE_CUS_TYPE_NINE:
                            msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                            msgInfo.setExtra("[破冰任务]");
                            break;
                        case READ_PACKAGE_CUS_TYPE_TEN:
                            msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                            msgInfo.setExtra("[红包]");
                            break;

                        case INVITETOPARTY:
                            msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                            msgInfo.setExtra("[见面聚会]");
                            break;

                        case INVITETOPARTYSTATUS:
                            msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                            if (!TextUtils.isEmpty(customMessage.getAgreeOrReject()) && customMessage.getAgreeOrReject().equals("1")) {
                                if (msgInfo.getFromUser().equals(TIMManager.getInstance().getLoginUser())) {
                                    msgInfo.setExtra("[你拒绝了对方的报名申请]");
                                } else {
                                    msgInfo.setExtra("[对方拒绝了你的报名申请]");
                                }
                            } else if (!TextUtils.isEmpty(customMessage.getAgreeOrReject()) && customMessage.getAgreeOrReject().equals("2")) {
                                if (msgInfo.getFromUser().equals(TIMManager.getInstance().getLoginUser())) {
                                    msgInfo.setExtra("[你同意了对方的报名申请]");
                                } else {
                                    msgInfo.setExtra("[对方同意了你的报名申请]");
                                }
                            }

                            break;
                        case READ_PACKAGE_CUS_TYPE_FOUR:
                            if (customMessage.getStatus() != null && customMessage.getStatus().equals(READ_GIF_CUS_TYPE_ONE)) {
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                if (msgInfo.isSelf()) {
                                    msgInfo.setExtra("礼物");
                                } else {
                                    msgInfo.setExtra("对方拆开了您的礼物");
                                }
                            }
                            break;
                        case READ_GIF_CUS_TYPE_ZERO:
                            if (customMessage.getStatus() != null && customMessage.getStatus().equals(READ_GIF_CUS_TYPE_ZERO)) {
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                                msgInfo.setExtra("礼物");
                            }
                            break;
                        case SINGLECHATREMINDERMESSAGE:
                            if (customMessage.getType() != null && customMessage.getType().equals(SINGLECHATREMINDERMESSAGE)) {
                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_TIPS);
                                msgInfo.setExtra("提示");
                            }
                            break;
                    }
                }
                else {

                    if (customMessage!=null && !TextUtils.isEmpty(customMessage.getSendType()) && customMessage.getSendType().equals("5")) {
                        switch (customMessage.getType()) {
                            case "5001":

                                msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                                msgInfo.setExtra("" + customMessage.getText());
                                break;
                        }
                    } else {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
                        msgInfo.setExtra("[礼物]");
                        try {
                            JSONObject jsonObject = new JSONObject(new String(customElem.getData()));
                            if (jsonObject.getString("requestUser") != null && jsonObject.getString("requestUser").length() > 0) {
                                msgInfo.setExtra("[视频通话]");
                                customElem.setExt("[视频通话]".getBytes());
//                            ((TIMCustomElem) ele).setData("[视频通话]".getBytes());
//                            msgInfo.setElement(ele);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (type == TIMElemType.GroupTips) {
            TIMGroupTipsElem groupTips = (TIMGroupTipsElem) ele;
            TIMGroupTipsType tipsType = groupTips.getTipsType();
            String user = "";
            StringBuilder stringBuilder = new StringBuilder();
            if (groupTips.getChangedGroupMemberInfo().size() > 0) {
                Object ids[] = groupTips.getChangedGroupMemberInfo().keySet().toArray();
//                for (int i = 0; i < ids.length; i++) {
//                    user = user + ids[i].toString();
//                    if (i != 0)
//                        user = "，" + user;
//                    if (i == 2 && ids.length > 3) {
//                        user = user + "等";
//                        break;
//                    }
//                }
                for (int i = 0; i < ids.length; i++) {
                    user = FriendsUtils.queryUsersProfile(ids[i].toString());
//                    stringBuilder.append(FriendsUtils.queryUsersProfile(ids[i].toString()));
//                    user = user + ids[i].toString();
                    if (ids.length == 1) {
                        stringBuilder.append(user);
                        break;
                    }
//                    user = user + "，";
                    stringBuilder.append(user).append(",");
                    if (i == 2 && ids.length > 3) {
//                        user = user + "等";
                        stringBuilder.append(user).append("等");
                        break;
                    }
                }

            } else {
                String id = groupTips.getOpUserInfo().getIdentifier();
                user = FriendsUtils.queryUsersProfile(id);
                stringBuilder.append(user);
            }

            StringBuilder message = new StringBuilder(TUIKitConstants.covert2HTMLString(stringBuilder.toString()));
            if (tipsType == TIMGroupTipsType.Join) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_JOIN);
                message.append("加入俱乐部");
            }
            if (tipsType == TIMGroupTipsType.Quit) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_QUITE);
                message.append("退出俱乐部");
            }
            if (tipsType == TIMGroupTipsType.Kick) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_KICK);
                TIMGroupTipsElem timGroupTipsElem = (TIMGroupTipsElem) msgInfo.getElement();
                if (timGroupTipsElem.getOpUser().equals("admin")) {
                    message.append("未完成").append(TUIKitConstants.covert2HTMLString(timGroupTipsElem.getGroupName())).append("的日常任务,已被系统踢出");
                } else {
                    message.append("被踢出俱乐部");
                }
            }
            if (tipsType == TIMGroupTipsType.SetAdmin) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                message.append("被设置管理员");
            }
            if (tipsType == TIMGroupTipsType.CancelAdmin) {
                msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                message.append("被取消管理员");
            }
            if (tipsType == TIMGroupTipsType.ModifyGroupInfo) {
                List<TIMGroupTipsElemGroupInfo> modifyList = groupTips.getGroupInfoList();
                for (int i = 0; i < modifyList.size(); i++) {
                    TIMGroupTipsElemGroupInfo modifyInfo = modifyList.get(i);
                    TIMGroupTipsGroupInfoType modifyType = modifyInfo.getType();
                    if (modifyType == TIMGroupTipsGroupInfoType.ModifyName) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NAME);
                        message.append("修改俱乐部名称为\"").append(modifyInfo.getContent()).append("\"");
                    } else if (modifyType == TIMGroupTipsGroupInfoType.ModifyNotification) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                        message.append("修改俱乐部公告为\"").append(modifyInfo.getContent()).append("\"");
                    } else if (modifyType == TIMGroupTipsGroupInfoType.ModifyOwner) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                        message.append("转让群主给\"").append(modifyInfo.getContent()).append("\"");
                    } else if (modifyType == TIMGroupTipsGroupInfoType.ModifyFaceUrl) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                        message.append("修改了俱乐部头像");
                    } else if (modifyType == TIMGroupTipsGroupInfoType.ModifyIntroduction) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                        message.append("修改俱乐部介绍为\"").append(modifyInfo.getContent()).append("\"");
                    }
                    if (i < modifyList.size() - 1) {
                        message.append("、");
                    }
                }
            }
            if (tipsType == TIMGroupTipsType.ModifyMemberInfo) {
                List<TIMGroupTipsElemMemberInfo> modifyList = groupTips.getMemberInfoList();
                if (modifyList.size() > 0) {
                    long shutupTime = modifyList.get(0).getShutupTime();
                    if (shutupTime > 0) {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                        message.append("被禁言\"").append(DateTimeUtil.formatSeconds(shutupTime)).append("\"");
                    } else {
                        msgInfo.setMsgType(MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE);
                        message.append("被取消禁言");
                    }
                }
            }


            if (TextUtils.isEmpty(message.toString())) {
                return null;
            }
            msgInfo.setExtra(message.toString());
        } else {
            if (type == TIMElemType.Text) {
                TIMTextElem txtEle = (TIMTextElem) ele;
                msgInfo.setExtra(txtEle.getText());
            } else if (type == TIMElemType.Face) {
                TIMFaceElem txtEle = (TIMFaceElem) ele;
                if (txtEle.getIndex() < 1 || txtEle.getData() == null) {
                    TUIKitLog.e("MessageInfoUtil", "txtEle data is null or index<1");
                    return null;
                }
                msgInfo.setExtra("[自定义表情]");


            } else if (type == TIMElemType.Sound) {
                TIMSoundElem soundElemEle = (TIMSoundElem) ele;
                if (msgInfo.isSelf()) {
                    msgInfo.setDataPath(soundElemEle.getPath());
                } else {
                    final String path = TUIKitConstants.RECORD_DOWNLOAD_DIR + soundElemEle.getUuid();
                    File file = new File(path);
                    if (!file.exists()) {
                        soundElemEle.getSoundToFile(path, new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                TUIKitLog.e("MessageInfoUtil getSoundToFile", code + ":" + desc);
                            }

                            @Override
                            public void onSuccess() {
                                msgInfo.setDataPath(path);
                            }
                        });
                    } else {
                        msgInfo.setDataPath(path);
                    }
                }
                msgInfo.setExtra("[语音]");
            } else if (type == TIMElemType.Image) {
                TIMImageElem imageEle = (TIMImageElem) ele;
                String localPath = imageEle.getPath();
                if (msgInfo.isSelf() && !TextUtils.isEmpty(localPath)) {
                    msgInfo.setDataPath(localPath);
                    int size[] = ImageUtil.getImageSize(localPath);
                    msgInfo.setImgWidth(size[0]);
                    msgInfo.setImgHeight(size[1]);
                }
                //本地路径为空，可能为更换手机或者是接收的消息
                else {
                    List<TIMImage> imgs = imageEle.getImageList();
                    for (int i = 0; i < imgs.size(); i++) {
                        TIMImage img = imgs.get(i);
                        if (img.getType() == TIMImageType.Original) {
                            final String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + img.getUuid();
                            msgInfo.setImgWidth((int) img.getWidth());
                            msgInfo.setImgHeight((int) img.getHeight());
                            File file = new File(path);
                            if (file.exists()) {
                                msgInfo.setDataPath(path);
                            }
                        }
                    }
                }

                msgInfo.setExtra("[图片]");
            } else if (type == TIMElemType.Video) {
                TIMVideoElem videoEle = (TIMVideoElem) ele;
                if (msgInfo.isSelf() && !TextUtils.isEmpty(videoEle.getSnapshotPath())) {
                    int size[] = ImageUtil.getImageSize(videoEle.getSnapshotPath());
                    msgInfo.setImgWidth(size[0]);
                    msgInfo.setImgHeight(size[1]);
                    msgInfo.setDataPath(videoEle.getSnapshotPath());
                    msgInfo.setDataUri(FileUtil.getUriFromPath(videoEle.getVideoPath()));
                } else {
                    TIMVideo video = videoEle.getVideoInfo();
                    final String videoPath = TUIKitConstants.VIDEO_DOWNLOAD_DIR + video.getUuid();
                    Uri uri = Uri.parse(videoPath);
                    msgInfo.setDataUri(uri);
                    TIMSnapshot snapshot = videoEle.getSnapshotInfo();
                    msgInfo.setImgWidth((int) snapshot.getWidth());
                    msgInfo.setImgHeight((int) snapshot.getHeight());
                    final String snapPath = TUIKitConstants.IMAGE_DOWNLOAD_DIR + snapshot.getUuid();
                    //判断快照是否存在,不存在自动下载
                    if (new File(snapPath).exists()) {
                        msgInfo.setDataPath(snapPath);
                    }
                }

                msgInfo.setExtra("[视频]");
            } else if (type == TIMElemType.File) {
                TIMFileElem fileElem = (TIMFileElem) ele;
                String filename = fileElem.getUuid();
                if (TextUtils.isEmpty(filename)) {
                    filename = System.currentTimeMillis() + fileElem.getFileName();
                }
                final String path = TUIKitConstants.FILE_DOWNLOAD_DIR + filename;
                File file = new File(path);
                if (file.exists()) {
                    if (msgInfo.isSelf()) {
                        msgInfo.setStatus(MessageInfo.MSG_STATUS_SEND_SUCCESS);
                    } else {
                        msgInfo.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                    }
                    msgInfo.setDataPath(path);
                } else {
                    if (msgInfo.isSelf()) {
                        if (TextUtils.isEmpty(fileElem.getPath())) {
                            msgInfo.setStatus(MessageInfo.MSG_STATUS_UN_DOWNLOAD);
                            msgInfo.setDataPath(path);
                        } else {
                            file = new File(fileElem.getPath());
                            if (file.exists()) {
                                msgInfo.setStatus(MessageInfo.MSG_STATUS_SEND_SUCCESS);
                                msgInfo.setDataPath(fileElem.getPath());
                            } else {
                                msgInfo.setStatus(MessageInfo.MSG_STATUS_UN_DOWNLOAD);
                                msgInfo.setDataPath(path);
                            }
                        }
                    } else {
                        msgInfo.setStatus(MessageInfo.MSG_STATUS_UN_DOWNLOAD);
                        msgInfo.setDataPath(path);
                    }
                }
                msgInfo.setExtra("[文件]");
            }
            msgInfo.setMsgType(TIMElemType2MessageInfoType(type));
        }


        if (timMessage.status() == TIMMessageStatus.HasRevoked) {
            msgInfo.setStatus(MessageInfo.MSG_STATUS_REVOKE);
            msgInfo.setMsgType(MessageInfo.MSG_STATUS_REVOKE);
            if (msgInfo.isSelf()) {
                msgInfo.setExtra("您撤回了一条消息");
            } else if (msgInfo.isGroup()) {
                String message = TUIKitConstants.covert2HTMLString(msgInfo.getFromUser());
                msgInfo.setExtra(message + "撤回了一条消息");
            } else {
                msgInfo.setExtra("对方撤回了一条消息");
            }
        } else {
            if (msgInfo.isSelf()) {
                if (timMessage.status() == TIMMessageStatus.SendFail) {
                    msgInfo.setStatus(MessageInfo.MSG_STATUS_SEND_FAIL);
                } else if (timMessage.status() == TIMMessageStatus.SendSucc) {
                    msgInfo.setStatus(MessageInfo.MSG_STATUS_SEND_SUCCESS);
                } else if (timMessage.status() == TIMMessageStatus.Sending) {
                    msgInfo.setStatus(MessageInfo.MSG_STATUS_SENDING);
                }
            }
        }


        return msgInfo;
    }


    private static int TIMElemType2MessageInfoType(TIMElemType type) {
        switch (type) {
            case Text:
                return MessageInfo.MSG_TYPE_TEXT;
            case Image:
                return MessageInfo.MSG_TYPE_IMAGE;
            case Sound:
                return MessageInfo.MSG_TYPE_AUDIO;
            case Video:
                return MessageInfo.MSG_TYPE_VIDEO;
            case File:
                return MessageInfo.MSG_TYPE_FILE;
            case Location:
                return MessageInfo.MSG_TYPE_LOCATION;
            case Face:
                return MessageInfo.MSG_TYPE_CUSTOM_FACE;
            case GroupTips:
                return MessageInfo.MSG_TYPE_TIPS;
        }
        return -1;
    }
}
