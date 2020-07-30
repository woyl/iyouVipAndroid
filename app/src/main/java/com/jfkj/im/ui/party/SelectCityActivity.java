package com.jfkj.im.ui.party;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.jfkj.im.Bean.AreasBean;
import com.jfkj.im.Bean.City;
import com.jfkj.im.Bean.CityPickerBean;
import com.jfkj.im.Bean.LocateState;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.SoftKeyBoardUtil;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.adapter.CityListAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.GsonUtil;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.utils.ReadAssetsFileUtil;
import com.jfkj.im.view.SideLetterBar;
import com.lzy.okgo.utils.OkLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择城市
 */
public class SelectCityActivity extends BaseActivity {


    private ListView mListView;
    private SideLetterBar mLetterBar;
    private CityListAdapter mCityAdapter;
    private List<City> mCities;

    @BindView(R.id.tv_continent)
    TextView tvContinent;

    @BindView(R.id.tv_overseas)
    TextView tvOverseas;

    @BindView(R.id.ed_serch_city)
    EditText edSerchCity;

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;


    private List<City> searchCity;
    private ArrayList<City> cities;

    String selectCity = "";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("selectCity",selectCity);
        setResult(0x02,intent);
        finish();
    }


    @SuppressLint("ResourceType")
    @OnClick({R.id.iv_title_back,R.id.tv_continent , R.id.tv_overseas})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:

                Intent intent = new Intent();
                intent.putExtra("selectCity",selectCity);
                setResult(0x02,intent);
                finish();
                break;
            case R.id.tv_continent:


                tvContinent.setTextColor( this.getResources().getColor(R.color.white));
                tvOverseas.setTextColor(this.getResources().getColor(R.color.color_ff333333));


                if(mCities!=null){
                    mCities.clear();
                    mCities= null;
                }
                mCities = new ArrayList<>();
                mCities.add(new City("北京市"));
                mCities.add(new City("天津市"));
                mCities.add(new City("广州市"));
                mCities.add(new City("深圳市"));
                mCities.add(new City("上海市"));
                mCities.add(new City("西安市"));
                mCities.add(new City("武汉市"));
                mCities.add(new City("苏州市"));
                mCities.add(new City("福州市"));
                mCityAdapter.setmHotData(mCities);
                break;
            case R.id.tv_overseas:


                tvContinent.setTextColor( this.getResources().getColor(R.color.color_ff333333));
                tvOverseas.setTextColor(this.getResources().getColor(R.color.white));

                //tvContinent.setText(Color.parseColor("#FF333333"));

                if(mCities!=null){
                    mCities.clear();
                    mCities= null;
                }
                mCities = new ArrayList<>();
                mCities.add(new City("中国香港"));
                mCities.add(new City("中国澳门"));
                mCities.add(new City("东京"));
                mCities.add(new City("曼谷"));
                mCities.add(new City("新加坡"));
                mCities.add(new City("台北"));
                mCityAdapter.setmHotData(mCities);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();
        initData();

        tvTitle.setText("选择城市");

        edSerchCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件

                    String s = edSerchCity.getText().toString().trim();



                    if(!"".equals(s)){
                        if(searchCity !=null){
                            searchCity.clear();
                            searchCity = null;
                        }
                        searchCity = new ArrayList<>();
                        for (int i = 0 ; i <cities.size(); i ++ ) {
                            if(cities.get(i).getName().contains(s)){
                                searchCity.add(cities.get(i));
                            }
                        }

                        OkLogger.e("搜索长度 = " + searchCity.size());
                        mCityAdapter.setMCities(searchCity);

                    }else{
                        mCityAdapter.setData(cities);
                    }



                    //隐藏软键盘
                    SoftKeyBoardUtil.hideKeyBoard(edSerchCity.getWindowToken());
                }
                return true;
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }



    protected void initView() {
        mListView = findViewById(R.id.listview_all_city);
        TextView overlay = findViewById(R.id.tv_letter_overlay);
        mLetterBar = findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });
        mCityAdapter = new CityListAdapter(this);
        mListView.setAdapter(mCityAdapter);
    }


    public void getCityData() {
        String json = ReadAssetsFileUtil.getJson(this, "city.json");
        CityPickerBean bean = GsonUtil.getBean(json, CityPickerBean.class);
        HashSet<City> citys = new HashSet<>();

        OkLogger.e(bean.toString());


        if(bean !=null ){
            if(bean.getData()!=null){
        for (int i = 0 ; i < bean.getData().getAreas().size();i++){
            List<CityPickerBean.DataBean.AreasBean> areasBean = bean.getData().getAreas();
            String name = areasBean.get(i).getName().replace("　", "");
            citys.add(new City(areasBean.get(i).getId(), name, PinyinUtils.getPinYin(name), areasBean.get(i).getIs_hot() == 1));

            for (int j = 0 ; j < areasBean.get(i).getChildren().size() ; j++){
                CityPickerBean.DataBean.AreasBean.ChildrenBeanX childrenBeanX = areasBean.get(i).getChildren().get(j);
                citys.add(new City(childrenBeanX.getId(), childrenBeanX.getName(), PinyinUtils.getPinYin(childrenBeanX.getName()), childrenBeanX.getIs_hot() == 1));
            }

        }}

        }
        //set转换list
        cities = new ArrayList<>(citys);
        //按照字母排序
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City city, City t1) {
                return city.getPinyin().compareTo(t1.getPinyin());
            }
        });
        mCityAdapter.setData(cities);
    }


    protected void initData() {
        getCityData();
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {//选择城市

                selectCity = name;
                Intent intent = new Intent();
                intent.putExtra("selectCity",selectCity);
                setResult(0x02,intent);
                finish();
               //    ToastUtil.toastLongMessage(name);

            }

            @Override
            public void onLocateClick() {//点击定位按钮
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
              //  getLocation();//重新定位
            }
        });
    }

}
