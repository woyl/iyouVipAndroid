package com.jfkj.im.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.jfkj.im.App;
import com.jfkj.im.Bean.chat.Message;
import com.jfkj.im.Bean.chat.MsgSendStatus;
import com.jfkj.im.Bean.chat.MsgType;
import com.jfkj.im.entity.GiftData;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.netty.channel.Channel;

public class Utils {

    /**
     * 这个是用来做sign 加密的key MD5(reqtime + key)
     */
    public static final String KEY = "3ae50f0fc8475cc11ea3c1c0c74d7f73";
    //responseCode
    public static final String SP_KEY_HEADER_STATE = "sp_key_header_state";
    public static final String SP_KEY_MINE_HEADER_STATE = "sp_key_mine_header_state";

    public static final String STATUS_SUCCESS = "200";
    public static final String STATUS_NOT_LOGIN = "2001";//没有登录
    public static final String STATUS_LOGIN_OUT_OF_DATA = "11001";//登录过期
    public static final String STATUS_SYSTEM_ERROR = "500";//系统异常
    public static final String STATUS_LOGIN_FREEZE_USER = "2003";//账户冻结
    public static final String STATUS_UPLOADING_ERROR = "30001";//上传失败
    public static final String STATUS_LOGIN_ACCOUNT_NOT_EXIST = "2004";//账户不存在
    public static final String STATUS_LOGIN_MULTI_DEVICE = "2005";//多设备登录
    public static final String SP_KEY_USER_SCHOOL="sp_key_user_school";
    public static final int GET_USER_DETAIL = 10002;//用户详情
    public static final int SET_USER_HEADER = 10003;//用户头像
    public static final int SET_USER_NICKNAME = 10004;//用户nickname
    public static final int SET_USER_DESCRIBE = 10005;//用户描述
    public static final int SET_USER_AGE = 10006;//用户年级
    public static final int SET_USER_WEIGHT= 10007;//用户身高
    public static final int SET_USER_HEIGHT = 10008;//用户体重
    public static final int SET_USER_EDUCATION= 10009;//用户学历
    public static final int SET_USER_DELETE_VIDEO = 10010;//删除视频
    public static final int SET_USER_DYNAMIC_COMMENT = 10011;//用户评论
    public static final int SET_USER_DYNAMIC_LIKE = 10012;//用户点赞或者取消点赞
    public static final int SET_USER_UPLOAD_VIDEO = 10013;//用户上传了视频
    public static final int SET_USER_HOMETOWN = 10014;//用户家乡
    public static final int REFRUSH_USER_BALANCE = 10015;//   充值成功，刷新用户余额，买卡
    public static final int USER_UPGRADE_LEVEL = 10019;  // 升级提醒
    
    public static  final String SP_KEY_USER_INDUSTRY = "sp_key_industry"; //职业
    public static final String SP_KEY_USER_SMOKING = "sp_key_user_smoking";
    public static final String SP_KEY_USER_DRINKWINK="sp_key_user_drinkwink";
    public static final String SP_KEY_USER_LIKECUISINE="sp_key_user_likecuisine";
    public static final String SP_KEY_USER_OCCUPATION = "sp_key_user_occupation";

    public static final String PATH_DATA = App.getAppContext() == null ? null :App.getAppContext().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static boolean isShowFloatWindow;
    public static ViewGroup mVideoViewLayout;
    public static final String PRAISE_TRUE= "1";//点赞
    public static final String PRAISE_CANCEL= "2";//取消点赞
    public static final String GETGIFTLIST= "getgiftlist";//取消点赞
    public static final String MESSAGE_NUMBER= "message_number";//点赞
    /**
     * Intent params
     */
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    public static final String USERIDS = "userIds";

    public static final String SP_KEY_REGISTRATION_ID= "sp_key_registrationID";//别名

