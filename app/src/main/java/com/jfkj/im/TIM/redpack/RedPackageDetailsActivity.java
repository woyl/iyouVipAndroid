package com.jfkj.im.TIM.redpack;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.CirImageView;
import com.tencent.imsdk.TIMConversation;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_FOUR;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;

public class RedPackageDetailsActivity extends BaseActivity {

    @BindView(R.id.recyc_list)
    RecyclerView recyc_list;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.rounded_img)
    CirImageView rounded_img;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_details)
    TextView tv_details;
    @BindView(R.id.tv_content_money)
    TextView tv_content_money;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.ll_money)
    LinearLayout ll_money;

    private String getRedId,state;
    private String size = "10";
    private Context mContext;
    private TIMConversation conversation;
    private MyHandler myHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_red_package_details;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
//        setStaturBar(constraint_head);
        mContext = this;
        getRedId = getIntent().getStringExtra("redId");
        state = getIntent().getStringExtra("state");
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(0,300);

    }

    /**
     * 避免handler内存泄露的写法
     * handler为何会导致内存泄露：
     * 如果handler为内部类（非静态内部类），那么会持有外部类的实例，
     * 若在handler.sendMessage的时候，activity finish掉了，那么此时activity将无法得到释放
     * <p>
     * 如果申明handler为静态内部类，则不会含有外部类的引用，
     * 但是需要在handler中更新UI（注意此时handler为static），则需要引入一个activity引用，
     * 显然必须是弱引用，否则会导致和上面一样的结果
     * <p>
     * 使用activity弱引用
     * 之所以使用弱引用，是因为handler为static,使用activity的弱引用来访问activity对象内的成员
     */

    public static class MyHandler extends Handler {
        private WeakReference<RedPackageDetailsActivity> weakReference;

        public MyHandler(RedPackageDetailsActivity redPackageDetailsActivity) {
            this.weakReference = new WeakReference<>(redPackageDetailsActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            RedPackageDetailsActivity activity = weakReference.get();
            if (msg.what == 0) {
                activity.getData();
            }
        }
    }

    private void getData() {
        getRedHeadData();
        getRedContent();
    }

    private void getRedContent() {
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.REDID, getRedId);
        map.put(Utils.PAGESIZE, size);
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/group/loadRedPacketReceiveList")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONObject object = new JSONObject(jsonObject.getString("data"));
                        List<RedPackageListBean> redPackageListBean = JSON.parseArray(object.getString("array"), RedPackageListBean.class);
                        setRecyclerview(redPackageListBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setRecyclerview(List<RedPackageListBean> redPackageListBean) {
        recyc_list.setLayoutManager(new LinearLayoutManager(this));
        CommonRecyclerAdapter<RedPackageListBean> adapter = new CommonRecyclerAdapter<RedPackageListBean>(this, redPackageListBean, R.layout.item_collect_red_details) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, RedPackageListBean redPackageListBeans, int position){
                CirImageView cir_head = holder.getView(R.id.cir_head);
                TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_time = holder.getView(R.id.tv_time);
                TextView tv_money_d = holder.getView(R.id.tv_money_d);
                TextView tv_best = holder.getView(R.id.tv_best);
                if (redPackageListBeans.getBestLuck() == 1){
                    tv_best.setVisibility(View.VISIBLE);
                }else {
                    tv_best.setVisibility(View.INVISIBLE);
                }
                TextView tv_vip = holder.getView(R.id.tv_vip);
                tv_vip.setText("V"+redPackageListBeans.getVipLevel());
                Glide.with(mContext).load(redPackageListBeans.getUserHead()).into(cir_head);
                tv_name.setText(redPackageListBeans.getUserNickName());
                tv_money_d.setText(redPackageListBeans.getMoney() + "");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
                try {
                    Date date = format1.parse(redPackageListBeans.getAddDate());
                    tv_time.setText(format2.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra("userId", redPackageListBeans.getSendId());
                        startActivity(intent);
                    }
                });

            }
        };
        recyc_list.setAdapter(adapter);
    }

    private void getRedHeadData() {
        //redType 201 每日红包 202俱乐部 203 游戏 204广场
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.REDID, getRedId);
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/group/loadRedPacketDetails")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("200")) {
                        RedPackageHeadBean redPackageHeadBean = JSON.parseObject(jsonObject.getString("data"), RedPackageHeadBean.class);
                        setData(redPackageHeadBean);
                    } else {
                        toastShow(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(RedPackageHeadBean redPackageHeadBean) {
        if (redPackageHeadBean == null) return;
        Glide.with(this).load(redPackageHeadBean.getUserHead()).into(rounded_img);
        tv_name.setText(redPackageHeadBean.getUserNickName() + "的红包");

        if(redPackageHeadBean.getRedType() == 203){
            tv_content.setText("游戏内容：" + redPackageHeadBean.getSendWord());
        }else{
            tv_content.setText(redPackageHeadBean.getSendWord());
        }

        if (!TextUtils.isEmpty(state) && !TextUtils.equals(state,"4")){
            if (redPackageHeadBean.getReceiveMoney() > 0){
                ll_money.setVisibility(View.VISIBLE);
                tv_content_money.setText(redPackageHeadBean.getReceiveMoney() + "");
            }else {
                ll_money.setVisibility(View.INVISIBLE);
            }
        } else {
            ll_money.setVisibility(View.VISIBLE);
            tv_content_money.setText(redPackageHeadBean.getMoney() + "");
        }


        size = String.valueOf(redPackageHeadBean.getPacketSize());
        if (redPackageHeadBean.getIsReceive() == 1) {
            if (redPackageHeadBean.getState() == 1){
                //已领取5/10，红包总金额1000
                tv_details.setText("已领取" + redPackageHeadBean.getReceiveSize() + "/" + redPackageHeadBean.getPacketSize()
                        + ",红包总金额" + redPackageHeadBean.getMoney());
            }else if (redPackageHeadBean.getState() == 2){
                //10个红包，已被抢完，红包总金额1000
                tv_details.setText(redPackageHeadBean.getPacketSize() + "个红包，已被抢完，红包总金额" + redPackageHeadBean.getMoney());
            }
        }else {
            if (redPackageHeadBean.getState() == 1){
                //已领取5/10，红包总金额1000
                tv_details.setText("已领取" + redPackageHeadBean.getReceiveSize() + "/" + redPackageHeadBean.getPacketSize()
                        + ",红包总金额" + redPackageHeadBean.getMoney());
            }else if (redPackageHeadBean.getState() == 2){
                //10个红包，已被抢完，红包总金额1000
                tv_details.setText(redPackageHeadBean.getPacketSize() + "个红包，已被抢完，红包总金额" + redPackageHeadBean.getMoney());
            }else if (redPackageHeadBean.getState() == 3){//过期
                tv_details.setText("已领取" + redPackageHeadBean.getReceiveSize() + "/" + redPackageHeadBean.getPacketSize()
                        + ",红包总金额" + redPackageHeadBean.getMoney());
            }
        }
    }

    @OnClick(R.id.iv_title_back)
    void OnClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
