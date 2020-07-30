package com.jfkj.im.ui.discovery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.R;
import com.jfkj.im.adapter.RankListAdapter;
import com.jfkj.im.entity.RankListBean;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.discovery.CharmPresenter;
import com.jfkj.im.mvp.discovery.CharmView;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description: 魅力榜
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public class CharmFragment extends BaseFragment<CharmPresenter> implements CharmView {
    @BindView(R.id.iv_second_icon)
    ImageView mIvSecondIcon;
    @BindView(R.id.tv_second_name)
    TextView mTvSecondName;
    @BindView(R.id.tv_second_gift)
    TextView mTvSecondGift;
    @BindView(R.id.iv_third_icon)
    ImageView mIvThirdIcon;
    @BindView(R.id.tv_third_name)
    TextView mTvThirdName;
    @BindView(R.id.tv_third_gift)
    TextView mTvThirdGift;
    @BindView(R.id.layout_top)
    LinearLayout mLayoutTop;
    @BindView(R.id.iv_first_bg)
    ImageView mIvFirstBg;
    @BindView(R.id.iv_first_icon)
    ImageView mIvFirstIcon;
    @BindView(R.id.tv_first_rank)
    TextView mTvFirst;
    @BindView(R.id.tv_first_name)
    TextView mTvFirstName;
    @BindView(R.id.tv_first_gift)
    TextView mTvFirstGift;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_second_rank)
    TextView mSecondRank;
    @BindView(R.id.tv_third_rank)
    TextView mThirdRank;
    @BindView(R.id.layout_list_content)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;
    @BindView(R.id.view3)
    View mView3;
    @BindView(R.id.layout_third)
    LinearLayout mLayoutThird;
    private RankListAdapter mAdapter = null;
    private List<RankListBean> mRankList = new ArrayList<>();
    private String param;
    private String firstUserId;
    private String secondUserId;
    private String thirdUserId;

    public static CharmFragment getInstance(String param1, String param2) {
        CharmFragment fragment = new CharmFragment();
        Bundle args = new Bundle();
        args.putString(Utils.ARG_PARAM1, param1);
        args.putString(Utils.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected CharmPresenter createPresenter() {
        return new CharmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank_list;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            param = bundle.getString(Utils.ARG_PARAM1);
            mvpPresenter.getRankList(param);
        }
        mAdapter = new RankListAdapter(getContext(), mRankList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void showRankList(List<RankListBean> listBeans) {
        mConstraintLayout.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.GONE);

        if (listBeans.size() >= 3) {
            RankListBean firstBean = listBeans.get(0);
            Glide.with(getActivity()).load(firstBean.getHead()).transform(new CircleCrop()).into(mIvFirstIcon);
            mTvFirstName.setText(firstBean.getNickname());
            mTvFirst.setText("1");
            mTvFirstGift.setText(String.valueOf(firstBean.getMoney()));
            firstUserId = firstBean.getUserid();

            RankListBean secondBean = listBeans.get(1);
            Glide.with(getActivity()).load(secondBean.getHead()).transform(new CircleCrop()).into(mIvSecondIcon);
            mTvSecondName.setText(secondBean.getNickname());
            mSecondRank.setText("2");
            mTvSecondGift.setText(String.valueOf(secondBean.getMoney()));
            secondUserId = secondBean.getUserid();

            RankListBean thirdBean = listBeans.get(2);
            Glide.with(getActivity()).load(thirdBean.getHead()).transform(new CircleCrop()).into(mIvThirdIcon);
            mTvThirdName.setText(thirdBean.getNickname());
            mThirdRank.setText("3");
            mTvThirdGift.setText(String.valueOf(thirdBean.getMoney()));
            thirdUserId = thirdBean.getUserid();

            mRankList.clear();
            mRankList.addAll(listBeans.subList(3, listBeans.size()));
            mAdapter.notifyDataSetChanged();
            mLayoutTop.setVisibility(View.VISIBLE);
            mLayoutThird.setVisibility(View.VISIBLE);
            mView3.setVisibility(View.GONE);
        } else if (listBeans.size() == 2) {
            RankListBean firstBean = listBeans.get(0);
            Glide.with(getActivity()).load(firstBean.getHead()).transform(new CircleCrop()).into(mIvFirstIcon);
            mTvFirstName.setText(firstBean.getNickname());
            mTvFirstGift.setText(String.valueOf(firstBean.getMoney()));
            mTvFirst.setText("1");
            firstUserId = firstBean.getUserid();

            RankListBean secondBean = listBeans.get(1);
            Glide.with(getActivity()).load(secondBean.getHead()).transform(new CircleCrop()).into(mIvSecondIcon);
            mTvSecondName.setText(secondBean.getNickname());
            mTvSecondGift.setText(String.valueOf(secondBean.getMoney()));
            mSecondRank.setText("2");
            secondUserId = secondBean.getUserid();

            mRecyclerView.setVisibility(View.GONE);
            mLayoutTop.setVisibility(View.VISIBLE);
            mLayoutThird.setVisibility(View.GONE);
            mView3.setVisibility(View.VISIBLE);
        } else if (listBeans.size() == 1) {
            RankListBean firstBean = listBeans.get(0);
            Glide.with(getActivity()).load(firstBean.getHead()).transform(new CircleCrop()).into(mIvFirstIcon);
            mTvFirstName.setText(firstBean.getNickname());
            mTvFirstGift.setText(String.valueOf(firstBean.getMoney()));
            mRecyclerView.setVisibility(View.GONE);
            mTvFirst.setText("1");
            firstUserId = firstBean.getUserid();
            mLayoutTop.setVisibility(View.GONE);
        }

    }

    @Override
    public void showContributeList() {

    }

    @Override
    public void hideList() {
        mConstraintLayout.setVisibility(View.GONE);
        mTvNoData.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_second_icon, R.id.iv_third_icon, R.id.iv_first_icon})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_second_icon:
                bundle.putString("userId", firstUserId);
                JumpUtil.overlay(getContext(), UserDetailActivity.class, bundle);
                break;
            case R.id.iv_third_icon:
                bundle.putString("userId", secondUserId);
                JumpUtil.overlay(getContext(), UserDetailActivity.class, bundle);
                break;
            case R.id.iv_first_icon:
                bundle.putString("userId", thirdUserId);
                JumpUtil.overlay(getContext(), UserDetailActivity.class, bundle);
                break;
        }
    }
}
