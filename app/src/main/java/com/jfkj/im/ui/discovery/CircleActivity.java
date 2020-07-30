package com.jfkj.im.ui.discovery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.Test;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.adapter.dynamic.CircleDynamicAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.listener.ItemClickListener;
import com.jfkj.im.listener.onItemClickListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.discovery.circle.CirclePresenter;
import com.jfkj.im.mvp.discovery.circle.CircleView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.NoHintDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.ReportDialog;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.SoftKeyBoardListener;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.ViewUtils;
import com.jfkj.im.videoPlay.CompleteView;
import com.jfkj.im.videoPlay.ErrorView;
import com.jfkj.im.videoPlay.GestureView;
import com.jfkj.im.videoPlay.StandardVideoController;
import com.jfkj.im.videoPlay.VodControlView;
import com.jfkj.im.view.ByteLimitWatcher;
import com.jfkj.im.view.DividerItemDecoration;
import com.jfkj.im.widget.TitleView;
import com.luck.picture.lib.widget.FocusNoLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.nc.player.player.VideoView;
import com.nc.player.player.VideoViewManager;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.ResponseBody;


/**
 * <pre>
 * Description:  圈子
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public class CircleActivity extends BaseActivity<CirclePresenter> implements CircleView, SwipeRefreshLayout.OnRefreshListener, ItemClickListener, SwipeRecyclerView.LoadMoreListener, CircleDynamicAdapter.OnClickOutListener, OnItemClickListener {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.iv_title_right_icon1)
    AppCompatImageView mIvTitleRightIcon1;
    @BindView(R.id.iv_title_right_icon2)
    AppCompatImageView mIvTitleRightIcon2;
    @BindView(R.id.tv_unread_message)
    TextView mTvUnreadMessage;
    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.layout_comment)
    ConstraintLayout mLayoutComment;
    @BindView(R.id.tv_replay)
    TextView mTvReplay;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    private ReportDialog mDialog;
    private List<CircleBean.DataBean> mList = new ArrayList<>();
    private int pageNo = 1;
    private int pageSize = 10;
    private CircleDynamicAdapter mDynamicAdapter;
    private boolean isLike = false;
    private NoHintDialog mHintDialog = null;
    //videoView
    private VideoView mVideoView;
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    static TIMConversation conversation;
    protected int mLastPos = mCurPos;
    private StandardVideoController mController;
    private ErrorView mErrorView;
    private CompleteView mCompleteView;
    private FocusNoLayoutManager mFocusNoLayoutManager;
    private static final int POST_VIDEO = 0x123;
    private static final int POST_PICTURE = 0x124;
    private String parseId = ""; //点赞返回的id
    private boolean isRefresh = true;
    private TitleView mTitleView;
    private String content;
    private int listPosition;
    private String replayName;
    private static boolean iscomment;
    private int commentPosition; // 获取评论列表的position
    private MessageInfo messageInfo;
    private String contentId, userId;

    private boolean isOnActivityResult = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_circle;
    }

    @Override
    public CirclePresenter createPresenter() {
        return new CirclePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mTvTitle.setText("I you圈");
        setAndroidNativeLightStatusBar(mActivity, false);
        mIvTitleRightIcon2.setImageResource(R.mipmap.nav_icon_photo);
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        //初始化播放器
        initVideoView();
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(this);
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ISDYNAMIC, false);

        mvpPresenter.getFindDynamic(pageNo + "", pageSize + "");

        mDynamicAdapter = new CircleDynamicAdapter(this, mList, this, false);
        mFocusNoLayoutManager = new FocusNoLayoutManager(this);
        mRecycler.setLayoutManager(mFocusNoLayoutManager);
        mRecycler.setOnItemClickListener(this);
        mRecycler.setAdapter(mDynamicAdapter);
        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);
//        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        ((DefaultItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        mDynamicAdapter.setOutClickListener(this);
        mRecycler.loadMoreFinish(false, true);

//        mEditContent.addTextChangedListener(new ByteLimitWatcher(mEditContent,null,200));
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

        //   mvpPresenter.Circlefriend(1+"");
    }


    /**
     * 播放器设置
     */

    private void initVideoView() {
        mVideoView = new VideoView(this);
        mVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    ViewUtils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(this);
        mErrorView = new ErrorView(this);
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(this);
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(this);
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(this));
        mController.addControlComponent(new GestureView(this));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    public void showLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_right_icon1, R.id.iv_title_right_icon2, R.id.tv_send, R.id.swipe_refresh, R.id.ly, R.id.recycler, R.id.tv_unread_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_unread_message:
            case R.id.iv_title_right_icon1:
                JumpUtil.overlay(this, MyMessageActivity.class);
                break;
            case R.id.iv_title_right_icon2:
                //todo 发布动态
                showDialog();
                break;
            case R.id.tv_send:
