package com.jfkj.im.ui.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.OnClick;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.base.IMEventListener;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.DataProvide;
import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.mine.BindSettleAccountPresenter;
import com.jfkj.im.mvp.mine.BindSettleAccountView;
import com.jfkj.im.ui.dialog.ButtomBlankDialog;
import com.jfkj.im.utils.JumpUtil;
import com.lzy.okgo.utils.OkLogger;

public class BindSettleAccountActivity extends BaseActivity<BindSettleAccountPresenter> implements BindSettleAccountView, TextWatcher {

    @BindView(R.id.iv_title_right)
    AppCompatImageView iv_title_right;

    private BindSettleAccountPresenter bindSettleAccountPresenter;


//    //开户名
//    @BindView(R.id.ed_user_name)
//    EditText edUserName;


    @BindView(R.id.ed_blank_branch)
    EditText mBlankBranch;


    @BindView(R.id.ed_bank_card)
    EditText mBankCard;


    @BindView(R.id.ed_bank_card_repeat)
    EditText mBankCardRepat;


    @BindView(R.id.tv_bank_name)
    TextView tv_bank_name;

    @BindView(R.id.tv_title)
    AppCompatTextView titleName;

    @BindView(R.id.tv_submit)
    TextView tv_submit;

    @BindView(R.id.ed_blank_branch_name)
    AppCompatTextView ed_blank_branch_name;



    private String blakBranchName;
    private String bankCard;
    private String bankCardRepat;
    private String bankName = "";
    private ButtomBlankDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        titleName.setText("绑定结算账户");
        bindSettleAccountPresenter = new BindSettleAccountPresenter(this);
        bindSettleAccountPresenter.getBindBank();
        initdata();





        mBlankBranch.addTextChangedListener(this);
        mBankCard.addTextChangedListener(this);
        mBankCardRepat.addTextChangedListener(this);
    }


    @OnClick({R.id.iv_title_back,R.id.tl_blank_name,R.id.tv_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_submit:
//                userName = edUserName.getText().toString().trim();
                blakBranchName = mBlankBranch.getText().toString().trim();
                bankCard = mBankCard.getText().toString().trim();
                bankCardRepat = mBankCardRepat.toString().trim();

                if( "".equals(blakBranchName) || "".equals(bankCard) || "".equals(bankCardRepat) || "".equals(bankName)){
                    ToastUtils.showShort("请填写完整内容");
                }
//                else if(bankCard)
                else if(bankCard.equals(bankCardRepat)){
                    ToastUtils.showShort("两次输入卡号不同");
                }
                else{

                    bindSettleAccountPresenter.bindBank(bankCard,bankName,blakBranchName);
                }


                 break;
            case R.id.tl_blank_name:

                dialog = new ButtomBlankDialog(this, false, false, DataProvide.getBlank(),new ButtomBlankDialog.onConfirmDialogListener() {
                    @Override
                    public void onStrPick(String pickStr) {

                        blakBranchName = mBlankBranch.getText().toString().trim();
                        bankCard = mBankCard.getText().toString().trim();
                        bankCardRepat = mBankCardRepat.toString().trim();

                        OkLogger.e(blakBranchName+ bankCard + bankCardRepat  +bankName);

                        if( !"".equals(blakBranchName) && !"".equals(bankCard) && !"".equals(bankCardRepat) && !"".equals(bankName)){
                            tv_submit.setClickable(true);
                            tv_submit.setBackgroundResource(R.drawable.shape_carry_btn_rgb);
                        }else{
                            tv_submit.setClickable(false);
                            tv_submit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
                        }

                            bankName = pickStr;
                            tv_bank_name.setText(pickStr);

                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }


    private void initdata() {
        iv_title_right.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_settle_account;
    }

    @Override
    public BindSettleAccountPresenter createPresenter() {
        return bindSettleAccountPresenter;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void bindSuccess(BindSettleAccountBean bean) {
        if(bean.getCode().equals("200")){
            ToastUtils.showShort(bean.getMessage());
            JumpUtil.overlay(this,CarryOverActivity.class);
        }else{
            ToastUtils.showShort(bean.getMessage());
        }
    }

    @Override
    public void getBindSuccess(String s) {
        ed_blank_branch_name.setText(s);
    }

    public String getBindBlankName(){
        return ed_blank_branch_name.getText().toString().trim();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        blakBranchName = mBlankBranch.getText().toString().trim();
        bankCard = mBankCard.getText().toString().trim();
        bankCardRepat = mBankCardRepat.getText().toString().trim();
        OkLogger.e(blakBranchName+ bankCard + bankCardRepat  +bankName);
        if( !"".equals(blakBranchName) && !"".equals(bankCard) && !"".equals(bankCardRepat) &&! "".equals(bankName)){
            tv_submit.setClickable(true);
            tv_submit.setAlpha(1);
            tv_submit.setBackgroundResource(R.drawable.shap_bt_two_bg);
        }else{
            tv_submit.setClickable(false);
            tv_submit.setAlpha(0.5f);
            tv_submit.setBackgroundResource(R.drawable.shap_bt_two_bg);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
