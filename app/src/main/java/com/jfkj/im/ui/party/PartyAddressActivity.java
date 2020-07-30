package com.jfkj.im.ui.party;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.blankj.utilcode.util.GsonUtils;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.SearchHistoryBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.TIM.utils.SoftKeyBoardUtil;
import com.jfkj.im.adapter.PartyAddressAdapter;
import com.jfkj.im.citypick.Bean;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.CollectionUtils;
import com.jfkj.im.utils.GsonUtil;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.flowlayout.FlowLayout;
import com.jfkj.im.view.flowlayout.TagAdapter;
import com.jfkj.im.view.flowlayout.TagFlowLayout;
import com.lzy.okgo.utils.OkLogger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jfkj.im.utils.Utils.SEARCH_HISTORY_ADDRESS;

/**
 *
 */
public class PartyAddressActivity extends BaseActivity implements SwipeRecyclerView.LoadMoreListener, PartyAddressAdapter.OnItemClick {

    private PoiSearch mPoiSearch;


    int pageNum = 1;


    @BindView(R.id.ed_serch)
    EditText edSerch;


    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.recycler)
    SwipeRecyclerView recyclerView;

    @BindView(R.id.flowLayout)
    TagFlowLayout mTagFlowLayout;

    @BindView(R.id.tv_popular)
    TextView tv_popular;

    @BindView(R.id.img_del)
    ImageView img_del;


    private List<PoiInfo> allPoi  = new ArrayList<>();
    private PartyAddressAdapter adapter;
    private String selectAddress = "";

    private List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        mPoiSearch = PoiSearch.newInstance();

        mPoiSearch.setOnGetPoiSearchResultListener(listener);


        tvTitle.setText(UserInfoManger.getcity());

        edSerch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())
                || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //处理事件

                    pageNum = 1;
                    getSerchCity();

                    //清空数据
                    allPoi.clear();
                    allPoi = new ArrayList<>();



                    String json = SPUtils.getInstance(getApplicationContext()).getString(SEARCH_HISTORY_ADDRESS, "");


                    String searchText = edSerch.getText().toString().trim();

                    if("".equals(json)){
                        SearchHistoryBean bean = new SearchHistoryBean();
                        List<SearchHistoryBean.DataBean> b =  new ArrayList<>();
                        bean.setDataBean(b);
                        bean.getDataBean().add(new SearchHistoryBean.DataBean(searchText));

                        String json1 = GsonUtils.toJson(bean);

                        SPUtils.getInstance(getApplicationContext()).put(SEARCH_HISTORY_ADDRESS,json1);
                    }else{
                        SearchHistoryBean searchHistoryBean = GsonUtils.fromJson(json, SearchHistoryBean.class);
                        searchHistoryBean.getDataBean().add(new SearchHistoryBean.DataBean(searchText));


                        //只显示最近5次的搜索记录

                        if(searchHistoryBean.getDataBean().size() == 6){
                            searchHistoryBean.getDataBean().remove(0);

                            String json1 = GsonUtils.toJson(searchHistoryBean);
                            SPUtils.getInstance(getApplicationContext()).put(SEARCH_HISTORY_ADDRESS,json1);
                        }else{
                            String json1 = GsonUtils.toJson(searchHistoryBean);
                            SPUtils.getInstance(getApplicationContext()).put(SEARCH_HISTORY_ADDRESS,json1);
                        }
                    }


                    tv_popular.setVisibility(View.GONE);
                    mTagFlowLayout.setVisibility(View.GONE);

                }
                return true;
            }
        });

        edSerch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    img_del.setVisibility(View.VISIBLE);
                } else {
                    img_del.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PartyAddressAdapter(allPoi,this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.useDefaultLoadMore();
        recyclerView.loadMoreFinish(false, true);
        recyclerView.setLoadMoreListener(this);
        recyclerView.setAutoLoadMore(false);

        getSearchHistory();
    }

    private boolean getSearchHistory() {
        String json = SPUtils.getInstance(this).getString(SEARCH_HISTORY_ADDRESS, "");

        if("".equals(json)){
            return false;
        }





        SearchHistoryBean searchHistoryBean = GsonUtils.fromJson(json, SearchHistoryBean.class);

        lists.clear();
        lists = new ArrayList<>();

//0+1  5
        for (int i = 0 ; i < searchHistoryBean.getDataBean().size();i++){
            lists.add(searchHistoryBean.getDataBean().get(i).getAddress());
        }


        Collections.reverse(lists);


        TagAdapter tagAdapter = new TagAdapter<String>(lists) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(PartyAddressActivity.this).inflate(R.layout.item_address_text, parent, false);
                textView.setText(s);
                return textView;
            }
        };

        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                String searchText = lists.get(position);
                edSerch.setText(searchText);

                //处理事件

                pageNum = 1;
                getSerchCity();

                //清空数据
                allPoi.clear();
                allPoi = new ArrayList<>();

                String json = SPUtils.getInstance(getApplicationContext()).getString(SEARCH_HISTORY_ADDRESS, "");


                String search = edSerch.getText().toString().trim();

                if("".equals(json)){
                    SearchHistoryBean bean = new SearchHistoryBean();
                    List<SearchHistoryBean.DataBean> b =  new ArrayList<>();
                    bean.setDataBean(b);
                    bean.getDataBean().add(new SearchHistoryBean.DataBean(search));

                    String json1 = GsonUtils.toJson(bean);

                    SPUtils.getInstance(getApplicationContext()).put(SEARCH_HISTORY_ADDRESS,json1);
                }else{
                    SearchHistoryBean searchHistoryBean = GsonUtils.fromJson(json, SearchHistoryBean.class);
                    searchHistoryBean.getDataBean().add(new SearchHistoryBean.DataBean(search));


                    //只显示最近5次的搜索记录

                    if(searchHistoryBean.getDataBean().size() == 6){
                        searchHistoryBean.getDataBean().remove(0);

                        String json1 = GsonUtils.toJson(searchHistoryBean);
                        SPUtils.getInstance(getApplicationContext()).put(SEARCH_HISTORY_ADDRESS,json1);
                    }else{
                        String json1 = GsonUtils.toJson(searchHistoryBean);
                        SPUtils.getInstance(getApplicationContext()).put(SEARCH_HISTORY_ADDRESS,json1);
                    }
                }

