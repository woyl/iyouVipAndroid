package com.jfkj.im.ui.discovery;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jfkj.im.App;
import com.jfkj.im.Bean.DiscoverBean;
import com.jfkj.im.Bean.RankingBean;
import com.jfkj.im.Bean.SelectOneEndPartyBean;
import com.jfkj.im.Bean.SelectadvertisementBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.event.GuideEvent;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.ui.RankActivity;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.activity.CrushIceTaskHallActivity;
import com.jfkj.im.ui.activity.PartyActivity;
import com.jfkj.im.ui.activity.WebActivity;
import com.jfkj.im.ui.dialog.CommonCenterDialog;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.ui.party.SignInActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.GlideRoundTransform;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DiscoveryFragment extends BaseFragment implements View.OnClickListener {

    static DiscoveryFragment discoveryFragment;
//    @BindView(R.id.iv_circle)
//    ImageView mIvCircle;
    @BindView(R.id.tv_circle)
    TextView mTvCircle;
    @BindView(R.id.tv_msg_number)
    TextView mTvMsgNumber;

    @BindView(R.id.iv_user_icon)
    CircleImageView mIvUserIcon;

    @BindView(R.id.iv_rank)
    ImageView mIvRank;
    @BindView(R.id.tv_rank)
    TextView mTvRank;
    @BindView(R.id.iv_rank_go)
    ImageView mIvRankGo;
    @BindView(R.id.layout_rank)
    ConstraintLayout mLayoutRank;

    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.iv_red_icon)
    TextView iv_red_icon;

    Map<String, String> querymap = new HashMap();
    @BindView(R.id.iv_advertisement)
    AppCompatImageView iv_advertisement;
    @BindView(R.id.rl_ad)
    RelativeLayout rlAd;

    @BindView(R.id.fl1)
    ImageView fl1;
    @BindView(R.id.fl2)
    ImageView fl2;
    @BindView(R.id.tv_name1)
    TextView tv_name1;
    @BindView(R.id.tv_name2)
    TextView tv_name2;
    @BindView(R.id.tv_number1)
    TextView tv_number1;
    @BindView(R.id.tv_number2)
    TextView tv_number2;
    @BindView(R.id.fl_meili)
    FrameLayout fl_meili;
    @BindView(R.id.fl_shili)
    FrameLayout fl_shili;
    @BindView(R.id.tv_gg_title)
    TextView tvGGTitle;
    @BindView(R.id.tv_new)
    TextView tvNew;

    @BindView(R.id.iv_meili)
    ImageView ivMeili;

    @BindView(R.id.iv_shili)
    ImageView ivShili;


    private DiscoverBean discoverBean;
    private CommonCenterDialog promptDialog;


    public static DiscoveryFragment getInstance() {
        if (discoveryFragment == null) {
            discoveryFragment = new DiscoveryFragment();
        }
        return discoveryFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragament_news;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTvTitle.setText("发现");
        rlAd.setVisibility(View.GONE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("praise");
        intentFilter.addAction("dynamic");
        intentFilter.addAction("main_find");

        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME,Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, Utils.ANDROID);
        querymap.put(Utils.REQTIME, AppUtils.getReqTime());

        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        mTvMsgNumber.setText(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0"));
        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0").equals("0")) {
            mTvMsgNumber.setVisibility(View.GONE);
        } else {
            mTvMsgNumber.setVisibility(View.VISIBLE);
        }
//        String strNum = SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0");
//
//        int num = Integer.parseInt(strNum);
//
//        if(num>99){
//            mTvMsgNumber.setText("99+");
//        }else{
//            mTvMsgNumber.setText(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0"));
//        }
//        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0").equals("0")) {
//            mTvMsgNumber.setVisibility(View.GONE);
//
//        } else {
//            mTvMsgNumber.setVisibility(View.VISIBLE);
//        }
        SelectadvertisementBean selectadvertisementBean = JSON.parseObject(SPUtils.getInstance(mActivity).getString(Utils.SELECTADVERTISEMENT), SelectadvertisementBean.class);
        if (selectadvertisementBean.getData().size() > 0) {
            Glide.with(mActivity).load(selectadvertisementBean.getData().get(0).getUrl()).into(iv_advertisement);
        }
         RequestOptions options = new RequestOptions()
                       .centerCrop()
                       .placeholder(R.mipmap.icon_faxian_zhanweitu) //预加载图片
                       .error(R.mipmap.icon_faxian_zhanweitu) //加载失败图片
                        .priority(Priority.HIGH) //优先级
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                         .transform(new GlideRoundTransform(5)); //圆角
        RankingBean rankingBeanone = JSON.parseObject(SPUtils.getInstance(mActivity).getString(Utils.GETRANKING + "2"), RankingBean.class);
        RankingBean rankingBeantwo = JSON.parseObject(SPUtils.getInstance(mActivity).getString(Utils.GETRANKING + "1"), RankingBean.class);
         if(rankingBeanone.getData()!=null){
             if(rankingBeanone.getData().size() > 0  ){
                 fl_meili.setVisibility(View.VISIBLE);
                 Glide.with(mActivity).load(rankingBeanone.getData().get(0).getHead()).apply(options).into(fl1);
                 tv_name1.setText(rankingBeanone.getData().get(0).getNickname());
                 tv_number1.setText(rankingBeanone.getData().get(0).getMoney() + "");
             }else{
                 fl_meili.setVisibility(View.VISIBLE);
             }
         }else{
             fl_meili.setVisibility(View.VISIBLE);
         }
        if(rankingBeantwo.getData()!=null){
            if(rankingBeantwo.getData().size()>0){
                fl_shili.setVisibility(View.VISIBLE);
                Glide.with(mActivity).load(rankingBeantwo.getData().get(0).getHead()).apply(options).into(fl2);
                tv_name2.setText(rankingBeantwo.getData().get(0).getNickname());
                tv_number2.setText(rankingBeantwo.getData().get(0).getMoney() + "");
            }else{
                fl_shili.setVisibility(View.VISIBLE);
            }
        }else{
            fl_shili.setVisibility(View.VISIBLE);
        }
        fl1.setOnClickListener(this);
        fl2.setOnClickListener(this);
      //  iv_red_icon.setVisibility(View.GONE);

        querymap.put("apipkg", "iyou_az");
        OkGo.<String>post(ApiStores.base_url + "/base/selectAdvertisement")
                .tag(mActivity)
                .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                .headers(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkLogger.e(response.body());
                        discoverBean = GsonUtils.fromJson(response.body(), DiscoverBean.class);
                        if(discoverBean.getData().size()>0){
                            rlAd.setVisibility(View.VISIBLE);
                            Glide.with(mActivity).load(discoverBean.getData().get(0).getUrl()).into(iv_advertisement);
                        }
                        try {
                            if(discoverBean.getData().size() == 0 ){
                                rlAd.setVisibility(View.GONE);
                            }else{
                                rlAd.setVisibility(View.VISIBLE);
                                Glide.with(mActivity).load(discoverBean.getData().get(0).getUrl()).into(iv_advertisement);
                            }
                        }catch (Exception e){
                            rlAd.setVisibility(View.GONE);
                        }
                    }
                });


        Glide.with(this).load(R.mipmap.gif_meili).error(R.mipmap.vip_icon_id_secret).into(ivMeili);
        Glide.with(this).load(R.mipmap.gif_shili).error(R.mipmap.vip_icon_id_secret).into(ivShili);
    }




    private void showNoMoneyDialog(SelectOneEndPartyBean bean) {
        promptDialog = new CommonCenterDialog(getActivity(), R.style.dialogstyle, new CommonCenterDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_confirm) {
                    promptDialog.dismiss();
                    Intent intent = new Intent(getContext(), SignInActivity.class);
                    intent.putExtra("partyId",bean.getData().getPartyId());
                    startActivity(intent);
                } else if (view.getId() == R.id.tv_cancel) {
                    promptDialog.dismiss();

                }
            }
        });
        promptDialog.setTitleText("温馨提示").setContentTextHtml("你发起的见面聚会- <font color=\"#FF2B66\" >"+bean.getData().getTitle()+"</font>已结束，请对应邀用户进行评价").setConfirmBtnText("点评聚会").setCancelBtnText("取消").show();
    }

    /**
     * 点评聚会
     */
    private void  reviewParty(){
        querymap.clear();
        querymap = new HashMap<>();
        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME,Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, Utils.ANDROID);
        querymap.put(Utils.REQTIME, AppUtils.getReqTime());

        OkGo.<String>post(ApiStores.base_url + "/party/selectOneEndParty")
                .tag(this)
                .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkLogger.e(response.body());
                        try {
                            SelectOneEndPartyBean selectOneEndPartyBean = GsonUtils.fromJson(response.body(), SelectOneEndPartyBean.class);
                            if(selectOneEndPartyBean.getData().getPartyId()!=null){
                                showNoMoneyDialog(selectOneEndPartyBean);
                            }
                        }catch (Exception e){
                        }
                    }
                });
        }

    @Override
    public void onResume() {
        super.onResume();
//        int message_number = Integer.parseInt(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.MESSAGE_NUMBER, 0 + ""));
//        message_number = message_number + 1;
////        SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.MESSAGE_NUMBER, message_number + "");
//        if(message_number>99){
//            mTvMsgNumber.setText("99+");
//        }else{
//            mTvMsgNumber.setText(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0"));
//        }
//        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0").equals("0")) {
//            mTvMsgNumber.setVisibility(View.GONE);
//        } else {
//            mTvMsgNumber.setVisibility(View.VISIBLE);
//        }
        mTvMsgNumber.setText(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0"));
        if(Integer.parseInt(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0"))>99){
            mTvMsgNumber.setText("99+");
        }
        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0").equals("0")) {
            mTvMsgNumber.setVisibility(View.GONE);
        } else {
            mTvMsgNumber.setVisibility(View.VISIBLE);
        }
        if (SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.ISDYNAMIC, false)) {
           // mIvUserIcon.setVisibility(View.VISIBLE);
        //    iv_red_icon.setVisibility(View.VISIBLE);
        } else {
          //  mIvUserIcon.setVisibility(View.GONE);
        //    iv_red_icon.setVisibility(View.GONE);
        }

        //查询需要点评已结束的聚会
        reviewParty();
        //
        unreadRecord();

        //刷新红点
        allMyPartysRedType();
    }

    //R.id.iv_user_icon,
    @OnClick({R.id.iv_advertisement, R.id.tv_msg_number,  R.id.layout_circle, R.id.layout_rank,
    R.id.layout_party,R.id.layout_crush_ice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_advertisement:
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("url", discoverBean.getData().get(0).getAdverturl());
                startActivity(intent);
                break;
            case R.id.tv_msg_number:
                break;
          //  case R.id.iv_user_icon:
            //    break;
            case R.id.layout_circle:
                JumpUtil.overlay(getContext(), CircleActivity.class);
                break;
            case R.id.layout_rank:
                JumpUtil.overlay(getContext(), RankListActivity.class);
                break;
            case R.id.layout_crush_ice:
                JumpUtil.overlay(getContext(), CrushIceTaskHallActivity.class);
                break;
            case R.id.layout_party:
                Bundle bundle = new Bundle();
                bundle.putBoolean("red",SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.NEW_POINT, false));
                JumpUtil.overlay(getContext(), PartyActivity.class,bundle);
                break;
            default:
                break;
        }
    }



    @Subscribe
    public void init(String str) {

        if(str.equals("unlikeMessage")){

            //取消点赞
            int message_cut_number = Integer.parseInt(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.MESSAGE_NUMBER, 0 + ""));

            if(message_cut_number != 0){
                message_cut_number = message_cut_number -1 ;
                SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.MESSAGE_NUMBER, message_cut_number + "");

                mTvMsgNumber.setText(message_cut_number + "");

                if(message_cut_number<=0){
                    mTvMsgNumber.setVisibility(View.GONE);
                }else{
                    mTvMsgNumber.setVisibility(View.VISIBLE);
                }

            }else{
                mTvMsgNumber.setVisibility(View.GONE);
            }
        }
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "praise":
//                    int message_number = Integer.parseInt(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.MESSAGE_NUMBER, 0 + ""));
//                    message_number = message_number + 1;
//                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.MESSAGE_NUMBER, message_number + "");
//                    if(message_number>99){
//                        mTvMsgNumber.setText("99+");
//                    }else{
//                        mTvMsgNumber.setText(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0"));
//                    }
//                    mTvMsgNumber.setVisibility(View.VISIBLE);

                    int message_number = Integer.parseInt(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.MESSAGE_NUMBER, 0 + ""));
                    message_number = message_number + 1;
                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.MESSAGE_NUMBER, message_number + "");

                    mTvMsgNumber.setText(message_number + "");
                    if(message_number>99){
                        mTvMsgNumber.setText("99+");
                    }
                    mTvMsgNumber.setVisibility(View.VISIBLE);

                    break;
                case "main_find":
                    querymap.put(Utils.REQTIME, AppUtils.getReqTime());
                    unreadRecord();

                    break;

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl1:
                Intent intentone = new Intent(mActivity, RankActivity.class);
                intentone.putExtra("postion", 0);
                startActivity(intentone);
                break;
            case R.id.fl2:
                Intent intenttwo = new Intent(mActivity, RankActivity.class);
                intenttwo.putExtra("postion", 1);
                startActivity(intenttwo);
                break;
        }
    }




    public void unreadRecord(){

        querymap.clear();
        querymap = new HashMap<>();
        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME,Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, Utils.ANDROID);
        querymap.put(Utils.REQTIME, AppUtils.getReqTime());

        OkGo.<String>post(ApiStores.base_url + "/find/unreadRecord")
                .tag(mActivity)
                .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        try {
                            String body = response.body();
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("code").equals("200")) {
                                if(jsonObject.getJSONObject("data").getString("head")!=null&&jsonObject.getJSONObject("data").getString("head").length()>0){
                                    Glide.with(mActivity).load(jsonObject.getJSONObject("data").getString("head")).into(mIvUserIcon);
                                    mIvUserIcon.setVisibility(View.VISIBLE);
                                    iv_red_icon.setVisibility(View.VISIBLE);
                                }else {
                                    mIvUserIcon.setVisibility(View.GONE);
                                    iv_red_icon.setVisibility(View.GONE);
                                }
                                if (jsonObject.getJSONObject("data").getInt("sum") > 0) {
                                    Intent intent1 = new Intent("dynamic");
                                    getActivity().sendBroadcast(intent1);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 查询是否有未读标识
     */

    private void allMyPartysRedType(){
        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        OkGo.<String>post(ApiStores.base_url + "/party/allMyPartysRedType")
                .tag(mActivity)
                .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String strRedType = jsonObject.getJSONObject("data").getString("redType");
                            if("1".equals(strRedType)){
                                tvNew.setVisibility(View.VISIBLE);
                            }else{
                                tvNew.setVisibility(View.GONE);
                                Intent intent1 = new Intent("hideRed");
                                getActivity().sendBroadcast(intent1);
                            }
                        } catch (JSONException e) {
                            tvNew.setVisibility(View.GONE);
                            Intent intent1 = new Intent("hideRed");
                            getActivity().sendBroadcast(intent1);
                          //  e.printStackTrace();
                        }

                    }
                });
    }

}
