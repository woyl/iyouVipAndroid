package com.jfkj.im.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupcenterBean;
import com.jfkj.im.GroupMessagecenterBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupcenterAdapter extends BaseMultiItemQuickAdapter<GroupcenterBean, BaseViewHolder> {

    public GroupcenterAdapter(List<GroupcenterBean> data) {
        super(data);
        addItemType(1, R.layout.item_groupcenter1);
        addItemType(2, R.layout.item_groupcenter2);
        addItemType(3, R.layout.item_groupcenter3);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupcenterBean item) {
        GroupcenterBean.WaitpassageBean waitpassageBean = item.getWaitpassageBean();
        CircleImageView circleimageview= helper.getView(R.id.circleimageview);
        Glide.with(App.getAppContext()).load(waitpassageBean.getMasterhead()).into(circleimageview);
        TextView tv_name = helper.getView(R.id.tv_name);
        tv_name.setText(waitpassageBean.getMaster());
        switch (helper.getItemViewType()) {
            case 1:
                RelativeLayout relativelayout1 = helper.getView(R.id.relativelayout);
                TextView tv_message1 = helper.getView(R.id.tv_message);
                tv_message1.setText(waitpassageBean.getMessage());
                TextView tv_nickname1= helper.getView(R.id.tv_nickname);
                TextView tv_enter_message1= helper.getView(R.id.tv_enter_message);
                TextView tv_group_name1= helper.getView(R.id.tv_group_name);
                TextView tv_group_message1= helper.getView(R.id.tv_group_message);
                TextView tv_answer1= helper.getView(R.id.tv_answer);
                if(waitpassageBean.getChat2().trim().length()>0){
                    tv_group_name1.setVisibility(View.VISIBLE);
                    tv_group_message1.setVisibility(View.VISIBLE);
                    tv_group_name1.setText(waitpassageBean.getVisitor()+":");
                    tv_group_message1.setText(waitpassageBean.getChat2());
                }else {
                    tv_group_name1.setVisibility(View.GONE);
                    tv_group_message1.setVisibility(View.GONE);
                }
                if(waitpassageBean.getChat2().trim().length()>0){
                    tv_answer1.setVisibility(View.GONE);
                }else {
                    tv_answer1.setVisibility(View.VISIBLE);
                }
               if(waitpassageBean.getChat1().length()>0){
                   relativelayout1.setVisibility(View.VISIBLE);
               }else {
                   relativelayout1.setVisibility(View.GONE);
               }
               if(waitpassageBean.getChat2().length()>0){
                   tv_group_name1.setVisibility(View.VISIBLE);
                   tv_group_message1.setVisibility(View.VISIBLE);
               }else {
                   tv_group_name1.setVisibility(View.GONE);
                   tv_group_message1.setVisibility(View.GONE);
               }
                tv_nickname1.setText(waitpassageBean.getMaster());
                tv_enter_message1.setText(waitpassageBean.getChat1());

                tv_answer1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent("answer");
                        intent.putExtra(Utils.GROUPID,waitpassageBean.getGroupId()+"");
                        intent.putExtra(Utils.APPLYID,waitpassageBean.getMasterId()+"");
                        intent.putExtra(Utils.MSGID,waitpassageBean.getMsgId()+"");
                        intent.putExtra(Utils.NICKNAME,tv_name.getText().toString().trim());
                        App.getAppContext().sendBroadcast(intent);
                    }
                });
                //拒绝
                if(tv_message1.getText().toString().trim().startsWith("邀请")){
                    helper.itemView.findViewById(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Utils.netWork()) {
                                Intent intent = new Intent("handlerInviteGroup");
                                intent.putExtra(Utils.HANDLERINVITEGROUP, "reject");
                                intent.putExtra(Utils.GROUPID, waitpassageBean.getGroupId() + "");
                                intent.putExtra(Utils.MSGID, waitpassageBean.getMsgId() + "");
                                intent.putExtra(Utils.SENDID, waitpassageBean.getMasterId() + "");
                                App.getAppContext().sendBroadcast(intent);
                            } else {
                                Toast.makeText(mContext, R.string.nonetwork, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //接受
                    helper.itemView.findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Utils.netWork()) {
                                Intent intent = new Intent("handlerInviteGroup");
                                intent.putExtra(Utils.HANDLERINVITEGROUP, "accept");
                                intent.putExtra(Utils.GROUPID, waitpassageBean.getGroupId() + "");
                                intent.putExtra(Utils.MSGID, waitpassageBean.getMsgId() + "");
                                intent.putExtra(Utils.SENDID, waitpassageBean.getMasterId() + "");
                                App.getAppContext().sendBroadcast(intent);
                            } else {
                                Toast.makeText(mContext, R.string.nonetwork, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if(tv_message1.getText().toString().trim().startsWith("申请")){
                    helper.itemView.findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Utils.netWork()) {
                                Intent intent = new Intent("handlerJoinGroup");
                                intent.putExtra(Utils.HANDLERINVITEGROUP, "accept");
                                intent.putExtra(Utils.GROUPID, waitpassageBean.getGroupId() + "");
                                intent.putExtra(Utils.MSGID, waitpassageBean.getMsgId() + "");
                                intent.putExtra(Utils.SENDID, waitpassageBean.getMasterId() + "");
                                App.getAppContext().sendBroadcast(intent);
                            } else {
                                Toast.makeText(mContext, R.string.nonetwork, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    helper.itemView.findViewById(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Utils.netWork()) {
                                Intent intent = new Intent("handlerJoinGroup");
                                intent.putExtra(Utils.HANDLERINVITEGROUP, "reject");
                                intent.putExtra(Utils.GROUPID, waitpassageBean.getGroupId() + "");
                                intent.putExtra(Utils.MSGID, waitpassageBean.getMsgId() + "");
                                intent.putExtra(Utils.SENDID, waitpassageBean.getMasterId() + "");
                                mContext.sendBroadcast(intent);
                            } else {
                                Toast.makeText(mContext, R.string.nonetwork, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            break;
            case 2://这里设置成群主主动邀请用户的布局
                TextView tv_message2 = helper.getView(R.id.tv_message);
                tv_message2.setText(waitpassageBean.getMessage());
                Glide.with(mContext).load(UserInfoManger.getUserHeadUrl()).into(circleimageview);
                break;
            case 3:
                RelativeLayout relativeLayout3=helper.getView(R.id.relativelayout);
                TextView tv_state3 = helper.getView(R.id.tv_state);
                TextView tv_message3 = helper.getView(R.id.tv_message);

                tv_state3.setText(waitpassageBean.getState());
                tv_message3.setText(waitpassageBean.getMessage());
                TextView tv_nickname3= helper.getView(R.id.tv_nickname);
                TextView tv_enter_message3= helper.getView(R.id.tv_enter_message);
                tv_nickname3.setText(waitpassageBean.getMaster());
                tv_enter_message3.setText(waitpassageBean.getChat1());
                TextView tv_group_name3= helper.getView(R.id.tv_group_name);
                TextView tv_group_message3= helper.getView(R.id.tv_group_message);
              if(waitpassageBean.getChat1().trim().length()>0){
                  relativeLayout3.setVisibility(View.VISIBLE);
              }else {
                  relativeLayout3.setVisibility(View.GONE);
              }
                tv_group_message3.setText(waitpassageBean.getChat2());
                tv_group_name3.setText(waitpassageBean.getVisitor());


                break;
        }
    }
}
