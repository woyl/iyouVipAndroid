package com.jfkj.im.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.DiscoverBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.SplashAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.ScollLinearLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class Loginregister_phone_Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.customer_service_iv)
    ImageView customer_service_iv;
    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.tv_user_agreement)
    TextView tv_user_agreement;
    @BindView(R.id.tv_privacy_policy)
    TextView tv_privacy_policy;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;



    Dialog dialog;
    Button btn_agree;
    TextView tv_five;
    ImageView close_iv;
    TextView dialog_user_agreement;
    TextView dialog_privacy_policy;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_loginregister_phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        mRecyclerView.setAdapter(new SplashAdapter(this));
        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(this));
        //smoothScrollToPosition滚动到某个位置（有滚动效果）
        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);

    }





    public void init() {
        customer_service_iv.setOnClickListener(this);
        tv_user_agreement.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(customer_service_iv);
        dialog = new Dialog(mActivity, R.style.dialogstyle);
        dialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_reminder, null);
        dialog.setContentView(view);

        dialog_user_agreement=view.findViewById(R.id.tv_user_agreement);
        dialog_privacy_policy=view.findViewById(R.id.tv_privacy_policy);



        dialog_user_agreement.setOnClickListener(this);
        dialog_privacy_policy.setOnClickListener(this);

        btn_agree = view.findViewById(R.id.btn_agree);
        tv_five = view.findViewById(R.id.tv_five);
        close_iv = view.findViewById(R.id.close_iv);
        close_iv.setOnClickListener(this);
        tv_five.setText("请您确认已年满十八周岁，务必仔细阅读并理 \n" +
                "解相关条款内容，在确认充分理解并同意后使用I you相关产品 \n" +
                "及服务。点击同意即代表您已阅读并同意《用户协议及隐私政策》， \n" +
                "如果您不满意，将可能影响使用I you产品和服务。\n" +
                "我们将按法律法规要求，采取相应安全保护措施，尽力保护您的个人信息安全可控。"

        );
        login_btn.setOnClickListener(this);
        btn_agree.setOnClickListener(this);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.horizontalMargin = 0;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        window.getDecorView().setMinimumWidth(mActivity.getResources().getDisplayMetrics().widthPixels);


    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customer_service_iv:

          Map<String,String> querymap = new HashMap<>();

                querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                querymap.put(Utils.OSNAME, Utils.ANDROID);
                querymap.put(Utils.CHANNEL, Utils.ANDROID);
                querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
                querymap.put(Utils.DEVICEID, Utils.ANDROID);
                querymap.put(Utils.REQTIME, AppUtils.getReqTime());

                OkGo.<String>post(ApiStores.base_url+"/my/customerService")
                        .tag(mActivity)
                        .headers(Utils.TOKEN,SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                        .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                        .params(querymap)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                OkLogger.e(response.body());
                                CustomerServiceBean customerServiceBean = GsonUtils.fromJson(response.body(), CustomerServiceBean.class);
                                customerServiceBean.getData().getUrl();

                                Intent uintent = new Intent(getApplicationContext(),WebActivity.class);
                                uintent.putExtra("title", "在线客服");
                                uintent.putExtra("url",   customerServiceBean.getData().getUrl());
                                startActivity(uintent);
                            }
                        });



                break;
            case R.id.login_btn:
                dialog.show();
                break;
            case R.id.btn_agree:
                dialog.dismiss();

                SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN,"");
                startActivity(new Intent(App.getAppContext(), LoginregisternextActivity.class));
//                finish();
                break;
            case R.id.close_iv:
                dialog.dismiss();

                break;

            case R.id.tv_user_agreement:
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("title", " I you用户协议");
                intent.putExtra("url", ApiStores.baseJsonUrl+"/static/iyou/iyouyhxy.html");
                startActivity(intent);
                break;
            case R.id.tv_privacy_policy:
                Intent privacyintent = new Intent(mActivity, WebActivity.class);
                privacyintent.putExtra("title", "  I you隐私政策");
                privacyintent.putExtra("url", ApiStores.baseJsonUrl+"/static/iyou/iyouysxy.html");
                startActivity(privacyintent);
                break;

        }
    }
}
