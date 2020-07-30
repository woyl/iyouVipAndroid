package com.jfkj.im.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jfkj.im.Bean.HomeRecommendBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.ChooseLikeFemaleDialogFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.view.GlideRoundTransform;
import com.lin.cardlib.CardLayoutManager;
import com.lin.cardlib.CardSetting;
import com.lin.cardlib.CardTouchHelperCallback;
import com.lin.cardlib.OnSwipeCardListener;
import com.lin.cardlib.utils.ReItemTouchHelper;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ChooseLikePersonActivity2 extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_head)
    RecyclerView recycler_head;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_choose)
    TextView tv_choose;


    private List<HomeRecommendBean> homeRecommendBeans = new ArrayList<>();
    private CommonRecyclerAdapter<HomeRecommendBean> adapter;

    private List<String> heads;
    private List<String> userid;
    private CommonRecyclerAdapter<String> adapterHead;
    private Context mContext;
    private int num, getNum;

    int pageNo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(tv_1);


        initData();

        initView();

        setHeadAdater();
    }

    private void setHeadAdater() {
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_head.setLayoutManager(manager);
        recycler_head.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = -60;
            }
        });
        adapterHead = new CommonRecyclerAdapter<String>(mContext, heads, R.layout.item_cho_head) {
            @SuppressLint("CheckResult")
            @Override
            public void convert(CommonRecyclerHolder holder, String s, int position) {
                ImageView imageView = holder.getView(R.id.rounded_img);
                if (!TextUtils.isEmpty(s)) {
                    Glide.with(mContext).load(s).into(imageView);
                } else {
                    Glide.with(mContext).load(s).load(imageView);
                }
            }
        };
        recycler_head.setAdapter(adapterHead);
    }

    private void initView() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CardSetting cardSetting = new CardSetting() {
            @Override
            public boolean isLoopCard() {
                return true;
            }

            @Override
            public int getSwipeDirection() {
                return ReItemTouchHelper.LEFT | ReItemTouchHelper.RIGHT;
            }

            @Override
            public int couldSwipeOutDirection() {
                return ReItemTouchHelper.LEFT | ReItemTouchHelper.RIGHT;
            }

            @Override
            public int getShowCount() {
                return 1;
            }
        };
        cardSetting.setSwipeListener(new OnSwipeCardListener() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float v, float v1, int direction) {
                CommonRecyclerHolder holder = (CommonRecyclerHolder) viewHolder;
                int all = ScreenUtil.getScreenWidth() - ScreenUtil.dpToPx(80);
                float alpha = Math.abs(v) / all;
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Log.e("aaa", "swiping direction=down");
                        break;
                    case ReItemTouchHelper.UP:
                        Log.e("aaa", "swiping direction=up");
                        break;
                    case ReItemTouchHelper.LEFT:
                        Log.e("aaa", "swiping direction=left");
                        Log.e("aaa", "swiping direction=left" + v);
                        Log.e("aaa", "swiping direction=left" + v1);
                        Log.e("aaa", "swiping direction=left" + direction);
                        holder.getView(R.id.iv_dislike).setAlpha(alpha);
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Log.e("aaa", "swiping direction=right");
                        holder.getView(R.id.iv_like).setAlpha(alpha);
                        break;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSwipedOut(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                CommonRecyclerHolder holder = (CommonRecyclerHolder) viewHolder;
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Log.e("aaa", "swiping... direction=down");
                        break;
                    case ReItemTouchHelper.UP:
                        Log.e("aaa", "swiping ...direction=up");
                        break;
                    case ReItemTouchHelper.LEFT:
                        Log.e("aaa", "swiping ....direction=left");
                        holder.getView(R.id.iv_dislike).setAlpha(0);
                        holder.getView(R.id.iv_like).setAlpha(0);
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Log.e("aaa", "swiping ....direction=right");
                        HomeRecommendBean homeRecommendBean = (HomeRecommendBean) o;
                        OkLogger.e("添加的UseriD" + homeRecommendBean.getRecommenduid());
                        OkLogger.e("添加的head" + homeRecommendBean.getHead());
                        heads.add(homeRecommendBean.getHead());
                        userid.add(homeRecommendBean.getRecommenduid());
                        num++;
                        tv_number.setText(num+"/9");
                        if(num == 9){
                            startActivity(new Intent(mContext,GiftGivingActivity.class)
                                    .putStringArrayListExtra("list", (ArrayList<String>) heads).putStringArrayListExtra("useridList", (ArrayList<String>) userid));
                            finish();
                        }
                        holder.getView(R.id.iv_dislike).setAlpha(0);
                        holder.getView(R.id.iv_like).setAlpha(0);
                        break;
                }
            }

            @Override
            public void onSwipedClear() {
                toastShow("你已选择：");
            }
        });


        CardTouchHelperCallback helperCallback = new CardTouchHelperCallback(recyclerView, homeRecommendBeans, cardSetting);
        ReItemTouchHelper mReItemTouchHelper = new ReItemTouchHelper(helperCallback);
        CardLayoutManager layoutManager = new CardLayoutManager(mReItemTouchHelper, cardSetting);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new CommonRecyclerAdapter<HomeRecommendBean>(this, homeRecommendBeans, R.layout.item_choose_like) {
            @Override
            public void convert(CommonRecyclerHolder holder, HomeRecommendBean bean, int position) {
                ImageView view = holder.getView(R.id.iv_avatar);

                Glide.with(getApplicationContext()).load(bean.getHead()).into(view);

            }


        };

        recyclerView.setAdapter(adapter);


    }


    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGENO, pageNo + "");
        map.put(Utils.PAGESIZE, "10");
        map.put(Utils.GENDER, "2");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/home/homeRecommend")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    List<HomeRecommendBean> homeRecommendBean = JSON.parseArray(jsonObject.getString("data"), HomeRecommendBean.class);
                                    if (homeRecommendBean != null) {
                                        homeRecommendBeans.addAll(homeRecommendBean);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            JumpUtil.overlay(this, Loginregister_phone_Activity.class);
            toastShow(R.string.nonetwork);
        }


        heads = new ArrayList<>();
        userid = new ArrayList<>();


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_like_person;
    }


    @Override
    public BasePresenter createPresenter() {
        return null;
    }



    @OnClick({R.id.img_cancel, R.id.img_sel})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.img_cancel:
                OkLogger.e(homeRecommendBeans.size() + "--------");
                if (homeRecommendBeans.size() > 0) {
                    homeRecommendBeans.remove(0);
                    adapter.notifyDataSetChanged();
                }

                if (homeRecommendBeans.size() == 1) {
                    pageNo++;
                    refushAddData();
                }
                break;
            case R.id.img_sel:
                OkLogger.e(homeRecommendBeans.size() + " \t-----------------------\t " + num);
                if (homeRecommendBeans.size() > 0) {

                    heads.add(homeRecommendBeans.get(0).getHead());
                    userid.add(homeRecommendBeans.get(0).getRecommenduid());
                    num++;
                    tv_number.setText(num + "/9");
                    adapterHead.notifyDataSetChanged();
                    homeRecommendBeans.remove(0);
                    adapter.notifyDataSetChanged();
                } else {
                    pageNo++;
                    refushAddData();
                }

                if (heads.size() >= 9) {
                    startActivity(new Intent(mContext, GiftGivingActivity.class).putStringArrayListExtra("list", (ArrayList<String>) heads).putStringArrayListExtra("useridList", (ArrayList<String>) userid));
                    finish();
                } else {
                    //社交高级版，已注册，男性用户，进入到挑选好友页面，点击右下角√按钮，弹出提示“选择少于九位”？理解错误，此“√”功能与右滑一样。
                    //toastShow("选择少于9位");
                }
                break;
        }
    }


    private void refushAddData() {
        Log.e("msg", "..........token...." + SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN));
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGENO, pageNo + "");
        map.put(Utils.PAGESIZE, "10");
        map.put(Utils.GENDER, "2");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());

        OkHttpUtils.post()
                .tag(mActivity)
                .url(ApiStores.base_url + "/home/homeRecommend")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("code").equals("200")) {

                                List<HomeRecommendBean> homeRecommendBean = JSON.parseArray(jsonObject.getString("data"), HomeRecommendBean.class);

                                if (homeRecommendBean != null && homeRecommendBean.size() > 0) {


                                    Iterator<HomeRecommendBean> iterator = homeRecommendBean.iterator();

                                    while (iterator.hasNext()) {
                                        if (userid.contains(iterator.next().getUserid())) {
                                            iterator.remove();
                                        }
                                    }


                                    adapter.addData(homeRecommendBean);

                                } else {

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
