package com.jfkj.im.retrofit;

import com.jfkj.im.ArticleDynamicBean;
import com.jfkj.im.Bean.AllMyPartysRedBean;
import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.CrushIceTaskHallBean;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.Bean.GetBindBankBean;
import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.Bean.MyDynamicMessage;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.SignInBean;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.entity.AccountBalance;
import com.jfkj.im.entity.AddFriend;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.entity.CardInfoBean;
import com.jfkj.im.entity.CarryScaleBean;
import com.jfkj.im.entity.ChargeRecordBean;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.CommentBeanX;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.entity.InviteBean;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.entity.MyObtainGiftBean;
import com.jfkj.im.entity.RankListBean;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.entity.ResultRecordBean;
import com.jfkj.im.entity.SettleAccountBean;
import com.jfkj.im.entity.Setup;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.entity.inviteFriendAddressBean;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiStores {

     /**
     * 环境切换  --
     * 1.域名
     * 2.腾讯appid 切换
     * 3.bugly 腾讯切换
     *
     * 正式服
     *
     * @TGS#3SM5K5KGH 广场
     * @TGS#aWK6K5KGZ Iyou圈
     * 测试服
     *
     * @TGS#aHAFI5KGO Iyou圈
     * @TGS#3TIG3IJGR 广场
     */


//      /*  正式api*/
//     String base_url = "http://api.vipiyou.com";
//      /*正式 文件上传*/
//     String baseupload_url = "http://upload.vipiyou.com";
//      /*文件类域名*/
//     String baseJsonUrl = "http://file.vipiyou.com/";
//      /*正式*/
//     String h5BaseUrl = "http://file.vipiyou.com/static/iyou/vip.html";
//     /*   正式*/
//     String vip_url = "http://file.vipiyou.com/static/iyou/vip.html";



     /* 测试Api */
     String base_url = "http://iyapi.tiantiancaidian.com";
      /*本地Api*/
//     String base_url = "http://192.168.110.158:8081";
       /*测试文件上传*/
     String baseupload_url = "http://iyupload.tiantiancaidian.com";
    String baseJsonUrl = "http://file.vipiyou.com/";
    /*vip文件路径*/
    String h5BaseUrl = "http://iyfile.tiantiancaidian.com/static/iyou/vip.html";





     //获取国家地区代码
     String nationalareaurl ="/static/NationalArea.json";

    //vip特权地址
    String vip_url = "http://iyfile.tiantiancaidian.com/static/iyou/vip.html";

     /**
      * 版本更新confirmTask
      */
    @FormUrlEncoded
    @POST("/base/getNewVersionAndroid")
    Observable<AppVersion> getVersionAndroid(@FieldMap Map<String,String> map);

    /**
     * 软件更新
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> onDownload(@Url String url);

    /**
     获取用户俱乐部和个人任务
     */
    @FormUrlEncoded
    @POST("/home/getUserGroupTask")
    Observable<ResponseBody> getUserGroupTask(@FieldMap Map<String,String> map);

    /**
     获取用户俱乐部和个人任务
     */
    @FormUrlEncoded
    @POST("/home/getUserGroupTask")
    Observable<UserDetailClubBean> getUserDetailClub(@FieldMap Map<String,String> map);

    //获取验证码
    @GET("/base/getVPhone")
    Observable<ResponseBody> getVPhone(@QueryMap Map<String, String> map);

    //验证验证码是否正确
    @FormUrlEncoded
    @POST("/user/phoneLoginYZM")
    Observable<LoginBean> phoneLoginYZM(@FieldMap Map<String, String> map);

    //账号密码登录
    @FormUrlEncoded
    @POST("/user/phoneLoginYZM")
    Observable<ResponseBody> phoneLoginYZMS(@FieldMap Map<String, String> map);

//    // 上传图片
    @Multipart
    @POST("/base/uploadImage")
    Observable<ResponseBody> uploadImage(
            @Query("type") String type,
            @Query("mobileNo") String mobileNo,
            @Part MultipartBody.Part file);
    //    // 上传图片
    @Multipart
    @POST
    Observable<ResponseBody> uploadImages(
            @Url String url,
            @HeaderMap HashMap<String,String> map,
            @Part MultipartBody.Part file);


    //账号密码登录
    @FormUrlEncoded
    @POST("/user/phoneLogin")
    Observable<ResponseBody> phoneLogin(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("/user/verificationParameter")
    Observable<ResponseBody> verificationParameter(@FieldMap Map<String, String> map);
    //上传文件
    @Multipart
    // @FormUrlEncoded
    @POST("/base/uploadFile")
    Observable<ResponseBody> uploadFile(@Part MultipartBody.Part file);


    @Multipart
    // @FormUrlEncoded
    @POST
    Observable<ResponseBody> uploadSingleFileVideo(@Url String url,@Header("fileType") String fileType, @Header("pathType") String pathType,@Header("userId")String userId, @Part MultipartBody.Part file);

    /**
     * @param fileType 文件类型（1图片 2gif 3音频 4视频 5文件）
     * @param pathType 上传地址类型（1 个人用户信息 2 动态圈 3聊天文件 4 系统图标 5用户验证文件）
     * @param file
     * @return
     */
    @Multipart
    // @FormUrlEncoded
    @POST("/file/uploadSingleFile")
    Observable<ResponseFileUrl> uploadSingleFile(@Header("fileType") String fileType, @Header("pathType") String pathType, @Part MultipartBody.Part file);

    /**
     * 上传视频
     *
     * @param map
     * @param parts
     * @return
     */
    @Multipart
    @POST("/file/uploadSingleFile")
    Observable<ResponseFileUrl> upload(@HeaderMap Map<String, String> map, @Part MultipartBody.Part parts);

    /**
     * 多文件上传
     *
     * @param map
     * @return
     */
    @Multipart
    @POST("/file/uploadFiles")
    Observable<ResponseFileUrl> uploadFiles(@HeaderMap Map<String, String> map, @Part() List<MultipartBody.Part> partList);

    //用户注册
    @FormUrlEncoded
    @POST("/user/register")
    Observable<ResponseBody> register(@FieldMap Map<String, String> map);

    //用户提交审核
    @FormUrlEncoded
    @POST("/user/userExamine")
    Observable<ResponseBody> userExamine(@FieldMap Map<String, String> map);




    /**
     * 获取用户详情
     */
    @FormUrlEncoded
    @POST("/home/getUsertail")
    Observable<UserDetail> getUserDetail(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("/home/getUsertail")
    Observable<ResponseBody> getTRTCUserDetail(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("/home/getUsertail")
    Observable<ResponseBody> getchatUserDetail(@FieldMap Map<String, String> map);

    /**
     * 首页附近
     *

     * @return nearby
     */

    @FormUrlEncoded
    @POST("/home/homeNearby")
    Observable<ResponseBody> homeNearby(@FieldMap Map<String, String> map);

    //判断俱乐部名称是否存在
    @FormUrlEncoded
    @POST("/group/checkGroupName")
    Observable<ResponseBody> checkGroupName(@FieldMap Map<String, String> map);

    //创建俱乐部
    @FormUrlEncoded
    @POST("/group/createGroup")
    Observable<ResponseBody> createGroup(@FieldMap Map<String, String> map);

    //重置密码
    @FormUrlEncoded
    @POST("/user/updatePassword")
    Observable<ResponseBody> updatePassword(@FieldMap Map<String, String> map);

    //拉取好友列表
    @FormUrlEncoded
    @POST("/init/loadUserFriendList")
    Observable<ResponseBody> loadUserFriendList(@FieldMap Map<String, String> map);

    //获得IM服务器连接
    @FormUrlEncoded
    @POST("/user/getChatRoute")
    Observable<ResponseBody> getChatRoute(@FieldMap Map<String, String> map);

    //加载推荐群聊列表
    @FormUrlEncoded
    @POST("/group/loadRecommendGroupList")
    Observable<ResponseBody> loadRecommendGroupList(@FieldMap Map<String, String> map);

    //用户申请加入群聊
    @FormUrlEncoded
    @POST("/group/applyJoinGroup")
    Observable<ResponseBody> applyJoinGroup(@FieldMap Map<String, String> map);


    /**
     * 举报
     *
     * @param file 图片
     */
    @Multipart
    @POST("/home/toUserReport")
    Observable<ResponseBody> toUserResport(@Field("userId") String userId, @Field("type") String type, @Part MultipartBody.Part file);

    /**
     * 拉黑
     *
     * @param userId
     * @param type   1 拉黑 2解除(默认为1，解除待定)
     * @return
     */
    @FormUrlEncoded
    @POST("/home/toUserBlack")
    Observable<BaseResponse> toUserBlack(@Field("userId") String userId,
                                         @Field("type") String type,
                                         @Field("osName") String osName,
                                         @Field("channel") String channel,
                                         @Field("appVersion") String appVersion,

                                         @Field("deviceName") String deviceName,
                                         @Field("deviceId") String deviceId,
                                         @Field("reqTime") String reqTime);

    /**
     * editorIndividual编辑个人信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/my/editorIndividual")
    Observable<BaseResponse> editorInfo(@FieldMap Map<String, String> map);

    //获取图片验证码
    @GET("/base/getStaticCode")
    Observable<ResponseBody> getStaticCode(@QueryMap Map<String, String> map);

    //删除好友
    @FormUrlEncoded
    @POST("/friend/deleteFriend")
    Observable<ResponseBody> deleteFriend(@FieldMap Map<String, String> map);

    //群主邀请好友加入俱乐部
    @FormUrlEncoded
    @POST("/group/inviteJoinGroup")
    Observable<ResponseBody> inviteJoinGroup(@FieldMap Map<String, String> map);


    //加载俱乐部详情
    @FormUrlEncoded
    @POST("/group/loadGroupInfo")
    Observable<ResponseBody> loadGroupInfo(@FieldMap Map<String, String> map);

    //拉取俱乐部列表
    @FormUrlEncoded
    @POST("/init/loadUserGroupList")
    Observable<ResponseBody> loadUserGroupList(@FieldMap Map<String, String> map);

    /**
     * 获取状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST("/my/SetUp")
    Observable<Setup> setUp(@FieldMap Map<String,String> maps);

    /**
     * 修改在线状态，隐藏位置权限
     *
     * @return
     */
    @FormUrlEncoded
    @POST("/my/SetUpVIP")
    Observable<BaseResponse> setUpVip(@Field("hideOnline") String hideOnLine, @Field("hideLocation") String hideLocation);

    @FormUrlEncoded
    @POST("/my/SetUpVIP")
    Observable<BaseResponse> sethide(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/home/homeRecommend")
    Observable<HomeRecommend> homeRecommend(@Field("pageNo") String pageNo, @Field("pageSize") String pageSize, @Field("gender") String gender);
    @FormUrlEncoded
    @POST("/home/homeRecommend")
    Observable<HomeRecommend> homeRecommend(@FieldMap Map<String, String> map);

    /**

     * @return
     */
    @FormUrlEncoded
    @POST("/home/toUserReport")
    Observable<BaseResponse> toUserReport(@FieldMap Map<String,String> maps);

    /**
     * gift list
     *
     * @param osName     系统，android, ios
     * @param appVersion 版本号
     * @param channel    渠道
     * @return
     */
//    @FormUrlEncoded
//    @POST("/friend/getGiftList")
//    Observable<GiftData> getGiftList(@Field("osName") String osName, @Field(Utils.APPVERSION) String appVersion, @Field(Utils.CHANNEL) String channel);

    /**
     * 申请添加好友
     *
     * @param userId  被添加用户ID
     * @param giftId  赠送礼物ID
     * @param size    礼物数量
     * @param content 申请添加好友描述
     * @return
     */
    @FormUrlEncoded
    @POST("/friend/applyAddFriend")
    Observable<AddFriend> applyAddFriend(@Field("osName") String osName, @Field(Utils.APPVERSION) String appVersion, @Field(Utils.CHANNEL) String channel,
                                         @Field(Utils.USERID) String userId, @Field("giftId") String giftId, @Field("size") String size,
                                         @Field("content") String content);

    @FormUrlEncoded
    @POST("/friend/applyAddFriend")
    Observable<ResponseBody> applyAddFriend(@FieldMap Map<String, String> map);
    /**
     * 发布动态
     *
     * @return
     * @Field("content") String content,@Field("img") String iamge, @Field("location") String location,
     * @Field(Utils.LONGITUDE) String longitude, @Field(Utils.LATITUDE) String latitude,@Field("videoPath") String videoPath,
     * @Field("videoImage") String videoImage, @Field("videoTime") String videoTime,@Field("islocation") String isLocation
     */
    @FormUrlEncoded
    @POST("/find/publishDynamic")
    Observable<BaseResponse> publishDynamic(@FieldMap Map<String, String> map);

    /**
     * 上传个人视频
     */
    @FormUrlEncoded
    @POST("/my/publishDynamic")
    Observable<BaseResponse> uploadVideo(@Field("homeVideo") String homeVideo);
    @FormUrlEncoded
    @POST("/my/personalVideo")
    Observable<BaseResponse> uploadVideo(@FieldMap Map<String, String> map);
    /**
     * 发布动态
     *
     * @return
     */
    //群主邀请好友加入俱乐部
    @FormUrlEncoded
    @POST("/friend/updateFriend")
    Observable<ResponseBody> updateFriend(@FieldMap Map<String, String> map);


    //重置密码
    @FormUrlEncoded
    @POST("/user/updatePassword")
    Observable<BaseResponse> updatePassword(@Field("password") String pwd);


    /**
     * 点击我 获取信息
     */
    @FormUrlEncoded
    @POST("/my/homepage")
    Observable<MineInfo> homePage(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("/home/getPosition")
    Observable<BaseResponse> getPosition(@FieldMap Map<String, String> map);

    /**
     * 新人推荐
     */
    @FormUrlEncoded
    @POST("/home/homeNewPeople")
    Observable<HomeRecommend> homeNewPeople(@FieldMap Map<String, String> map);

    /**
     * 朋友圈
     *
     * @return
     */
    @FormUrlEncoded
    @POST("/home/getUserCarcleArcitle")
    Observable<CircleBean> getUserCircleArticle(@Field(Utils.LONGITUDE) String longitude, @Field(Utils.LATITUDE) String latitude, @Field(Utils.USERID) String userId);
    @FormUrlEncoded
    @POST("/home/getUserCarcleArcitle")
    Observable<CircleBean> getUserCircleArticle(@FieldMap Map<String, String> map);



    /**
     * tata 圈动态
     */
    @FormUrlEncoded
    @POST("/find/getfindDynamic")
    Observable<CircleBean> getFindDynamic(@Field("pageNo") String pageNo, @Field("pageSize") String pageSize);
    @FormUrlEncoded
    @POST("/find/getfindDynamic")
    Observable<CircleBean> getFindDynamic(@FieldMap Map<String, String> map);



    @FormUrlEncoded
    @POST("/find/getfindDynamic")
    Observable<ResponseBody> circlefriend(@FieldMap Map<String, String> map);
    /**
     * 评论朋友圈
     *
     * @param userId    评论用户id
     * @param articleId 动态id
     * @param content   评论内容 评论要传
     * @param type      1 评论 2删除评论
     * @param commentId 评论ID
     * @return
     */
    @FormUrlEncoded
    @POST("/home/setNewComment")
    Observable<BaseResponse> setComment(@Field("userId") String userId, @Field("articleId") String articleId, @Field("content") String content,
                                        @Field("type") String type, @Field("commentId") String commentId);
    @FormUrlEncoded
    @POST("/home/setComment")
    Observable<BaseResponse> setCommentHashMap(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("/home/findAllNewComment")
    Observable<CommentBeanX> findAllNewComment(@FieldMap Map<String,String> maps );

    /**
     * 点赞朋友圈
     *
     * @param articleId 朋友圈id
     * @param type      1 点赞 2取消点赞
     * @param praiseId  点赞id
     */
    @FormUrlEncoded
    @POST("/home/getUserPraise")
    Observable<PraiseIdBean> getUserPraise(@Field("articleId") String articleId, @Field("type") String type, @Field("praiseId") String praiseId);

    @FormUrlEncoded
    @POST("/home/getUserPraise")
    Observable<PraiseIdBean> getUserPraise(@FieldMap Map<String, String> map);

    @GET
    Observable<ResponseBody> loadTwice(@Url String url);


    @FormUrlEncoded
    @POST("/home/getUserPraise")
    Observable<ResponseBody> getUserPraise2(@FieldMap Map<String, String> map);


    /**
     * 删除动态
     *
     * @param articleId
     */
    @FormUrlEncoded
    @POST("/find/deleteDynamic")
    Observable<BaseResponse> deleteDynamic(@Field("articleId") String articleId);
    @FormUrlEncoded
    @POST("/find/deleteDynamic")
    Observable<BaseResponse> deleteDynamic(@FieldMap Map<String, String> map);
    /**
     * 加载用户所在群聊的所有群成员
     *
     * @return
     */

    @FormUrlEncoded
    @POST("/group/loadGroupMemberList")
    Observable<ResponseBody> loadGroupMemberList(@FieldMap Map<String, String> map);

    /**
     * -修改群设置
     * @return
     */
    @FormUrlEncoded
    @POST("/group/updateGroup")
    Observable<ResponseBody> updateGroup(@FieldMap Map<String, String> map);

    /**
     -群主删除群成员
     * @return
     */
    @FormUrlEncoded
    @POST("/group/delGroupMember")
    Observable<ResponseBody> delGroupMember(@FieldMap Map<String, String> map);

    /**
     -被邀请人处理入群消息
     * @return
     */
    @FormUrlEncoded
    @POST("/group/handlerInviteGroup")
    Observable<ResponseBody> handlerInviteGroup(@FieldMap Map<String, String> map);


    /**
     获得userSig
     * @return
     */
    @FormUrlEncoded
    @POST("/user/genUserSig")
    Observable<ResponseBody> genUserSig(@FieldMap Map<String, String> map);

    /**
     * 充值记录
     */
    @FormUrlEncoded
    @POST("/my/rechargeRecord")
    Observable<ChargeRecordBean> rechargeRecord(@FieldMap Map<String,String> map);




    /**
     * 贡献 / 魅力 排行榜
     * @return
     */
    @GET("/static/ranking/{type}.json")
    Observable<List<RankListBean>> rankList(@Path("type") String type);

    /**
     *  礼物
     * @param
     * @param
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/my/Gift")
    Observable<MyGiftBean> myGift(@FieldMap  Map<String,String> map);

    /**
     * 累计获得的礼物
     */
    @FormUrlEncoded
    @POST("/my/Gift")
    Observable<MyObtainGiftBean> ObmyGift(@FieldMap  Map<String,String> map);

    /**
     *   账户余额
     */
    @FormUrlEncoded
    @POST("/my/accountBalance")
    Observable<AccountBalance> accountBalance(@FieldMap Map<String, String> map);
    /**
     *
     */
    @FormUrlEncoded
    @POST("/group/quitGroup")
    Observable<ResponseBody> quitGroup(@FieldMap Map<String, String> map);

    /**
     * 修改密码验证旧密码
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/my/updatePasswordVerification")
    Observable<BaseResponse> updatePasswordVerification(@FieldMap Map<String,String> maps);

    /**
     *   账户余额
     */
    @FormUrlEncoded
    @POST("/group/handlerJoinGroup")
    Observable<ResponseBody> handlerJoinGroup(@FieldMap Map<String, String> map);

    /**
     * 处理申请好友消息
     */
    @FormUrlEncoded
    @POST("/friend/handlerApplyMessage")
    Observable<ResponseBody> handlerApplyMessage(@FieldMap Map<String, String> map);

    /**
     * loadOfflineMessage--拉取离线消息
     */
    @FormUrlEncoded
    @POST("/init/loadOfflineMessage")
    Observable<ResponseBody> loadOfflineMessage(@FieldMap Map<String, String> map);

    /**
     * 生成拼手气红包
     */
    @FormUrlEncoded
    @POST("/group/sendRedPacked")
    Observable<ResponseBody> sendRedPacked(@FieldMap Map<String, String> map);

    /**
     * 修改手机号 验证手机
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/my/updateMobileVerification")
    Observable<BaseResponse> updateMobileVerification(@FieldMap Map<String,String> maps);

    /**
     * 修改手机号
     */
    @FormUrlEncoded
    @POST("/my/updateMobile")
    Observable<BaseResponse> updateMobile(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/my/updatePassword")
    Observable<BaseResponse> updateMyPassword(@FieldMap Map<String, String> map);

    /**
     * 获取验证码 1
     * @param
     * @return
     */
    @GET("/base/getVPhone")
    Observable<BaseResponse> getPhoneCode(@QueryMap  Map<String,String> map);


    /**
     * 抢红包检测
     */
    @FormUrlEncoded
    @POST("/group/checkRedPacket")
    Observable<ResponseBody> checkRedPacket(@FieldMap Map<String, String> map);

    /**
     * 拆红包
     */
    @FormUrlEncoded
    @POST("/group/receiveRedPacket")
    Observable<ResponseBody> receiveRedPacket(@FieldMap Map<String, String> map);

    /**
     * 加载红包记录详情
     */
    @FormUrlEncoded
    @POST("/group/loadRedPacketDetails")
    Observable<ResponseBody> loadRedPacketDetails(@FieldMap Map<String, String> map);

    /**
     * -加载红包领取记录列表
     */
    @FormUrlEncoded
    @POST("/group/loadRedPacketReceiveList")
    Observable<ResponseBody> loadRedPacketReceiveList(@FieldMap Map<String, String> map);

    /**
     * -获得礼物列表
     */
    @FormUrlEncoded
    @POST("/friend/getGiftList")
    Observable<GiftData> getGiftList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/friend/getGiftList")
    Observable<GiftData> gethomeGiftList(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("/friend/getGiftList")
    Observable<ResponseBody> getGiftListResponseBody(@FieldMap Map<String, String> map);
    /**
     * -单聊赠送礼物
     */
    @FormUrlEncoded
    @POST("/friend/giveFriendGift")
    Observable<ResponseBody> giveFriendGift(@FieldMap Map<String, String> map);

    /**
     * -领取礼物
     */
    @FormUrlEncoded
    @POST("/friend/receiveFriendGift")
    Observable<ResponseBody> receiveFriendGift(@FieldMap Map<String, String> map);

    /**
     * 发现圈tata圈该动态
     * ,@Field("pageNo") String pageNo,@Field("pageSize") String pageSize
     */
    @FormUrlEncoded
    @POST("/find/getArticleDynamic")
    Observable<ArticleDynamicBean> getArticleDynamic(@Field("articleId") String articleId, @Field("ruserid") String ruserid);
    @FormUrlEncoded
    @POST("/find/getArticleDynamic")
    Observable<ArticleDynamicBean> getArticleDynamic(@FieldMap Map<String, String> map);

    /**
     * 我的消息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("/find/getfindDynamicCommen")
    Observable<MyDynamicMessage> getFindDynamicMessage(@Field("pageNo") String pageNo, @Field("pageSize") String pageSize);
    @FormUrlEncoded
    @POST("/find/getfindDynamicCommen")
    Observable<MyDynamicMessage> getFindDynamicMessage(@FieldMap Map<String, String> map);



    @FormUrlEncoded
    @POST("/my/exchangeList")
    Observable<ExChangeBean> exchangeList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("/my/exchangeDiamond")
    Observable<BaseResponse> exchangeDiamond(@FieldMap Map<String,String> map);



    /**
     * -群主回复申请加入群聊消息
     */
    @FormUrlEncoded
    @POST("/group/replyJoinGroupMessage")
    Observable<ResponseBody> replyJoinGroupMessage(@FieldMap Map<String, String> map);


    /**
     * -用户提交审核
     */
    @FormUrlEncoded
    @POST("user/setExamine")
    Observable<ResponseBody> setExamine(@FieldMap Map<String, String> map);

    /**
     * -用户提交审核
     */
    @FormUrlEncoded
    @POST("/group/loadGroupMemberInfo")
    Observable<ResponseBody> loadGroupMemberInfo(@FieldMap Map<String, String> map);

    /**
     * -发言支付钻石-
     */
    @FormUrlEncoded
    @POST("/square/paySpeakOrder")
    Observable<ResponseBody> paySpeakOrder(@FieldMap Map<String, String> map);

    /**
     * -拉取广场消息记录-
     */
    @FormUrlEncoded
    @POST("/square/loadSquareMessage")
    Observable<ResponseBody> loadSquareMessage(@FieldMap Map<String, String> map);


    /**

     */
    @FormUrlEncoded
    @POST("/square/sendSquareRedPacket")
    Observable<ResponseBody> sendSquareRedPacket(@FieldMap Map<String, String> map);


    /**
     更换问题
     */
    @FormUrlEncoded
    @POST("/square/replaceQuestion")
    Observable<ResponseBody> replaceQuestion(@FieldMap Map<String, String> map);
    /**
     更换问题
     */
    @FormUrlEncoded
    @POST("/square/getTestQuestion")
    Observable<ResponseBody> getTestQuestion(@FieldMap Map<String, String> map);

    /**
     更换问题
     */
    @FormUrlEncoded
    @POST("/square/sendSquareGame")
    Observable<ResponseBody> sendSquareGame(@FieldMap Map<String, String> map);
    /**

     */
    @FormUrlEncoded
    @POST("/square/loadSquareGameList")
    Observable<ResponseBody> loadSquareGameList(@FieldMap Map<String, String> map);

    /**

     */
    @FormUrlEncoded
    @POST("/square/loadUserAnswerList")
    Observable<ResponseBody> loadUserAnswerList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("base/selectAdvertisement")
    Observable<ResponseBody> selectAdvertisement(@FieldMap Map<String, String> map);


    /**
     submitUserAnswer
     */
    @FormUrlEncoded
    @POST("/square/submitUserAnswer")
    Observable<ResponseBody> submitUserAnswer(@FieldMap Map<String, String> map);

    /**

     */
    @FormUrlEncoded
    @POST("/square/loadSquareGameQuestion")
    Observable<ResponseBody> loadSquareGameQuestion(@FieldMap Map<String, String> map);

    /**
     comparisonAnswer
     */
    @FormUrlEncoded
    @POST("/square/comparisonAnswer")
    Observable<ResponseBody> comparisonAnswer(@FieldMap Map<String, String> map);

    /**
     comparisonAnswer
     */
    @FormUrlEncoded
    @POST("/square/getSquareTips")
    Observable<ResponseBody> getSquareTips(@FieldMap Map<String, String> map);

    /**

     */
    @FormUrlEncoded
    @POST("/user/loginOut")
    Observable<ResponseBody> loginOut(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("/my/inviteFriends")
    Observable<ResponseBody> inviteFriends(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/home/addAccessSpace")
    Observable<ResponseBody> addAccessSpace(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/my/czModeList")
    Observable<ResponseBody> czModeList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("/find/getRanking")
    Observable<ResponseBody> getRanking(@FieldMap Map<String, String> map);

    /**
     * 上传头像
     *
     */
    @FormUrlEncoded
    @POST("/my/portraitExamine")
    Observable<ResponseBody> portraitExamine(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/my/inviteFriendAddress")
    Observable<inviteFriendAddressBean> inviteFriendAddress(@FieldMap Map<String,String> map);


    /**
     * 实名认证
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/my/authentication")
    Observable<BindSettleAccountBean> authentication(@FieldMap Map<String,String> map);


    /**
     * 绑定结算账户
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/my/bindBank")
    Observable<BindSettleAccountBean> bindBank (@FieldMap Map<String,String> map);

    /**获取绑定结算用户信息*/
    @FormUrlEncoded
    @POST("/my/getUFRealName")
    Observable<GetBindBankBean> getBindBank (@FieldMap Map<String,String> map);


    /**
     * 获取我的结算比例
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/my/getUserCashRate")
    Observable<CarryScaleBean>  getUserCashRate(@FieldMap Map<String,String> map);


    /**
     * 结算记录
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/my/settlementRecord")
    Observable<ResultRecordBean>  settlementRecord(@FieldMap Map<String,String> map);


    @FormUrlEncoded
    @POST("/my/settlement")
    Observable<SettleAccountBean> settlement(@FieldMap Map<String,String> map);


    @FormUrlEncoded
    @POST("/my/bankCardInfo")
    Observable<CardInfoBean> bankCardInfo(@FieldMap Map<String,String> map);


    @FormUrlEncoded
    @POST("/my/settlementApplication")
    Observable<BaseResponse> settlementApplication(@FieldMap Map<String,String> map);



    @FormUrlEncoded
    @POST("/my/addMoney")
    Observable<ResponseBody> addMoney(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("my/inviteFriendTail")
    Observable<InviteBean>  inviteFriendTail(@FieldMap Map<String,String> map);



    @FormUrlEncoded
    @POST("/friend/pickFemaleAdd")
    Observable<BaseResponse> pickFemaleAdd(@FieldMap Map<String,String> map);

    /*破冰任务大厅*/
    @FormUrlEncoded
    @POST("/task/selectAllTaskByGender")
    Observable<BaseBeans<CrushIceTaskHallBean>> crushIceTask(@FieldMap Map<String,String> map);

    /*聚会报名*/
    @FormUrlEncoded
    @POST("party/selectAllParty")
    Observable<BaseBeans<PartyBean>> partyRegiser(@FieldMap Map<String,String> map);

    /*我的聚会*/
    @FormUrlEncoded
    @POST("/party/myPartys")
    Observable<BaseBeans<PartyBean>> myParty(@FieldMap Map<String,String> map);

    /**编辑我的任务*/
    @FormUrlEncoded
    @POST("/task/editTask")
    Observable<BaseBean> submitTask(@FieldMap Map<String,String> map);


    /**
     * 聚会详情
     */
    @FormUrlEncoded
    @POST("/party/partyInfo")
    Observable<PartyInfoBean> partyInfo(@FieldMap Map<String,String> map);


    /**
     * 报名参加
     */
    @FormUrlEncoded
    @POST("/party/joinParty")
    Observable<BaseBean> joinParty(@FieldMap Map<String,String> maps);

    /**
     * 应邀用户
     */
    @FormUrlEncoded
    @POST("/party/inviteAllUsers")
    Observable<SignInBean> inviteAllUsers(@FieldMap Map<String,String> map);

    /**
     *发起聚会
     */
    @FormUrlEncoded
    @POST("/party/addParty")
    Observable<BaseBean> addParty(@FieldMap Map<String,String> map);

    /**
     * 确认到场
     */
    @FormUrlEncoded
    @POST("/party/confirmArrival")
    Observable<BaseBean> confirmArrival(@FieldMap Map<String,String> maps);

    /**
     * 投诉
     */
    @FormUrlEncoded
    @POST("/party/submitComplaint")
    Observable<BaseBean> submitComplaint(@FieldMap Map<String,String> maps);


    /**
     * 查询需要点评已结束的聚会
     */
    @FormUrlEncoded
    @POST("/party/selectOneEndParty")
    Observable<BaseBean> selectOneEndParty(@FieldMap Map<String,String> maps);


    @FormUrlEncoded
    @POST("/home/selectCharmNumberOne")
    Observable<BaseBeans<TodayStarBean>> getTodayStar(@FieldMap Map<String,String> maps);


    /**
     * 聚会返回前端是否展示红点
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/party/allMyPartysRedType")
    Observable<AllMyPartysRedBean>  allMyPartysRedType(@FieldMap Map<String,String> map);


    /**
     * 确认邀约
     */
    @FormUrlEncoded
    @POST("/party/confirmInvitation")
    Observable<BaseBean> confirmInvitation(@FieldMap Map<String,String> map);

    //获取用户
    @FormUrlEncoded
    @POST("/user/getUserInfo")
    Observable<ResponseBody> getUserInfo (@FieldMap Map<String,String> map);

    /**
     *
     * 添加好友
     * */
    @FormUrlEncoded
    @POST("/friend/addTaskFriend")
    Observable<BaseBean> addFrend(@FieldMap Map<String,String> map);
}


