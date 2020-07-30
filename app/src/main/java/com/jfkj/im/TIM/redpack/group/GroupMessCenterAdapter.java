package com.jfkj.im.TIM.redpack.group;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.interfaces.ResponListeners;
import com.jfkj.im.interfaces.ResponListtenerThrees;
import com.tencent.imsdk.ext.group.TIMGroupPendencyHandledStatus;

import java.util.List;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_TWO;

public class GroupMessCenterAdapter extends BaseMultiItemQuickAdapter<RedPackageCustom, BaseViewHolder> {

    private ResponListtenerThrees<Integer, String, String, String> responListeners;
    private FragmentManager fragmentManager;

    public void setResponListeners(ResponListtenerThrees<Integer, String, String, String> responListeners) {
        this.responListeners = responListeners;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GroupMessCenterAdapter(List<RedPackageCustom> data, FragmentManager fragmentManager) {
        super(data);
        this.fragmentManager = fragmentManager;
        addItemType(9, R.layout.item_groupcenter1);
        addItemType(7, R.layout.item_groupcenter2);
        addItemType(8, R.layout.item_groupcenter3);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, RedPackageCustom item) {

        int position = helper.getPosition();

        RelativeLayout relativelayout = helper.getView(R.id.relativelayout);
        RelativeLayout ly_reject_accept = helper.getView(R.id.ly_reject_accept);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_message = helper.getView(R.id.tv_message);
        TextView tv_state = helper.getView(R.id.tv_state);
        TextView tv_nickname = helper.getView(R.id.tv_nickname);
        TextView tv_enter_message = helper.getView(R.id.tv_enter_message);
        TextView tv_group_name = helper.getView(R.id.tv_group_name);
        TextView tv_group_message = helper.getView(R.id.tv_group_message);
        TextView tv_answer = helper.getView(R.id.tv_answer);
        switch (helper.getItemViewType()) {
            case 9:
                if (item.isReceive()) {//接收
                    relativelayout.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.getReceiveHeadUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                    tv_name.setText(item.getReceiveName());
                    tv_nickname.setText(item.getReceiveName() + ":");
                    tv_enter_message.setText(item.getSendContent());
                    tv_message.setText("申请加入" + item.getGroupName());
                    tv_state.setVisibility(View.GONE);
                    tv_group_name.setVisibility(View.GONE);
                    tv_group_message.setVisibility(View.GONE);
                    tv_answer.setVisibility(View.GONE);
                } else {
                    relativelayout.setVisibility(View.GONE);
                    Glide.with(mContext).load(item.getAvatarUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                    tv_name.setText(item.getSendName());
                    tv_message.setText("邀请你加入" + item.getGroupName());
                    tv_state.setVisibility(View.GONE);
                    if (item.getIntHandledStatus() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()
                            && item.getIntOperationType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()
                            && item.getIntPendencyType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()) {

                    } else if (item.getIntHandledStatus() == TIMGroupPendencyHandledStatus.HANDLED_BY_SELF.getValue()
                            && item.getIntOperationType() == TIMGroupPendencyHandledStatus.HANDLED_BY_OTHER.getValue()
                            && item.getIntPendencyType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()) {
                        tv_state.setText("已通过");
                        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        tv_state.setVisibility(View.VISIBLE);
                        ly_reject_accept.setVisibility(View.GONE);
                    } else if (item.getIntHandledStatus() == TIMGroupPendencyHandledStatus.HANDLED_BY_SELF.getValue()
                            && item.getIntOperationType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()
                            && item.getIntPendencyType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()) {
                        tv_state.setText("已拒绝");
                        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        tv_state.setVisibility(View.VISIBLE);
                        ly_reject_accept.setVisibility(View.GONE);
                    }
                }
                //拒绝
                helper.getView(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RefuseGroupDialogFragment dialogFragment = new RefuseGroupDialogFragment(true, Gravity.CENTER);
                        dialogFragment.show(fragmentManager, "");
                        dialogFragment.setResponListener(new ResponListener<String>() {
                            @Override
                            public void Rsp(String s) {
                                responListeners.Rsps(0, item.getGroupId(), s, item.getSendId(), position);
                            }
                        });
                    }
                });
                //同意
                helper.getView(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        responListeners.Rsps(1, item.getGroupId(), "", item.getSendId(), position);
                    }
                });
                break;
            case 8:
                if (item.isReceive()) {
                    if (item.getIntHandledStatus() == TIMGroupPendencyHandledStatus.HANDLED_BY_SELF.getValue()
                            && item.getIntOperationType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()
                            && item.getIntPendencyType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()) {
                        tv_state.setText("已拒绝");
                        Glide.with(mContext).load(item.getReceiveHeadUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                        tv_name.setText(item.getReceiveName());
                        tv_message.setText("申请加入" + item.getGroupName());
                        tv_nickname.setText(item.getReceiveName() + ":");
                        tv_enter_message.setText(item.getSendContent());
                        if (!TextUtils.isEmpty(item.getReceiveContent())) {
                            tv_group_name.setText(item.getSendName() + ":");
                            tv_group_message.setText(item.getReceiveContent());
                            tv_group_name.setVisibility(View.VISIBLE);
                            tv_group_message.setVisibility(View.VISIBLE);
                        }
                    } else if (item.getIntHandledStatus() == TIMGroupPendencyHandledStatus.HANDLED_BY_SELF.getValue()
                            && item.getIntOperationType() == TIMGroupPendencyHandledStatus.HANDLED_BY_OTHER.getValue()
                            && item.getIntPendencyType() == TIMGroupPendencyHandledStatus.NOT_HANDLED.getValue()) {
                        tv_state.setText("已同意");
                        Glide.with(mContext).load(item.getReceiveHeadUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                        tv_name.setText(item.getReceiveName());
                        tv_message.setText("申请加入" + item.getGroupName());
                        tv_nickname.setText(item.getReceiveName() + ":");
                        tv_enter_message.setText(item.getSendContent());
                        tv_group_name.setText(item.getGroupName() + ":");
                        tv_group_message.setText(item.getReceiveContent());
                    }
                } else {
                    if (item.getIntHandledStatus() == 0) {
                        tv_state.setText("等待通过");
                        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.cFF2B66));
                        Glide.with(mContext).load(item.getAvatarUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                        tv_name.setText(item.getSendName());
                        tv_message.setText("申请加入" + item.getGroupName());
                        tv_nickname.setText(item.getSendName() + ":");
                        tv_enter_message.setText(item.getSendContent());
//                        tv_group_name.setText(item.getGroupName()+":");
//                        tv_group_message.setText(item.getReceiveContent());
                    } else if (item.getIntHandledStatus() == 1) {
                        tv_state.setText("已拒绝");
                        Glide.with(mContext).load(item.getAvatarUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                        tv_name.setText(item.getSendName());
                        tv_message.setText("申请加入" + item.getGroupName());
                        tv_nickname.setText(item.getSendName() + ":");
                        tv_enter_message.setText(item.getSendContent());
                        if (!TextUtils.isEmpty(item.getReceiveContent())) {
                            tv_group_name.setText(item.getReceiveName() + ":");
                            tv_group_message.setText(item.getReceiveContent());
                            tv_group_name.setVisibility(View.VISIBLE);
                            tv_group_message.setVisibility(View.VISIBLE);
                        }
                    } else if (item.getIntHandledStatus() == 2) {
                        tv_state.setText("已通过");
                        Glide.with(mContext).load(item.getAvatarUrl()).into((ImageView) helper.getView(R.id.circleimageview));
                        tv_name.setText(item.getSendName());
                        tv_message.setText("申请加入" + item.getGroupName());
                        tv_nickname.setText(item.getSendName() + ":");
                        tv_enter_message.setText(item.getSendContent());
                    }
                }
                break;
        }
    }
}
