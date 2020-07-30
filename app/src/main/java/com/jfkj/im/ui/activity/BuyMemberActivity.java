package com.jfkj.im.ui.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.CzModeListBean;
import com.jfkj.im.Bean.VipListBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.BannerAdapter;
import com.jfkj.im.banner.BannerLayoutManager;
import com.jfkj.im.banner.BannerPageSnapHelper;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.Paydialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.RecyclerViewPageChangeListenerHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;

public class BuyMemberActivity extends BaseActivity implements Paydialog.OnClickOutListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_1)
    TextView tv_1;


    private int rvPosition = 0;


    private VipListBean vipListBeans;
    private BannerAdapter bannerAdapter;
    private List<VipListBean> datas = new ArrayList<>();
    private Paydialog paydialog;
    private ArrayList<String> useridList;
    private int vipType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(tv_1);
        initData();
        getVip();

        useridList = getIntent().getStringArrayListExtra("useridList");


        SPUtils.getInstance(BuyMemberActivity.this).put("vipType", "");

        paydialog = new Paydialog(this,this,1);


    }



    @OnClick({R.id.tv_open_member})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_open_member:

                //rvPosition


                //开通会员  1.获取支付方式
                if (recycler != null && recycler.getChildCount() > 0) {
                    try {
                        account();
//                        int currentPosition = ((RecyclerView.LayoutParams) recycler.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
//                        OkLogger.e("" + currentPosition);
                        paydialog.setMoney(vipListBeans.getData().get(rvPosition).getIexchangemoney() + "");

                        int vipType = vipListBeans.getData().get(rvPosition).getVipType();

                        SPUtils.getInstance(BuyMemberActivity.this).put("vipType",vipType+ "");

                    } catch (Exception e) {
                    }
                }
                break;
        }
    }




    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserInfo(EventMessage message) {

        if (message.getType() == REFRUSH_USER_BALANCE) {
            //买卡成功
              String type = SPUtils.getInstance(BuyMemberActivity.this).getString("vipType","");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        // 1银卡
                        if(type.equals("1") ){
                            //未提交审核
                            Intent uploadpictures_code = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                            uploadpictures_code.putExtra(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));
                            startActivity(uploadpictures_code);
                        }else if (type.equals("2")  || type.equals("3")){
                            // 2 金卡  3 黑卡
                            Intent intent = new Intent(App.getAppContext(), MainActivity.class).putStringArrayListExtra("useridList",useridList);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            ApiClient.onDestroy();

                        }
                    }


                }
            }).start();



        }
    }








    private void account(){

        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.CFROM,Utils.ANDROID);


        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/my/czModeList")
                    .params(map)
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY+AppUtils.getReqTime()))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String s, int id) {
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                if(jsonObject.getString("code").equals("10003")){

                                    CzModeListBean czModeListBean= JSON.parseObject(s,CzModeListBean.class);
                                    paydialog.setCzModeListBean(czModeListBean,BuyMemberActivity.this);
                                    paydialog.setMoney(vipListBeans.getData().get(rvPosition).getIexchangemoney() + "");
                                    paydialog.setExchangeId(vipListBeans.getData().get(rvPosition).getCexchangeid());
                                    paydialog.show();




                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            toastShow(R.string.nonetwork);
        }
    }




    private void getVip() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/user/getVipCardList")
                    .params(map)
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    vipListBeans = JSON.parseObject(response,VipListBean.class);
                                    bannerAdapter.setData(vipListBeans);
                                    bannerAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            toastShow(R.string.nonetwork);
        }
    }

    private void initData() {
        bannerAdapter = new BannerAdapter(vipListBeans, this);
        BannerLayoutManager layoutManager2 = new BannerLayoutManager();
        bannerAdapter.setItemWidth(1f);
        bannerAdapter.setRatio(1f);
        recycler.setAdapter(bannerAdapter);
        recycler.setLayoutManager(layoutManager2);

        BannerPageSnapHelper bannerPageSnapHelper2 = new BannerPageSnapHelper();
        bannerPageSnapHelper2.setInfinite(true);
        bannerPageSnapHelper2.attachToRecyclerView(recycler);
        recycler.addOnScrollListener(new RecyclerViewPageChangeListenerHelper(bannerPageSnapHelper2, new RecyclerViewPageChangeListenerHelper.OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onPageSelected(int position) {
                rvPosition = position;
            }
        }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_member;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onPlay(String playId) {
        //play

//        vipType = vipListBeans.getData().get(rvPosition).getVipType();


       // String vipType = SPUtils.getInstance(BuyMemberActivity.this).getString("vipType", "");


        // 1银卡
        if(this.vipType ==1 ){
            //未提交审核
            Intent uploadpictures_code = new Intent(App.getAppContext(), UploadpicturesActivity.class);
            uploadpictures_code.putExtra(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));
            startActivity(uploadpictures_code);
        }else{
            // 2 金卡  3 黑卡
            Intent intent = new Intent(App.getAppContext(), MainActivity.class).putStringArrayListExtra("useridList",useridList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ApiClient.onDestroy();

        }

    }



}
