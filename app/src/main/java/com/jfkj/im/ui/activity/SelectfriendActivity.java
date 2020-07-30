package com.jfkj.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jfkj.im.Bean.GroupNewBean;
import com.jfkj.im.Bean.GroupcenterBean;
import com.jfkj.im.Bean.PinyinComparator;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.contact.ContactItemBean;
import com.jfkj.im.TIM.modules.group.member.GroupMemberInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.group.InvitaionMemberUtils;
import com.jfkj.im.TIM.redpack.tips.SendmessageUtils;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ThreadHelper;
import com.jfkj.im.adapter.SelectfriendAdapter;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Selectfriend.SelectfriendPresenter;
import com.jfkj.im.mvp.Selectfriend.SelectfriendView;
import com.jfkj.im.ui.dialog.InvitationDialogFragment;
import com.jfkj.im.ui.dialog.TextAddFriendDialogfragment;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.DividerItemDecoration;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class SelectfriendActivity extends BaseActivity<SelectfriendPresenter> implements SelectfriendView, TextWatcher, View.OnClickListener {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    SelectfriendPresenter selectfriendPresenter;
    @BindView(R.id.contact_list)
    XRecyclerView recyclerView;
    //    @BindView(R.id.group_create_member_list)
//    ContactListView mContactListView;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;

    private LinearLayoutManager layoutManager;
    //    List<FriendBean.DataBean.FriendArrayBean> list, selectresult;
    SelectfriendAdapter selectfriendAdapter;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    StringBuffer stringBuffer;
    StringBuffer namestringBuffer;
    Intent getIntent;
    String adddelete;
    String clientVersion;
    private String PUBLICK = "Public";
    private Context mContxt;
    private List<ContactItemBean> mData = new ArrayList<>();
    private GroupNewBean groupNewBean;

    /**
     * 创建群组
     */
    private ArrayList<GroupMemberInfo> mMembers = new ArrayList<>();
    private String needUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, true);
        setStaturBar(ll_head);
        mContxt = this;
//        list = new ArrayList<>();
//        selectresult = new ArrayList<>();
        SourceDateList = new ArrayList<>();
        pinyinComparator = new PinyinComparator();
        title_right_tv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        selectfriendPresenter = new SelectfriendPresenter(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLoadingMoreEnabled(true);

        selectfriendAdapter = new SelectfriendAdapter(this);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(selectfriendAdapter);
        stringBuffer = new StringBuffer();
        namestringBuffer = new StringBuffer();
        getIntent = getIntent();
        adddelete = getIntent.getStringExtra("adddelete");
//        groupNewBean = getIntent.getParcelableExtra("data");
        selectfriendPresenter.selectfriend();
        if (adddelete.equals("deletefriend")) {
            title_center_tv.setText("删除成员");
            title_right_tv.setText("删除");
            selectfriendPresenter.loadGroupMemberList(getIntent.getStringExtra(Utils.GROUPID), "");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("selectfriend");
        registerReceiver(broadcastReceiver, intentFilter);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        //显示好友
        showFriend();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectfriend;
    }

    @Override
    public SelectfriendPresenter createPresenter() {
        return selectfriendPresenter;
    }

    private void showFriend() {
        loadFriendListDataAsync();
    }

    private void loadFriendListDataAsync() {
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
                fillFriendListData(timFriends);
            }
        });
    }

    private void fillFriendListData(final List<TIMFriend> timFriends) {
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
                            assembleFriendListData(timFriends);
                        }
                    });
                } else {
                    assembleFriendListData(timFriends);
                }
            }
        });
    }

    private void assembleFriendListData(final List<TIMFriend> timFriends) {

        for (TIMFriend timFriend : timFriends) {
            ContactItemBean info = new ContactItemBean();
            info.covertTIMFriend(timFriend);
            mData.add(info);
            //在这里处理数据务
            SortModel sortModel = new SortModel();
            sortModel.setHead_url(info.getAvatarurl());
            sortModel.setUserid(Long.parseLong(info.getId()));
            sortModel.setName(info.getNickname());
            SourceDateList.add(sortModel);

            SourceDateList = filledData(SourceDateList);
            Collections.sort(SourceDateList, pinyinComparator);
            selectfriendAdapter.setmData(SourceDateList);
        }

    }

    @OnClick(R.id.tv_add)
    void Onclick() {
        TextAddFriendDialogfragment dialogfragment = new TextAddFriendDialogfragment(true, Gravity.CENTER);
        dialogfragment.show(getSupportFragmentManager(), "");
        dialogfragment.setResponListener(new ResponListener<String>() {
            @Override
            public void Rsp(String s) {
                needUserid = s;
            }
        });
    }

    private void createGroupChat() {

        /**
         * //0礼物   1 性格测试  2 红包  3冒险游戏
         * */

//        param.setCustomInfo("group",groups.getBytes());
//添加群成员
//        List<TIMGroupMemberInfo> infos = new ArrayList<TIMGroupMemberInfo>();
//        TIMGroupMemberInfo member = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (SortModel list : SourceDateList
        ) {
            if (list.isIsselect()) {
//                member = new TIMGroupMemberInfo(String.valueOf(list.getUserid()));
//                infos.add(member);
                stringBuilder.append(list.getUserid()).append(",");
            }
        }
