package com.jfkj.im.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.activity.ClubnameActivity;
import com.jfkj.im.ui.activity.GroupActivity;
import com.jfkj.im.ui.activity.SelectfriendActivity;
import com.jfkj.im.utils.Utils;

public class ClubDialog extends Dialog implements View.OnClickListener {
    private TextView create_club_tv, add_club_tv, cancel_tv;

    private FragmentActivity fragmentActivity;

    public ClubDialog(@NonNull Context context, FragmentActivity fragmentActivity) {
        super(context, R.style.dialogstyle);
        this.fragmentActivity = fragmentActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_club);
        setCanceledOnTouchOutside(false);
        create_club_tv = findViewById(R.id.create_club_tv);
        add_club_tv = findViewById(R.id.add_club_tv);
        cancel_tv = findViewById(R.id.cancel_tv);
        create_club_tv.setOnClickListener(this);
        add_club_tv.setOnClickListener(this);
        cancel_tv.setOnClickListener(this);
//        if(Utils.ISSEX.equals("男")){
//            create_club_tv.setVisibility(View.VISIBLE);
//        }else {
//            create_club_tv.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_club_tv:
                if (UserInfoManger.getGender().equals("1")){
                    if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 3) {
                        jump();
                    }else {
                        VipSetGradeDialogFragment dialogFragment
                                = new VipSetGradeDialogFragment(true, Gravity.CENTER, "VIP等级达到3级以后，才可解锁创建俱乐部功能。","确认");
                        dialogFragment.setRsp(new ResponListener<Boolean>() {
                            @Override
                            public void Rsp(Boolean s) {
                                if (s) {

                                }
                            }
                        });
                        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "");
                    }
                }else if (UserInfoManger.getGender().equals("2")){
                    jump();
                }

                dismiss();
                break;
            case R.id.add_club_tv:
                getContext().startActivity(new Intent(App.getAppContext(), GroupActivity.class));
                dismiss();
                break;
            case R.id.cancel_tv:
                dismiss();
                break;
        }
    }

    private void jump() {
        Intent intent = new Intent(getContext(), ClubnameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type","club_dialog");
        intent.putExtra(TUIKitConstants.Selection.CONTENT,bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }
}
