package com.jfkj.im.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.jfkj.im.App;
import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.TIMReadPackage;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.adapter.dynamic.MineDynamicAdapter;
import com.jfkj.im.adapter.dynamic.MineGroupTaskAdapter;
import com.jfkj.im.adapter.fragment.MineAdapter;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.listener.LoadMoreOnScrollListener;
import com.jfkj.im.listener.SwipyAppBarScrollListener;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.mine.MinePresenter;
import com.jfkj.im.mvp.mine.MineView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.tab.TabLayoutMediator;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.activity.InviteFriendActivity;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.LottieDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.ReportDialog;
import com.jfkj.im.ui.dialog.UpgradeDialog;
import com.jfkj.im.ui.discovery.PostingActivity;
import com.jfkj.im.ui.party.MyCrushIceTaskActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ScrollLinearLayoutManager;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.SoftKeyBoardListener;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.StringUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.ViewUtils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.videoPlay.CompleteView;
import com.jfkj.im.videoPlay.ErrorView;
import com.jfkj.im.videoPlay.StandardVideoController;
import com.jfkj.im.view.WhiteCircleImageView;
import com.jfkj.im.view.flowlayout.FlowLayout;
import com.jfkj.im.view.flowlayout.TagAdapter;
import com.jfkj.im.view.flowlayout.TagFlowLayout;
import com.jfkj.im.widget.CircleImageView;
import com.jfkj.im.widget.TitleView;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.widget.FocusNoLayoutManager;
import com.lzy.okgo.utils.OkLogger;
import com.nc.player.player.VideoView;
import com.nc.player.player.VideoViewManager;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.liteav.trtc.impl.TRTCRoomInfo;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;
import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;


public class MineFragment extends BaseFragment<MinePresenter> implements MineView, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener, MineDynamicAdapter.onDynamicClickListener, MineDynamicAdapter.OnClickOutListener {
    static MineFragment mineFragment;
    @BindView(R.id.recyclerview)
    SwipeRecyclerView mRecyclerview;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_mine_setting)
    AppCompatImageView mIvMineSetting;
    @BindView(R.id.iv_user_head)
    CircleImageView mIvUserHead;
    @BindView(R.id.tv_user_name)
    AppCompatTextView mTvUserName;
    @BindView(R.id.iv_user_sex)
    AppCompatImageView mIvUserSex;
    @BindView(R.id.tv_user_age)
    AppCompatTextView mTvUserAge;
    @BindView(R.id.tv_user_address)
    AppCompatTextView mTvUserAddress;
    @BindView(R.id.tv_user_constellation)
    AppCompatTextView mTvUserConstellation;
    @BindView(R.id.tv_set_message)
    AppCompatTextView mTvSetMessage;
    @BindView(R.id.tv_describe)
    AppCompatTextView mTvDescribe;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.layout_balance)
    LinearLayout mLayoutBalance;
    @BindView(R.id.tv_gift)
    TextView mTvGift;
    @BindView(R.id.layout_gift)
    LinearLayout mLayoutGift;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.iv_photo)
    AppCompatImageView mIvPhoto;
    @BindView(R.id.layout_no_dynamic)
    LinearLayout mLayoutNoDynamic;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.layout_comment)
    ConstraintLayout mLayoutComment;
    @BindView(R.id.tv_replay)
    TextView mTvReplay;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.me_icon_invite_nor)
    AppCompatImageView me_icon_invite_nor;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapse_layout;
    @BindView(R.id.rv_group)
    RecyclerView rv_group;
    @BindView(R.id.tv_vip_level)
    TextView tv_vip_level;
    @BindView(R.id.tv_club)
    ImageView tv_club;

    @BindView(R.id.tv_head_type)
    TextView tv_head_type;
    @BindView(R.id.tv_infomsg)
    ImageView tv_infomsg;
    //    @BindView(R.id.iv_vip_card)
