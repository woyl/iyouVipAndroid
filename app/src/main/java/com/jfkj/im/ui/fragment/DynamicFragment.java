package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jfkj.im.Bean.UserDetailBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.dynamic.MineDynamicAdapter;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.entity.UserGroupTaskBean;
import com.jfkj.im.event.CommentEvent;
import com.jfkj.im.event.MineCircleEvent;
import com.jfkj.im.event.PraiseEvent;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.mine.MinePresenter;
import com.jfkj.im.mvp.mine.MineView;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.mine.PlayVideoActivity;
import com.jfkj.im.utils.ScrollLinearLayoutManager;
import com.jfkj.im.utils.SoftKeyBoardListener;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jfkj.im.adapter.dynamic.MineDynamicAdapter.REQUST;
import static com.jfkj.im.ui.home.DynamicDetailActivity.RESULT;

public class DynamicFragment extends BaseFragment<MinePresenter> implements MineView, SwipeRecyclerView.LoadMoreListener, MineDynamicAdapter.OnClickOutListener, MineDynamicAdapter.onDynamicClickListener {

    @BindView(R.id.recyclerview)
    SwipeRecyclerView mRecyclerview;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    private LinearLayoutManager linearLayoutManager;
    private MineDynamicAdapter mAdapter;

    /**
     * 评论
     */
    private List<MineInfo.DataBean.CircleArticleBean> mArticleBeanList = new ArrayList<>();
    private int commentPosition;

    /**
     * 点赞
     */
    private boolean isLike;
    private String praiseId;

    /**
     * 发送
     */
    @BindView(R.id.edit_content)
    EditText edit_content;
    @BindView(R.id.tv_replay)
    TextView tv_replay;
    @BindView(R.id.layout_comment)
    ConstraintLayout layout_comment;
    private int listPosition;
    private boolean isComment = false;

    //加载更多
    private int pageSize = 10;
    private int pageNo = 1;

    private CommonDialog exitDialog;
    private boolean isRefreshData;



    public static DynamicFragment getInstance() {
        DynamicFragment informationFragment = new DynamicFragment();
        return informationFragment;
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        mvpPresenter.getMineInfo("10", "1");
    }

    private void initView() {
        linearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mArticleBeanList.clear();

        mRecyclerview.useDefaultLoadMore();
        mRecyclerview.loadMoreFinish(false, true);
        mRecyclerview.setLoadMoreListener(this);
//        mRecyclerview.setAutoLoadMore(false);

        mAdapter = new MineDynamicAdapter(getActivity(),this, mArticleBeanList, this);
        mAdapter.setRsp(new ResponListeners<String, Integer>() {
            @Override
            public void Rsps(String data, Integer view) {
                showExitDialog(data,view);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOutClickListener(this);

        //发送
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                layout_comment.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide() {
                layout_comment.setVisibility(View.GONE);
            }
        });
    }


    private void showExitDialog(String articleId,int position) {
        exitDialog = new CommonDialog(getActivity(), R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdate(MineCircleEvent event){
        if (event.isUpdate().size() > 0){
            this.mArticleBeanList.clear();
            this.mArticleBeanList.addAll(event.isUpdate());
            if (mAdapter == null) return;
            tv_no_data.setVisibility(View.GONE);
            mRecyclerview.setVisibility(View.VISIBLE);
            mAdapter.setData(event.isUpdate());
            mAdapter.notifyDataSetChanged();
        } else {
            tv_no_data.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.GONE);
        }
    }

    public void setFriendCircleData(List<MineInfo.DataBean.CircleArticleBean> mArticleBeanList) {

    }

    @Override
    public void showMineInfo(MineInfo info) {
        if (isRefreshData){
            isRefreshData = false;
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
    public void showDelete(int position) {
        mAdapter.refreshDeleteData(position);
    }

    /**
     * 点赞成功或者取消点赞 过来更新ui
     *
     * @param id
     * @param position
     */
    @Override
    public void showPraise(String id, int position) {
        if (isLike) {
            //取消点赞成功
            mArticleBeanList.get(position).setIsPraise("0");

            String praisecount = mArticleBeanList.get(position).getPraisecount();
            int i = Integer.parseInt(praisecount);
            i -= 1;
            OkLogger.e(i + "");
            mArticleBeanList.get(position).setPraisecount(String.valueOf(i));
        } else {
            mArticleBeanList.get(position).setIsPraise("1");
            praiseId = id;

            String praisecount = mArticleBeanList.get(position).getPraisecount();
            int i = Integer.parseInt(praisecount);
            i += 1;
            OkLogger.e(i + "");

            mArticleBeanList.get(position).setPraisecount(String.valueOf(i));
        }

        mAdapter.notifyItemChanged(position);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSuccess() {
    }

    @Override
    public void showUserGroupTask(UserGroupTaskBean userGroupTaskBean) {

    }

    @Override
    public void onSuccessBalance() {

    }

    @Override
    public void onFindOnNew(List<CommentBean> commentBean) {
        mAdapter.setData(mArticleBeanList);
        mAdapter.notifyItemChanged(commentPosition, "3");
    }

    @Override
    public void getUserInfo(UserDetailBean userDetailBean) {

    }

    @Override
    public void showLoading() {
//        progressDialog.show();
    }

    @Override
    public void hideLoading() {
//        if (progressDialog.isShowing() && progressDialog != null)
//            progressDialog.dismiss();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        isRefreshData = true;
        mvpPresenter.getMineInfo(pageSize + "", pageNo + "");
    }

    @Override
    public void getClickName(String name, int position, int outPosition) {
//        try {
//            layout_comment.setVisibility(View.VISIBLE);
//            KeyBoardUtils.requestShowKeyBord(edit_content);
//            edit_content.setHint("回复：" + name);
//            commentPosition = position;
//            listPosition = outPosition;
//            isComment = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPositionClick(int position, View v) {
        switch (v.getId()) {
            case R.id.player_container:
                Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                intent.putExtra("video_url", mArticleBeanList.get(position).getVideoPath());
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.comment_recycler:
                break;
            case R.id.tv_like:
            case R.id.lottie_animation_like:
                showLoading();
                if (mArticleBeanList.size() == 0) return;
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
//            case R.id.tv_discuss:
//                layout_comment.setVisibility(View.VISIBLE);
//                MineInfo.DataBean.CircleArticleBean articleBean = mArticleBeanList.get(position);
//                listPosition = position;
//                isComment = false;
//                KeyBoardUtils.requestShowKeyBord(edit_content);
//                tv_replay.setText("回复：" + articleBean.getNickName());
//                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upPraise(PraiseEvent event){
        if (mArticleBeanList.size() == 0) return;
        int position = event.getPosition();
        if ("1".equals(mArticleBeanList.get(position).getIsPraise())) {
            //点赞过的  点击要取消点赞
            isLike = true;
            mvpPresenter.getUserPraise(mArticleBeanList.get(position).getArticleId(), Utils.PRAISE_CANCEL, "".equals(praiseId) ? mArticleBeanList.get(position).getPraiseId() : praiseId, position,event.getView());
        } else {
            //点赞
            isLike = false;
            mvpPresenter.getUserPraise(mArticleBeanList.get(position).getArticleId(), Utils.PRAISE_TRUE, null, position,event.getView());
        }
    }


    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void upComment(CommentEvent event){
        mArticleBeanList.get(event.getPosition()).setCommenCount(event.getCount());
        mAdapter.notifyItemChanged(event.getPosition());
    }

    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
