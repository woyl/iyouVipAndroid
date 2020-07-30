package com.jfkj.im.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.github.iielse.imageviewer.ImageViewerBuilder;
import com.github.iielse.imageviewer.core.OverlayCustomizer;
import com.jfkj.im.ArticleDynamicBean;
import com.jfkj.im.Bean.MyPicPhoto;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.adapter.dynamic.CircleDynamicAdapter;
import com.jfkj.im.adapter.dynamic.CommentAdapter;
import com.jfkj.im.adapter.dynamic.ImageAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.entity.UserPraiseBean;
import com.jfkj.im.event.CommentEvent;
import com.jfkj.im.event.MineCircleEvent;
import com.jfkj.im.event.PraiseEvent;
import com.jfkj.im.imageviewer.MyCustomController;
import com.jfkj.im.imageviewer.MySimpleLoader;
import com.jfkj.im.imageviewer.MyTestDataProvider;
import com.jfkj.im.imageviewer.MyTransformer;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Home.DynamicDetailPresenter;
import com.jfkj.im.mvp.Home.DynamicDetailView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.DelDynamicDialog;
import com.jfkj.im.ui.dialog.NoHintDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.KeyBoardUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SoftKeyBoardListener;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.videoPlay.CompleteView;
import com.jfkj.im.videoPlay.ErrorView;
import com.jfkj.im.videoPlay.GestureView;
import com.jfkj.im.videoPlay.PrepareView;
import com.jfkj.im.videoPlay.StandardVideoController;
import com.jfkj.im.videoPlay.VodControlView;
import com.jfkj.im.view.FriendCircleView;
import com.jfkj.im.view.MyScaleImageView;
import com.jfkj.im.widget.GridItemDecoration;
import com.jfkj.im.widget.TitleView;

import com.luck.picture.lib.entity.LocalMedia;

import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 * Description:  评论详情
 * @author :   ys
 * @date :         2019/12/20
 * </pre>
 */
public class DynamicDetailActivity extends BaseActivity<DynamicDetailPresenter> implements DynamicDetailView, CommentAdapter.onCommentClick, DelDynamicDialog.OnDelClickLister {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;

    @BindView(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @BindView(R.id.iv_user_head)
    AppCompatImageView mIvUserHead;
    @BindView(R.id.tv_user_name)
    AppCompatTextView mTvUserName;
    //    @BindView(R.id.tv_time)
//    AppCompatTextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    //    @BindView(R.id.tv_address)
//    AppCompatTextView mTvAddress;
    @BindView(R.id.tv_discuss)
    AppCompatTextView mTvDiscuss;
    @BindView(R.id.tv_like)
    AppCompatTextView mTvLike;
    @BindView(R.id.comment_recycler)
    RecyclerView commentRecycler;
    @BindView(R.id.edit_content)
    AppCompatEditText mEditContent;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.iv_like)
    ImageView mIvLike;
    @BindView(R.id.recycler_image)
    RecyclerView imageRecycler;

    @BindView(R.id.layout_like)
    LinearLayout mLayoutLike;

    @BindView(R.id.layout_comment)
    ConstraintLayout layoutComment;




    @BindView(R.id.discuss_num)
    TextView discuss_num;

    @BindView(R.id.f_video_layout)
    FrameLayout  frameLayout;

    @BindView(R.id.iv_video_brg)
    MyScaleImageView iv_video_brg;

    @BindView(R.id.tv_diamonds)
    AppCompatTextView tv_diamonds;

    @BindView(R.id.fl_comment)
    FrameLayout fl_comment;





    private boolean isLike;
    private List<LocalMedia> imageUrlList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private String articleId;
    private String content;
    private String commentId;
    private String praisecount;
    private ImageView mIvThumb;
    private String cuserid;
    private String ruserId;
    private String praiseId = "";
    private List<CommentBean> commenlist = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private String type = "1";
    private NoHintDialog mHintDialog = null;
    private int num;
    private String replayName;
    private boolean iscomment = false;
    private int listPosition;
    private NoHintDialog chargeHintDiag = null;

