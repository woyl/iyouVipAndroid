package com.jfkj.im.TIM.redpack.group;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.protobuf.StringValue;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jfkj.im.App;
import com.jfkj.im.Bean.PinyinComparator;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallsBack;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.contact.ContactItemBean;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.game.GameListBean;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ThreadHelper;
import com.jfkj.im.adapter.SelectfriendAdapter;
import com.jfkj.im.entity.AddDeteleMemberEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.InvitationDialogFragment;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.DividerItemDecoration;
import com.lzy.okgo.utils.OkLogger;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.imsdk.friendship.TIMDelFriendType;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.ResponseBody;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_NINE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;

public class GroupAddOrDelMeberActivity extends BaseActivity {
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    @BindView(R.id.contact_list)
    XRecyclerView contact_list;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    private SelectfriendAdapter selectfriendAdapter;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private boolean isAdd;
    private String groupId;
    private GroupInfoBean groupInfoBean;
    private List<String> strings;

    private List<TIMUserProfile> timUserProfiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        isAdd = getIntent().getBooleanExtra("add", false);
        groupId = getIntent().getStringExtra("groupId");
        if (isAdd) {
            title_center_tv.setText("选择好友");
            title_right_tv.setText("邀请");
        } else {
            title_center_tv.setText("删除成员");
            title_right_tv.setText("删除");
        }
        iniViews();

