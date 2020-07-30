package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.entity.SettleAccountBean;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.ExchangeListPresenter;
import com.jfkj.im.mvp.mine.ExchangeView;
import com.jfkj.im.ui.dialog.CommonCenterDialog;
import com.jfkj.im.ui.dialog.ExChangeDialog;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;

/**
 * <pre>
 * Description:  礼物
 * @author :   ys
 * @date :         2019/12/5
 * </pre>
 */
public class GiftActivity extends BaseActivity<ExchangeListPresenter> implements ExchangeView {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.tv_gift)
    TextView mTvGift;
    @BindView(R.id.layout_gift)
    LinearLayout mLayoutGift;

    @BindView(R.id.ll_gift)
    LinearLayout ll_gift;

    @BindView(R.id.tv_jsmoney)
    TextView tv_jsmoney;


    @BindView(R.id.ll_layout)
    LinearLayout linearLayout;


    private String irhz = "2";
    private String osName = "1";

    private ExChangeDialog mExChangeDialog = null;
    private CommonCenterDialog mConfirmDialog = null;
    private String exchangeId ; //汇率id
    private String IRHZ ; //1：人民币充值钻石 2：金币兑换钻石
    private String gender;
    private ExchangeListPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift;
    }

    @Override
    public ExchangeListPresenter createPresenter() {
        return presenter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.netWork()) {
            linearLayout.setVisibility(View.GONE);
        }else{
            toastShow(R.string.nonetwork);
            linearLayout.setVisibility(VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        gender = UserInfoManger.getGender();

          presenter =   new ExchangeListPresenter(this);

        if("1".equals(gender)){
            //性别男
            ll_gift.setVisibility(View.GONE);
            mTvTitleRight.setText("兑换钻石");
        }else{
            //性别女
            ll_gift.setVisibility(View.VISIBLE);
            mTvTitleRight.setText("结算");
        }

        mTvTitle.setText("我的礼物");



        if (UserInfoManger.getUserGift() != null  && !"null".equals(UserInfoManger.getUserGift())){
            mTvGift.setText(UserInfoManger.getAccumulatedGifts()+" ");
        }else{
            mTvGift.setText( 0 + " ");
        }

        presenter.settlement();
    }

    @OnClick({R.id.tv_chong, R.id.iv_title_back, R.id.tv_title_right, R.id.layout_gift})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_chong:
                if (Utils.netWork()) {
                    presenter.settlement();
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

                if("1".equals(gender)){
                    //男--- 兑换砖石
                    presenter.getExchangeList("2");
                }else{
                    //女--- 结算
                    JumpUtil.overlay(this,SettleAccountActivity.class);
                }

//                mvpPresenter.getExchangeList(irhz,osName);
                break;
            case R.id.layout_gift:
                Bundle bundle = new Bundle();
                bundle.putString("obtain_gift",UserInfoManger.getUserGift());
                JumpUtil.overlay(this,ObtainGiftActivity.class,bundle);
                break;
        }
    }


    @Override
    public void showList(ExChangeBean bean) {

        mExChangeDialog = new ExChangeDialog(this, bean.getData(), new onItemPositionClickListener() {
            @Override
            public void onPositionClick(int position) {
                mExChangeDialog.dismiss();
                exchangeId = bean.getData().get(position).getCexchangeid();
                IRHZ = bean.getData().get(position).getIrhz();
                showConfrimDialog();
            }
        });
        mExChangeDialog.show();
    }

    /**
     * 兑换成功
     */
    @Override
    public void exchangeSuccess() {
        //兑换成功之后要在请求一次账户金额
        ToastUtils.showShortToast("兑换成功");

        EventMessage eventMessage = new EventMessage();
        eventMessage.setType(REFRUSH_USER_BALANCE);
        eventMessage.setMessage("兑换钻石成功");
        EventBus.getDefault().post(eventMessage);
    }

    @Override
    public void showSettlement(SettleAccountBean bean) {
        tv_jsmoney.setText("￥" + bean.getData().getSum() );
    }

    /**
     * 确认兑换dialog
     */
    private void showConfrimDialog() {
        mConfirmDialog = new CommonCenterDialog(this, R.style.dialogstyle, new CommonCenterDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_confirm:
                        if (mExChangeDialog != null && mExChangeDialog.isShowing()){
                            mExChangeDialog.dismiss();
                        }
                        mConfirmDialog.dismiss();
                        presenter.exchange(exchangeId,IRHZ);
                        break;
                    case R.id.tv_cancel:
                        mConfirmDialog.dismiss();
                        break;
                }
            }
        });
        mConfirmDialog.setTitleText("确认兑换").setContentText("是否确认兑换钻石").show();
    }




    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
