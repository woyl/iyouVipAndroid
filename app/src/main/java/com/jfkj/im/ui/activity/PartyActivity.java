package com.jfkj.im.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.Bean.AllMyPartysRedBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.fragment.TabPartyAdapter;
import com.jfkj.im.event.PartyRedEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.party.PartyPresenter;
import com.jfkj.im.mvp.party.PartyView;
import com.jfkj.im.tab.TabLayoutMediator;
import com.jfkj.im.ui.party.ReleasePartyActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.TablayoutViewpagerUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.TipRadioButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class PartyActivity extends BaseActivity<PartyPresenter> implements PartyView {

    private PartyPresenter partyPresenter;

    //    @BindView(R.id.radio)
//    RadioGroup radioGroup;
    @BindView(R.id.deals_header_tab)
    TabLayout deals_header_tab;
    @BindView(R.id.view_pager2)
    ViewPager2 view_pager2;
    @BindView(R.id.constraint_head)
    FrameLayout constraint_head;
    @BindView(R.id.iv_title_back)
    AppCompatImageView iv_title_back;

    @BindView(R.id.radio_2)
    TipRadioButton radioButton;

//    @BindView(R.id.tv_red_point)
//    TextView tv_red_point;

    @BindView(R.id.view_red)
    View view_red;

    private TabPartyAdapter tabPartyAdapter;

    private boolean isRed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyPresenter = new PartyPresenter(this);

        partyPresenter.allMyPartysRedType();

        isRed = getIntent().getBooleanExtra("red",false);

        if (isRed) {
            view_red.setVisibility(View.VISIBLE);
        } else {
            view_red.setVisibility(View.GONE);
        }

        iniViews();
//        addLister();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upRed(PartyRedEvent event){
        if (event.isUpdate()) {
            view_red.setVisibility(View.GONE);
            SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.NEW_POINT, false);
        }
    }

    private void addLister() {
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radio_1:
//                        view_pager2.setCurrentItem(TabPartyAdapter.PAGE_HOME);
//                        break;
//                    case R.id.radio_2:
//                        view_pager2.setCurrentItem(TabPartyAdapter.PAGE_CLASS);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
    }

    private void iniViews() {
        tabPartyAdapter = new TabPartyAdapter(this);
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
//                radioGroup.check(getCheckedId(position));
                if (position == 0) {
                 //   setTextSize(true,deals_header_tab.getTabAt(position));
                } else {
                 //   setTextSize(false,deals_header_tab.getTabAt(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        view_pager2.setAdapter(tabPartyAdapter);



       new TablayoutViewpagerUtils().setViewpagerWithTablayout(mActivity,deals_header_tab,view_pager2,"聚会报名","我的聚会");



    }

//    public void setTextSize(boolean is,TabLayout.Tab tab){
//        if (is){
//            View view = tab.getCustomView();
//            if (view == null){
//                tab.setCustomView(R.layout.tab_layout);
//            }
//            TextView textView = tab.getCustomView().findViewById(R.id.tv_content);
//            textView.setText(tab.getText());
//            textView.setTextSize(18);
//            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
//        } else {
//            View view = tab.getCustomView();
//            if (view == null){
//                tab.setCustomView(R.layout.tab_layout);
//            }
//            TextView textView = tab.getCustomView().findViewById(R.id.tv_content);
//            textView.setText(tab.getText());
//            textView.setTextSize(13);
//            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.line_btn));
//        }
//    }

    private int getCheckedId(int position) {
        int checkedId = R.id.radio_1;
        switch (position) {
            case 0:
                checkedId = R.id.radio_1;
                break;
            case 1:
                checkedId = R.id.radio_2;
                break;
        }
        return checkedId;
    }

    @OnClick({R.id.iv_title_back, R.id.floatbutton})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.floatbutton:

                JumpUtil.overlay(this, ReleasePartyActivity.class);

                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party;
    }

    @Override
    public PartyPresenter createPresenter() {
        return partyPresenter;
    }

    @Override
    public void PartySuccess(BaseBeans<PartyBean> partyBeanBaseBeans) {

    }

    @Override
    public void PartyFails(String error) {

    }

    /**
     * 是否显示小红点
     *
     * @param allMyPartysRedBean
     */
    @Override
    public void myPartysRedTypeSuccess(AllMyPartysRedBean allMyPartysRedBean) {
        try {
            if(allMyPartysRedBean!=null)
                if (allMyPartysRedBean.getData().getRedType().equals("1")) {
                    // tv_red_point.setVisibility(View.VISIBLE);
                    radioButton.setTipOn(true);
                } else {
                    // tv_red_point.setVisibility(View.GONE);
                    radioButton.setTipOn(false);
                }
        }catch (Exception e){
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
