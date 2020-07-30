package com.jfkj.im.TIM.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.game.GameListBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.SendRedPackageActivity;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.view.RoundImageView;
import com.lin.cardlib.CardLayoutManager;
import com.lin.cardlib.CardSetting;
import com.lin.cardlib.CardTouchHelperCallback;
import com.lin.cardlib.OnSwipeCardListener;
import com.lin.cardlib.utils.ReItemTouchHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class AdventureFragment extends BaseFragment {

    @BindView(R.id.recycler_select)
    RecyclerView recycler_select;
    @BindView(R.id.tv_selected)
    TextView tv_selected;

    private CommonRecyclerAdapter<GameListBean> adapter;
    private List<GameListBean> datas = new ArrayList<>();
    private StringBuffer stringBuffer;
    private EditText et_content;
    private ChatInfo chatInfo;
    private LoadingDialog loadingDialog;

    public static AdventureFragment getInstance(ChatInfo chatInfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable("info",chatInfo);
        AdventureFragment adventureFragment = new AdventureFragment();
        adventureFragment.setArguments(bundle);
        return adventureFragment;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_true_words;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniView();
        initData();
        getData();
    }

    private void getData() {
        loadingDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.PAGENO,"1");
        map.put(Utils.PAGESIZE,"60");
        map.put(Utils.TYPE,"1");
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/group/getAdventureTitleList")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getActivity()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            loadingDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    List<GameListBean> listBeans = JSON.parseArray(object.getString("array"),GameListBean.class);
                                    datas.addAll(listBeans);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            toastShow(R.string.nonetwork);
        }
    }

    private void initData() {
        assert getArguments() != null;
        chatInfo = (ChatInfo) getArguments().getSerializable("info");
    }

    private void iniView() {
        loadingDialog = new LoadingDialog(getActivity(),R.style.dialog);
        stringBuffer = new StringBuffer();
        recycler_select.setItemAnimator(new DefaultItemAnimator());
        CardSetting cardSetting = new CardSetting(){
            @Override
            public boolean isLoopCard() {
                return true;
            }

            @Override
            public int getSwipeDirection() {
                return ReItemTouchHelper.LEFT | ReItemTouchHelper.RIGHT;
            }

            @Override
            public int couldSwipeOutDirection() {
                return ReItemTouchHelper.LEFT | ReItemTouchHelper.RIGHT;
            }

            @Override
            public int getShowCount() {
                return 1;
            }
        };
        cardSetting.setSwipeListener(new OnSwipeCardListener() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float v, float v1, int direction) {
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Log.e("aaa", "swiping direction=down");
                        break;
                    case ReItemTouchHelper.UP:
                        Log.e("aaa", "swiping direction=up");
                        break;
                    case ReItemTouchHelper.LEFT:
                        Log.e("aaa", "swiping direction=left");
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Log.e("aaa", "swiping direction=right");
                        break;
                }
            }

            @Override
            public void onSwipedOut(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Log.e("aaa", "swiping... direction=down");
                        break;
                    case ReItemTouchHelper.UP:
                        Log.e("aaa", "swiping ...direction=up");
                        break;
                    case ReItemTouchHelper.LEFT:
                        Log.e("aaa", "swiping ....direction=left");
                        if (!TextUtils.isEmpty(getContent())){
//                            stringBuffer.append(getContent()).append(",");
                        }
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Log.e("aaa", "swiping ....direction=right");
                        if (!TextUtils.isEmpty(getContent())){
//                            stringBuffer.append(getContent()).append(",");
                        }
                        break;
                }
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(getContext(), "你已选择："+stringBuffer.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        CardTouchHelperCallback helperCallback = new CardTouchHelperCallback(recycler_select,datas,cardSetting);
        ReItemTouchHelper reItemTouchHelper = new ReItemTouchHelper(helperCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(reItemTouchHelper,cardSetting);
        recycler_select.setLayoutManager(cardLayoutManager);

        adapter = new CommonRecyclerAdapter<GameListBean>(getActivity(),datas,R.layout.fragment_adventure_item) {
            @Override
            public void convert(CommonRecyclerHolder holder, GameListBean bean, int position) {
                et_content = holder.getView(R.id.et_content);
                et_content.setText(bean.getContent());
                et_content.setEnabled(false);
                KeyBoardUtils.hideKeybord(et_content);
                LinearLayout linearLayout = holder.getView(R.id.ll_1);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.netWork()) {
                            if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 6) {
                                et_content.setEnabled(true);
                                et_content.setSelection(et_content.getText().toString().trim().length());
                                KeyBoardUtils.requestShowKeyBord(et_content);
//                                datas.add(new GameListBean());
//                                adapter.notifyDataSetChanged();
                            } else {
                                VipSetGradeDialogFragment dialogFragment =
                                        new VipSetGradeDialogFragment(true, Gravity.CENTER, " 等级达到6级以后，才可解锁自定义冒险游戏内容功能。","确认");
                                dialogFragment.setRsp(new ResponListener<Boolean>() {
                                    @Override
                                    public void Rsp(Boolean s) {
                                        if (s) {

                                        }
                                    }
                                });
                                if (getFragmentManager() != null) {
                                    dialogFragment.show(getFragmentManager(), "");
                                }

                            }
                        } else {
                            toastShow(R.string.nonetwork);
                        }
                    }
                });
            }
        };
        recycler_select.setAdapter(adapter);
    }

    public String getContent (){
        return et_content.getText().toString().trim();
    }

    @OnClick(R.id.tv_selected)
    void Onclick(){
        if (TextUtils.isEmpty(getContent()) && TextUtils.isEmpty(stringBuffer.toString())){
            toastShow("请输入大冒险");
            return;
        }
        if (!TextUtils.isEmpty(getContent())){
            stringBuffer.append(getContent());
        }
        startActivity(new Intent(getContext(), SendRedPackageActivity.class)
//                .putExtra("content",stringBuffer.toString())
                .putExtra("content",getContent())
                .putExtra("type","1")
                .putExtra("data",chatInfo));
        if (getActivity() != null){
            getActivity().finish();
        }
    }
}
