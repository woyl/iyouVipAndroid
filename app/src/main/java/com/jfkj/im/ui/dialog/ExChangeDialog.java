package com.jfkj.im.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.ExchangeAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.widget.GridItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/16
 * </pre>
 */
public class ExChangeDialog extends BottomSheetDialog implements onItemPositionClickListener {

    RecyclerView mRecycler;
    TextView mTvCoinNumber;
    private Context mContext;
    private List<ExChangeBean.DataBean> mChargeBeans = new ArrayList<>();
    private onItemPositionClickListener mListener;

    public ExChangeDialog(@NonNull Context context,List<ExChangeBean.DataBean> list,onItemPositionClickListener listener) {
        super(context,R.style.BottomSheetDialog);
        this.mContext = context;
        this.mChargeBeans = list;
        this.mListener = listener;
        createView();
    }

    public ExChangeDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        createView();
    }

    private void createView() {

        View bottomView = LayoutInflater.from(mContext).inflate(R.layout.dialog_exchange, null);
        setContentView(bottomView);
        mRecycler = bottomView.findViewById(R.id.recycler);
        mTvCoinNumber = bottomView.findViewById(R.id.tv_coin_number);
        mTvCoinNumber.setText(UserInfoManger.getUserGift() + "");
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRecycler.setLayoutManager(layoutManager);
        GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtils.dip2px(mContext,15), 3);
        mRecycler.addItemDecoration(itemDecoration);
        Log.d("@@@","mChargeBeans" + mChargeBeans.size());
        ExchangeAdapter adapter = new ExchangeAdapter(mContext, mChargeBeans,this);
        mRecycler.setAdapter(adapter);
        mRecycler.setHasFixedSize(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPositionClick(int position) {
        mListener.onPositionClick(position);
    }
}
