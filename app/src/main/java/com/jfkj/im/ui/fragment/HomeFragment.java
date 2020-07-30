package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.MyViewPagerAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Home.HomePresenter;
import com.jfkj.im.mvp.Home.HomeView;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.FilterDialog;
import com.jfkj.im.ui.home.NearByFragment;
import com.jfkj.im.ui.home.RecommendFragment;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import okhttp3.ResponseBody;

import static android.view.View.VISIBLE;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView, TabLayout.BaseOnTabSelectedListener, View.OnClickListener {
    static HomeFragment homeFragment;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.iv_filter)
    AppCompatImageView mIvFilter;
    HomePresenter homePresenter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private NearByFragment nearFragment;
    private RecommendFragment recommendFragment;
    private NewCustomerFragment newCustomerFragment;

    private FilterDialog filterDialog;
    private List<String> titles = new ArrayList<>();
    private CommonDialog locationDialog;
    private String sexType = "0";
    private MyViewPagerAdapter viewPagerAdapter;
    private boolean isFirst = true;

    public static HomeFragment getInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(Utils.ARG_PARAM1, param1);
        args.putString(Utils.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected HomePresenter createPresenter() {
        return homePresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(),false);
        homePresenter = new HomePresenter(this);
        homePresenter.accountBalance();
        initViewPager();

        mIvFilter.setOnClickListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViewPager() {

        titles.add("附近");
        titles.add("活跃");
        titles.add("新人");

        nearFragment = NearByFragment.getInstance(UserInfoManger.getLatitude(), UserInfoManger.getLongitude());
        recommendFragment = RecommendFragment.getInstance(null, null);
        newCustomerFragment = NewCustomerFragment.getInstance(null, null);
        mFragments.add(nearFragment);
        mFragments.add(recommendFragment);
        mFragments.add(newCustomerFragment);

        viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.getTabAt(1).select();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**
         *  重新设置tablayout的点击事件 进行点击刷新
         */
        overrideTabClick();

    }

    private void overrideTabClick() {
        for (int i = 0 ;i < mTabLayout.getTabCount();i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab == null){
                return;
            }

            Class tabClass = tab.getClass();
            try {
                Field field = tabClass.getDeclaredField("view");
                field.setAccessible(true);
                View view = (View) field.get(tab);
                if (null == view) return;
                view.setTag(i);
                view.setOnClickListener(mTabOnClickListener);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (mViewPager.getVisibility() == View.INVISIBLE) {
                mViewPager.setVisibility(VISIBLE);
            }
            TabLayout.Tab tab = mTabLayout.getTabAt(pos);
            if (tab != null) {
                tab.select();
            }
            Log.d("@@@","点击需要刷新页面了兄弟" + pos);

            if (pos == 0 ){
                if (nearFragment != null){
                    nearFragment.onRefresh();
                }
            }else if (pos == 1){
                if (recommendFragment != null){
                    recommendFragment.onRefresh();
                }
            }else if (pos == 2){
                if (newCustomerFragment != null){
                    newCustomerFragment.onRefresh();
                }
            } else {
                Log.d("@@@","没有else的 我也不知道为啥会走到这个地方");
            }

        }
    };

    @Override
    public void getDataSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            JSONObject data = jsonObject.getJSONObject("data");
            SPUtils.getInstance(getContext()).put(Utils.USERID, data.getLong("userid") + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getDataFail(String msg) {
        Toast.makeText(getContext(), "没有网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void GetImSuccess(ResponseBody responseBody) {

    }

    @Override
    public void GetImfail(String s) {

    }

    @Override
    public void UserSigSuccess(ResponseBody responseBody) {

    }

    @Override
    public void UserSigfail(String s) {

    }

    @Override
    public void getgiftlist(String s) {

    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {

    }

    @Override
    public void loginOutSuccess(ResponseBody body) {

    }

    @Override
    public void showUpdateDialog(AppVersion version) {

    }

    @Override
    public void getTodayStar(List<TodayStarBean> todayStarBean) {

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (null == view) {
            tab.setCustomView(R.layout.layout_tab_text);
        }
        TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
        //        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setAlpha(1f);
        textView.setText(tab.getText());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        View view = tab.getCustomView();
        if (null == view) {
            tab.setCustomView(R.layout.layout_tab_text);
        }
        TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
        textView.setText(tab.getText());
        textView.setAlpha(0.7f);
        //        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        filterDialog = new FilterDialog(getContext(), R.style.dialogstyle);
        filterDialog.onListItem(new FilterDialog.onListItem() {
            @Override
            public void onItem(int i) {
                switch (i) {
                    case 0:
                        sexType = "1";
                        setFilter(sexType);
                        break;
                    case 1:
                        sexType = "2";
                        setFilter(sexType);
                        break;
                    case 2:
                        sexType = "0";
                        setFilter(sexType);
                        break;
                }
            }
        });
        filterDialog.show();
    }

    private void setFilter(String sexType) {
        nearFragment.setFilter(sexType);
        recommendFragment.setFilter(sexType);
        newCustomerFragment.setFilter(sexType);
    }
}