//    ImageView mVipcard;
    @BindView(R.id.flowLayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.rl_layout_title)
    RelativeLayout rl_title;

    @BindView(R.id.ll_smoking_drink_cuisine)
    LinearLayout ll_smoking_drink_cuisine;

    @BindView(R.id.tv_smoing)
    TextView tv_smoing;

    @BindView(R.id.tv_drink)
    TextView tvDrink;

    @BindView(R.id.tv_like_cuisine)
    TextView tvLikeCuisine;


    @BindView(R.id.tv_height_weight)
    TextView tv_height_weight;

    @BindView(R.id.tv_hometown)
    TextView tv_hometown;

    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;

    @BindView(R.id.ll_layout_two)
    LinearLayout linearLayout;


    /**破冰任务*/
    @BindView(R.id.rv_prush_ice)
    RecyclerView rv_prush_ice;

    @BindView(R.id.nested_scorollview)
    NestedScrollView nestedScrollView;


    @BindView(R.id.iv_mine_brg)
    ImageView iv_mine_brg;

    @BindView(R.id.tv_mine_title)
    TextView tvMineTitle;

    /**viewpager*/
    @BindView(R.id.deals_header_tab)
    TabLayout deals_header_tab;
    @BindView(R.id.view_pager2)
    ViewPager2 view_pager2;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.ll_bg_2)
    LinearLayout ll_bg_2;
    @BindView(R.id.fl_bg)
    FrameLayout fl_bg;


    private List<String> labelList = new ArrayList<>();
    private MineDynamicAdapter mAdapter;
    private List<String> mStringLabelList = new ArrayList<>();
    private List<String> mStringListYS = new ArrayList<>();
    private List<MineInfo.DataBean.CircleArticleBean> mArticleBeanList = new ArrayList<>();
    protected List<LocalMedia> selectImages = new ArrayList<>();
    private int pageSize = 10;
    private int pageNo = 1;
    private boolean isRefrush = true;
    private VideoView mVideoView;
    protected int mCurPos = -1;
    protected int mLastPos = mCurPos;
    private StandardVideoController mController;
    private ErrorView mErrorView;
    private CompleteView mCompleteView;
    private FocusNoLayoutManager mFocusNoLayoutManager;

    private boolean isFirst = true;
    private ReportDialog chooseTypeDialog = null;
    private static final int POST_DYNAMIC = 0x124;
    private static final int EDIT_DESCRIBE = 0x125;
    private String locality;
    private boolean isLike = false;
    private String praiseId = "";
    private int listPosition;
    private String content;
    private String replayName;
    private boolean iscomment = false;
    private int commentPosition;
    private UserDetail detail;
    private LinearLayoutManager linearLayoutManager;
    private CommonDialog exitDialog;
    private LottieDialog loadingDialog;
    private MineGroupTaskAdapter adapter;

    public static MineFragment getInstance() {
        if (mineFragment == null) {
            mineFragment = new MineFragment();
        }
        return mineFragment;
    }

    private UserGroupTaskBean userGroupTaskBean;

    /**
     * 破冰任务
     */
    private CommonRecyclerAdapter<UserDetailBean.TaskList> taskListCommonRecyclerAdapter;
    private List<UserDetailBean.TaskList> taskLists;
    private NumberFormat numberFormat;


    /**viewpager*/
    private MineAdapter mineAdapter;


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(this);
    }


    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshGroupTask(List<TIMGroupBaseInfo> carrier) {
        //刷新俱乐部数据

        mvpPresenter.getGroupTask(SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshDT(String type){
        if(type.equals("0x0001")){
            //删除动态成功刷新
            mvpPresenter.getMineInfo(pageSize + "", pageNo + "");
        }
    }


    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserInfo(EventMessage message) {
        if (message.getType() == REFRUSH_USER_BALANCE) {
            //VIP等级
            getUserDetails();
            //刷新余额数据
            mvpPresenter.getAccountBalance();
        }
    }


    /**
     * 刷新个人信息
     */
    public void refresh() {
        mTvBalance.setText(StringUtil.formatString(UserInfoManger.getUserBanlance()));
        mTvGift.setText(StringUtil.formatString(UserInfoManger.getUserGift()));
//        if ("3".equals(UserInfoManger.getVipCard())) {
//            //黑卡用户
//            mVipcard.setVisibility(View.VISIBLE);
//        } else {
//            mVipcard.setVisibility(View.GONE);
//        }

        //1 已审核  0未审核  2未通过

        if ("1".equals(UserInfoManger.getMineUserHeadState())) {
            mIvUserHead.setAlpha(180);
            tv_head_type.setText("审核中…");
        } else {
            tv_head_type.setText("");
        }


        Glide.with(getContext()).load(UserInfoManger.getMineUserHeadUrl()).transform(new CircleCrop()).into(mIvUserHead);

        mTvUserName.setText(UserInfoManger.getNickName());
        tvMineTitle.setText(UserInfoManger.getNickName());
        mTvUserConstellation.setText(UserInfoManger.getConstellation());
        if ("1".equals(UserInfoManger.getGender())) {
            mIvUserSex.setImageResource(R.mipmap.icon_male);
            iv_mine_brg.setBackgroundResource(R.mipmap.me_bg_male_man);
            rl_title.setBackgroundResource(R.mipmap.icon_man_img_mine);
        } else {
            mIvUserSex.setImageResource(R.mipmap.icon_female);

            iv_mine_brg.setBackgroundResource(R.mipmap.me_bg_male_female);
            rl_title.setBackgroundResource(R.mipmap.icon_wuman_img_mine);
        }
        if (!"".equals(UserInfoManger.getDescribe()) && UserInfoManger.getDescribe() != null) {
            mTvDescribe.setText(UserInfoManger.getDescribe());
        } else {
//            mTvDescribe.setVisibility(View.GONE);
            mTvDescribe.setText("暂未添加个人描述...");
        }

        //VIP等级
        tv_vip_level.setText("VIP" + UserInfoManger.getUserVipLevel());

        //年龄
        mTvUserAge.setText(UserInfoManger.getAge() + "岁");


//        if("".equals(UserInfoManger.getHometown())  && "0".equals(UserInfoManger.getUserHeight()) && "".equals(UserInfoManger.getSchool()) && "".equals(UserInfoManger.getDescribe()) && "".equals(UserInfoManger.getSmoking()) && "".equals(UserInfoManger.getLikeCuisine())){
//            tv_infomsg.setVisibility(View.GONE);
//        }else{
//            tv_infomsg.setVisibility(View.VISIBLE);
//        }
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
            heightWeight = null;
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
        if ("".equals(smoking) && "".equals(drinkWine) && "".equals(likeCuisine)) {
            ll_smoking_drink_cuisine.setVisibility(View.GONE);
        } else {
            ll_smoking_drink_cuisine.setVisibility(View.VISIBLE);

            if (!"".equals(smoking)) {
                tv_smoing.setText(smoking);
                tv_smoing.setVisibility(View.VISIBLE);
            } else {
                tv_smoing.setVisibility(View.GONE);
            }

            if (!"".equals(drinkWine)) {
                tvDrink.setText(drinkWine);
                tvDrink.setVisibility(View.VISIBLE);
            } else {
                tvDrink.setVisibility(View.GONE);
            }

            if (!"".equals(likeCuisine)) {
                tvLikeCuisine.setText(likeCuisine);
                tvLikeCuisine.setVisibility(View.VISIBLE);
            } else {
                tvLikeCuisine.setVisibility(View.GONE);
            }


        }

        //身高体重家乡
        if (TextUtils.isEmpty(heightWeight) && TextUtils.isEmpty(homeTown)) {
            ll_layout.setVisibility(View.GONE);
        } else {
            ll_layout.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(heightWeight)) {
                tv_height_weight.setVisibility(View.VISIBLE);
                tv_height_weight.setText(heightWeight);
            } else {
                tv_height_weight.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(homeTown)) {
                tv_hometown.setVisibility(View.VISIBLE);
                tv_hometown.setText(homeTown);
            } else {
                tv_hometown.setVisibility(View.GONE);
            }
        }


        if (labelList.size() > 0 && !"".equals(smoking) && !"".equals(drinkWine) && !"".equals(likeCuisine) && !heightWeight.equals("") && !"".equals(homeTown)) {
            tv_infomsg.setVisibility(View.VISIBLE);
        } else {
            tv_infomsg.setVisibility(View.GONE);
        }


        if (labelList.size() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(), false);

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        getData();

        mFocusNoLayoutManager = new FocusNoLayoutManager(getContext());
        mArticleBeanList.clear();
        mAdapter = new MineDynamicAdapter(getActivity(), this,mArticleBeanList, this);
        mAdapter.setOutClickListener(this);

        linearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        //  ((DefaultItemAnimator) mRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);

        mRecyclerview.useDefaultLoadMore();
        mRecyclerview.loadMoreFinish(false, true);
        mRecyclerview.setLoadMoreListener(this);
        mRecyclerview.setAutoLoadMore(false);
        rv_group.setLayoutManager(new ScrollLinearLayoutManager(getContext()));
        getLocation();
        addtips();

        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        mAppbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                mAppbar.post(new Runnable() {
                    @Override
                    public void run() {
                        if (state == State.COLLAPSED) {
                            toolbar.setBackgroundColor(getResources().getColor(R.color.colorff33343f));
                            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);

                            mToolbarTitle.setVisibility(View.VISIBLE);
                            mToolbarTitle.setText(UserInfoManger.getNickName());
                        } else if (state == State.EXPANDED || state == State.IDLE) {
                            toolbar.setBackgroundColor(getResources().getColor(R.color.color_00000000));
                            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);

                            mToolbarTitle.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });

        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mLayoutComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide() {
                mLayoutComment.setVisibility(View.GONE);
            }
        });
        test();


        adapter = new MineGroupTaskAdapter(getContext(), userGroupTaskBean);
        rv_group.setAdapter(adapter);


        getUserDetails();
        /**破冰任务*/
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
                    tv_money.setText(numberFormat.format(Double.parseDouble(taskList.getIrewardamount())));
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
                        bundle.putString("taskType", taskList.getItasktype());
                        JumpUtil.overlay(getContext(), MyCrushIceTaskActivity.class, bundle);
                    }
                });
            }
        };
        rv_prush_ice.setAdapter(taskListCommonRecyclerAdapter);
        numberFormat = NumberFormat.getInstance();


        //判断 nestedScrollView 是否滑到了底部
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                int height = dp2Px(70);

                if (scrollY <= 0) {
                    rl_title.setAlpha(0);
                } else if (scrollY > 0 && scrollY < height) {
                    //获取渐变率
                    float scale = (float) scrollY / height;
                    //获取渐变数值
                    float alpha = (1.0f * scale);
                    rl_title.setAlpha(alpha);

                } else {
                    rl_title.setAlpha(1f);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //到底了
                    // Toast.makeText(mActivity, "1111", Toast.LENGTH_SHORT).show();
                    onLoadMore();
                }
            }
        });


        /**viewpager*/