        getGroupMembers();
    }

    private void getGroupMembers() {
        GroupInfoSetUtils.getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                if (isAdd) {
                    //显示好友
                    showFriend(timGroupMemberInfos);
                }else {
                    for (TIMGroupMemberInfo timGroupMemberInfo : timGroupMemberInfos) {

                        //vip
                        TIMUserProfile userProfile = FriendsUtils.queryUsersProfileUser(timGroupMemberInfo.getUser());
                        timUserProfiles.add(userProfile);
                        if (userProfile != null){
                            //在这里处理数据务
                            SortModel sortModel = new SortModel();
                            sortModel.setVipLevel((int) userProfile.getLevel());
                            sortModel.setHead_url(userProfile.getFaceUrl());
                            sortModel.setUserid(Long.parseLong(timGroupMemberInfo.getUser()));
                            sortModel.setName(userProfile.getNickName());
                            if (!timGroupMemberInfo.getUser().equals(TIMManager.getInstance().getLoginUser())){
                                SourceDateList.add(sortModel);
                            }
                        }
                        SourceDateList = filledData(SourceDateList);
                        Collections.sort(SourceDateList, pinyinComparator);
                        selectfriendAdapter.setmData(SourceDateList);
                    }

                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getData(GroupInfoBean groupInfoBean){
        this.groupInfoBean = groupInfoBean;
    }

    private void showFriend(List<TIMGroupMemberInfo> timGroupMemberInfos) {
        loadFriendListDataAsync(timGroupMemberInfos);
    }

    private void loadFriendListDataAsync(List<TIMGroupMemberInfo> timGroupMemberInfos) {
        TUIKitLog.e("msg", "loadFriendListDataAsync");
        ThreadHelper.INST.execute(new Runnable() {
            @Override
            public void run() {
                // 压测时数据量比较大，query耗时比较久，所以这里使用新线程来处理
                TUIKitLog.e("msg", "queryFriendList");
                List<TIMFriend> timFriends = TIMFriendshipManager.getInstance().queryFriendList();
                if (timFriends == null) {
                    timFriends = new ArrayList<>();
                }
                TUIKitLog.e("msg", "queryFriendList: " + timFriends.size());
                fillFriendListData(timFriends,timGroupMemberInfos);
            }
        });
    }


    private void fillFriendListData(final List<TIMFriend> timFriends,List<TIMGroupMemberInfo> timGroupMemberInfos) {
        // 外部调用是在其他线程里面，但是更新数据同时会刷新UI，所以需要放在主线程做。
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timFriends.size() == 0) {
                    TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {
                        @Override
                        public void onError(int code, String desc) {
                            TUIKitLog.e("msg", "getFriendList err code = " + code);
                        }

                        @Override
                        public void onSuccess(List<TIMFriend> timFriends) {
                            if (timFriends == null) {
                                timFriends = new ArrayList<>();
                            }
                            TUIKitLog.e("msg", "getFriendList success result = " + timFriends.size());
                            assembleFriendListData(timFriends,timGroupMemberInfos);
                        }
                    });
                } else {
                    assembleFriendListData(timFriends,timGroupMemberInfos);
                }
            }
        });
    }


    private void assembleFriendListData(final List<TIMFriend> timFriends,List<TIMGroupMemberInfo> timGroupMemberInfos) {

        for (TIMFriend timFriend : timFriends) {
            ContactItemBean info = new ContactItemBean();
            info.covertTIMFriend(timFriend);
//            mData.add(info);

            //vip
            TIMUserProfile userProfile = FriendsUtils.queryUsersProfileUser(info.getId());
            if (userProfile != null){
                //在这里处理数据务
                SortModel sortModel = new SortModel();
                sortModel.setHead_url(info.getAvatarurl());
                sortModel.setUserid(Long.parseLong(info.getId()));
                sortModel.setName(info.getNickname());
                sortModel.setVipLevel((int) userProfile.getLevel());
                for (TIMGroupMemberInfo timGroupMemberInfo : timGroupMemberInfos){
                    if (String.valueOf(sortModel.getUserid()).equals(timGroupMemberInfo.getUser())){
                        sortModel.setGroupMember(true);
                    }
                }
                SourceDateList.add(sortModel);
            }

            SourceDateList = filledData(SourceDateList);
            Collections.sort(SourceDateList, pinyinComparator);
            selectfriendAdapter.setmData(SourceDateList);
        }

    }

    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            sortModel.setUserid(date.get(i).getUserid());
            sortModel.setVipLevel(date.get(i).getVipLevel());
            sortModel.setHead_url(date.get(i).getHead_url());
            sortModel.setGroupMember(date.get(i).isGroupMember());
            //汉字转换成拼音
            if (!TextUtils.isEmpty(date.get(i).getName())){
                String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
                if (pinyin.length() > 0) {
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        sortModel.setLetters(sortString.toUpperCase());
                    } else {
                        sortModel.setLetters("#");
                    }
                } else {
                    sortModel.setLetters("#");
                }
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    private void iniViews() {
        contact_list.setPullRefreshEnabled(false);
        SourceDateList = new ArrayList<>();
        pinyinComparator = new PinyinComparator();
        selectfriendAdapter = new SelectfriendAdapter(this);
        contact_list.setLayoutManager(new LinearLayoutManager(this));
//        contact_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        contact_list.setAdapter(selectfriendAdapter);
        selectfriendAdapter.setResponListener(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s){
                    List<TIMGroupMemberInfo> infos = new ArrayList<TIMGroupMemberInfo>();
                    TIMGroupMemberInfo member = null;
                    for (SortModel list : SourceDateList
                    ) {
                        if (list.isIsselect()) {
                            member = new TIMGroupMemberInfo(String.valueOf(list.getUserid()));
                            infos.add(member);
                        }
                    }
                    strings = new ArrayList<>();
                    for (TIMGroupMemberInfo info : infos
                    ) {
                        strings.add(info.getUser());
                    }
                    if (strings.size() == 0) {
                        title_right_tv.setEnabled(false);
                        title_right_tv.setAlpha(0.5f);
                    }else {
                        title_right_tv.setEnabled(true);
                        title_right_tv.setAlpha(1f);
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_add_or_del_meber;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.title_left_tv, R.id.title_right_tv})
    void Onclick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.title_right_tv:

                if (strings.size() == 0) {
                    toastShow("请选择好友");
                    return;
                }
                if (isAdd) {
                    //邀请用户入群
//                    addGroup(strings);
//                    addGroupMembers(strings);
                    InvitationMembers(strings);

                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (TIMUserProfile timUserProfile: timUserProfiles){
                        for (String id:strings){
                            if (TextUtils.equals(id,timUserProfile.getIdentifier())){
                                stringBuilder.append(timUserProfile.getNickName()).append(",");
                            }
                        }
                    }
//                    String content = "确认要删除俱乐部成员"+stringBuilder.substring(0,stringBuilder.length()-1);
                    String content = "确认要删除选中的俱乐部成员";

                    TipsBaseDialogFragment tipsBaseDialogFragment
                            = new TipsBaseDialogFragment(true, Gravity.CENTER, content, "取消", "确认", true);
                    tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                        @Override
                        public void Rsp(Boolean s) {
                            if (s) {
                                delGroup(strings);
                            }
                        }
                    });
                    tipsBaseDialogFragment.show(getSupportFragmentManager(), "");

                }
                break;
        }
    }

    private void InvitationMembers(List<String> strings) {
        StringBuilder stringBuffer = new StringBuilder();
        for (String id : strings){
            stringBuffer.append(id).append(",");
        }
        InvitatioMember(stringBuffer.toString());
    }







    @SuppressLint("ResourceAsColor")
    private void InvitatioMember(String ids) {
        progressDialog.show();
        invitationSuccessful(ids);
//        /*
//            群头像合成
//            1.查询群成员头像
//            2.合成群头像
//            3.生成的bitmap保存本地File
//            4.上传file 到服务
//            5.修改腾讯群头像
//         */
//        List<String> list = new ArrayList<>();
//
//        // 1.查询群成员头像
//        GroupInfoSetUtils.loadGroupMemberList(this, groupId, "9", "", new IUIKitCallsBack<GroupMemberInfoBean>() {
//            @Override
//            public void onSuccess(List<GroupMemberInfoBean> data) {
//                if(data!=null&& data.size()>0){
//                    for (int i = 0;i<data.size();i++){
//                        list.add(data.get(i).getHead());
//                        String[] strings = list.toArray(new String[list.size()]);
//                        //2.合成群头像
//                        CombineBitmap.init(GroupAddOrDelMeberActivity.this)
//                                .setLayoutManager(new WechatLayoutManager()) //
//                                .setSize(50) // 必选，组合后Bitmap的尺寸，单位dp
//                                .setGap(2) // 单个图片之间的距离，单位dp，默认0dp
//                                .setGapColor(R.color.white) // 单个图片间距的颜色，默认白色
//                                .setPlaceholder(R.drawable.logo) // 单个图片加载失败的默认显示图片
//                                .setUrls(strings) // 要加载的图片url数组
//                                // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
//                                .setOnProgressListener(new OnProgressListener() {
//                                    @Override
//                                    public void onStart() {
//
//                                    }
//                                    @Override
//                                    public void onComplete(Bitmap bitmap) {
//                                        //3.九宫格生成成功,上传bitmap
//
//
//                                    }
//                                })
//                                .build();
//                    }
//                }
//            }
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//                invitationSuccessful(ids);
//            }
//        });
    }

    public void invitationSuccessful(String ids){
        InvitaionMemberUtils.InvitatioMember(ids, groupId, this, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                EventBus.getDefault().postSticky(new AddDeteleMemberEvent(true));

                InvitationDialogFragment invitationDialogFragment = new InvitationDialogFragment(Gravity.CENTER,120,200);
                invitationDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s){
                            finish();
                        }
                    }
                });
                invitationDialogFragment.show(getSupportFragmentManager(),"");
            }
        });
    }

    private void addGroupMembers(List<String> strings) {
        TIMMessage message = buildInvite();
        for (String id : strings){
            TIMManager.getInstance().getConversation(TIMConversationType.C2C,id).sendMessage(message, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e("msg","......code.........."+code+ ",,,desc,,,,,"+desc);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    Log.e("msg","......success..........");
                }
            });
        }

    }

    private TIMMessage buildInvite() {
        Gson gson = new Gson();
        CustomMessage message = new CustomMessage();
        RedPackageCustom redPackageCustom = new RedPackageCustom();
        redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_ONE);
        redPackageCustom.setAvatarUrl(groupInfoBean.getGroupOwnerHead());
        redPackageCustom.setSendName(groupInfoBean.getGroupOwnerName());
        redPackageCustom.setSendId(groupInfoBean.getGroupOwnerId());
        redPackageCustom.setVIP(groupInfoBean.getVipLevel());
        redPackageCustom.setDate(System.currentTimeMillis());
        redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_NINE);
        redPackageCustom.setGroupName(groupInfoBean.getGroupName());
        redPackageCustom.setGroupId(groupInfoBean.getGroupId());
