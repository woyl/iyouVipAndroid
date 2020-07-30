package com.jfkj.im.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.VipData;
import com.jfkj.im.Bean.VipPrivilegeBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.adapter.VipAdapter;
import com.jfkj.im.data.DataProvide;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.WebActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * <pre>
 * Description:  vip 特权
 * @author :   ys
 * @date :         2019/12/25
 * </pre>
 */
public class VIPActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.iv_crown)
    ImageView mIvCrown;
    @BindView(R.id.tv_level)
    TextView mTvLevel;
    @BindView(R.id.tv_current_level)
    TextView mTvCurrentLevel;
    @BindView(R.id.tv_need_money)
    TextView mTvNeedMoney;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_level_detail)
    TextView mTvLevelDetail;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private VipAdapter mAdapter;
    private List<VipData> mList = new ArrayList<>();

    @BindView(R.id.recycler_introduce)
    RecyclerView recycler_introduce;
    private CommonRecyclerAdapter<String> adapter;


    @BindView(R.id.progress_bar_red)
    ProgressBar progressBar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);


        mTvTitle.setText("VIP特权");
        mTvLevel.setText("VIP" + UserInfoManger.getUserVipLevel());
        mTvMoney.setText(UserInfoManger.getUpgradeAmount()+"元");

        mList = DataProvide.getVipData();

        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new VipAdapter(this,mList,Integer.parseInt(UserInfoManger.getUserVipLevel()));
        mRecycler.setAdapter(mAdapter);
        mRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.notifyDataSetChanged();

        initDate();

        recyclerIntroduce();
    }

    private void recyclerIntroduce() {
        List<String> list = new ArrayList<>();
        String data1 = "VIP1-10级：&#160;&#160;&#160;每充值<font color = '#FF2B66'>¥1000</font>，等级提升1级";
        String data2 = "VIP11-49级：每充值<font color = '#FF2B66'>¥5000</font>，等级提升1级";
        String data3 = "VIP50-99级：每充值<font color = '#FF2B66'>¥10000</font>，等级提升1级";
        String data4 = "VIP≥100级： 每充值<font color = '#FF2B66'>¥50000</font>，等级提升1级";
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recycler_introduce.setLayoutManager(layoutManager);
        adapter = new CommonRecyclerAdapter<String>(this,list,R.layout.item_vip_introduce) {
            @Override
            public void convert(CommonRecyclerHolder holder, String s, int position) {
                View view_1 = holder.getView(R.id.view_1);
                View view_2 = holder.getView(R.id.view_2);
                AppCompatTextView tv_content = holder.getView(R.id.tv_content);
                if (position == 0) {
                    view_1.setVisibility(View.INVISIBLE);
                }
                if (position == 3){
                    view_2.setVisibility(View.INVISIBLE);
                }
                tv_content.setText(Html.fromHtml(s));
            }
        };
        recycler_introduce.setAdapter(adapter);

    }

    private void initDate() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/my/vipPrivilege")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(this).getString(Utils.TOKEN))
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

    @OnClick({R.id.iv_title_back, R.id.tv_level_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

            case R.id.tv_level_detail:
//                ToastUtils.showShortToast("等级详情");
//                JumpUtil.overlay(this,PrivilegeActivity.class);

                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("title", "VIP特权说明");
                intent.putExtra("url", ApiStores.h5BaseUrl);

                startActivity(intent);

                break;
        }
    }
}