    public static final String SP_KEY_USER_ID = "sp_key_user_id";
    public static final String SP_KEY_USER_TOKEN = "sp_key_user_token";
    public static final String SP_KEY_PHONE_NUM = "sp_key_phone_number";
    public static final String SP_KEY_USER_GENDER = "sp_key_user_gender";
    public static final String SP_KEY_USER_DESCRIBE = "sp_key_user_describe";
    public static final String SP_KEY_USER_HEIGHT = "sp_key_user_height";
    public static final String SP_KEY_USER_WEIGHT = "sp_key_user_weight";
    public static final String SP_KEY_USER_VIPCARD = "sp_key_user_vipcard";
    public static final String SP_KEY_USER_EDUCATION = "sp_key_user_education";
    public static final String SP_KEY_USER_BIRTHDAY = "sp_key_user_birthday";
    public static final String SP_KEY_USER_VIDEO_URL = "sp_key_user_video_url";
    public static final String SP_KEY_USER_LONGITUDE = "sp_key_user_longitude";  //经度
    public static final String SP_KEY_USER_LATITUDE = "sp_key_user_latitude";    //纬度
    public static final String SP_KEY_USER_AGE = "sp_key_user_age";    //纬度
    public static final String SP_KEY_USER_BALANCE = "sp_key_user_balance";    //余额
    public static final String SP_KEY_USER_GIFT = "sp_key_user_gift";    //礼物
    public static final String SP_KEY_USER_COST = "sp_key_user_cost";    //消费
    public static final String SP_KEY_USER_GOLD = "sp_key_user_gold";    //金币
    public static final String SP_KEY_USER_UPGRADE_AMOUNT = "sp_key_user_upgrade";    //升级需要多少钱
    public static final String SP_KEY_USER_UPGRADE_MONEY = "sp_key_user_upgrade_money";    //累计获得的礼物

