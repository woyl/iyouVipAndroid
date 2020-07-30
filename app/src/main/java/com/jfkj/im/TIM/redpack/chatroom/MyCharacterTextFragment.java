package com.jfkj.im.TIM.redpack.chatroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.R;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;

public class MyCharacterTextFragment extends BaseFragment {
    @BindView(R.id.recycler)
    SwipeRecyclerView recycler;
    @BindView(R.id.ll_no_date)
    LinearLayout ll_no_date;

    private String gameId;
    private CommonRecyclerAdapter<AnswerSelfBean> adapter;
    private List<AnswerSelfBean> answerSelfBeans = new ArrayList<>();

    public static MyCharacterTextFragment getInstance(String gameId,List<AnswerSelfBean> answerSelfBeans){
        Bundle bundle = new Bundle();
        bundle.putString("gameId",gameId);
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) answerSelfBeans);
        MyCharacterTextFragment adventureFragment = new MyCharacterTextFragment();
        adventureFragment.setArguments(bundle);
        return adventureFragment;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recycler_character_left;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniviews();

    }

    private void iniviews() {
        if (getArguments() != null){
            gameId = getArguments().getString("gameId");
            answerSelfBeans = getArguments().getParcelableArrayList("data");
            if (answerSelfBeans.size() > 0){
                ll_no_date.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }else {
                ll_no_date.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            }
        }

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new SpacesItemDecoration(30));
        adapter = new CommonRecyclerAdapter<AnswerSelfBean>(getActivity(),answerSelfBeans,R.layout.item_my_character_t_fragment_left) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, AnswerSelfBean answerSelfBean, int position) {
                TextView tv_content = holder.getView(R.id.tv_content);
                tv_content.setText((position+1)+"." +answerSelfBean.getSubject());
                TextView tv_answer = holder.getView(R.id.tv_answer);

                char a1 = (char) ((char)answerSelfBean.getAnswer().getSerialNo() + 96);
                tv_answer.setText( a1 + "." + answerSelfBean.getAnswer().getContent());
            }
        };
        recycler.setAdapter(adapter);
    }


}
