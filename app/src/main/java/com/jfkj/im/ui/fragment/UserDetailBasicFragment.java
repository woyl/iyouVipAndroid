package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.IceSubmitBean;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.ice.IceUtils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.adapter.UserDetailClubAdapter;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseLazyFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.userDetail.UserDetailPresenter;
import com.jfkj.im.mvp.userDetail.UserDetailView;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.ui.party.MyCrushIceTaskActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.PictureSelectorUtils;
import com.jfkj.im.utils.RomUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.flowlayout.FlowLayout;
import com.jfkj.im.view.flowlayout.TagAdapter;
import com.jfkj.im.view.flowlayout.TagFlowLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 用户基本信息
 */
public class UserDetailBasicFragment extends BaseLazyFragment<UserDetailPresenter> implements UserDetailView {

    @BindView(R.id.tv_basic_title)
    TextView tv_infomsg;


    @BindView(R.id.flowLayout)
    TagFlowLayout mFlowLayout;

    @BindView(R.id.layout_club)
    RelativeLayout mLayoutClub;

    @BindView(R.id.club_recyclerView)
    RecyclerView clubRecyclerView;

    private TIMConversation conversation;

    public static String Gender;

    private String taskId, taskContent;
    //照片
    private List<LocalMedia> selectList = new ArrayList<>();


    private List<String> labelList = new ArrayList<>();
    private List<String> labelList_2 = new ArrayList<>();

    private String userId;


    /**
     * 破冰任务
     */
    @BindView(R.id.rv_prush_ice)
    RecyclerView rv_prush_ice;

    private List<UserDetailBean.TaskList> taskLists;
    private CommonRecyclerAdapter<UserDetailBean.TaskList> adapter;

    private UserDetailClubAdapter mDetailClubAdapter;


    private List<UserDetailClubBean.DataBean> clubList = new ArrayList<>();

    private UserDetailPresenter mPresenter;


    @Override
    protected UserDetailPresenter createPresenter() {
        return mPresenter = new UserDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_detail_basic;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        userId = arguments.getString("UserId");

        initAdapter();

        mvpPresenter.getUserGroup(userId, getContext());



        mPresenter.getUserDetail(getActivity(), userId);

    }



