package com.jfkj.im.ui.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.github.iielse.imageviewer.ImageViewerBuilder;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupEveryday;
import com.jfkj.im.Bean.IceSubmitBean;
import com.jfkj.im.Bean.IsFriendBean;
import com.jfkj.im.Bean.MyPicPhoto;
import com.jfkj.im.Bean.TUserBean;
import com.jfkj.im.Bean.TipsStartActivity;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.BaseTimFragment;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.component.AudioPlayer;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.helper.ChatLayoutHelper;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.ChatLayout;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.chat.layout.input.InputLayout;
import com.jfkj.im.TIM.modules.chat.layout.message.MessageLayout;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.HeadUrlNameUtils;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.RedPackageDetailsActivity;
import com.jfkj.im.TIM.redpack.RedPackageReceiver;
import com.jfkj.im.TIM.redpack.RedPackageUtil;
import com.jfkj.im.TIM.redpack.UnreadMessageNumUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomUtil;
import com.jfkj.im.TIM.redpack.chatroom.MyCharacterTextActivity;
import com.jfkj.im.TIM.redpack.chatroom.PayChatRoomUtils;
import com.jfkj.im.TIM.redpack.game.SubmissionTaskDialogFragment;
import com.jfkj.im.TIM.redpack.game.TimeTaskUtils;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.TIM.redpack.group.GroupInformationActivity;
import com.jfkj.im.TIM.redpack.group.SubmitGroupEverydayTaskDialogFragment;
import com.jfkj.im.TIM.redpack.ice.GiveThumbsUpBean;
import com.jfkj.im.TIM.redpack.ice.IceUtils;
import com.jfkj.im.TIM.redpack.ice.RedpacketIceUtils;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.SendPayChatRoomEvent;
import com.jfkj.im.event.GiveThumbsUpEvent;
import com.jfkj.im.event.JumpRedPackageDetailsEvent;
import com.jfkj.im.event.MessageFailEvent;
import com.jfkj.im.event.RedPackageEvent;
import com.jfkj.im.event.RedPackageIceEvent;
import com.jfkj.im.event.ShowDialogEvent;
import com.jfkj.im.event.ShowPicEvent;
import com.jfkj.im.event.TaskRedPackageEvent;
import com.jfkj.im.event.TimeTaskEvent;
import com.jfkj.im.event.UpdateMessageEvent;
import com.jfkj.im.imageviewer.MyCustomController;
import com.jfkj.im.imageviewer.MySimpleLoader;
import com.jfkj.im.imageviewer.MyTestDataProvider;
import com.jfkj.im.imageviewer.MyTransformer;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.service.FloatViewService;
import com.jfkj.im.ui.activity.ChatsetActivity;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.ProgressDownDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.SendMessageDialogFragment;
import com.jfkj.im.ui.dialog.SpannableStringBaseDialogFragment;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.AtGroupUtils;
import com.jfkj.im.utils.BitmapUtil;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.TimeChange;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videocompression.Util;
import com.jfkj.im.widget.GiftBottomDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.friendship.TIMCheckFriendResult;
import com.tencent.imsdk.friendship.TIMFriendCheckInfo;
import com.tencent.imsdk.friendship.TIMFriendRelationType;
import com.tencent.imsdk.friendship.TIMFriendResponse;
import com.tencent.imsdk.friendship.TIMFriendResult;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TEN;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.jfkj.im.utils.Utils.START_ACTIVITY_CLUB_INFO_CHAT_FRAGMENT;
import static com.jfkj.im.utils.Utils.START_ACTIVITY_DETAIL_CHAT_FRAGMENT;
import static com.tencent.imsdk.friendship.TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE;

public class ChatFragment extends BaseTimFragment {


    private View mBaseView;
    private ChatLayout mChatLayout;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;
    ConversationMessage conversationMessage;

//    private IntentFilter intentFilter;
//    private RedPackageReceiver redPackageReceiver;
//    private LocalBroadcastManager localBroadcastManager;

    private TimeCountUtil mCountUtilTpis, mCountUtil,mCountUtilGroupEveryDayTask;

    private List<LocalMedia> selectList = new ArrayList<>();
    private String redId, redRec;
    private TIMConversation conversation;
    private LoadingDialog loadingDialog;
    private CommonDialog friendDialog;

    /**每日任务Id*/
    private String groupEveryDayTaskId;
    private boolean isFristEveryDay;
    private TIMGroupDetailInfo groupDetailInfo;



