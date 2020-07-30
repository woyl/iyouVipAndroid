package com.jfkj.im.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.github.iielse.imageviewer.ImageViewerBuilder;
import com.google.android.material.appbar.AppBarLayout;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.jfkj.im.Bean.GiftchatBean;
import com.jfkj.im.Bean.IceSubmitBean;
import com.jfkj.im.Bean.MyPicPhoto;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.ice.IceUtils;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.adapter.RankPagedrAdapter;
import com.jfkj.im.adapter.ViewPager2Adapter;

import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.imageviewer.MyCustomController;
import com.jfkj.im.imageviewer.MySimpleLoader;
import com.jfkj.im.imageviewer.MyTestDataProvider;
import com.jfkj.im.imageviewer.MyTransformer;
import com.jfkj.im.listener.onItemClickListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.userDetail.UserDetailPresenter;
import com.jfkj.im.mvp.userDetail.UserDetailView;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonCenterDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.DialogUserDetails;
import com.jfkj.im.ui.dialog.Dialog_gift;
import com.jfkj.im.ui.dialog.NoHintDialog;
import com.jfkj.im.ui.dialog.ReportDialog;
import com.jfkj.im.ui.fragment.UserDetailBasicFragment;
import com.jfkj.im.ui.fragment.UserDetailDynamicFragment;
import com.jfkj.im.ui.mine.PlayVideoActivity;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.RomUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videoPlay.CompleteView;
import com.jfkj.im.videoPlay.ErrorView;
import com.jfkj.im.widget.GiftBottomDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.widget.FocusNoLayoutManager;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tencent.imsdk.friendship.TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE_AND_ADD;

/**
 * <pre>
 * Description:  用户详情
 * @author :   ys
 * @date :         2019/11/22
 * </pre>
 */
public class UserDetailActivity extends BaseActivity<UserDetailPresenter> implements UserDetailView, onItemClickListener {

    @BindView(R.id.iv_user_head)
    ImageView mIvUserHead;
    @BindView(R.id.tv_user_name)
    AppCompatTextView mTvUserName;
    @BindView(R.id.tv_user_level)
    AppCompatTextView mTvUserLevel;
    @BindView(R.id.iv_user_sex)
    AppCompatImageView mIvUserSex;
    @BindView(R.id.tv_user_self_info)
    TextView mTvUserSelfInfo;
    @BindView(R.id.tv_last_online)
    AppCompatTextView mTvLastOnline;
    @BindView(R.id.layout_online)
    LinearLayout layoutOnline;
    @BindView(R.id.tv_describe)
    AppCompatTextView mTvDescribe;
//    @BindView(R.id.tv_dynamic)
//    ImageView tvDynamic;

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.iv_right)
    ImageView mIvRight;


    //    @BindView(R.id.recycler)
//    RecyclerView mRecycler;
    @BindView(R.id.iv_message)
    AppCompatImageView mIvMessage;
    @BindView(R.id.iv_gift)
    AppCompatImageView mIvGift;
    @BindView(R.id.layout_chat_gift)
    LinearLayout mLayoutChatGift;
    @BindView(R.id.iv_add_friend)
    AppCompatImageView mIvAddFriend;

    @BindView(R.id.iv_has_video)
    ImageView mIvHasVideo;
    @BindView(R.id.tv_replay)
    TextView mTvReplay;
    @BindView(R.id.edit_content)
    AppCompatEditText mEditContent;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.layout_comment)
    ConstraintLayout mLayoutComment;

    @BindView(R.id.iv_activity_brg)
    ImageView iv_activity_brg;

    LinkedList<String> list;
    List<Fragment> fragments;


    @BindView(R.id.tv_info)
    TextView tvInfo;

    @BindView(R.id.tv_dynamic_two)
    TextView tv_dynamic;

    @BindView(R.id.view_dynamic)
    View viewDynamic;

    @BindView(R.id.view_info)
    View viewInfo;

    @BindView(R.id.view_pager2)
    ViewPager2 view_pager2;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;


    private String userId;
    private String from;
    private UserDetailPresenter mPresenter;
    private ReportDialog mReportDialog;
    private CommonDialog blackHintDialog;
    private GiftBottomDialog giftDialog;
    private List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();

    private List<CircleBean.DataBean> mDynamicList = new ArrayList<>();
    private boolean isLike; // 是否点赞过
    private String userNickName;
    private NoHintDialog commentDialog;
    protected List<LocalMedia> selectImages = new ArrayList<>();
    Dialog_gift popupwindow_gift;
    //videoView
