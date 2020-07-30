package com.jfkj.im.TIM.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.game.GameListBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.ChooseGameDialogFragment;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class AdventureActivity extends BaseActivity {

    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    private TabAdapter tabAdapter;
    private Context mContext;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    private ChatInfo chatInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        chatInfo = (ChatInfo) getIntent().getSerializableExtra("data");
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head);
//        tabAdapter = new TabAdapter(this);
//        viewpage2.setOffscreenPageLimit(2);
//        viewpage2.setUserInputEnabled(true);
//        viewpage2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                radioGroup.check(getCheckedId(position));
//            }
//        });
//        viewpage2.setAdapter(tabAdapter);
//        addOnclick();
        iniViews();
    }

    private void iniViews() {
        fragments.add(AdventureFragment.getInstance(chatInfo));
        fragments.add(TrueWordsFragment.getInstance(chatInfo));
        titles.add("大冒险");
        titles.add("真心话");
        viewpage.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        tab_layout.setupWithViewPager(viewpage);

        if (!SPUtils.getInstance(getApplicationContext()).getBoolean("gameIsFirst")) {
            SPUtils.getInstance(getApplicationContext()).put("gameIsFirst", true);
            ChooseGameDialogFragment gameDialogFragment = new ChooseGameDialogFragment(true, Gravity.CENTER);
            gameDialogFragment.show(getSupportFragmentManager(), "");
        }
    }




//    private void addOnclick() {
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.rb_home:
//                        viewpage2.setCurrentItem(TabAdapter.PAGE_HOME);
//                        view_1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.cEF4769));
//                        view_2.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent));
//                        break;
//                    case R.id.rb_classify:
//                        viewpage2.setCurrentItem(TabAdapter.PAGE_CLASS);
//                        view_1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent));
//                        view_2.setBackgroundColor(ContextCompat.getColor(mContext,R.color.cEF4769));
//                        break;
//                    default:break;
//                }
//            }
//        });
//    }

//    private int getCheckedId(int position) {
//        int checkedId = R.id.rb_home;
//        switch (position){
//            case 0 : {
//                checkedId = R.id.rb_home;
//                break;
//            }
//            case 1 : {
//                checkedId = R.id.rb_classify;
//                break;
//            }
//        }
//        return checkedId;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_adventure;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.img_back)
    void OnClick() {
        finish();
    }
}
