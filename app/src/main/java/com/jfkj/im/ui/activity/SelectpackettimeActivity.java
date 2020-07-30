package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupBean;
import com.jfkj.im.Bean.GroupNewBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.TIM.redpack.group.InvitaionMemberUtils;
import com.jfkj.im.TIM.redpack.tips.SendmessageUtils;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.createGroup.CreateGroupPresenter;
import com.jfkj.im.mvp.createGroup.CreateGroupView;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.SelecttimeDialog;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.ui.fragment.ClubFragment;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class SelectpackettimeActivity extends BaseActivity<CreateGroupPresenter> implements View.OnClickListener, SelecttimeDialog.SelectResult, CreateGroupView {
    TextView time_tv;
    SelecttimeDialog selecttimeDialog;
    @BindView(R.id.next_btn)
    Button next_btn;
    CreateGroupPresenter presenter;
    Intent getIntent;
    @BindView(R.id.more_iv)
    ImageView more_iv;
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    private boolean isGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        time_tv = findViewById(R.id.time_tv);
        time_tv.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        more_iv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        title_left_tv.setBackgroundResource(R.mipmap.iv_back_black);
        selecttimeDialog = new SelecttimeDialog(this);
        selecttimeDialog.setSelectResult(this);
        presenter = new CreateGroupPresenter(this);
        getIntent = getIntent();
        isGroup = getIntent.getBooleanExtra("group", false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectpackettime;
    }

    @Override
    public CreateGroupPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_tv:
            case R.id.more_iv:
                selecttimeDialog.show();
                break;
            case R.id.next_btn:
                if (Utils.netWork()) {
                    if (isGroup) {
                        GroupInfoSetUtils.setGroupInfo(this,
                                getIntent.getStringExtra(Utils.GROUPID), "",
                                time_tv.getText().toString().trim().substring(0, 2), new TIMValueCallBack<Boolean>() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        toastShow(code);
                                    }

                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        if (aBoolean){
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        if (getIntent.getBooleanExtra(Utils.UPDATE, false)) {
                            presenter.updateGroup(getIntent.getStringExtra(Utils.GROUPID), time_tv.getText().toString().trim().substring(0, 2));
                        } else {
                            presenter.creategroup(
                                    getIntent.getStringExtra("name"),
                                    getIntent.getStringExtra("notice"),
                                    getIntent.getStringExtra("packets"),
                                    time_tv.getText().toString().trim().substring(0, 2));
                        }
                    }

                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
        }
    }

    @Override
    public void selecttime(String s) {
        selecttimeDialog.dismiss();
        time_tv.setText(s);
    }

    @Override
    public void CreateGroupSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);

            //创建群组
            if (jsonObject.getString("code").equals("50000")) {
                ImageSpan imageSpan = new ImageSpan(this, R.mipmap.icon_diamond_yellow);
                SpannableString spannableString = new SpannableString("创建俱乐部最低金额为100，，您的余额不足，请及时充值！");
                spannableString.setSpan(imageSpan, 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "钻石不足",
                        spannableString, "立即充值",false);
                dialog.setRsp(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s){
                            ChargeDialog mChargeDialog = new ChargeDialog(mActivity, mActivity);
                            mChargeDialog.show();
                        }
                    }
                });
                dialog.show(getSupportFragmentManager(), "");
            } else if (jsonObject.getString("code").equals("200")) {
//                Intent creategroupintent = new Intent("creategroup");
//                creategroupintent.putExtra(Utils.GROUPID, jsonObject.getJSONObject("data").getLong("groupId") + "");
//                creategroupintent.putExtra(Utils.GROUPNAME, jsonObject.getJSONObject("data").getString("groupName"));
//                creategroupintent.putExtra(Utils.GROUP_HEAD_URL, jsonObject.getJSONObject("data").getString("groupHead"));
//                sendBroadcast(creategroupintent);

                GroupNewBean groupBean = new Gson().fromJson(jsonObject.getString("data"), GroupNewBean.class);
                String groupId = jsonObject.getJSONObject("data").getLong("groupId") + "";
                String groupName = jsonObject.getJSONObject("data").getString("groupName");
                String groupHead = jsonObject.getJSONObject("data").getString("groupHead");
                String groupNotice = getIntent.getStringExtra("notice");
                String name = getIntent.getStringExtra("name");
                createGroup(groupId,name,groupHead,groupNotice,groupBean);
            }else if (jsonObject.getString("code").equals("70004")){//{"code":"70004","message":"您创建俱乐部数量已到达上限","data":{}}
                toastShow(jsonObject.getString("message"));
                VipSetGradeDialogFragment dialogFragment
                        = new VipSetGradeDialogFragment(true, Gravity.CENTER, "您已经达到当前创建俱乐部数量上限，请升级VIP等级。","确认");
                dialogFragment.setRsp(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {

                        }
                    }
                });
                if (getFragmentManager() != null) {
                    dialogFragment.show(getSupportFragmentManager(), "");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGroup(String groupId, String groupName, String groupHead, String groupNotice,GroupNewBean groupBean) {
        TIMGroupManager.CreateGroupParam param = new TIMGroupManager.CreateGroupParam(Utils.GROUP_TYPE, groupName);
        param.setGroupId(groupId);
        param.setNotification(groupNotice);
        param.setGroupName(groupName);
//        param.setFaceUrl(groupHead);
        InvitaionMemberUtils.CreateGroup(param, new TIMValueCallBack<String>() {
            @Override
            public void onError(int code, String desc) {
                toastShow(desc);
            }

            @Override
            public void onSuccess(String s) {
                toastShow("创建俱乐部成功");
//                Intent intent = new Intent(App.getAppContext(), UnmannedActivity.class);
//                intent.putExtra(Utils.GROUPID, groupId);
//                intent.putExtra(Utils.GROUPNAME, groupName);
//                intent.putExtra(Utils.GROUP_HEAD_URL, groupHead);
//                intent.putExtra("notice", groupNotice);
//                intent.putExtra("name", getIntent.getStringExtra("name"));
//                intent.putExtra("data", groupBean);
//                startActivity(intent);
                sendMessage(s,groupName);
            }
        });
    }

    private void sendMessage(String groupId,String name) {
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
                AppManager.getAppManager().finishAllActivity();
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("msg", "sendTipsMessage failed, code: " + errCode + "|desc: " + errMsg);
            }
        });
    }

    @Override
    public void CreateGroupfail(String s) {
        toastShow(R.string.nonetwork);
    }

    @Override
    public void updateGroupSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            toastShow(jsonObject.getString("message"));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }


}
