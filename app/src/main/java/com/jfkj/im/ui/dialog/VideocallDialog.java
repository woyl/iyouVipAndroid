package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.utils.GlideUtils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideocallDialog extends Dialog implements View.OnClickListener {
    View view;
    CircleImageView circleImageView;
    TextView tv_refuse;
    ImageButton btn_refuse;
    ImageButton btn_accept;
    TextView tv_accept;
    TextView tv_user_name;
    Context context;
    OnClick onClick;
    public VideocallDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.context=context;
    }
    public interface  OnClick{
        public void onclickrefuse();
        public void onclickaccject();
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_videocall);
        circleImageView=findViewById(R.id.circleimageview);
        tv_refuse=findViewById(R.id.tv_refuse);
        btn_refuse=findViewById(R.id.btn_refuse);
        btn_accept=findViewById(R.id.btn_accept);
        tv_accept=findViewById(R.id.tv_accept);
        tv_user_name=findViewById(R.id.tv_user_name);
        circleImageView=findViewById(R.id.circleimageview);

        tv_refuse.setOnClickListener(this);
        btn_refuse.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        tv_accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_refuse:
                dismiss();
                onClick.onclickrefuse();
                break;
            case R.id.btn_refuse:
                dismiss();
                onClick.onclickrefuse();
                break;
            case R.id.btn_accept:
                dismiss();
                onClick.onclickaccject();
                break;
            case R.id.tv_accept:
                dismiss();
                onClick.onclickaccject();
                break;

        }
    }
    public void setUser(String userid){
        List<String> list = new ArrayList<>();
        list.add(userid);
        TIMFriendshipManager.getInstance().getUsersProfile(list, true, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                TIMUserProfile timUserProfile = timUserProfiles.get(0);
                GlideUtils.loadChatImage(getContext(),timUserProfile.getFaceUrl(),circleImageView);
                tv_user_name.setText(timUserProfile.getNickName());
            }
        });
    }
    public boolean showSystemDialog() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                ToastUtil.toastLongMessage("请打开设置“允许显示在其他应用的上层”选项");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return false;
            } else {
                // Android6.0以上
                if (!isShowing()) {
                   show();
                    return true;
                }
            }
        } else {
            // Android6.0以下，不用动态声明权限
            if (!isShowing()) {
                show();
                return true;
            }
        }
        return false;
    }

}
