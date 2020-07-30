package com.jfkj.im.TIM.helper;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.VideocallBean;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.SendRedPackageBean;
import com.jfkj.im.TIM.redpack.chatroom.AnswerSubmitBean;
import com.jfkj.im.TIM.redpack.game.GameRedPackageBean;
import com.jfkj.im.TIM.utils.DemoLog;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 自定义消息的bean实体，用来与json的相互转化
 */
public class CustomMessage {

    private static final String TAG = CustomMessage.class.getSimpleName();

    public static final int VIDEO_CALL_ACTION_UNKNOWN = -1;
    /**
     * 正在呼叫
     */
    public static final int VIDEO_CALL_ACTION_DIALING = 0;
    /**
     * 发起人取消
     */
    public static final int VIDEO_CALL_ACTION_SPONSOR_CANCEL = 1;
    /**
     * 拒接电话
     */
    public static final int VIDEO_CALL_ACTION_REJECT = 2;
    /**
     * 无人接听
     */
    public static final int VIDEO_CALL_ACTION_SPONSOR_TIMEOUT = 3;
    /**
     * 连接进入通话
     */
    public static final int VIDEO_CALL_ACTION_ACCEPTED = 4;
    /**
     * 挂断
     */
    public static final int VIDEO_CALL_ACTION_HANGUP = 5;
    /**
     * 电话占线
     */
    public static final int VIDEO_CALL_ACTION_LINE_BUSY = 6;

    public static final int JSON_VERSION_1_HELLOTIM = 1;
    public static final int JSON_VERSION_2_ONLY_IOS_TRTC = 2;
    public static final int JSON_VERSION_3_ANDROID_IOS_TRTC = 3;

    // 一个欢迎提示富文本
    public static final int HELLO_TXT = 1;
    // 视频通话
    public static final int VIDEO_CALL = 2;
    //红包
    public static final String READ_PACKAGE_CUS_TYPE_ZERO = "0";
    public static final String READ_PACKAGE_CUS_TYPE_ONE = "1";
    public static final String READ_PACKAGE_CUS_TYPE_TWO = "2";
    public static final String READ_PACKAGE_CUS_TYPE_THREE = "3";
    public static final String READ_PACKAGE_CUS_TYPE_FOUR = "4";
    public static final String READ_PACKAGE_CUS_TYPE_FIV = "5";
    public static final String READ_PACKAGE_CUS_TYPE_SIX = "6";
    public static final String READ_PACKAGE_CUS_TYPE_SEVEN = "7";

    public static final String PIAZZA_DAILY_TOP_TIPS_TYPE_8 = "8";
    public static final String PIAZZA_DAILY_TOP_TIPS_TYPE_9 = "9";
    public static final String PIAZZA_DAILY_TOP_TIPS_TYPE_10 = "10";
    public static final String PIAZZA_DAILY_TOP_TIPS_TYPE_11 = "11";


    //邀请
    //礼物的转态
    public static final String READ_GIF_CUS_TYPE_ZERO = "0";
    public static final String READ_GIF_CUS_TYPE_ONE = "1";
    public static final String READ_GIF_CUS_TYPE_TWO = "2";
    public static final String READ_GIF_CUS_TYPE_THREE = "3";

    public static final String READ_PACKAGE_CUS_TYPE_NINE = "9";
    //拒绝
    public static final String READ_PACKAGE_CUS_TYPE_EIGHT = "8";
    public static final String READ_PACKAGE_CUS_TYPE_TEN = "10";
    public static final String READ_PACKAGE_CUS_TYPE_ELEVENT = "11";
    public static final String INVITETOPARTY = "12";   //邀请
    public static final String INVITETOPARTYSTATUS = "13";   //邀请
    public static final String INVITETOPART_ENTHGTH = "14";
    public static final String SINGLECHATREMINDERMESSAGE="15";   //单聊提示消息 涉黄、暴力.....


    public static final String REDENVELOPECOLLECTIONCOMPLETED="3001";  // 红包领取完成  广场提示消息


    public static final String DYNAMIC_UNLIKE_TYPE = "100";  //取消点赞


    //chatroom
    public static final String RED_PACKAGE_TYPE_ONE = "1";
    public static final String RED_PACKAGE_CUS_TYPE_TWELVE = "12";

