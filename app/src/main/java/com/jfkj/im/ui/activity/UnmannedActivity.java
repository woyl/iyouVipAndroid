package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupNewBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.group.info.GroupInfoActivity;
import com.jfkj.im.TIM.redpack.group.GroupAddOrDelMeberActivity;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.Utils;

import butterknife.BindView;

public class UnmannedActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_left_tv)
    ImageView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.title_right_tv)
    ImageView title_right_tv;
    @BindView(R.id.invite_tv)
    TextView invite_tv;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;

    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(ll_head);
        title_center_tv.setTextColor(Color.parseColor("#ffffff"));
        title_right_tv.setOnClickListener(this);
        invite_tv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        getIntent = getIntent();
        title_center_tv.setText(getIntent.getStringExtra(Utils.GROUPNAME));
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unmanned;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_tv:
//                Intent intent = new Intent(mActivity, SelectfriendActivity.class);
//                intent.putExtra("adddelete", "addfriend");
//                intent.putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID));
//                intent.putExtra("notice",getIntent.getStringExtra("notice"));
//                intent.putExtra("name",getIntent.getStringExtra("name"));
//                GroupNewBean groupNewBean = getIntent.getParcelableExtra("data");
//                intent.putExtra("data", groupNewBean);
//                startActivity(intent);
                startActivity(new Intent(this, GroupAddOrDelMeberActivity.class)
                        .putExtra("add", true)
                        .putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID)));
                finish();
                break;
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.title_right_tv:
//                Intent intentset = new Intent(mActivity, GroupsetActivity.class);
//                intentset.putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID));
//                startActivity(intentset);
                Intent intent = new Intent(mActivity, GroupInfoActivity.class);
                intent.putExtra(TUIKitConstants.Group.GROUP_ID, getIntent.getStringExtra(Utils.GROUPID));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
