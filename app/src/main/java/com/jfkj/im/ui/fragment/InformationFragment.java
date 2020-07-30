package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.App;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.adapter.dynamic.MineGroupTaskAdapter;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.event.GroupMyTaskEvent;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.mine.MinePresenter;
import com.jfkj.im.mvp.mine.MineView;
import com.jfkj.im.ui.mine.SettingActivity;
import com.jfkj.im.ui.mine.UserInfoActivity;
import com.jfkj.im.ui.party.MyCrushIceTaskActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ScrollLinearLayoutManager;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.flowlayout.FlowLayout;
import com.jfkj.im.view.flowlayout.TagAdapter;
import com.jfkj.im.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InformationFragment extends BaseFragment<MinePresenter> implements MineView {

    @BindView(R.id.tv_infomsg)
    TextView tv_infomsg;

    @BindView(R.id.flowLayout)
    TagFlowLayout mFlowLayout;

    /**
     * 破冰任务
     */
    @BindView(R.id.rv_prush_ice)
    RecyclerView rv_prush_ice;

    /**
     * 俱乐部
     */
    @BindView(R.id.tv_club)
    TextView tv_club;
    @BindView(R.id.rv_group)
    RecyclerView rv_group;

    @BindView(R.id.tv_add)
    TextView tvAdd;

    private MineGroupTaskAdapter adapter;
    private UserGroupTaskBean userGroupTaskBean;


    private List<String> labelList = new ArrayList<>();


    /**
     * 破冰任务
     */
    private CommonRecyclerAdapter<UserDetailBean.TaskList> taskListCommonRecyclerAdapter;
    private List<UserDetailBean.TaskList> taskLists;
    private NumberFormat numberFormat;


    public static InformationFragment getInstance() {
        InformationFragment informationFragment = new InformationFragment();
        return informationFragment;
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAndroidNativeLightStatusBar(getActivity(),false);
        initView();
    }

    private void initView() {


        /**破冰任务*/
        taskLists = new ArrayList<>();
        rv_prush_ice.addItemDecoration(new SpacesItemDecoration(9));
        rv_prush_ice.setLayoutManager(new ScrollLinearLayoutManager(getContext()));
        iceTask();

        /**
         * 获取用户信息
         * */
        mvpPresenter.getUserDetails();

        /**
         * 俱乐部
         * */
        rv_group.setLayoutManager(new ScrollLinearLayoutManager(getContext()));
        adapter = new MineGroupTaskAdapter(getContext(), userGroupTaskBean);
        rv_group.setAdapter(adapter);
        mvpPresenter.getGroupTask(SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.overlay(getContext(), UserInfoActivity.class);
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGroupTask(GroupMyTaskEvent event) {
        if (event.getUserGroupTaskBean().getData() != null && event.getUserGroupTaskBean().getData().size() > 0) {
            adapter.setData(event.getUserGroupTaskBean());
            tv_club.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setData(null);
            tv_club.setVisibility(View.GONE);
        }
    }

    private void iceTask() {
        taskLists = new ArrayList<>();
        rv_prush_ice.addItemDecoration(new SpacesItemDecoration(9));
        rv_prush_ice.setLayoutManager(new ScrollLinearLayoutManager(getContext()));
        taskListCommonRecyclerAdapter = new CommonRecyclerAdapter<UserDetailBean.TaskList>(getContext(), taskLists, R.layout.item_mine_prush_ice_task) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, UserDetailBean.TaskList taskList, int position) {
                ImageView img_state = holder.getView(R.id.img_state);
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_money = holder.getView(R.id.tv_money);
                TextView tv_state = holder.getView(R.id.tv_state);
                tv_title.setText(taskList.getCtaskcontent());
                if (!TextUtils.isEmpty(taskList.getIrewardamount())) {
                    long money = (long) Double.parseDouble(taskList.getIrewardamount());
                    tv_money.setText(money + "/人");
                }

                if (taskList.getItasktype().equals("1")) {
                    img_state.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_club_mine_ice_pic));
                } else {
                    img_state.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_club_mine_ice_video));
                }
                tv_state.setText("编辑");
                tv_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("taskId", taskList.getItaskid());
                        bundle.putString("taskContent", taskList.getCtaskcontent());
                        bundle.putString("taskMoney", taskList.getIrewardamount());
                        bundle.putString("taskType",taskList.getItasktype());
                        JumpUtil.overlay(getContext(), MyCrushIceTaskActivity.class, bundle);
                    }
                });
            }
        };
        rv_prush_ice.setAdapter(taskListCommonRecyclerAdapter);
        numberFormat = NumberFormat.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        convertLabel();
    }

    /**
     * 处理用户信息内容展示
     *
     * @param
     */
    private void convertLabel() {

        labelList.clear();

        String heightWeight = "";
        String homeTown = "";


        String height = TextUtils.isEmpty(UserInfoManger.getUserHeight()) ? "" : UserInfoManger.getUserHeight() + "cm";
        String weight = TextUtils.isEmpty(UserInfoManger.getUserWeight()) ? "" : UserInfoManger.getUserWeight() + "kg";

        homeTown = TextUtils.isEmpty(UserInfoManger.getHometown()) ? "" : UserInfoManger.getHometown();


        //学历
        String education = TextUtils.isEmpty(UserInfoManger.getUserEducation()) ? "" : UserInfoManger.getUserEducation();
        //学院
        String school = TextUtils.isEmpty(UserInfoManger.getSchool()) ? "" : UserInfoManger.getSchool();
        //职业
        String occupation = TextUtils.isEmpty(UserInfoManger.getOccupation()) ? "" : UserInfoManger.getOccupation();
        //行业
        String industry = TextUtils.isEmpty(UserInfoManger.getindustry()) ? "" : UserInfoManger.getindustry();


        String smoking = TextUtils.isEmpty(UserInfoManger.getSmoking()) ? "" : UserInfoManger.getSmoking();
        String drinkWine = TextUtils.isEmpty(UserInfoManger.getDrinkwine()) ? "" : UserInfoManger.getDrinkwine();
        String likeCuisine = TextUtils.isEmpty(UserInfoManger.getLikeCuisine()) ? "" : UserInfoManger.getLikeCuisine();

        if (!"0cm".equals(height) && !"0kg".equals(weight)) {
            heightWeight = height + "·" + weight;
        } else if (!"0cm".equals(height)) {
            heightWeight = height;
        } else if (!"0kg".equals(weight)) {
            heightWeight = weight;
        } else {
            heightWeight = "";
        }

        //身高体重家乡
        if (!"".equals(heightWeight)) {
            labelList.add(heightWeight);
        }

        if (!"".equals(homeTown)) {
            labelList.add(homeTown);
        }


        if (!"".equals(education)) {
            labelList.add(education);
        }

        if (!"".equals(school)) {
            labelList.add(school);
        }

        if (!"".equals(occupation)) {
            labelList.add(occupation);
        }

        if (!"".equals(industry)) {
            labelList.add(industry);
        }

        //抽烟 喝酒   喜欢菜系
        if (!"".equals(smoking)) {
            labelList.add(smoking);
        }

        if (!"".equals(drinkWine)) {
            labelList.add(drinkWine);
        }

        if (!"".equals(likeCuisine)) {
            labelList.add(likeCuisine);
        }



        if (labelList.size() > 0) {
            mFlowLayout.setVisibility(View.VISIBLE);
            tvAdd.setVisibility(View.GONE);
        } else {
            tvAdd.setVisibility(View.VISIBLE);
            mFlowLayout.setVisibility(View.GONE);
        }



        TagAdapter tagAdapter = new TagAdapter<String>(labelList) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_label_text, parent, false);
                textView.setText(s);
                return textView;
            }
        };

        mFlowLayout.setAdapter(tagAdapter);
    }

    @Override
    public void showMineInfo(MineInfo info) {

    }

    @Override
    public void showDelete(int position) {

    }

    @Override
    public void showPraise(String id, int position) {

    }

    @Override
    public void showSuccess() {

    }


    /**
     * 显示我的俱乐部
     *
     * @param userGroupTaskBean
     */
    @Override
    public void showUserGroupTask(UserGroupTaskBean userGroupTaskBean) {
        if (userGroupTaskBean.getData() != null && userGroupTaskBean.getData().size() > 0) {
            adapter.setData(userGroupTaskBean);
            tv_club.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setData(null);
            tv_club.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSuccessBalance() {

    }

    @Override
    public void onFindOnNew(List<CommentBean> commentBean) {

    }

    /**
     * 获取用户信息
     */
    @Override
    public void getUserInfo(UserDetailBean userDetailBean) {
        if (taskLists.size() != 0) {
            taskLists.clear();
        }
        /**破冰任务*/
        taskLists.addAll(userDetailBean.getData().getTaskList());
        taskListCommonRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
