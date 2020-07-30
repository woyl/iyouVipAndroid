package com.jfkj.im.TIM.redpack.chatroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.R;
import com.jfkj.im.TIM.game.TabAdapter;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MyCharacterTextDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.progress_bar_red)
    ProgressBar progress_bar_red;
    @BindView(R.id.view_pager2)
    ViewPager2 view_pager2;
    @BindView(R.id.radio)
    RadioGroup radioGroup;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    @BindView(R.id.radio_1)
    RadioButton r1;

    @BindView(R.id.radio_2)
    RadioButton r2;

    private TabAdapter tabAdapter;
    private String gameId;
    private String time, cadddate;
    private TimeCountUtil timeCountUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head);
        gameId = getIntent().getStringExtra("gameId");
        time = getIntent().getStringExtra("time");
        cadddate = getIntent().getStringExtra("cadddate");
        long times = 0;
        if (cadddate != null) {
            times = Long.parseLong(cadddate);
        } else {
            times = DataFormatUtils.getStringToDate(time, "yyyy-MM-dd HH:mm:ss");
        }

//        if (!TextUtils.isEmpty(time)) {
//            times = DataFormatUtils.getStringToDate(time, "yyyy-MM-dd HH:mm:ss");
//        } else {
//            times = DataFormatUtils.getStringToDate(cadddate, "yyyy-MM-dd HH:mm:ss");
//        }

        Log.e("msg","...........times............."+times);
        Log.e("msg","...........System.currentTimeMillis()............."+System.currentTimeMillis());
        long syTime = System.currentTimeMillis() - times;

        Log.e("msg","...........syTime............."+syTime);
        if (syTime > 60*1000*60) {
            progress_bar_red.setProgress(100);
            tv_date.setVisibility(View.GONE);
        } else {
            tv_date.setVisibility(View.VISIBLE);
            timeCountUtil = new TimeCountUtil((60*60*1000 - syTime), 1000, new CountDownTimeListener() {
                @Override
                public void onTimeFinish() {
                    tv_date.setVisibility(View.GONE);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeTick(long millisUntilFinished) {
                    Log.e("msg", ".........millisUntilFinished............" + millisUntilFinished);
                    tv_date.setText("剩余时间  " + DataFormatUtils.getDateToString(millisUntilFinished, "mm分ss秒"));


                    int progress = (int) ((millisUntilFinished / ((double) 60 * 1000)));
                    int shengyuTime = (int) (((60 - progress) * 1.66 / (double) 100) * 100);
                    Log.e("progress", "............" + shengyuTime);
                    progress_bar_red.setProgress(shengyuTime);
                }
            });
            timeCountUtil.start();

        }

        view_pager2.setOffscreenPageLimit(2);
        view_pager2.setUserInputEnabled(true);
        view_pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                radioGroup.check(getCheckedId(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        inSelf();
        addLister();
        iniTest();
    }

    @OnClick(R.id.iv_title_right_icon1)
    void onClick() {
        finish();
    }

    private void inSelf() {
        progressDialog.show();
        new AnswerUtrils(mActivity).getSelfAnswer(gameId, new TIMValueCallBack<List<AnswerSelfBean>>() {
            @Override
            public void onError(int code, String desc) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(List<AnswerSelfBean> answerSelfBeans) {
                progressDialog.dismiss();
                if (answerSelfBeans != null) {
                    setAdapter(answerSelfBeans);
                }
            }
        });
    }

    private void addLister() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_1:
                        view_pager2.setCurrentItem(TabAdapter.PAGE_HOME);
                        break;
                    case R.id.radio_2:
                        view_pager2.setCurrentItem(TabAdapter.PAGE_CLASS);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private int getCheckedId(int position) {
        int checkedId = R.id.radio_1;
        switch (position) {
            case 0:
                checkedId = R.id.radio_1;
                r1.setTextSize(16);
                r2.setTextSize(14);
                break;
            case 1:
                checkedId = R.id.radio_2;
                r1.setTextSize(14);
                r2.setTextSize(16);
                break;
        }
        return checkedId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_character_text_details;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    private void setAdapter(List<AnswerSelfBean> answerSelfBeans) {
        tabAdapter = new TabAdapter(this, gameId, answerSelfBeans);
        view_pager2.setAdapter(tabAdapter);
    }

    private void iniTest() {
        progressDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/square/getSquareTips")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(this).getString(Utils.TOKEN))
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
//                                    List<MyCharacterTextBeanLeft> myCharacterTextBeanLeft = JSON.parseArray(object.getString("array"), MyCharacterTextBeanLeft.class);
//                                    setAdapter(myCharacterTextBeanLeft);
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
        if (timeCountUtil != null) {
            timeCountUtil.onFinish();
            timeCountUtil.cancel();
        }
        super.onDestroy();

    }
}
