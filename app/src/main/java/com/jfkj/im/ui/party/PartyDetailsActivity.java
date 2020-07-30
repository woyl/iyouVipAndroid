package com.jfkj.im.ui.party;

import butterknife.BindView;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.party.PartyDetailsPresenter;
import com.jfkj.im.mvp.party.PartyDetailsView;

/**
 * 聚會詳情
 */
public class PartyDetailsActivity extends BaseActivity<PartyDetailsPresenter> implements PartyDetailsView {

    private PartyDetailsPresenter partyDetailsPresenter;

    @BindView(R.id.iv_head)
    ImageView iv_head;

    @BindView(R.id.tv_nike_name)
    TextView tvNikeName;


    @BindView(R.id.tv_title_message)
    TextView tvTtitle;


    @BindView(R.id.tv_welfare)
    TextView tvWelfare;

    @BindView(R.id.tv_diamonds)
    TextView tvDiamonds;

    @BindView(R.id.tv_place)
    TextView tvPlace;

    @BindView(R.id.tv_cadddate)
    TextView tvCadddate;

    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;

    @BindView(R.id.tv_max_number)
    TextView tvMaxNumber;

    @BindView(R.id.tv_sign_count)
    TextView tvSignCount;


    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @BindView(R.id.iv_brg)
    ImageView iv_brg;

    @BindView(R.id.tv_vip_level)
    TextView tv_vip_level;
    private int type;
    private String partyId;
    private String userid;

    private  String detailsphoto;


    @OnClick({R.id.tv_submit, R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

            case R.id.tv_submit:
                //	1 报名参加 2名额已满 3已报名 4已取消 5应邀用户

                if (type == 1) {

                    Intent intent = new Intent(this, EnrollActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("partyId", partyId);
                    bundle.putString("title", tvTtitle.getText().toString());
                    bundle.putString("cadddate", tvCadddate.getText().toString());
                    bundle.putString("place", tvPlace.getText().toString());
                    bundle.putString("user_id", userid);
                    bundle.putString("detailsphoto",detailsphoto);
                    bundle.putString("nick_name",tvNikeName.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,5);
                } else if (type == 5) {

                    Intent intent = new Intent(this, SignInActivity.class);
                    intent.putExtra("partyId", partyId);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5){
            if (resultCode == 10){
                tvSubmit.setEnabled(false);
                tvSubmit.setText("已报名");
                tvSubmit.setTextColor(ContextCompat.getColor(mActivity,R.color.c333333));
                tvSubmit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void initDate(PartyInfoBean partyInfoBean) {

        //VIP等级
//        tv_vip_level.setText(UserInfoManger.getUserVipLevel());

        Glide.with(this).load(partyInfoBean.getData().getDetailsphoto()).error(R.drawable.icon_party_detail_brg).into(iv_brg);

        detailsphoto = partyInfoBean.getData().getDetailsphoto();

        userid = partyInfoBean.getData().getUserId();

        Glide.with(this).load(partyInfoBean.getData().getHead()).transform(new CircleCrop()).into(iv_head);
        tvNikeName.setText(partyInfoBean.getData().getNickName());

        tvTtitle.setText(partyInfoBean.getData().getTitle());

        //0:不提供 1:提供机票+住宿
        if (partyInfoBean.getData().getWelfare() == 0) {

            tvWelfare.setText("不提供机票+住宿");
        } else {

            tvWelfare.setText("提供机票+住宿");
        }


        tvDiamonds.setText("X" + partyInfoBean.getData().getDiamonds());

        tvPlace.setText(partyInfoBean.getData().getPlace());

        tvCadddate.setText(partyInfoBean.getData().getStringDate());

        tvIntroduce.setText(partyInfoBean.getData().getIntroduce());

        tv_vip_level.setText("VIP" + partyInfoBean.getData().getVipLevel() + "");


        String htmlStr = "<font color=\"#ffffff\">限定名额</font>" + "<font color=\"#EF4769\" >" + partyInfoBean.getData().getMaxNumber() + "</font>" + "<font color=\"#ffffff\">人</font>";

        tvMaxNumber.setText(Html.fromHtml(htmlStr));

        String htmlSignStr = "<font color=\"#ffffff\">累计报名</font>" + "<font color=\"#EF4769\" >" + partyInfoBean.getData().getSignCount() + "</font>" + "<font color=\"#ffffff\">人</font>";


        tvSignCount.setText(Html.fromHtml(htmlSignStr));


        type = partyInfoBean.getData().getType();
        //	1 报名参加 2名额已满 3已报名 4已取消 5应邀用户
        if (type == 1) {
            tvSubmit.setText("报名参加");
            tvSubmit.setBackgroundResource(R.drawable.shap_bt_two_bg);
        } else if (type == 2) {
            tvSubmit.setText("名额已满");
            tvSubmit.setTextColor(ContextCompat.getColor(mActivity,R.color.c333333));
            tvSubmit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
        } else if (type == 3) {
            tvSubmit.setText("已报名");
            tvSubmit.setTextColor(ContextCompat.getColor(mActivity,R.color.c333333));
            tvSubmit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
        } else if (type == 4) {
            tvSubmit.setText("已取消");
            tvSubmit.setTextColor(ContextCompat.getColor(mActivity,R.color.c333333));
            tvSubmit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
        } else if (type == 5) {
            tvSubmit.setText("应邀用户");
            tvSubmit.setBackgroundResource(R.drawable.shap_bt_two_bg);
        } else if (type == 6) {
            tvSubmit.setText("已结束");
            tvSubmit.setBackgroundResource(R.drawable.shape_bg_ten_huise);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyDetailsPresenter = new PartyDetailsPresenter(this);


        Intent intent = getIntent();
        partyId = intent.getStringExtra("partyId");


        partyDetailsPresenter.getPartyDetails(partyId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party_details;
    }

    @Override
    public PartyDetailsPresenter createPresenter() {
        return partyDetailsPresenter;
    }

    @Override
    public void getPartyDetailsSuccess(PartyInfoBean partyInfoBean) {
        try {
            initDate(partyInfoBean);
        } catch (Exception e) {
        }
    }


    @Override
    public void showLoading() {
        //  progressDialog.show();
    }

    @Override
    public void hideLoading() {
        //progressDialog.dismiss();
    }
}
