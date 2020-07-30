package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.Systemnotificationbean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.adapter.SystemnotificationAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.DividerItemDecoration;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
//type 接收类型 7官方系统通知  8群聊系统通知   9活动通知  10广场红包性格测试通知

public class SystemActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener, OnItemMenuClickListener, View.OnClickListener {
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swipeRecyclerView;
    SystemnotificationAdapter systemnotificationAdapter;
    List<Systemnotificationbean> linkedList;
    TIMConversation mCurrentConversation;
    @BindView(R.id.back_black_iv)
    ImageView back_black_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);

        linkedList = JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.SYSTEMID), Systemnotificationbean.class);
        systemnotificationAdapter = new SystemnotificationAdapter(mActivity);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRecyclerView.setOnItemMenuClickListener(this);
        swipeRecyclerView.setOnItemClickListener(this);
        back_black_iv.setOnClickListener(this);
        swipeRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeRecyclerView.setOnItemLongClickListener(this);
        swipeRecyclerView.setAdapter(systemnotificationAdapter);
        systemnotificationAdapter.setLinkedList(linkedList);
        mCurrentConversation = mCurrentConversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, Utils.SYSTEMID);
        mCurrentConversation.setReadMessage(mCurrentConversation.getLastMsg(), new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.SYSTEMID,JSON.toJSONString(linkedList));
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity)
                    .setBackgroundColor(getResources().getColor(R.color.color_FF5959))
                    .setText("删除") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(200)
                    .setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.

            // 上面的菜单哪边不要菜单就不要添加。
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {

    }

    @Override
    public void onItemLongClick(View view, int adapterPosition) {

    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {

        linkedList.remove(adapterPosition);
        systemnotificationAdapter.notifyDataSetChanged();
        menuBridge.closeMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_black_iv:
                finish();
                break;
        }
    }
}
