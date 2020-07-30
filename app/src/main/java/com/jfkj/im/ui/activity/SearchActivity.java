package com.jfkj.im.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jfkj.im.Bean.GroupBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.GroupAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.LoadUserGroupList.LoadUserGroupListPresenter;
import com.jfkj.im.mvp.LoadUserGroupList.LoadUserGroupListView;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;

public class SearchActivity extends BaseActivity<LoadUserGroupListPresenter> implements View.OnClickListener, LoadUserGroupListView, TextView.OnEditorActionListener, SwipeRecyclerView.LoadMoreListener {
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.recyclerview)
    SwipeRecyclerView recyclerview;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    LoadUserGroupListPresenter presenter;
    GroupAdapter groupAdapter;
    List<GroupBean.DataBean.ArrayBean> list;
    int pageNo = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        setStaturBar(ll_head);
        tv_cancel.setOnClickListener(this);
        groupAdapter=new GroupAdapter(this,"SearchActivity",1);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(groupAdapter);
        presenter=new LoadUserGroupListPresenter(this);
        list = new ArrayList<>();
        search_et.setOnClickListener(this);
        search_et.setInputType(TYPE_TEXT_FLAG_MULTI_LINE);
        search_et.setOnEditorActionListener(this);
        recyclerview.setLoadMoreListener(this);
        recyclerview.useDefaultLoadMore();
        recyclerview.loadMoreFinish(false,true);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    public LoadUserGroupListPresenter createPresenter() {
        return presenter;
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {


        if (actionId == EditorInfo.IME_ACTION_SEND
                || actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_GO
                || actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_NEXT
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

            if (!TextUtils.isEmpty(search_et.getText().toString().trim())) {
                pageNo=1;
                list.clear();
                presenter.getdata(pageNo + "", search_et.getText().toString().trim(), 2+"", "");
            } else {
                toastShow("搜索内容不能为空");
            }


        }





        return true;
    }


    public void onLoadMore() {
        recyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Utils.netWork()){
                    pageNo++;
                    presenter.getdata(pageNo + "", search_et.getText().toString(), 2+"", list.get(list.size()-1).getSort());
                }else {
                    toastShow(R.string.nonetwork);
                }

            }
        },2000);
    }

    @Override
    public void LoadUserGroupSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject=new JSONObject(string);
            toastShow(jsonObject.getString("message"));
            if(jsonObject.getString("code").equals("200")){
                if(pageNo==1){
                    list.clear();
                }
                recyclerview.loadMoreFinish(false, true);
                list.addAll(JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("array").toString(), GroupBean.DataBean.ArrayBean.class));
                groupAdapter.setList(list);

            }
            if(list.size()==0){
                ToastUtils.showShortToast("暂无搜索结果");
            }else {
                //  recyclerview.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
