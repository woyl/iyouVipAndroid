package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jfkj.im.R;
import com.jfkj.im.adapter.ProvinceCityAdapter;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/17
 * </pre>
 */
public class CityDialog extends Dialog implements OnItemClickListener {

    private SwipeRecyclerView swipeRecyclerView;
    private ProvinceCityAdapter provincecityAdapter;

    public CityDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_city);
        swipeRecyclerView = findViewById(R.id.swiperecyclerview);
        provincecityAdapter = new ProvinceCityAdapter(getContext());
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRecyclerView.setOnItemClickListener(this);
        swipeRecyclerView.setAdapter(provincecityAdapter);
        init();
    }

    public void init() {

    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.horizontalMargin = 0;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        window.getDecorView().setMinimumWidth(getContext().getResources().getDisplayMetrics().widthPixels);
        window.getDecorView().setBackgroundResource(R.drawable.shape_type_shopselect);

    }

    @Override
    public void onItemClick(View view, int adapterPosition) {

    }
}