    private List<String> lists;
    private String contentId;
    private String startType;
    private CommonDialog exitDialog;
    private DelDynamicDialog delDynamicDialog;
    private int commenCount;
    private MessageInfo messageInfo;
    static TIMConversation conversation;
    public final static int RESULT = 105;
    private int position ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_detail;
    }

    @Override
    public DynamicDetailPresenter createPresenter() {
        return new DynamicDetailPresenter(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setAndroidNativeLightStatusBar(this, false);
        if (bundle != null) {
            cuserid = bundle.getString("cuserId");
            articleId = bundle.getString("articleId");
            startType = bundle.getString("startType", "0");
            position = bundle.getInt("position",0);

            mvpPresenter.getArticleDynamic(articleId, cuserid, false);
        }
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                layoutComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide() {
                layoutComment.setVisibility(View.GONE);
            }
        });
        lists = new ArrayList<>();
//        query();

        if(startType.equals("1")){
            //从我的页面跳转到详情
            mIvTitleRight.setVisibility(View.VISIBLE);
        }else{
            mIvTitleRight.setVisibility(View.GONE);
        }

    }




    private void showExitDialog(int position) {
        exitDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        exitDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        if (Utils.netWork()) {
                            mvpPresenter.deleteDynamic(articleId, position);
                        } else {

                        }
                        exitDialog.dismiss();
                        break;
                }
            }
        });
        exitDialog.setTitleText("删除动态").setContentText("确认删除动态？").show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void showView(ArticleDynamicBean articleDynamicBean) {

        ArticleDynamicBean.DataBean circleBean = articleDynamicBean.getData().get(0);
        mTvTitle.setText("详情");
        Glide.with(this).load(circleBean.getHead()).transform(new CircleCrop()).into(mIvUserHead);

        mTvUserName.setText(circleBean.getNickName());

        mTvContent.setText(circleBean.getContent());




        commenCount = circleBean.getCommenCount();



        if(!circleBean.getPraisecount().equals("0")){
            mTvLike.setText(circleBean.getPraisecount());
        }

        if(circleBean.getCommenlist().size() != 0){
            mTvDiscuss.setText(circleBean.getCommenlist().size() + "");
        }



        if (circleBean.getPraiseId() != null) {
            praiseId = circleBean.getPraiseId();
        }

        if (circleBean.getIsPraise() == 1) {
            mIvLike.setBackgroundResource(R.mipmap.paise_red);
            isLike = true;
        } else {
            mIvLike.setBackgroundResource(R.mipmap.paise);
            isLike = false;
        }

        initImageAndVideo(circleBean);

        if (circleBean.getCommenlist() != null && circleBean.getCommenlist().size() > 0) {
            commentRecycler.setVisibility(View.VISIBLE);
        } else {
            commentRecycler.setVisibility(View.GONE);
        }
        initComment(circleBean);
        articleId = circleBean.getArticleId();
        commentId = circleBean.getCuserid();
        praisecount = circleBean.getPraisecount();
        commenCount = circleBean.getCommenCount();
//        discuss_num.setText(circleBean.getPraisecount() + "次赞、共" + circleBean.getCommenCount() + "条评论");

        discuss_num.setText(circleBean.getLocation()+"·" + circleBean.getAdddate());


        long diamonds = Long.parseLong(circleBean.getCommenDiamonds());
        if (diamonds > 0) {
            String htmls ="已获得"+ "<font> "+ circleBean.getCommenCount()+"</font>"+"条评论，收到"+ "<font color = '#FF2B66'> "+ circleBean.getCommenDiamonds()+"</font>";
            tv_diamonds.setText(Html.fromHtml(htmls));
            fl_comment.setVisibility(View.VISIBLE);
        } else {
            fl_comment.setVisibility(View.GONE);
        }

    }

    @Override
    public void showDelete(int position) {
        //删除成功发条消息,更新页面
        EventBus.getDefault().post("0x0001");

        ToastUtils.showShortToast("删除成功");
        finish();
    }


    private void initImageAndVideo(ArticleDynamicBean.DataBean bean) {

        if (!"".equals(bean.getVideoPath()) && bean.getVideoPath() != null) {

            imageRecycler.setVisibility(View.GONE);

            frameLayout.setVisibility(View.VISIBLE);

            iv_video_brg.test(bean.getVideoImages().getWidth(),bean.getVideoImages().getHeight(),this);

            Glide.with(this).load(bean.getVideoImages().getUrl()).into(iv_video_brg);


//            initVideo(bean);
        } else if (!"".equals(bean.getImages()) && bean.getImages() != null) {

            imageRecycler.setVisibility(View.VISIBLE);

            frameLayout.setVisibility(View.GONE);

            setRecyclerImage(bean);
        } else {

        }

    }


    private void setRecyclerImage(ArticleDynamicBean.DataBean bean) {



        imageUrlList = new ArrayList<>();
        ArrayList<String> lists = new ArrayList<>();



        for (int i = 0 ; i <bean.getPicImages().size() ; i ++){
            LocalMedia imageBean = new LocalMedia();

            imageBean.setPath(bean.getPicImages().get(i).getUrl());
            imageBean.setWidth(bean.getPicImages().get(i).getWidth());
            imageBean.setHeight(bean.getPicImages().get(i).getHeight());

            imageUrlList.add(imageBean);
            lists.add(bean.getPicImages().get(i).getUrl());
        }


        if (imageUrlList.size() >= 3) {
            if (imageUrlList.size() == 4) {
                GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
                imageRecycler.setLayoutManager(layoutManager);
                GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 2);
                if (imageRecycler.getItemDecorationCount() == 0) {
                    imageRecycler.addItemDecoration(itemDecoration);
                }
                num = 4;
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                imageRecycler.setLayoutManager(layoutManager);
                GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 3);
                if (imageRecycler.getItemDecorationCount() == 0) {
                    imageRecycler.addItemDecoration(itemDecoration);
                }
                num = 3;
            }
        } else if (imageUrlList.size() == 2) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            imageRecycler.setLayoutManager(layoutManager);
            GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 2);
            if (imageRecycler.getItemDecorationCount() == 0) {
                imageRecycler.addItemDecoration(itemDecoration);
            }
            num = 2;
        } else if (imageUrlList.size() == 1) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            imageRecycler.setLayoutManager(layoutManager);
            GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 1);
            if (imageRecycler.getItemDecorationCount() == 0) {
                imageRecycler.addItemDecoration(itemDecoration);
            }
            num = 1;
        }


        imageAdapter = new ImageAdapter(this, imageUrlList, num);
        imageRecycler.setAdapter(imageAdapter);
        imageRecycler.setFocusableInTouchMode(false);
        imageRecycler.requestFocus();
        imageRecycler.setNestedScrollingEnabled(false);

        imageAdapter.notifyDataSetChanged();


        boolean isShow = false;
        if (bean.getIsPraise() == 1) {
            isShow = true;
        }

        boolean finalIsShow = isShow;
        imageAdapter.setOnListener(new onItemPositionClickListener() {
            @Override
            public void onPositionClick(int position) {
//                onClicks(position, parentPosition);

                PhotoViewer.INSTANCE.setData(lists).setImgContainer(imageRecycler).setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public void onLongClick(@NotNull View view) {

                    }
                })
                        .showView(false)
                        .setData(bean.getAdddate(), bean.getContent(), bean.getPraisecount(), bean.getCommenCount() + "", finalIsShow, 0)
                        .setCurrentPage(position)
                        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(@NotNull ImageView iv, @NotNull String url) {
                                Glide.with(DynamicDetailActivity.this).load(url).into(iv);
                            }
                        }).start(DynamicDetailActivity.this);
            }
        });
    }





    private void initComment(ArticleDynamicBean.DataBean circleBean) {
        // 评论部分
        commenlist = circleBean.getCommenlist();
        commentAdapter = new CommentAdapter(this, 0, commenlist, this);
        commentRecycler.setNestedScrollingEnabled(false);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentRecycler.setAdapter(commentAdapter);
        commentRecycler.setFocusableInTouchMode(false);
        commentRecycler.requestFocus();
        commentAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_title_right,R.id.iv_title_back, R.id.layout_like, R.id.tv_send, R.id.tv_discuss})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_right:

