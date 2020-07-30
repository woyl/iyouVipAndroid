package com.jfkj.im.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CzModeListBean;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.ChargeAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.okhttp.LoginException;
import com.jfkj.im.payments.alipay.PayResult;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.ui.mine.VIPActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.widget.GridItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;

/**
 * <pre>
 * Description:  充值dialog
 * @author :   ys
 * @date :         2019/12/21
 * </pre>   //这里聊天界面的充值
 */
public class ChargeDialog extends BottomSheetDialog implements View.OnClickListener {
    TextView mTvBalanceNumber;
    TextView mTvVip;
    RecyclerView mRecycler;
    TextView mTvLevelUpMoney;
    private Context mContext;
    private ExChangeBean bean = null;
    private ChargeAdapter adapter = null;
    Paydialog paydialog;
    private int SDK_PAY_FLAG = 0x11;

    private Activity activity;

    public ChargeDialog(@NonNull Context context, Activity activity) {
        super(context,R.style.BottomSheetDialog);
        this.mContext = context;
        this.activity = activity;
        createView();


    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {


            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            switch (payResult.getResultStatus()) {
                case "9000":

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




        };
    };

    private void createView() {
        View bottomView = LayoutInflater.from(mContext).inflate(R.layout.dialog_charge, null);
        setContentView(bottomView);
        paydialog = new Paydialog(mContext,activity,0);

        mRecycler = bottomView.findViewById(R.id.recycler);
        mTvBalanceNumber = bottomView.findViewById(R.id.tv_balance_number);
        mTvVip = bottomView.findViewById(R.id.tv_vip);
        mTvVip.setOnClickListener(this);
        mTvLevelUpMoney = bottomView.findViewById(R.id.tv_level_up_money);
        mTvBalanceNumber.setText(UserInfoManger.getUserBanlance());
        mTvLevelUpMoney.setText(UserInfoManger.getUpgradeAmount() + "元");
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRecycler.setLayoutManager(layoutManager);
        GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtils.dip2px(mContext, 15), 3);
        mRecycler.addItemDecoration(itemDecoration);
        adapter = new ChargeAdapter(mContext);
        mRecycler.setAdapter(adapter);
        mRecycler.setHasFixedSize(false);

        initData();

    }

    private void initData() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        hashMap.put(Utils.OSNAME, Utils.ANDROID);
        hashMap.put(Utils.CHANNEL, Utils.ANDROID);
        hashMap.put(Utils.DEVICENAME, Utils.getdeviceName());
        hashMap.put(Utils.DEVICEID, Utils.ANDROID);
        hashMap.put(Utils.REQTIME, AppUtils.getReqTime());
        hashMap.put(Utils.CFROM, Utils.ANDROID);
        OkHttpUtils
                .post()
                .tag(mContext)
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .url(ApiStores.base_url + "/my/czModeList")
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("10003")) {
                                CzModeListBean czModeListBean = JSON.parseObject(response, CzModeListBean.class);
                                paydialog.setCzModeListBean(czModeListBean, new Paydialog.OnClickOutListener() {
                                    @Override
                                    public void onPlay(String playId) {
                                        Map<String, String> maps = new HashMap<>();
                                        maps.put("payWayId", playId);
                                        maps.put("money", paydialog.getMoney());
                                        maps.put("exchangeId", paydialog.getExchangeId());
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

                                                    private void playAlipay(AddMoneyBean bean) {
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

                                                    private void playWeChat(AddMoneyBean bean) {
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


                                                });
                                    }
                                });

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });




        /**
         *  钻石兑换汇率
         * @param IRHZ  1：人民币充值钻石   2：金币兑换钻石
         */

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, AppUtils.getIMEI(mContext));
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("IRHZ", "1");
        ApiClient.getmRetrofit().create(ApiStores.class)
                .exchangeList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<ExChangeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ExChangeBean exChangeBean) {
                        adapter.setBean(exChangeBean);
                        bean = exChangeBean;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        if (adapter != null) {
            adapter.setClick(new ChargeAdapter.onClick() {
                @Override
                public void onclickitem(View view, int positon) {
                    ExChangeBean.DataBean dataBean = bean.getData().get(positon);
                    paydialog.setMoney(dataBean.getIexchangemoney() + "");
                    paydialog.setExchangeId(dataBean.getCexchangeid());
                    paydialog.setImoney(dataBean.getImoney()+"");
                    paydialog.show();
                    dismiss();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        JumpUtil.overlay(mContext, VIPActivity.class);
    }

    public void updatevelep(String vipLevel) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_LEVEL, Integer.parseInt(vipLevel));
        TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {


            }

            @Override
            public void onSuccess() {

            }
        });
    }
}
