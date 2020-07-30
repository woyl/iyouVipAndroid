package com.jfkj.im.TIM.modules.group.apply;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.contact.FriendProfileLayout;
import com.jfkj.im.TIM.utils.TUIKitConstants;

public class GroupApplyMemberActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_apply_member_activity);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);

        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
    }

}
