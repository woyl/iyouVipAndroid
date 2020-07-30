package com.jfkj.im.adapter.dynamic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.ChildCommentList;
import com.jfkj.im.R;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CommentBean;
import com.jfkj.im.ui.home.DynamicDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.TimeUtil;
import com.jfkj.im.utils.ToastUtils;
import com.lzy.okgo.utils.OkLogger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<CommentBean> mCommentBeanList = new ArrayList<>();
    private LayoutInflater mInflater;
    private onCommentClick mCommentClick;
    private int outPosition;

    private CommonRecyclerAdapter<ChildCommentList> adapter;
    private String id;
    private String type = "0";

    public CommentAdapter(Context context, int position, List<CommentBean> list, onCommentClick commentClick) {
        this.mCommentBeanList = list;
        this.mContext = context;
        this.mCommentClick = commentClick;
        this.outPosition = position;
        mInflater = LayoutInflater.from(mContext);
    }

    public CommentAdapter(Context context, int position, List<CommentBean> list, onCommentClick commentClick, String id, String type) {
        this.mCommentBeanList = list;
        this.mContext = context;
        this.mCommentClick = commentClick;
        this.outPosition = position;
        mInflater = LayoutInflater.from(mContext);
        this.id = id;
        this.type = type;
    }

    public void setNewData(List<CommentBean> list) {
        mCommentBeanList.clear();
        mCommentBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentBean bean = mCommentBeanList.get(position);

        try {
            holder.text_time.setText(TimeUtil.getInstance().getTimeFormatText(bean.getAddDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(mContext).load(bean.getCommentHead()).into(holder.cir_head);
        holder.tv_name.setText(bean.getCommentNickName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        holder.recycler_dis.setLayoutManager(linearLayoutManager);
        holder.recycler_dis.addItemDecoration(new SpacesItemDecoration(10));

        if(bean.getRuserId().equals("0")){
            //一级
            holder.mTvComment.setText(Html.fromHtml(" <font color=\\'#fffdfdfd\\'>"+bean.getContent()+"</font>"));
        }else{
            holder.mTvComment.setText(Html.fromHtml("<font color=\'#fffdfdfd\'>回复</font> <font color=\'#FF2B66\'>"+bean.getCommentRNickName()+"：</font> <font color=\'#fffdfdfd\'>"+bean.getContent()+"</font>"));
        }

//        holder.mTvComment.setText(bean.getContent());

//        adapter = new CommonRecyclerAdapter<ChildCommentList>(mContext, bean.getChildCommentList(), R.layout.item_discuss) {
//            @Override
//            public void convert(CommonRecyclerHolder holder, ChildCommentList childCommentList, int position) {
//                TextView text_content = holder.getView(R.id.text_content);
//                SpannableStringBuilder builder = new SpannableStringBuilder();
//                SpannableString nickName = new SpannableString(childCommentList.getCommentNickName());
//                SpannableString rNickName = new SpannableString(childCommentList.getCommentRNickName());
//                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF333333"));
//                ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF333333"));
//                nickName.setSpan(colorSpan, 0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                rNickName.setSpan(colorSpan1, 0, rNickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                builder.append(nickName);
//                builder.append("回复");
//                builder.append(rNickName);
//                builder.append(":");
//                builder.append(childCommentList.getContent());
//                text_content.setText(builder);
//                text_content.setMovementMethod(LinkMovementMethod.getInstance());
////                if (!"".equals(bean.getCommentRNickName()) && bean.getCommentRNickName() != null){
////                    SpannableString nickName = new SpannableString(bean.getCommentNickName());
////                    SpannableString rNickName = new SpannableString(bean.getCommentRNickName());
////                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF333333"));
////                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF333333"));
////                    nickName.setSpan(colorSpan, 0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
////                    rNickName.setSpan(colorSpan1,0,rNickName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
////                    builder.append(nickName);
////                    builder.append("回复");
////                    builder.append(rNickName);
////                    builder.append(":");
////                    builder.append(bean.getContent());
////                    text_content.setText(builder);
////                    text_content.setMovementMethod(LinkMovementMethod.getInstance());
////                }else {
////                    SpannableString nickName = new SpannableString(bean.getCommentNickName());
////                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF333333"));
////                    nickName.setSpan(colorSpan, 0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
////                    builder.append(nickName);
////                    builder.append(":");
////                    builder.append(bean.getContent());
////                    text_content.setText(builder);
////                    text_content.setMovementMethod(LinkMovementMethod.getInstance());
////                }
//
//                text_content.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String userId = "";
//                        String name = "";
//                        if (!childCommentList.getUserId().equals(UserInfoManger.getUserId())){
//                            userId = childCommentList.getUserId();
//                            name = childCommentList.getCommentNickName();
//                        }else {
//                            userId = childCommentList.getRuserId();
//                            name = childCommentList.getCommentRNickName();
//                        }
//                        mCommentClick.getReplayName(name, outPosition, position, bean.getContentId(),userId);
//                    }
//                });
//            }
//        };
//        holder.recycler_dis.setAdapter(adapter);
//        if (bean.getChildCommentList() != null && bean.getChildCommentList().size() > 0) {
//            holder.recycler_dis.setVisibility(View.VISIBLE);
//        }

        final int[] num = {0};
        if (type.equals("1")) {
            ViewTreeObserver viewTreeObserver = holder.recycler_dis.getViewTreeObserver();
            LinearLayout.LayoutParams recyclerDisLayoutManager = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerDisLayoutManager.height = 0;
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mCommentBeanList.size() == num[0]) {
                        return;
                    }
                    if (bean.getChildCommentList() != null && bean.getChildCommentList().size() > 2) {
                        View view = linearLayoutManager.findViewByPosition(0);
                        if (view != null) {
                            int w = linearLayoutManager.getDecoratedMeasuredWidth(view);
                            int h = linearLayoutManager.getDecoratedMeasuredHeight(view);
                            recyclerDisLayoutManager.height = (int) (h * 1.8);
                            holder.recycler_dis.setLayoutParams(recyclerDisLayoutManager);
                            holder.tv_view_more.setVisibility(View.VISIBLE);
                            Log.i("msg", "..........22222........................." + w);
                            Log.i("msg", "...........2222........................" + h);
                            num[0]++;
                            Log.i("msg", "...........num[0]........................" + num[0]);
                        }
                    } else {
                        holder.tv_view_more.setVisibility(View.GONE);
                    }
                }
            });
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(UserInfoManger.getUserId(),bean.getUserId())){
                    mCommentClick.getReplayName(bean.getCommentNickName(), outPosition, position, bean.getContentId(),bean.getUserId());
                }
            }
        });

        holder.tv_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("cuserId", bean.getUserId());
                bundle.putString("articleId", bean.getArticleId());
                JumpUtil.overlay(mContext, DynamicDetailActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCommentBeanList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView mTvComment;
        LinearLayout layout;
        CircleImageView cir_head;
        AppCompatTextView tv_name;
        AppCompatTextView text_time;
        RecyclerView recycler_dis;
        TextView tv_view_more;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvComment = itemView.findViewById(R.id.tv_comment);
            layout = itemView.findViewById(R.id.cl_layoud);
            cir_head = itemView.findViewById(R.id.cir_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            text_time = itemView.findViewById(R.id.text_time);
            recycler_dis = itemView.findViewById(R.id.recycler_dis);
            tv_view_more = itemView.findViewById(R.id.tv_view_more);
        }
    }

    public interface onCommentClick {
        void getReplayName(String name, int outPosition, int position, String contentId,String userId);
    }

}
