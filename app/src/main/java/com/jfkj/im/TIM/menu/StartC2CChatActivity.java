package com.jfkj.im.TIM.menu;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.BaseTimActivity;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.contact.ContactItemBean;
import com.jfkj.im.TIM.modules.contact.ContactListView;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.ui.activity.ChatActivity;
import com.tencent.imsdk.TIMConversationType;


import java.util.ArrayList;
import java.util.List;

public class StartC2CChatActivity extends BaseTimActivity {

    private static final String TAG = StartC2CChatActivity.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private ContactListView mContactListView;
    private ContactItemBean mSelectedItem;
    private List<ContactItemBean> mContacts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_start_c2c_chat_activity);

        mTitleBar = findViewById(R.id.start_c2c_chat_title);
        mTitleBar.setTitle(getResources().getString(R.string.sure), TitleBarLayout.POSITION.RIGHT);
        mTitleBar.getRightTitle().setTextColor(getResources().getColor(R.color.title_bar_font_color));
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConversation();
            }
        });
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContactListView = findViewById(R.id.contact_list_view);
        mContactListView.setSingleSelectMode(true);
        mContactListView.loadDataSource(ContactListView.DataSource.FRIEND_LIST);
        mContactListView.setOnSelectChangeListener(new ContactListView.OnSelectChangedListener() {
            @Override
            public void onSelectChanged(ContactItemBean contact, boolean selected) {
                if (selected) {
                    if (mSelectedItem == contact) {
                        // 相同的Item，忽略
                    } else {
                        if (mSelectedItem != null) {
                            mSelectedItem.setSelected(false);
                        }
                        mSelectedItem = contact;
                    }
                } else {
                    if (mSelectedItem == contact) {
                        mSelectedItem.setSelected(false);
                    }
                }
            }
        });
    }

    public void startConversation() {
        if (mSelectedItem == null || !mSelectedItem.isSelected()) {
            ToastUtil.toastLongMessage("请选择聊天对象");
            return;
        }
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.C2C);
        chatInfo.setId(mSelectedItem.getId());
        chatInfo.setChatRoom(false);
        String chatName = mSelectedItem.getId();
        if (!TextUtils.isEmpty(mSelectedItem.getRemark())) {
            chatName = mSelectedItem.getRemark();
        } else if (!TextUtils.isEmpty(mSelectedItem.getNickname())) {
            chatName = mSelectedItem.getNickname();
        }
        chatInfo.setChatName(chatName);
        Intent intent = new Intent(App.getAppContext(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);

        finish();
    }
}
