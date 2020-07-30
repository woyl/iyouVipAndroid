package com.jfkj.im.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.SelectSexDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.TimeChange;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.datepicker.CustomDatePicker;
import com.jfkj.im.view.CustomVideoView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class SelectSexActivity extends BaseActivity implements TextWatcher {
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.tv_year)
    TextView tv_year;
    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.image_man)
    ImageButton image_man;
    @BindView(R.id.image_woman)
    ImageButton image_woman;
    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    private long startL = 0;
    private long endL = 0;
    private String birthday;

    private boolean isMan, isWoman;
    SelectSexDialog selectSexDialog;
    Intent getintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(rl_head);
        selectSexDialog = new SelectSexDialog(this);
        getintent = getIntent();
        tv_next.setEnabled(false);
        et_name.addTextChangedListener(this);

    }

    public String getName() {
        return et_name.getText().toString().trim();
    }



    private void isShowTvBg() {
        if (!TextUtils.isEmpty(getName()) && (isWoman || isMan) && !TextUtils.isEmpty(birthday)) {
            tv_next.setEnabled(true);
            tv_next.setAlpha(1f);
            tv_next.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
        } else {
            tv_next.setAlpha(0.4f);
            tv_next.setEnabled(false);
            tv_next.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_base_btn_white_20dp));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_sex;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.tv_next, R.id.ll_brith, R.id.image_man, R.id.image_woman})
    void onClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                dialogFragment();
                break;
            case R.id.ll_brith:
                jumpDate();
                break;
            case R.id.image_man:
                isMan = true;
                isWoman = false;
                isShowTvBg();
                image_man.setBackground(ContextCompat.getDrawable(this, R.mipmap.sex_icon_male_unsel_1));
                image_woman.setBackground(ContextCompat.getDrawable(this, R.mipmap.sex_icon_female_unsel));
                break;
            case R.id.image_woman:
                isWoman = true;
                isMan = false;
                isShowTvBg();
                image_man.setBackground(ContextCompat.getDrawable(this, R.mipmap.sex_icon_male_unsel));
                image_woman.setBackground(ContextCompat.getDrawable(this, R.mipmap.sex_icon_female_unsel_1));
                break;
        }
    }




    private void jumpDate() {
        String start = "1950-01-01 00:00";

        String end = TimeChange.getFormatTime(System.currentTimeMillis());

        CustomDatePicker customDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_year.setText(DataFormatUtils.getDateToString(timestamp, "yyyy"));
                tv_month.setText(DataFormatUtils.getDateToString(timestamp, "MM"));
                tv_day.setText(DataFormatUtils.getDateToString(timestamp, "dd"));
                birthday = DataFormatUtils.getDateToString(timestamp, "yyyy-MM-dd");
                isShowTvBg();
            }
        }, start, end, startL, endL);

        // 允许点击屏幕或物理返回键关闭
        customDatePicker.setCancelable(true);
        // 显示时和分
        customDatePicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        customDatePicker.setScrollLoop(true);
        // 允许滚动动画
        customDatePicker.setCanShowAnim(true);
        customDatePicker.show(System.currentTimeMillis());
    }


    private void dialogFragment() {
        selectSexDialog.show();
        selectSexDialog.setOnItemClick(new SelectSexDialog.OnItemClick() {
            @Override
            public void setOnItem() {
                //验证是否满18
                //验证是否满18
                HashMap<String, String> map = new HashMap<>();
                map.put(Utils.Birthday, birthday);
                map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
                map.put(Utils.OSNAME, Utils.ANDROID);
                map.put(Utils.CHANNEL, Utils.ANDROID);
                map.put(Utils.DEVICENAME, Utils.getdeviceName());
                map.put(Utils.DEVICEID, "1");
                map.put(Utils.REQ_TIME, AppUtils.getReqTime());
                Log.e("msg","..........token.........."+SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));


                Map<String, String> headmap = new HashMap<>();
                headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
                headmap.put(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));

                if (Utils.netWork()) {
                    OkHttpUtils.post()
                            .tag(mActivity)
                            .url(ApiStores.base_url + "/user/ageVerification")
                            .headers(headmap)
                            .params(map)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    selectSexDialog.dismiss();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        toastShow(jsonObject.getString("message"));
                                        if (jsonObject.getString("code").equals("200")) {
                                            next();
                                        }
                                        selectSexDialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    toastShow(R.string.nonetwork);
                }
            }
        });
    }

    private void next() {
        Map<String, String> headmap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("nickName", getName());
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        String sex = "";
        if (isMan) {
            sex = "1";
        }
        if (isWoman) {
            sex = "2";
        }
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.Birthday, birthday);
        map.put(Utils.GENDER, sex);
        headmap.put(Utils.TOKEN, getintent.getStringExtra(Utils.TOKEN));
        headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/user/updateGender")
//                    .headers(headmap)
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            selectSexDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    Intent intent = null;
                                    if (isMan) {
                                        UserInfoManger.saveGender("2");
                                        intent = new Intent(App.getAppContext(), ChooseLikePersonActivity.class);
                                        intent.putExtra(Utils.TOKEN, getintent.getStringExtra(Utils.TOKEN));
                                        intent.putExtra(Utils.SEX, "1");//性别
                                    } else {
                                        UserInfoManger.saveGender("1");
                                        intent = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                                        intent.putExtra(Utils.TOKEN, getintent.getStringExtra(Utils.TOKEN));
                                        intent.putExtra(Utils.SEX, "2");//性别
                                    }
                                    startActivity(intent);
                                    finish();
                                    selectSexDialog.dismiss();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        isShowTvBg();
    }
}
