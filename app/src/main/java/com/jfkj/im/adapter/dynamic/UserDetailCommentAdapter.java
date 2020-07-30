package com.jfkj.im.adapter.dynamic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.Bean.ChildCommentList;
import com.jfkj.im.R;
import com.jfkj.im.entity.CommentBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class UserDetailCommentAdapter extends RecyclerView.Adapter<UserDetailCommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<CommentBean> mCommentBeanList = new ArrayList<>();
    private LayoutInflater mInflater;
    private onCommentClick mCommentClick;
    private int outPosition;

    public UserDetailCommentAdapter(Context context, int position, List<CommentBean> list, onCommentClick commentClick) {
        this.mCommentBeanList = list;
        this.mContext = context;
        this.mCommentClick = commentClick;
        this.outPosition = position;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setNewData(List<CommentBean> list) {
        mCommentBeanList.clear();
        mCommentBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_mine_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentBean bean = mCommentBeanList.get(position);
//        if (!"".equals(bean.getCommentRNickName()) && bean.getCommentRNickName() != null){
//
//            SpannableString nickName = new SpannableString(bean.getCommentNickName());
//            SpannableString rNickName = new SpannableString(bean.getCommentRNickName());
//            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFFFF"));
//            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FFFFFFFF"));
//            nickName.setSpan(colorSpan, 0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            rNickName.setSpan(colorSpan1,0,rNickName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//
//            builder.append(nickName);
//            builder.append("回复");
//            builder.append(rNickName);
//            builder.append(":");
//            builder.append(bean.getContent());
//
//
//            holder.mTvComment.setText(builder);
//            holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
//        }else {
//            SpannableString nickName = new SpannableString(bean.getCommentNickName());
//            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFFFF"));
//            nickName.setSpan(colorSpan, 0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//
//            builder.append(nickName);
//            builder.append(":");
//            builder.append(bean.getContent());
//
//            holder.mTvComment.setText(builder);
//            holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
//        }

        if (!TextUtils.isEmpty(bean.getCommentNickName())) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString nickName = new SpannableString(bean.getCommentNickName());
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFFFF"));
            nickName.setSpan(colorSpan, 0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            builder.append(nickName);
            builder.append(":");
            builder.append(bean.getContent());

            if (bean.getChildCommentList() != null && bean.getChildCommentList().size() > 0) {
                for (ChildCommentList childCommentList : bean.getChildCommentList()) {
                    SpannableStringBuilder nickName1 = new SpannableStringBuilder(childCommentList.getCommentNickName());
                    SpannableStringBuilder rNickName1 = new SpannableStringBuilder(childCommentList.getCommentRNickName());
                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FFFFFFFF"));
                    ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FFFFFFFF"));
                    nickName1.setSpan(colorSpan1, 0, nickName1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    rNickName1.setSpan(colorSpan2, 0, rNickName1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    nickName1.append(" 回复 ");
                    nickName1.append(rNickName1);
                    nickName1.append(":");
                    nickName1.append(childCommentList.getContent());

                    builder.append("\n").append(nickName1);
                }
            }

            if (mCommentBeanList != null) {
                if (mCommentBeanList.size() == 1) {
                    if (mCommentBeanList.get(0).getChildCommentList() != null && mCommentBeanList.get(0).getChildCommentList().size() > 1) {
//                        holder.mTvComment.measure(View.MeasureSpec.getMode(0), View.MeasureSpec.getMode(0));
//                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
//                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.mTvComment.getLayoutParams();
//                        Log.e("msg","................................"+holder.mTvComment.getMeasuredHeight());
//                        params.height = 0;
//                        params.height = holder.mTvComment.getMeasuredHeight() * 3;
//                        layoutParams.height = 0;
//                        layoutParams.height = holder.mTvComment.getMeasuredHeight() * 2;
                        holder.tvViewMore.setVisibility(View.VISIBLE);
//                        Log.e("msg","................................"+ params.height);
                        holder.mTvComment.setText(builder);
                        holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                    } else {
                        holder.mTvComment.setText(builder);
                        holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                } else if (mCommentBeanList.size() == 2) {
                    for (int i = 0; i < mCommentBeanList.size(); i++) {
                        if (position == 0) {
                            if (mCommentBeanList.get(0).getChildCommentList() != null && mCommentBeanList.get(0).getChildCommentList().size() > 1) {
//                                holder.mTvComment.measure(View.MeasureSpec.getMode(0), View.MeasureSpec.getMode(0));
//                                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
//                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.mTvComment.getLayoutParams();
//                                params.height = 0;
//                                params.height = holder.mTvComment.getMeasuredHeight() * 3;
//                                layoutParams.height = 0;
//                                layoutParams.height = holder.mTvComment.getMeasuredHeight() * 2;
                                holder.tvViewMore.setVisibility(View.VISIBLE);
                                holder.mTvComment.setText(builder);
                                holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                                break;
                            } else {
                                holder.mTvComment.setText(builder);
                                holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                                break;
                            }
                        } else {
                            if (i == 1) {
                                if (mCommentBeanList.get(0).getChildCommentList() != null && mCommentBeanList.get(0).getChildCommentList().size() == 0) {
//                                    holder.mTvComment.measure(View.MeasureSpec.getMode(0), View.MeasureSpec.getMode(0));
//                                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
//                                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.mTvComment.getLayoutParams();
//                                    params.height = 0;
//                                    params.height = holder.mTvComment.getMeasuredHeight() * 2;
//                                    layoutParams.height = 0;
//                                    layoutParams.height = holder.mTvComment.getMeasuredHeight();
                                    holder.tvViewMore.setVisibility(View.VISIBLE);
                                    holder.mTvComment.setText(builder);
                                    holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                                    break;
                                } else {
                                    holder.tvViewMore.setVisibility(View.GONE);
                                    holder.mTvComment.setVisibility(View.GONE);
                                    break;
                                }
                            }
                        }
                    }

                } else {
                    if (position == 0) {
                        if (mCommentBeanList.get(0).getChildCommentList() != null && mCommentBeanList.get(0).getChildCommentList().size() > 1) {
//                                holder.mTvComment.measure(View.MeasureSpec.getMode(0), View.MeasureSpec.getMode(0));
//                                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
//                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.mTvComment.getLayoutParams();
//                                params.height = 0;
//                                params.height = holder.mTvComment.getMeasuredHeight() * 3;
//                                layoutParams.height = 0;
//                                layoutParams.height = holder.mTvComment.getMeasuredHeight() * 2;
                            holder.tvViewMore.setVisibility(View.VISIBLE);
                            holder.mTvComment.setText(builder);
                            holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                        } else {
                            holder.mTvComment.setText(builder);
                            holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                    } else {
                        if (position == 1) {
                            if (mCommentBeanList.get(0).getChildCommentList() != null && mCommentBeanList.get(0).getChildCommentList().size() == 0) {
//                                    holder.mTvComment.measure(View.MeasureSpec.getMode(0), View.MeasureSpec.getMode(0));
//                                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layout.getLayoutParams();
//                                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.mTvComment.getLayoutParams();
//                                    params.height = 0;
//                                    params.height = holder.mTvComment.getMeasuredHeight() * 2;
//                                    layoutParams.height = 0;
//                                    layoutParams.height = holder.mTvComment.getMeasuredHeight();
                                holder.tvViewMore.setVisibility(View.VISIBLE);
                                holder.mTvComment.setText(builder);
                                holder.mTvComment.setMovementMethod(LinkMovementMethod.getInstance());
                            } else {
                                holder.tvViewMore.setVisibility(View.GONE);
                                holder.mTvComment.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

        }

        holder.mTvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentClick.getReplayName(bean.getCommentNickName(), outPosition, position);
            }
        });


        //第二条评论,显示
//        if (position == 1) {
//            holder.tvViewMore.setVisibility(View.VISIBLE);
//        }


        holder.tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentClick.onMore(outPosition);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCommentBeanList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView mTvComment;
        TextView tvViewMore;
        private final ConstraintLayout layout;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvComment = itemView.findViewById(R.id.tv_comment);
            tvViewMore = itemView.findViewById(R.id.tv_view_more);
            layout = itemView.findViewById(R.id.cl_layoud);
        }
    }

    public interface onCommentClick {
        void getReplayName(String name, int outPosition, int position);

        void onMore(int position);
    }

}
