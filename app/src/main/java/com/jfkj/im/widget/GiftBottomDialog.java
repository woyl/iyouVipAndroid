package com.jfkj.im.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.GridPagerAdapter;
import com.jfkj.im.adapter.chat.GiftAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/9
 * </pre>
 */
public class GiftBottomDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ViewPager viewPager;
    private CircleIndicator mIndicator;
    private TextView tvBalanceNum;
    private TextView tvCharge;
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
    private ExChangeBean mExChangeBean = null;
    Activity activity;
    public void setName(String name) {
        this.name = name;
    }

    public GiftBottomDialog(@NonNull Context context, int theme, List<GiftData.DataBean.ArrayBean> list, Activity activity) {
        super(context, theme);
        this.mContext = context;
        this.giftList = list;
        this.activity=activity;
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
        tvBalanceNum.setText(UserInfoManger.getUserBanlance());
        assert tvCharge != null;
        tvCharge.setOnClickListener(this);
        totalPageSize = giftList.size() / (spanSize * 2);
        if (giftList.size() % (spanSize * 2) > 0) {
            totalPageSize++;
        }
        mIndicator.createIndicators(totalPageSize,0);
        initGridView(rowNum, spanSize);
//        getChargeList();
    }

    private void getChargeList() {
        /**
         *  钻石兑换汇率
         * @param IRHZ  1：人民币充值钻石   2：金币兑换钻石
         */

        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, AppUtils.getBRAND());
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
                    public void onNext(ExChangeBean bean) {
                        mExChangeBean = bean;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void setList(List<GiftData.DataBean.ArrayBean> data) {
        this.giftList = data;
    }

    private void initGridView(int rowNum, int spanNum) {
        for (int i = 0; i < totalPageSize; i++) {
            RecyclerView recyclerView = new RecyclerView(mContext);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
            recyclerView.setLayoutManager(layoutManager);
            int fromIndex = i * singlePageSize;
            int toIndex = (i + 1) * singlePageSize;
            if (toIndex > giftList.size()) {
                toIndex = giftList.size();
            }
            truePosition = i * singlePageSize;
            //分隔开没个页面的数据
            itemData = new ArrayList<>(giftList.subList(fromIndex, toIndex));
            GiftAdapter adapter = new GiftAdapter(mContext, itemData,userId);
            adapter.setName(name);
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

        chargeDialog = new ChargeDialog(mContext,activity);
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
        //        params.height = (int) (heightPixels * 0.5f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ;//设置布局属性适应高度
        params.gravity = Gravity.BOTTOM;//设置dialog位于底部
        window.setAttributes(params);
    }

}
