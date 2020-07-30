package com.jfkj.im.adapter.dynamic;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.R;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.listener.ItemClickListener;
import com.jfkj.im.listener.onItemClickListener;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.ui.fragment.UserDetailDynamicFragment;
import com.jfkj.im.ui.home.DynamicDetailActivity;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.ui.mine.PlayVideoActivity;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.videoPlay.PrepareView;
import com.jfkj.im.view.FriendCircleView;
import com.jfkj.im.view.MyScaleImageView;
import com.jfkj.im.widget.GridItemDecoration;

import com.luck.picture.lib.entity.LocalMedia;

import com.lzy.okgo.utils.OkLogger;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;


/**
 * Description: 圈子adapter
 *
 * @author :   ys
 * @date :         2019/11/19
 * </pre>
 */
public class UserDetailDynamicAdapter extends RecyclerView.Adapter<UserDetailDynamicAdapter.ViewHolder> implements CommentAdapter.onCommentClick {

    private List<CircleBean.DataBean> mArticleBeanList = new ArrayList<>();
    private Context mContext;
    private AppCompatActivity activity;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ImageAdapter imageAdapter;

    private boolean isUserDetail = true;//是不是个人中心标志
    private int num;

    private CircleDynamicAdapter.OnClickOutListener mOutListener;





    public UserDetailDynamicAdapter(Context context, List<CircleBean.DataBean> list, ItemClickListener listener, boolean isDetail) {
        this.mContext = context;
        this.mArticleBeanList = list;
        this.mClickListener = listener;
        this.isUserDetail = isDetail;
        mInflater = LayoutInflater.from(mContext);
        activity = (AppCompatActivity) context;
    }

    public void setOutClickListener(CircleDynamicAdapter.OnClickOutListener listener) {
        this.mOutListener = listener;
    }

    public void loadMoreData(List<CircleBean.DataBean> list) {
        mArticleBeanList.addAll(list);
        notifyItemRangeChanged(mArticleBeanList.size() - list.size(), mArticleBeanList.size());
    }

