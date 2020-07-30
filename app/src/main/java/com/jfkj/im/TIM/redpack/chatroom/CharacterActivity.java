package com.jfkj.im.TIM.redpack.chatroom;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class CharacterActivity extends BaseActivity {

    @BindView(R.id.tv_start_test)
    TextView tv_start_test;
    @BindView(R.id.iv_title_right_icon1)
    ImageView iv_title_right_icon1;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_4)
    TextView tv_4;
    @BindView(R.id.tv_5)
    TextView tv_5;
    @BindView(R.id.tv_6)
    TextView tv_6;
    @BindView(R.id.img_head)
    ImageView img_head;
    @BindView(R.id.tv_bottom)
    TextView tv_bottom;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.constraint_head_1)
    ConstraintLayout constraint_head_1;

    private int isChangeTopic = -1;

    private CommonRecyclerAdapter<AnswerBeans> adapter;
    private List<AnswerBeans> answerBeansList = new ArrayList<>();
    private Context mContext;
    private LinearLayoutManager gridLayoutManager;
    private StringBuilder questionIds, answerIds;
    private LinkedHashMap<String,String> answerIdsList, questionIdsList;
    private String type, orderId;

    private CommonRecyclerAdapter<AnswerBeans.AnswerBean> adapter1;

    private String questionId;
    private int currentPosition;
    private int typeBt = 1;
    private Handler handler;
    private Runnable runnable;

    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageAtTime(1,500);
    }


    public static class MyHandler extends Handler {
        private WeakReference<CharacterActivity> weakReference;
        private MyHandler(CharacterActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            CharacterActivity characterActivity = weakReference.get();
            if (msg.what == 1) {
                characterActivity.iniViews();
                characterActivity.setAdapter();
            }
        }
    }

    private void setAdapter() {
        adapter = new CommonRecyclerAdapter<AnswerBeans>(this, answerBeansList, R.layout.item_character_text) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, AnswerBeans answerBeans, int position_all) {
                TextView tv_number = holder.getView(R.id.tv_number);
                tv_number.setText((position_all + 1) + "/" + answerBeansList.size());
                questionId = String.valueOf(answerBeans.getQuestionId());
                currentPosition = position_all;
                TextView tv_content = holder.getView(R.id.tv_content);
                tv_content.setText(position_all + 1+"."+answerBeans.getSubject());
                RecyclerView recycler_answer = holder.getView(R.id.recycler_answer);
                recycler_answer.setLayoutManager(new LinearLayoutManager(mContext));
                if (recycler_answer.getItemDecorationCount() == 0) {
                    recycler_answer.addItemDecoration(new SpacesItemDecoration(65));
                }
                adapter1 = new CommonRecyclerAdapter<AnswerBeans.AnswerBean>(mContext, answerBeans.getAnswer(), R.layout.item_select_character_answer) {
                    @Override
                    public void convert(CommonRecyclerHolder holder, AnswerBeans.AnswerBean answerBean, int position) {
                        TextView tv_answer = holder.getView(R.id.tv_answer);
                        LinearLayout linearLayout  = holder.getView(R.id.ll_layout);

                        char a = (char) ((char)answerBean.getSerialNo() + 96);

                        tv_answer.setText(a+"."+answerBean.getContent());
                        if (answerBean.isSelect()) {
                            linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shpe_select_two_character_answer));
                        } else {
                            linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_character_brg));
                        }
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (AnswerBeans.AnswerBean answerBean1 : answerBeans.getAnswer()) {
                                    answerBean1.setSelect(false);
                                    linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shpe_select_two_character_answer));
                                }
                                answerBean.setSelect(true);

//                                answerIds.append(answerBean.getAnswerId()).append(",");
//                                questionIds.append(answerBean.getQuestionId()).append(",");
                                questionIdsList.put(String.valueOf(answerBean.getQuestionId()), answerBean.getQuestionId() + "");
                                answerIdsList.put(String.valueOf(answerBean.getQuestionId()), answerBean.getAnswerId() + "");


                                if (position_all + 1 < answerBeansList.size()) {
                                    runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            gridLayoutManager.scrollToPositionWithOffset(position_all + 1, 0);
                                        }
                                    };
                                    handler.postDelayed(runnable, 300);
                                }

                                if (TextUtils.equals("1", type)) {
                                    if (position_all == answerBeansList.size() - 1) {
                                        if (questionIdsList.size() == 0){
                                            toastShow("数据加载失败");
                                            return;
                                        }
                                        for (Map.Entry<String,String> map : questionIdsList.entrySet()){
                                            questionIds.append(map.getValue()).append(",");
                                        }
                                        for (Map.Entry<String,String> map : answerIdsList.entrySet()){
                                            answerIds.append(map.getValue()).append(",");
                                        }
                                        if (answerIds.length() == 0 && questionIds.length() == 0){
                                            toastShow("数据加载失败");
                                            return;
                                        }
                                        startActivity(new Intent(mContext, AnswerSubmitActivity.class)
                                                .putExtra("answerIds", answerIds.toString())
                                                .putExtra("questionIds", questionIds.toString()));
                                        finish();
                                    }
                                } else {
                                    if (position_all == answerBeansList.size() - 1) {
                                        tv_sure.setVisibility(View.VISIBLE);
                                    }
                                }
                                adapter1.notifyDataSetChanged();
                            }
                        });
                    }
                };
                recycler_answer.setAdapter(adapter1);
            }
        };
        recycler.setAdapter(adapter);

    }

    private void iniViews() {
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head_1);
        mContext = this;
        type = getIntent().getStringExtra("type");
