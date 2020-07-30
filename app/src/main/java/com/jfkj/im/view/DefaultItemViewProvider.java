package com.jfkj.im.view;

import android.graphics.Color;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.utils.SPUtils;


public class DefaultItemViewProvider implements IViewProvider<String> {
    @Override
    public int resLayout() {
        return R.layout.scroll_picker_default_item_layout;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable String text) {
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(text);
        view.setTag(text);

    }

    @Override
    public void updateView(@NonNull View itemView, boolean isSelected) {
        TextView tv = itemView.findViewById(R.id.tv_content);
        tv.setTextColor(Color.parseColor(isSelected ? "#000000" : "#CCCCCC"));
        if (isSelected) {
            SPUtils.getInstance(App.getAppContext()).put("isSelected", tv.getText().toString().trim());
        }
    }
}