    private List<TUserBean> tBean = new ArrayList<>();
    private List<String> userIds;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.chat_fragment, container, false);

        tBean = new ArrayList<>();

        return mBaseView;
    }

    private void initView() {
        //从布局文件中获取聊天面板组件
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);
        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault(getChildFragmentManager(), mChatInfo);

        /*
         * 需要聊天的基本信息
         */

        mChatLayout.setChatInfo(mChatInfo);
        if (getArguments() != null) {
            conversationMessage = (ConversationMessage) getArguments().getParcelable(Utils.CONVERSATIONMESSAGE);
        }
        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();

        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        TIMFriendCheckInfo checkinfo = new TIMFriendCheckInfo();
        List<String> list = new ArrayList<>();
        list.add(mChatInfo.getId());
        checkinfo.setUsers(list);
        TIMFriendshipManager.getInstance().checkFriends(checkinfo, new TIMValueCallBack<List<TIMCheckFriendResult>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMCheckFriendResult> timCheckFriendResults) {

                if (timCheckFriendResults.get(0).getResultType() == TIMFriendRelationType.TIM_FRIEND_RELATION_TYPE_NONE) {
                    mTitleBar.getRightIcon().setVisibility(View.GONE);
                } else {
                    mTitleBar.getRightIcon().setVisibility(View.VISIBLE);
                    if (mChatInfo.getType() == TIMConversationType.C2C) {
                        mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ChatsetActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
                                intent.putExtra(Utils.CONVERSATIONMESSAGE, conversationMessage);
                                Objects.requireNonNull(getActivity()).startActivity(intent);
                            }
                        });
                    }
                }
            }
        });


        ((SimpleItemAnimator) mChatLayout.getMessageLayout().getItemAnimator()).setSupportsChangeAnimations(false);


        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo) {
                    return;
                }
                ChatInfo info = new ChatInfo();
                info.setId(messageInfo.getFromUser());
                info.setChatRoom(false);
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", messageInfo.getFromUser());
                Objects.requireNonNull(getActivity()).startActivity(intent);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onUserLongClick(View view, int position, MessageInfo messageInfo) {


                if (messageInfo.isGroup()) {


                    InputLayout inputLayout = mChatLayout.getInputLayout();
                    EditText inputText = inputLayout.getInputText();

                    //查询@的UserName
                    TIMUserProfile selfInfo = TIMFriendshipManager.getInstance().queryUserProfile(messageInfo.getFromUser());


                    String aTStr = "@" + selfInfo.getNickName();


                    if (tBean == null) {
                        tBean = new ArrayList<>();
                    }
                    tBean.add(new TUserBean(selfInfo.getNickName(), messageInfo.getFromUser()));


                    //不能重复@
                    if (!inputText.getText().toString().contains(aTStr)) {

                        inputText.setText(inputText.getText().toString() + "" + aTStr + " ");

                       String edStr =  AtGroupUtils.setAtImageSpan(getContext(), inputText, inputText.getText().toString() + " " + aTStr);

                        KeyBoardUtils.requestShowKeyBord(inputText);

                        inputLayout.setOnLongUserIcon(new InputLayout.OnLongUser() {
                            @Override
                            public void onLongUserIconListener(InputLayout.MessageHandler mMessageHandler) {

                                if (userIds != null) {
                                    userIds.clear();
                                    userIds = null;
                                }
                                userIds = new ArrayList<>();
                                //AtMessage     @aaaa @bbb


                                String atMessage = inputText.getText().toString();
                                for (int i = 0; i < tBean.size(); i++) {
                                    if (!atMessage.contains("@" + tBean.get(i).getUserName())) {
                                        tBean.remove(i);
                                    } else {
                                        userIds.add(tBean.get(i).getUserId());
                                    }
                                }

                                //点击发送按钮
                                mMessageHandler.sendMessage(MessageInfoUtil.buildTextMessage(inputText.getText().toString().trim(), userIds,inputText.getText().toString()));

                                tBean.clear();
                            }
                        });

                    }
//
                }
            }
        });

        if (mChatInfo.isChatRoom()) {
            if (mCountUtilTpis == null) {
                mChatLayout.mChatRoomLayout.setVisibility(View.VISIBLE);
                mChatLayout.mChatRoomLayout.getContent().setText("广场严禁不文明用语 违规将严格处理");
                mChatLayout.mChatRoomLayout.getContentExtra().setVisibility(View.GONE);
                mCountUtilTpis = new TimeCountUtil(5000, 1000, new CountDownTimeListener() {
                    @Override
                    public void onTimeFinish() {
                        mChatLayout.mChatRoomLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTimeTick(long millisUntilFinished) {

                    }
                });
                mCountUtilTpis.start();
            }

            ChatRoomUtil.getSquareTips(new TIMValueCallBack<Integer>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(Integer integer) {
                    if (integer == 1) {
                        TipsBaseDialogFragment tipsBaseDialogFragment
                                = new TipsBaseDialogFragment(true, Gravity.CENTER,
                                "你发布的性格测试已完成，立即前往查看般配的TA吧！", "取消", "立即查看", false);
                        tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                            @Override
                            public void Rsp(Boolean s) {
                                if (s) {
                                    Intent intent = new Intent(App.getAppContext(), MyCharacterTextActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    App.getAppContext().startActivity(intent);
                                }
                            }
                        });
                        tipsBaseDialogFragment.show(getChildFragmentManager(), "");
                    }
                }
            });
