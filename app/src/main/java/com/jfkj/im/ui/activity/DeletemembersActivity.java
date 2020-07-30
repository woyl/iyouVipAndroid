package com.jfkj.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jfkj.im.Bean.DeletemembersBean;
import com.jfkj.im.Bean.FriendBean;
import com.jfkj.im.Bean.PinyinComparator;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.adapter.DeletemembersAdapter;
import com.jfkj.im.adapter.SelectfriendAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Selectfriend.SelectfriendPresenter;
import com.jfkj.im.mvp.Selectfriend.SelectfriendView;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.DividerItemDecoration;
import com.tencent.imsdk.friendship.TIMFriend;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class DeletemembersActivity extends BaseActivity<SelectfriendPresenter> implements SelectfriendView, TextWatcher, View.OnClickListener {
    @BindView(R.id.title_left_tv)
    TextView title_left_tv;
    @BindView(R.id.title_center_tv)
    TextView title_center_tv;
    @BindView(R.id.title_right_tv)
    TextView title_right_tv;
    SelectfriendPresenter selectfriendPresenter;
    @BindView(R.id.contact_list)
    XRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    List<DeletemembersBean.DataBean.ArrayBean> list, selectresult;
    DeletemembersAdapter selectfriendAdapter;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    StringBuffer stringBuffer;
    Intent getIntent;
    String adddelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        selectresult = new ArrayList<>();
        SourceDateList = new ArrayList<>();
        pinyinComparator = new PinyinComparator();
        title_right_tv.setOnClickListener(this);
        title_left_tv.setOnClickListener(this);
        selectfriendPresenter = new SelectfriendPresenter(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        selectfriendAdapter = new DeletemembersAdapter(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(selectfriendAdapter);
        pinyinComparator = new PinyinComparator();
        stringBuffer = new StringBuffer();
        getIntent = getIntent();
        adddelete = getIntent.getStringExtra("adddelete");

        if (adddelete.equals("deletefriend")) {
            title_center_tv.setText("删除成员");
            title_right_tv.setText("删除");
            selectfriendPresenter.loadGroupMemberList(getIntent.getStringExtra(Utils.GROUPID), "");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("selectfriend");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_tv:
                if (adddelete.equals("deletefriend")) {
                    stringBuffer.delete(0, stringBuffer.length());
                    for (int j = 0; j < SourceDateList.size(); j++) {
                        if (SourceDateList.get(j).isIsselect()) {
                            for (int i = 0; i < SourceDateList.size(); i++) {
                                if (SourceDateList.get(i).isIsselect()) {
                                    stringBuffer.append(SourceDateList.get(i).getUserid() + ",");
                                }
                            }
                            stringBuffer.deleteCharAt(stringBuffer.toString().length() - 1);
                        }
                    }
                    if (stringBuffer.toString().length() > 0) {
                        if (Utils.netWork()) {
                            selectfriendPresenter.delGroupMember(stringBuffer.toString(), getIntent.getStringExtra(Utils.GROUPID));
                        } else {
                            toastShow(R.string.nonetwork);
                        }
                    } else {
                        toastShow("没有选中的");
                    }
                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectfriend;
    }

    @Override
    public SelectfriendPresenter createPresenter() {
        return selectfriendPresenter;
    }


    @Override
    public void Selectfriendsuccess(List<TIMFriend> responseBody) {

    }

    @Override
    public void Selectfriendfail(String s) {

    }

    @Override
    public void deletefriendsuccess(ResponseBody responseBody,String s) {

    }

    @Override
    public void deletefriendfail(String s) {

    }

    @Override
    public void invitesuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            Intent intent = new Intent(mActivity, GroupsetActivity.class);
            intent.putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID));
            sendBroadcast(intent);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invitefail(String s) {

    }

    @Override
    public void delGroupMembersuccess(ResponseBody responseBody) {

        try {
            String  string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if(jsonObject.getString("code").equals("200")){
                Intent intent = new Intent(mActivity, GroupsetActivity.class);
                intent.putExtra(Utils.GROUPID, getIntent.getStringExtra(Utils.GROUPID));
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void delGroupMemberfail(String s) {

    }

    @Override
    public void loadGroupMemberListSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if(jsonObject.getString("code").equals("200")){
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("array");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    SortModel sortModel = new SortModel();
                    sortModel.setName(jsonObject1.getString("nickName"));
                    sortModel.setUserid(jsonObject1.getLong("userId"));
                    sortModel.setHead_url(jsonObject1.getString("head"));
                    SourceDateList.add(sortModel);
                }
                SourceDateList = filledData(SourceDateList);
                Collections.sort(SourceDateList, pinyinComparator);
                selectfriendAdapter.setmData(SourceDateList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGroupMemberListfail(String responseBody) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            sortModel.setUserid(date.get(i).getUserid());
            sortModel.setHead_url(date.get(i).getHead_url());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "selectfriend":
                    if (stringBuffer.toString().length() > 0) {
                        title_right_tv.setAlpha(1.0f);
                        title_right_tv.setEnabled(true);
                    } else {
                        title_right_tv.setAlpha(0.5f);
                        title_right_tv.setEnabled(false);
                    }
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