    public static final String INVITE_CUS_TYPE_ONE = "1";
    public static final String INVITE_CUS_TYPE_TWO = "2";
    public static final String INVITE_CUS_TYPE_THREE = "3";
    public static final String RED_PAGE_TYPE_CODE_FOUR = "4";
    public static final String RED_PAGE_TYPE_CODE_FIV = "5";

    public static final String RED_PACKAGE_STATES_ZERO = "-1";
    public static final String RED_PACKAGE_STATES_ONE = "1";
    public static final String RED_PACKAGE_STATES_TWO = "2";
    public static final String RED_PACKAGE_STATES_THREE = "3";

    public static final String READ_PACKAGE_MSG_TYPE_ONE = "1";
    public static final String READ_PACKAGE_MSG_TYPE_TWO = "2";
    public static final String RED_PACKAGE_MST_TYPE_THREE = "3";
    public static final String RED_PACKAGE_MST_TYPE_FIVE = "5";

    public static final String CLUB_DELETE_THIRTEEN = "13";



    public static final String CLUB_AT_5001 = "5001";


    //群data
    public static final int GROUP_DATA = 11;

    public static final int GIF = 13;

    private String partner = "";

    String text = "欢迎加入云通信IM大家庭！";
    String link = "https://cloud.tencent.com/document/product/269/3794";
    /**
     * 1: 仅仅是一个带链接的文本消息
     * 2: iOS支持的视频通话版本，后续已经不兼容
     * 3: Android/iOS/Web互通的视频通话版本
     */
    public int version = 1;
    /**
     * 表示一次通话的唯一ID
     */
    public String type;
    public int duration = 0;
    public String requestUser;
    public int roomID = 0;
    public int videoState = VIDEO_CALL_ACTION_UNKNOWN;
    public String sendType;

    /**
     * 群组时需要添加邀请人，接受者判断自己是否在邀请队列来决定是否加入通话
     */
    public String[] invited_list;

    private SendRedPackageBean sendRedPackageBean;
    private GameRedPackageBean gameRedPackageBean;
    private RedPackageCustom packageCustom;


    public CustomMessage(RedPackageCustom packageCustom) {
        this.packageCustom = packageCustom;
    }

    private VideocallBean videocallBean;

    public VideocallBean getVideocallBean() {
        return videocallBean;
    }

    public void setVideocallBean(VideocallBean videocallBean) {
        this.videocallBean = videocallBean;
    }