    public static final String SP_KEY_USER_VIP_LEVEL = "sp_key_user_vip_level";    //等级
    public static final String SP_KEY_USER_HOMETOWN = "sp_key_user_hometown";    //家乡
    public static final String SP_KEY_USER_CONSTELLATION = "sp_key_user_constellation";    //星座
    public static final String SP_KEY_USER_NOT_HINT = "sp_key_user_not_hint";    //不在提示评论钱
    public static final String SP_KEY_USER_HAS_GRANTED = "sp_key_user_has_granted";    //位置权限是否以获取
    public static final String SP_KEY_PWD = "sp_key_pwd";
    public static final String SP_KEY_PWD_MD5 = "sp_key_pwd_md5";
    public static final String SP_KEY_NICK_NAME = "sp_key_nick_name";
    public static final String SP_KEY_HEADER_URL = "sp_key_header_url";
    public static final String SP_KEY_MINE_HEADER_URL = "sp_key_mine_header_url" ;
    public static final String SP_KEY_LIKES = "sp_key_likes";
    public static final String SP_KEY_SUMMARY = "sp_key_summary";
    public static final String SP_KEY_FANS = "sp_key_fans";
    public static final String SP_KEY_FOCUS = "sp_key_focus";
    public static final String SP_KEY_SIGN = "sp_key_sign";
    public static final String SP_KEY_TRUE_NAME = "sp_key_true_name";
    public static final String SP_KEY_IDNO = "sp_key_idno";
    public static final String CFROM = "cfrom";
    public static final String HOMETOWN = "hometown";
    public static final String CITY = "city";
    public static final String USERINFO = "userInfoJson";
    public static final String UpSex= "UpdateSex";
    //是不是第一次安装
    public static final String SP_KEY_FIRST_INSTALL = "sp_key_first_install";
    public static final String USERID = "userId";
    //iyou_az android
    public static final String ANDROID = "android";
    public static final String APP_CHANNEL = "iyou_az";
    public static final String LONGITUDE = "longitude";//经度
    public static final String LATITUDE = "latitude";//维度
    public static final String APPVERSION = "appVersion";
    public static final String CHANNEL = "channel";
    public static String MOBILENO = "mobileNo";
    public static String TYPES = "types";
    public static String code = "code";
    public static String POSITION = "position";
    public static String PASSWORD = "password";
    public static String OSNAME = "osName";
    public static String DEVICENAME = "deviceName";
    public static String DEVICEID = "deviceId";
    public static String REQ_TIME = "reqTime";
    public static String SEX = "sex";
    public static String Birthday = "birthday";
    public static String TYPES_REGISTER = "0";//0代表注册获取验证码
    public static String TYPES_FORGETPASSWORD = "1";//1代表忘记密码获取验证码,
    public static String TYPES_LOGIN = "0";//代表登录获取验证码,
    public static String SIGN = "sign";     //参数用来处理加密的
    public static String CHANNEL_ANDROID = "android";//手机厂商
    public static String ARTICLEID="articleId";
    public static String GENDER="gender";
    public static String HEAD="head";
    public static String NICKNAME="nickName";
    public static String TOKEN="token";
    public static String HEAD_URL="head_url";
    public static String PHOTO1="photo1";
    public static String PHOTO2="photo2";
    public static String USERVIDEO="userVideo";
    public static String TYPE="type";
    public static String GROUPNAME="groupName";
    public static String NOTICE="notice";
    public static String GROUPNOTICE="groupNotice";
    public static String PAGESIZE="pageSize";
    public static String PAGENO="pageNo";
    public static String CLIENTVERSION="clientVersion";
    public static String SEQROOM="seqroom";
    public static String MSGIDS="msgIds";
    public static String COMMENTID="commentId";
    public static int PORT=1;
    public static String SORT="sort";
    public static String SERIALNO="serialNo";
    public static String RECEIVEID="receiveid";
    public static String SENDID="sendid";
    public static String GROUPID="groupId";
   public static String PRAISEID="praiseId";
    public static String AREACODE="areaCode";
    public static String IMGYZM="imgYzm";
    public static String MSGID="msgId";
    public static String SENDTIME="sendTime";
    public static String CONTENT="content";
    public static String CHAT_HEAD_URL="chat_head_url";
    public static String ISPRIVATEFRIEND="isprivatefriend";
    public static String TOP="top";
    public static String NODISTURB="noDisturb";
    public static String VROOMID="VRoomId";
    public static String SDKAPPID="SDKAPPID";
    public static String GroupNumber="GroupNumber";
    public static String SECRETKEY="SECRETKEY";
    public static String UID ="Uid";
    public static String RUSERID ="ruserid";
    public static String REDPACKETNUM ="redPacketNum";
    public static String SENDHOUR ="sendHour";
    public static String ISSUPER ="isSuper";
    public static int ROOMID =0;
    public static String CONTENTTYPE ="contenttype";
    public static String HEADIMGURL="headimgurl";
    public static String RECENTPERSONMESSAGE ="recentpersonmessage";
    public static String RECENTGROUPMESSAGE ="recentgroupmessage";
    public static Channel channel=null;
    public static String SEND_HEAD_URL ="send_head_url";
    public static String GROUP_HEAD_URL ="group_head_url";
    public static String RECEIVE_HEAD_URL ="receive_head_url";
    public static String SEND_NICK_NAME ="send_nick_name";
    public static String ISPRIVATE ="ISPRIVATE";
    public static String UPDATE="update";
    public static String USER_NAME ="user_name";
    public static String TOMSGID ="toMsgId";
    public static String ISDISTURB="isDISTURB";
    public static String HANDLERINVITEGROUP ="handlerInviteGroup";
    public static String ADD_GROUP ="add_group";
    public static String APPID ="appid";//这个是用户的id  主要是用于区别每个不同的用户登录   也可以在某些接口传入用户的id
    public static String ISDYNAMIC ="isdynamic";
    public static String RECENTTIME ="recenttime";
    public static long TIMEINTERVAL =30000L;
    public static String   FRIENDORDERID="Friendorderid";
    public static String APPLYID ="applyId";
    public static String ADDFRIEND ="addfriend";
    public static String ADDFRIENDNUMBER ="addfriendNumber";
    public static String ORDERID ="orderId";
    public static String TRADEORDERNO ="tradeOrderNo";
    public static String AVCHATROOMID ="AVChatRoomId";
    public static String MONEY ="money";
    public static String SIZE ="size";
    public static String SENDWORD ="sendWord";
    public static String GIFIMGURL ="gifimgurl";
    public static String REDID ="redId";
    public static String NUMBER ="number";
    public static String GIFTID ="giftId";
    public static String Adventurecontent ="";
    public static String GIFTSIZE ="giftsize";
    public static String GITNUMBER ="gitnumber";
    public static String SENDFRIENDGIFT ="sendfriendgift";
    public static String MESSAGERECORDPAGESIZE ="200";
    public static String ISSEX ="1";
    public static String MEDIAPATH ="MEDIAPATH";
    public static String CALLVIDEO ="callvideo";
    public static String VIDEOCALL_TYPE ="videocall_type";
    public static String MEDIASIZE ="MEDIASIZE";
    public static String GROUPHEAD="grouphead";
    public static String GROUPMESSAGECENTER ="groupmessagecenter";
    public static String VISITORID ="visitorid";
    public static String PERMISSION ="PERMISSION";
    public static String FILETYP ="FILETYP";
    public static String MEDIATIME ="MediaTime";
    public static String MEDIAPICTURE ="MediaPicture";
    public static String SENDTYPE ="sendType";
    public static String NUMBERPEOPLE="numberpeople";
    public static String QUESTIONIDS="questionIds";
    public static String ANSWERIDS="answerIds";
    public static String CGAMEID="cgameid";
    public static String GAMEID="gameId";
    public static String SEQ="seq";
    public static String RAND="rand";
    public static String TIMESTAMP="timestamp";
    public static String TEST="TEST";
    public static String RESULTID="resultId";
    public static String FRIENDMESSAGE="friendmessage";
    public static String TIME="TIME";
    public static String MINECHARACTER="minecharacter";
    public static String SQUREHINT="Squrehint";
    public static String AGE="age";
    public static String INVITE_ADD_GROUP="invite_add_group";
    public static String REQTIME="reqTime";
    public static String FIRST = "first";
    public static String USER_SIG="userSig";
    public static String FILE_TYPE="fileType";
    public static String PATH_TYPE = "pathType";
    public static List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
     public static String BODY="body";
    public static String SELECTADVERTISEMENT = "selectAdvertisement";
    public static String APIPKG = "apipkg";
    public static String STATUS = "status";
    public static String GETRANKING = "getRanking";
    public static String CONVERSATIONMESSAGE = "ConversationMessage";
    public static String GAME_TYPE= "gameType";
    public static String GROUP_TYPE = "Public";//Public OAGroup
    public static String GROUP_NO_NUM = "NO_NUM";
    public static String GROUP_CENTER_MESSAGE = "group_center_mess";
    public static String GROUP_CENTER_MESSAGE_ISSHOW = "group_center_mess_is_show";
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static final int MIN_CLICK_DELAY_TIME_ONE = 100;
    private static long lastClickTime;
    public static final String AV_CHAT_ROOM_ID = "AVChatRoomId";
    public static String JOIN_CHAT_ROOM = "join_chat_room";
    public static String ANSWER_IDS= "answerIds";
    public static String QUESTION_IDS= "questionIds";
    public static String SYSTEMID= "999999";        //系统通知ID
    public static String UNREADMESSAGE="unreadmessage";
    public static String IS_TIPS = "is_tips";
    public static String VIP = "vip";
    public static String  RED_DESC= "redDesc";
    public static String  EXCHANGELIST="exchangeList";
    public static String  DISTURB="disturb";
    public static String  CIRCLEID="circleid";
    public static String  CIRCLEROOMID="circleRoomId";
    public static String CONTENTYPE="Content-Type";
    public static String DEVICENAMETYOE= "device_nametyoe";
    public static String TASK_ID = "taskId";
    public static String TASK_CONTENT = "ctaskcontent";
    public static String I_REWARD_AMOUNT = "irewardamount";
    public static String URL = "url";
    public static String TASK_COUNT = "taskCount";
    public static String OLD_DAY = "old_day";
    public static String MINE_OLD_DAY = "mine_old_day";
    public static String CHAT_GROUP_OLD_DAY= "chat_group_old_day";
    public static String CHAT_GROUP_OLD_EVERY_DAY = "chat_group_old_every_day";
    public static String CHAT_GROUP_OLD_EVERY_CLOSE = "chat_group_old_every_close";
    public static String IS_SHOW_DIALOG = "is_show_dialog";
    public static String IS_NEW_USER = "isNewUser";
    public static String CONTENT_ID = "contentId";
    public static String IS_NEW_MESSAGE = "isNew";
    public static String IS_NEW_MESSAGE_ROLL = "is_foll";
    public static String UN_READ_NUM="unreadNum";


