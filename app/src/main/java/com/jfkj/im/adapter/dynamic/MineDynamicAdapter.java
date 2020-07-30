package com.jfkj.im.adapter.dynamic;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.bumptech.glide.load.DecodeFormat;
import com.jfkj.im.R;
import com.jfkj.im.entity.MineInfo;
import com.jfkj.im.event.MineCircleEvent;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.ui.home.DynamicDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.view.MyScaleImageView;
import com.jfkj.im.widget.CircleImageView;
import com.jfkj.im.widget.GridItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;
import com.wanglu.photoviewerlibrary.PhotoViewerFragment;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 * Description: 动态adapter
 * @author :   ys
 * @date :         2019/11/19
 * </pre>
 */
public class MineDynamicAdapter extends RecyclerView.Adapter<MineDynamicAdapter.ViewHolder> implements MineCommentAdapter.onCommentClick {

    private List<MineInfo.DataBean.CircleArticleBean> mDataBeans = new ArrayList<>();
    private Activity mContext;
    private Fragment mFragment;
    private LayoutInflater mInflater;
    private onDynamicClickListener mClickListener;
    private List<LocalMedia> imageUrlList = new ArrayList<>();
    private List<LocalMedia> urlList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private int num;
    private getReplayName mReplayName;
    private OnClickOutListener mOutListener;

    private ResponListeners<String, Integer> responListeners;

    public final static int REQUST = 101;

    public void setRsp(ResponListeners<String, Integer> responListeners) {
        this.responListeners = responListeners;
    }


    public void setData(List<MineInfo.DataBean.CircleArticleBean> mArticleBeanList) {
        this.mDataBeans = mArticleBeanList;

    }

    public MineDynamicAdapter(Activity context, Fragment fragment,List<MineInfo.DataBean.CircleArticleBean> list, onDynamicClickListener listener) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mDataBeans = list;
        this.mClickListener = listener;
        mInflater = LayoutInflater.from(mContext);
    }

    public void SetGetReplayNameListener(getReplayName listener) {
        this.mReplayName = listener;
    }

    public void setOutClickListener(OnClickOutListener listener) {
        this.mOutListener = listener;
    }

    @NonNull
    @Override
    public MineDynamicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_user_dynamic, parent, false);
        return new MineDynamicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    public void refreshDeleteData(int position) {
        mDataBeans.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataBeans.size() - position);
        if (mDataBeans.size() == 0) {
            EventBus.getDefault().post(new MineCircleEvent(mDataBeans));
        }
    }

    public void addData(List<MineInfo.DataBean.CircleArticleBean> list) {
        mDataBeans.addAll(list);
        notifyItemRangeChanged(mDataBeans.size() - list.size(), mDataBeans.size());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MineDynamicAdapter.ViewHolder holder, int position, List playloads) {

        MineInfo.DataBean.CircleArticleBean bean = mDataBeans.get(position);

        if (playloads.isEmpty()) {
            holder.mPosition = position;
            holder.mTvContent.setText(bean.getContent());

            if (!bean.getPraisecount().equals("0")) {
                holder.mTvLike.setText(bean.getPraisecount());
            } else {
                holder.mTvLike.setText("");
            }

            if (bean.getCommenCount() != 0) {
                holder.mTvDiscuss.setText(bean.getCommenCount() + "");
            } else {
                holder.mTvDiscuss.setText("");
            }


            holder.tvName.setText(bean.getNickName());
            // holder.tvAddressTime.setText(bean.getLocation() + " · " + bean.getAdddate());

            Glide.with(mContext).load(bean.getHead()).into(holder.iv_user_head);
            holder.tv_like_comment.setText(bean.getLocation() + " · " + bean.getAdddate());

//            if ("".equals(bean.getLocation())) {
//                holder.mTvAddress.setVisibility(View.GONE);
//            } else {
//                holder.mTvAddress.setVisibility(View.VISIBLE);
//                holder.mTvAddress.setText(bean.getLocation());
//            }

//        holder.mTvTime.setText(bean.getAdddate());

            if ("1".equals(bean.getIsPraise())) {
//                holder.mIvLikeIcon.setBackgroundResource(R.mipmap.paise_red);
                holder.lottie_animation_like.setProgress(1);
                holder.lottie_animation_like.playAnimation();
            } else {
//                holder.mIvLikeIcon.setBackgroundResource(R.mipmap.paise);
                holder.lottie_animation_like.setProgress(0);
                holder.lottie_animation_like.pauseAnimation();
            }

            //图片部分
            if (!"".equals(bean.getVideoPath()) && bean.getVideoPath() != null) {
                //显示视频
                holder.layoutImage.setVisibility(View.VISIBLE);
                holder.containerPlay.setVisibility(View.VISIBLE);

//                holder.ll_view.setVisibility(View.GONE);
                holder.imageRecycler.setVisibility(View.GONE);
                holder.iv_video_brg.test(bean.getVideoImages().getWidth(), bean.getVideoImages().getHeight(), mContext);
                Glide.with(mContext).load(bean.getVideoImage()).into(holder.iv_video_brg);

            } else if (!"".equals(bean.getImages()) && bean.getImages() != null) {
                //显示图片
                holder.layoutImage.setVisibility(View.VISIBLE);
                holder.containerPlay.setVisibility(View.GONE);
//                holder.ll_view.setVisibility(View.VISIBLE);
                holder.imageRecycler.setVisibility(View.VISIBLE);
                setRecyclerImage(holder, holder.getAdapterPosition(), bean);
            } else {
                holder.layoutImage.setVisibility(View.GONE);
                holder.containerPlay.setVisibility(View.GONE);
            }

            // 评论部分
//            RecyclerView commentRecycler = holder.commentRecycler;

//            if (bean.getCommenlist().size() > 0) {
//                holder.img_sanjiao.setVisibility(View.VISIBLE);
//            } else {
//                holder.img_sanjiao.setVisibility(View.GONE);
//            }

//            holder.commentAdapter = new MineCommentAdapter(mContext, position, bean.getCommenlist(), this);
//            commentRecycler.setNestedScrollingEnabled(false);
//            commentRecycler.setLayoutManager(new LinearLayoutManager(mContext));
//            commentRecycler.setAdapter(holder.commentAdapter);
//            commentRecycler.setFocusableInTouchMode(false);
//            commentRecycler.requestFocus();
//            ((DefaultItemAnimator) commentRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
//            holder.commentAdapter.notifyDataSetChanged();
            Log.d("@@@", "要走这儿来，这个是评论部分刷新" + bean.getCommenlist().size());


//            if (bean.getCommenlist() != null && bean.getCommenlist().size() > 0) {
//                commentRecycler.setVisibility(View.GONE);
//            } else {
//                commentRecycler.setVisibility(View.GONE);
//            }

            long diamonds = Long.parseLong(bean.getCommenDiamonds());
            if (diamonds > 0) {
                String htmls ="已获得"+ "<font> "+ bean.getCommenCount()+"</font>"+"条评论，收到"+ "<font color = '#FF2B66'> "+ bean.getCommenDiamonds()+"</font>";
                holder.tv_diamonds.setText(Html.fromHtml(htmls));
                holder.fl_comment.setVisibility(View.VISIBLE);
            } else {
                holder.fl_comment.setVisibility(View.GONE);
            }

            holder.lottie_animation_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    mClickListener.onPositionClick(position, v);
                }
            });

            holder.mTvDiscuss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onPositionClick(position, v);
                }
            });

            holder.mTvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onPositionClick(position, v);
                }
            });

