package com.jfkj.im.ui.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CardInfoBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.mine.CarryOverPresenter;
import com.jfkj.im.mvp.mine.CarryOverView;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CarryOverActivity extends BaseActivity<CarryOverPresenter> implements CarryOverView, TextWatcher {
    private CarryOverPresenter presenter;

    @BindView(R.id.tv_title)
    TextView tvTitle;


    @BindView(R.id.tv_bank_user_name)
    TextView tvBankAndUserName;

    @BindView(R.id.tv_card)
    TextView tv_card;

    @BindView(R.id.iv_blank)
    ImageView ivBlank;


    @BindView(R.id.tv_max_money)
    TextView mTvMaxMoney;


    @BindView(R.id.ed_money)
    EditText edMoney;
    private String maxmoney;


    @BindView(R.id.tv_submit)
    TextView tvSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);

        presenter = new CarryOverPresenter(this);
        presenter.getBankCardInfo();


        edMoney.addTextChangedListener(this);

        initData();
    }


    @OnClick({R.id.iv_title_back, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                String inMoney = edMoney.getText().toString().trim();

                if (!"".equals(inMoney)) {
                    if (Double.parseDouble(inMoney) > Double.parseDouble(maxmoney)) {
                        ToastUtils.showShortToast("不能大于最高提现金额");
                    } else {
                        presenter.settlementApplication(inMoney);
                    }
                }
                break;
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    private void initData() {
        tvTitle.setText("");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carry_over;
    }

    @Override
    public CarryOverPresenter createPresenter() {
        return presenter;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void showBankCardInfo(CardInfoBean bean) {

        tvBankAndUserName.setText(bean.getData().getBankname() + "(" + bean.getData().getRealname() + ")");

        tv_card.setText(bean.getData().getBankcard());


        maxmoney = bean.getData().getMoney();
        mTvMaxMoney.setText("最高可结算：￥" + maxmoney);


        if (bean.getData().getBankname().equals("中国储蓄银行")) {
            Glide.with(this).load(R.drawable.icon_youzheng).into(ivBlank);
        } else if (bean.getData().getBankname().equals("中国建设银行")) {
            Glide.with(this).load(R.drawable.icon_jianshe).into(ivBlank);
        } else if (bean.getData().getBankname().equals("中国交通银行")) {
            Glide.with(this).load(R.drawable.icon_jiaotong).into(ivBlank);
        } else if (bean.getData().getBankname().equals("中国农业银行")) {
            Glide.with(this).load(R.drawable.icon_nongye).into(ivBlank);
        } else if (bean.getData().getBankname().equals("中国银行")) {
            Glide.with(this).load(R.drawable.icon_china).into(ivBlank);
        } else if (bean.getData().getBankname().equals("中国招商银行")) {
            Glide.with(this).load(R.drawable.icon_zhaoshang).into(ivBlank);
        }

    }

    @Override
    public void settlementSuccess(BaseResponse response) {
        ToastUtils.showShortToast(response.getMessage());

        if (response.isSuccess()) {
            JumpUtil.overlay(this, MainActivity.class);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (TextUtils.isEmpty(s)) {
            tvSubmit.setAlpha(0.5f);
            tvSubmit.setClickable(false);
            tvSubmit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
        } else {
            tvSubmit.setAlpha(1f);
            tvSubmit.setClickable(true);
            tvSubmit.setBackgroundResource(R.drawable.shap_bt_two_black_bg);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
