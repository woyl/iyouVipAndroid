package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.RankingBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.RankAdapter;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Rank.RankPresenter;
import com.jfkj.im.mvp.Rank.RankView;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class Rankfragment extends BaseFragment<RankPresenter> implements RankView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;


    private ImageView fl_img_middle, fl_img_left, fl_img_right;
    TextView tv_name1;
    TextView tv_number1;
    TextView tv_position;
    RankPresenter rankPresenterl;
    RankAdapter rankAdapter;
    View fragment_rank_head;
    List<RankingBean.DataBean> dataBeanList;
    int position;

    private int screenWith, screenHeiht, img_insideWith, img_insideHeight;
    private TextView tv_name1_middle, tv_number1_middle, tv_name1_right, tv_number1_right;

    @Override
    protected RankPresenter createPresenter() {
        return rankPresenterl;
    }

    public Rankfragment(int img_insideWith, int img_insideHeight) {
        this.img_insideWith = img_insideWith;
        this.img_insideHeight = img_insideHeight;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rankAdapter = new RankAdapter(mActivity);
        dataBeanList = new ArrayList<>();
        fragment_rank_head = LayoutInflater.from(mActivity).inflate(R.layout.fragment_rank_head, null);

        screenHeiht = ScreenUtil.getScreenHeight();
        screenWith = ScreenUtil.getScreenWidth();


        tv_name1 = fragment_rank_head.findViewById(R.id.tv_name1);
        tv_position = fragment_rank_head.findViewById(R.id.tv_position);
        tv_number1 = fragment_rank_head.findViewById(R.id.tv_number1);


        LinearLayout ll_inside_bg = fragment_rank_head.findViewById(R.id.ll_inside_bg);
        fl_img_left = fragment_rank_head.findViewById(R.id.fl_img_left);
        fl_img_middle = fragment_rank_head.findViewById(R.id.fl_img_middle);
        fl_img_right = fragment_rank_head.findViewById(R.id.fl_img_right);

        tv_name1_middle = fragment_rank_head.findViewById(R.id.tv_name1_middle);
        tv_number1_middle = fragment_rank_head.findViewById(R.id.tv_number1_middle);
        tv_name1_right = fragment_rank_head.findViewById(R.id.tv_name1_right);
        tv_number1_right = fragment_rank_head.findViewById(R.id.tv_number1_right);


        ConstraintLayout constraint_left = fragment_rank_head.findViewById(R.id.constraint_left);


        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fl_img_left.getLayoutParams();
        FrameLayout.LayoutParams layoutParamsRight = (FrameLayout.LayoutParams) fl_img_right.getLayoutParams();
        fl_img_left.post(new Runnable() {
            @Override
            public void run() {
                int w = fl_img_left.getWidth();
                layoutParams.height = w;
                layoutParamsRight.height = w;


                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ll_inside_bg.getLayoutParams();

                constraint_left.post(new Runnable() {
                    @Override
                    public void run() {
                        int h = constraint_left.getHeight() + w - ScreenUtil.dpToPx(10);
                        params.height = h;
                    }
                });
            }
        });

        FrameLayout.LayoutParams layoutParamsMiddle = (FrameLayout.LayoutParams) fl_img_middle.getLayoutParams();
        fl_img_middle.post(new Runnable() {
            @Override
            public void run() {
                int w = fl_img_middle.getWidth();
                layoutParamsMiddle.height = w;
            }
        });


        swiperecyclerview.addHeaderView(fragment_rank_head);
        swiperecyclerview.setLayoutManager(new GridLayoutManager(mActivity, 2));
//        if (swiperecyclerview.getItemDecorationCount() == 0){
//            swiperecyclerview.addItemDecoration(new GridSpacingItemDecoration(2,ScreenUtil.dpToPx(5),true));
//        }
        swiperecyclerview.setAdapter(rankAdapter);
        rankPresenterl = new RankPresenter(this);
        position = getArguments().getInt("position");
        rankPresenterl.Rank(position + "");
        swipeRefreshLayout.setOnRefreshListener(this);
        fl_img_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBeanList.size() > 0) {
                    RankingBean.DataBean dataBean = dataBeanList.get(0);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", dataBean.getUserid());
                    JumpUtil.overlay(getActivity(), UserDetailActivity.class, bundle);
                }
            }
        });
        fl_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBeanList.size() > 2) {
                    RankingBean.DataBean dataBean = dataBeanList.get(2);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", dataBean.getUserid());
                    JumpUtil.overlay(getActivity(), UserDetailActivity.class, bundle);
                }
            }
        });
        fl_img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBeanList.size() > 1) {
                    RankingBean.DataBean dataBean = dataBeanList.get(1);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", dataBean.getUserid());
                    JumpUtil.overlay(getActivity(), UserDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void RankSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);

            if (jsonObject.getString("code").equals("200")) {
                RankingBean rankingBean = JSON.parseObject(string, RankingBean.class);
                if (rankingBean.getData() == null) {
                    Glide.with(mActivity).load(R.mipmap.null_position).into(fl_img_right);
                    Glide.with(mActivity).load(R.mipmap.null_position).into(fl_img_middle);
                    Glide.with(mActivity).load(R.mipmap.null_position).into(fl_img_left);
                    tv_number1_middle.setVisibility(View.GONE);
                    tv_number1_right.setVisibility(View.GONE);
                    tv_number1.setVisibility(View.GONE);
                } else {
                    dataBeanList.addAll(rankingBean.getData());

                    if (dataBeanList.size() == 1) {
                        RankingBean.DataBean dataBean = dataBeanList.get(0);
                        Glide.with(mActivity).load(dataBean.getHead()).into(fl_img_middle);
                        tv_name1_middle.setText(dataBean.getNickname());
                        tv_number1_middle.setText(dataBean.getMoney() + "");
                        tv_position.setText("#1");
                        Glide.with(mActivity).load(R.mipmap.null_position).into(fl_img_right);
                        Glide.with(mActivity).load(R.mipmap.null_position).into(fl_img_left);
                        tv_number1_right.setVisibility(View.GONE);
                        tv_number1.setVisibility(View.GONE);


                    } else if (dataBeanList.size() == 2) {

                        RankingBean.DataBean dataBean = dataBeanList.get(0);
                        Glide.with(mActivity).load(dataBean.getHead()).into(fl_img_middle);
                        tv_name1_middle.setText(dataBean.getNickname());
                        tv_number1_middle.setText(dataBean.getMoney() + "");
                        tv_position.setText("#1");

                        RankingBean.DataBean dataBean2 = dataBeanList.get(1);
                        Glide.with(mActivity).load(dataBean2.getHead()).into(fl_img_right);
                        tv_name1_right.setText(dataBean2.getNickname());
                        tv_number1_right.setText(dataBean2.getMoney() + "");
                        tv_position.setText("#1");

                        Glide.with(mActivity).load(R.mipmap.null_position).into(fl_img_left);
                        tv_number1.setVisibility(View.GONE);

                    } else if (dataBeanList.size() > 2) {
                        RankingBean.DataBean dataBean = dataBeanList.get(0);
                        Glide.with(mActivity).load(dataBean.getHead()).into(fl_img_middle);
                        tv_name1_middle.setText(dataBean.getNickname());
                        tv_number1_middle.setText(dataBean.getMoney() + "");

                        RankingBean.DataBean dataBean2 = dataBeanList.get(1);
                        Glide.with(mActivity).load(dataBean2.getHead()).into(fl_img_right);
                        tv_name1_right.setText(dataBean2.getNickname());
                        tv_number1_right.setText(dataBean2.getMoney() + "");

                        RankingBean.DataBean dataBean3 = dataBeanList.get(2);
                        Glide.with(mActivity).load(dataBean3.getHead()).into(fl_img_left);
                        tv_name1.setText(dataBean3.getNickname());
                        tv_number1.setText(dataBean3.getMoney() + "");


                        if (dataBeanList.size() > 1) {
                            List<RankingBean.DataBean> dataBeanList = rankingBean.getData();
                            dataBeanList.remove(0);
                            dataBeanList.remove(0);
                            dataBeanList.remove(0);
                            rankAdapter.setList(dataBeanList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        if (Utils.netWork()) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dataBeanList.clear();
                    rankPresenterl.Rank(position + "");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        } else {
            toastShow(R.string.nonetwork);
        }
    }
}
