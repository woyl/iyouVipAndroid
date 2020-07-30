package com.jfkj.im.mvp.Interaction;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.chat.GroupRecentMessageBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import java.util.ArrayList;
import java.util.List;


public class InteractionPresenter extends BasePresenter<InteractionView> {

    public InteractionPresenter(InteractionView view){
        attachView(view);
    }

}