//    private VideoView mVideoView;
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    private ErrorView mErrorView;
    private CompleteView mCompleteView;
    private FocusNoLayoutManager mFocusNoLayoutManager;
    private String homeVideo = "";
    private List<String> mStringLabelList = new ArrayList<>();
    private String content;
    private int listPosition;
    private boolean iscomment = true;
    private String replayName;
    private int commentPosition;
    GiftBottomDialog mSheetDialog;

    private List<UserDetailClubBean.DataBean> clubList = new ArrayList<>();

    private String praiseid = "";
    private String userHeadUrl;
    private CommonCenterDialog noMoneyDialog = null;


    private String Gender;
    //照片
    private List<LocalMedia> selectList = new ArrayList<>();

    private String taskId, taskContent;
    private RankPagedrAdapter rankPagedrAdapter;


    private UserDetailBasicFragment userDetailBasicFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_detail;
    }


    @Override
    public UserDetailPresenter createPresenter() {
        return mPresenter = new UserDetailPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
            Log.d("@@@", "userID===" + userId);
        }



        mPresenter.getUserDetail(this,userId);


        getgiftlist(SPUtils.getInstance(mActivity).getString(Utils.GETGIFTLIST));


        list = new LinkedList<>();
        fragments = new ArrayList<>();


        list.add("信息");
        list.add("动态");

        userDetailBasicFragment = new UserDetailBasicFragment();
        UserDetailDynamicFragment userDetailDynamicFragment = new UserDetailDynamicFragment();

        Bundle bundle1 = new Bundle();
        bundle1.putString("UserId", userId);
        userDetailBasicFragment.setArguments(bundle1);
        userDetailDynamicFragment.setArguments(bundle1);


        fragments.add(userDetailBasicFragment);
        fragments.add(userDetailDynamicFragment);


        view_pager2.setOffscreenPageLimit(2);
        view_pager2.setUserInputEnabled(true);
        view_pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    tvInfo.setTextSize(20);
                    viewInfo.setVisibility(View.VISIBLE);
                    tvInfo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));  //加粗
                    tvInfo.setTextColor(Color.parseColor("#ffffff"));
                    tv_dynamic.setTextSize(14);
                    tv_dynamic.setTextColor(Color.parseColor("#ffbbbbbb"));
                    viewDynamic.setVisibility(View.GONE);
                    tv_dynamic.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                } else {

                    tv_dynamic.setTextSize(20);
                    viewDynamic.setVisibility(View.VISIBLE);
                    tv_dynamic.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));  //加粗
                    tv_dynamic.setTextColor(Color.parseColor("#ffffff"));
                    tvInfo.setTextSize(14);
                    tvInfo.setTextColor(Color.parseColor("#ffbbbbbb"));
                    viewInfo.setVisibility(View.GONE);
                    tvInfo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        });

        view_pager2.setAdapter(new ViewPager2Adapter(this, fragments));

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    mToolbarTitle.setText("");
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mToolbarTitle.setText(mTvUserName.getText().toString().trim());
                }
            }
        });
    }




    public void getgiftlist(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            //   toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {

                GiftchatBean giftchatBean = JSON.parseObject(s, GiftchatBean.class);
                List<GiftchatBean.DataBean.ArrayBean> array = giftchatBean.getData().getArray();
                for (int i = 0; i < array.size(); i++) {
                    GiftchatBean.DataBean.ArrayBean arrayBean = array.get(i);
                    GiftData.DataBean.ArrayBean bean = new GiftData.DataBean.ArrayBean(arrayBean.getGiftId(), arrayBean.getTop(), arrayBean.getPrice(), arrayBean.getPictureUrl(), arrayBean.getName(), arrayBean.getState());
                    giftList.add(bean);
                }
                popupwindow_gift = new Dialog_gift(mActivity, R.style.Dialog_Light, giftList, mTvUserName.getText().toString().trim());
                popupwindow_gift.setUserId(userId);
                mSheetDialog = new GiftBottomDialog(mActivity, R.style.Dialog_Light, giftList, this);
                mSheetDialog.setUserId(userId);
                giftDialog.setName(mTvUserName.getText().toString().trim());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDetail(UserDetail detail) {
//        Log.d("@@@@", "走这儿来加载数据啊");

        if (TextUtils.isEmpty(detail.getData().getHomeVideo())) {
            mIvHasVideo.setVisibility(View.GONE);
        } else {
            mIvHasVideo.setVisibility(View.VISIBLE);
            homeVideo = detail.getData().getHomeVideo();
        }
        Glide.with(this).load(detail.getData().getHead()).transform(new CircleCrop()).into(mIvUserHead);
        userHeadUrl = detail.getData().getHead();
        mTvUserName.setText(detail.getData().getUserNickName());

        userNickName = detail.getData().getUserNickName();
        if (!"".equals(detail.getData().getVipLevel())) {
            mTvUserLevel.setVisibility(View.VISIBLE);
            mTvUserLevel.setText("VIP" + detail.getData().getVipLevel());
        } else {
            mTvUserLevel.setVisibility(View.GONE);
        }
        if (detail.getData().getGender() == 2) {
            mIvUserSex.setBackgroundResource(R.mipmap.icon_female);

            Glide.with(this).load(R.mipmap.me_bg_male_female).into(iv_activity_brg);
        } else {
            mIvUserSex.setBackgroundResource(R.mipmap.icon_male);
            Glide.with(this).load(R.mipmap.me_bg_male_man).into(iv_activity_brg);

        }

        Gender = String.valueOf(detail.getData().getGender());
        UserDetailBasicFragment.Gender = Gender;
//        if ("3".equals(detail.getData().getVipCard())) {
//            mIvVipCard.setVisibility(View.VISIBLE);
//            mIvVipCard.setImageResource(R.drawable.vip_icon_ferrari);
//        } else {
//            mIvVipCard.setVisibility(View.GONE);
//        }

        if (detail.getData().getSignature() != null && !"".equals(detail.getData().getSignature())) {
            mTvDescribe.setText(detail.getData().getSignature());
        } else {
            mTvDescribe.setText("暂未添加个人描述…");
        }

//        convertLabel(detail);

        if (detail.getData().getHideOnline() == 1) {
//            layoutOnline.setVisibility(View.GONE);

            layoutOnline.setBackgroundResource(R.drawable.shape_online_gray);
            mTvLastOnline.setText("隐身");
        } else {
            if ("0".equals(detail.getData().getOnLine())) {
                mTvLastOnline.setText("在线");
            } else {
                mTvLastOnline.setText(detail.getData().getActiveTime());
            }
            layoutOnline.setVisibility(View.VISIBLE);
            layoutOnline.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_online_green));
        }

        String age = TextUtils.isEmpty(detail.getData().getAge()) ? "" : detail.getData().getAge() + "岁";
        String hometown = "";
        if (detail.getData().getHideLocation() == 1) {
            //隐藏
            hometown = "位置保密";
        } else {
            hometown = TextUtils.isEmpty(detail.getData().getPosition()) ? "" : detail.getData().getPosition();
        }
        String constellation = TextUtils.isEmpty(detail.getData().getConstellation()) ? "" : detail.getData().getConstellation();
        ;
        mTvUserSelfInfo.setText(hometown + "·" + constellation + "·" + age);

        if (UserInfoManger.getUserId().equals(userId)) {
            mLayoutChatGift.setVisibility(View.GONE);
            mIvAddFriend.setVisibility(View.GONE);
        } else if (detail.getData().isFriend()) {
            mLayoutChatGift.setVisibility(View.VISIBLE);
            mIvAddFriend.setVisibility(View.GONE);
        } else {
            mLayoutChatGift.setVisibility(View.GONE);
            mIvAddFriend.setVisibility(View.VISIBLE);
        }

        if (userDetailBasicFragment != null) {
            userDetailBasicFragment.setTaskLists(detail.getData().getTaskList());
        }