//                content = mEditContent.getText().toString();
//                KeyBoardUtils.hideKeybord(mEditContent);
//                if ("".equals(content)) {
//                    ToastUtils.showShortToast("请输入评论内容");
//                } else {
//                    if (iscomment) {
//                        Log.d("@@@", "这个是回复别人");
//
////                        mvpPresenter.setComment(
////                                mList.get(listPosition).getCommenlist().get(commentPosition).getUserId(),
////                                mList.get(listPosition).getCommenlist().get(commentPosition).getArticleId(),
////                                content, "1", "",listPosition,contentId);
//                        mvpPresenter.setComment(
//                                userId,
//                                mList.get(listPosition).getCommenlist().get(commentPosition).getArticleId(),
//                                content, "1", "", listPosition, contentId, new TIMValueCallBack<Boolean>() {
//                                    @Override
//                                    public void onError(int i, String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Boolean aBoolean) {
//                                        if (aBoolean){
//                                            showPay();
//                                        }
//                                    }
//                                });
//                    } else {
//                        Log.d("@@@", "这个进行评论");
//                        mvpPresenter.setComment("", mList.get(listPosition).getArticleId(), content, "1", "", listPosition, "0", new TIMValueCallBack<Boolean>() {
//                            @Override
//                            public void onError(int i, String s) {
//
//                            }
//
//                            @Override
//                            public void onSuccess(Boolean aBoolean) {
//                                if (aBoolean){
//                                    showPay();
//                                }
//                            }
//                        });
//                    }
        }
//                break;


