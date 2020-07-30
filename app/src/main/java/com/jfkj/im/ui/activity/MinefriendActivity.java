package com.jfkj.im.ui.activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jfkj.im.Bean.FriendBean;
import com.jfkj.im.Bean.PinyinComparator;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.contact.ContactItemBean;
import com.jfkj.im.TIM.modules.contact.ContactLayout;
import com.jfkj.im.TIM.modules.contact.ContactListView;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.ThreadHelper;
import com.jfkj.im.adapter.MinefriendAdapter;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Selectfriend.SelectfriendPresenter;
import com.jfkj.im.mvp.Selectfriend.SelectfriendView;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMDelFriendType;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

import static android.view.View.GONE;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_BOTH;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_COME_IN;


public class MinefriendActivity extends BaseActivity<SelectfriendPresenter> implements SelectfriendView, View.OnClickListener {
    SelectfriendPresenter selectfriendPresenter;
    @BindView(R.id.title_left_tv)
    ImageView title_left_tv;
    @BindView(R.id.number_newfriend_tv)
    TextView number_newfriend_tv;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.ly_number_friend)
    LinearLayout ly_number_friend;
    LinearLayoutManager layoutManager;
    List<FriendBean.DataBean.FriendArrayBean> list, selectresult;
    private MinefriendAdapter adapter;
    private List<SortModel> sortModels;
    private PinyinComparator pinyinComparator;
    Dialog dialog;
    TextView tv_cancel, tv_enter;
    String uerid;

    @BindView(R.id.img_no_friend)
    ImageView img_no_friend;

    ContactLayout mContactLayout;
    private List<ContactItemBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        selectfriendPresenter = new SelectfriendPresenter(this);
        list = new ArrayList<>();
        sortModels = new ArrayList<>();
        selectresult = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MinefriendAdapter(this,swipeMenuCreator);

        adapter.setRsp(new ResponListener<String>() {
            @Override
            public void Rsp(String date) {
                TipsBaseDialogFragment tipsBaseDialogFragment
                        = new TipsBaseDialogFragment(true, Gravity.CENTER, "删除后您将不能再和ta聊天，是否确认删除？", "取消", "确认", true);
                tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            uerid = date;
                            deleFriend();
                        }
                    }
                });
                tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
            }
        });
        recyclerView.setAdapter(adapter);


        ly_number_friend.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        pinyinComparator = new PinyinComparator();
        initdialog();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("longuser");
        registerReceiver(broadcastReceiver, intentFilter);
        mContactLayout = new ContactLayout(this);
        mContactLayout.getContactListView().setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                Intent intent = new Intent(mActivity, FriendProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                startActivity(intent);
            }
        });


    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            SwipeMenuItem addItem = new SwipeMenuItem(mActivity)
//                    .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
//                    .setImage(R.mipmap.ic_action_add) // 图标。
                    .setWidth(100) // 宽度。
                    .setHeight(100); // 高度。
//            swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

            SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity)
                    .setBackgroundColor(ContextCompat.getColor(mActivity, R.color.read_dot_bg))
