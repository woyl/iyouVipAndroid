package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ClubmemberBean;
import com.jfkj.im.Bean.ClubmessageBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.ClubmemberAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.loadGroupInfo.LoadGroupInfoPresenter;
import com.jfkj.im.mvp.loadGroupInfo.LoadGroupInfoView;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;

public class ClubmessageActivity extends BaseActivity<LoadGroupInfoPresenter> implements View.OnClickListener, LoadGroupInfoView, OnItemClickListener {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.head_iv)
    CircleImageView head_iv;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_group_message)
    TextView tv_group_message;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_number)
    TextView tv_number;
    LoadGroupInfoPresenter loadGroupInfoPresenter;
    Intent getIntent;
    ClubmemberAdapter clubmemberAdapter;
    @BindView(R.id.tv_rank)
    TextView tv_rank;
    @BindView(R.id.gif_tv_number)
    TextView gif_tv_number;
    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    List<ClubmemberBean.DataBean.ArrayBean> arrayBeanList;
    ClubmessageBean.DataBean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayBeanList = new ArrayList<>();
        tv_add.setOnClickListener(this);
        clubmemberAdapter = new ClubmemberAdapter();
        swiperecyclerview.setLayoutManager(new GridLayoutManager(mActivity, 5));
        swiperecyclerview.setOnItemClickListener(this);
        swiperecyclerview.setAdapter(clubmemberAdapter);

        title_left_tv.setOnClickListener(this);
        loadGroupInfoPresenter = new LoadGroupInfoPresenter(this);
        getIntent = getIntent();

        loadGroupInfoPresenter.LoadGroupInfo(getIntent.getStringExtra(Utils.GROUPID));
        loadGroupInfoPresenter.loadGroupMemberList(getIntent.getStringExtra(Utils.GROUPID), "");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_groupmessage;
    }

    @Override
    public LoadGroupInfoPresenter createPresenter() {
        return loadGroupInfoPresenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.tv_add:
                 if(tv_add.getText().toString().equals("邀请好友加入")){
                     Intent intent = new Intent(mActivity, SelectfriendActivity.class);
                     intent.putExtra("adddelete", "addfriend");
                     intent.putExtra(Utils.GROUPNAME,data.getGroupName());
                     intent.putExtra(Utils.GROUPID, data.getGroupId());
                     startActivity(intent);
                     finish();
                 }else {
                     Intent intent = new Intent(mActivity, AdditiongroupActivity.class);
                     intent.putExtra(Utils.GROUPID, data.getGroupId());
                     startActivity(intent);
                     finish();
                 }
                break;
        }
    }

    @Override
    public void LoadGroupInfoSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {

                ClubmessageBean clubmessageBean = JSON.parseObject(string, ClubmessageBean.class);
                data = clubmessageBean.getData();
                Glide.with(mActivity).load(data.getGroupOwnerHead()).into(head_iv);
                tv_name.setText(data.getGroupOwnerName());
                if (data.getCity() != null) {
                    tv_city.setText(data.getCity());
                }
                tv_rank.setText("VIP" + data.getVipLevel());
                tv_number.setText(data.getRedPacketNum() + "");
                tv_group_message.setText(data.getGroupNotice());
                gif_tv_number.setText(data.getTotalNum() + "");
                if (data.getIsOwner() == 1) {
                    tv_add.setText("邀请好友加入");
                } else {
                    tv_add.setText("申请加入");
                    tv_add.setVisibility(View.GONE);
                }
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
                ClubmemberBean clubmemberBean = JSON.parseObject(string, ClubmemberBean.class);
                List<ClubmemberBean.DataBean.ArrayBean> array = clubmemberBean.getData().getArray();
                arrayBeanList.addAll(clubmemberBean.getData().getArray());
                if (array.size() == 10) {
                    loadGroupInfoPresenter.loadGroupMemberList(getIntent.getStringExtra(Utils.GROUPID), array.get(9).getSort());
                } else {
                    clubmemberAdapter.setNewData(arrayBeanList);

                }
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

    }

    @Override
    public void updateGroupfail(String responseBody) {

    }

    @Override
    public void quitGroupSuccess(ResponseBody responseBody) {

    }

    @Override
    public void loadGroupMemberInfo(ResponseBody responseBody) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        ClubmemberBean.DataBean.ArrayBean arrayBean = arrayBeanList.get(adapterPosition);
        Intent intent=new Intent(mActivity,UserDetailActivity.class);
        intent.putExtra("userId",arrayBean.getUserId());
        startActivity(intent);
        finish();
    }
}
