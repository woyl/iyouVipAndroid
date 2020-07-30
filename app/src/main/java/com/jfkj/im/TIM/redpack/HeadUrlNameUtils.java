package com.jfkj.im.TIM.redpack;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

public class HeadUrlNameUtils {

    public static String[] getHeadName(){
        String[] strings = new String[2];
        TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().querySelfProfile();
        if (timUserProfile == null) {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    strings[0] = timUserProfile.getNickName();
                    strings[1] = timUserProfile.getFaceUrl();
                }
            });
        } else {
            strings[0] = timUserProfile.getNickName();
            strings[1] = timUserProfile.getFaceUrl();
        }
        return strings;
    }

}
