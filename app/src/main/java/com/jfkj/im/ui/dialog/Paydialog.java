package com.jfkj.im.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.App;
import com.jfkj.im.Bean.BuyMemberBean;
import com.jfkj.im.Bean.CzModeListBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.adapter.PayAdapter;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.payments.alipay.PayResult;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;

public class Paydialog extends BottomSheetDialog implements OnItemClickListener {
    SwipeRecyclerView swipeRecyclerView;
    private Context mContext;
    PayAdapter payAdapter;
    CzModeListBean czModeListBean;
    String money;
    ImageView iv_close;
    String exchangeId;
    TextView tv_money, tv_get_money, tv_recharge;
    private int SDK_PAY_FLAG = 0x11;
    Activity activity;
    String imoney;

    int type;
    public String getImoney() {
        return imoney;
    }

    public void setImoney(String imoney) {
        this.imoney = imoney;
        tv_get_money.setText("可获得"+imoney);
        tv_get_money.setVisibility(View.VISIBLE);
        tv_recharge.setText("充值");
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;

    }

    OnClickOutListener listener;

    public void setMoney(String money) {
        this.money = money;
        tv_money.setText("¥"+money);
    }

    public String getMoney() {
        return money;
    }

    public void setCzModeListBean(CzModeListBean czModeListBean, OnClickOutListener listener) {
        this.czModeListBean = czModeListBean;
        payAdapter.setCzModeListBean(czModeListBean, listener);
        this.listener = listener;
        payAdapter.notifyDataSetChanged();
    }

    public Paydialog(@NonNull Context context, Activity activity,int type) {
        super(context,R.style.BottomSheetDialog);
        this.mContext = context;
        this.activity=activity;
        this.type = type;
        createView();
    }

    public Paydialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        createView();
    }

    private void createView() {
        View bottomView = LayoutInflater.from(mContext).inflate(R.layout.dialog_pay, null);
        setContentView(bottomView);
        swipeRecyclerView = bottomView.findViewById(R.id.swiperecyclerview);

        TextView tvMoney = bottomView.findViewById(R.id.tv_money);
        tvMoney.setText(money);
        iv_close = bottomView.findViewById(R.id.iv_close);
        tv_recharge = bottomView.findViewById(R.id.tv_recharge);
        tv_money = bottomView.findViewById(R.id.tv_money);
        tv_get_money = bottomView.findViewById(R.id.tv_get_money);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        payAdapter = new PayAdapter(App.getAppContext());
        swipeRecyclerView.setOnItemClickListener(this);
        swipeRecyclerView.setAdapter(payAdapter);
        payAdapter.setCzModeListBean(czModeListBean, listener);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(type == 0){
                     recharge();
                }else if(type == 1 ){
                    getVipCard();
                }


            }
        });
    }



    public void getVipCard(){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("money",money);
        map.put("payWayId",dataBean.getIwayid());
        map.put("cfrom","az");
        map.put("exchangeId",exchangeId);


            OkHttpUtils.post()
                    .tag(activity)
                    .url(ApiStores.base_url + "/my/getVipCard")
                    .params(map)
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(activity).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String s, int id) {
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                if(jsonObject.getString("code").equals("200")){
                                    AddMoneyBean bean = JSON.parseObject(s, AddMoneyBean.class);

                                    if(bean.getData().getPaymentMethod().equals("1")){
                                        //wechat
                                        playWeChat(bean);
                                    }else if (bean.getData().getPaymentMethod().equals("2")){
                                        playAlipay(bean);
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });





    }


    private void recharge() {
        if(dataBean==null){
            ToastUtil.toastShortMessage("请选择支付方式");
            return;
        }
        Map<String, String> maps = new HashMap<>();
        maps.put("payWayId", dataBean.getIwayid());
        maps.put("money", money);
        maps.put("exchangeId",exchangeId);
        maps.put("cfrom", "az");
        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.OSNAME, Utils.ANDROID);
        maps.put(Utils.CHANNEL, Utils.ANDROID);
        maps.put(Utils.DEVICENAME, Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());
        maps.put(Utils.CFROM, Utils.ANDROID);
        OkHttpUtils
                .post()
                .tag(mContext)
                .url(ApiStores.base_url + "/my/addMoney")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(maps)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        OkLogger.e(e.toString());
                        dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            dismiss();

                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("code").equals("200")) {
                                OkLogger.e(response);
                                AddMoneyBean bean = JSON.parseObject(response, AddMoneyBean.class);

                                if(bean.getData().getPaymentMethod().equals("1")){
                                    //wechat
                                    playWeChat(bean);
                                }else if (bean.getData().getPaymentMethod().equals("2")){
                                    playAlipay(bean);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
    }

    CzModeListBean.DataBean dataBean;


    //微信支付
    public void playWeChat(AddMoneyBean bean){

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, null);

        // 将该app注册到微信

        msgApi.registerApp(bean.getData().getAppid());
        PayReq request = new PayReq();
        request.appId = bean.getData().getAppid();
        request.partnerId = bean.getData().getPartnerid();
        request.prepayId= bean.getData().getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr= bean.getData().getNoncestr();
        request.timeStamp= bean.getData().getTimestamp();
        request.sign= bean.getData().getSign();
        msgApi.sendReq(request);


    }

    public void playAlipay(AddMoneyBean bean){
        // ToastUtils.showShortToast(bean.getMessage());
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(bean.getData().getPreInfo(), true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        tv_recharge.setAlpha(1);
        List<CzModeListBean.DataBean> data = czModeListBean.getData();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIsselected(false);
        }
        data.get(adapterPosition).setIsselected(true);
        dataBean = data.get(adapterPosition);
        payAdapter.notifyDataSetChanged();


    }


    public interface OnClickOutListener {
        void onPlay(String playId);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            switch (payResult.getResultStatus()) {
                case "9000":

                  //  listener.onPlay("");
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(REFRUSH_USER_BALANCE);
                    eventMessage.setMessage("充值成功");
                    EventBus.getDefault().post(eventMessage);

                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    break;
                case "8000":
                case "6004":
                    Toast.makeText(mContext, "正在处理中", Toast.LENGTH_SHORT).show();
                    break;
                case "4000":
                    Toast.makeText(mContext, "订单支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case "5000":
                    Toast.makeText(mContext, "重复请求", Toast.LENGTH_SHORT).show();
                    break;
                case "6001":
                    Toast.makeText(mContext, "已取消支付", Toast.LENGTH_SHORT).show();
                    break;
                case "6002":
                    Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    };
}