//        taskLists.clear();
//        taskLists.addAll(detail.getData().getTaskList());
//        adapter.notifyDataSetChanged();

    }

//    private void releaseVideoView() {
//        mVideoView.release();
//        if (mVideoView.isFullScreen()) {
//            mVideoView.stopFullScreen();
//        }
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        }
//        mCurPos = -1;
//    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.tv_info, R.id.tv_dynamic_two, R.id.iv_has_video, R.id.iv_back, R.id.iv_add_friend, R.id.iv_right, R.id.iv_gift, R.id.iv_message, R.id.iv_user_head, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_info:
                tvInfo.setTextSize(20);
                viewInfo.setVisibility(View.VISIBLE);
                tvInfo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));  //加粗
                tvInfo.setTextColor(Color.parseColor("#ffffff"));
                tv_dynamic.setTextSize(14);
                tv_dynamic.setTextColor(Color.parseColor("#ffbbbbbb"));
                viewDynamic.setVisibility(View.GONE);
                tv_dynamic.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                view_pager2.setCurrentItem(0);
//                fragment = getFragment(0);
//                switchFragment(tempFragemnt, fragment);
                break;
            case R.id.tv_dynamic_two:

                tv_dynamic.setTextSize(20);
                viewDynamic.setVisibility(View.VISIBLE);
                tv_dynamic.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));  //加粗
                tv_dynamic.setTextColor(Color.parseColor("#ffffff"));
                tvInfo.setTextSize(14);
                tvInfo.setTextColor(Color.parseColor("#ffbbbbbb"));
                viewInfo.setVisibility(View.GONE);
                tvInfo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                view_pager2.setCurrentItem(1);