//        }
    }

    private void showPay() {
        PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                "您的余额不足，请及时充值！", "立即充值", true);
        dialog.setRsp(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
                    ChargeDialog mChargeDialog = new ChargeDialog(mActivity, mActivity);
                    mChargeDialog.show();
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        mEditContent.setText("");
        mLayoutComment.setVisibility(View.GONE);
    }

    private void showDialog() {
        mDialog = new ReportDialog(this, R.style.dialogstyle);
        mDialog.setFirst("发布照片").setSecond("发布视频").show();
        mDialog.setOnItemDialogClick(new ReportDialog.itemDialogClick() {
            @Override
            public void onItemClick(int viewId) {
                switch (viewId) {
                    case R.id.tv_report:
                        if (Utils.netWork()) {
                            //发布照片
//                            PermissionsUtil.requestPermission(getApplication(), new PermissionListener() {
//                                @Override
//                                public void permissionGranted(@NonNull String[] permissions) {
                            mDialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("POSTING_TYPE", "type_image");
                            JumpUtil.startForResult(CircleActivity.this, PostingActivity.class, POST_PICTURE, bundle);
//                                }
//
//                                @Override
//                                public void permissionDenied(@NonNull String[] permissions) {
//                                    mDialog.dismiss();
//                                    Toast.makeText(mActivity, "没有位置权限", Toast.LENGTH_LONG).show();
//                                }
//                            }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
                        } else {
                            toastShow(R.string.nonetwork);
                        }
                        break;
                    case R.id.tv_blacklist:
                        if (Utils.netWork()) {
//                            PermissionsUtil.requestPermission(getApplication(), new PermissionListener() {
//                                @Override
//                                public void permissionGranted(@NonNull String[] permissions) {
                            //发布视频
                            mDialog.dismiss();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("POSTING_TYPE", "type_video");
                            JumpUtil.startForResult(CircleActivity.this, PostingActivity.class, POST_PICTURE, bundle1);
//                                }
//
//                                @Override
//                                public void permissionDenied(@NonNull String[] permissions) {
//                                    mDialog.dismiss();
//                                    Toast.makeText(mActivity, "没有位置权限", Toast.LENGTH_LONG).show();
//                                }
//                            }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
                        } else {
                            toastShow(R.string.nonetwork);
                        }
                        break;
                    case R.id.tv_cancel:
                        mDialog.dismiss();
                        break;
                }
            }
        });
    }

    @Override
    public void showDynamic(CircleBean circleBean) {
        if (pageNo == 1) {
            mvpPresenter.addAccessSpace();
        }
        if (circleBean.getData() == null) {
            mRecycler.loadMoreFinish(false, false);
            return;
        }
        if (circleBean.getData().size() == 0) {
            mRecycler.loadMoreFinish(false, false);
            return;
        }
        if (isRefresh) {
            if (isOnActivityResult) {
                if (circleBean.getData().size() > 0) {
                    mList.add(0, circleBean.getData().get(0));
                    mDynamicAdapter.notifyItemInserted(0);
                    mDynamicAdapter.notifyItemRangeChanged(0, mList.size());
//                    mDynamicAdapter.setItem(mList);
                    isOnActivityResult = false;
                    mRecycler.scrollToPosition(0);
                }
            } else {
                mList.clear();
                mList.addAll(circleBean.getData());
                mDynamicAdapter.setmArticleBeanList(mList);
            }
//            mList.clear();
//            mList.addAll(circleBean.getData());
//            mDynamicAdapter.setmArticleBeanList(mList);
        } else {
            if (circleBean.getData().size() > 0) {
                mRecycler.loadMoreFinish(false, true);
                mDynamicAdapter.loadMoreData(circleBean.getData());
            } else {
                mRecycler.loadMoreFinish(false, false);
            }
        }
        if (circleBean.getData().size() < 10) {
            mRecycler.loadMoreFinish(false, false);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isOnActivityResult = true;
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 1;
//        mList.clear();
//        mDynamicAdapter.notifyDataSetChanged();
        mvpPresenter.getFindDynamic(pageNo + "", pageSize + "");

    }

    /**
     * 处理item点击事件
     * 评论
     *
     * @param position
     * @param view
     */
    @Override
    public void onPositionClick(int position, View view) {
        Log.d("@@@", "这是CircleActvitity:::mList" + position);
        switch (view.getId()) {

            case R.id.tv_discuss:


                iscomment = false;
                mTvReplay.setText("");
                mEditContent.setHint("");

                String flag = (!mList.get(position).getCuserid().equals(Utils.APPID) && UserInfoManger.getIfNeedShow()) + "";
                if (!mList.get(position).getCuserid().equals(Utils.APPID) && UserInfoManger.getIfNeedShow()) {
                    showHintDialog(position);
                } else {
                    mLayoutComment.setVisibility(View.VISIBLE);
                    CircleBean.DataBean dataBean = mList.get(position);
                    listPosition = position;
                    KeyBoardUtils.requestShowKeyBord(mEditContent);
                    iscomment = false;
                    //mTvReplay.setText("回复：" + dataBean.getNickName());
                    mTvReplay.setVisibility(View.GONE);


                }

                break;

        }

    }


    private void showHintDialog(int position) {
        mHintDialog = new NoHintDialog(this, R.style.dialogstyle, new NoHintDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        mHintDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        mHintDialog.dismiss();
                        mLayoutComment.setVisibility(View.VISIBLE);
                        CircleBean.DataBean dataBean = mList.get(position);
                        listPosition = position;
                        KeyBoardUtils.requestShowKeyBord(mEditContent);
                        mTvReplay.setVisibility(View.GONE);
//                        mEditContent.setHint("回复：" + dataBean.getNickName());
//                        Bundle bundle = new Bundle();
//                        bundle.putString("cuserId",mList.get(position).getCuserid());
//                        bundle.putString("articleId",mList.get(position).getArticleId());
//                        JumpUtil.overlay(CircleActivity.this, DynamicDetailActivity.class,bundle);
                        break;
                }
            }
        });

        mHintDialog.setTitleText("温馨提示").setContentText("私密评论需花费10颗钻石").setCancelBtnText("取消")
                .setConfirmBtnText("确认").show();
    }


    @Override
    public void onPositionClick(int position, View v, String b) {

        if (b.equals("1")) {

            Log.d("@@@", "这个是点赞过的，现在要走取消点赞");
            //已经点赞过 在点击要取消点赞
            parseId = mList.get(position).getPraiseId();
            mvpPresenter.getUserPraise(mList.get(position).getArticleId(), Utils.PRAISE_CANCEL, mList.get(position).getPraiseId(), position, b,v);
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);
            MessageInfo messageInfo = MessageInfoUtil.buildPraise("100", "4", mList.get(position).getCuserid());
            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(final int code, final String desc) {
                    OkLogger.e("");
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    OkLogger.e("");
                }
            });

        } else {
            //要点赞
            Log.d("@@@", "这个是没有点赞的，现在要进行点赞");
            isLike = true;
            mvpPresenter.getUserPraise(mList.get(position).getArticleId(), Utils.PRAISE_TRUE, "", position, b,v);
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);

            MessageInfo messageInfo = MessageInfoUtil.buildPraise("5", "4", mList.get(position).getCuserid());
            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(final int code, final String desc) {
                    OkLogger.e("");
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    OkLogger.e("");
                }
            });

        }
    }



    @Override
    public void showPraise(PraiseIdBean bean, int position, String b) {
        parseId = (bean.getData() == null ? "" : bean.getData().getPraiseid());

        if (b.equals("1")) {
            //在这里要做取消点赞的事情
            mList.get(position).setIsPraise(0);
            mList.get(position).setPraisecount(String.valueOf(Integer.valueOf(mList.get(position).getPraisecount()) - 1));
            mList.get(position).setPraiseId(bean.getData().getPraiseid());
            mDynamicAdapter.notifyItemChanged(position, "0");
        } else {
            //在这里要做点赞的事情
            mList.get(position).setIsPraise(1);
            mList.get(position).setPraisecount(String.valueOf(Integer.valueOf(mList.get(position).getPraisecount()) + 1));
            mList.get(position).setPraiseId(bean.getData().getPraiseid());
            mDynamicAdapter.notifyItemChanged(position, "1");
        }
    }

    /**
     * 评论成功
     */
    @Override
    public void showSuccess(int position) {

        mEditContent.setText("");
        mLayoutComment.setVisibility(View.GONE);

        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);
        if (iscomment) {


        } else {
            MessageInfo messageInfo = MessageInfoUtil.buildPraise("6", "4", mList.get(position).getCuserid());
            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(final int code, final String desc) {
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    onRefresh();
                }
            });
        }
    }

    @Override
    public void addAccessSpace() {

    }

    @Override
    public void CirclefriendSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            Test test = new Gson().fromJson(string, Test.class);
            List<Test.DataBean> dataBeanList = test.getData();
            for (int i = 0; i < dataBeanList.size(); i++) {
                Log.d("--->Commenlist", dataBeanList.get(i).getCommenlist().size() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.getCode()) {
            case Utils.SET_USER_DYNAMIC_COMMENT:
                mvpPresenter.getFindDynamic(pageNo + "", pageSize + "");
                break;
            case Utils.SET_USER_DYNAMIC_LIKE:
                mvpPresenter.getFindDynamic(pageNo + "", pageSize + "");
                break;
        }
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        isRefresh = false;
        mvpPresenter.getFindDynamic(pageNo + "", pageSize + "");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
        int message_number = Integer.parseInt(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, 0 + ""));
        if (message_number == 0) {
            mTvUnreadMessage.setVisibility(View.GONE);
            mIvTitleRightIcon1.setImageResource(R.mipmap.nav_icon_message_w);
        } else {
            mTvUnreadMessage.setVisibility(View.VISIBLE);
            mIvTitleRightIcon1.setImageResource(R.mipmap.mess_red);
            if (message_number <= 99) {
                mTvUnreadMessage.setText(message_number + "条未读消息");
            } else {
                mTvUnreadMessage.setText(99 + "+" + "条未读消息");
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress()) {
            return;
        }
        if (mVideoView == null || !mVideoView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void getClickName(String name, int outPosition, int position, String contentId, String userId) {
        Log.d("@@@", "这个position 是评论里面的position" + position);
        listPosition = outPosition;
        this.contentId = contentId;
        this.userId = userId;
        Log.d("@@@", "这个position 外面动态的outPosition" + outPosition);
        try {
//            if (mList.get(listPosition).getCommenlist().get(position).getUserId().equals(userId) && userId.equals(UserInfoManger.getUserId())) {
//                //  ToastUtils.showShortToast("不能自己回复自己");
//            } else {
//                replayName = name;
//                mLayoutComment.setVisibility(View.VISIBLE);
//                KeyBoardUtils.requestShowKeyBord(mEditContent);
//
////                mTvReplay.setText("回复：" + name);
//                mEditContent.setHint("回复：" + name);
//                iscomment = true;
//                Log.d("@@@", "这个是通用的发布者回复别人");
//                commentPosition = position;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