//        holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("@@@", "点击删除");
//                mClickListener.onPositionClick(position, v);
//            }
//        });


            holder.containerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("@@@", "点击播放");
                    mClickListener.onPositionClick(position, v);
                }
            });

//            holder.commentRecycler.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mClickListener.onPositionClick(position, v);
//                }
//            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("cuserId", bean.getCuserid());
                    bundle.putString("articleId", bean.getArticleId());
                    bundle.putString("startType", "1");
                    bundle.putInt("position", position);
                    JumpUtil.overlay(mContext, DynamicDetailActivity.class, bundle);
                }
            });

        } else {


            if ("1".equals(playloads.get(0))) {
//                holder.mIvLikeIcon.setBackgroundResource(R.mipmap.paise_red);
                holder.lottie_animation_like.setProgress(1);
            } else if ("0".equals(playloads.get(0))) {
//                holder.mIvLikeIcon.setBackgroundResource(R.mipmap.paise);
                holder.lottie_animation_like.setProgress(0);
            } else {
//                holder.commentAdapter.setdata(bean.getCommenlist());
//                holder.commentAdapter.notifyDataSetChanged();
            }
            holder.tv_like_comment.setText(bean.getLocation() + " · " + bean.getAdddate());

            //  holder.mTvLike.setText(bean.getPraisecount() + "");
        }

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responListeners.Rsps(bean.getArticleId(), position);
            }
        });
    }

    /**
     * recyclerview
     */
    private void setRecyclerImage(MineDynamicAdapter.ViewHolder holder, int parentPosition, MineInfo.DataBean.CircleArticleBean bean) {

        String imagesUrl = bean.getImages();

        imageUrlList = new ArrayList<>();
        ArrayList<String> lists = new ArrayList<>();


//        String[] strings = imagesUrl.split(",");
//        list = Arrays.asList(strings);
//        for (String string : strings) {
//            LocalMedia imageBean = new LocalMedia();
//            imageBean.setPath(string);
//            imageUrlList.add(imageBean);
//            lists.add(string);
//        }


        for (int i = 0; i < bean.getPicImages().size(); i++) {
            LocalMedia imageBean = new LocalMedia();

            imageBean.setPath(bean.getPicImages().get(i).getUrl());
            imageBean.setWidth(bean.getPicImages().get(i).getWidth());
            imageBean.setHeight(bean.getPicImages().get(i).getHeight());

            imageUrlList.add(imageBean);
            lists.add(bean.getPicImages().get(i).getUrl());
        }
        RecyclerView imageRecycler = holder.imageRecycler;
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
        if (bean.getIsPraise().equals("1")) {
            isShow = true;
        }

        boolean finalIsShow = isShow;
        imageAdapter.setOnListener(new onItemPositionClickListener() {
            @Override
            public void onPositionClick(int position) {
                PhotoViewer.INSTANCE.setData(lists).setImgContainer(imageRecycler)
                        .setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public void onLongClick(@NotNull View view) {

                            }
                        }).onClick(new PhotoViewerFragment.OnClick() {
                    @Override
                    public void onclick() {
                        Bundle bundle = new Bundle();
                        bundle.putString("cuserId", bean.getCuserid());
                        bundle.putString("articleId", bean.getArticleId());
                        bundle.putString("startType", "1");
                        bundle.putInt("position", position);
                        JumpUtil.overlay(mContext, DynamicDetailActivity.class, bundle);
                    }
                })
                        .showView(true)
                        .setData(bean.getAdddate(), bean.getContent(), bean.getPraisecount(), bean.getCommenCount() + "", finalIsShow, 0)
                        .setCurrentPage(position)
                        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(@NotNull ImageView iv, @NotNull String url) {
                                Glide.with(mFragment).load(url)
//                                        .placeholder(R.drawable.nim_default_img_failed) //预加载图片
//                                        .error(R.drawable.nim_default_img_failed) //加载失败图片
                                        .into(iv);
                            }
                        }).start(mFragment);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeans == null ? 0 : mDataBeans.size();
    }

    @Override
    public void getReplayName(String name, int outPosition, int position) {
        mOutListener.getClickName(name, position, outPosition);
    }

    @Override
    public void clickMore(int outPosition) {
        Bundle bundle = new Bundle();
        bundle.putString("cuserId", mDataBeans.get(outPosition).getCuserid());
        bundle.putString("articleId", mDataBeans.get(outPosition).getArticleId());
        JumpUtil.overlay(mContext, DynamicDetailActivity.class, bundle);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //        TextView mTvDelete;
        TextView mTvContent;
        //   AppCompatTextView mTvAddress;
        AppCompatTextView mTvDiscuss;
        AppCompatTextView mTvLike;
        //        TextView mTvTime;
//        ImageView mIvLikeIcon;
        //        RecyclerView commentRecycler;
        RecyclerView imageRecycler;
        RelativeLayout layoutImage;
        public FrameLayout containerPlay;
        //        public PrepareView mPrepareView;
//        ImageView mIvThumb;
//        private MineCommentAdapter commentAdapter;
        private TextView tv_like_comment;

        public int mPosition;
        //        private final ImageView img_sanjiao;
        private final LinearLayout ll_view;
        private final CircleImageView iv_user_head;
        private final TextView tvName;
        //        private final TextView tvAddressTime;
        private MyScaleImageView iv_video_brg;
        private ImageView img_more;

        private LottieAnimationView lottie_animation_like;

        private FrameLayout fl_comment;
        private AppCompatTextView tv_diamonds;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);   //显示内容
            //    mTvAddress = itemView.findViewById(R.id.tv_address);  //地址
            mTvDiscuss = itemView.findViewById(R.id.tv_discuss);  //评论
