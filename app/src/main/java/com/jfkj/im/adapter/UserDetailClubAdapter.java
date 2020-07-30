package com.jfkj.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.group.ApplyJoinGroupActivity;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * Description:  用户详情 club adapter
 * @author :   ys
 * @date :         2020/3/17
 * </pre>
 */
public class UserDetailClubAdapter extends RecyclerView.Adapter<UserDetailClubAdapter.ViewHolder> {


    private Context mContext;
    private List<UserDetailClubBean.DataBean> mDataBeanList = new ArrayList<>();

    public UserDetailClubAdapter(Context context, List<UserDetailClubBean.DataBean> list) {
        this.mContext = context;
        this.mDataBeanList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grouptask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDetailClubBean.DataBean dataBean = mDataBeanList.get(position);
        holder.mTvName.setText(dataBean.getGroupName());  //俱乐部名称

     //   Glide.with(mContext).load(dataBean.getGroupHead()).transform(new CircleCrop()).into(holder.mImgClubVip);

        holder.mTvGift.setText("x" +dataBean.getRedPacketNum() + "/人");

        if (dataBean.getIsSuper() != 0) {
            Glide.with(mContext).load(R.mipmap.icon_club_vip).into( holder.mImgClubVip);
            holder.ivHeat.setVisibility(View.VISIBLE);
        } else {
            Glide.with(mContext).load(R.mipmap.icon_club_ordinary).into( holder.mImgClubVip);
            holder.ivHeat.setVisibility(View.GONE);
        }


        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add(Utils.APPID);


                TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                        List<String> listgroupid = new ArrayList<>();
                        for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                            listgroupid.add(timGroupBaseInfo.getGroupId());
                        }
                        if (listgroupid.contains(dataBean.getGroupId())) {
                            Intent intent = new Intent(mContext, ChatActivity.class);
                            ChatInfo chatInfo = new ChatInfo();
                            chatInfo.setChatName(dataBean.getGroupName());
                            chatInfo.setType(TIMConversationType.Group);
                            chatInfo.setChatRoom(false);
                            chatInfo.setId(dataBean.getGroupId());
                            intent.putExtra(Constants.CHAT_INFO, chatInfo);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, ApplyJoinGroupActivity.class);
                            intent.putExtra(Utils.GROUPID, dataBean.getGroupId());
                            mContext.startActivity(intent);

                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeanList == null ? 0 : mDataBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_club_vip)
        ImageView mImgClubVip;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_gift)
        TextView mTvGift;
     //   @BindView(R.id.img_vip_group)
        ImageView mImgVipGroup;
        @BindView(R.id.rl_layout)
        RelativeLayout rl_layout;

        @BindView(R.id.iv_heat)
        ImageView ivHeat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
