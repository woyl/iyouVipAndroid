package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.ResponseFileUrl;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Recordvideo.RecordvideoView;
import com.jfkj.im.mvp.Recordvideo.RecordvideopRresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.ApiUploadClient;
import com.jfkj.im.ui.fragment.PlayVideoFragment;
import com.jfkj.im.ui.fragment.RecordVideoFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.RecordVideoView;
import com.jfkj.im.view.SendView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class RecordvideoActivity extends BaseActivity<RecordvideopRresenter> implements View.OnClickListener, RecordvideoView {
    @BindView(R.id.title_left_tv)
    ImageView title_left_tv;
//    @BindView(R.id.title_center_tv)
//    TextView title_center_tv;
    private RelativeLayout mRecordLayout;
    private RecordVideoView mVideoRecord;
    private SendView mSendView;
    private RecordVideoFragment mRecordVideoFragment;
    private PlayVideoFragment mPlayVideoFragment;
    @BindView(R.id.onClose)
    ImageView onClose;
    @BindView(R.id.onSwitch)
    ImageView onSwitch;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.tv_number)
    TextView tv_number;


    Intent getIntent;
    RecordvideopRresenter recordvideopRresenter;
    String url_video = "";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
//        setStaturBar(rl_head);
//        title_left_tv.setBackgroundResource(R.drawable.back_iv);
//        title_center_tv.setTextSize(R.dimen.tv_size_15sp);
//        title_center_tv.setText("请在录制中读出以下数字");
//        title_center_tv.setTextColor(getResources().getColor(R.color.white));
        onClose.setOnClickListener(this);
        onSwitch.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        getIntent = getIntent();
        token = getIntent.getStringExtra(Utils.TOKEN);
        SPUtils.getInstance(mActivity).put(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
        mRecordVideoFragment = RecordVideoFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fragment_content, mRecordVideoFragment).commit();
        recordvideopRresenter = new RecordvideopRresenter(this);
        initView();
        startAnim();

        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);

        }
        tv_number.setText(sb.toString());


    }

    private void initView() {
        mRecordLayout = findViewById(R.id.record_layout);
        mVideoRecord = findViewById(R.id.record_video_btn_record);
        mVideoRecord.setRecordVideoListener(mRecordVideoListener);
        mSendView = findViewById(R.id.record_SendView);
        mSendView.img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RxView
                .clicks(mSendView.image_complete)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onSaveRecord();
                    }
                });

    }

    private RecordVideoView.RecordVideoListener mRecordVideoListener = new RecordVideoView.RecordVideoListener() {
        @Override
        public void onStartRecord() {
            mRecordVideoFragment.startRecord();
        }

        @Override
        public void onCompleteRecord(boolean isTakePhoto) {
            if (isTakePhoto) {
                Toast.makeText(RecordvideoActivity.this, "请至少录制5S以上", Toast.LENGTH_SHORT).show();
                mRecordVideoFragment.stopRecordShort();
            } else {
                mRecordVideoFragment.stopRecord();
                mSendView.startAnim();
                mRecordLayout.setVisibility(View.GONE);
                mPlayVideoFragment = PlayVideoFragment.
                        newInstance(mRecordVideoFragment.getVideoFilePath());
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, mPlayVideoFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    };


    private void onSaveRecord() {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mRecordVideoFragment.getFile());
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", mRecordVideoFragment.getFile().getName().replace("com.shop.client", ""), requestBody);
//        recordvideopRresenter.uploadfile(ApiStores.base_file+"/file/uploadFiles",
//                "4","1",SPUtils.getInstance(getApplication()).getString(Utils.USERID),body);
        Map<String, String> map = new HashMap<>();
        Map<String, String> headmap = new HashMap<>();
        headmap.put("fileType", "4");
        headmap.put("pathType", "1");
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
//        map.put(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));
//        map.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        headmap.put("userId", SPUtils.getInstance(getApplication()).getString(Utils.USERID));

        OkHttpUtils.post()
                .tag(mActivity)
                .url(ApiStores.baseupload_url + "/file/uploadFiles")
                .headers(headmap)

                .addFile("file", mRecordVideoFragment.getFile().getName(), mRecordVideoFragment.getFile())
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            toastShow(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("200")) {
                                url_video = jsonObject.getJSONObject("data").getString("fileUrls");
                                register();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mPlayVideoFragment != null) {
            mSendView.stopAnim();
            mRecordLayout.setVisibility(View.VISIBLE);
            mPlayVideoFragment.onDelVideo();
        }
        super.onBackPressed();
        mPlayVideoFragment = null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RecordvideoActivity.class);
        context.startActivity(starter);
    }

    public void startAnim() {
        overridePendingTransition(android.R.anim.fade_in, 0);

    }

    public void outAnim() {
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        outAnim();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recordvideo;
    }

    @Override
    public RecordvideopRresenter createPresenter() {
        return recordvideopRresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onClose:
                finish();
                break;
            case R.id.onSwitch:
                mRecordVideoFragment.switchCamera();
                break;
            case R.id.title_left_tv:
                finish();
                break;

        }
    }

    //所有资料齐全了  然后提交注册申请  等待后台审核
    public void register() {
        ApiClient.onDestroy();
        Map<String, String> querymap = new HashMap();
        querymap.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        querymap.put(Utils.OSNAME, Utils.ANDROID);
        querymap.put(Utils.CHANNEL, Utils.ANDROID);
        querymap.put(Utils.DEVICENAME, Utils.getdeviceName());
        querymap.put(Utils.DEVICEID, "1");
        querymap.put(Utils.REQ_TIME, AppUtils.getReqTime());
        querymap.put(Utils.PHOTO1, getIntent.getStringExtra(Utils.PHOTO1));
        querymap.put(Utils.PHOTO2, getIntent.getStringExtra(Utils.PHOTO2));
        querymap.put(Utils.USERVIDEO, url_video);


        OkGo.<String>post(ApiStores.base_url + "/user/setExamine")
                .headers(Utils.TOKEN, UserInfoManger.getToken())
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(querymap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String string = response.body();
                            JSONObject jsonObject = new JSONObject(string);
                            toastShow(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("200")) {
                                SPUtils.getInstance(getApplication()).put(Utils.TOKEN, "");
                                Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);


                                //保存一个状态值用来验证是否是新用户第一次登录
                                SPUtils.getInstance(RecordvideoActivity.this).put(Utils.IS_NEW_USER,"1");

                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void uploadFileSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                url_video = jsonObject.getJSONObject("data").getString("fileUrls");
                register();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void uploadFilefail(String string) {

    }

    @Override
    public void uploadSuccess(ResponseBody responseBody) {

        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void uploadfaail(String s) {

    }

    @Override
    public void setExamine(String setExamine) {
        try {

            JSONObject jsonObject = new JSONObject(setExamine);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);
                startActivity(intent);
                finish();
                ApiClient.onDestroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }
}
