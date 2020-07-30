package com.jfkj.im.ui.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.utils.JumpUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  所在位置
 * @author :   ys
 * @date :         2019/12/10
 * </pre>
 */
public class ChooseLocationActivity extends BaseActivity {
    private final static int SETTING_LOCATION = 112;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title_location)
    TextView mTvTitleLocation;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.tv_hide_location)
    TextView mTvHideLocation;
    @BindView(R.id.tv_set_location)
    TextView mTvSetLocation;
    @BindView(R.id.layout_set_location)
    LinearLayout mLayoutSetLocation;
    private String location;
    private String setLocation;
    private CommonDialog hintDialog = null;
    private ChargeDialog chargeDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_location;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            location = bundle.getString("location");
            mTvLocation.setText(location);
        }

    }

    @OnClick({R.id.iv_back, R.id.tv_hide_location, R.id.layout_set_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent();
                intent.putExtra("user_location", mTvLocation.getText().toString());
                setResult(10, intent);
                finish();
                break;
            case R.id.tv_hide_location:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.layout_set_location:
                if (Integer.valueOf(UserInfoManger.getUserVipLevel()) < 40) {
                    showHintDialog();
                } else {
                    Bundle bundle = new Bundle();
                    JumpUtil.startForResult(this, SetLocationActivity.class, SETTING_LOCATION, bundle);
                }

                break;
        }
    }

    private void showHintDialog() {
        hintDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_confirm) {
                    hintDialog.dismiss();
                    showChargeDialog();
                } else if (view.getId() == R.id.tv_cancel) {
                    hintDialog.dismiss();
                }
            }
        });
        hintDialog.setTitleText("温馨提示").setContentText("VIP等级达到40级以后，才可解锁自定义位置功能。").setConfirmBtnText("去充值").show();
    }

    private void showChargeDialog() {
        chargeDialog = new ChargeDialog(this,this);
        chargeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SETTING_LOCATION) {
                if (data != null) {
                    setLocation = data.getStringExtra("location");
                    mTvLocation.setText(setLocation);
                }
            }
        }
    }
}
