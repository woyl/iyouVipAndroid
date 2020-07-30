package com.jfkj.im.ui.activity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.LoadRedPacketDetailsBean;
import com.jfkj.im.Bean.loadRedPacketReceiveListBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.RedPacketDetailsAdapter;
import com.jfkj.im.mvp.RedPacketDetails.RedPacketDetailsPresenter;
import com.jfkj.im.mvp.RedPacketDetails.RedPacketDetailsView;
import com.jfkj.im.mvp.SpecialBaseActivity;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RedPacketDetailsActivity extends SpecialBaseActivity<RedPacketDetailsPresenter> implements RedPacketDetailsView, View.OnClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    RedPacketDetailsPresenter redPacketDetailsPresenter;
    Intent getintent;
    RedPacketDetailsAdapter redpacketdetailsadapter;
    List<loadRedPacketReceiveListBean.DataBean.ArrayBean> beanList;
    View head_view, head_activity_redpacketdetails1;
    ImageView back_iv;
    CircleImageView default_head_iv;
    TextView tv_name;
    TextView tv_hint;
    TextView tv_money;
    TextView tv_readdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        head_view = LayoutInflater.from(mActivity).inflate(R.layout.head_activity_redpacketdetails, null);
        head_activity_redpacketdetails1 = LayoutInflater.from(mActivity).inflate(R.layout.head_activity_redpacketdetails1, null);
        default_head_iv = head_view.findViewById(R.id.default_head_iv);
        back_iv = head_view.findViewById(R.id.back_iv);
        tv_readdetail = head_activity_redpacketdetails1.findViewById(R.id.tv_readdetail);
        tv_name = head_view.findViewById(R.id.tv_name);
        tv_hint = head_view.findViewById(R.id.tv_hint);
        tv_money = head_view.findViewById(R.id.tv_money);
        back_iv.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        redPacketDetailsPresenter = new RedPacketDetailsPresenter(this);
        getintent = getIntent();
        redPacketDetailsPresenter.loadRedPacketDetails(getintent.getStringExtra(Utils.GROUPID));
        redPacketDetailsPresenter.loadRedPacketReceiveList(getintent.getStringExtra(Utils.GROUPID), "");
        Glide.with(mActivity).load(getintent.getStringExtra(Utils.HEAD_URL)).into(default_head_iv);
        beanList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullScreen(mActivity);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_redpacketdetails;
    }

    @Override
    public RedPacketDetailsPresenter createPresenter() {
        return redPacketDetailsPresenter;
    }


    @Override
    public void loadRedPacketDetails(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("200")) {
                LoadRedPacketDetailsBean loadRedPacketDetailsBean = JSON.parseObject(s, LoadRedPacketDetailsBean.class);
                LoadRedPacketDetailsBean.DataBean loadRedPacketDetailsBeanData = loadRedPacketDetailsBean.getData();
                tv_readdetail.setText("已领取" + loadRedPacketDetailsBeanData.getReceiveSize() + "/" + loadRedPacketDetailsBeanData.getPacketSize() + ",红包总金额" + loadRedPacketDetailsBeanData.getMoney());
                tv_hint.setText(loadRedPacketDetailsBeanData.getSendWord());
                tv_money.setText(loadRedPacketDetailsBeanData.getMoney() + "");
                Glide.with(mActivity).load(loadRedPacketDetailsBean.getData().getUserHead()).into(default_head_iv);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadRedPacketReceiveList(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("200")) {
                loadRedPacketReceiveListBean loadRedPacketReceiveListBean = JSON.parseObject(s, loadRedPacketReceiveListBean.class);
                List<loadRedPacketReceiveListBean.DataBean.ArrayBean> array = loadRedPacketReceiveListBean.getData().getArray();
                if (array.size() > 0) {
                    beanList.addAll(array);
                    redPacketDetailsPresenter.loadRedPacketReceiveList(getintent.getStringExtra(Utils.GROUPID), array.get(array.size() - 1).getSort());
                }
                if (array.size() == 0) {
                    redpacketdetailsadapter = new RedPacketDetailsAdapter(beanList);
                    recyclerView.setAdapter(redpacketdetailsadapter);
                    redpacketdetailsadapter.addHeaderView(head_view, 0);
                    redpacketdetailsadapter.addHeaderView(head_activity_redpacketdetails1, 1);

                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
