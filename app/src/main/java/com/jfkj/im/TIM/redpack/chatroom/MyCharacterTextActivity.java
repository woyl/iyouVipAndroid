package com.jfkj.im.TIM.redpack.chatroom;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MyCharacterTextActivity extends BaseActivity {

    @BindView(R.id.swipe_recycler)
    SwipeRecyclerView swipe_recycler;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.img_no_date)
    ImageView img_no_date;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    private CommonRecyclerAdapter<MyCharacterTextBean> adapter;
    private List<MyCharacterTextBean> myCharacterTextBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniviews();
        getData();
    }

    private void iniviews() {
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        swipe_recycler.setLayoutManager(new LinearLayoutManager(this));
//        swipe_recycler.addItemDecoration(new SpacesItemDecoration(30));
        adapter = new CommonRecyclerAdapter<MyCharacterTextBean>(this,myCharacterTextBeans,R.layout.item_character_my) {
            @Override
            public void convert(CommonRecyclerHolder holder, MyCharacterTextBean myCharacterTextBean, int position) {

                if(position==0 && myCharacterTextBeans.size()>1){
                    holder.getView(R.id.view_1).setVisibility(View.GONE);
                }else if ((position == myCharacterTextBeans.size()-1) && myCharacterTextBeans.size()!=1){
                    holder.getView(R.id.view_2).setVisibility(View.GONE);
                }


                TextView tv_data = holder.getView(R.id.tv_data);
                tv_data.setText(myCharacterTextBean.getAddDate());
                TextView tv_state = holder.getView(R.id.tv_state);
                if (myCharacterTextBean.getGameState() == 1){
                    tv_state.setText("进行中");
                    tv_state.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shap_my_character_text_bg_two));
                    tv_state.setTextColor(ContextCompat.getColor(mContext,R.color.cff32d5f1));
                }else {
                    tv_state.setText("已结束");
                    tv_state.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shap_my_character_text_bg));
                    tv_state.setTextColor(ContextCompat.getColor(mContext,R.color.c666666));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,MyCharacterTextDetailsActivity.class)
                        .putExtra("gameId",myCharacterTextBean.getGameId())
                        .putExtra("time",myCharacterTextBean.getAddDate()));
                    }
                });
            }
        };
        swipe_recycler.setAdapter(adapter);
    }
    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.PAGESIZE,"10");
//        map.put(Utils.SORT,)
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/loadSquareGameList")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            finish();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    myCharacterTextBeans = JSON.parseArray(object.getString("array"), MyCharacterTextBean.class);
                                    if (myCharacterTextBeans.size() > 0){
                                        adapter.addData(myCharacterTextBeans);
                                        img_no_date.setVisibility(View.GONE);
                                        tv_no_data.setVisibility(View.GONE);
                                    }else {
                                        img_no_date.setVisibility(View.VISIBLE);
                                        tv_no_data.setVisibility(View.VISIBLE);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            toastShow(R.string.nonetwork);
            finish();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_character_text;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.iv_title_back)
    void Onclick(){
        finish();
    }
}
