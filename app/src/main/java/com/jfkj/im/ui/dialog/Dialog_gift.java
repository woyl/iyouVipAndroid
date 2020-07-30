package com.jfkj.im.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.jfkj.im.Bean.GiftchatBean;
import com.jfkj.im.Bean.VipPrivilegeBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.game.GameListBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.ChatGiftAdapter;
import com.jfkj.im.adapter.GridPagerAdapter;
import com.jfkj.im.adapter.chat.GiftAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.mine.VIPActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videocompression.Util;
import com.jfkj.im.widget.GridItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

public class Dialog_gift extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private ViewPager viewPager;
    private CircleIndicator mIndicator;
    private TextView tvBalanceNum;
    private TextView tvCharge;
    private TextView tv_add_hint;
    private List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
    private GridPagerAdapter mPagerAdapter;
    private ArrayList<RecyclerView> mListView = new ArrayList<>();
    //总共有多少页
    private int totalPageSize;
    private int spanSize = 4;
    private int rowNum = 2;
    private int singlePageSize = rowNum * spanSize;
    private List<GiftData.DataBean.ArrayBean> itemData = null;
    private int truePosition;
    private BottomSheetBehavior<View> mBehavior;
    private String userId;
    private ChargeDialog chargeDialog;
    private String name;
    private boolean isfirend=false;
    private LinearLayout layout;
    private TextView tv_vip_level;
    private TextView tv_remaining_money;
    private ProgressBar progressBar;

    private TextView tv_vip_privilege;


    public Dialog_gift(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public Dialog_gift(@NonNull Activity context, int theme, List<GiftData.DataBean.ArrayBean> list,String name) {
        super(context, theme);
        this.mContext = context;
        this.name=name;
        this.giftList = list;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gift_bottom);
        viewPager = findViewById(R.id.viewPager);
        mIndicator = findViewById(R.id.layout_indicator);
        tvBalanceNum = findViewById(R.id.tv_balance_number);
        tvCharge = findViewById(R.id.tv_charge);
        tv_add_hint=findViewById(R.id.tv_add_hint);
        layout = (LinearLayout) findViewById(R.id.ll_layout);

        tv_vip_level = findViewById(R.id.tv_vip_level);
        tv_remaining_money = findViewById(R.id.tv_remaining_money);
        progressBar = findViewById(R.id.progress_bar_red);
        tv_vip_privilege = (TextView) findViewById(R.id.tv_vip_privilege);





        findViewById(R.id.tv_this_level);


        tvBalanceNum.setText(UserInfoManger.getUserBanlance());
        assert tvCharge != null;
        tvCharge.setOnClickListener(this);
        totalPageSize = giftList.size() / (spanSize * 2);
        if (giftList.size() % (spanSize * 2) > 0) {
            totalPageSize++;
        }
        mIndicator.createIndicators(totalPageSize,0);
        initGridView(rowNum, spanSize);
    }


    public void setList(List<GiftData.DataBean.ArrayBean> data) {
        this.giftList = data;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isOutOfBounds(mContext,event)){
            String s = SPUtils.getInstance(mContext).getString(Utils.GETGIFTLIST);
            GiftchatBean giftchatBean = JSON.parseObject(s, GiftchatBean.class);

            List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
            for (int i = 0 ; i < giftchatBean.getData().getArray().size(); i++){
                giftchatBean.getData().getArray().get(i).setSelect(false);

                GiftData.DataBean.ArrayBean bean = new GiftData.DataBean.ArrayBean(
                        giftchatBean.getData().getArray().get(i).getGiftId(),
                        giftchatBean.getData().getArray().get(i).getTop(),
                        giftchatBean.getData().getArray().get(i).getPrice(),
                        giftchatBean.getData().getArray().get(i).getPictureUrl(),
                        giftchatBean.getData().getArray().get(i).getName(),
                        giftchatBean.getData().getArray().get(i).getState(),
                        giftchatBean.getData().getArray().get(i).isSelect());
                giftList.add(bean);
            }
            String json = new Gson().toJson(giftchatBean);
            SPUtils.getInstance(mContext).put(Utils.GETGIFTLIST, json);

            Utils.giftList.clear();
            Utils.giftList.addAll(giftList);
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();//相对弹窗左上角的x坐标
        final int y = (int) event.getY();//相对弹窗左上角的y坐标
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();//最小识别距离
        final View decorView = Objects.requireNonNull(getWindow()).getDecorView();//弹窗的根View
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    private void initGridView(int rowNum, int spanNum) {
        for (int i = 0; i < totalPageSize; i++) {
            RecyclerView recyclerView = new RecyclerView(mContext);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
            recyclerView.setLayoutManager(layoutManager);
//
////            ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//            recyclerView.setItemAnimator(null);
////            recyclerView.getItemAnimator().setChangeDuration(0);

            int fromIndex = i * singlePageSize;
            int toIndex = (i + 1) * singlePageSize;
            if (toIndex > giftList.size()) {
                toIndex = giftList.size();
            }
            truePosition = i * singlePageSize;
            //分隔开没个页面的数据
            itemData = new ArrayList<>(giftList.subList(fromIndex, toIndex));

            ChatGiftAdapter adapter = new ChatGiftAdapter(mContext, itemData,userId,name);
            GridItemDecoration itemDecoration = new GridItemDecoration(30, spanNum);
            recyclerView.addItemDecoration(itemDecoration);


            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setListener(new onItemPositionClickListener() {
                @Override
                public void onPositionClick(int position) {
                    dismiss();
                }
            });
            mListView.add(recyclerView);
        }

        mPagerAdapter = new GridPagerAdapter(mListView);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(totalPageSize - 1);
        mIndicator.setViewPager(viewPager);
    }

    /**
     *
     */
    private void initViewPager() {

    }

    @Override
    public void onClick(View v) {
        dismiss();
        chargeDialog = new ChargeDialog(mContext,mContext);
        chargeDialog.show();
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//设置布局属性占满宽度

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ;//设置布局属性适应高度
        params.gravity = Gravity.BOTTOM;//设置dialog位于底部
        window.setAttributes(params);

        if(isfirend){
            layout.setVisibility(View.VISIBLE);
            tv_add_hint.setVisibility(View.GONE);


            tv_vip_privilege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpUtil.overlay(mContext, VIPActivity.class);
                }
            });




            HashMap<String, String> map = new HashMap<>();
            map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
            map.put(Utils.OSNAME, Utils.ANDROID);
            map.put(Utils.CHANNEL, Utils.ANDROID);
            map.put(Utils.DEVICENAME, Utils.getdeviceName());
            map.put(Utils.DEVICEID, "1");
            map.put(Utils.REQ_TIME, AppUtils.getReqTime());

            OkHttpUtils.post()
                    .tag(mContext)
                    .url(ApiStores.base_url + "/my/vipPrivilege")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(mContext).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    VipPrivilegeBean vipPrivilegeBean = JSON.parseObject(response, VipPrivilegeBean.class);

                                    tv_vip_level.setText("VIP"+vipPrivilegeBean.getData().getVipLevel());
                                    tv_remaining_money.setText("您距离"+ (vipPrivilegeBean.getData().getVipLevel() + 1) +"级还需要充值"+vipPrivilegeBean.getData().getUpgradeAmountNext()+"元");




                                    double scale = (vipPrivilegeBean.getData().getUpgradeamount() - vipPrivilegeBean.getData().getUpgradeAmountNext() )/ vipPrivilegeBean.getData().getUpgradeamount();


                                    int IScale = new Double((scale * 100)).intValue();

                                    progressBar.setProgress(IScale);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    public void isfirend(boolean isfirend) {
        this.isfirend=isfirend;
    }
}
