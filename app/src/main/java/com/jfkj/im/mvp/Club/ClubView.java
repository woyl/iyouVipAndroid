package com.jfkj.im.mvp.Club;

import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.chat.GroupRecentMessageBean;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

import okhttp3.ResponseBody;

public interface ClubView extends BaseView {

    public void loadClubSuccess(ResponseBody responseBody);

    public void loadClubfail(String responseBody);



    public void messagerecord(List<InteractionBean> responseBody);



}

