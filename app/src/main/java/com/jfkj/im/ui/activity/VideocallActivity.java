package com.jfkj.im.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.Utils;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class VideocallActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.circleimageview)
    CircleImageView circleImageView;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.btn_refuse)
    ImageButton btn_refuse;
    @BindView(R.id.btn_accept)
    ImageButton btn_accept;
    @BindView(R.id.tv_refuse)
    TextView tv_refuse;
    @BindView(R.id.tv_accept)
    TextView tv_accept;
    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_refuse.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        tv_accept.setOnClickListener(this);
        tv_refuse.setOnClickListener(this);
        getIntent = getIntent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("canclevideocall");
        registerReceiver(broadcastReceiver, intentFilter);
        Glide.with(mActivity).load(getIntent.getStringExtra(Utils.CHAT_HEAD_URL)).into(circleImageView);
        tv_user_name.setText(getIntent.getStringExtra(Utils.SEND_NICK_NAME));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_videocall;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refuse:
                if (Utils.netWork()) {

                    refuse();
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.btn_accept:
                if (Utils.netWork()) {
                    PermissionGen.needPermission(this, 100, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.tv_accept:
                if (Utils.netWork()) {
                    PermissionGen.needPermission(this, 100, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.tv_refuse:
                if (Utils.netWork()) {
                    ExecutorServiceUtils.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    refuse();
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
        }
        finish();
    }

    public void refuse() {

    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
       // Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT).show();


    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "canclevideocall":

                    Intent videocall_typeintent = new Intent(mActivity,ChatActivity.class);
                    videocall_typeintent.putExtra(Utils.VIDEOCALL_TYPE,"对方已经取消");
                    videocall_typeintent.putExtra(Utils.NICKNAME,getIntent.getStringExtra(Utils.NICKNAME));
                    videocall_typeintent.putExtra(Utils.CHAT_HEAD_URL,getIntent.getStringExtra(Utils.CHAT_HEAD_URL));
                    videocall_typeintent.putExtra( Utils.RECEIVEID,getIntent.getStringExtra(Utils.USERID));
                    startActivity(videocall_typeintent);
                    finish();
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