/**底部弹出*/
//                delDynamicDialog = new DelDynamicDialog(this,this);
//                delDynamicDialog.show();
                showExitDialog(0);
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.layout_like:
                if (isLike) {
                    mvpPresenter.getUserPraise(articleId, Utils.PRAISE_CANCEL, praiseId,view);

                    conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);
                    MessageInfo messageInfo = MessageInfoUtil.buildPraise("100", "4", cuserid);
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
                    mvpPresenter.getUserPraise(articleId, Utils.PRAISE_TRUE, null,view);

                    conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);

                    MessageInfo messageInfo = MessageInfoUtil.buildPraise("5", "4", cuserid);
                    conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(final int code, final String desc) {


                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {

                        }
                    });

                }
                break;
            case R.id.tv_send:
                if (check()) {


                    if (iscomment) {

                        Log.d("@@@", "这个是回复别人");
                        mvpPresenter.setComment(
                                ruserId,
                                articleId,
                                content, "1", "", "0", new TIMValueCallBack<Boolean>() {
                                    @Override
                                    public void onError(int i, String s) {
                                        ToastUtils.showLongToast(s);
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
                        mvpPresenter.setComment("", articleId, content, "1", "", "0", new TIMValueCallBack<Boolean>() {
                            @Override
                            public void onError(int i, String s) {
                                ToastUtils.showLongToast(s);
                            }

                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                if (aBoolean){
                                    showPay();
                                }
                            }
                        });
                    }

//                    if (iscomment){
//                        mvpPresenter.setComment(cuserid,articleId,content,type,"");
//                    }else {
//                        mvpPresenter.setComment(cuserid,articleId,content,type,"");
//                    }
                }
                break;
            case R.id.tv_discuss:
                iscomment = false;
                if (UserInfoManger.getUserId().equals(cuserid)) break;
                if (UserInfoManger.getIfNeedShow() && !UserInfoManger.getUserId().equals(cuserid)) {
                    showHintDialog();
                } else {
                    layoutComment.setVisibility(View.VISIBLE);
                    KeyBoardUtils.requestShowKeyBord(mEditContent);
                }
                break;
        }
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



    private void showHintDialog() {
        mHintDialog = new NoHintDialog(this, R.style.dialogstyle, new NoHintDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        mHintDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        mHintDialog.dismiss();
                        layoutComment.setVisibility(View.VISIBLE);
                        KeyBoardUtils.requestShowKeyBord(mEditContent);
                        break;
                }
            }
        });

        mHintDialog.setTitleText("温馨提示").setContentText("私密评论需花费10颗钻石").setCancelBtnText("取消")
                .setConfirmBtnText("确认").show();
    }

    /**
     * 点赞改变
     */
    @Override
    public void showPraise(UserPraiseBean bean,View v) {

        if (isLike) {
            mIvLike.setBackgroundResource(R.mipmap.paise);
            //mTvLike.setText();


            if((Integer.valueOf(praisecount)) == 1 ){
                mTvLike.setText( "");
            }else{
                mTvLike.setText((Integer.valueOf(praisecount) - 1) + "");
            }





//            discuss_num.setText(String.valueOf(Integer.valueOf(praisecount) - 1) + "次赞、共" + commenCount  + "条评论");

            praisecount = String.valueOf(Integer.valueOf(praisecount) - 1);

        } else {
            praiseId = bean.getData().getPraiseid();

            mIvLike.setBackgroundResource(R.mipmap.paise_red);

            //discuss_num.setText(String.valueOf(Integer.valueOf(praisecount) + 1) + "次赞、共" + commenCount + "条评论");
            mTvLike.setText((Integer.valueOf(praisecount) + 1) + "");

            praisecount = String.valueOf(Integer.valueOf(praisecount) + 1);
        }
        isLike = !isLike;
        EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_DYNAMIC_LIKE, ""));
        EventBus.getDefault().post(new PraiseEvent(position,v));
    }

    public void query() {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put(Utils.ARTICLEID, articleId);
        map.put(Utils.RUSERID, cuserid);
        Map<String, String> headmap = new HashMap<>();

        headmap.put(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN));
        headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        OkHttpUtils.post()
                .tag(mActivity)
                .url(ApiStores.base_url + "/find/getArticleDynamic")
                .headers(headmap)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("10004")) {
                                toastShow(jsonObject.getString("message"));
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showSuccess() {
        KeyBoardUtils.hideKeybord(mEditContent);
        mEditContent.setText("");
        mvpPresenter.getArticleDynamic(articleId, cuserid, true);


        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);

        if (iscomment) {

            messageInfo = MessageInfoUtil.buildPraise("6", "4", cuserid);

            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(final int code, final String desc) {

                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                }
            });
        } else {
            MessageInfo messageInfo = MessageInfoUtil.buildPraise("6", "4", cuserid);
            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(final int code, final String desc) {

                }

                @Override
                public void onSuccess(TIMMessage timMessage) {

                }
            });
        }


        //评论成功

        try {
            String counts = mTvDiscuss.getText().toString().trim();
            if (TextUtils.isEmpty(counts)){
                counts = "0";
            }
            int count =  Integer.parseInt(counts);
            count++;
            mTvDiscuss.setText(count+"");
            EventBus.getDefault().post(new CommentEvent(position,count));
        }catch (Exception e){

        }



    }

    /**
     * 评论完成请求接口 显示评论内容
     *
     * @param o
     */
    @Override
    public void refreshData(CircleBean o) {

    }

    /**
     * 刷新评论部分
     *
     * @param bean
     */
    @Override
    public void refreshComment(ArticleDynamicBean bean) {
        commentRecycler.setVisibility(View.VISIBLE);

        discuss_num.setText( praisecount    + "次赞、共" + commenCount  + "条评论");


        //   mTvDiscuss.setText(bean.getData().get(0).getPraisecount());
        // 评论部分
        if (commentAdapter != null) {
            commenlist.clear();
            commenlist.addAll(bean.getData().get(0).getCommenlist());
            commentAdapter.notifyDataSetChanged();
        } else {
            commentAdapter = new CommentAdapter(this, 0, bean.getData().get(0).getCommenlist(), this);
            commentAdapter.notifyDataSetChanged();
        }

        EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_DYNAMIC_COMMENT, ""));
    }

    private boolean check() {
        content = mEditContent.getText().toString();
        if ("".equals(content)) {
            ToastUtils.showShortToast("请输入评论内容");
            return false;
        }

//        if (UserInfoManger.getUserBanlance())
        return true;
    }

    @Override
    public void getReplayName(String name, int outPosition, int position, String contentId,String userId) {
        Log.d("@@@", "这个position 是评论里面的position" + position);
        listPosition = position;
        this.contentId = contentId;
        if (commenlist.get(0).getUserId().equals(userId) && userId.equals(UserInfoManger.getUserId())) {
            Log.d("--->commenlist", "不能自己回复自己");
            return;
        }
        Log.d("@@@", "这个position 外面动态的outPosition" + outPosition);
        try {
//            if (commenlist.get(position).getUserId().equals(UserInfoManger.getUserId())){
//             //   ToastUtils.showShortToast("不能自己回复自己");
//            }else {
            replayName = name;
            layoutComment.setVisibility(View.VISIBLE);
            KeyBoardUtils.requestShowKeyBord(mEditContent);
            mEditContent.setHint("回复：" + name);
            iscomment = true;
//            ruserId = commenlist.get(position).getUserId();
            ruserId = userId;
            Log.d("@@@", "这个是通用的发布者回复别人");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 点击删除动态
     */
    @Override
    public void onClickDel(View v) {
        showExitDialog(0);
    }
}
