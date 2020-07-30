package com.jfkj.im.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.GroupMessageBean;
import com.jfkj.im.Bean.GroupcenterBean;
import com.jfkj.im.Bean.HandlerInviteGroupBean;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.GroupcenterAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.GroupMessagecenter.GroupMessagecenterPresenter;
import com.jfkj.im.mvp.GroupMessagecenter.GroupMessagecenterView;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class GroupcenterActivity extends BaseActivity<GroupMessagecenterPresenter> implements GroupMessagecenterView, View.OnClickListener, TextWatcher {
    GroupcenterAdapter groupcenterAdapter;
    List<GroupcenterBean> groupcenterBeans;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    @BindView(R.id.newfriend_clear_iv)
    TextView newfriend_clear_iv;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    EditText et_message;
    Dialog dialog, answerdialog;
    View inflate, viewanswer;
    TextView tv_cancel, tv_enter, tv_answer_name;
    TextView tv_answer_cancel, tv_answer_enter;
    int type;
    String groupid, masterid, msgid;
    GroupMessagecenterPresenter groupMessagecenterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(mActivity, R.style.dialogstyle);
        answerdialog = new Dialog(mActivity, R.style.dialogstyle);
        groupMessagecenterPresenter = new GroupMessagecenterPresenter(this);
        inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_groupmessagecenter, null);
        viewanswer = LayoutInflater.from(mActivity).inflate(R.layout.dialog_answer, null);

        tv_cancel = inflate.findViewById(R.id.tv_cancel);
        tv_enter = inflate.findViewById(R.id.tv_enter);
        tv_answer_cancel = viewanswer.findViewById(R.id.tv_cancel);
        tv_answer_enter = viewanswer.findViewById(R.id.tv_enter);
        et_message = viewanswer.findViewById(R.id.et_message);
        tv_answer_name = viewanswer.findViewById(R.id.tv_answer_name);
        et_message.addTextChangedListener(this);
        tv_cancel.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        tv_answer_enter.setEnabled(false);
        tv_answer_enter.setAlpha(0.5f);
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.GROUPMESSAGECENTER, false);
        dialog.setContentView(inflate);
        answerdialog.setContentView(viewanswer);
        show();
        swiperecyclerview.setLayoutManager(new LinearLayoutManager(this));
        groupcenterBeans = new ArrayList<>();
        newfriend_clear_iv.setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("handlerInviteGroup");
        intentFilter.addAction("handlerJoinGroup");
        intentFilter.addAction("answer");

        registerReceiver(broadcastReceiver, intentFilter);
        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP) != null) {
            if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP).trim().length() > 0) {
                groupcenterBeans.addAll(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP), GroupcenterBean.class));
                groupcenterAdapter = new GroupcenterAdapter(groupcenterBeans);
                swiperecyclerview.setAdapter(groupcenterAdapter);
            }
        }
        initListener();
        invite_add_group();
    }
  public void invite_add_group(){
      int invite_add_group = 0;
      if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.RECENTGROUPMESSAGE) != null) {
          if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.RECENTGROUPMESSAGE).trim().length() > 0) {
              List<InteractionBean> interactionBeanList = JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.RECENTGROUPMESSAGE), InteractionBean.class);
              for(int i=0;i<interactionBeanList.size();i++){
                  invite_add_group=invite_add_group+Integer.parseInt(interactionBeanList.get(i).getNumber());
              }
          }
      }
      int invite__group = 0;
      if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.INVITE_ADD_GROUP) != null) {
          if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.INVITE_ADD_GROUP).trim().length() > 0) {
              invite__group = Integer.parseInt(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.INVITE_ADD_GROUP).trim());
          }
      }
      int total=invite__group-invite_add_group;
      SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.INVITE_ADD_GROUP, total+"");
  }
    public void initListener() {
        tv_answer_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.netWork()) {
                    groupMessagecenterPresenter.replyJoinGroupMessageSuccess(masterid, groupid, msgid, et_message.getText().toString().trim());
                } else {
                    toastShow(R.string.nonetwork);
                }
            }
        });
        tv_answer_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerdialog.dismiss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_groupcenter;
    }

    @Override
    public GroupMessagecenterPresenter createPresenter() {
        return null;
    }

    public void show() {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        WindowManager.LayoutParams answerparams = dialog.getWindow().getAttributes();
        answerparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        answerparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        answerparams.gravity = Gravity.CENTER;
        answerdialog.getWindow().setAttributes(answerparams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.newfriend_clear_iv:
                dialog.show();
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_enter:
                if (groupcenterBeans.size() > 0) {
                    groupcenterBeans.clear();
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADD_GROUP, "");
                    groupcenterAdapter.setNewData(groupcenterBeans);

                }
                dialog.dismiss();
                toastShow("清除成功");
                break;

        }
    }

    @Override
    public void handlerInviteGroupSuccess(ResponseBody responseBody, String id) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                HandlerInviteGroupBean handlerInviteGroupBean = JSON.parseObject(string, HandlerInviteGroupBean.class);
                HandlerInviteGroupBean.DataBean.GroupBean group = handlerInviteGroupBean.getData().getGroup();
                HandlerInviteGroupBean.DataBean.OwnerBean owner = handlerInviteGroupBean.getData().getOwner();
                GroupcenterBean.WaitpassageBean waitpassageBean = new GroupcenterBean.WaitpassageBean();
                GroupcenterBean groupMessagecenterBean = new GroupcenterBean();
                waitpassageBean.setGroupId(group.getGroupId());
                waitpassageBean.setMasterId(group.getOwnerId());
                waitpassageBean.setMsgId(group.getOwnerId());
                waitpassageBean.setNeedAck(group.getGroupName());
                waitpassageBean.setMaster(owner.getNickName());
                waitpassageBean.setToMsgId(group.getOwnerId());
                waitpassageBean.setGroupName(group.getGroupName());
                waitpassageBean.setVisitor(owner.getNickName());
                waitpassageBean.setMasterhead(owner.getHead());
                for (int i = 0; i < groupcenterBeans.size(); i++) {
                    if (group.getGroupId().equals(groupcenterBeans.get(i).getWaitpassageBean().getGroupId()) && Utils.APPID.equals(groupcenterBeans.get(i).getWaitpassageBean().getVisitorId()) && id.equals(groupcenterBeans.get(i).getWaitpassageBean().getMasterId())) {
                        waitpassageBean.setMessage("邀请" + groupcenterBeans.get(i).getWaitpassageBean().getVisitor() + "加入" + groupcenterBeans.get(i).getWaitpassageBean().getGroupName());
                        groupcenterBeans.remove(i);
                        if (type == 0) {
                            waitpassageBean.setState("已拒绝");
                        } else {
                            waitpassageBean.setState("已通过");
                        }
                        groupMessagecenterBean.setItemType(3);
                        groupMessagecenterBean.setWaitpassageBean(waitpassageBean);
                        groupcenterBeans.add(0, groupMessagecenterBean);


                    }
                }
                groupcenterAdapter.setNewData(groupcenterBeans);
                if (groupcenterAdapter.getData().size() > 0) {
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADD_GROUP, JSON.toJSONString(groupcenterAdapter.getData()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handlerInviteGroupfail(String s) {

    }

    @Override
    public void handlerJoinGroupSuccess(String responseBody, String id) {
        String state;
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                List<GroupcenterBean> messagecenterBeanList = JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP).trim(), GroupcenterBean.class);
                GroupMessageBean groupMessageBean = JSON.parseObject(responseBody, GroupMessageBean.class);
                for (int i = 0; i < messagecenterBeanList.size(); i++) {
                    GroupMessageBean.DataBean.GroupBean group = groupMessageBean.getData().getGroup();
                    GroupMessageBean.DataBean.UserBean user = groupMessageBean.getData().getUser();
                    if (messagecenterBeanList.get(i).getWaitpassageBean().getGroupId().equals(group.getGroupId()) && messagecenterBeanList.get(i).getWaitpassageBean().getVisitorId().equals(Utils.APPID) && messagecenterBeanList.get(i).getWaitpassageBean().getMasterId().equals(id)) {
                        messagecenterBeanList.get(i).getWaitpassageBean().setGroupName(group.getGroupName());
                        messagecenterBeanList.get(i).getWaitpassageBean().setMasterhead(user.getHead());
                        messagecenterBeanList.get(i).getWaitpassageBean().setMaster(user.getNickName());
                        messagecenterBeanList.get(i).setItemType(3);
                        state =messagecenterBeanList.get(i).getWaitpassageBean().getState();
                        if(state.equals("已拒绝")||state.equals("已通过")){

                        }else {
                            if (type == 0) {
                                messagecenterBeanList.get(i).getWaitpassageBean().setState("已拒绝");
                            } else {
                                messagecenterBeanList.get(i).getWaitpassageBean().setState("已通过");
                                List<InteractionBean> interactionBeanList = new ArrayList<>();
                                InteractionBean interactionBean_add_groupsuccess = new InteractionBean();
                                interactionBean_add_groupsuccess.setHeadurl(group.getGroupHead());
                                interactionBean_add_groupsuccess.setName(group.getGroupName());
                                interactionBean_add_groupsuccess.setGroupId(Long.parseLong(group.getGroupId()));
                                interactionBeanList.addAll(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.RECENTGROUPMESSAGE), InteractionBean.class));
                                for (int k = 0; k < interactionBeanList.size(); k++) {
                                    if (interactionBeanList.get(k).getGroupId() == Long.parseLong(group.getGroupId())) {
                                        interactionBeanList.remove(k);
                                    }
                                }
                                interactionBeanList.add(0, interactionBean_add_groupsuccess);
                                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, JSON.toJSONString(interactionBeanList));
                            }
                        }

                    }
                }
                groupcenterAdapter.setNewData(messagecenterBeanList);
                if (groupcenterAdapter.getData().size() > 0) {
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADD_GROUP, JSON.toJSONString(groupcenterAdapter.getData()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handlerJoinGroupfail(String responseBody) {

    }

    @Override
    public void replyJoinGroupMessageSuccess(String responseBody, String applyid) {
        try {
            answerdialog.dismiss();
            JSONObject jsonObject = new JSONObject(responseBody);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {

                List<GroupcenterBean> messagecenterBeanList = JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.ADD_GROUP).trim(), GroupcenterBean.class);
                for (int i = 0; i < messagecenterBeanList.size(); i++) {
                    if (messagecenterBeanList.get(i).getWaitpassageBean().getGroupId().equals(groupid) && messagecenterBeanList.get(i).getWaitpassageBean().getMasterId().equals(applyid) && messagecenterBeanList.get(i).getWaitpassageBean().getState().trim().length()==0) {
                        GroupcenterBean groupcenterBean = messagecenterBeanList.get(i);
                        groupcenterBean.getWaitpassageBean().setChat2(et_message.getText().toString().trim());
                        messagecenterBeanList.remove(i);

                        messagecenterBeanList.add(0, groupcenterBean);

                        groupcenterAdapter.setNewData(messagecenterBeanList);
                        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADD_GROUP, JSON.toJSONString(groupcenterAdapter.getData()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "handlerInviteGroup":
                    if (intent.getStringExtra(Utils.HANDLERINVITEGROUP).equals("reject")) {
                        type = 0;
                        groupMessagecenterPresenter.loadmessagecenter(intent.getStringExtra(Utils.SENDID), intent.getStringExtra(Utils.GROUPID),
                                intent.getStringExtra(Utils.MSGID), 2 + "");
                    } else {
                        type = 1;
                        groupMessagecenterPresenter.loadmessagecenter(intent.getStringExtra(Utils.SENDID), intent.getStringExtra(Utils.GROUPID),
                                intent.getStringExtra(Utils.MSGID), 1 + "");
                    }
                    break;
                case "handlerJoinGroup":
                    if (intent.getStringExtra(Utils.HANDLERINVITEGROUP).equals("reject")) {
                        type = 0;
                        groupMessagecenterPresenter.handlerJoinGroup(intent.getStringExtra(Utils.SENDID), intent.getStringExtra(Utils.GROUPID), intent.getStringExtra(Utils.MSGID), "2");
                    } else {
                        type = 1;
                        groupMessagecenterPresenter.handlerJoinGroup(intent.getStringExtra(Utils.SENDID), intent.getStringExtra(Utils.GROUPID), intent.getStringExtra(Utils.MSGID), "1");

                    }
                    break;
                case "answer":
                    answerdialog.show();
                    groupid = intent.getStringExtra(Utils.GROUPID);
                    masterid = intent.getStringExtra(Utils.APPLYID);
                    msgid = intent.getStringExtra(Utils.MSGID);
                    tv_answer_name.setText(intent.getStringExtra(Utils.NICKNAME));
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (et_message.getText().toString().trim().length() > 0) {
            tv_answer_enter.setEnabled(true);
            tv_answer_enter.setAlpha(1.0f);
        } else {
            tv_answer_enter.setEnabled(false);
            tv_answer_enter.setAlpha(0.5f);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
