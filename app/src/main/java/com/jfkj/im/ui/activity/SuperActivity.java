package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Super.SuperPresenter;
import com.jfkj.im.mvp.Super.SuperView;
import com.jfkj.im.ui.dialog.SuperDialog;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class SuperActivity extends BaseActivity<SuperPresenter> implements View.OnClickListener, SuperView {
    @BindView(R.id.upgrade_super_btn)
    AppCompatButton upgrade_super_btn;
    @BindView(R.id.back_iv)
    AppCompatImageView back_iv;
    Intent intent;
    SuperPresenter superPresenter;
    SuperDialog superDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upgrade_super_btn.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        superDialog=new SuperDialog(mActivity);
        intent = getIntent();
        superPresenter = new SuperPresenter(this);
        if (intent.getIntExtra(Utils.ISSUPER, -1) ==1) {
            upgrade_super_btn.setVisibility(View.GONE);
        } else {
            upgrade_super_btn.setVisibility(View.VISIBLE);
        }
        superDialog.setClick(new SuperDialog.onClick() {
            @Override
            public void onclick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_super;
    }

    @Override
    public SuperPresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upgrade_super_btn:
                if (Utils.netWork()) {
                    superPresenter.Super(intent.getStringExtra(Utils.GROUPID), "1");
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    @Override
    public void SuperSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);

            superDialog.show();
            if (jsonObject.getString("code").equals("200")) {
                upgrade_super_btn.setVisibility(View.GONE);

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
}
