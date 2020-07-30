package com.jfkj.im.TIM.redpack.chatroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.event.MatchCharacterEvent;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;

public class MyCharavterUserTextFragment extends BaseFragment {

    @BindView(R.id.recycler)
    SwipeRecyclerView recycler;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;

    private String gameId;
    private CommonRecyclerAdapter<MyCharacterTextBeanLeft> adapter;
    private List<MyCharacterTextBeanLeft> myCharacterTextBeanLeft;
    private List<AnswerSelfBean> answerSelfBeans = new ArrayList<>();

    public static MyCharavterUserTextFragment getInstance(String gameId,List<AnswerSelfBean> answerSelfBeans){
        Bundle bundle = new Bundle();
        bundle.putString("gameId",gameId);
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) answerSelfBeans);
        MyCharavterUserTextFragment adventureFragment = new MyCharavterUserTextFragment();
        adventureFragment.setArguments(bundle);
        return adventureFragment;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview_my_character;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniviews();
        inDatas();
    }


    private void iniviews() {
        if (getArguments() != null){
            gameId = getArguments().getString("gameId");
            answerSelfBeans = getArguments().getParcelableArrayList("data");
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ll_bg.getLayoutParams());
        layoutParams.gravity = Gravity.CENTER;
        ll_bg.setLayoutParams(layoutParams);
        layoutParams.width = (int) (ScreenUtil.getScreenWidth(Objects.requireNonNull(getActivity())) * 0.85);
        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new CommonRecyclerAdapter<MyCharacterTextBeanLeft>(mActivity,myCharacterTextBeanLeft,R.layout.item_character_user_right) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, MyCharacterTextBeanLeft myCharacterTextBeanLeft, int position) {
                ImageView cir_head = holder.getView(R.id.cir_head);
                TextView tv_nickname = holder.getView(R.id.tv_nickname);
                TextView tv_match_pre = holder.getView(R.id.tv_match_pre);
                TextView tv_rank = holder.getView(R.id.tv_rank);
                int rank = position + 1;
                tv_rank.setText(rank+"");
//                if (rank < 4){
//                    tv_rank.setTextColor(ContextCompat.getColor(mContext,R.color.cEF4769));
//                }else {
//                    tv_rank.setTextColor(ContextCompat.getColor(mContext,R.color.black));
//                }
                Glide.with(mContext).load(myCharacterTextBeanLeft.getHead()).into(cir_head);
                tv_nickname.setText(myCharacterTextBeanLeft.getNickName());
                tv_match_pre.setText("匹配度:"+myCharacterTextBeanLeft.getPercentage()+"%");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,MatchCharacterActivity.class)
                        .putExtra("RESULTID",myCharacterTextBeanLeft.getResultId())
                        .putExtra("gameId",gameId));
                        EventBus.getDefault().postSticky(new MatchCharacterEvent(answerSelfBeans));
                    }
                });
            }
        };
        recycler.setAdapter(adapter);
    }

    private void inDatas() {
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GAMEID,gameId);
        map.put(Utils.PAGENO,"1");
        map.put(Utils.PAGESIZE,"20");
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/loadUserAnswerList")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getActivity()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    List<MyCharacterTextBeanLeft> myCharacterTextBeanLefts = JSON.parseArray(object.getString("array"), MyCharacterTextBeanLeft.class);
                                    if (myCharacterTextBeanLefts.size() > 0 ){
                                        recycler.setVisibility(View.VISIBLE);
                                        adapter.addData(myCharacterTextBeanLefts);
                                        tv_no_data.setVisibility(View.GONE);
                                    }else {
                                        tv_no_data.setVisibility(View.VISIBLE);
                                        recycler.setVisibility(View.GONE);
                                    }

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
}
