package com.jfkj.im.TIM.redpack.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.HeadUrlNameUtils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.litepal.GroupMessCenterLitepal;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyJoinGroupActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.et_content)
    EditText editText;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    private TIMConversation conversation;

    private int selectionStart;
    private int selectionEnd;
    private CharSequence temp;
    private String nickName;

    //群组信息
    private GroupInfoBean groupInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head);
        editText.addTextChangedListener(this);
        tv_send.setEnabled(false);
        groupInfoBean = getIntent().getParcelableExtra("groupInfo");
        if (groupInfoBean != null) {
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupInfoBean.getGroupId());
        }
        nickName = HeadUrlNameUtils.getHeadName()[0];


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_join_group;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
        selectionStart = editText.getSelectionStart();
        selectionEnd = editText.getSelectionEnd();
        if (temp.length() > 100) {
            toastShow("你输入的字数已经超过了限制！");
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionStart;
            editText.setText(s);
            editText.setSelection(tempSelection);
        } else {
            tv_num.setText("");
            tv_num.setText(temp.length() + "/100");
        }

        if (TextUtils.isEmpty(s.toString())) {
            tv_send.setEnabled(false);
            tv_send.setAlpha(0.3f);
        } else {
            tv_send.setEnabled(true);
            tv_send.setAlpha(1f);
        }
    }

    public String getContent() {
        return editText.getText().toString().trim();
    }

    @OnClick({R.id.tv_send, R.id.iv_title_back})
    void Onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(getContent())) {
                    toastShow("输入申请理由");
                    return;
                }
                send();
                break;
        }
    }

    private void send() {
        TIMGroupManager.getInstance().applyJoinGroup(groupInfoBean.getGroupId(), getContent(), new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                toastShow(desc);
                //接口返回了错误码 code 和错误描述 desc，可用于原因
                //错误码 code 列表请参见错误码表
                Log.i("msg", "applyJoinGroup err code = " + code + ", desc = " + desc);


            }

            @Override
            public void onSuccess() {


                toastShow("发送成功");
                Log.i("msg", "applyJoinGroup success");

                List<GroupMessCenterLitepal> groupMessCenterLitepals =  LitePal.findAll(GroupMessCenterLitepal.class);
//                        LitePal.where("groupId = ?",groupInfoBean.getGroupId()).where("userId = ?",UserInfoManger.getUserId()).find(GroupMessCenterLitepal.class);
                if (groupMessCenterLitepals != null && groupMessCenterLitepals.size() > 0) {
                    for (GroupMessCenterLitepal groupMessCenterLitepal : groupMessCenterLitepals) {
                        if (TextUtils.equals(groupMessCenterLitepal.getGroupId(),groupInfoBean.getGroupId())){
                            groupMessCenterLitepal.delete();
                        }
                    }
                }
                save();



//                TIMMessage msg = new TIMMessage();
//                TIMMessageOfflinePushSettings settings = new TIMMessageOfflinePushSettings();
//                settings.setEnabled(true);
//
//                settings.setDescr(nickName + "申请加入您的俱乐部，点击处理");
//                msg.setOfflinePushSettings(settings);
//                //将 elem 添加到消息
//                conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
//                    @Override
//                    public void onError(int code, String desc) {
//                        progressDialog.dismiss();
//                        Log.e("msg", ",,,,,,code,,,,,,,,," + code);
//                        Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
//                    }
//
//                    @Override
//                    public void onSuccess(TIMMessage timMessage) {
//                        progressDialog.dismiss();
//                        Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
//                        finish();
//                    }
//                });
                finish();
            }
        });
    }

    private void save() {
        GroupMessCenterLitepal groupMessCenterLitepal = new GroupMessCenterLitepal();
        groupMessCenterLitepal.setAddTime(System.currentTimeMillis());
        groupMessCenterLitepal.setApplyStr(getContent());
        groupMessCenterLitepal.setRefuseStr("");
        groupMessCenterLitepal.setGroupId(groupInfoBean.getGroupId());
        groupMessCenterLitepal.setState("0");
        groupMessCenterLitepal.setUserId(UserInfoManger.getUserId());
        groupMessCenterLitepal.setUserName(UserInfoManger.getNickName());
        groupMessCenterLitepal.setUserHeadUrl(UserInfoManger.getUserHeadUrl());
        groupMessCenterLitepal.setHandlerHeadUrl(groupInfoBean.getGroupOwnerHead());
        groupMessCenterLitepal.setHandlerId(groupInfoBean.getGroupOwnerId());
        groupMessCenterLitepal.setHandlerName(groupInfoBean.getGroupOwnerName());
        groupMessCenterLitepal.setVipGrade(groupInfoBean.getVipLevel());
        groupMessCenterLitepal.setGroupName(groupInfoBean.getGroupName());
        groupMessCenterLitepal.save();
    }
}