//        redPackageCustom.setInviteType(INVITE_CUS_TYPE_ONE);
        message.setPackageCustom(redPackageCustom);
        String data = gson.toJson(message);
        MessageInfo info = MessageInfoUtil.buildInviteCustomMessage(data);
        return info.getTIMMessage();
    }

    private void delGroup(List<String> strings) {
        TIMGroupManager.DeleteMemberParam param = new TIMGroupManager.DeleteMemberParam(groupId, strings);
        TIMGroupManager.getInstance().deleteGroupMember(param, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("msg", "deleteGroupMember onErr. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> results) { //群组成员操作结果
                for(TIMGroupMemberResult r : results) {
                    Log.d("msg", "result: " + r.getResult()  //操作结果:  0：删除失败；1：删除成功；2：不是群组成员
                            + " user: " + r.getUser());    //用户帐号
                    if (r.getResult() == 1){
                        EventBus.getDefault().postSticky(new AddDeteleMemberEvent(true));
                        toastShow("删除成功");
                        finish();
                    }else if (r.getResult() == 0){
                        toastShow("删除失败");

                    }else if (r.getResult() == 2){
                        toastShow("不是俱乐部成员");
                    }
                }
            }
        });
    }

    //邀请用户入群
    private void addGroup(List<String> strings) {
        /**
         * 获取操作结果
         * @return 操作结果 ： 0：失败；1：成功；2：添加成员时，该成员已经在群组中 或 删除成员时，该成员不在群组中
         */
        TIMGroupManager.getInstance().inviteGroupMember(groupId, strings, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("msg", "addGroupMember onErr. code: " + code + " errmsg: " + desc);
            }
            /**
             * 获取操作结果
             * @return 操作结果 ：
             * 0：失败；
             * 1：成功；
             * 2：添加成员时，该成员已经在群组中 或 删除成员时，该成员不在群组中;
             * 3: 邀请成员加入时，需要该被邀请者的同意确认
             */
            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                for(TIMGroupMemberResult r : timGroupMemberResults) {
                    Log.d("msg", "result: " + r.getResult()  //操作结果:  0：删除失败；1：删除成功；2：不是群组成员
                            + " user: " + r.getUser());    //用户帐号
                }
            }
        });
    }
}
