package com.jfkj.im.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jfkj.im.Bean.GroupBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.GroupAdapter;
import com.jfkj.im.adapter.GroupheadAdapter;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.LoadUserGroupList.LoadUserGroupListPresenter;
import com.jfkj.im.mvp.LoadUserGroupList.LoadUserGroupListView;
import com.jfkj.im.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class Groupfragment extends BaseFragment<LoadUserGroupListPresenter> implements LoadUserGroupListView, XRecyclerView.LoadingListener {
    GroupheadAdapter groupheadAdapter;
    LoadUserGroupListPresenter presenter;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    int pageNo = 1;
    String position;
    String sort;
    RecyclerView head_xrecyclerview;
    List<GroupBean.DataBean.ArrayBean> list;
    List<GroupBean.DataBean.ArrayBean> list_head;
    GroupAdapter groupAdapter;

    public static Groupfragment getInstance(Bundle bundle) {
        Groupfragment richestfragment = new Groupfragment();
        richestfragment.setArguments(bundle);
        return richestfragment;
    }

    @Override
    protected LoadUserGroupListPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View fragment_group_head = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_group_head, null);
        head_xrecyclerview = fragment_group_head.findViewById(R.id.head_xrecyclerview);
        presenter = new LoadUserGroupListPresenter(this);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        head_xrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        list_head = new ArrayList<>();
        position = getArguments().getString("position");
        presenter.getdata(pageNo + "", "", position, "");
        groupAdapter = new GroupAdapter(getActivity(),"Groupfragment");
        groupheadAdapter = new GroupheadAdapter(getActivity());
        head_xrecyclerview.setAdapter(groupheadAdapter);
        xrecyclerview.setAdapter(groupAdapter);
        xrecyclerview.setLoadingListener(this);
        xrecyclerview.addHeaderView(fragment_group_head);
    }

    @Override
    public void LoadUserGroupSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.getString("code").equals("200")) {
                list.addAll(JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("array").toString(), GroupBean.DataBean.ArrayBean.class));
                List<GroupBean.DataBean.ArrayBean> arrayBeans = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("array").toString(), GroupBean.DataBean.ArrayBean.class);
                if (arrayBeans.size() > 0) {
                    sort = arrayBeans.get(arrayBeans.size() - 1).getSort();
                }
                if (arrayBeans.size() == 0) {
                    xrecyclerview.loadMoreComplete();
                    return;
                }
                if (pageNo == 1) {//当首页是第一的时候 头部才需要添加数据
                    if (list.size() == 1) {
                        list_head.add(0, list.get(0));
                        list.remove(0);
                    }
                    if (list.size() == 2) {
                        list_head.add(0, list.get(0));
                        list_head.add(1, list.get(1));
                        list.remove(0);
                        list.remove(0);
                    }
                    if (list.size() >= 3) {
                        list_head.add(0, list.get(0));
                        list_head.add(1, list.get(1));
                        list_head.add(2, list.get(2));
                        list.remove(0);
                        list.remove(0);
                        list.remove(0);
                    }
                }
                groupAdapter.setList(list);
                groupheadAdapter.setList(list_head);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void LoadUserGroupSuccessfail(String s) {

    }

    @Override
    public void showLoading() {
        if(progressDialog !=null  && !progressDialog.isShowing()){
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();

    }

    @Override
    public void onRefresh() {
        xrecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.netWork()) {
                    list.clear();
                    list_head.clear();
                    pageNo = 1;
                    presenter.getdata(pageNo + "", "", position, "");
                } else {
                    toastShow(R.string.nonetwork);
                }
                xrecyclerview.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        xrecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.netWork()) {
                    pageNo++;
                    presenter.getdata(pageNo + "", "", position, sort);
                } else {
                    toastShow(R.string.nonetwork);
                }
                xrecyclerview.loadMoreComplete();
            }
        }, 2000);
    }
}
