package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.loadSquareGameQuestionBean;
import com.jfkj.im.Bean.loadUserAnswerList;
import com.jfkj.im.R;
import com.jfkj.im.adapter.Characteruseradapter;
import com.jfkj.im.adapter.loadSquareGameQuestionAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Minecharacter.MinecharacterPresenter;
import com.jfkj.im.mvp.Minecharacter.MinecharacterView;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

public class MinecharacterActivity extends BaseActivity<MinecharacterPresenter> implements MinecharacterView, View.OnClickListener, OnItemClickListener {
    @BindView(R.id.close_iv)
    AppCompatImageView close_iv;
    @BindView(R.id.progress_tv)
    AppCompatTextView progress_tv;
    @BindView(R.id.time_tv)
    AppCompatTextView time_tv;
    @BindView(R.id.title_tv)
    AppCompatTextView title_tv;
    @BindView(R.id.user_tv)
    AppCompatTextView user_tv;
    @BindView(R.id.swiperecyclerview_mime)
    SwipeRecyclerView swiperecyclerview_mime;
    @BindView(R.id.swiperecyclerview_user)
    SwipeRecyclerView swiperecyclerview_user;
    MinecharacterPresenter minecharacterPresenter;
    loadSquareGameQuestionAdapter questionAdapter;
    List<loadUserAnswerList.DataBean.ArrayBean> loadUserAnswerListList;
    Characteruseradapter characteruseradapter;
    String minecharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUserAnswerListList = new ArrayList<>();
        characteruseradapter = new Characteruseradapter(this);
        swiperecyclerview_mime.setLayoutManager(new LinearLayoutManager(this));
        swiperecyclerview_user.setLayoutManager(new LinearLayoutManager(this));
        minecharacterPresenter = new MinecharacterPresenter(this);
        swiperecyclerview_user.setOnItemClickListener(this);
        Intent intent = getIntent();
        time_tv.setText(intent.getStringExtra(Utils.TIME));
        questionAdapter = new loadSquareGameQuestionAdapter(this);
        swiperecyclerview_mime.setAdapter(questionAdapter);
        swiperecyclerview_user.setAdapter(characteruseradapter);
        minecharacterPresenter.Minecharacter(intent.getStringExtra(Utils.GAMEID));
        minecharacterPresenter.loadSquareGameQuestion(intent.getStringExtra(Utils.GAMEID));
        title_tv.setOnClickListener(this);
        user_tv.setOnClickListener(this);
        close_iv.setOnClickListener(this);
        swiperecyclerview_user.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_tv:
                title_tv.setBackgroundResource(R.drawable.shape_minecharacter_select);
                user_tv.setBackgroundResource(R.drawable.shape_minecharacter_unselect);
                title_tv.setTextColor(Color.parseColor("#ffffff"));
                user_tv.setTextColor(Color.parseColor("#FF666666"));
                swiperecyclerview_mime.setVisibility(View.VISIBLE);
                swiperecyclerview_user.setVisibility(View.GONE);
                break;
            case R.id.user_tv:
                title_tv.setBackgroundResource(R.drawable.shape_minecharacter_unselect_right);
                user_tv.setBackgroundResource(R.drawable.shape_minecharacter_select_right);
                user_tv.setTextColor(Color.parseColor("#ffffff"));
                title_tv.setTextColor(Color.parseColor("#FF666666"));
                swiperecyclerview_mime.setVisibility(View.GONE);
                swiperecyclerview_user.setVisibility(View.VISIBLE);
                break;
            case R.id.close_iv:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_minecharacter;
    }

    @Override
    public MinecharacterPresenter createPresenter() {
        return minecharacterPresenter;
    }

    @Override
    public void MinecharacterSuccess(String responseBody) {
        try {

            JSONObject jsonObject = new JSONObject(responseBody);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                loadUserAnswerListList.addAll(JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("array").toString(), loadUserAnswerList.DataBean.ArrayBean.class));
                characteruseradapter.setList(loadUserAnswerListList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSquareGameQuestionSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                minecharacter = string;
                loadSquareGameQuestionBean loadSquareGameQuestionBean = JSON.parseObject(string, loadSquareGameQuestionBean.class);
                questionAdapter.setLoadSquareGameQuestionBeans(loadSquareGameQuestionBean.getData().getArray());

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
        Intent intent = new Intent(mActivity, MatchingdegreeActivity.class);
        loadUserAnswerList.DataBean.ArrayBean arrayBean = loadUserAnswerListList.get(adapterPosition);
        intent.putExtra(Utils.RESULTID, arrayBean.getResultId());
        intent.putExtra(Utils.MINECHARACTER, minecharacter);
        startActivity(intent);
    }
}
