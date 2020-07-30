package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.ChatFriendBean;
import com.jfkj.im.Bean.SendGifBean;
import com.jfkj.im.Bean.UsertailBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.ChatFriend.ChatFriendPresenter;
import com.jfkj.im.mvp.ChatFriend.ChatFriendView;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatFriendActivity extends BaseActivity<ChatFriendPresenter> implements ChatFriendView {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_gift)
    TextView mTvGift;
    @BindView(R.id.tv_gift_num)
    TextView mTvGiftNum;
    @BindView(R.id.iv_gift_picture)
    ImageView mIvGiftPicture;
    @BindView(R.id.view_line1)
    View mViewLine1;
    @BindView(R.id.tv_give_gift)
    TextView mTvGiveGift;
    @BindView(R.id.iv_plus)
    ImageView mIvPlus;
    @BindView(R.id.tv_give_gift_num)
    TextView mTvGiveGiftNum;
    @BindView(R.id.iv_minus)
    ImageView mIvMinus;
    @BindView(R.id.view_line2)
    View mViewLine2;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.iv_diamond)
    ImageView mIvDiamond;
    @BindView(R.id.layout_confirm)
    LinearLayout mLayoutConfirm;
    private int giftNumber = 1;
    private int totalNum;
    private CommonDialog moneyDialog;
    private int defaultPrice = 0;
    private String giftId;
    UsertailBean usertailBean;
    private String userId;
    private ChargeDialog chargeDialog;
    ChatFriendPresenter chatFriendPresenter;
    Intent getIntent;
    public static IUIKitCallBack mCallback;
    public SendGifBean sendGifBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        mTvGiveGiftNum.setText(String.valueOf(giftNumber));
        chatFriendPresenter = new ChatFriendPresenter(this);
        getIntent = getIntent();
        GiftData.DataBean.ArrayBean giftData = getIntent.getParcelableExtra("GiftData");
        userId = getIntent.getStringExtra("userId");
        sendGifBean = new SendGifBean();
        Glide.with(this).load(getIntent.getStringExtra("getPictureUrl")).into(mIvGiftPicture);
        chatFriendPresenter.loadUserVerifyInfo(userId);
        defaultPrice = Integer.parseInt(getIntent.getStringExtra("price"));
        int privateprice = (int) defaultPrice;
        mTvConfirm.setText(privateprice + "");
        giftId = giftData.getGiftId();
        mTvGiftNum.setText("x" + giftNumber);
    }

    @OnClick({R.id.iv_back, R.id.iv_plus, R.id.iv_minus, R.id.layout_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_plus:
                giftNumber++;
                if (giftNumber >= 99) {
                    mIvPlus.setBackgroundResource(R.mipmap.iv_gift_plus_disable);
                    mIvPlus.setEnabled(false);
                } else if (giftNumber >= 1) {
                    mIvPlus.setBackgroundResource(R.mipmap.iv_gift_plus);
                    mIvPlus.setEnabled(true);
                    mIvMinus.setBackgroundResource(R.mipmap.iv_gift_minus);
                    mIvMinus.setEnabled(true);
                }
                changeNumber(giftNumber);
                break;
            case R.id.iv_minus:
                giftNumber--;
                if (giftNumber <= 1) {
                    mIvMinus.setBackgroundResource(R.mipmap.iv_gift_minus_disable);
                    mIvMinus.setEnabled(false);
                } else if (giftNumber <= 99) {
                    mIvMinus.setBackgroundResource(R.mipmap.iv_gift_minus);
                    mIvMinus.setEnabled(true);
                    mIvPlus.setBackgroundResource(R.mipmap.iv_gift_plus);
                    mIvPlus.setEnabled(true);
                }
                changeNumber(giftNumber);
                break;
            case R.id.layout_confirm:

                if (check()) {

                    chatFriendPresenter.chatfriendpresenter(userId, giftId, giftNumber + "");
                }
                break;
        }
    }

    /**
     * 对比余额和好友人数
     *
     * @return
     */
    private boolean check() {

        if (Double.valueOf(UserInfoManger.getUserBanlance()) < totalNum) {
            showHintDialog();
            return false;
        }
        return true;
    }

    /**
     * 余额不足提示dialog
     */
    private void showHintDialog() {
        moneyDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_confirm) {
                    moneyDialog.dismiss();
                    //todo 充值

                    showChargeDialog();
                } else if (view.getId() == R.id.tv_cancel) {
                    moneyDialog.dismiss();
                }
            }
        });
        moneyDialog.setTitleText("温馨提示")
                .setContentText("您的余额不足请及时充值！")
                .setCancelBtnText("取消")
                .setConfirmBtnText("立即充值")
                .show();
    }

    private void showChargeDialog() {
        chargeDialog = new ChargeDialog(this, this);
        chargeDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_friend;
    }

    /**
     * 礼物数量和话费金额
     *
     * @param giftNumber
     */
    private void changeNumber(int giftNumber) {
        mTvGiftNum.setText("x" + giftNumber);
        totalNum = giftNumber * defaultPrice;
        mTvConfirm.setText(totalNum + "");
        mTvGiveGiftNum.setText(giftNumber + "");
    }

    @Override
    public ChatFriendPresenter createPresenter() {
        return chatFriendPresenter;
    }

    @Override
    public void ChatFriend(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("200")) {
                mCallback.onError(s, Integer.parseInt(mTvGiveGiftNum.getText().toString().trim()), giftId);
                Intent intent = new Intent(mActivity, ChatActivity.class);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setChatName(usertailBean.getData().getUserNickName());
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(userId);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            } else if (jsonObject.getString("code").equals("50000")) {
                PublicComCancelDialog dialog
                        = new PublicComCancelDialog(true, Gravity.CENTER,
                        "钻石不足", "你的余额不足,请及时充值！", "立即充值", true);
                dialog.setRsp(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        showChargeDialog();
                    }
                });
                dialog.show(getSupportFragmentManager(), "");
            } else if (jsonObject.getString("code").equals("70020")) {//{"code":"70020","message":"对方不是您的好友,无法继续操作","data":{}}
                toastShow("对方不是您的好友,无法继续操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUserVerifyInfoSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);

            if (jsonObject.getString("code").equals("200")) {
                usertailBean = JSON.parseObject(s, UsertailBean.class);
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

    public static void setCallback(IUIKitCallBack callback) {
        mCallback = callback;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mCallback = null;
    }
}
