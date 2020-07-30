package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.MineCharacterttestBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.MineCharacterttestAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.loadSquareGameList.LoadSquareGameListPresenter;
import com.jfkj.im.mvp.loadSquareGameList.LoadSquareGameListView;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class MineCharacterttestActivity extends BaseActivity<LoadSquareGameListPresenter> implements LoadSquareGameListView, OnItemClickListener, View.OnClickListener {
    LoadSquareGameListPresenter presenter;
    List<MineCharacterttestBean.DataBean.ArrayBean> list;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    MineCharacterttestAdapter mineCharacterttestAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back_iv.setOnClickListener(this);
        presenter=new LoadSquareGameListPresenter(this);
        presenter.LoadSquareGameList("");
        list=new ArrayList<>();
        swiperecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mineCharacterttestAdapter=new MineCharacterttestAdapter(mActivity);
        swiperecyclerview.setOnItemClickListener(this);
        swiperecyclerview.setAdapter(mineCharacterttestAdapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_characterttest;
    }

    @Override
    public LoadSquareGameListPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void LoadSquareGameListSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject=new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if(jsonObject.getString("code").equals("200")){
                List<MineCharacterttestBean.DataBean.ArrayBean> listjson =JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("array").toString(),MineCharacterttestBean.DataBean.ArrayBean.class);
                list.addAll(listjson);

                if(listjson.size()==10){
                    presenter.LoadSquareGameList(listjson.get(9).getSort());
                }else {
                    mineCharacterttestAdapter.setList(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        if(Utils.netWork()){
            Intent intent=new Intent(mActivity,MinecharacterActivity.class);
            MineCharacterttestBean.DataBean.ArrayBean arrayBean = list.get(adapterPosition);
            intent.putExtra(Utils.GAMEID,arrayBean.getGameId()+"");
            intent.putExtra(Utils.TIME,arrayBean.getAddDate());
            startActivity(intent);
        }else {
            toastShow(R.string.nonetwork);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
