package com.jfkj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.Bean.PinyinComparator;
import com.jfkj.im.Bean.SelectArea;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.adapter.SelectAreaAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.NationalArea.NationalAreaPresenter;
import com.jfkj.im.mvp.NationalArea.NationalAreaView;
import com.jfkj.im.utils.PinyinUtils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;

public class SelectAreaActivity extends BaseActivity<NationalAreaPresenter> implements NationalAreaView, OnItemClickListener, View.OnClickListener {
    NationalAreaPresenter presenter;
    Intent intent;
    private List<SortModel> sourcedatelist;
    private PinyinComparator pinyinComparator;
    SelectAreaAdapter selectAreaAdapter;
    @BindView(R.id.recyclerview)
    SwipeRecyclerView recyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NationalAreaPresenter(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.black_iv).setOnClickListener(this);
        intent = getIntent();
        presenter.NationalArea(this);
        sourcedatelist = new ArrayList<>();
        pinyinComparator = new PinyinComparator();
        selectAreaAdapter=new SelectAreaAdapter(this);
        recyclerview.setOnItemClickListener(this);
        recyclerview.setAdapter(selectAreaAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectarea;
    }

    @Override
    public NationalAreaPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void nationSuccess(String s) {
        List<SelectArea> selectAreas = JSON.parseArray(s, SelectArea.class);
        for (int i = 0; i < selectAreas.size(); i++) {
            SelectArea area = selectAreas.get(i);
            SortModel sortModel = new SortModel();
            sortModel.setName(area.get国家中文名());
            sortModel.setUserid(Long.parseLong(area.get国家代码()));
            sourcedatelist.add(sortModel);
        }

        sourcedatelist = filledData(sourcedatelist);
        Collections.sort(sourcedatelist, pinyinComparator);
        selectAreaAdapter.setmData(sourcedatelist);
    }
    @Override
    public void onItemClick(View view, int adapterPosition) {
        LinearLayout ly_user= view.findViewById(R.id.ly_user);
        ly_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortModel sortModel = sourcedatelist.get(adapterPosition);
                Intent intent = getIntent();
                intent.putExtra("code",sortModel.getUserid()+"");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            sortModel.setUserid(date.get(i).getUserid());
            sortModel.setHead_url(date.get(i).getHead_url());
            sortModel.setTop(date.get(i).getTop());
            sortModel.setNoDisturb(date.get(i).getNoDisturb());
            sortModel.setVipLevel(date.get(i).getVipLevel());
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


    @Override
    public void onClick(View v) {
        finish();
    }
}
