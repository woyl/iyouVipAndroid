package com.jfkj.im.ui.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.App;
import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.adapter.MyViewPagerAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Home.HomePresenter;
import com.jfkj.im.mvp.Home.HomeView;
import com.jfkj.im.service.FloatViewService;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.FilterDialog;
import com.jfkj.im.ui.dialog.TodayStarDialogFragment;
import com.jfkj.im.ui.fragment.HomeFragment;
import com.jfkj.im.ui.fragment.NewCustomerFragment2;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.TimeChange;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.LazyHomeChatNoticeView;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;

import static android.view.View.VISIBLE;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/19
 * </pre>
 */
public class LazyHomeFragment extends BaseFragment<HomePresenter> implements HomeView, TabLayout.BaseOnTabSelectedListener, View.OnClickListener {
    static HomeFragment homeFragment;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.iv_filter)
    AppCompatImageView mIvFilter;
    HomePresenter homePresenter;
//    @BindView(R.id.squre_ly)
//    LinearLayout squre_ly;

//    @BindView(R.id.head_iv)
//    CircleImageView head_iv;

//    @BindView(R.id.tv_message)
//    TextView tv_message;

    @BindView(R.id.ll_layout)
    LinearLayout linearLayout;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private NearByFragment2 nearFragment;
    private RecommendFragment2 recommendFragment;
    private NewCustomerFragment2 newCustomerFragment;

    private FilterDialog filterDialog;
    private List<String> titles = new ArrayList<>();
    private CommonDialog locationDialog;
    private String sexType = "0";
    private MyViewPagerAdapter viewPagerAdapter;
    private boolean isFirst = true;
    private boolean isChecked = true;
    private String type = "0";

    public static LazyHomeFragment getInstance(String param1, String param2) {
        LazyHomeFragment fragment = new LazyHomeFragment();
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


    @OnClick({R.id.tv_chong})
    public void onClickTwo(View view) {
        switch (view.getId()) {
            case R.id.tv_chong:
                if (Utils.netWork()) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    toastShow(R.string.nonetwork);
                    linearLayout.setVisibility(VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.netWork()) {
            linearLayout.setVisibility(View.GONE);
        } else {
            toastShow(R.string.nonetwork);
            linearLayout.setVisibility(VISIBLE);
        }
    }

    @SuppressLint({"HandlerLeak", "CutPasteId"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(), false);
        homePresenter = new HomePresenter(this);
        homePresenter.accountBalance();

        String gender = UserInfoManger.getGender();
        OkLogger.e(gender);

        //2女性 1 男性
        if ("1".equals(UserInfoManger.getGender())) {
            type = "2";
        } else if ("2".equals(UserInfoManger.getGender())) {
            type = "1";
        } else {
            type = "0";
        }
        Log.d("@@@", "这儿是type的值 === " + type);
        initViewPager();
        mIvFilter.setOnClickListener(this);

        String currDay = TimeChange.getCurrDay();

        if (TextUtils.isEmpty(SPUtils.getInstance(getActivity()).getString(Utils.APPID+Utils.OLD_DAY))) {
            SPUtils.getInstance(getActivity()).put(Utils.APPID+Utils.OLD_DAY, currDay);
            homePresenter.getTodayStar();
        } else if (!SPUtils.getInstance(getActivity()).getString(Utils.APPID+Utils.OLD_DAY).equals(currDay)) {
            SPUtils.getInstance(getActivity()).put(Utils.APPID+Utils.OLD_DAY, currDay);
            homePresenter.getTodayStar();
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private void initViewPager() {

        titles.add("附近");
        titles.add("活跃");
        titles.add("新人");

        nearFragment = NearByFragment2.getInstance(type, null);
        recommendFragment = RecommendFragment2.getInstance(type, null);
        newCustomerFragment = NewCustomerFragment2.getInstance(type, null);
        mFragments.add(nearFragment);
        mFragments.add(recommendFragment);
        mFragments.add(newCustomerFragment);

        viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.getTabAt(1).select();
        // squre_ly.setVisibility(View.GONE);
//        squre_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendMessage(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID));
//            }
//        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (nearFragment != null) {
                        nearFragment.onResume();
                    }
                    if (recommendFragment != null) {
                        recommendFragment.onPause();
                    }
                    if (newCustomerFragment != null) {
                        newCustomerFragment.onPause();
                    }
                } else if (position == 1) {
                    if (nearFragment != null) {
                        nearFragment.onPause();
                    }
                    if (newCustomerFragment != null) {
                        newCustomerFragment.onPause();
                    }

                    if (recommendFragment != null) {
                        recommendFragment.onResume();
                    }
                } else if (position == 2) {
                    if (nearFragment != null) {
                        nearFragment.onPause();
                    }
                    if (recommendFragment != null) {
                        recommendFragment.onPause();
                    }
                    if (newCustomerFragment != null) {
                        newCustomerFragment.onResume();
                    }
                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**
         *  重新设置tablayout的点击事件 进行点击刷新
         */
        overrideTabClick();


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在这里可以设置选中状态下  tab字体显示样式

                View view = tab.getCustomView();
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(18);
                    ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(18);
                    ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.CB3ffffff));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void overrideTabClick() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab == null) {
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
            Log.d("@@@", "点击需要刷新页面了兄弟" + pos);

            if (pos == 0) {
                if (nearFragment != null) {
                    nearFragment.onRefresh();
                }
            } else if (pos == 1) {
                if (recommendFragment != null) {
                    recommendFragment.onRefresh();
                }
            } else if (pos == 2) {
                if (newCustomerFragment != null) {
                    newCustomerFragment.onRefresh();
                }
            } else {
                Log.d("@@@", "没有else的 我也不知道为啥会走到这个地方");
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
        if (todayStarBean != null && todayStarBean.size() > 0) {
            TodayStarDialogFragment dialogFragment = new TodayStarDialogFragment(true, Gravity.CENTER, todayStarBean);
            if (getFragmentManager() != null) {
                dialogFragment.show(getFragmentManager(), "");
            }

        }
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("receiver_success");
        intentFilter.addAction("unline_squre_message");
        intentFilter.addAction("removefriend");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
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

    @Override
    public void onPause() {
        super.onPause();
//        Jzvd.releaseAllVideos();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isChecked) {
            Log.d("@@@", "首页主界面 被选中但是  可见");
            if (nearFragment != null) {
                nearFragment.onResume();
            }
            if (recommendFragment != null) {
                recommendFragment.onResume();
            }
            if (newCustomerFragment != null) {
                newCustomerFragment.onResume();
            }
//                    getLastMessage();
        } else {
            Log.d("@@@", "首页主界面 没选中 全暂停起来");
            if (nearFragment != null) {
                nearFragment.onPause();
            }
            if (recommendFragment != null) {
                recommendFragment.onPause();
            }
            if (newCustomerFragment != null) {
                newCustomerFragment.onPause();
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "receiver_success":
                    if (!intent.getBooleanExtra(Utils.ISPRIVATEFRIEND, false)) {
                        if (intent.getStringExtra(Utils.GROUPID).equals("0")) {
//                            Glide.with(mActivity).load(intent.getStringExtra(Utils.SEND_HEAD_URL)).into(head_iv);
//                            tv_message.setText(intent.getStringExtra(Utils.CONTENT));
                        }
                    }
                    break;
                case "unline_squre_message":
                    if (intent != null) {
                        String stringExtra = intent.getStringExtra(Utils.GROUPID);

                        if (stringExtra.equals("0")) {
//                            Glide.with(mActivity).load(intent.getStringExtra(Utils.SEND_HEAD_URL)).into(head_iv);
//                            tv_message.setText(intent.getStringExtra(Utils.CONTENT));
                        }
                    }
                    break;
                case "removefriend":
                    viewPagerAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
//        if (!PermissionsCheckUtils.checkFloatPermission(getActivity())) {
//            if (mHandler != null) {
//                mHandler.removeCallbacks(runnable);
//            }
//        }
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    public void setIsCheckHome(boolean isCheckHome) {
        this.isChecked = isCheckHome;
        if (newCustomerFragment != null) {
            newCustomerFragment.setChecked(isChecked);
        }
        if (recommendFragment != null) {
            recommendFragment.setChecked(isChecked
            );
        }
        if (nearFragment != null) {
            nearFragment.setChecked(isChecked);
        }
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getCode() == Utils.SET_USER_DELETE_VIDEO || event.getCode() == Utils.SET_USER_UPLOAD_VIDEO) {
            if (nearFragment != null) {
                nearFragment.onRefresh();
            }
            if (recommendFragment != null) {
                recommendFragment.onRefresh();
            }
            if (newCustomerFragment != null) {
                newCustomerFragment.onRefresh();
            }
        }
    }
}