    public void setTaskLists(List<UserDetailBean.TaskList> taskLists) {
        this.taskLists.clear();
        this.taskLists.addAll(taskLists);
        //刷新个人任务
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {

    }

    private void initAdapter() {

        mDetailClubAdapter = new UserDetailClubAdapter(getContext(), clubList);
        clubRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        clubRecyclerView.setAdapter(mDetailClubAdapter);

        /**破冰任务*/
        taskLists = new ArrayList<>();
        rv_prush_ice.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_prush_ice.addItemDecoration(new SpacesItemDecoration(9));


        adapter = new CommonRecyclerAdapter<UserDetailBean.TaskList>(getContext(), taskLists, R.layout.item_mine_prush_ice_task) {
            @Override
            public void convert(CommonRecyclerHolder holder, UserDetailBean.TaskList taskList, int position) {
                ImageView img_state = holder.getView(R.id.img_state);
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_money = holder.getView(R.id.tv_money);
                TextView tv_state = holder.getView(R.id.tv_state);
                tv_title.setText(taskList.getCtaskcontent());
                if (!TextUtils.isEmpty(taskList.getIrewardamount() + "")) {
                    tv_money.setText("x" +taskList.getIrewardamount() + "/人");
                }
                if (taskList.getItasktype().equals("1")) {
                    img_state.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_club_mine_ice_pic));
                } else {
                    img_state.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_club_mine_ice_video));
                }
                if (TextUtils.equals(userId, TIMManager.getInstance().getLoginUser())) {
                    tv_state.setText("编辑");
                    tv_state.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shap_mine_ice_bg));
                } else {
                    if (!TextUtils.isEmpty(taskList.getItype())) {
                        if (taskList.getItype().equals("1")) {//1去完成 2已提交 3已完成
                            tv_state.setText("去完成");
                            tv_state.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shap_mine_ice_bg));
                        } else if (taskList.getItype().equals("2")) {
                            tv_state.setText("已提交");
                            tv_state.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shap_mine_ice_bg));
                        } else {
                            tv_state.setText("已完成");
                            tv_state.setBackground(null);
                        }
                    }
                }

                tv_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.equals(userId, TIMManager.getInstance().getLoginUser())) {
                            Bundle bundle = new Bundle();
                            bundle.putString("taskId", taskList.getItaskid());
                            bundle.putString("taskContent", taskList.getCtaskcontent());
                            bundle.putString("taskMoney", taskList.getIrewardamount() + "");
                            JumpUtil.overlay(getContext(), MyCrushIceTaskActivity.class, bundle);
                        } else {
                            if (UserInfoManger.getTaskCount().equals("10")) {
                                ToastUtils.showShortToast("今日提交任务次数已达到上限");
                                return;
                            }
                            if (!TextUtils.isEmpty(taskList.getItype())) {
                                if (taskList.getItype().equals("1")) {//1去完成 2已提交 3已完成
                                    //userid
                                    conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, userId);
                                    taskId = taskList.getItaskid();
                                    taskContent = taskList.getCtaskcontent();
                                    String selfGender = UserInfoManger.getGender();

                                    if (selfGender.equals("1")) {
                                        if (!TextUtils.equals(selfGender, Gender)) {
                                            if (taskList.getItasktype().equals("1")) {
                                                PictureSelectorUtils.choicePic(UserDetailBasicFragment.this, R.style.picture_default_style, 1);
                                            } else {
                                                PictureSelectorUtils.choiceVideo(UserDetailBasicFragment.this, R.style.picture_default_style, 1);
                                            }
                                        } else {
                                            VipSetGradeDialogFragment dialogFragment =
                                                    new VipSetGradeDialogFragment(true, Gravity.CENTER, "仅女生可以完成个人任务，获得红包奖励","确认");
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
                                    } else if (selfGender.equals("2")) {
                                        if (!TextUtils.equals(selfGender, Gender)) {
                                            if (taskList.getItasktype().equals("1")) {
                                                PictureSelectorUtils.choicePic(UserDetailBasicFragment.this, R.style.picture_default_style, 1);
                                            } else {
                                                PictureSelectorUtils.choiceVideo(UserDetailBasicFragment.this, R.style.picture_default_style, 1);
                                            }
                                        } else {
                                            VipSetGradeDialogFragment dialogFragment =
                                                    new VipSetGradeDialogFragment(true, Gravity.CENTER, "仅男生可以完成个人任务，获得红包奖励","确认");
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
                                    }
                                }
                            }
                        }

                    }
                });
            }
        };

        rv_prush_ice.setAdapter(adapter);
    }


    private void showddDialog(String s) {
        VipSetGradeDialogFragment dialogFragment = new VipSetGradeDialogFragment(true, Gravity.CENTER, s,"确认");
        dialogFragment.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {

                }
            }
        });
        dialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }


    /**
     * 处理用户信息内容展示
     *
     * @param
     */
    /**
     * 处理用户信息内容展示
     *
     * @param detail
     */
    private void convertLabel(UserDetail detail) {

        labelList.clear();


        String heightWeight = "";
        String homeTown = "";
        String schoolEducation = "";
        String occIndustry = "";
        String smokeDrink = "";
        String height = TextUtils.isEmpty(detail.getData().getHeight()) ? "" : detail.getData().getHeight() + "cm";
        String weight = TextUtils.isEmpty(detail.getData().getWeight()) ? "" : detail.getData().getWeight() + "kg";
        String education = TextUtils.isEmpty(detail.getData().getEducational()) ? "" : detail.getData().getEducational();
        String school = TextUtils.isEmpty(detail.getData().getSchool()) ? "" : detail.getData().getSchool();
        //职业
        String occupation = TextUtils.isEmpty(detail.getData().getOccupation()) ? "" : detail.getData().getOccupation();
        //行业
        String industry = TextUtils.isEmpty(detail.getData().getIndustry()) ? "" : detail.getData().getIndustry();
        String smoking = TextUtils.isEmpty(detail.getData().getSmoking()) ? "" : detail.getData().getSmoking();
        String drinkWine = TextUtils.isEmpty(detail.getData().getDrinkwine()) ? "" : detail.getData().getDrinkwine();
        String likeCuisine = TextUtils.isEmpty(detail.getData().getLikeCuisine()) ? "" : detail.getData().getLikeCuisine();

        homeTown =detail.getData().getHometown();



        if (!"".equals(height) && !"".equals(weight)) {
            heightWeight = height + "·" + weight;
        } else if (!"".equals(height)) {
            heightWeight = height;
        } else if (!"".equals(weight)) {
            heightWeight = weight;
        } else {
            heightWeight = null;
        }

        if (!"".equals(school) && !"".equals(education)) {
            schoolEducation = school + "·" + education;
        } else if (!"".equals(school)) {
            schoolEducation = school;
        } else if (!"".equals(education)) {
            schoolEducation = education;
        } else {
            schoolEducation = null;
        }

        if (!"".equals(industry) && !"".equals(occupation)) {
            occIndustry = industry + "·" + occupation;
        } else if (!"".equals(industry)) {
            occIndustry = industry;
        } else if (!"".equals(occupation)) {
            occIndustry = occupation;
        } else {
            occIndustry = null;
        }

        if (!"".equals(smoking) && !"".equals(drinkWine)) {
            smokeDrink = smoking + "·" + drinkWine;
        } else if (!"".equals(smoking)) {
            smokeDrink = smoking;
        } else if (!"".equals(drinkWine)) {
            smokeDrink = drinkWine;
        } else {
            smokeDrink = null;
        }

        if (!TextUtils.isEmpty(heightWeight)) {
            labelList.add(heightWeight);
        }

        if (!TextUtils.isEmpty(homeTown)) {
            labelList.add(homeTown);
        }

        if (!TextUtils.isEmpty(schoolEducation)) {
            labelList.add(schoolEducation);
        }
        if (!TextUtils.isEmpty(occIndustry)) {
            labelList.add(occIndustry);
        }
        if (!TextUtils.isEmpty(smokeDrink)) {
            labelList.add(smokeDrink);
        }
        if (!TextUtils.isEmpty(likeCuisine)) {
            labelList.add(likeCuisine);
        }


        if(labelList.size() == 0){
            tv_infomsg.setVisibility(View.GONE);
        }


        TagAdapter tagAdapter = new TagAdapter<String>(labelList) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_label_text, parent, false);
                textView.setText(s);
                return textView;
            }
        };

        mFlowLayout.setAdapter(tagAdapter);
    }



    @Override
    public void showDetail(UserDetail detail) {

        convertLabel(detail);

        try {
            taskLists.clear();
            taskLists.addAll(detail.getData().getTaskList());
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            LogUtil.e("data==null");
        }

        Gender = String.valueOf(detail.getData().getGender());
    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {

    }

    @Override
    public void showCircleArticle(CircleBean circleBean) {

    }

    @Override
    public void showPraise(PraiseIdBean o, int position) {

    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void showGroupList(UserDetailClubBean detailClubBean) {
        if (detailClubBean != null && detailClubBean.getData().size() > 0) {
            clubList.clear();
            clubList.addAll(detailClubBean.getData());
            mLayoutClub.setVisibility(View.VISIBLE);
            mDetailClubAdapter.notifyDataSetChanged();
        } else {
            mLayoutClub.setVisibility(View.GONE);
        }
    }

    @Override
    public void Pullblacksuccess() {

    }

    @Override
    public void upLoadPic(String url) {
        IceUtils.submitTask(url, taskId, TIMManager.getInstance().getLoginUser(), new TIMValueCallBack<IceSubmitBean>() {
            @Override
            public void onError(int i, String s) {
                ToastUtils.showShortToast(s);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(IceSubmitBean iceSubmitBean) {
                if (iceSubmitBean == null) return;
                UserInfoManger.saveTaskCount(iceSubmitBean.getTaskCount());
                mPresenter.getUserDetail(getActivity(), userId);
            }
        });
    }


    @Override
    public void fails(String error) {

    }

    @Override
    public void showLoading() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList.clear();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() == 0) {
                        ToastUtils.showShortToast("上传视频失败");
                        return;
                    }
                    String path = "";
                    if (RomUtil.isMiui()) {
                        if (!TextUtils.isEmpty(selectList.get(0).getAndroidQToPath()) && selectList.get(0).getAndroidQToPath().contains(".mp4")) {
                            path = selectList.get(0).getAndroidQToPath();
                        } else if (!TextUtils.isEmpty(selectList.get(0).getCompressPath()) && selectList.get(0).getCompressPath().contains(".mp4")) {
                            path = selectList.get(0).getCompressPath();
                        } else if (!TextUtils.isEmpty(selectList.get(0).getPath()) && selectList.get(0).getPath().contains(".mp4")) {
                            path = selectList.get(0).getPath();
                        }
                    } else {
                        if (!TextUtils.isEmpty(selectList.get(0).getAndroidQToPath()) && selectList.get(0).getAndroidQToPath().contains(".mp4")) {
                            path = selectList.get(0).getAndroidQToPath();
                        } else if (!TextUtils.isEmpty(selectList.get(0).getCompressPath()) && selectList.get(0).getCompressPath().contains(".mp4")) {
                            path = selectList.get(0).getCompressPath();
                        } else if (!TextUtils.isEmpty(selectList.get(0).getPath()) && selectList.get(0).getPath().contains(".mp4")) {
                            path = selectList.get(0).getPath();
                        }
                    }
                    if (TextUtils.isEmpty(path)) {
                        ToastUtils.showShortToast("上传视频失败");
                        return;
                    }
                    mPresenter.uploadFiles("4", "1", new File(path), SPUtils.getInstance(getActivity()).getString(Utils.USERID) + System.currentTimeMillis());
                    break;
                case PictureConfig.REQUEST_CAMERA://图片
                    selectList.clear();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() == 0) {
                        ToastUtils.showShortToast("上传图片失败");
                        return;
                    }
                    String path_pic = "";
                    if (!TextUtils.isEmpty(selectList.get(0).getCompressPath())) {
                        if (selectList.get(0).getCompressPath().contains(".jpeg") || selectList.get(0).getCompressPath().contains(".JPEG")) {
                            path_pic = selectList.get(0).getCompressPath();
                        } else if (selectList.get(0).getCompressPath().contains(".png") || selectList.get(0).getCompressPath().contains(".PNG")) {
                            path_pic = selectList.get(0).getCompressPath();
                        } else if (selectList.get(0).getCompressPath().contains(".gif") || selectList.get(0).getCompressPath().contains(".GIF")) {
                            path_pic = selectList.get(0).getCompressPath();
                        } else if (selectList.get(0).getCompressPath().contains(".webp") || selectList.get(0).getCompressPath().contains(".WEBP")) {
                            path_pic = selectList.get(0).getCompressPath();
                        }
                    } else if (!TextUtils.isEmpty(selectList.get(0).getAndroidQToPath())) {
                        if (selectList.get(0).getAndroidQToPath().contains(".jpeg") || selectList.get(0).getAndroidQToPath().contains(".JPEG")) {
                            path_pic = selectList.get(0).getAndroidQToPath();
                        } else if (selectList.get(0).getAndroidQToPath().contains(".png") || selectList.get(0).getAndroidQToPath().contains(".PNG")) {
                            path_pic = selectList.get(0).getAndroidQToPath();
                        } else if (selectList.get(0).getAndroidQToPath().contains(".gif") || selectList.get(0).getAndroidQToPath().contains(".GIF")) {
                            path_pic = selectList.get(0).getAndroidQToPath();
                        } else if (selectList.get(0).getAndroidQToPath().contains(".webp") || selectList.get(0).getAndroidQToPath().contains(".WEBP")) {
                            path_pic = selectList.get(0).getAndroidQToPath();
                        }
                    } else if (!TextUtils.isEmpty(selectList.get(0).getPath())) {
                        if (selectList.get(0).getPath().contains(".jpeg") || selectList.get(0).getPath().contains(".JPEG")) {
                            path_pic = selectList.get(0).getPath();
                        } else if (selectList.get(0).getPath().contains(".png") || selectList.get(0).getPath().contains(".PNG")) {
                            path_pic = selectList.get(0).getPath();
                        } else if (selectList.get(0).getPath().contains(".gif") || selectList.get(0).getPath().contains(".GIF")) {
                            path_pic = selectList.get(0).getPath();
                        } else if (selectList.get(0).getPath().contains(".webp") || selectList.get(0).getPath().contains(".WEBP")) {
                            path_pic = selectList.get(0).getPath();
                        }
                    }
                    if (TextUtils.isEmpty(path_pic)) {
                        ToastUtils.showShortToast("上传图片失败");
                        return;
                    }
                    mPresenter.uploadFiles("1", "1", new File(path_pic), SPUtils.getInstance(getActivity()).getString(Utils.USERID) + System.currentTimeMillis());
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        Gender = null;
        super.onDestroy();
    }
}