//            destroyServise();
            SPUtils.getInstance(getContext()).put(Utils.IS_NEW_MESSAGE, "");
            SPUtils.getInstance(getContext()).put(Utils.IS_NEW_MESSAGE_ROLL, false);
        } else {
            if (mChatInfo.getType() == TIMConversationType.Group) {
                UnreadMessageNumUtils.setReadMessage(TIMConversationType.Group, mChatInfo.getId());
            }
        }

        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog);


        /***更新每日dialog*/
        if (mChatInfo.getType() == TIMConversationType.Group) {

            if(conversationMessage!=null){
            //更新@已读消息
                SPUtils.getInstance(getContext()).put(conversationMessage.getId(), "");
            }

            if (!mChatInfo.isChatRoom()) {
                groupDetailInfo = GroupInfoSetUtils.queryGroupInfo(mChatInfo.getId());
                String currDay = TimeChange.getCurrDay();


                /**这里是否显示每日任务*/
                if (TextUtils.isEmpty(SPUtils.getInstance(getActivity()).getString(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_DAY))){//第一次进入没有添加过的时间

                    SPUtils.getInstance(getActivity()).put(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_DAY, currDay);
                    SPUtils.getInstance(getActivity()).put(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_CLOSE, false);
                    getEveryDay(currDay);

                } else if ( !SPUtils.getInstance(getActivity()).getString(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_DAY).equals(currDay)) {//没有进入过，

                    SPUtils.getInstance(getActivity()).put(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_DAY, currDay);
                    SPUtils.getInstance(getActivity()).put(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_CLOSE, false);
                    getEveryDay(currDay);

                } else if (SPUtils.getInstance(getActivity()).getString(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_DAY).equals(currDay)
                        && !SPUtils.getInstance(getActivity()).getBoolean(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_CLOSE)) {//是今天已经进入过，切已经关闭了

                    getEveryDay(currDay);

                }
            }
        }
    }

    private void getEveryDay( String currDay) {
        /**
         * 任务类型(1:照片 2视频)
         * 每日任务*/
        TimeTaskUtils.EveryDayTask(mChatInfo.getId(), new TIMValueCallBack<GroupEveryday>() {
            @Override
            public void onError(int i, String s) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(GroupEveryday groupEveryday) {
                groupEveryDayTaskId = groupEveryday.getItaskid();

                if (groupEveryDayTaskId == null) return;

                /**成员显示提示信息*/
                if (groupDetailInfo != null && TextUtils.equals(groupDetailInfo.getGroupOwner(), UserInfoManger.getUserId())){
                    if (TextUtils.isEmpty(SPUtils.getInstance(getActivity()).getString(Utils.APPID+Utils.CHAT_GROUP_OLD_DAY))){
                        SPUtils.getInstance(getActivity()).put(Utils.APPID+Utils.CHAT_GROUP_OLD_DAY, currDay);
                        showDay();
                    } else if (!SPUtils.getInstance(getActivity()).getString(Utils.APPID+Utils.CHAT_GROUP_OLD_DAY).equals(currDay)) {
                        SPUtils.getInstance(getActivity()).put(Utils.APPID+Utils.CHAT_GROUP_OLD_DAY, currDay);
                        showDay();
                    }
                }

                /**判断是否是成员或者群主*/
                if (groupDetailInfo != null && TextUtils.equals(groupDetailInfo.getGroupOwner(), UserInfoManger.getUserId())){
                    mChatLayout.constraint_daily_tasks.setVisibility(View.VISIBLE);
                    mChatLayout.tv_daily_sure.setText("关闭");
                    showTopTime(groupEveryday);
                } else {
                    /**判断是否已经提交过每日任务*/
                    if (TextUtils.equals("0",groupEveryday.getSubmitType())){
                        mChatLayout.constraint_daily_tasks.setVisibility(View.VISIBLE);
                        mChatLayout.tv_daily_sure.setText("提交");
                        showTopTime(groupEveryday);
                        if (!isFristEveryDay) {
                            show2Day();
                        }
                    } else {
                        mChatLayout.constraint_daily_tasks.setVisibility(View.GONE);
                    }
                }
                isFristEveryDay = true;
            }
        });
    }

    /**展示提交每日任务信息*/
    private void showTopTime(GroupEveryday groupEveryday) {
        if (mCountUtilGroupEveryDayTask == null) {
            if (TextUtils.isEmpty(groupEveryday.getSurplustime())) return;
            mCountUtilGroupEveryDayTask = new TimeCountUtil((Integer.parseInt(groupEveryday.getSurplustime()) * 1000), 1000, new CountDownTimeListener() {
                @Override
                public void onTimeFinish() {
                    mChatLayout.constraint_daily_tasks.setVisibility(View.GONE);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeTick(long millisUntilFinished) {

                    //一小时
                    long totalSeconds = millisUntilFinished/1000;
                    long seconds = totalSeconds % 60;
                    long minutes = (totalSeconds/60)%60;
                    long hours = totalSeconds/3600;
                    String time = null;
                    if (hours > 1) {
                        time = "<html><body style='font-size:10px;><p><font size='12px'>剩余<size><font color='#FFDE5F'>"+ hours+"时"+minutes+"分"+seconds+"秒</font></size></font></p></body></html>";
                    } else {
                        time = "<html><body style='font-size:10px;><p><font size='12px'>剩余<size><font color='#FFDE5F'>"+ minutes+"分"+seconds+"秒</font></size></font></p></body></html>";
                    }

                    mChatLayout.tv_daily_time.setText(Html.fromHtml(time));
                }
            });
            mCountUtilGroupEveryDayTask.start();
        }
        if (groupEveryday.getItasktype().equals("1")){
            mChatLayout.tv_daily_content.setText("【图片】"+groupEveryday.getTaskcontent());
        } else {
            mChatLayout.tv_daily_content.setText("【视频】"+groupEveryday.getTaskcontent());
        }
        showEveydayDialog(groupEveryday,groupDetailInfo);
    }

    private void showEveydayDialog(GroupEveryday groupEveryday,TIMGroupDetailInfo groupDetailInfo) {
        mChatLayout.tv_daily_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupDetailInfo != null && TextUtils.equals(groupDetailInfo.getGroupOwner(), UserInfoManger.getUserId())){
                    SPUtils.getInstance(getActivity()).put(Utils.APPID+groupDetailInfo.getGroupId()+Utils.CHAT_GROUP_OLD_EVERY_CLOSE,true);
                    mChatLayout.constraint_daily_tasks.setVisibility(View.GONE);
                } else {
                    SubmitGroupEverydayTaskDialogFragment submitGroupEverydayTaskDialogFragment = new SubmitGroupEverydayTaskDialogFragment(false,Gravity.BOTTOM);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chat", mChatInfo);
                    bundle.putString("taskId", groupEveryday.getItaskid());
                    submitGroupEverydayTaskDialogFragment.setArguments(bundle);
                    submitGroupEverydayTaskDialogFragment.Rsp(new ResponListener<Boolean>() {
                        @Override
                        public void Rsp(Boolean s) {
                            if (s) {
                                if (groupEveryday.getItasktype().equals("1")){
                                    choiceVideo("1",1);
                                } else {
                                    choiceVideo("2",2);
                                }
                            }
                        }
                    });
                    submitGroupEverydayTaskDialogFragment.show(getChildFragmentManager(),"");
                }


            }
        });
    }


    /**
     * 跳转
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventStartActivity(TipsStartActivity type){
        switch (type.getType()){
            case START_ACTIVITY_DETAIL_CHAT_FRAGMENT:
                Intent intent = new Intent(getContext(),UserDetailActivity.class);
                intent.putExtra("userId",type.getId());
                Objects.requireNonNull(getContext()).startActivity(intent);
                break;

            case START_ACTIVITY_CLUB_INFO_CHAT_FRAGMENT:
                Intent cIntent = new Intent(getContext(), GroupInformationActivity.class);
                cIntent.putExtra("type", true);
                cIntent.putExtra(Utils.GROUPID, type.getId());
                startActivity(cIntent);
                break;

        }
    }

    private void showDay() {
        String content = "俱乐部每天都有日常任务，会员们每天都要完成任务才能留在俱乐部里。<br>" +
                "<font color ='#FF531A' <small>系统每天会随机发照片或视频任务。</small></font>";
        Spanned spanned = Html.fromHtml(content);
        VipSetGradeDialogFragment dialogFragment =
                new VipSetGradeDialogFragment(true, Gravity.CENTER, spanned,"确认");
        dialogFragment.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {

                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "");
    }

    private void show2Day() {
        String content = "您收到一条俱乐部任务，请在指定时间内完成！认真做好任务才能留在俱乐部里。<br>" +
                "<font color ='#FF531A' <small>若在制定时间内未完成任务，将被系统自动移出俱乐部。</small></font>";
        Spanned spanned = Html.fromHtml(content);
        VipSetGradeDialogFragment dialogFragment =
                new VipSetGradeDialogFragment(true, Gravity.CENTER, spanned,"确认");
        dialogFragment.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {

                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "");
    }

    public void destroyServise() {
        // 销毁悬浮窗
        Intent intent = new Intent(getContext(), FloatViewService.class);
        //终止FloatViewService
        Objects.requireNonNull(getActivity()).stopService(intent);
    }


    /**
     * 添加好友
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addFriend(IsFriendBean bean) {
        showExitDialog(bean);
    }

    private void showExitDialog(IsFriendBean bean) {
        FriendsUtils.checkFriend(bean.getUid(), new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int i, String s) {
                ToastUtils.showLongToast(s);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    friendDialog = new CommonDialog(getContext(), R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.tv_cancel:
                                    friendDialog.dismiss();
                                    break;
                                case R.id.tv_confirm:
                                    //添加好友
                                    friendDialog.dismiss();
                                    getGiftList(bean.getUid());
                                    break;
                            }
                        }
                    });
                    String htmlStr = "你和对方暂未建立好友关系，添加 对方为好友后便于线下聚会沟通";
                    friendDialog.setTitleText("温馨提示").setYesBtn("添加好友").setContentText(htmlStr).show();
                    friendDialog.show();
                }
            }
        });

    }

    private GiftBottomDialog mSheetDialog;

    /**
     * 礼物viewpager
     */
    private void showBottomDialog(List<GiftData.DataBean.ArrayBean> list, String id) {
        mSheetDialog = new GiftBottomDialog(getContext(), R.style.Dialog_Light, list, getActivity());
        mSheetDialog.setUserId(id);
        mSheetDialog.setName("");
        mSheetDialog.show();
    }

    //获取礼物列表
    public void getGiftList(String uid) {
        String string = SPUtils.getInstance(App.getAppContext()).getString(Utils.GETGIFTLIST);
        GiftData giftData = JSON.parseObject(string, GiftData.class);
        showBottomDialog(giftData.getData().getArray(), uid);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRedPackage(RedPackageEvent data) {
        RedPackageUtil redPackageUtil = new RedPackageUtil(this, data, mChatInfo);

        redPackageUtil.testing(mChatInfo.isChatRoom()?"1":"0");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDate(UpdateMessageEvent event) {
        if (event.isUpdate()) {
            Bundle bundle = getArguments();
            mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
            if (mChatInfo == null) {
                return;
            }
            initView();

            ChatLayoutHelper helper;
            // TODO 通过api设置ChatLayout各种属性的样例
            if (mChatInfo.getType() == TIMConversationType.C2C) {
                helper = new ChatLayoutHelper(getActivity(), 1, getChildFragmentManager());
            } else {
                if (mChatInfo.isChatRoom()) {
                    helper = new ChatLayoutHelper(getActivity(), 4, getChildFragmentManager());
                } else {
                    helper = new ChatLayoutHelper(getActivity(), 2, getChildFragmentManager());
                }
            }
            helper.customizeChatLayout(mChatLayout, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRedPackageJumpDetials(JumpRedPackageDetailsEvent jumpRedPackageDetailsEvent) {
        Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), RedPackageDetailsActivity.class)
                .putExtra("redId", jumpRedPackageDetailsEvent.getRedId())
                .putExtra("id", jumpRedPackageDetailsEvent.getId())
                .putExtra("state", jumpRedPackageDetailsEvent.getState()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDialog(ShowDialogEvent showDialogEvent) {
        if (showDialogEvent.isDialog()) {
            SubmissionTaskDialogFragment submissionTaskDialogFragment
                    = new SubmissionTaskDialogFragment(false, Gravity.BOTTOM);
            Bundle bundle = new Bundle();
            bundle.putSerializable("chat", mChatInfo);
            bundle.putString("taskRedpackId", showDialogEvent.getRedPackageId());
            submissionTaskDialogFragment.setArguments(bundle);
            submissionTaskDialogFragment.setResponListener(new ResponListeners<Integer, String>() {
                @Override
                public void Rsps(Integer s, String id) {
                    if (s == 1) {
                        redId = id;
                        redRec = showDialogEvent.getRedRec();
                        choiceVideo("2",0);
                    }
                }
            });
            submissionTaskDialogFragment.show(getChildFragmentManager(), "");
        }

    }

    private void choiceVideo(String type,int resultId) {

        int themeId = R.style.picture_default_style;
        int maxSelectNum = 1;
        //进入相册

        int types = 0;
        if (TextUtils.equals(type,"1")){
            types = PictureMimeType.ofImage();
        } else {
            types = PictureMimeType.ofVideo();
        }

        int result = 0;
        if (resultId == 0) {
            result = PictureConfig.CHOOSE_REQUEST;
        } else if (resultId == 1) {
            result = PictureConfig.EVERY_DAT_IMG;
        } else {
            result = PictureConfig.EVERY_DAT_VIDEO;
        }


        PictureSelector.create(this)
                .openGallery(types)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                //                .setLanguage(language)// 设置语言，默认中文
                //                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //                .setPictureWindowAnimationStyle(windowAnimationStyle)// 自定义相册启动退出动画
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                //                .isOriginalImageControl(cb_original.isChecked())// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.cameraFileName("test.png")    // 重命名拍照文件名、注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile("test.png")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                //.renameCropFileName("test.png")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                //                .isSingleDirectReturn(cb_single_back.isChecked())// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                //                .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressQuality(80)// 图片压缩后输出质量 0~ 100
                .synOrAsy(true)//同步false或异步true 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                //                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //                .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                //                .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                //                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                //                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //                .openClickSound(cb_voice.isChecked())// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径  注：已废弃
                .videoMaxSecond(60)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(5)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒
                .forResult(result);//结果回调onActivityResult code

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList.clear();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    String outputPath = "";
                    if (!TextUtils.isEmpty(selectList.get(0).getAndroidQToPath())) {
                        outputPath = selectList.get(0).getAndroidQToPath();
                    } else {
                        outputPath = selectList.get(0).getPath();
                    }
                    File file = new File(outputPath);
//                    Bitmap bitmap = BitmapUtil.getVideoThumb(outputPath);
//                    String suoluepic = BitmapUtil.bitmapToString(bitmap);
                    String finalOutputPath = outputPath;
                    TimeTaskUtils.getVideoUrl(file, redId, new TIMValueCallBack<String>() {
                        @Override
                        public void onError(int code, String desc) {
                            ToastUtils.showShortToast("上传错误");
                        }

                        @Override
                        public void onSuccess(String s) {
                            TimeTaskUtils.subGameVideo(redId, new TIMValueCallBack<Boolean>() {
                                @Override
                                public void onError(int code, String desc) {
                                    ToastUtils.showShortToast(desc);
                                }

                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (aBoolean) {
                                        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mChatInfo.getId());
                                        sendMess(s, finalOutputPath);
                                    }
                                }
                            });
                        }
                    });
                    // 图片选择结果回调
                    // 例如 LocalMedia 里面返回三种 path
                    // 1.media.getPath(); 为原图 path
                    // 2.media.getCutPath();为裁剪后 path，需判断 media.isCut();是否为 true
                    // 3.media.getCompressPath();为压缩后 path，需判断 media.isCompressed();是否为 true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
                case PictureConfig.EVERY_DAT_IMG:
                    mChatLayout.tv_daily_sure.setEnabled(false);
                    mChatLayout.tv_daily_sure.setFocusable(false);
                    LocalMedia selectListImg = PictureSelector.obtainMultipleResult(data).get(0);
                    String outputPath1 = "";
                    if (!TextUtils.isEmpty(selectListImg.getAndroidQToPath())) {
                        outputPath1 = selectListImg.getAndroidQToPath();
                    } else {
                        outputPath1 = selectListImg.getPath();
                    }
                    File file1 = new File(outputPath1);
                    String finalOutputPath1 = outputPath1;
                    TimeTaskUtils.getImagVideoUrl(file1, "1", new TIMValueCallBack<String>() {
                        @Override
                        public void onError(int i, String s) {
                            ToastUtils.showShortToast(s);
                            mChatLayout.tv_daily_sure.setEnabled(true);
                            mChatLayout.tv_daily_sure.setFocusable(true);
                        }

                        @Override
                        public void onSuccess(String s) {
                            TimeTaskUtils.EverySubmitDayTask(s, groupEveryDayTaskId,
                                    SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID), "1", mChatInfo.getId(),new TIMValueCallBack<IceSubmitBean>() {
                                        @Override
                                        public void onError(int i, String s) {
                                            mChatLayout.tv_daily_sure.setEnabled(true);
                                            mChatLayout.tv_daily_sure.setFocusable(true);
                                        }

                                        @Override
                                        public void onSuccess(IceSubmitBean iceSubmitBean) {
                                            mChatLayout.tv_daily_sure.setEnabled(true);
                                            mChatLayout.tv_daily_sure.setFocusable(true);
                                            if (iceSubmitBean != null){
                                                sendMessImage(finalOutputPath1);
                                            }
                                        }
                                    });
                        }
                    });
                    break;
                case PictureConfig.EVERY_DAT_VIDEO:
                    mChatLayout.tv_daily_sure.setEnabled(false);
                    mChatLayout.tv_daily_sure.setFocusable(false);
                    LocalMedia selectListVedio = PictureSelector.obtainMultipleResult(data).get(0);
                    String outputPath2 = "";
                    if (!TextUtils.isEmpty(selectListVedio.getAndroidQToPath())) {
                        outputPath2 = selectListVedio.getAndroidQToPath();
                    } else {
                        outputPath2 = selectListVedio.getPath();
                    }
                    long time = selectListVedio.getDuration();
                    int w = selectListVedio.getWidth();
                    int h = selectListVedio.getHeight();

                    File file2 = new File(outputPath2);
                    String outPath = outputPath2;
                    Bitmap bitmap = ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(outPath, MediaStore.Images.Thumbnails.MINI_KIND), (int)(w*0.5), (int)(h*0.5),ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//指定视频缩略图的大小
                    String dirName = "TaskEveryday";
                    File fileSave = BitmapUtil.saveBitmap(dirName,bitmap, Objects.requireNonNull(getContext()));

                    ProgressDownDialog  progressDownDialog = new ProgressDownDialog();
                    progressDownDialog.show(getChildFragmentManager(),"");

                    Util.compressVideo(outPath, getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath(), new TIMValueCallBack<String>() {
                        @Override
                        public void onError(int i, String s) {
                            mChatLayout.tv_daily_sure.setEnabled(true);
                            mChatLayout.tv_daily_sure.setFocusable(true);
                            progressDownDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(String compressPath) {
                            Log.i("msg_cy","...filePath....."+compressPath);
                            TimeTaskUtils.getImagVideoUrl(new File(compressPath), "4", new TIMValueCallBack<String>() {
                                @Override
                                public void onError(int i, String s) {
                                    ToastUtils.showShortToast(s);
                                    mChatLayout.tv_daily_sure.setEnabled(true);
                                    mChatLayout.tv_daily_sure.setFocusable(true);
                                    progressDownDialog.dismiss();
                                }

                                @Override
                                public void onSuccess(String s) {
                                    TimeTaskUtils.EverySubmitDayTask(s, groupEveryDayTaskId,
                                            SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID), "1", mChatInfo.getId(),new TIMValueCallBack<IceSubmitBean>() {
                                                @Override
                                                public void onError(int i, String s) {
                                                    mChatLayout.tv_daily_sure.setEnabled(true);
                                                    mChatLayout.tv_daily_sure.setFocusable(true);
                                                    progressDownDialog.dismiss();
                                                }

                                                @Override
                                                public void onSuccess(IceSubmitBean iceSubmitBean) {
                                                    mChatLayout.tv_daily_sure.setEnabled(true);
                                                    mChatLayout.tv_daily_sure.setFocusable(true);
                                                    if (iceSubmitBean != null){
                                                        if (fileSave != null) {
                                                            sendMessVideo(compressPath,fileSave.getPath(),time,w,h,progressDownDialog);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    });

                    break;
            }
        }
    }

    private void sendMessVideo(String video,String imgUrl,long duration,int w,int h,ProgressDownDialog  progressDownDialog) {
        if (!checkPermission(VIDEO_RECORD)) {
            TUIKitLog.i("TAG", "startVideoRecord checkPermission failed ");
            return;
        }
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mChatInfo.getId());
        MessageInfo msg = MessageInfoUtil.buildVideoMessage(imgUrl, video, w, h, duration);
        conversation.sendMessage(msg.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastShortMessage(s);
                progressDownDialog.dismiss();
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                EventBus.getDefault().post(new UpdateMessageEvent(true));
                progressDownDialog.dismiss();
            }
        });
//        mChatLayout.getInputLayout().mMessageHandler.sendMessage(msg);
    }

    private void sendMessImage(String url) {
        if (!checkPermission(VIDEO_RECORD)) {
            TUIKitLog.i("TAG", "startVideoRecord checkPermission failed");
            return;
        }
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mChatInfo.getId());
        Uri uri = Uri.parse(url);
        MessageInfo msg = MessageInfoUtil.buildImageMessage(uri, true);
        conversation.sendMessage(msg.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastShortMessage(s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                EventBus.getDefault().post(new UpdateMessageEvent(true));
            }
        });
//        mChatLayout.getInputLayout().mMessageHandler.sendMessage(info);
    }

    protected static final int CAPTURE = 1;
    protected static final int AUDIO_RECORD = 2;
    protected static final int VIDEO_RECORD = 3;
    protected static final int SEND_PHOTO = 4;
    protected static final int SEND_FILE = 5;

    protected boolean checkPermission(int type) {
        if (!checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return false;
        }
        if (!checkPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return false;
        }
        if (type == SEND_FILE || type == SEND_PHOTO) {
            return true;
        } else if (type == CAPTURE) {
            return checkPermission(getActivity(), Manifest.permission.CAMERA);
        } else if (type == AUDIO_RECORD) {
            return checkPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        } else if (type == VIDEO_RECORD) {
            return checkPermission(getActivity(), Manifest.permission.CAMERA)
                    && checkPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        }
        return true;
    }

    protected boolean checkPermission(Context context, String permission) {
        TUIKitLog.i("TAG", "checkPermission permission:" + permission + "|sdk:" + Build.VERSION.SDK_INT);
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ActivityCompat.checkSelfPermission(context, permission);
            if (PackageManager.PERMISSION_GRANTED != result) {
                showPermissionDialog();
                flag = false;
            }
        }
        return flag;
    }

    private AlertDialog mPermissionDialog;

    private void showPermissionDialog() {

        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(getActivity())
                    .setMessage("使用该功能，需要开启权限，鉴于您禁用相关权限，请手动设置开启权限")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            getActivity().startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    private void cancelPermissionDialog() {
        if (mPermissionDialog != null && mPermissionDialog.isShowing()) {
            mPermissionDialog.dismiss();
        }
    }



    private void sendMess(String s, String pic) {
        buildSubVedio(s, pic);
    }

    private void buildSubVedio(String url_video, String pic) {
//        Uri uri = Uri.fromFile(new File(pic));
//        Log.e("msg","......."+uri.getPath());
        String slt = url_video.replace("mp4", "jpg");
        TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().querySelfProfile();
        if (timUserProfile == null) {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    String nickName = timUserProfile.getNickName();
                    String urlHead = timUserProfile.getFaceUrl();
                    Gson gson = new Gson();
                    CustomMessage message = new CustomMessage();
                    RedPackageCustom redPackageCustom = new RedPackageCustom();
                    redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
                    redPackageCustom.setSendId(TIMManager.getInstance().getLoginUser());
                    redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_THREE);
                    redPackageCustom.setSendType(READ_PACKAGE_MSG_TYPE_TWO);
                    redPackageCustom.setTaskImage(slt);
                    redPackageCustom.setRedDesc(redRec);
                    redPackageCustom.setSendName(nickName);
                    redPackageCustom.setTaskUrl(url_video);

                    message.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
                    message.setSendId(TIMManager.getInstance().getLoginUser());
                    message.setCusType(READ_PACKAGE_CUS_TYPE_THREE);
                    message.setSendType(READ_PACKAGE_MSG_TYPE_TWO);
                    message.setTaskImage(slt);
                    message.setSendName(nickName);
                    message.setTaskUrl(url_video);

                    message.setPackageCustom(redPackageCustom);
                    String data = gson.toJson(message);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Utils.SENDID, TIMManager.getInstance().getLoginUser());
                        jsonObject.put("cusType", READ_PACKAGE_CUS_TYPE_THREE);
                        jsonObject.put("redDesc", redRec);
                        jsonObject.put("tipsType", "0");
                        jsonObject.put("sendType", READ_PACKAGE_MSG_TYPE_TWO);
                        jsonObject.put("avatarUrl", timUserProfile.getFaceUrl());
                        jsonObject.put("VIP", timUserProfile.getLevel() + "");
                        jsonObject.put("taskImage", slt);
                        jsonObject.put("sendName", timUserProfile.getNickName());
                        jsonObject.put("redId", redId);
                        jsonObject.put("taskUrl", url_video);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    conversation.sendMessage(MessageInfoUtil.buildTaskCustomMessage(jsonObject.toString()).getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int code, String desc) {
                            Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                            Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                            EventBus.getDefault().post(new UpdateMessageEvent(true));
                        }
                    });
                }
            });
        } else {
            // Gson gson = new Gson();
            CustomMessage message = new CustomMessage();
            RedPackageCustom redPackageCustom = new RedPackageCustom();
//            redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
            redPackageCustom.setSendId(TIMManager.getInstance().getLoginUser());
            redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_THREE);
            redPackageCustom.setSendType(READ_PACKAGE_MSG_TYPE_TWO);
            redPackageCustom.setTaskImage(slt);
            redPackageCustom.setSendName(timUserProfile.getNickName());
            redPackageCustom.setTaskUrl(url_video);
            redPackageCustom.setRedDesc(redRec);
            message.setPackageCustom(redPackageCustom);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Utils.SENDID, TIMManager.getInstance().getLoginUser());
                jsonObject.put("cusType", READ_PACKAGE_CUS_TYPE_THREE);
                jsonObject.put("redDesc", redRec);
                jsonObject.put("tipsType", "0");
                jsonObject.put("sendType", READ_PACKAGE_MSG_TYPE_TWO);
                jsonObject.put("avatarUrl", timUserProfile.getFaceUrl());
                jsonObject.put("VIP", timUserProfile.getLevel() + "");
                jsonObject.put("taskImage", slt);
                jsonObject.put("sendName", timUserProfile.getNickName());
                jsonObject.put("redId", redId);
                jsonObject.put("taskUrl", url_video);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            String data = gson.toJson(message);
            MessageInfo messageInfo = MessageInfoUtil.buildTaskCustomMessage(jsonObject.toString());
            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                    Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                    EventBus.getDefault().post(new UpdateMessageEvent(true));
                }
            });
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showTime(TimeTaskEvent timeTaskEvent) {
        if (timeTaskEvent.getBean() == null) {
            mChatLayout.rl_time_task.setVisibility(View.GONE);
            return;
        }

        mChatLayout.rl_time_task.setVisibility(View.VISIBLE);
        String[] head = HeadUrlNameUtils.getHeadName();
        Glide.with(this).load(head[1]).into(mChatLayout.time_head);

        mChatLayout.tv_content.setText(timeTaskEvent.getBean().getGameConten());

        mChatLayout.rl_time_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ShowDialogEvent(true, timeTaskEvent.getBean().getRedId(), timeTaskEvent.getBean().getGameConten()));
            }
        });
        if (mCountUtil == null) {
            mCountUtil = new TimeCountUtil(Integer.parseInt(timeTaskEvent.getBean().getGameTime()) * 1000, 1000, new CountDownTimeListener() {
                @Override
                public void onTimeFinish() {
                    mChatLayout.rl_time_task.setVisibility(View.GONE);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeTick(long millisUntilFinished) {
                    mChatLayout.tv_time.setText("剩余" + DataFormatUtils.getDateToString(millisUntilFinished, "mm分ss秒"));
                }
            });
            mCountUtil.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void taskRedPack(TaskRedPackageEvent taskRedPackageEvent) {
        TimeTaskUtils.timeTask(mChatInfo.getId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendChatRoom(SendPayChatRoomEvent event) {
        if (event.isPay()) {
            paychatroom();
        } else {
            SendMessageDialogFragment dialogFragment = new SendMessageDialogFragment(true, Gravity.CENTER);
            dialogFragment.setResponListener(new ResponListener<Boolean>() {
                @Override
                public void Rsp(Boolean s) {
                    if (s) {
                        paychatroom();
                    }
                }
            });
            dialogFragment.show(getChildFragmentManager(), "");
        }
    }

    private void paychatroom() {
        PayChatRoomUtils.pay(new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    mChatLayout.getInputLayout()
                            .mMessageHandler.sendMessage(MessageInfoUtil.buildTextMessage(mChatLayout.getInputLayout().getInputText().getText().toString().trim(), null,""));
                    mChatLayout.getInputLayout().getInputText().setText("");
                }
            }
        }, new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
//                ImageSpan imageSpan = new ImageSpan(Objects.requireNonNull(getActivity()), R.mipmap.icon_diamond_yellow);
//                SpannableString spannableString = new SpannableString("您的余额不足，请及时充值！");
//                spannableString.setSpan(imageSpan, 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                        "您的余额不足，请及时充值！", "立即充值", true);
                dialog.setRsp(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            ChargeDialog mChargeDialog = new ChargeDialog(Objects.requireNonNull(getContext()), getActivity());
                            mChargeDialog.show();
                        }
                    }
                });
                dialog.show(getChildFragmentManager(), "");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void sendGive(GiveThumbsUpEvent giveThumbsUpEvent) {
        if (giveThumbsUpEvent.isGive()) {
            FriendsUtils.checkFriend(mChatInfo.getId(), new TIMValueCallBack<Boolean>() {
                @Override
                public void onError(int i, String s) {
                    ToastUtils.showLongToast(s);
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    if (aBoolean) {
                        isFriend(giveThumbsUpEvent);
                    } else {
                        //非好友
                        noFriend(giveThumbsUpEvent);
                    }
                }
            });

        }
    }

    private void isFriend(GiveThumbsUpEvent giveThumbsUpEvent) {
        ImageSpan imageSpan = new ImageSpan(Objects.requireNonNull(getActivity()), R.mipmap.icon_diamond_yellow);
        String money = "";
        if (TextUtils.isEmpty(giveThumbsUpEvent.getMoney())) {
            money = "0";
        } else {
            money = giveThumbsUpEvent.getMoney();
        }
        String content = "赞赏用户后，将赠送" + money + "红包给对方!";
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(imageSpan, content.length() - 7, content.length() - 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBaseDialogFragment dialogFragment = new SpannableStringBaseDialogFragment(true, Gravity.CENTER, spannableString, "取消", "确定赞赏", false);
        dialogFragment.setResponListener(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
                    loadingDialog.show();
                    IceUtils.giveTask(giveThumbsUpEvent.getTaskId(), giveThumbsUpEvent.getUserId(), new TIMValueCallBack<GiveThumbsUpBean>() {
                        @Override
                        public void onError(int i, String s) {
                            ToastUtils.showLongToast(s);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(GiveThumbsUpBean giveThumbsUpBean) {
                            loadingDialog.dismiss();
                            FriendsUtils.addFriend(mChatInfo.getId(), new TIMValueCallBack<Boolean>() {
                                @Override
                                public void onError(int i, String s) {
                                    ToastUtils.showLongToast("添加失败");
                                }

                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (aBoolean) {
                                        addFriendSdk();
                                    }
                                }
                            });

                            if (giveThumbsUpBean == null) return;
                            JSONObject jsonObject = new JSONObject();
                            try {
//                                        jsonObject.put(Utils.SENDID,TIMManager.getInstance().getLoginUser());
                                jsonObject.put("sendType", READ_PACKAGE_MSG_TYPE_ONE);
                                jsonObject.put("redId", giveThumbsUpBean.getRedId());
                                jsonObject.put("type", READ_PACKAGE_CUS_TYPE_TEN);
                                jsonObject.put("sendName", giveThumbsUpBean.getNickName());
                                jsonObject.put("head", giveThumbsUpBean.getHead());
                                jsonObject.put("taskContent", giveThumbsUpEvent.getTaskContent());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mChatInfo.getId());
                            MessageInfo messageInfo = MessageInfoUtil.buildIceRedPackCustomMessage(jsonObject.toString());
                            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                                @Override
                                public void onError(int code, String desc) {
                                    Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                                    Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                                }

                                @Override
                                public void onSuccess(TIMMessage timMessage) {
                                    Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                                    EventBus.getDefault().post(new UpdateMessageEvent(true));
                                    SPUtils.getInstance(getActivity()).put(Utils.APPID + giveThumbsUpEvent.getTaskId() + giveThumbsUpEvent.getSendId(), READ_PACKAGE_MSG_TYPE_ONE);
                                }
                            });
                        }
                    }, new TIMValueCallBack<Boolean>() {
                        @Override
                        public void onError(int i, String s) {
                            ToastUtils.showLongToast(s);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            loadingDialog.dismiss();
                            if (aBoolean) {
                                showPayDialog();
                            }
                        }
                    });
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "");
    }

    private void noFriend(GiveThumbsUpEvent giveThumbsUpEvent) {

        ImageSpan imageSpan = new ImageSpan(Objects.requireNonNull(getActivity()), R.mipmap.icon_diamond_yellow);
        String money = "";
        if (TextUtils.isEmpty(giveThumbsUpEvent.getMoney())) {
            money = "0";
        } else {
            money = giveThumbsUpEvent.getMoney();
        }
        String content = "赞赏用户后，将赠送" + money + "红包给对方，并自动成为好友!";
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(imageSpan, content.length() - 15, content.length() - 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableStringBaseDialogFragment tipsBaseDialogFragment = new SpannableStringBaseDialogFragment(true, Gravity.CENTER, spannableString, "取消", "确定赞赏", false);
        tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
                    //isFriend(giveThumbsUpEvent);
                    loadingDialog.show();
                    IceUtils.giveTask(giveThumbsUpEvent.getTaskId(), giveThumbsUpEvent.getUserId(), new TIMValueCallBack<GiveThumbsUpBean>() {
                        @Override
                        public void onError(int i, String s) {
                            ToastUtils.showLongToast(s);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(GiveThumbsUpBean giveThumbsUpBean) {
                            loadingDialog.dismiss();
                            FriendsUtils.addFriend(mChatInfo.getId(), new TIMValueCallBack<Boolean>() {
                                @Override
                                public void onError(int i, String s) {
                                    ToastUtils.showLongToast("添加失败");
                                }

                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (aBoolean) {
                                        addFriendSdk();
                                    }
                                }
                            });

                            if (giveThumbsUpBean == null) return;
                            JSONObject jsonObject = new JSONObject();
                            try {
//                                        jsonObject.put(Utils.SENDID,TIMManager.getInstance().getLoginUser());
                                jsonObject.put("sendType", READ_PACKAGE_MSG_TYPE_ONE);
                                jsonObject.put("redId", giveThumbsUpBean.getRedId());
                                jsonObject.put("type", READ_PACKAGE_CUS_TYPE_TEN);
                                jsonObject.put("sendName", giveThumbsUpBean.getNickName());
                                jsonObject.put("head", giveThumbsUpBean.getHead());
                                jsonObject.put("taskContent", giveThumbsUpEvent.getTaskContent());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mChatInfo.getId());
                            MessageInfo messageInfo = MessageInfoUtil.buildIceRedPackCustomMessage(jsonObject.toString());
                            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                                @Override
                                public void onError(int code, String desc) {
                                    Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                                    Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                                }

                                @Override
                                public void onSuccess(TIMMessage timMessage) {
                                    Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                                    EventBus.getDefault().post(new UpdateMessageEvent(true));
                                    SPUtils.getInstance(getActivity()).put(Utils.APPID + giveThumbsUpEvent.getTaskId() + giveThumbsUpEvent.getSendId(), READ_PACKAGE_MSG_TYPE_ONE);
                                }
                            });
                        }
                    }, new TIMValueCallBack<Boolean>() {
                        @Override
                        public void onError(int i, String s) {
                            ToastUtils.showLongToast(s);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            loadingDialog.dismiss();
                            if (aBoolean) {
                                showPayDialog();
                            }
                        }
                    });
                }
            }
        });
        tipsBaseDialogFragment.show(getChildFragmentManager(), "");

    }

    private void addFriendSdk() {

        doResponse(mChatInfo.getId());

//        FriendsUtils.checkFriend(mChatInfo.getId(), new TIMValueCallBack<Boolean>() {
//            @Override
//            public void onError(int i, String s) {
//                ToastUtils.showLongToast(s);
//            }
//
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//                if (!aBoolean) {
//
//                }
//            }
//        });
    }


    private void showPayDialog() {
        PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                "您的余额不足，请及时充值！", "立即充值", true);
        dialog.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
                    ChargeDialog mChargeDialog = new ChargeDialog(Objects.requireNonNull(getActivity()), getActivity());
                    mChargeDialog.show();
                }
            }
        });
        dialog.show(getChildFragmentManager(), "");
    }

    private void doResponse(String id) {
        TIMFriendResponse response = new TIMFriendResponse();
        response.setIdentifier(id);
        response.setResponseType(TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE_AND_ADD);
        TIMFriendshipManager.getInstance().doResponse(response, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                ToastUtils.showLongToast(s);
                OkLogger.e("添加好友失败---------" + s);
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                if (timFriendResult.getResultCode() == TIM_FRIEND_RESPONSE_AGREE) {
                    OkLogger.e("-------------添加好友成功");
//                    ToastUtils.showLongToast("添加好友成功");
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendIceRed(RedPackageIceEvent event) {
        RedpacketIceUtils iceUtils = new RedpacketIceUtils(this, event, mChatInfo);
        iceUtils.checkRed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePath(ShowPicEvent showPicEvent) {
//        List<LocalMedia> medias = new ArrayList<>();
//        LocalMedia localMedia = new LocalMedia();
//        localMedia.setPath(showPicEvent.getPath());
//        medias.add(localMedia);
//        PictureSelector.create(this)
//                //.themeStyle(themeId) // xml设置主题
//                .setPictureStyle(new PictureParameterStyle())// 动态自定义相册主题
//                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
//                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
//                .loadImageEngine(com.jfkj.im.utils.GlideEngine.createGlideEngine())
//                .openExternalPreview(0, medias);   // 外部传入图片加载引擎，必传项


        List<MyPicPhoto> myPicPhotoList = new ArrayList<>();
        MyPicPhoto myPicPhoto = new MyPicPhoto(0, showPicEvent.getPath(), false);
        myPicPhotoList.add(myPicPhoto);
        myPicPhoto.setData(myPicPhotoList);

        ImageViewerBuilder imageViewerBuilder = new ImageViewerBuilder(getActivity(), new MySimpleLoader(), new MyTestDataProvider(myPicPhoto), new MyTransformer(), 0);
        imageViewerBuilder.setAllnum(1);
        MyCustomController myCustomController = new MyCustomController(getActivity());
        myCustomController.setType(0);
        myCustomController.setData(null, 1);
        myCustomController.init(imageViewerBuilder);
        imageViewerBuilder.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendOneMessage(MessageFailEvent event) {
        PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                "是否重新发送此条消息", "确认", true);
        dialog.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
                    conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mChatInfo.getId());
                    conversation.sendMessage(event.getMsg().getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int code, String desc) {
                            Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                            Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                            EventBus.getDefault().post(new UpdateMessageEvent(true));
                        }
                    });
                }
            }
        });
        dialog.show(getChildFragmentManager(), "");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        if (mChatInfo == null) {
            return;
        }
        initView();

        ChatLayoutHelper helper;
        // TODO 通过api设置ChatLayout各种属性的样例
        if (mChatInfo.getType() == TIMConversationType.C2C) {
            helper = new ChatLayoutHelper(getActivity(), 1, getChildFragmentManager());
        } else {
            if (mChatInfo.isChatRoom()) {
                helper = new ChatLayoutHelper(getActivity(), 4, getChildFragmentManager());
            } else {
                helper = new ChatLayoutHelper(getActivity(), 2, getChildFragmentManager());
            }
        }
        helper.customizeChatLayout(mChatLayout, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlay();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);

        //清除@
        if (conversationMessage != null && conversationMessage.getId() != null && !TextUtils.isEmpty(conversationMessage.getId())){
            SPUtils.getInstance(getContext()).put(conversationMessage.getId(), "");
        }
    }

    @Override
    public void onDestroy() {
        if (mCountUtil != null) {
            mCountUtil.onFinish();
            mCountUtil.cancel();
        }
        if (mCountUtilTpis != null) {
            mCountUtilTpis.onFinish();
            mCountUtilTpis.cancel();
        }

        if (mCountUtilGroupEveryDayTask != null){
            mCountUtilGroupEveryDayTask.onFinish();
            mCountUtilGroupEveryDayTask.cancel();
        }
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
        isFristEveryDay = false;
//        localBroadcastManager.unregisterReceiver(redPackageReceiver);
    }

}
