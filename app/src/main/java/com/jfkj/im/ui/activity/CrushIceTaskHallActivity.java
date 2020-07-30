package com.jfkj.im.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.CrushIceTaskHallBean;
import com.jfkj.im.Bean.IceSubmitBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.ice.GiveThumbsUpBean;
import com.jfkj.im.TIM.redpack.ice.IceUtils;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.UpdateMessageEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.crushicetasl.CrushIceTaskHallPresenter;
import com.jfkj.im.mvp.crushicetasl.CrushIceTaskView;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.DisplayUtil;
import com.jfkj.im.utils.PictureSelectorUtils;
import com.jfkj.im.utils.RomUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videocompression.SiliCompressor;
import com.jfkj.im.view.recyclerview.WrapContentLinearLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.immersive.RomUtils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_NINE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.tencent.imsdk.friendship.TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE_AND_ADD;

public class CrushIceTaskHallActivity extends BaseActivity<CrushIceTaskHallPresenter> implements CrushIceTaskView, SwipeRecyclerView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_recycler)
    SwipeRecyclerView swipe_recycler;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.tv_task_num)
    TextView tv_task_num;

    private CommonRecyclerAdapter<CrushIceTaskHallBean> adapter;
    private CrushIceTaskHallPresenter crushIceTaskHallPresenter;
    private List<CrushIceTaskHallBean> crushIceTaskHallBeanList;
    private String pageNo = "1";
    private String pageSize = "10";
    long num = Long.parseLong(pageNo);
    private String taskId, userId, taskContent, taskType, nickName, money;

    //鐓х墖
    private List<LocalMedia> selectList = new ArrayList<>();

    private TIMConversation conversation;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    @SuppressLint("SetTextI18n")
    private void initview() {
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv_title.setText("破冰任务大厅");
        setStaturBar(constraint_head);
        tv_task_num.setText("今日任务已提交" + UserInfoManger.getTaskCount() + "/10");
        swipe_recycler.useDefaultLoadMore();
        swipe_recycler.loadMoreFinish(false, true);
        swipe_recycler.setLoadMoreListener(this);
//        swipe_recycler.setAutoLoadMore(true);
        crushIceTaskHallPresenter = new CrushIceTaskHallPresenter(this);
        crushIceTaskHallBeanList = new ArrayList<>();
        crushIceTaskHallPresenter.getData(pageNo, pageSize);
        if (swipe_recycler.getItemDecorationCount() == 0) {
            swipe_recycler.addItemDecoration(new SpacesItemDecoration(15));
        }
        swipe_recycler.setLayoutManager(new WrapContentLinearLayoutManager(this));
        adapter = new CommonRecyclerAdapter<CrushIceTaskHallBean>(this, crushIceTaskHallBeanList, R.layout.item_ice_task_hall) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, CrushIceTaskHallBean crushIceTaskHallBean, int position) {
                ImageView cir_head = holder.getView(R.id.cir_head);
                TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_vip = holder.getView(R.id.tv_vip);
                ImageView img_head = holder.getView(R.id.img_head);
                TextView tv_content = holder.getView(R.id.tv_content);
                TextView tv_money = holder.getView(R.id.tv_money);
                TextView tv_state = holder.getView(R.id.tv_state);
                Glide.with(mContext).load(crushIceTaskHallBean.getHead()).into(cir_head);
                tv_name.setText(crushIceTaskHallBean.getNickName());
                tv_content.setText(crushIceTaskHallBean.getCtaskcontent());
                tv_money.setText("x" + crushIceTaskHallBean.getIrewardamount());
                tv_vip.setText("VIP" + crushIceTaskHallBean.getVipLevel());
                if (!TextUtils.isEmpty(crushIceTaskHallBean.getItype())) {
                    if (crushIceTaskHallBean.getItype().equals("1")) {//1鍘诲畬鎴?2宸叉彁浜?3宸插畬鎴?
                        tv_state.setText("去完成");
                        tv_state.setBackground(ContextCompat.getDrawable(mContext, R.drawable.stoke_red_three_bg));
                    } else if (crushIceTaskHallBean.getItype().equals("2")) {
                        tv_state.setText("已提交");
                        tv_state.setBackground(null);
                    } else {
                        tv_state.setText("已完成");
                        tv_state.setBackground(null);
                    }
                }
                if (TextUtils.equals(crushIceTaskHallBean.getItasktype(), "1")) {
                    Glide.with(mContext).load(R.mipmap.ice_pic).override(DisplayUtil.dip2px(mContext, 55), DisplayUtil.dip2px(mContext, 57)).into(img_head);
                } else {
                    Glide.with(mContext).load(R.mipmap.ice_video).override(DisplayUtil.dip2px(mContext, 56), DisplayUtil.dip2px(mContext, 45)).into(img_head);
                }
                tv_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //crushIceTaskHallBean.getUserId()
                        if (UserInfoManger.getTaskCount().equals("10")) {
                            toastShow("今日提交任务次数已达到上限");
                            return;
                        }
                        if (crushIceTaskHallBean.getItype().equals("1")) {
                            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, crushIceTaskHallBean.getUserId());
                            taskId = crushIceTaskHallBean.getItaskid();
                            userId = crushIceTaskHallBean.getUserId();
                            taskContent = crushIceTaskHallBean.getCtaskcontent();
                            taskType = crushIceTaskHallBean.getItasktype();
                            nickName = crushIceTaskHallBean.getNickName();
                            money = crushIceTaskHallBean.getIrewardamount();
                            if (crushIceTaskHallBean.getItasktype().equals("1")) {
                                PictureSelectorUtils.choicePic(CrushIceTaskHallActivity.this, R.style.picture_default_style, 1);
                            } else {
                                PictureSelectorUtils.choiceVideo(CrushIceTaskHallActivity.this, R.style.picture_default_style, 1);
                            }
                        }
                    }
                });
                cir_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userId", crushIceTaskHallBean.getUserId());
                        startActivity(intent);
                    }
                });
            }
        };
        swipe_recycler.setAdapter(adapter);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_recycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isRefresh = true;
                        num = 1;
                        crushIceTaskHallPresenter.getData(pageNo, pageSize);
                        //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                        swipe_layout.setRefreshing(false);
                        swipe_recycler.loadMoreFinish(false, true);
                    }
                }, 1000);

            }
        });
    }


    /**
     * 视频压缩
     */
    private void compressVideo(final String srcPath, final String destDirPath) {
        // final String destDirPath =  getTrimmedVideoDir(this, "small_video");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    int outWidth = 0;
                    int outHeight = 0;

                    //竖屏
                    outWidth = 720;
                    outHeight = 480;

                    String compressedFilePath = SiliCompressor.with(App.getAppContext())
                            .compressVideo(srcPath, destDirPath, outWidth, outHeight, 7200000);
                    emitter.onNext(compressedFilePath);
                } catch (Exception e) {
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        subscribe(d);
                    }

                    @Override
                    public void onNext(String outputPath) {

                        //   hideLoading();
//                        //文件上传
//                        mvpPresenter.upLoadFile(imageBean);


                        crushIceTaskHallPresenter.uploadFiles("4", "1", new File(outputPath), SPUtils.getInstance(getApplicationContext()).getString(Utils.USERID) + System.currentTimeMillis());


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("TAG", "compressVideo---onError:" + e.toString());
                        toastShow("上传视频失败");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 文件类型（1图片 2gif 3音频 4视频 5文件）
     * 上传地址类型（1 个人用户信息 2 动态圈 3聊天文件 4 系统图标 5用户验证文件）
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i("reportActivity", "压缩::" + media.getCompressPath());
                        Log.i("reportActivity", "原图::" + media.getPath());
                        Log.i("reportActivity", "裁剪::" + media.getCutPath());
                        Log.i("reportActivity", "是否开启原图::" + media.isOriginal());
                        Log.i("reportActivity", "原图路径::" + media.getOriginalPath());
                        Log.i("reportActivity", "Android Q 特有Path::" + media.getAndroidQToPath());
                        if (media.getAndroidQToPath() != null) {
                            if (media.getAndroidQToPath().trim().length() > 0) {
                                media.setPath(media.getAndroidQToPath().trim());
                            }
                        }
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        selectList.get(0).setPath(selectList.get(0).getAndroidQToPath());
                    } else {

                    }


                    String path = selectList.get(0).getPath();


                    showLoading();
                    compressVideo(path, this.getCacheDir().getAbsolutePath());


                    break;
                case PictureConfig.REQUEST_CAMERA://鍥剧墖
                    selectList.clear();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() == 0) {
                        toastShow("上传图片失败");
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
                        toastShow("上传图片失败");
                        return;
                    }
                    crushIceTaskHallPresenter.uploadFiles("1", "1", new File(path_pic), SPUtils.getInstance(this).getString(Utils.USERID) + System.currentTimeMillis());
                    break;
            }
        }
    }

    @OnClick({R.id.iv_title_back})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crush_ice_task_hall;
    }

    @Override
    public CrushIceTaskHallPresenter createPresenter() {
        return crushIceTaskHallPresenter;
    }

    @Override
    public void getCrushIce(BaseBeans<CrushIceTaskHallBean> crushIceTaskHallBean) {
        if (crushIceTaskHallBean.getCode().equals("200")) {
            if (crushIceTaskHallBean.getData() != null && crushIceTaskHallBean.getData().size() > 0) {
                if (crushIceTaskHallBean.getData() == null) {
                    swipe_recycler.loadMoreFinish(false, true);
                    return;

                }

                if (crushIceTaskHallBean.getData().size() == 0) {
                    swipe_recycler.loadMoreFinish(false, true);
                    return;
                }
                if (isRefresh) {
                    isRefresh = false;
                    crushIceTaskHallBeanList.clear();
                    crushIceTaskHallBeanList.addAll(crushIceTaskHallBean.getData());
                    adapter.notifyDataSetChanged();
                    swipe_recycler.loadMoreFinish(false, true);
                } else {
                    if (crushIceTaskHallBean.getData().size() > 0) {
                        swipe_recycler.loadMoreFinish(false, true);
                        crushIceTaskHallBeanList.addAll(crushIceTaskHallBean.getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        swipe_recycler.loadMoreFinish(false, false);
                    }
                }

                if (crushIceTaskHallBean.getData().size() < 10) {
                    swipe_recycler.loadMoreFinish(false, false);
                }
            }
        }
    }

    @Override
    public void getCrushFails(String error) {
        toastShow("视频太大");
    }

    @Override
    public void upLoadPic(String s) {
        IceUtils.submitTask(s, taskId, TIMManager.getInstance().getLoginUser(), new TIMValueCallBack<IceSubmitBean>() {
            @Override
            public void onError(int i, String s) {
                toastShow(s);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(IceSubmitBean iceSubmitBean) {

                if (iceSubmitBean == null) return;
                tv_task_num.setText("今日任务已提交：" + iceSubmitBean.getTaskCount() + "/10");
                UserInfoManger.saveTaskCount(iceSubmitBean.getTaskCount());
                isRefresh = true;
                num = 1;
                crushIceTaskHallPresenter.getData(pageNo, pageSize);

//                ChatInfo chatInfo = new ChatInfo();
//                chatInfo.setType(TIMConversationType.C2C);
//                chatInfo.setId(userId);
//                chatInfo.setChatName(nickName);
//
//                ConversationMessage conversationMessage = new ConversationMessage();
//                conversationMessage.setConversationId(userId);
//                conversationMessage.setGroup(false);
//                conversationMessage.setId(userId);
//                conversationMessage.setTitle(nickName);
//
//                Intent intent = new Intent(CrushIceTaskHallActivity.this, ChatActivity.class);
//                intent.putExtra(Constants.CHAT_INFO, chatInfo);
//                intent.putExtra(Utils.CONVERSATIONMESSAGE, conversationMessage);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("sendType",READ_PACKAGE_MSG_TYPE_ONE);
//                    jsonObject.put("taskId",taskId);
//                    jsonObject.put("receiverId",userId);
//                    jsonObject.put("sendId",TIMManager.getInstance().getLoginUser());
//                    jsonObject.put("taskContent",taskContent);
//                    jsonObject.put("taskURL", s);
//                    jsonObject.put("taskType",taskType);
//                    jsonObject.put("type",READ_PACKAGE_CUS_TYPE_NINE);
//                    jsonObject.put("money",money);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                MessageInfo messageInfo = MessageInfoUtil.buildTaskCustomMessage(jsonObject.toString());
//                conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
//                    @Override
//                    public void onError(int i, String s) {
//                        toastShow(s);
//                        Log.e("msg","onError" + s + "code " + i);
//                    }
//
//                    @Override
//                    public void onSuccess(TIMMessage timMessage) {
//                        Log.e("msg","success");
//                        FriendsUtils.checkFriend(userId, new TIMValueCallBack<Boolean>() {
//                            @Override
//                            public void onError(int i, String s) {
//                                toastShow(s);
//                            }
//
//                            @Override
//                            public void onSuccess(Boolean aBoolean) {
//                                if (aBoolean){
//                                    addFriend(userId);
//                                }
//                            }
//                        });
//                        EventBus.getDefault().post(new UpdateMessageEvent(true));
//                    }
//                });
            }
        });
    }

    @Override
    public void addFriend() {

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
    public void showLoading() {
        if(progressDialog!=null){
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialog.isShowing() && progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onLoadMore() {
        if (swipe_layout.isRefreshing()){
            swipe_layout.setRefreshing(false);
            swipe_recycler.loadMoreFinish(false, false);
        } else {
            swipe_recycler.loadMoreFinish(false, true);
        }
        if (!isRefresh){
            swipe_recycler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    num++;
                    swipe_recycler.loadMoreFinish(false, true);
                    crushIceTaskHallPresenter.getData(String.valueOf(num), pageSize);
                    Log.e("msg", "|///////////onLoadMore////////////" + num);
                }
            }, 1000);
        }
    }

    @Override
    public void onRefresh() {
        Log.e("msg", "|///////////onRefresh////////////");
    }
}
