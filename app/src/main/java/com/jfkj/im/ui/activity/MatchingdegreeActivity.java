package com.jfkj.im.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.MatchingdegreeBean;
import com.jfkj.im.Bean.loadSquareGameQuestionBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.MatchingdegreAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Matchingdegree.MatchingdegreePresenter;
import com.jfkj.im.mvp.Matchingdegree.MatchingdegreeView;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;

public class MatchingdegreeActivity extends BaseActivity<MatchingdegreePresenter> implements MatchingdegreeView {
    @BindView(R.id.back_iv)
    AppCompatImageView back_iv;
    @BindView(R.id.head1_iv)
    CircleImageView head1_iv;
    @BindView(R.id.head2_iv)
    CircleImageView head2_iv;
    @BindView(R.id.tv_number)
    AppCompatTextView tv_number;
    @BindView(R.id.swiperecyclerview)
    SwipeRecyclerView swiperecyclerview;
    MatchingdegreePresenter matchingdegreePresenter;
    String resultid;
    MatchingdegreeBean bean;
    Intent getIntent;
    String minecharacter;
    loadSquareGameQuestionBean gameQuestionBean;
    MatchingdegreAdapter matchingdegreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent = getIntent();
        resultid = getIntent.getStringExtra(Utils.RESULTID);
        minecharacter = getIntent.getStringExtra(Utils.MINECHARACTER);
        gameQuestionBean = JSON.parseObject(minecharacter, loadSquareGameQuestionBean.class);
        swiperecyclerview.setLayoutManager(new LinearLayoutManager(this));
        matchingdegreePresenter = new MatchingdegreePresenter(this);
        matchingdegreePresenter.Matchingdegree(resultid);
        Glide.with(mActivity).load(UserInfoManger.getUserHeadUrl()).into(head1_iv);
        matchingdegreAdapter=new MatchingdegreAdapter(this);
        swiperecyclerview.setAdapter(matchingdegreAdapter);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_matchingdegree;
    }

    @Override
    public MatchingdegreePresenter createPresenter() {
        return matchingdegreePresenter;
    }

    @Override
    public void matchingdegreesuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                MatchingdegreeBean matchingdegreeBean = JSON.parseObject(string, MatchingdegreeBean.class);
                List<MatchingdegreeBean.DataBean.AnswerBean> answer = matchingdegreeBean.getData().getAnswer();
                Glide.with(mActivity).load(matchingdegreeBean.getData().getHead()).into(head2_iv);
                tv_number.setText(matchingdegreeBean.getData().getPercentage() + "%");
                matchingdegreAdapter.setList(answer,gameQuestionBean.getData().getArray());
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
}
