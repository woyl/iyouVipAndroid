package com.jfkj.im.TIM.redpack.chatroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.HeadUrlNameUtils;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.redpack.tips.SendmessageUtils;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.AnswerSubmit.AnswerSubmitPresenter;
import com.jfkj.im.mvp.AnswerSubmit.AnswerSubmitView;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.SendRedPackageSqure.SendRedPackagePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_TYPE_ONE;

public class AnswerSubmitActivity extends BaseActivity<AnswerSubmitPresenter> implements AnswerSubmitView {

    @BindView(R.id.iv_title_right_icon1)
    ImageView iv_title_right_icon1;
    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    AnswerSubmitPresenter answerSubmitPresenter;
    private String answerIds,questionIds,headUrl,nickName;
    private ChargeDialog mChargeDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniViews();
        intDatas();
    }

    private void iniViews() {
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
    }

    @SuppressLint("SetTextI18n")
    private void intDatas() {
        answerSubmitPresenter=new AnswerSubmitPresenter(this);
        answerIds = getIntent().getStringExtra("answerIds");
        questionIds = getIntent().getStringExtra("questionIds");
        headUrl = HeadUrlNameUtils.getHeadName()[1];
        nickName = HeadUrlNameUtils.getHeadName()[0];
        answerSubmitPresenter.getExchangeList("1");
        et_money.setText("500");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer_submit;
    }

    @Override
    public AnswerSubmitPresenter createPresenter() {
        return answerSubmitPresenter;
    }

    public String getMoney (){
        return et_money.getText().toString().trim();
    }

    @OnClick({R.id.iv_title_right_icon1,R.id.tv_confirm})
    void Onclick(View view){
        switch (view.getId()){
            case R.id.iv_title_right_icon1:
                TipsBaseDialogFragment tipsBaseDialogFragment
                        = new TipsBaseDialogFragment(true, Gravity.CENTER, "关闭后将不会保存你的答题记录，是否退出？","取消","确定",false);
                tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            finish();
                        }
                    }
                });
                tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.tv_confirm:
                if (TextUtils.isEmpty(getMoney())){
                    toastShow("请输入奖励！");
                    return;
                }
                questionData();
                break;
        }
    }

    private void questionData() {
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.ANSWER_IDS,answerIds);
        map.put(Utils.QUESTION_IDS,questionIds);
        map.put(Utils.MONEY,getMoney());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/sendSquareGame")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    AnswerSubmitBean answerSubmitBean = JSON.parseObject(jsonObject.getString("data"),AnswerSubmitBean.class);
                                    TIMMessage message = buildAnswer(answerSubmitBean);
                                    String chatId = SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID);
                                    TIMConversation conversation
                                            = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatId);
                                    sendMessage(conversation,message,chatId);
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (jsonObject.getString("code").equals("50000")) {
                                    answerSubmitPresenter.getExchange();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            toastShow(R.string.nonetwork);
        }
    }

    private void sendMessage(TIMConversation conversation,TIMMessage message ,String groupId) {
        SendmessageUtils.sendTipsMessage(conversation, message, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.Group);
                chatInfo.setId(groupId);
                chatInfo.setChatName("广场");
                chatInfo.setChatRoom(true);

                ConversationMessage conversationMessage = new ConversationMessage();
                conversationMessage.setConversationId(groupId);
                conversationMessage.setGroup(true);
                conversationMessage.setId(groupId);
                conversationMessage.setTitle("广场");

                Intent intent = new Intent(mActivity, ChatActivity.class);
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

    private TIMMessage buildAnswer(AnswerSubmitBean answerSubmitBean) {
        Gson gson = new Gson();
        RedPackageCustom redPackageCustom = new RedPackageCustom();
//        redPackageCustom.setMsgType(READ_PACKAGE_MSG_TYPE_TWO);
        redPackageCustom.setSendId(TIMManager.getInstance().getLoginUser());
        redPackageCustom.setType(RED_PACKAGE_TYPE_ONE);

        redPackageCustom.setAvatarUrl(headUrl);
        redPackageCustom.setSendName(nickName);
        redPackageCustom.setSendType(RED_PACKAGE_MST_TYPE_THREE);
        redPackageCustom.setVIP(UserInfoManger.getUserVipLevel().trim());
        redPackageCustom.setRedDesc(answerSubmitBean.getCbiztypename());
        redPackageCustom.setTipsType("");
        redPackageCustom.setOrderId(answerSubmitBean.getCgameid());
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectData = new JSONObject();
        try {
//            jsonObjectData.put(Utils.SENDID,TIMManager.getInstance().getLoginUser());
//            jsonObjectData.put(Utils.HEAD_URL,headUrl);
//            jsonObjectData.put(Utils.NICKNAME,nickName);
//            jsonObjectData.put(Utils.VIP,UserInfoManger.getUserVipLevel().trim());
//            jsonObjectData.put(Utils.RED_DESC,answerSubmitBean.getCbiztypename());
            jsonObjectData.put(Utils.ORDERID,answerSubmitBean.getCgameid());
            jsonObjectData.put("cadddate",System.currentTimeMillis()+"");
            jsonObject.put(Utils.TYPE,RED_PACKAGE_TYPE_ONE);
            jsonObject.put(Utils.SENDTYPE,RED_PACKAGE_MST_TYPE_THREE);
            jsonObject.put(Utils.BODY,jsonObjectData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        String data = gson.toJson(redPackageCustom);
        MessageInfo info = MessageInfoUtil.buildCharacterCustomMessage(jsonObject.toString());
        return info.getTIMMessage();
    }

    @Override
    public void showList(ExChangeBean bean) {
        mChargeDialog = new ChargeDialog(this,this);
        mChargeDialog.show();
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "recharge":
                    answerSubmitPresenter.getExchange();
                    break;
            }
        }
    };
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
