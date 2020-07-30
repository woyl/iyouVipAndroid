package com.jfkj.im.TIM.redpack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import androidx.fragment.app.FragmentManager;


public class RedPackageReceiver extends BroadcastReceiver {

    private Context mContext;
    private FragmentManager fragmentManager;

    public RedPackageReceiver(Context mContext, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        RedPackageDialogFragment dialog = new RedPackageDialogFragment(true, Gravity.CENTER);
//        dialog.show(fragmentManager,"");
    }
}