    public CustomMessage() {
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public static CustomMessage convert2VideoCallData(List<TIMMessage> msgs) {

        if (null == msgs || msgs.size() == 0) {
            return null;
        }
        for (TIMMessage msg : msgs) {




            List<MessageInfo> list = MessageInfoUtil.TIMMessage2MessageInfo2(msg, false);
            if (list == null) {
                return null;
            }
            if (list.size() == 0) {
                return null;
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
                CustomMessage data = new CustomMessage();
                Log.d("--->getData2",Utils.APPID+new String(elem.getData()));
                try {
                    data = JSON.parseObject(new String(elem.getData()), CustomMessage.class);

                } catch (Exception e) {
                }
                if (data == null) {
                    DemoLog.e(TAG, "No Custom Data: " + new String(elem.getData()));
                    continue;
                }
//                else if (data.version != JSON_VERSION_3_ANDROID_IOS_TRTC) {
//                    continue;
//                }
                data.setPartner(info.getFromUser());
                data.setSendName(msg.getSenderNickname());
                return data;
            }
        }
        return null;
    }

    public SendRedPackageBean getSendRedPackageBean() {
        return sendRedPackageBean;
    }

    public void setSendRedPackageBean(SendRedPackageBean sendRedPackageBean) {
        this.sendRedPackageBean = sendRedPackageBean;
    }

    public GameRedPackageBean getGameRedPackageBean() {
        return gameRedPackageBean;
    }

    public void setGameRedPackageBean(GameRedPackageBean gameRedPackageBean) {
        this.gameRedPackageBean = gameRedPackageBean;
    }

    public RedPackageCustom getPackageCustom() {
        return packageCustom;
    }

    public void setPackageCustom(RedPackageCustom packageCustom) {
        this.packageCustom = packageCustom;
    }


    //自定义
    private String sendId;//发送者id
    private String avatarUrl;//信息发送者头像 url
    private String sendName;//信息发送者昵称
    private String VIP;//发送者VIP等级
    private String msgType;//消息类型，1单聊，2群组，这里固定为2，辅助判断
    /**
     * 1.群红包
     * 2.冒险游戏红包
     * 3.冒险游戏提交的任务
     * 4.半透明tips提示：
     */
    private String cusType;
    private String redId;
    private String redDesc;
    private String taskUrl;
    private String taskImage;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * （1.群红包领取，2.群红包领完，3.领取冒险游戏红包参与提示，
     * 4.冒险游戏红包领取完成需做任务，5.冒险游戏任务未提交，
     * 6.俱乐部聊天冻结，7.新用户加入俱乐部涉黄暴力提示（入群后自动发送一条，仅发送者自己可看到))
     */
    private String tipsType;
    private String[] receiveId;
    private String redSendId;
    private String redIsDone;
    //    private int sendType;
    private String gameUserId;
    private String orderId;

    private String cadddate;

    /**
     * 性格测试data
     */
    private CustomMessage body;
    private AnswerSubmitBean msg;

    /**
     * 破冰任务
     */
    private String taskId;
    private String receiverId;
    private String taskContent;
    private String taskURL;
    private String taskType;
    private String head;
    private String money;
    private String receiveName;
    private String receivedId;

    /**
     * 聚会
     */
    private String partyId;
    private String joinTime;
    private String senderId;  // 参加聚会ID
    private String pagePhoto;


    /**
     * 俱乐部@
     */
    private   List<String>  users;  //@列表
    private String notiye;


    private String userId;
    private String clubName;

    private String packageReceivedId;   //红包领取人ID

    private String clubId;
    private String diamondNumber;



    public String getDiamondNumber() {
        return diamondNumber;
    }

    public void setDiamondNumber(String diamondNumber) {
        this.diamondNumber = diamondNumber;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getPackageReceivedId() {
        return packageReceivedId;
    }

    public void setPackageReceivedId(String packageReceivedId) {
        this.packageReceivedId = packageReceivedId;
    }

    public String getClubName() {
        return clubName == null ? "":clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String>  getUserList() {
        return users;
    }

    public void setUserList(   List<String>   userList) {
        this.users = userList;
    }

    public String getNotiye() {
        return notiye;
    }

    public void setNotiye(String notiye) {
        this.notiye = notiye;
    }

    public String getPagePhoto() {
        return pagePhoto;
    }

    public void setPagePhoto(String pagePhoto) {
        this.pagePhoto = pagePhoto;
    }

    //1:已经拒绝 2:已经同意
    private String agreeOrReject;


    public String getAgreeOrReject() {
        return agreeOrReject;
    }

    public void setAgreeOrReject(String agreeOrReject) {
        this.agreeOrReject = agreeOrReject;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getReceiveName() {
        return receiveName == null ? "":receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(String receivedId) {
        this.receivedId = receivedId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskURL() {
        return taskURL;
    }

    public void setTaskURL(String taskURL) {
        this.taskURL = taskURL;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCadddate() {
        return cadddate;
    }

    public void setCadddate(String cadddate) {
        this.cadddate = cadddate;
    }

    public AnswerSubmitBean getMsg() {
        return msg;
    }

    public void setMsg(AnswerSubmitBean msg) {
        this.msg = msg;
    }

    public CustomMessage getBody() {
        return body;
    }

    public void setBody(CustomMessage body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getVIP() {
        return VIP;
    }

    public void setVIP(String VIP) {
        this.VIP = VIP;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public String getRedDesc() {
        return redDesc;
    }

    public void setRedDesc(String redDesc) {
        this.redDesc = redDesc;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }

    public String getTipsType() {
        return tipsType;
    }

    public void setTipsType(String tipsType) {
        this.tipsType = tipsType;
    }

    public String[] getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String[] receiveId) {
        this.receiveId = receiveId;
    }

    public String getRedSendId() {
        return redSendId;
    }

    public void setRedSendId(String redSendId) {
        this.redSendId = redSendId;
    }

    public String getRedIsDone() {
        return redIsDone;
    }

    public void setRedIsDone(String redIsDone) {
        this.redIsDone = redIsDone;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(String gameUserId) {
        this.gameUserId = gameUserId;
    }
}
