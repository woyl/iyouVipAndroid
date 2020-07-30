package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.Bean.SelectpacketsBean;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.adapter.SelectpacketsAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.selectpackets.SelectpacketsView;
import com.jfkj.im.mvp.selectpackets.Selectpacketspresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.NumberpacketsDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class SelectpacketsActivity extends BaseActivity<Selectpacketspresenter> implements View.OnClickListener, OnItemClickListener, NumberpacketsDialog.Ontemclick, ResponListener<Boolean>, SelectpacketsView {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.next_btn)
    Button next_btn;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    SelectpacketsAdapter selectpacketsAdapter;
    List<SelectpacketsBean> selectpacketsBeanList = new ArrayList<>();
    NumberpacketsDialog dialog;
    Intent getIntent;
    private boolean isGroup;
    Selectpacketspresenter selectpacketspresenter;
    private ChargeDialog mChargeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head);
        AppManager.getAppManager().addActivity(this);
        title_left_tv.setBackgroundResource(R.mipmap.iv_back_black);
        selectpacketsAdapter = new SelectpacketsAdapter();
        swiperecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        swiperecyclerview.setOnItemClickListener(this);
        swiperecyclerview.setAdapter(selectpacketsAdapter);
        title_left_tv.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        next_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_tv_three_bg_gray_s));
        next_btn.setEnabled(true);
        selectpacketsBeanList.addAll(getList());
        selectpacketsAdapter.setList(selectpacketsBeanList);
        dialog = new NumberpacketsDialog(this);
        dialog.setOntemclick(this);
        getIntent = getIntent();
        isGroup = getIntent.getBooleanExtra("group", false);
        selectpacketspresenter = new Selectpacketspresenter(this);

        selectpacketspresenter.getExchangeList("1");
        if (isGroup) {
            next_btn.setText("确认");
        } else {
            next_btn.setText("下一步");
        }
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("recharge");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectpackets;
    }

    @Override
    public Selectpacketspresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.next_btn:
                if (isGroup) {
                    String redNum = "";
                    for (int i = 0; i < selectpacketsBeanList.size(); i++) {
                        if (selectpacketsBeanList.get(i).isFlag()) {
                            redNum = selectpacketsBeanList.get(i).getPackets();
                        }
                    }
                    GroupInfoSetUtils.setGroupInfo(this,getIntent.getStringExtra(Utils.GROUPID), redNum, "", new TIMValueCallBack<Boolean>() {
                        @Override
                        public void onError(int code, String desc) {
                            toastShow(code);
                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            if (aBoolean){
                                finish();
                            }
                        }
                    });
                } else {
                    long banlance = 0;
                    if (!TextUtils.isEmpty(UserInfoManger.getUserBanlance())) {
                        banlance = Long.parseLong(UserInfoManger.getUserBanlance());
                    }
                    Intent intent = new Intent(App.getAppContext(), SelectpackettimeActivity.class);
                    intent.putExtra("name", getIntent.getStringExtra("name"));
                    intent.putExtra("notice", getIntent.getStringExtra("notice"));
                    intent.putExtra("group", false);
                    for (int i = 0; i < selectpacketsBeanList.size(); i++) {
                        if (selectpacketsBeanList.get(i).isFlag()) {
                            intent.putExtra("packets", selectpacketsBeanList.get(i).getPackets());
                            if (banlance < 100) {
                                ImageSpan imageSpan = new ImageSpan(this, R.mipmap.icon_diamond_yellow);
                                SpannableString spannableString = new SpannableString("创建俱乐部最低金额为100，，您的余额不足，请及时充值！");
                                spannableString.setSpan(imageSpan, 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "钻石不足",
                                        spannableString, "立即充值",true);

                                dialog.setRsp(new ResponListener<Boolean>() {
                                    @Override
                                    public void Rsp(Boolean s) {
                                        if (s){
                                            ChargeDialog mChargeDialog = new ChargeDialog(mActivity,mActivity);
                                            mChargeDialog.show();
                                        }
                                    }
                                });
                                dialog.show(getSupportFragmentManager(), "");
                            } else {
                                if (i == 4) {
                                    long money = Long.parseLong(selectpacketsBeanList.get(i).getPackets());
                                    if (banlance < money) {
                                        ImageSpan imageSpan = new ImageSpan(this, R.mipmap.icon_diamond_yellow);
//                                        SpannableString spannableString = new SpannableString("创建俱乐部最低金额不能等于定义金额，，您的余额不足，请及时充值！");
//                                        spannableString.setSpan(imageSpan, 17, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        SpannableString spannableString = new SpannableString("创建俱乐部最低金额为100，，您的余额不足，请及时充值！");
                                        spannableString.setSpan(imageSpan, 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        showPayDialog(spannableString);
                                    }else {
                                        startActivity(intent);
                                    }
                                } else {
                                    startActivity(intent);
                                }
                            }

                        }
                    }
                }
//                finish();
                break;


        }
    }


    private void showPayDialog(SpannableString spannableString) {
        PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                spannableString, "立即充值",true);
        dialog.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s){
                    ChargeDialog mChargeDialog = new ChargeDialog(mActivity,mActivity);
                    mChargeDialog.show();
                }
            }
        });
        if (getFragmentManager() != null) {
            dialog.show(getSupportFragmentManager(), "");
        }
    }

    public List<SelectpacketsBean> getList() {
        List<SelectpacketsBean> list = new ArrayList<>();
        SelectpacketsBean selectpacketsBean1 = new SelectpacketsBean("10", true);
        SelectpacketsBean selectpacketsBean2 = new SelectpacketsBean("30", false);
        SelectpacketsBean selectpacketsBean3 = new SelectpacketsBean("50", false);
        SelectpacketsBean selectpacketsBean4 = new SelectpacketsBean("100", false);
        SelectpacketsBean selectpacketsBean5 = new SelectpacketsBean("", false);
        list.add(selectpacketsBean1);
        list.add(selectpacketsBean2);
        list.add(selectpacketsBean3);
        list.add(selectpacketsBean4);
        list.add(selectpacketsBean5);
        return list;
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        if (adapterPosition != 4) {
            for (int i = 0; i < selectpacketsBeanList.size(); i++) {
                selectpacketsBeanList.get(i).setFlag(false);
            }
            selectpacketsBeanList.get(adapterPosition).setFlag(true);
            selectpacketsAdapter.setList(selectpacketsBeanList);
        }
        if (adapterPosition == 4) {
            dialog.show();
        }
    }

    @Override
    public void onclick(String s) {
        selectpacketsBeanList.remove(4);
        for (int i = 0; i < selectpacketsBeanList.size(); i++) {
            if (selectpacketsBeanList.get(i).isFlag()) {
                selectpacketsBeanList.get(i).setFlag(false);
            }
        }
        SelectpacketsBean selectpacketsBean = new SelectpacketsBean(s, true);
        selectpacketsBeanList.add(4, selectpacketsBean);
        selectpacketsAdapter.setList(selectpacketsBeanList);
    }

    @Override
    public void Rsp(Boolean s) {


    }

    @Override
    public void showList(ExChangeBean bean) {
        mChargeDialog = new ChargeDialog(this,this);
        mChargeDialog.show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "recharge":

                    selectpacketspresenter.getExchange();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }
}