    public void setmArticleBeanList(List<CircleBean.DataBean> mArticleBeanList) {
        this.mArticleBeanList = mArticleBeanList;
        OkLogger.e(mArticleBeanList + " ");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_dynamic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, List obj) {
        CircleBean.DataBean bean = mArticleBeanList.get(position);


        if (obj.isEmpty()) {
            holder.mPosition = position;
            holder.mTvContent.setText(bean.getContent());
            Glide.with(mContext).load(bean.getHead()).transform(new CircleCrop()).into(holder.mIvUserHead);
            holder.mTvUserName.setText(bean.getNickName());
            holder.mTvTime.setText( bean.getAdddate());


            if(!bean.getPraisecount().equals("0")){
                holder.mTvLike.setText(bean.getPraisecount());
            }

            if(bean.getCommenCount() != 0){
             holder.mTvDiscuss.setText(bean.getCommenCount() + "");
            }

            if (bean.getLocation() != null && bean.getLocation().length() > 0) {
                holder.mTvAddress.setText(bean.getLocation() + "·");
//                holder.mTvAddress.setVisibility(View.VISIBLE);
            } else {
//                holder.mTvAddress.setVisibility(View.GONE);
                holder.mTvAddress.setText("");
            }

            holder.containerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlayVideoActivity.class);
                    intent.putExtra("video_url", bean.getVideoPath());
                    intent.putExtra("type", "1");
                    mContext.startActivity(intent);
                }
            });


            if (bean.getIsPraise() == 1) {
//                holder.mIvLike.setBackgroundResource(R.mipmap.paise_red);
                holder.lottie_animation_like.setProgress(1);
            } else {
//                holder.mIvLike.setBackgroundResource(R.mipmap.paise);
                holder.lottie_animation_like.setProgress(0);
            }
            if (!"".equals(bean.getVideoPath()) && bean.getVideoPath() != null) {
                //显示视频
                holder.layoutImage.setVisibility(View.VISIBLE);
                holder.containerPlay.setVisibility(View.VISIBLE);

                holder.videoImage.test(bean.getVideoImages().getWidth(),bean.getVideoImages().getHeight(),mContext);
                Glide.with(mContext).load(bean.getVideoImages().getUrl()).into( holder.videoImage);

            } else if (!"".equals(bean.getImages()) && bean.getImages() != null) {
                //显示图片
                holder.layoutImage.setVisibility(View.VISIBLE);
                holder.containerPlay.setVisibility(View.GONE);

                setRecyclerImage(holder, position, bean);
            } else {
                holder.layoutImage.setVisibility(View.GONE);
                holder.containerPlay.setVisibility(View.GONE);
            }

            holder.discuss_num.setText(bean.getPraisecount() + "次赞、共" + bean.getCommenCount() + "条评论");

            long diamonds = Long.parseLong(bean.getCommenDiamonds());
            if (diamonds > 0) {
                String htmls ="已获得"+ "<font> "+ bean.getCommenCount()+"</font>"+"条评论，收到"+ "<font color = '#FF2B66'> "+ bean.getCommenDiamonds()+"</font>";
                holder.tv_diamonds.setText(Html.fromHtml(htmls));
                holder.fl_comment.setVisibility(View.VISIBLE);
            } else {
                holder.fl_comment.setVisibility(View.GONE);
            }



            holder.mLayoutLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onPositionClick(position, v, mArticleBeanList.get(position).getIsPraise() + "");
                }
            });



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUserDetail) {
                        Bundle bundle = new Bundle();
                        bundle.putString("cuserId", bean.getCuserid());
                        bundle.putString("articleId", bean.getArticleId());
                        bundle.putInt("position",position);
                        JumpUtil.overlay(mContext, DynamicDetailActivity.class, bundle);
                    }
                }
            });

            holder.mIvUserHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isUserDetail) {
                        //用户详情就不要跳转了 避免套娃
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", bean.getCuserid());
                        JumpUtil.overlay(mContext, UserDetailActivity.class, bundle);
                    }
                }
            });

            holder.mTvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isUserDetail) {
                        //用户详情就不要跳转了 避免套娃
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", bean.getCuserid());
                        JumpUtil.overlay(mContext, UserDetailActivity.class, bundle);
                    }

                }
            });
        } else {

//            holder.discuss_num.setText(bean.getPraisecount() + "次赞、共" + bean.getCommenCount() + "条评论");

            int num = 0;
            if("".equals(holder.mTvLike.getText().toString().trim())){
                num = 0;
            }else{

              num = Integer.parseInt(holder.mTvLike.getText().toString().trim());
            }

            if ("2222".equals(obj.get(0))) {

//                holder.mIvLike.setBackgroundResource(R.mipmap.paise_red);
                holder.lottie_animation_like.setProgress(1);
                holder.lottie_animation_like.playAnimation();

                holder.mTvLike.setText(num+1 +"");
            } else {

                if(num == 1 ){
                    holder.mTvLike.setText("");
                }else{
                    holder.mTvLike.setText(num-1+"");
                }

//                holder.mIvLike.setBackgroundResource(R.mipmap.paise);
                holder.lottie_animation_like.setProgress(0);
                holder.lottie_animation_like.pauseAnimation();

            }

        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }




    private void setRecyclerImage( ViewHolder holder, int parentPosition, CircleBean.DataBean bean) {

        list = new ArrayList<>();

        imageUrlList = new ArrayList<>();
        ArrayList<String> lists = new ArrayList<>();
        friendCircleView = new FriendCircleView();


        for (int i = 0 ; i <bean.getPicImages().size() ; i ++){
            LocalMedia imageBean = new LocalMedia();

            imageBean.setPath(bean.getPicImages().get(i).getUrl());
            imageBean.setWidth(bean.getPicImages().get(i).getWidth());
            imageBean.setHeight(bean.getPicImages().get(i).getHeight());

            imageUrlList.add(imageBean);
            lists.add(bean.getPicImages().get(i).getUrl());
        }

        RecyclerView imageRecycler = holder.recycler_list;
        if (imageUrlList.size() >= 3) {
            if (imageUrlList.size() == 4) {
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
                imageRecycler.setLayoutManager(layoutManager);
                GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 2);
                if (imageRecycler.getItemDecorationCount() == 0) {
                    imageRecycler.addItemDecoration(itemDecoration);
                }
                num = 4;
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                imageRecycler.setLayoutManager(layoutManager);
                GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 3);
                if (imageRecycler.getItemDecorationCount() == 0) {
                    imageRecycler.addItemDecoration(itemDecoration);
                }
                num = 3;
            }
        } else if (imageUrlList.size() == 2) {
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            imageRecycler.setLayoutManager(layoutManager);
            GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 2);
            if (imageRecycler.getItemDecorationCount() == 0) {
                imageRecycler.addItemDecoration(itemDecoration);
            }
            num = 2;
        } else if (imageUrlList.size() == 1) {
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
            imageRecycler.setLayoutManager(layoutManager);
            GridItemDecoration itemDecoration = new GridItemDecoration(ScreenUtil.pxToDp(40), 1);
            if (imageRecycler.getItemDecorationCount() == 0) {
                imageRecycler.addItemDecoration(itemDecoration);
            }
            num = 1;
        }


        imageAdapter = new ImageAdapter(mContext, imageUrlList, num);
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

                PhotoViewer.INSTANCE.setData(lists).setImgContainer(holder.recycler_list).setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public void onLongClick(@NotNull View view) {

                    }
                })
                        .showView(true)
                        .setData(bean.getAdddate(), bean.getContent(), bean.getPraisecount(), bean.getCommenCount() + "", finalIsShow, 0)
                        .setCurrentPage(position)
                        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(@NotNull ImageView iv, @NotNull String url) {
                                Glide.with(mContext).load(url).into(iv);
                            }
                        }).start(activity);
            }
        });
    }

    /**
     * 设置图片
     *
     * @param holder
     * @param parentPosition
     * @param bean
     */
    private List<String> list;
    private List<LocalMedia> imageUrlList;
    private FriendCircleView friendCircleView;

    @Override
    public int getItemCount() {
        return mArticleBeanList.size();
    }

    @Override
    public void getReplayName(String name, int outPosition, int position, String contentId, String userId) {

        mOutListener.getClickName(name, outPosition, position, contentId, userId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView mIvUserHead;
        AppCompatTextView mTvUserName;
        AppCompatTextView mTvTime;
        AppCompatTextView mTvDelete;
        TextView mTvContent;
        AppCompatTextView mTvAddress;
        AppCompatTextView mTvDiscuss;
        AppCompatTextView mTvLike;


//        ImageView mIvLike;
        LinearLayout mLayoutLike;
        RelativeLayout layoutImage;



        ConstraintLayout constraintlayout;
        public int mPosition;
        private AppCompatTextView discuss_num;


          RecyclerView recycler_list;
        private final FrameLayout containerPlay;
        private final MyScaleImageView videoImage;

        private LottieAnimationView lottie_animation_like;
        private FrameLayout fl_comment;
        private AppCompatTextView tv_diamonds;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintlayout = itemView.findViewById(R.id.constraintlayout);
            mIvUserHead = itemView.findViewById(R.id.iv_user_head);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvDelete = itemView.findViewById(R.id.tv_delete);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvDiscuss = itemView.findViewById(R.id.tv_discuss);
            mTvLike = itemView.findViewById(R.id.tv_like);
//            mIvLike = itemView.findViewById(R.id.iv_like);
            mLayoutLike = itemView.findViewById(R.id.layout_like);
            layoutImage = itemView.findViewById(R.id.layout_image_video);
            discuss_num = itemView.findViewById(R.id.discuss_num);
            recycler_list = itemView.findViewById(R.id.recycler_list);

            containerPlay = itemView.findViewById(R.id.f_video_layout);
            videoImage = itemView.findViewById(R.id.iv_video_brg);
            lottie_animation_like = itemView.findViewById(R.id.lottie_animation_like);

            fl_comment = itemView.findViewById(R.id.fl_comment);
            tv_diamonds = itemView.findViewById(R.id.tv_diamonds);

            itemView.setTag(this);


        }
    }
}