    //跳转到用户详情
    public static final int START_ACTIVITY_DETAIL_CHAT_FRAGMENT = 0x1001;

    //跳转到俱乐部信息页面
    public static final int START_ACTIVITY_CLUB_INFO_CHAT_FRAGMENT = 0x1002;



    public static String SEARCH_HISTORY_ADDRESS = "app_search_history_address";

    public static String VIP_UPDADT = "VIP_DATE";
    public static String NEW_POINT = "new_point";

    public static String HIDE = "hide";
    public static String GROUP_TYPE_TASK = "groupType";


    public static boolean netWork() {//请求数据之前要去检测是否有没有网络  没有网络不请求数据
        ConnectivityManager mConnectivity = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting() || !info.isAvailable()) {
            return false;
        }
        int netType = info.getType();
        if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE) {
            return info.isConnected();
        }
        return false;
    }

    /**
     * 手机厂商
     *
     * @return
     */
    public static String getdeviceName() {

        String string = SPUtils.getInstance(App.getAppContext()).getString(DEVICENAMETYOE);

//        getAppMetaData();

        return TextUtils.isEmpty(string)?"i_you":string;
        //return android.os.Build.BRAND;
    }





    /**
     * 获得当前程序版本号
     *
     * @return int 版本号
     * @throws Exception 异常
     */
    public static int getVersionCode() {
        // 获取PackageManager的实例
        PackageManager packageManager = App.getAppContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(App.getAppContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }

    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (char curchar : input) {
                if (java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                } else {
                    output += java.lang.Character.toString(curchar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String formatCurrentTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return df.format(calendar.getTime());
    }

    public static String getMsgid() {
        UUID uuid = UUID.randomUUID();
        String dateStr = formatCurrentTime(System.currentTimeMillis());
        return dateStr + uuid.toString().replaceAll("-", "");
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        //        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static boolean isFirstInstall() {
        boolean installState = SPUtils.getInstance(App.getAppContext()).getBoolean(SP_KEY_FIRST_INSTALL, true);
        return installState;
    }
    public static String gettime(){
        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }
    public static final String mSenderId = "right";
    public static final String mTargetId = "left";

    public static  Message getBaseSendMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(Utils.getMsgid() + "");
        mMessgae.setSenderId(mSenderId);
        mMessgae.setTargetId(mTargetId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }

    public static Message getBaseReceiveMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(Utils.getMsgid() + "");
        mMessgae.setSenderId(mTargetId);
        mMessgae.setTargetId(mSenderId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }
    //获取视频文件长短
    public static String getVideoDuration(String path){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); //
        return duration;
    }

    public static boolean getSystemVersion() {//不是第10个系统
        return android.os.Build.VERSION.SDK_INT<Build.VERSION_CODES.Q;
    }

    public static String getTimeString(Long timestamp) {
        String result = "";
        String[] weekNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String hourTimeFormat = "HH:mm";
        String monthTimeFormat = "M月d日 HH:mm";
        String yearTimeFormat = "yyyy年M月d日 HH:mm";
        try {
            Calendar todayCalendar = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);

            if (todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {//当年
                if (todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {//当月
                    int temp = todayCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
                    switch (temp) {
                        case 0://今天
                            result = getTime(timestamp, hourTimeFormat);
                            break;
                        case 1://昨天
                            result = "昨天 " + getTime(timestamp, hourTimeFormat);
                            break;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            result = weekNames[dayOfWeek - 1] + " " + getTime(timestamp, hourTimeFormat);
                            break;
                        default:
                            result = getTime(timestamp, monthTimeFormat);
                            break;
                    }
                } else {
                    result = getTime(timestamp, monthTimeFormat);
                }
            } else {
                result = getTime(timestamp, yearTimeFormat);
            }
            return result;
        } catch (Exception e) {

            return "";
        }
    }

    public static String getTime(long time, String pattern) {
        Date date = new Date(time);
        return dateFormat(date, pattern);
    }
    public static String dateFormat(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastClick2() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME_ONE) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22,
            23, 23, 23, 23, 22, 22 };

    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }

    public static final String[] constellationArray = { "水瓶座", "双鱼座", "白羊座",
            "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };


}