//                        Intent intent = new Intent();
//                        intent.putExtra("address",lists.get(position));
//                        setResult(0x01,intent);
//                        finish();


                tv_popular.setVisibility(View.GONE);
                mTagFlowLayout.setVisibility(View.GONE);


//
                return false;
            }
        });




        mTagFlowLayout.setAdapter(tagAdapter);
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party_address;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    private void getSerchCity(){
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(tvTitle.getText().toString()) //必填
                .keyword(edSerch.getText().toString()) //必填
                .pageNum(pageNum));
    }

    OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {

            if(poiResult == null  || poiResult.getAllPoi() ==null){
                return;
            }else{
                allPoi.addAll(poiResult.getAllPoi());
                adapter.setData(allPoi);

                if(poiResult.getAllPoi().size()>0){
                    recyclerView.loadMoreFinish(false, true);
                }else{
                    recyclerView.loadMoreFinish(false, false);
                }
            }






        }
        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
            OkLogger.e(poiDetailSearchResult+"");
        }
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            OkLogger.e(poiIndoorResult + "");
        }
        //废弃
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            OkLogger.e(poiDetailResult + "");
        }
    };

    @OnClick({R.id.tv_title,R.id.iv_title_back,R.id.img_del})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:
                Intent intent = new Intent();
                intent.putExtra("address",selectAddress);
                setResult(0x01,intent);
                finish();
                break;
            case R.id.tv_title:
                JumpUtil.startForResult(this,SelectCityActivity.class,0x02,null);
                break;
            case R.id.img_del:
                edSerch.setText("");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (resultCode){
                case 0x02:
                    String selectCity = data.getStringExtra("selectCity");

                    if(!"".equals(selectCity)){
                        tvTitle.setText(selectCity);
                    }
                    break;
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("address",selectAddress);
        setResult(0x01,intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
    }



    //加载更多
    @Override
    public void onLoadMore() {
        pageNum++;
        getSerchCity();
    }

    @Override
    public void selectAddress(int position, String address) {
        //保存搜索记录

        selectAddress = address;
        Intent intent = new Intent();
        intent.putExtra("address",selectAddress);
        setResult(0x01,intent);
        finish();
    }

}