//            mTvTime = itemView.findViewById(R.id.tv_time);   //发布时间
            mTvLike = itemView.findViewById(R.id.tv_like);
//            mTvDelete = itemView.findViewById(R.id.tv_delete);
//            mIvLikeIcon = itemView.findViewById(R.id.iv_like_icon);
//            commentRecycler = itemView.findViewById(R.id.comment_recycler);
            imageRecycler = itemView.findViewById(R.id.recycler_image);
            layoutImage = itemView.findViewById(R.id.layout_image_video);
            containerPlay = itemView.findViewById(R.id.player_container);
//            mPrepareView = itemView.findViewById(R.id.prepare_view);
            iv_video_brg = itemView.findViewById(R.id.iv_video_brg);
//            mIvThumb = mPrepareView.findViewById(R.id.iv_thumb);
//            img_sanjiao = itemView.findViewById(R.id.img_sanjiao);
            ll_view = itemView.findViewById(R.id.ll_view);
            iv_user_head = itemView.findViewById(R.id.iv_user_head);
            tvName = itemView.findViewById(R.id.tv_name);
//            tvAddressTime = itemView.findViewById(R.id.tv_address_time);
            tv_like_comment = itemView.findViewById(R.id.tv_like_comment);
            img_more = itemView.findViewById(R.id.img_more);
            lottie_animation_like = itemView.findViewById(R.id.lottie_animation_like);

            fl_comment = itemView.findViewById(R.id.fl_comment);
            tv_diamonds = itemView.findViewById(R.id.tv_diamonds);

            itemView.setTag(this);
        }
    }

    public interface onDynamicClickListener {
        void onPositionClick(int position, View v);
    }

    public interface getReplayName {
        void replayName(String name);
    }

    public interface OnClickOutListener {
        void getClickName(String name, int position, int outPosition);
    }
}