//        ll_bg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        ll_bg_2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nestedScrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        fl_bg.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mineAdapter = new MineAdapter(Objects.requireNonNull(getActivity()));
        view_pager2.setOffscreenPageLimit(2);
        view_pager2.setUserInputEnabled(true);
        view_pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        view_pager2.setAdapter(mineAdapter);

        new TabLayoutMediator(deals_header_tab, view_pager2, true, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("信息");
                } else {
                    tab.setText("动态");
                }
                View view = tab.getCustomView();
                if (view != null && view instanceof TextView){
                    TextView textView = (TextView) view;
                    textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.white));
                    textView.setTextSize(19);
                }

            }
        }).attach();

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2Px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }


    private void getLocation() {
        if (Utils.netWork()) {
            PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    initLocationOption();
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {

                    Toast.makeText(mActivity, "没有位置权限", Toast.LENGTH_LONG).show();
                }
            }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            toastShow(R.string.nonetwork);
        }
    }

    private void initLocationOption() {
        LocationClient locationClient = new LocationClient(mActivity);
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("gcj02");
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationOption.setLocationNotify(true);
        locationOption.setIgnoreKillProcess(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationPoiList(true);
        locationOption.SetIgnoreCacheException(false);
        locationOption.setOpenGps(true);
        locationOption.setIsNeedAltitude(false);
        locationOption.setOpenAutoNotifyMode();
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        locationClient.setLocOption(locationOption);
        locationClient.start();
    }

    @Override
    public void getClickName(String name, int outPosition, int position) {
        listPosition = outPosition;
        try {
            replayName = name;
            mLayoutComment.setVisibility(View.VISIBLE);
            KeyBoardUtils.requestShowKeyBord(mEditContent);
            mEditContent.setHint("回复：" + name);
            iscomment = true;
            commentPosition = position;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            locality = location.getCity() + "";
            mTvUserAddress.setText(locality);
            UserInfoManger.savecity(location.getCity());
            UserInfoManger.saveLongitude(location.getLongitude() + "");
            UserInfoManger.saveLatitude(location.getLatitude() + "");

        }
    }

    private void addtips() {
        mStringLabelList.clear();
        if (!"".equals(UserInfoManger.getUserHeight()) && UserInfoManger.getUserHeight() != null) {
            mStringLabelList.add(UserInfoManger.getUserHeight() + "cm");
        }
        if (!"".equals(UserInfoManger.getUserWeight()) && UserInfoManger.getUserWeight() != null) {
            mStringLabelList.add(UserInfoManger.getUserWeight() + "kg");
        }
        if (!"".equals(UserInfoManger.getUserEducation()) && UserInfoManger.getUserEducation() != null) {
            mStringLabelList.add(UserInfoManger.getUserEducation());
        }
        if (!"".equals(UserInfoManger.getHometown()) && UserInfoManger.getHometown() != null) {
            mStringLabelList.add(UserInfoManger.getHometown());
        }
        addLabel(mStringLabelList);
    }

    /**
     * layout_balance
     * 获取数据信息
     */
    private void getData() {
        mvpPresenter.getMineInfo(pageSize + "", pageNo + "");
        mvpPresenter.getAccountBalance();
        mvpPresenter.getGroupTask(SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));

    }

    @OnClick({
            R.id.iv_mine_video,
            R.id.iv_mine_setting, R.id.iv_mine_setting_two,
            R.id.layout_balance, R.id.layout_gift, R.id.iv_user_head,
            R.id.tv_set_message, R.id.iv_photo,
            R.id.tv_describe, R.id.tv_send, R.id.me_icon_invite_nor,
            R.id.iv_mine_person_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mine_video:
                JumpUtil.overlay(getContext(), UploadVideoActivity.class);
                break;
            case R.id.iv_mine_setting_two:
            case R.id.iv_mine_setting:
                JumpUtil.overlay(getContext(), SettingActivity.class);
                break;
            case R.id.tv_set_message:
            case R.id.iv_mine_person_setting:
                //个人信息
                JumpUtil.overlay(getContext(), UserInfoActivity.class);
                break;
            case R.id.iv_photo:
                showChooseTypeDialog();
                break;
            case R.id.layout_balance:
                JumpUtil.overlay(getContext(), AccountActivity.class);
                break;
            case R.id.layout_gift:
                JumpUtil.overlay(getContext(), GiftActivity.class);
                break;
//            case R.id.iv_edit:
            case R.id.tv_describe:
                Bundle bundle = new Bundle();
                bundle.putString("TAG", "describe");
                JumpUtil.startForResult(MineFragment.this, CommChangeActivity.class, EDIT_DESCRIBE, bundle);
                break;
            case R.id.tv_send:
                //发送评论
                content = mEditContent.getText().toString();
                KeyBoardUtils.hideKeybord(mEditContent);
                if ("".equals(content)) {
                    ToastUtils.showShortToast("请输入评论内容");
                } else {
//                    mvpPresenter.setComment(mArticleBeanList.get(listPosition).getCuserid(), mArticleBeanList.get(listPosition).getArticleId(), content, "1", "");
                    if (iscomment) {
                        Log.d("@@@", "这个是回复别人");
                        mvpPresenter.setComment(
                                mArticleBeanList.get(commentPosition).getCommenlist().get(listPosition).getUserId(),
                                mArticleBeanList.get(commentPosition).getCommenlist().get(listPosition).getArticleId(),
                                content, "1", mArticleBeanList.get(commentPosition).getCommenlist().get(listPosition).getContentId(), new TIMValueCallBack<Boolean>() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        if (aBoolean){
                                            showPay();
                                        }
                                    }
                                });
                    } else {
                        Log.d("@@@", "这个进行评论");
                        mvpPresenter.setComment("", mArticleBeanList.get(listPosition).getArticleId(), content, "1", "0", new TIMValueCallBack<Boolean>() {
                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                if (aBoolean){
                                    showPay();
                                }
                            }
                        });
                    }

                }
                break;
            case R.id.me_icon_invite_nor:
                startActivity(new Intent(mActivity, InviteFriendActivity.class));
                break;
            default:
                break;
        }
    }

    private void showPay() {
        PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                "您的余额不足，请及时充值！", "立即充值",true);
        dialog.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s){
                    ChargeDialog mChargeDialog = new ChargeDialog(mActivity,mActivity);
                    mChargeDialog.show();
                }
            }
        });
        if (getFragmentManager() != null) {
            dialog.show(getFragmentManager(), "");
        }
    }

    /**
     * 发布动态弹窗
     */
    private void showChooseTypeDialog() {
        chooseTypeDialog = new ReportDialog(getContext(), R.style.dialogstyle);
        chooseTypeDialog.setFirst("发布照片").setSecond("发布视频").show();
        chooseTypeDialog.setOnItemDialogClick(new ReportDialog.itemDialogClick() {
            @Override
            public void onItemClick(int viewId) {
                switch (viewId) {
                    case R.id.tv_report:
                        //发布照片
                        chooseTypeDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("POSTING_TYPE", "type_image");
                        JumpUtil.startForResult(MineFragment.this, PostingActivity.class, POST_DYNAMIC, bundle);
                        break;
                    case R.id.tv_blacklist:
                        //发布视频
                        chooseTypeDialog.dismiss();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("POSTING_TYPE", "type_video");
                        JumpUtil.startForResult(MineFragment.this, PostingActivity.class, POST_DYNAMIC, bundle1);
                        break;
                    case R.id.tv_cancel:
                        chooseTypeDialog.dismiss();
                        break;
                }
            }
        });
        chooseTypeDialog.show();
    }

    @Override
    public void onRefresh() {
        mvpPresenter.getGroupTask(SPUtils.getInstance(App.getAppContext()).getString(Utils.USERID));

        isRefrush = true;
        isFirst = true;
        pageNo = 1;
        mvpPresenter.getMineInfo(pageSize + "", pageNo + "");
        getLocation();

        getUserDetails();

        //刷新余额数据
        mvpPresenter.getAccountBalance();
    }


    public void test() {
        //禁用刷新
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapse_layout.getLayoutParams();

        //设置可以滑动
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapse_layout.getLayoutParams();
    }

    @Override
    public void showMineInfo(MineInfo info) {


        if (isRefrush) {
            mArticleBeanList.clear();
            mArticleBeanList.addAll(info.getData().getCircleArticle());
            mAdapter.notifyDataSetChanged();
            if (mArticleBeanList.size() > 0) {

//                mRecyclerview.setVisibility(View.VISIBLE);
                mLayoutNoDynamic.setVisibility(View.GONE);
            } else {

                mRecyclerview.setVisibility(View.GONE);
//                mLayoutNoDynamic.setVisibility(View.VISIBLE);
            }

        } else {
            if (info.getData().getCircleArticle().size() > 0) {

                mAdapter.addData(info.getData().getCircleArticle());
                mRecyclerview.loadMoreFinish(false, true);
                mAdapter.notifyDataSetChanged();
            } else {

                mRecyclerview.loadMoreFinish(false, false);

            }
        }


    }


    @Override
    public void showLoading() {

        loadingDialog = new LottieDialog(getActivity());
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }


        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getCode() == Utils.SET_USER_HEADER) {
            Glide.with(getContext()).load(UserInfoManger.getMineUserHeadUrl()).transform(new CircleCrop()).into(mIvUserHead);
        } else if (event.getCode() == Utils.SET_USER_NICKNAME) {
            mTvUserName.setText(UserInfoManger.getNickName());
        } else if (event.getCode() == Utils.SET_USER_DESCRIBE) {
            mTvDescribe.setText(UserInfoManger.getDescribe());
        } else if (event.getCode() == Utils.SET_USER_AGE) {
            mTvUserAge.setText(UserInfoManger.getAge());
        } else if (event.getCode() == Utils.SET_USER_HEIGHT) {
            mStringLabelList.clear();
            addtips();
        } else if (event.getCode() == Utils.SET_USER_WEIGHT) {
            mStringLabelList.clear();
            addtips();
        } else if (event.getCode() == Utils.SET_USER_EDUCATION) {
            mStringLabelList.clear();
            addtips();
        } else if (event.getCode() == Utils.SET_USER_DELETE_VIDEO) {
            //setHead();
        } else if (event.getCode() == Utils.SET_USER_UPLOAD_VIDEO) {
            //setHead();
        } else if (event.getCode() == Utils.SET_USER_HOMETOWN) {
            mStringLabelList.clear();
            addtips();
        }
    }

    private void addLabel(List<String> label) {
        for (int i = 0; i < label.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setBackgroundResource(R.drawable.shape_label_9);
            textView.setPadding(ScreenUtil.dpToPx(8), ScreenUtil.dpToPx(2), ScreenUtil.dpToPx(8), ScreenUtil.dpToPx(2));
            textView.setTextColor(getResources().getColor(R.color.color_666666));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            textView.setText(label.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, ScreenUtil.dpToPx(10), 0);
            textView.setLayoutParams(params);
//            mLayoutLabel.addView(textView);
        }
    }


    @Override
    public void showDelete(int position) {
        mAdapter.refreshDeleteData(position);
    }

    /**
     * 点赞成功或者取消点赞 过来更新ui
     *
     * @param o
     * @param position
     */
    @Override
    public void showPraise(String o, int position) {

        List<String> list = new ArrayList<>();
        hideLoading();
        if (isLike) {
            //取消点赞成功
            mArticleBeanList.get(position).setIsPraise("0");

            String praisecount = mArticleBeanList.get(position).getPraisecount();
            int i = Integer.parseInt(praisecount);
            i -= 1;
            OkLogger.e(i + "");
            mArticleBeanList.get(position).setPraisecount(String.valueOf(i));

            mAdapter.notifyItemChanged(position, "0");
        } else {
            mArticleBeanList.get(position).setIsPraise("1");
            praiseId = o;


            String praisecount = mArticleBeanList.get(position).getPraisecount();
            int i = Integer.parseInt(praisecount);
            i += 1;
            OkLogger.e(i + "");

            mArticleBeanList.get(position).setPraisecount(String.valueOf(i));
            mAdapter.notifyItemChanged(position, "1");
        }
    }

    /**
     * 评论成功
     */
    @Override
    public void showSuccess() {
        mEditContent.setText("");
        mLayoutComment.setVisibility(View.GONE);

        //评论成功 刷新评论数据


        mvpPresenter.findAllNewComment(mArticleBeanList.get(listPosition).getArticleId());


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
        } else {
            adapter.setData(null);
            tv_club.setVisibility(View.GONE);
        }

        // adapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccessBalance() {
        if (!"null".equals(UserInfoManger.getUserBanlance())) {
            mTvBalance.setText(StringUtil.formatString(UserInfoManger.getUserBanlance()));
        } else {
            mTvBalance.setText(0 + "");
        }
        if (!"null".equals(UserInfoManger.getUserGift())) {


            mTvGift.setText(StringUtil.formatString(UserInfoManger.getUserGift()));
        } else {
            mTvGift.setText(0 + "");
        }
    }

    @Override
    public void onFindOnNew(List<CommentBean> commentBean) {
        //查询成功

        mArticleBeanList.get(listPosition).setCommenlist(commentBean);

        mAdapter.setData(mArticleBeanList);

        mAdapter.notifyItemChanged(commentPosition, "3");

    }

    @Override
    public void getUserInfo(UserDetailBean userDetailBean) {

    }


    @Override
    public void onLoadMore() {
        isRefrush = false;
        isFirst = false;
        pageNo++;
        mvpPresenter.getMineInfo(pageSize + "", pageNo + "");
    }

    private void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        MineInfo.DataBean.CircleArticleBean dataBean = mArticleBeanList.get(position);
        mVideoView.setUrl(dataBean.getVideoPath());
        View itemView = mFocusNoLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        MineDynamicAdapter.ViewHolder viewHolder = (MineDynamicAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
//        mController.addControlComponent(viewHolder.mPrepareView, true);
        ViewUtils.removeViewFormParent(mVideoView);
        viewHolder.containerPlay.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list");
        mVideoView.start();
        mCurPos = position;
    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        mCurPos = -1;
    }


    private void showExitDialog(int position) {
        exitDialog = new CommonDialog(getActivity(), R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        exitDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        if (Utils.netWork()) {
                            mvpPresenter.deleteDynamic(mArticleBeanList.get(position).getArticleId(), position);
                            if (mVideoView != null) {
                                mVideoView.release();
                                mVideoView.stopTinyScreen();
                            }
                        } else {

                        }
                        exitDialog.dismiss();
                        break;
                }
            }
        });
        exitDialog.setTitleText("删除动态").setContentText("确认删除动态？").show();
    }


    @Override
    public void onPositionClick(int position, View v) {
        switch (v.getId()) {
            case R.id.tv_delete:

                showExitDialog(position);


                break;
            case R.id.player_container:
                Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                intent.putExtra("video_url", mArticleBeanList.get(position).getVideoPath());
                intent.putExtra("type","1");
                startActivity(intent);

                break;
            case R.id.comment_recycler:
                break;
            case R.id.tv_like:
            case R.id.lottie_animation_like:
                showLoading();

                if ("1".equals(mArticleBeanList.get(position).getIsPraise())) {
                    //点赞过的  点击要取消点赞
                    isLike = true;

                    mvpPresenter.getUserPraise(mArticleBeanList.get(position).getArticleId(), Utils.PRAISE_CANCEL, "".equals(praiseId) ? mArticleBeanList.get(position).getPraiseId() : praiseId, position,v);
                } else {
                    //点赞
                    isLike = false;
                    mvpPresenter.getUserPraise(mArticleBeanList.get(position).getArticleId(), Utils.PRAISE_TRUE, null, position,v);
                }
                break;
            case R.id.tv_discuss:
                mLayoutComment.setVisibility(View.VISIBLE);
                MineInfo.DataBean.CircleArticleBean articleBean = mArticleBeanList.get(position);
                listPosition = position;
                iscomment = false;
                KeyBoardUtils.requestShowKeyBord(mEditContent);
                mTvReplay.setText("回复：" + articleBean.getNickName());
                break;
        }
    }

    /**
     * 获取用户信息
     */


    public void getUserDetails() {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.USERID, UserInfoManger.getUserId());
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        Map<String, String> headMap = new HashMap<>();
        headMap.put(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN));
        headMap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        OkHttpUtils.post()
                .tag(App.getAppContext())
                .url(ApiStores.base_url + "/home/getUsertails")
                .headers(headMap)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        OkLogger.e("mine json2 --- >" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {
                                UserDetailBean userDetail = JSON.parseObject(response, UserDetailBean.class);

                                UserInfoManger.savaMineUserHeadUrl(userDetail.getData().getHead());

                                UserInfoManger.saveAge(userDetail.getData().getAge() + "");
                                UserInfoManger.saveTaskCount(userDetail.getData().getTaskCount());
                                mTvUserAge.setText(UserInfoManger.getAge() + "岁");

                                mTvUserName.setText(userDetail.getData().getUserNickName());
                                UserInfoManger.saveUserVipLevel(userDetail.getData().getVipLevel() + "");

                                tv_vip_level.setText("VIP" + UserInfoManger.getUserVipLevel());

                                UserInfoManger.saveMineUserHeadState(userDetail.getData().getHeadState() + "");

                                UserInfoManger.saveUserVideo(userDetail.getData().getHomeVideo());

                                if ("1".equals(UserInfoManger.getMineUserHeadState())) {
                                    mIvUserHead.setAlpha(180);
                                    tv_head_type.setText("审核中…");
                                } else {
                                    tv_head_type.setText("");
                                }
                                if (taskLists.size() != 0) {
                                    taskLists.clear();
                                }
                                taskLists.addAll(userDetail.getData().getTaskList());
                                taskListCommonRecyclerAdapter.notifyDataSetChanged();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == POST_DYNAMIC) {
                onRefresh();
            } else if (requestCode == EDIT_DESCRIBE) {
//                mIvEdit.setVisibility(View.GONE);
                mTvDescribe.setText(UserInfoManger.getDescribe());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