//        tv_bottom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tv_bottom.getPaint().setAntiAlias(true);

        if (TextUtils.equals("1", type)) {
            tv_bottom.setVisibility(View.VISIBLE);
            tv_start_test.setVisibility(View.VISIBLE);
            tv_sure.setVisibility(View.GONE);
        } else {
            tv_sure.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);

            img_head.setVisibility(View.GONE);
            tv_start_test.setVisibility(View.GONE);
            tv_bottom.setVisibility(View.GONE);
            tv_1.setVisibility(View.GONE);
            tv_2.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
            tv_4.setVisibility(View.GONE);
            tv_5.setVisibility(View.GONE);
            tv_6.setVisibility(View.GONE);
            orderId = getIntent().getStringExtra("orderId");
            getData();
        }
        gridLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        recycler.setLayoutManager(gridLayoutManager);
        pagerSnapHelper.attachToRecyclerView(recycler);

        questionIds = new StringBuilder();
        answerIds = new StringBuilder();
        handler = new Handler();
        answerIdsList = new LinkedHashMap<>();
        questionIdsList = new LinkedHashMap<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_character;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.iv_title_right_icon1, R.id.tv_start_test, R.id.tv_sure, R.id.tv_bottom})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_test:
                hide();
                break;
            case R.id.iv_title_right_icon1:
                TipsBaseDialogFragment tipsBaseDialogFragment
                        = new TipsBaseDialogFragment(true, Gravity.CENTER, "关闭后将不会保存你的答题记录，是否退出？", "取消", "确定", false);
                tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            finish();
                        }
                    }
                });
                tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.tv_sure:
                if (TextUtils.isEmpty(orderId)) {
                    toastShow("获取数据失败");
                    return;
                }
                submitData();
                break;
            case R.id.tv_bottom:
                if (typeBt == 2) {
                    if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 6) {
                        changeQuestion();
                    } else {
                        VipSetGradeDialogFragment vipSetGradeDialogFragment
                                = new VipSetGradeDialogFragment(true, Gravity.CENTER, "VIP等级达到6级以后，才可解锁性格测试换一题功能。","确认");
                        vipSetGradeDialogFragment.setRsp(new ResponListener<Boolean>() {
                            @Override
                            public void Rsp(Boolean s) {
                                if (s) {

                                }
                            }
                        });
                        vipSetGradeDialogFragment.show(getSupportFragmentManager(), "");
                    }
                } else {
                    Intent intent = new Intent(App.getAppContext(), MyCharacterTextActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void submitData() {
        progressDialog.show();
        if (answerIdsList.size() == 0){
            toastShow("数据加载失败");
            return;
        }
        answerIds.delete(0,answerIds.length());
        for (Map.Entry<String,String> map : answerIdsList.entrySet()){
            answerIds.append(map.getValue()).append(",");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GAMEID, orderId);
        map.put(Utils.ANSWER_IDS, answerIds.toString());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/submitUserAnswer")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
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
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    finish();
                                } else if (jsonObject.getString("code").equals("30001")) {//{"code":"30001","message":"性格测试游戏已结束","data":{}}
                                    toastShow(jsonObject.getString("message"));
                                }else {
                                    toastShow(jsonObject.getString("message"));
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

    private void hide() {


        constraint_head_1.setBackgroundResource(R.mipmap.test_bg_nor);
        img_head.setVisibility(View.GONE);
        tv_1.setVisibility(View.GONE);
        tv_2.setVisibility(View.GONE);
        tv_3.setVisibility(View.GONE);
        tv_4.setVisibility(View.GONE);
        tv_5.setVisibility(View.GONE);
        tv_6.setVisibility(View.GONE);
        typeBt = 2;
        tv_start_test.setVisibility(View.GONE);
        tv_bottom.setText("换一题");

         Drawable drawable = this.getResources().getDrawable(R.mipmap.icon_charare_cutover);
        tv_bottom.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);


        recycler.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
//        progressDialog.show();
        if (TextUtils.equals(type,"2")){
            AnswerUtrils.getOtherUser(orderId, new TIMValueCallBack<List<AnswerBeans>>() {
                @Override
                public void onError(int code, String desc) {
//                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(List<AnswerBeans> answerBeans) {
//                    progressDialog.dismiss();
                    answerBeansList.addAll(answerBeans);
                    adapter.refreshData(answerBeans);

                }
            });
        }else {
            new AnswerUtrils(mContext).getData(new TIMValueCallBack<List<AnswerBeans>>() {
                @Override
                public void onError(int code, String desc) {
//                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(List<AnswerBeans> answerBeans) {
//                    progressDialog.dismiss();
                    answerBeansList.addAll(answerBeans);
                    adapter.refreshData(answerBeans);
                }
            });
        }

    }

    private void changeQuestion() {
        isChangeTopic = -1;
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.QUESTIONIDS, questionId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/replaceQuestion")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
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
                                    AnswerBeans answerBeans = JSON.parseObject(jsonObject.getString("data"), AnswerBeans.class);
                                    if (answerBeansList != null && answerBeansList.size() > 0) {
                                        for(int i =0;i<answerBeansList.size();i++){
                                            if(answerBeansList.get(i).getQuestionId() == answerBeans.getQuestionId()){
                                                isChangeTopic ++;
                                            }
                                        }

                                        if(isChangeTopic == -1){
                                            answerBeansList.set(currentPosition, answerBeans);
                                            adapter.setItemData(answerBeansList, currentPosition);
                                        }else{
                                            changeQuestion();
                                        }

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

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
