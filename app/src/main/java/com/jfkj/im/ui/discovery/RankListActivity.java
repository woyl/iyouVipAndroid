package com.jfkj.im.ui.discovery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.R;
import com.jfkj.im.adapter.MyViewPagerAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  排行榜list
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public class RankListActivity extends BaseActivity {
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPage)
    ViewPager mViewPage;
    private String[] titles = {"魅力榜","贡献榜"};
    private List<Fragment> mFragments = new ArrayList<>();
    List<String> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_rank_list;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        StatusBarUtil.setStatusBarDarkTheme(this,true);
        mTvTitle.setText("排行榜");
        initView();
    }

    private void initView() {
        list=new ArrayList<>();
        list.add("魅力榜");
        list.add("贡献榜");
        CharmFragment fragment = CharmFragment.getInstance("ml",null);
        CharmFragment contributionFragment = CharmFragment.getInstance("gx",null);
        mFragments.add(fragment);
        mFragments.add(contributionFragment);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),mFragments,list);
        mViewPage.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position ==0){
//                    StatusBarUtil.setStatusBarDarkTheme(RankListActivity.this,false);
                    mIvBg.setBackgroundResource(R.drawable.bg_find_list_purple);
                }else if(position ==1) {
                    mIvBg.setBackgroundResource(R.drawable.bg_find_list_red);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_title_right:
                //todo go search()
                break;
        }
    }
}
