package com.jfkj.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jfkj.im.Bean.City;
import com.jfkj.im.Bean.LocateState;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.utils.PinyinUtils;
import com.jfkj.im.view.WrapHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;

    private Context mContext;
    private LayoutInflater inflater;
    private List<City> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private int locateState = LocateState.LOCATING;
    private String locatedCity;
    private List<City> mHotData = new ArrayList<>();   //熱門城市

    private int type = -1;



    /**
     * 搜索刷新
     * @param mCities
     */
    public void setMCities(List<City> mCities){
        type = 1;
        this.mCities = mCities;
        mHotData.clear();
        notifyDataSetChanged();
    }


    public void setmHotData(List<City> mCities){
        type = -1;
        this.mHotData = mCities;
        notifyDataSetChanged();
    }

    public void setData(List<City> mCities) {
        type = -1;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
        if (mCities == null) {
            mCities = new ArrayList<>();
        }
        mCities.add(0, new City(-1, "定位", "0"));
        mCities.add(1, new City(-1, "热门", "1"));
        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];

        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1).getPinyin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }



        }

        mHotData.add(new City("北京市"));
        mHotData.add(new City("天津市"));
        mHotData.add(new City("广州市"));
        mHotData.add(new City("深圳市"));
        mHotData.add(new City("上海市"));
        mHotData.add(new City("西安市"));
        mHotData.add(new City("武汉市"));
        mHotData.add(new City("苏州市"));
        mHotData.add(new City("福州市"));

        notifyDataSetChanged();
    }

    public CityListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if(type==-1){
            return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
        }else{
            return 2;
        }
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:     //定位
                view = inflater.inflate(R.layout.cp_view_locate_city, parent, false);

                TextView thisCity = view.findViewById(R.id.tv_city_this);
                thisCity.setText("");
                thisCity.setVisibility(View.GONE);

                break;
            case 1:     //热门
                view = inflater.inflate(R.layout.cp_view_hot_city, parent, false);
                WrapHeightGridView gridView = (WrapHeightGridView) view.findViewById(R.id.gridview_hot_city);
                final HotCityGridAdapter hotCityGridAdapter = new HotCityGridAdapter(mContext);
                gridView.setAdapter(hotCityGridAdapter);
                hotCityGridAdapter.setData(mHotData);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onCityClickListener != null) {
                            onCityClickListener.onCityClick(hotCityGridAdapter.getItem(position).getName());
                        }
                    }
                });
                break;
            case 2:     //所有
                if (view == null) {
                    view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
                    view.setTag(holder);
                } else {
                    holder = (CityViewHolder) view.getTag();
                }
                if (position >= 1) {
                    final String city = mCities.get(position).getName();
                    holder.name.setText(city);
                    String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position).getPinyin());
                    String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1).getPinyin()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)) {
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.letter.setText(currentLetter);
                    } else {
                        holder.letter.setVisibility(View.GONE);
                    }
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(city);
                            }
                        }
                    });
                }
                break;
        }
        return view;
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name);

        void onLocateClick();
    }
}
