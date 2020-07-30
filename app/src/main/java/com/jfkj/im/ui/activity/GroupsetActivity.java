package com.jfkj.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jfkj.im.Bean.ClubmemberBean;
import com.jfkj.im.Bean.GroupsetBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.ClubmemberAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.loadGroupInfo.LoadGroupInfoPresenter;
import com.jfkj.im.mvp.loadGroupInfo.LoadGroupInfoView;
import com.jfkj.im.ui.dialog.Groupsetdialog;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class GroupsetActivity extends BaseActivity<LoadGroupInfoPresenter> implements LoadGroupInfoView, View.OnClickListener, OnItemClickListener {
    Groupsetdialog groupsetdialog;
    LoadGroupInfoPresenter presenter;
    Intent getintent;
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    TextView group_title_tv;
    TextView tv_group_name;
    TextView group_notice_tv;
    TextView tv_number_gif;
    TextView tv_time;
    TextView tv_exit;
    ImageView switch_open_iv;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    ClubmemberAdapter clubmemberAdapter;
    List<ClubmemberBean.DataBean.ArrayBean> arrayBeanList;
    String noDisturb;
    GroupsetBean.DataBean data;
    ImageView more_iv;
    RelativeLayout group_name_ly;
    RelativeLayout group_messaeg_ry;
    String groupid;
    RelativeLayout group_notice_ly;

    RelativeLayout number_gift_ly;

    RelativeLayout group_super_ly;
    RelativeLayout gift_time_ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullScreen(mActivity);
        }
        setAndroidNativeLightStatusBar(mActivity, true);
        groupsetdialog = new Groupsetdialog(this);
        presenter = new LoadGroupInfoPresenter(this);
        getintent = getIntent();
        arrayBeanList = new ArrayList<>();
        clubmemberAdapter = new ClubmemberAdapter();
        clubmemberAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        presenter.loadGroupMemberInfo(getintent.getStringExtra(Utils.GROUPID) + "");
        groupid = getintent.getStringExtra(Utils.GROUPID) + "";
        swiperecyclerview.setLayoutManager(new GridLayoutManager(mActivity, 5));
        View groupset_footview = LayoutInflater.from(mActivity).inflate(R.layout.groupset_footview, null);
        swiperecyclerview.addFooterView(groupset_footview);
        group_title_tv = groupset_footview.findViewById(R.id.group_title_tv);
        more_iv = groupset_footview.findViewById(R.id.more_iv);
        group_name_ly = groupset_footview.findViewById(R.id.group_name_ly);
        tv_group_name = groupset_footview.findViewById(R.id.tv_group_name);
        group_messaeg_ry = groupset_footview.findViewById(R.id.group_messaeg_ry);
        group_notice_ly = groupset_footview.findViewById(R.id.group_notice_ly);
        number_gift_ly = groupset_footview.findViewById(R.id.number_gift_ly);
        group_super_ly = groupset_footview.findViewById(R.id.group_super_ly);
        gift_time_ly = groupset_footview.findViewById(R.id.gift_time_ly);
        group_super_ly.setOnClickListener(this);
        gift_time_ly.setOnClickListener(this);
        group_notice_tv = groupset_footview.findViewById(R.id.group_notice_tv);
        tv_number_gif = groupset_footview.findViewById(R.id.tv_number_gif);
        tv_time = groupset_footview.findViewById(R.id.tv_time);
        switch_open_iv = groupset_footview.findViewById(R.id.switch_open_iv);
        tv_exit = groupset_footview.findViewById(R.id.tv_exit);
        tv_exit.setOnClickListener(this);
        group_messaeg_ry.setOnClickListener(this);
        swiperecyclerview.setOnItemClickListener(this);
        swiperecyclerview.setAdapter(clubmemberAdapter);
        title_left_tv.setBackgroundResource(R.drawable.back_black_iv);
        title_left_tv.setOnClickListener(this);
        title_center_tv.setText("俱乐部设置");
        title_center_tv.setTextColor(Color.parseColor("#000000"));
        title_center_tv.setTextSize(17);
        switch_open_iv.setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("groupdisbandedsuccessfully");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_groupset;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_open_iv:
                if (Utils.netWork()) {
                    if (noDisturb.equals("1")) {
                        noDisturb = "0";
                    } else {
                        noDisturb = "1";
                    }
                    presenter.updateGroup(getintent.getStringExtra(Utils.GROUPID), "", noDisturb, "", "", "", "", "");
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.tv_exit:
                if (Utils.netWork()) {
                    if (tv_exit.getText().toString().equals("解散俱乐部")) {//群主解散俱乐部

                    } else {
                        presenter.quitGroup(getintent.getStringExtra(Utils.GROUPID));
                    }
                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.group_messaeg_ry:
                Intent intent = new Intent(mActivity, ClubmessageActivity.class);
                intent.putExtra(Utils.GROUPID, data.getGroupId());
                startActivity(intent);
                break;
            case R.id.group_super_ly:
                Intent intentgroup_super = new Intent(mActivity, SuperActivity.class);
                intentgroup_super.putExtra(Utils.ISSUPER, data.getIsSuper());
                intentgroup_super.putExtra(Utils.GROUPID, data.getGroupId());
                startActivityForResult(intentgroup_super, 100);

                break;
            case R.id.gift_time_ly:
                if(tv_exit.getText().toString().equals("解散俱乐部")){
                    Intent intenttime = new Intent(mActivity, SelectpackettimeActivity.class);
                    intenttime.putExtra(Utils.UPDATE, true);
                    intenttime.putExtra(Utils.GROUPID, groupid);
                    startActivityForResult(intenttime,101);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                presenter.loadGroupMemberInfo(groupid);
                break;
            case 101:
                presenter.loadGroupMemberInfo(groupid);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayBeanList.clear();
        clubmemberAdapter.setNewData(arrayBeanList);
        presenter.LoadGroupInfo(getintent.getStringExtra(Utils.GROUPID));

    }

    @Override
    public LoadGroupInfoPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void LoadGroupInfoSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                GroupsetBean groupsetBean = JSON.parseObject(string, GroupsetBean.class);
                data = groupsetBean.getData();
                tv_group_name.setText(data.getGroupName());
                group_notice_tv.setText(data.getGroupNotice());

                group_title_tv.setText(data.getGroupName());
                tv_number_gif.setText("×" + data.getRedPacketNum() + "/人");

                if (data.getIsOwner() == 1) {
                    tv_exit.setText("解散俱乐部");
                    more_iv.setVisibility(View.VISIBLE);
                } else {
                    tv_exit.setText("退出俱乐部");
                    more_iv.setVisibility(View.GONE);
                    group_name_ly.setVisibility(View.GONE);
                    group_notice_ly.setVisibility(View.GONE);
                    group_super_ly.setVisibility(View.GONE);
                }
                presenter.loadGroupMemberList(getintent.getStringExtra(Utils.GROUPID), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void LoadGroupInfofail(String responseBody) {

    }

    @Override
    public void loadmenberSucess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                JSONObject dataobject = jsonObject.getJSONObject("data");
                JSONArray array = dataobject.getJSONArray("array");
                arrayBeanList.addAll(JSON.parseArray(array.toString(), ClubmemberBean.DataBean.ArrayBean.class));
                ClubmemberBean.DataBean.ArrayBean addarrayBean = new ClubmemberBean.DataBean.ArrayBean();
                addarrayBean.setAdddelete("addfriend");
                ClubmemberBean.DataBean.ArrayBean deletearrayBean = new ClubmemberBean.DataBean.ArrayBean();
                deletearrayBean.setAdddelete("deletefriend");
                if (tv_exit.getText().toString().equals("解散俱乐部")) {
                    arrayBeanList.add(addarrayBean);
                    arrayBeanList.add(deletearrayBean);
                }

                clubmemberAdapter.setNewData(arrayBeanList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadmenberfail(String string) {

    }

    @Override
    public void updateGroupSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                presenter.loadGroupMemberInfo(getintent.getStringExtra(Utils.GROUPID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateGroupfail(String responseBody) {

    }

    @Override
    public void quitGroupSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                Intent intent = new Intent("exitgroupsuccess");
                intent.putExtra(Utils.GROUPID, getintent.getStringExtra(Utils.GROUPID));
                sendBroadcast(intent);
                finish();
            } else {
                Intent intent = new Intent("exitgroupsuccess");
                intent.putExtra(Utils.GROUPID, getintent.getStringExtra(Utils.GROUPID));
                sendBroadcast(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGroupMemberInfo(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                tv_time.setText(jsonObject.getJSONObject("data").getInt("sendHour") + ":00");
            }
            if (jsonObject.getJSONObject("data").getInt("noDisturb") == 1) {
                switch_open_iv.setBackgroundResource(R.mipmap.switch_on_green);
                noDisturb = "1";
                SPUtils.getInstance(mActivity).put(Utils.APPID+groupid+Utils.NODISTURB,true);
            } else {
                SPUtils.getInstance(mActivity).put(Utils.APPID+groupid+Utils.NODISTURB,false);
                switch_open_iv.setBackgroundResource(R.mipmap.switch_off_white);
                noDisturb = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        ClubmemberBean.DataBean.ArrayBean arrayBean = arrayBeanList.get(adapterPosition);
        if (arrayBean.getAdddelete() != null) {
            if (arrayBean.getAdddelete().equals("addfriend")) {
                Intent intent = new Intent(mActivity, SelectfriendActivity.class);
                intent.putExtra("adddelete", arrayBean.getAdddelete());

                intent.putExtra(Utils.GROUPNAME, getintent.getStringExtra(Utils.GROUPNAME));
                intent.putExtra(Utils.GROUPID, data.getGroupId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(mActivity, DeletemembersActivity.class);
                intent.putExtra(Utils.GROUPID, data.getGroupId());
                intent.putExtra("adddelete", "deletefriend");
                startActivity(intent);
            }
            finish();
        } else {
            Intent intent = new Intent(mActivity, UserDetailActivity.class);
            intent.putExtra("userId", arrayBean.getUserId());
            startActivity(intent);
            finish();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "groupdisbandedsuccessfully":
                    Intent exitgroupsuccessintent = new Intent("exitgroupsuccess");
                    exitgroupsuccessintent.putExtra(Utils.GROUPID, getintent.getStringExtra(Utils.GROUPID));
                    sendBroadcast(exitgroupsuccessintent);
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
