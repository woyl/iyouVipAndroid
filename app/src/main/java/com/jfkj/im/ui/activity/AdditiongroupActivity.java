package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.JsonUtils;
import com.google.gson.Gson;
import com.huawei.hms.utils.JsonUtil;
import com.jfkj.im.Bean.GroupcenterBean;
import com.jfkj.im.GroupMessagecenterBean;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.applyJoinGroup.ApplyJoinGroupView;
import com.jfkj.im.mvp.applyJoinGroup.ApplyJoinGrouppresenter;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.ByteLimitWatcher;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class AdditiongroupActivity extends BaseActivity<ApplyJoinGrouppresenter> implements View.OnClickListener, TextWatcher, ApplyJoinGroupView {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.send_tv)
    TextView send_tv;
    @BindView(R.id.club_et)
    EditText club_et;
    @BindView(R.id.tv_number)
    TextView tv_number;
    ApplyJoinGrouppresenter applyJoinGrouppresenter;
    Intent getIntent;
    List<GroupMessagecenterBean> list = new ArrayList<>();
    Gson gson = new Gson();
    String groupid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_left_tv.setBackgroundResource(R.drawable.back_black_iv);
        title_left_tv.setOnClickListener(this);
        send_tv.setOnClickListener(this);
        club_et.addTextChangedListener(this);
        club_et.addTextChangedListener(new ByteLimitWatcher(club_et,null,150));

        applyJoinGrouppresenter = new ApplyJoinGrouppresenter(this);
        getIntent = getIntent();
        groupid=getIntent.getStringExtra(Utils.GROUPID);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_additiongroup;
    }

    @Override
    public ApplyJoinGrouppresenter createPresenter() {
        return applyJoinGrouppresenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.send_tv:
                if (TextUtils.isEmpty(club_et.getText().toString().trim())) {
                    toastShow("理由不能为空");
                } else {
                    applyJoinGrouppresenter.getdata(groupid, club_et.getText().toString().trim());
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (club_et.getText().toString().trim().length() > 0) {
            send_tv.setEnabled(true);
            send_tv.setAlpha(1.0f);
            tv_number.setText(club_et.getText().toString().trim().getBytes().length+"/"+150);
        } else {
            send_tv.setEnabled(false);
            send_tv.setAlpha(0.5f);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void getApplyJoinGroupSuucess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));

            List<GroupcenterBean> groupcenterBeans = new ArrayList<>();
            GroupcenterBean.WaitpassageBean groupcenterbeanwaitpassagebean= new GroupcenterBean.WaitpassageBean();
            groupcenterbeanwaitpassagebean.setMasterhead(jsonObject.getJSONObject("data").getJSONObject("group").getString("groupHead"));
            groupcenterbeanwaitpassagebean.setMaster(SPUtils.getInstance(mActivity).getString(Utils.USER_NAME));
            groupcenterbeanwaitpassagebean.setGroupName(jsonObject.getJSONObject("data").getJSONObject("group").getString("groupName"));
            groupcenterbeanwaitpassagebean.setGroupId(jsonObject.getJSONObject("data").getJSONObject("group").getString("groupId"));
            groupcenterbeanwaitpassagebean.setChat1(club_et.getText().toString().trim());
            groupcenterbeanwaitpassagebean.setMasterId(Utils.APPID);
            groupcenterbeanwaitpassagebean.setMessage("申请加入"+jsonObject.getJSONObject("data").getJSONObject("group").getString("groupName"));
            GroupcenterBean groupcenterBean=new GroupcenterBean();
            groupcenterBean.setItemType(3);
            groupcenterBean.setWaitpassageBean(groupcenterbeanwaitpassagebean);

            groupcenterBeans.add(groupcenterBean);
            if (SPUtils.getInstance(mActivity).getString(Utils.APPID+Utils.ADD_GROUP) != null) {
                if (SPUtils.getInstance(mActivity).getString(Utils.APPID+Utils.ADD_GROUP).trim().length() > 0) {
                    groupcenterBeans.addAll(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID+Utils.ADD_GROUP), GroupcenterBean.class));
                }
            }
            SPUtils.getInstance(mActivity).put(Utils.APPID+Utils.ADD_GROUP, JSON.toJSONString(groupcenterBeans));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void getApplyJoinGroupfail(String s) {
    }
    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }
}
