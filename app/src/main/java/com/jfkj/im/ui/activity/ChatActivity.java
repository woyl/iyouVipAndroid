package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.textclassifier.ConversationAction;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.BaseTimActivity;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.utils.Constants;

import com.jfkj.im.ui.fragment.ChatFragment;
import com.jfkj.im.utils.DisplayUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseTimActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();
    TextView title_left_tv;
    TextView title_center_tv;
    ImageView title_right_tv;
    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;
    ConversationMessage conversationInfo;
    RelativeLayout mInputLayout;
    LinearLayout rLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        title_left_tv = findViewById(R.id.title_left_tv);
        title_center_tv = findViewById(R.id.title_center_tv);
        title_right_tv = findViewById(R.id.title_right_tv);
        chat(getIntent());
        mInputLayout = findViewById(R.id.empty_view);
        rLayout =  findViewById(R.id.root);

        StatusBarUtil.setStatusBarDarkTheme(this,false);
        SPUtils.getInstance(App.getAppContext()).put(Utils.VIP_UPDADT,true);


    }


    private void chat(Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            startSplashActivity();
        } else {
            mChatInfo = (ChatInfo) intent.getSerializableExtra(Constants.CHAT_INFO);

            conversationInfo = intent.getParcelableExtra(Utils.CONVERSATIONMESSAGE);
            if (mChatInfo == null) {
                startSplashActivity();
                return;
            }
            mChatFragment = new ChatFragment();
            bundle.putParcelable(Utils.CONVERSATIONMESSAGE, conversationInfo);
            bundle.putSerializable(Constants.CHAT_INFO, intent.getSerializableExtra(Constants.CHAT_INFO));
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        }
    }

    private void startSplashActivity() {
        Intent intent = new Intent(ChatActivity.this, Loginregister_phone_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        chat(getIntent());
    }
}