//                fragment =   getFragment(1);
//                switchFragment(tempFragemnt, fragment);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add_friend:
                if (mSheetDialog != null) {
                    mSheetDialog.show();
                }
                break;
            case R.id.iv_gift:
                //  mvpPresenter.getGiftList(Utils.ANDROID, Utils.getVersionCode() + "", Utils.CHANNEL_ANDROID);

                if (popupwindow_gift != null) {
                    popupwindow_gift.show();
                }
                break;
            case R.id.iv_right:
                showDialog();
                break;
            case R.id.iv_message:

                Intent intentchat = new Intent(mActivity, ChatActivity.class);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(userId);
                chatInfo.setChatName(mTvUserName.getText().toString().trim());
                intentchat.putExtra(Constants.CHAT_INFO, chatInfo);
                intentchat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentchat);


                break;
            case R.id.iv_has_video:
                LocalMedia localMedia1 = new LocalMedia();
                localMedia1.setPath(homeVideo);
//                    PictureSelector.create(this).externalPictureVideo(localMedia.getPath());
                Intent intent = new Intent(this, PlayVideoActivity.class);
                intent.putExtra("video_url", homeVideo);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.iv_user_head:
                LocalMedia localMedia = new LocalMedia();
//                if (!TextUtils.isEmpty(homeVideo)) {
////                    localMedia.setPath(homeVideo);
////                    PictureSelector.create(this).externalPictureVideo(localMedia.getPath());
//
//
//                    Intent intent = new Intent(this, PlayVideoActivity.class);
//                    intent.putExtra("video_url", homeVideo);
//                    intent.putExtra("type","1");
//                    startActivity(intent);
//                } else {
                PictureParameterStyle style = new PictureParameterStyle();
                localMedia.setPath(userHeadUrl);
                selectImages.clear();
                selectImages.add(localMedia);