//                    .setImage(R.mipmap.ic_action_delete) // 图标。
                    .setText("删除") // 文字。
                    .setTextColor(ContextCompat.getColor(mActivity, R.color.white)) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(200)
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };


    private void assembleFriendListData(final List<TIMFriend> timFriends) {
        for (TIMFriend timFriend : timFriends) {
            ContactItemBean info = new ContactItemBean();
            info.covertTIMFriend(timFriend);
            mData.add(info);

        }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mData.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        img_no_friend.setVisibility(GONE);
                    } else {
                        recyclerView.setVisibility(GONE);
                        img_no_friend.setVisibility(View.VISIBLE);
                    }

                }
            });


        //在这里处理数据务
        for (int i = 0; i < mData.size(); i++) {

            SortModel sortModel = new SortModel();
            ContactItemBean contactItemBean = mData.get(i);
            sortModel.setHead_url(contactItemBean.getAvatarurl());
            sortModel.setUserid(Long.parseLong(contactItemBean.getId()));
            sortModel.setName(contactItemBean.getNickname());
            sortModel.setVipLevel((int) contactItemBean.getViplevel());
            sortModels.add(sortModel);
        }

        sortModels = filledData(sortModels);
        Collections.sort(sortModels, pinyinComparator);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setmData(sortModels);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        ThreadHelper.INST.execute(new Runnable() {
            @Override
            public void run() {
                list.clear();
                selectresult.clear();
                sortModels.clear();
                mData.clear();
                List<TIMFriend> timFriends = TIMFriendshipManager.getInstance().queryFriendList();
                for (TIMFriend friend : timFriends) {

                }
                assembleFriendListData(timFriends);

            }
        });
        TIMFriendPendencyRequest timFriendPendencyRequest = new TIMFriendPendencyRequest();
        timFriendPendencyRequest.setTimPendencyGetType(TIM_PENDENCY_BOTH);
        TIMFriendshipManager.getInstance().getPendencyList(timFriendPendencyRequest, new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int code, String desc) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TIMFriendPendencyResponse timFriendPendencyResponse) {
                List<TIMFriendPendencyItem> items = timFriendPendencyResponse.getItems();
                List<TIMFriendPendencyItem> timFriendPendencyItemList = new ArrayList<>();
                for (TIMFriendPendencyItem timFriendPendencyItem : items) {
                    if (!timFriendPendencyItem.getAddWording().equals("isTempy")) {
                        if (timFriendPendencyItem.getType() == TIM_PENDENCY_COME_IN) {
                            timFriendPendencyItemList.add(timFriendPendencyItem);
                        }
                    }
                }
                if (timFriendPendencyItemList.size() == 0) {
                    number_newfriend_tv.setVisibility(GONE);
                } else {
                    number_newfriend_tv.setVisibility(View.VISIBLE);
                    number_newfriend_tv.setText(timFriendPendencyItemList.size() + "");
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initdialog() {
        View dialogview = LayoutInflater.from(this).inflate(R.layout.dialog_delete, null);
        dialog = new Dialog(mActivity, R.style.dialogstyle);
        dialog.setContentView(dialogview);
        Window codedialogWindow = dialog.getWindow();
        WindowManager.LayoutParams attributes = codedialogWindow.getAttributes();
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.CENTER;
        codedialogWindow.setAttributes(attributes);
        tv_cancel = dialogview.findViewById(R.id.tv_cancel);
        tv_enter = dialogview.findViewById(R.id.tv_enter);
        tv_cancel.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullScreen(mActivity);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_minefriend;
    }

    @Override
    public SelectfriendPresenter createPresenter() {
        return selectfriendPresenter;
    }

    @Override
    public void Selectfriendsuccess(List<TIMFriend> timFriends) {

    }

    @Override
    public void Selectfriendfail(String s) {
    }

    @Override
    public void deletefriendsuccess(ResponseBody responseBody, String userIds) {
    }

    @Override
    public void deletefriendfail(String s) {

    }

    @Override
    public void invitesuccess(ResponseBody responseBody) {

    }

    @Override
    public void invitefail(String s) {

    }

    @Override
    public void delGroupMembersuccess(ResponseBody responseBody) {

    }

    @Override
    public void delGroupMemberfail(String s) {

    }

    @Override
    public void loadGroupMemberListSuccess(ResponseBody responseBody) {

    }

    @Override
    public void loadGroupMemberListfail(String responseBody) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            sortModel.setUserid(date.get(i).getUserid());
            sortModel.setHead_url(date.get(i).getHead_url());
            sortModel.setTop(date.get(i).getTop());
            sortModel.setNoDisturb(date.get(i).getNoDisturb());
            sortModel.setVipLevel(date.get(i).getVipLevel());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
            if (pinyin.length() > 0) {
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setLetters(sortString.toUpperCase());
                } else {
                    sortModel.setLetters("#");
                }
            }


            mSortList.add(sortModel);
        }

        return mSortList;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_number_friend:

                Intent intent_newfriend = new Intent(mActivity, NewfriendActivity.class);
                startActivity(intent_newfriend);
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_enter:
                TipsBaseDialogFragment tipsBaseDialogFragment
                        = new TipsBaseDialogFragment(true, Gravity.CENTER, "删除后您将不能再和ta聊天，是否确认删除？", "取消", "确认", true);
                tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            deleFriend();
                        }
                    }
                });
                tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.title_left_tv:

                finish();
                break;
        }
    }

    private void deleFriend() {
        if (Utils.netWork()) {
            List<String> list = new ArrayList<>();
            list.add(uerid);
            TIMFriendshipManager.getInstance().deleteFriends(list, TIMDelFriendType.TIM_FRIEND_DEL_BOTH, new TIMValueCallBack<List<TIMFriendResult>>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(List<TIMFriendResult> timFriendResults) {
                    sortModels.clear();
                    mData.clear();
                    ThreadHelper.INST.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<TIMFriend> timFriends = TIMFriendshipManager.getInstance().queryFriendList();

                            assembleFriendListData(timFriends);
                            if (dialog != null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                            boolean b = TIMManager.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C, uerid);
                            if (b) {
                                Intent intent = new Intent("removefriend");
                                intent.putExtra("removefriend", uerid);
                                sendBroadcast(intent);
                            } else {
                                toastShow("删除好友失败");
                            }
                        }
                    });
                }
            });
        } else {
            toastShow(R.string.nonetwork);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "longuser":
                    dialog.show();
                    uerid = intent.getStringExtra(Utils.RECEIVEID);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

}
