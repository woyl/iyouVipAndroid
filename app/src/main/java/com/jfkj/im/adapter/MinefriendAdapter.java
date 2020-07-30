package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.Bean.SortModel;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.face.FaceManager;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.activity.MinefriendActivity;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.WebSocket;

/**
 * @author: xp
 * @date: 2017/7/19
 */

public class MinefriendAdapter extends RecyclerView.Adapter<MinefriendAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SortModel> mData;
    private Context mContext;

    private CommonRecyclerAdapter<SortModel> commonRecyclerAdapter;
    private ResponListener<String> responListener;
    private SwipeMenuCreator swipeMenuCreator;


    public void setRsp(ResponListener<String> responListener) {
        this.responListener = responListener;
    }

    public MinefriendAdapter(Context context, SwipeMenuCreator swipeMenuCreator) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.swipeMenuCreator = swipeMenuCreator;
    }

    public void cleatList() {
        if (mData != null) {
            this.mData.clear();
        }

    }

    public void setmData(List<SortModel> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public MinefriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTag = (TextView) view.findViewById(R.id.tag);
        viewHolder.recyc_list = view.findViewById(R.id.recyc_list);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MinefriendAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);


        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(mData.get(position));

//        holder.recyc_list.setLayoutManager(new LinearLayoutManager(mContext));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        holder.recyc_list.setAdapter(null);
        holder.recyc_list.setSwipeMenuCreator(swipeMenuCreator);
        holder.recyc_list.setLayoutManager(layoutManager);
        holder.recyc_list.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
                menuBridge.closeMenu();
                responListener.Rsp(sortModels.get(adapterPosition).getUserid()+"");
            }
        });
        commonRecyclerAdapter = new CommonRecyclerAdapter<SortModel>(mContext, sortModels, R.layout.item_mine_friend) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, SortModel sortModel, int position) {
                TextView tvName = (TextView) holder.itemView.findViewById(R.id.name);
                ImageView select_iv = holder.itemView.findViewById(R.id.select_iv);
                LinearLayout ly_user = holder.itemView.findViewById(R.id.ly_user);
                TextView tv_level = holder.itemView.findViewById(R.id.tv_level);
                ImageView default_head_iv = holder.itemView.findViewById(R.id.default_head_iv);
                //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                Glide.with(mContext).load(mData.get(position).getHead_url()).into(default_head_iv);
                tv_level.setText("VIP" + this.mData.get(position).getVipLevel() + "");
                FaceManager.handlerEmojiText(tvName, mData.get(position).getName(), false);
                select_iv.setVisibility(View.GONE);


                default_head_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra("userId",  mData.get(position).getUserid()+"");
                        mContext.startActivity(intent);
                    }
                });




                ly_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SortModel sortModel = mData.get(position);
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setChatName(sortModel.getName());
                        chatInfo.setType(TIMConversationType.C2C);
                        chatInfo.setId(sortModel.getUserid() + "");
                        chatInfo.setChatRoom(false);
                        intent.putExtra(Constants.CHAT_INFO, chatInfo);
                        mContext.startActivity(intent);
                    }
                });
            }
        };

        holder.recyc_list.setAdapter(commonRecyclerAdapter);
//        // 设置菜单创建器。

        // holder.tvName.setText(this.mData.get(position).getName());

//        holder.ly_user.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = new Intent("longuser");
//                intent.putExtra(Utils.NICKNAME, mData.get(position).getName());
//                intent.putExtra(Utils.RECEIVEID, mData.get(position).getUserid()+"");
//                mContext.sendBroadcast(intent);
//                return false;
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        SwipeRecyclerView recyc_list;
        TextView tvTag;
        TextView tvName, tv_level;
        ImageView select_iv;
        LinearLayout ly_user;
        CircleImageView default_head_iv;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void updateList(List<SortModel> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    //    public int getSectionForPosition(int position) {
//        return mData.get(position).getLetters().charAt(0);
//    }
    public int getSectionForPosition(int position) {
        //  return mData.get(position).getLetters().charAt(0);
        if (mData.get(position).getLetters() != null) {
            return mData.get(position).getLetters().charAt(0);
        }
        return 0;
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            if (sortStr != null) {
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }
        return 0;
    }




}