//                PictureSelector.create(this)
////                        .themeStyle(R.style.picture_default_style) // xml设置主题
//                        .setPictureStyle(style)// 动态自定义相册主题
//                        //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
//                        .isNotPreviewDownload(true)// 预览图片长按是否可以下载
//                        .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
//                        .openExternalPreview(0, selectImages);
//                }

                List<MyPicPhoto> myPicPhotoList = new ArrayList<>();
                myPicPhotoList.add(new MyPicPhoto(0, localMedia.getPath(), false));
                MyPicPhoto myPicPhoto = new MyPicPhoto(0, userHeadUrl, false);
                myPicPhoto.setData(myPicPhotoList);

                ImageViewerBuilder imageViewerBuilder = new ImageViewerBuilder(mActivity, new MySimpleLoader(), new MyTestDataProvider(myPicPhoto), new MyTransformer(), 0);
                MyCustomController myCustomController = new MyCustomController((FragmentActivity) mActivity);
                myCustomController.setType(0);
                myCustomController.setData(null, 1);
                myCustomController.init(imageViewerBuilder);
                imageViewerBuilder.setAllnum(1);
                imageViewerBuilder.show();
                break;
            case R.id.tv_send:
                if (Double.valueOf(UserInfoManger.getUserBanlance()).intValue() > 10) {
                    content = mEditContent.getText().toString();
                    KeyBoardUtils.hideKeybord(mEditContent);
                    if ("".equals(content)) {
                        ToastUtils.showShortToast("请输入评论内容");
                    } else {
                        if (iscomment) {
                            Log.d("@@@", "这个是回复别人");
//                            mvpPresenter.setComment(mDynamicList.get(listPosition).getCommenlist().get(commentPosition).getUserId(), mDynamicList.get(listPosition).getCommenlist().get(commentPosition).getArticleId(), content, "1", "", mDynamicList.get(listPosition).getCommenlist().get(commentPosition).getContentId());
                        } else {
                            Log.d("@@@", "这个进行评论");
                            mvpPresenter.setComment("", mDynamicList.get(listPosition).getArticleId(), content, "1", "", "0");
                        }
                    }
                } else {
                    showNoMoneyDialog();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 余额不足 需要充值提示
     */
    private void showNoMoneyDialog() {
        noMoneyDialog = new CommonCenterDialog(UserDetailActivity.this, R.style.dialogstyle, new CommonCenterDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_confirm) {
                    noMoneyDialog.dismiss();
                    showChargeDialog();
                } else if (view.getId() == R.id.tv_cancel) {
                    noMoneyDialog.dismiss();
                }
            }
        });
        noMoneyDialog.setTitleText("温馨提示").setContentText("您的余额不足请及时充值!").setConfirmBtnText("立即充值").setCancelBtnText("取消").show();
    }

    private void showChargeDialog() {
        ChargeDialog chargeDialog = new ChargeDialog(this, this);
        chargeDialog.show();
    }

    private void showDialog() {

        DialogUserDetails dialogUserDetails = new DialogUserDetails(new DialogUserDetails.ItemDialogClick() {
            @Override
            public void onItemClick(int viewId) {
                switch (viewId) {
                    case R.id.tv_jubao:

                        Bundle bundle = new Bundle();
                        bundle.putString("userId", userId);
                        JumpUtil.overlay(UserDetailActivity.this, ReportActivity.class, bundle);
                        break;
                    case R.id.tv_lahei:

                        showHintDialog();
                        break;

                }
            }
        }, this, false, mTvUserName.getText().toString());

        dialogUserDetails.show();


//        mReportDialog = new ReportDialog(this, R.style.MyDialog);
//        mReportDialog.setOnItemDialogClick(new ReportDialog.itemDialogClick() {
//            @Override
//            public void onItemClick(int viewId) {
//                switch (viewId) {
//                    case R.id.tv_report:
//                        mReportDialog.dismiss();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("userId", userId);
//                        JumpUtil.overlay(UserDetailActivity.this, ReportActivity.class, bundle);
//                        break;
//                    case R.id.tv_blacklist:
//                        mReportDialog.dismiss();
//                        showHintDialog();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        mReportDialog.show();
    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {
        giftList.clear();
        giftList.addAll(array);
        giftDialog = new GiftBottomDialog(this, R.style.Dialog_Light, giftList, this);

        giftDialog.setUserId(userId);
        giftDialog.show();
        giftDialog.setName(mTvUserName.getText().toString().trim());
    }

    /**
     * 朋友圈动态
     *
     * @param circleBean
     */
    @Override
    public void showCircleArticle(CircleBean circleBean) {

    }

    @Override
    public void showPraise(PraiseIdBean o, int position) {

    }

    /**
     * 拉黑提示dialog
     */
    private void showHintDialog() {
        blackHintDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        blackHintDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        blackHintDialog.dismiss();
                        mvpPresenter.blackList(userId, "1");
                        break;
                    default:
                        break;
                }
            }
        });
        blackHintDialog.setTitleText("是否拉黑对方?").setContentText("拉黑对方后将无法看到该用户的照片 视频，并且再也无法于ta聊天。")
                .setCancelBtnText("取消").setConfirmBtnText("确认").show();
    }


    /**
     * 私密评论弹窗
     *
     * @param position
     */
    private void showComment(int position) {
        commentDialog = new NoHintDialog(this, R.style.dialogstyle, new NoHintDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        commentDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        commentDialog.dismiss();
                        mLayoutComment.setVisibility(View.VISIBLE);
                        CircleBean.DataBean dataBean = mDynamicList.get(position);
                        listPosition = position;
                        iscomment = false;
                        KeyBoardUtils.requestShowKeyBord(mEditContent);
                        mEditContent.setHint("回复：" + dataBean.getNickName());
                        break;
                    default:
                        break;
                }
            }
        });

        commentDialog.setTitleText("温馨提示").setContentText("私密评论需花费10颗钻石").setCancelBtnText("取消")
                .setConfirmBtnText("确认").show();
    }


    @Override
    public void showSuccess() {
        Log.d("@@@", "listPosition === " + listPosition);
        mLayoutComment.setVisibility(View.GONE);
        //刷新评论部分


//        mvpPresenter.getArticleDynamic(mDynamicList.get(listPosition).getArticleId(),mDynamicList.get(listPosition).getCuserid(),true);

    }

    @Override
    public void showGroupList(UserDetailClubBean detailClubBean) {

    }

    @Override
    public void Pullblacksuccess() {
        finish();
    }

    @Override
    public void upLoadPic(String url) {

    }

    @Override
    public void fails(String error) {
        toastShow(error);
    }

    private void addFriend(String userId) {
        TIMFriendRequest timFriendRequest = new TIMFriendRequest(userId);
        timFriendRequest.setIdentifier(userId);
        timFriendRequest.setAddSource("android");
        timFriendRequest.setAddType(TIM_FRIEND_RESPONSE_AGREE_AND_ADD);
        timFriendRequest.setAddWording("isTempy");
        TIMFriendshipManager.getInstance().addFriend(timFriendRequest, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                toastShow(s);
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                if (timFriendResult.getResultCode() == 0) {
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
//        if (mVideoView == null || !mVideoView.onBackPressed()) {
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.getCode()) {
            case Utils.SET_USER_DYNAMIC_COMMENT:

                break;
            case Utils.SET_USER_DYNAMIC_LIKE:

                break;
        }
    }

    @Override
    public void onPositionClick(int position, View v) {

    }

    @Override
    public void onPositionClick(int position, View v, boolean b) {

    }
}
