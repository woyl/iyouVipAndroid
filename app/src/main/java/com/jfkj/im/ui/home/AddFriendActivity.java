package com.jfkj.im.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.NewfriendBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.MessageImageHolder;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Home.AddFriendPresenter;
import com.jfkj.im.mvp.Home.AddFriendView;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description: 添加好友页
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class AddFriendActivity extends BaseActivity<AddFriendPresenter> implements AddFriendView {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_add_friend)
    TextView mTvAddFriend;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.layout_content)
    LinearLayout mLayoutContent;
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
    private double totalNum;
    private CommonDialog moneyDialog;
    private int defaultPrice = 0;
    private String giftId;
    private String content = "";
    private String userId;
    private ChargeDialog chargeDialog;
    String name;
    private IUIKitCallBack mCallback;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    public AddFriendPresenter createPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
//        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        mTvGiveGiftNum.setText(String.valueOf(giftNumber));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            userId = bundle.getString("userId");
            Glide.with(this).load(bundle.getString("pictureUrl")).into(mIvGiftPicture);
            defaultPrice = (int) bundle.getDouble("price");
            Log.d("@@@", "defaultPrice ==== " + defaultPrice);
            mTvConfirm.setText(defaultPrice + "");
            giftId = bundle.getString("giftId");
            mTvGiftNum.setText("x" + giftNumber);
            totalNum = giftNumber * defaultPrice;

            name = bundle.getString("name");

        }
        mEditContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        content = mEditContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            content = "我是" + UserInfoManger.getNickName() + "，很高兴遇见你，交个朋友吧";
        }
        mEditContent.setHint(content);
        mvpPresenter.userdetail(userId);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("rechargecompleted");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @OnClick({R.id.iv_back, R.id.layout_content, R.id.iv_plus, R.id.iv_minus, R.id.layout_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_content:
                KeyBoardUtils.requestShowKeyBord(mEditContent);
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
                if(giftNumber > 1){
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
                }
                break;
            case R.id.layout_confirm:
                if (check()) {
                    mvpPresenter.addFriend(this ,name, Utils.getVersionCode() + "", Utils.CHANNEL_ANDROID, userId, giftId + "", giftNumber + "", mEditContent.getText().toString());
                }
                break;
            default:
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
        chargeDialog = new ChargeDialog(this,this);
        chargeDialog.show();
    }

    /**
     * 礼物数量和话费金额
     *
     * @param giftNumber
     */
    private void changeNumber(int giftNumber) {
        mTvGiftNum.setText("x" + giftNumber);
        totalNum = giftNumber * defaultPrice;
        mTvConfirm.setText((int)totalNum + "");
        mTvGiveGiftNum.setText(giftNumber + "");
    }

    @Override
    public void addSuccess(String userId) {

        TIMFriendRequest timfriendrequest = new TIMFriendRequest(userId);
        TIMFriendshipManager.getInstance().addFriend(timfriendrequest, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtils.showLongToast("请求添加好友失败");
                finish();
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                NewfriendBean newfriendBean = new NewfriendBean();

                ToastUtils.showLongToast("好友申请已发送");
                finish();
            }
        });

    }

    @Override
    public void userdetail(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("200")) {
                UserDetail userDetail = JSON.parseObject(s, UserDetail.class);
                name = userDetail.getData().getUserNickName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFriendSuccess() {
        finish();
    }

    public void setCallback(IUIKitCallBack callback) {
        mCallback = callback;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mCallback = null;
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "rechargecompleted":
                    mvpPresenter.accountBalance();
                    break;
            }
        }
    };

}