//        if (infos.size() == 0){
//            toastShow("请选择群成员");
//            return;
//        }

        InvitatioMember(stringBuilder.toString(), getIntent.getStringExtra(Utils.GROUPID));

    }

    private void InvitatioMember(String toString, String groupId) {
        progressDialog.show();
        InvitaionMemberUtils.InvitatioMember(toString, groupId, this, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                InvitationDialogFragment invitationDialogFragment = new InvitationDialogFragment(Gravity.CENTER,120,200);
                invitationDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s){
                            sendMessage(groupId);
                        }
                    }
                });
                invitationDialogFragment.show(getSupportFragmentManager(),"");
            }
        });
    }



    private void sendMessage(String groupId) {
        //上面的提示
        String message = TIMManager.getInstance().getLoginUser() + "创建群组";
        final TIMMessage createTips = MessageInfoUtil.buildGroupCustomMessage(MessageInfoUtil.GROUP_CREATE, message);
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SendmessageUtils.sendTipsMessage(conversation, createTips, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.Group);
                chatInfo.setId(groupId);
                chatInfo.setChatName(getIntent.getStringExtra("name"));
                chatInfo.setChatRoom(false);

                ConversationMessage conversationMessage = new ConversationMessage();
                conversationMessage.setConversationId(groupId);
                conversationMessage.setGroup(true);
                conversationMessage.setId(groupId);
                conversationMessage.setTitle(getIntent.getStringExtra("name"));

                Intent intent = new Intent(mContxt, ChatActivity.class);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.putExtra(Utils.CONVERSATIONMESSAGE, conversationMessage);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("msg", "sendTipsMessage failed, code: " + errCode + "|desc: " + errMsg);
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_tv:
                createGroupChat();

//                if (adddelete.equals("addfriend")) {
//                    stringBuffer.delete(0, stringBuffer.length());
//                    namestringBuffer.delete(0, namestringBuffer.length());
//                    for (int j = 0; j < SourceDateList.size(); j++) {
//                        if (SourceDateList.get(j).isIsselect()) {
//                            stringBuffer.append(SourceDateList.get(j).getUserid() + ",");
//                        }
//                    }
//                    if (stringBuffer.toString().length() > 0){
//                        stringBuffer.deleteCharAt(stringBuffer.toString().length() - 1);
//                    }
//                    for (int j = 0; j < SourceDateList.size(); j++) {
//                        if (SourceDateList.get(j).isIsselect()) {
//                            if (SourceDateList.get(j).isIsselect()) {
//                                namestringBuffer.append(SourceDateList.get(j).getName() + ",");
//                            }
//                            namestringBuffer.deleteCharAt(namestringBuffer.toString().length() - 1);
//                        }
//                    }
//
//                    if (stringBuffer.toString().length() > 0) {
//                        if (Utils.netWork()) {
//                            selectfriendPresenter.inviteJoinGroup(stringBuffer.toString(), getIntent.getStringExtra(Utils.GROUPID));
//                        } else {
//                            toastShow(R.string.nonetwork);
//                        }
//                    } else {
//                        toastShow("没有选中的");
//                    }
//                }
//                if (adddelete.equals("deletefriend")) {
//                    stringBuffer.delete(0, stringBuffer.length());
//                    for (int j = 0; j < SourceDateList.size(); j++) {
//                        if (SourceDateList.get(j).isIsselect()) {
//                            for (int i = 0; i < SourceDateList.size(); i++) {
//                                if (SourceDateList.get(i).isIsselect()) {
//                                    stringBuffer.append(SourceDateList.get(i).getUserid() + ",");
//                                }
//                            }
//                            stringBuffer.deleteCharAt(stringBuffer.toString().length() - 1);
//                        }
//                    }
//                    if (stringBuffer.toString().length() > 0) {
//                        if (Utils.netWork()) {
//                            selectfriendPresenter.delGroupMember(stringBuffer.toString(), getIntent.getStringExtra(Utils.GROUPID));
//                        } else {
//                            toastShow(R.string.nonetwork);
//                        }
//                    } else {
//                        toastShow("没有选中的");
//                    }
//                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
        }
    }


    @Override
    public void Selectfriendsuccess(List<TIMFriend> timFriends) {
        try {
            String string = " ";
            JSONObject jsonObject = new JSONObject(string);
            clientVersion = jsonObject.getJSONObject("data").getString("clientVersion");
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("friendArray");
            if (jsonArray.length() > 0) {
                selectfriendPresenter.selectfriend();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                SortModel sortModel = new SortModel();
                sortModel.setName(jsonObject1.getString("nickname"));
                sortModel.setUserid(jsonObject1.getLong("userId"));
                sortModel.setVipLevel(jsonObject1.getInt("vipLevel"));
                sortModel.setHead_url(jsonObject1.getString("head"));
                SourceDateList.add(sortModel);
            }
            if (jsonArray.length() == 0) {
                SourceDateList = filledData(SourceDateList);
                Collections.sort(SourceDateList, pinyinComparator);
                selectfriendAdapter.setmData(SourceDateList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void Selectfriendfail(String s) {

    }

    @Override
    public void deletefriendsuccess(ResponseBody responseBody, String s) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {

                Intent intent = new Intent("adddelete");
                intent.putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID));
                sendBroadcast(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletefriendfail(String s) {

    }


    List<GroupcenterBean> groupcenterBeans = new ArrayList<>();

    @Override
    public void invitesuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {

                String[] split = stringBuffer.toString().split(",");
                String[] namesplit = namestringBuffer.toString().split(",");

                for (int i = 0; i < split.length; i++) {
                    GroupcenterBean bean = new GroupcenterBean();
                    bean.setItemType(2);
                    GroupcenterBean.WaitpassageBean waitpassageBean = new GroupcenterBean.WaitpassageBean();
                    waitpassageBean.setMasterhead(SPUtils.getInstance(mActivity).getString(Utils.HEAD_URL));
                    waitpassageBean.setMaster(SPUtils.getInstance(mActivity).getString(Utils.USER_NAME));
                    waitpassageBean.setGroupId(getIntent.getStringExtra(Utils.GROUPID));
                    waitpassageBean.setGroupName(getIntent.getStringExtra(Utils.GROUPNAME));
                    waitpassageBean.setMessage("邀请" + namesplit[i] + "加入" + getIntent.getStringExtra(Utils.GROUPNAME));
                    bean.setWaitpassageBean(waitpassageBean);
                    groupcenterBeans.add(bean);
                }
                if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP) != null) {
                    if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP).trim().length() > 0) {
                        groupcenterBeans.addAll(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP), GroupcenterBean.class));
                    }
                }
                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADD_GROUP, JSON.toJSONString(groupcenterBeans));
                Intent intent = new Intent(mActivity, GroupsetActivity.class);
                intent.putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID));
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invitefail(String s) {

    }

    @Override
    public void delGroupMembersuccess(ResponseBody responseBody) {

    }

    @Override
    public void delGroupMemberfail(String s) {

    }

    @Override
    public void loadGroupMemberListSuccess(ResponseBody responseBody) {

    }

    @Override
    public void loadGroupMemberListfail(String responseBody) {

    }

    @Override
    public void showLoading() {
        progressDialog.show();

    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            sortModel.setUserid(date.get(i).getUserid());
            sortModel.setVipLevel(date.get(i).getVipLevel());
            sortModel.setHead_url(date.get(i).getHead_url());
            //汉字转换成拼音
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
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "selectfriend":

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

}
