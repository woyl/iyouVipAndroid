package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.FriendlyBean;
import com.jfkj.im.Bean.GroupsetBean;
import com.jfkj.im.Bean.PinyinComparator;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;

import com.jfkj.im.TIM.modules.conversation.ConversationProvider;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.utils.SharedPreferenceUtils;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.zhf.ConversationManagerKit;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.updateFriend.UpdateFriendView;
import com.jfkj.im.mvp.updateFriend.UpdateFriendpresenter;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class ChatsetActivity extends BaseActivity implements UpdateFriendView, View.OnClickListener {

    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.switch_open_iv)
    ImageView switch_open_iv;
    @BindView(R.id.msg_switch_open_iv)
    ImageView msg_switch_open_iv;
    Intent getintent;
    String top = "0";
    String nodisturb = "";

    private ChatInfo mChatInfo;
    private List<FriendlyBean.DataBean.FriendArrayBean> SourceDateList;
    private PinyinComparator pinyinComparator;
    Intent intent;
    ConversationMessage conversationMessage;
    private final static String SP_NAME = "top_conversion_list";
    private final static String TOP_LIST = "top_list";
    private final String SP_IMAGE = "conversation_group_face";
    private ConversationProvider mProvider;

    private SharedPreferences mConversationPreferences;
    public LinkedList<ConversationInfo> mTopLinkedList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();
        intent.setAction("conversationtop");
        switch_open_iv.setOnClickListener(this);
        msg_switch_open_iv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        title_center_tv.setText("聊天设置");
        title_center_tv.setTextColor(ContextCompat.getColor(mActivity,R.color.white));
        title_left_tv.setBackgroundResource(R.mipmap.iv_back_black);
        getintent = getIntent();
        mChatInfo = (ChatInfo) getintent.getSerializableExtra(TUIKitConstants.ProfileType.CONTENT);
        setAndroidNativeLightStatusBar(this,false);
        conversationMessage = getintent.getParcelableExtra(Utils.CONVERSATIONMESSAGE);
        mProvider = new ConversationProvider();
        mConversationPreferences = TUIKit.getAppContext().getSharedPreferences(TIMManager.getInstance().getLoginUser() + SP_NAME, Context.MODE_PRIVATE);
        mTopLinkedList = SharedPreferenceUtils.getListData(mConversationPreferences, TOP_LIST, ConversationInfo.class);
        if (ConversationManagerKit.getInstance().isTopConversation(mChatInfo.getId())) {
            switch_open_iv.setBackgroundResource(R.mipmap.switch_on_green);

        } else {
            switch_open_iv.setBackgroundResource(R.mipmap.switch_off_white);

        }
        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + mChatInfo.getId() + Utils.NODISTURB, "closenodisturb").equals("closenodisturb")) {
            msg_switch_open_iv.setBackgroundResource(R.mipmap.switch_off_white);
        } else {
            msg_switch_open_iv.setBackgroundResource(R.mipmap.switch_on_green);
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chatset;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_open_iv:
                if (Utils.netWork()) {
                    if (ConversationManagerKit.getInstance().isTopConversation(mChatInfo.getId())) {
                        ConversationManagerKit.getInstance().setConversationTop(mChatInfo.getId(), false);

                        switch_open_iv.setBackgroundResource(R.mipmap.switch_off_white);
                    } else {
                        ConversationManagerKit.getInstance().setConversationTop(mChatInfo.getId(), true);

                        switch_open_iv.setBackgroundResource(R.mipmap.switch_on_green);
                    }
                   // sendBroadcast(intent);
                }

                break;
            case R.id.msg_switch_open_iv:

                if (Utils.netWork()) {
                    if (nodisturb.equals("1")) {
                        nodisturb = "0";
                        SPUtils.getInstance(mActivity).put(Utils.APPID + mChatInfo.getId() + Utils.NODISTURB, "closenodisturb");
                        msg_switch_open_iv.setBackgroundResource(R.mipmap.switch_off_white);
                    } else {
                        nodisturb = "1";
                        msg_switch_open_iv.setBackgroundResource(R.mipmap.switch_on_green);
                        SPUtils.getInstance(mActivity).put(Utils.APPID + mChatInfo.getId() + Utils.NODISTURB, "opennodisturb");

                    }
                    //  sendBroadcast(intent);
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
