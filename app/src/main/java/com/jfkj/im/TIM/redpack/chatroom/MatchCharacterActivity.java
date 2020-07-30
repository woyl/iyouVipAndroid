package com.jfkj.im.TIM.redpack.chatroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.HeadUrlNameUtils;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.event.MatchCharacterEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MatchCharacterActivity extends BaseActivity {
    @BindView(R.id.cir_left)
    ImageView cir_left;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_match)
    TextView tv_match;
    @BindView(R.id.cir_right)
    ImageView cir_right;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recycler)
    SwipeRecyclerView recycler;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    @BindView(R.id.progress_bar_red)
    ProgressBar progressBar;

    private String resultId,gameId;
    private List<MatchBean.AnswerBean> answerBeanList = new ArrayList<>();
    private CommonRecyclerAdapter<MatchBean.AnswerBean> adapter;
    private List<AnswerSelfBean> answerSelfBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniviews();
        iniData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getDatas(MatchCharacterEvent event){
        this.answerSelfBeans.addAll(event.getAnswerSelfBeans());
    }

    private void setAdapter(List<AnswerSelfBean> answerSelfBeans,List<MatchBean.AnswerBean> answerBeans) {
        adapter = new CommonRecyclerAdapter<MatchBean.AnswerBean>(this,answerBeans,R.layout.item_character_match) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, MatchBean.AnswerBean answerBean, int position) {

                TextView tv_content = holder.getView(R.id.tv_content);
                TextView tv_answer = holder.getView(R.id.tv_answer);
                TextView tv_no_answer = holder.getView(R.id.tv_no_answer);

                char a = (char) ((char)answerBean.getSerialNo() + 96);
                tv_no_answer.setText( a+ "." + answerBean.getContent());


                if (answerSelfBeans != null && answerSelfBeans.size() > 0){
                    tv_content.setText((position+1)+"."+answerSelfBeans.get(position).getSubject());

                    char a1 = (char) ((char)answerSelfBeans.get(position).getAnswer().getSerialNo() + 96);
                    tv_answer.setText(a1+"."+answerSelfBeans.get(position).getAnswer().getContent());

                    if (answerSelfBeans.get(position).getAnswer().getQuestionId() == answerBean.getQuestionId()){
                        if (answerSelfBeans.get(position).getAnswer().getAnswerId() == answerBean.getAnswerId()){
                            tv_no_answer.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_item_fragment_btn_brg2));
//                            tv_no_answer.setAlpha(1f);
                        }else {
//                            tv_no_answer.setAlpha(0.1f);
                            tv_no_answer.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_base_btn_white_alpha10_20dp));
                        }
                    }
                }
            }
        };
        recycler.setAdapter(adapter);
    }

    private void iniviews() {
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(constraint_head);
        resultId = getIntent().getStringExtra("RESULTID");
        gameId = getIntent().getStringExtra("gameId");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new SpacesItemDecoration(30));
        String [] heads = HeadUrlNameUtils.getHeadName();
        Glide.with(mActivity).load(heads[1]).into(cir_right);
        tv_right.setText(heads[0]);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_match_character;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.iv_title_right_icon1)
    void Onclick(){
        finish();
    }

    private void iniData() {
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.RESULTID,resultId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/comparisonAnswer")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(this).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    MatchBean matchBean = JSON.parseObject(jsonObject.getString("data"), MatchBean.class);
                                    Glide.with(mActivity).load(matchBean.getHead()).into(cir_left);
                                    tv_left.setText(matchBean.getNickName());
                                    tv_match.setText(matchBean.getPercentage()+"%");
                                    progressBar.setProgress(matchBean.getPercentage());


                                    if (answerSelfBeans != null && answerSelfBeans.size() > 0){
                                        setAdapter(answerSelfBeans,matchBean.getAnswer());
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
