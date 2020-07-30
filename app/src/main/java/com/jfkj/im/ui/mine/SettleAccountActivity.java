package com.jfkj.im.ui.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.OnClick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.entity.SettleAccountBean;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Selectfriend.SelectfriendPresenter;
import com.jfkj.im.mvp.mine.SettleAccountPresenter;
import com.jfkj.im.mvp.mine.SettleAccountView;
import com.jfkj.im.ui.dialog.CommonCenterDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.ExChangeDialog;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.ToastUtils;

public class SettleAccountActivity extends BaseActivity<SettleAccountPresenter> implements SettleAccountView {

    @BindView(R.id.iv_title_right)
    AppCompatImageView iv_title_right;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    private CommonDialog exitDialog;
    private Context context;
    private SettleAccountPresenter presenter;


    private int binding = -1;

    private int infoType = -1;


    @BindView(R.id.tv_max_money)
    TextView tv_max_money;

    @BindView(R.id.tv_rate)
    TextView tv_rate;

    @BindView(R.id.tv_sum)
    TextView tv_sum;

    @BindView(R.id.tv_info_rmationt_ype)
    TextView tv_info_rmationt_ype;
    private ExChangeDialog mExChangeDialog;

    private String irhz;
    private String exchangeId;
    private CommonCenterDialog mConfirmDialog;
    private int sum = 0;
    private boolean isShenhe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);

        presenter = new SettleAccountPresenter(this);
        context = this;
        initDate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.settlement();
    }

    private void initDate() {
        iv_title_right.setVisibility(View.GONE);
        tv_title.setText("结算礼物");
    }


    @OnClick({R.id.iv_title_back, R.id.rl_convert, R.id.rl_settlement, R.id.rl_highest, R.id.rl_scale, R.id.rl_certification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

            case R.id.rl_convert:
                //"女"兑换钻石
                presenter.getExchangeList("2");
                break;

            case R.id.rl_scale:
                //结算比例
                JumpUtil.overlay(this, CarryScaleActivity.class);
                break;
            case R.id.rl_settlement:
                //结算记录
                Intent intent = new Intent(this, ResultRecordActivity.class);
                intent.putExtra("ResultMoney", sum + "");
                startActivity(intent);
                break;
            case R.id.rl_highest:
                if (!isShenhe) {
                    if (infoType == 2) {
                        if (binding == 0) {
                            JumpUtil.overlay(this, BindSettleAccountActivity.class);
                        } else if (binding == 1) {
                            JumpUtil.overlay(this, CarryOverActivity.class);
                        }
                    } else {
                        showExitDialog();
                    }
                } else {
                    toastShow("您的实名认证正在审核中，请您耐心等待");
                }

                break;
            case R.id.rl_certification:
//                showExitDialog();
                if (!isShenhe) {
                    if (infoType == 2) {

                    } else {
                        JumpUtil.overlay(context, CertificationActivity.class);
                    }
                } else {
                    toastShow("您的实名认证正在审核中，请您耐心等待");
                }

                break;
        }
    }

    private void showExitDialog() {
        exitDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        exitDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        exitDialog.dismiss();
                        JumpUtil.overlay(context, CertificationActivity.class);
                        break;
                }
            }
        });
        exitDialog.setTitleText("温馨提示").setContentText("请先进行实名认证，完成后才可绑定结算方式。").setYesBtn("实名认证").show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_account;
    }

    @Override
    public SettleAccountPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSettlement(SettleAccountBean bean) {
        //结算比例
        tv_rate.setText(bean.getData().getRate() + "%");
        //最高可结算
        tv_max_money.setText("￥ " + bean.getData().getMoney());
        //累计结算

        sum = bean.getData().getSum();
        tv_sum.setText("已结算：¥" + bean.getData().getSum());

        //0 未实名 1已提交 2已实名 3实名未通过
        if (bean.getData().getInformationType() == 0) {
            tv_info_rmationt_ype.setText("未实名");
        } else if (bean.getData().getInformationType() == 1) {
            tv_info_rmationt_ype.setText("已提交");
            isShenhe = true;
        } else if (bean.getData().getInformationType() == 2) {
            tv_info_rmationt_ype.setText("已实名");
        } else {
            tv_info_rmationt_ype.setText("未通过");
        }
        binding = bean.getData().getBinding();
        infoType = bean.getData().getInformationType();
    }

    @Override
    public void showList(ExChangeBean bean) {
        mExChangeDialog = new ExChangeDialog(this, bean.getData(), new onItemPositionClickListener() {


            @Override
            public void onPositionClick(int position) {
                mExChangeDialog.dismiss();
                exchangeId = bean.getData().get(position).getCexchangeid();
                irhz = bean.getData().get(position).getIrhz();
                showConfrimDialog();
            }
        });
        mExChangeDialog.show();
    }

    @Override
    public void exchangeSuccess() {
        ToastUtils.showShortToast("兑换成功");
    }


    /**
     * 确认兑换dialog
     */
    private void showConfrimDialog() {
        mConfirmDialog = new CommonCenterDialog(this, R.style.dialogstyle, new CommonCenterDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_confirm:
                        if (mExChangeDialog != null && mExChangeDialog.isShowing()) {
                            mExChangeDialog.dismiss();
                        }
                        mConfirmDialog.dismiss();
                        presenter.exchange(exchangeId, irhz);
                        break;
                    case R.id.tv_cancel:
                        mConfirmDialog.dismiss();
                        break;
                }
            }
        });
        mConfirmDialog.setTitleText("确认兑换").setContentText("是否确认兑换钻石").show();
    }
}
