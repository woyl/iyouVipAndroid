package com.jfkj.im.ui.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.jfkj.im.Bean.CzModeListBean;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.mvp.Account.AccountPresenter;
import com.jfkj.im.mvp.Account.AccountView;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.LottieDialog;
import com.jfkj.im.ui.dialog.Paydialog;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;

/**
 * <pre>
 * Description:  余额
 * @author :   ys
 * @date :         2019/12/5
 * </pre>
 */
public class AccountActivity extends BaseActivity<AccountPresenter> implements AccountView, Paydialog.OnClickOutListener {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_describe)
    TextView mTvDescribe;
    @BindView(R.id.layout_balance)
    LinearLayout mLayoutBalance;
    @BindView(R.id.tv_gift)
    TextView mTvGift;
    @BindView(R.id.layout_gift)
    LinearLayout mLayoutGift;
    private ChargeDialog mChargeDialog;
    AccountPresenter accountPresenter;
    Paydialog paydialog;
    private String strMoney;
    private String cexchangeid;
    @BindView(R.id.ll_layout)
    LinearLayout linearLayout;
    private LottieDialog loadingDialog;

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.netWork()) {
            linearLayout.setVisibility(View.GONE);
        }else{
            toastShow(R.string.nonetwork);
            linearLayout.setVisibility(VISIBLE);
        }

        accountPresenter.accountBalance();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public AccountPresenter createPresenter() {
        return accountPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        paydialog=new Paydialog(mActivity,this,0);
        accountPresenter=new AccountPresenter(this);

        mTvTitle.setText("我的账户");
        mTvTitleRight.setText("充值记录");

        mTvBalance.setText(UserInfoManger.getUserBanlance());
        mTvGift.setText(UserInfoManger.getUserCost());

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("paymoney");
        intentFilter.addAction("rechargecompleted");
        registerReceiver(broadcastReceiver, intentFilter);

        // accountPresenter.getExchangeList("1");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @OnClick({R.id.tv_chong,R.id.iv_title_back, R.id.tv_title_right, R.id.layout_balance, R.id.layout_gift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_chong:
                if (Utils.netWork()) {
                    accountPresenter.accountBalance();
                    linearLayout.setVisibility(View.GONE);
                }else{
                    toastShow(R.string.nonetwork);
                    linearLayout.setVisibility(VISIBLE);
                }
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                JumpUtil.overlay(this, ChargeRecordActivity.class);
                break;
            case R.id.layout_balance:
                //todo 充值
//                showChargeDialog();
                accountPresenter.getExchange();
                break;
            case R.id.layout_gift:
                JumpUtil.overlay(this, SendGiftActivity.class);
                break;
        }
    }



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OkLogger.e(UserInfoManger.getToken());
            switch (intent.getAction()) {
                case "paymoney":
                    strMoney = intent.getStringExtra(Utils.MONEY);
                    cexchangeid = intent.getStringExtra("cexchangeid");

                    paydialog.setMoney(strMoney);
                    mChargeDialog.dismiss();
                    accountPresenter.account();

                    break;
                case "rechargecompleted":
                    accountPresenter.accountBalance();
                    if(mChargeDialog!=null){
                        mChargeDialog.dismiss();
                    }
                    if(paydialog!=null){
                        paydialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    public void AccountSuccess(String s) {
        try {
            JSONObject jsonObject=new JSONObject(s);
            if(jsonObject.getString("code").equals("10003")){
                CzModeListBean czModeListBean= JSON.parseObject(s,CzModeListBean.class);
                paydialog.setCzModeListBean(czModeListBean,this);
               // paydialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showList(ExChangeBean bean) {
        mChargeDialog = new ChargeDialog(this,this);
        mChargeDialog.show();
    }

    /**
     * showDesc 弹窗内容~~~~
     * isShow 是否弹窗展示 1 是 0 否
     * vipLevel vip等级
     *
     * 生成订单访问成功
     * @param response
     */
    @Override
    public void playSuccess(AddMoneyBean response) {
        hideLoading();

        ToastUtils.showShortToast(response.getMessage());
        if (response.getCode().equals("200")) {
            if (paydialog != null) {
                paydialog.dismiss();
            }

            accountPresenter.accountBalance();
//            if (response.getData().getIsShow() == 1) {
//                if (response.getCode().equals("200")) {
//                    ToastUtils.showShortToast(response.getMessage());
//                    OkLogger.e("" + response.getData().getIsShow());
//                    if (response.getData().getIsShow() == 1) {
//                        UserInfoManger.saveUserVipLevel(response.getData().getVipLevel() + "");
//                        //EventBus
//                        EventBus.getDefault().post(response);
//
//                    }
//                    startActivity(new Intent(this, MainActivity.class));
//                    //JumpUtil.overlay(this, MainActivity.class);
//                } else {
//                    ToastUtils.showShortToast(response.getMessage());
//                }
//
//
//            }
        }
    }
            @Override
            public void accountBalanceSuccess() {
                mTvBalance.setText(UserInfoManger.getUserBanlance());
                mTvGift.setText(UserInfoManger.getUserCost());
            }

            @Override
            public void showLoading () {
                loadingDialog = new LottieDialog(this);
                loadingDialog.show();
            }

            @Override
            public void hideLoading () {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onPlay (String playId){
//                showLoading();
//                accountPresenter.play(playId, strMoney, cexchangeid);
            }


    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserInfo(EventMessage message) {
        if (message.getType() == REFRUSH_USER_BALANCE) {

            //刷新余额数据
            mvpPresenter.accountBalance();
        }
    }

}
