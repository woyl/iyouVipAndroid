package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class GiftGivingActivity extends BaseActivity {

    @BindView(R.id.recycler_img)
    RecyclerView recycler_img;
    @BindView(R.id.tv_1)
    TextView tv_1;

    private List<String> imgs;
    private CommonRecyclerAdapter<String> adapter;
    private Context mContext;
    private List<String> uList;
    private UserInfoBean userInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(tv_1);
        iniviews();
    }

    String userIds;

    private void iniviews() {
        imgs = getIntent().getStringArrayListExtra("list");
        uList = getIntent().getStringArrayListExtra("useridList");

        Gson gson = new Gson();
        String json = gson.toJson(uList);

        SPUtils.getInstance(this).put(SPUtils.getInstance(this).getString(Utils.USERID) + Utils.UpSex,json);


        recycler_img.setLayoutManager(new GridLayoutManager(mContext,3));
        adapter = new CommonRecyclerAdapter<String>(mContext,imgs,R.layout.item_send_apply) {
            @Override
            public void convert(CommonRecyclerHolder holder, String s,int position) {
                ImageView img = holder.getView(R.id.img);
                if (!TextUtils.isEmpty(s)){
                    Glide.with(mContext).load(s).into(img);
                }else {
                    Glide.with(mContext).load(R.mipmap.img_avatar_01).into(img);
                }
            }
        };
        recycler_img.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift_giving;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.tv_send)
    void onClick(){

        userIds = "";
        for (int i = 0 ; i < uList.size() ; i++){
            userIds +="," + uList.get(i);
        }


        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("friends", userIds);


        Map<String, String> headmap = new HashMap<>();
        headmap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        headmap.put(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));

        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.base_url + "/user/regOptFriends")
                .headers(headmap)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(response.contains("200")){
                            Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
                        }

                        String userInfo = UserInfoManger.getUserInfo();

                        if(!"".equals(userInfo)){
                            userInfoBean = JSON.parseObject(userInfo, UserInfoBean.class);

                            if(userInfoBean.getVipCard() == 0){
                                //进入购买会员卡页面
                                startActivity(new Intent(GiftGivingActivity.this,BuyMemberActivity.class));
                            }else if(userInfoBean.getVipCard() == 1){
                                //银卡 ,进入审核流程
                                if(userInfoBean.getExamine() == 0){
                                    //未提交审核
                                    Intent uploadpictures_code = new Intent(App.getAppContext(), UploadpicturesActivity.class);
                                    uploadpictures_code.putExtra(Utils.TOKEN, UserInfoManger.getToken());
                                    startActivity(uploadpictures_code);
                                }else  if(userInfoBean.getExamine() ==2 ){
                                    //审核中
                                    Intent intent = new Intent(App.getAppContext(), AuditingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    //审核失败
                                    Intent intent = new Intent(App.getAppContext(), AuditresultActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }else if (userInfoBean.getVipCard() == 2  || userInfoBean.getVipCard() ==3){
                                //黑卡 金卡用户 直接进入首页
                                Intent intent = new Intent(App.getAppContext(), MainActivity.class).putStringArrayListExtra("useridList", (ArrayList<String>) uList);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                ApiClient.onDestroy();
                            }


                        }else{
                            startActivity(new Intent(mContext,BuyMemberActivity.class).putStringArrayListExtra("uList", (ArrayList<String>) uList));
                        }
                    }
                });





    }
}
