package com.jfkj.im.TIM.modules.group.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupNewBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.base.IUIKitCallsBack;
import com.jfkj.im.TIM.component.LineControllerView;
import com.jfkj.im.TIM.component.SelectionActivity;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.component.dialog.TUIKitDialog;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.component.picture.imageEngine.impl.GlideEngine;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.group.interfaces.IGroupMemberLayout;
import com.jfkj.im.TIM.modules.group.member.GroupMemberInfo;
import com.jfkj.im.TIM.modules.group.member.IGroupMemberRouter;
import com.jfkj.im.TIM.redpack.group.GroupAddOrDelMeberActivity;
import com.jfkj.im.TIM.redpack.group.GroupInfoBean;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.TIM.redpack.group.GroupInforEven;
import com.jfkj.im.TIM.redpack.group.GroupInformationActivity;
import com.jfkj.im.TIM.redpack.group.GroupMemberInfoBean;
import com.jfkj.im.TIM.redpack.group.UpGradeSuperGroupActivity;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AddDeteleMemberEvent;
import com.jfkj.im.event.GroupUnreadMessageNumEvent;
import com.jfkj.im.event.SeventyEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.activity.ClubnameActivity;
import com.jfkj.im.ui.activity.ClubnoticeActivity;
import com.jfkj.im.ui.activity.GroupMemberActivity;
import com.jfkj.im.ui.activity.SelectpacketsActivity;
import com.jfkj.im.ui.activity.SelectpackettimeActivity;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;


public class GroupInfoLayout extends LinearLayout implements IGroupMemberLayout, View.OnClickListener {

    private static final String TAG = GroupInfoLayout.class.getSimpleName();
    private TitleBarLayout mTitleBar;
    private LineControllerView mMemberView;
    //    private GroupInfoAdapter mMemberAdapter;
    private IGroupMemberRouter mMemberPreviewListener;
    private LineControllerView mGroupTypeView;
    private LineControllerView mGroupIDView;
    private LineControllerView mGroupNameView;
    private LineControllerView mGroupIcon;
    private LineControllerView mGroupNotice;
    private LineControllerView mNickView;
    private LineControllerView mJoinTypeView;
    private LineControllerView mTopSwitchView;
    private LineControllerView mGroupJlbName, mGiftDayView, mGroupDate, mGroupSuperClub, mGroupMessageNo, mGroupHideClubMember;
    private Button mDissolveBtn;

    private GroupInfo mGroupInfo;
    private GroupInfoPresenter mPresenter;
    private ArrayList<String> mJoinTypes = new ArrayList<>();
    private String groupId;
    //群组信息
    private GroupInfoBean groupInfoBean;
    private ArrayList<GroupMemberInfoBean> groupMemberInfoBeans;
    private ArrayList<GroupMemberInfoBean> memberInfoBeanArrayList;
    private LoadingDialog loadingDialog;
    private FragmentManager fragment;

    private Runnable runnable;
    private Handler mHandler;
    private CommonRecyclerAdapter<GroupMemberInfoBean> adapter;
    private static final int ADD_TYPE = -100;
    private static final int DEL_TYPE = -101;

    private TextView tv_look_more;

    public GroupInfoLayout(Context context) {
        super(context);
        init();
    }

    public GroupInfoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupInfoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.group_info_layout, this);
//        StatusBarUtil.setStatusBarColor((Activity) getContext(), R.color.white);
        // 标题
        mTitleBar = findViewById(R.id.group_info_title_bar);
        mTitleBar.getRightGroup().setVisibility(GONE);
        mTitleBar.setTitle(getResources().getString(R.string.group_club), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
        // 成员标题
        mMemberView = findViewById(R.id.group_member_bar);
        mMemberView.setOnClickListener(this);
        mMemberView.setCanNav(true);
        // 成员列表
//        GridView memberList = findViewById(R.id.group_members);
//        mMemberAdapter = new GroupInfoAdapter(getContext());
//        memberList.setAdapter(mMemberAdapter);


        // 群类型，只读
        mGroupTypeView = findViewById(R.id.group_type_bar);
        // 群ID，只读
        mGroupIDView = findViewById(R.id.group_account);
        // 群聊名称
        mGroupNameView = findViewById(R.id.group_name);
        mGroupNameView.setOnClickListener(this);
        mGroupNameView.setCanNav(true);
        // 群头像
        mGroupIcon = findViewById(R.id.group_icon);
        mGroupIcon.setOnClickListener(this);
        mGroupIcon.setCanNav(false);
        // 群公告
        mGroupNotice = findViewById(R.id.group_notice);
        mGroupNotice.setOnClickListener(this);
        mGroupNotice.setCanNav(true);
        // 加群方式
        mJoinTypeView = findViewById(R.id.join_type_bar);
        mJoinTypeView.setOnClickListener(this);
        mJoinTypeView.setCanNav(true);
        mJoinTypes.addAll(Arrays.asList(getResources().getStringArray(R.array.group_join_type)));
        // 群昵称
        mNickView = findViewById(R.id.self_nickname_bar);
        mNickView.setOnClickListener(this);
        mNickView.setCanNav(true);
        // 是否置顶
        mTopSwitchView = findViewById(R.id.chat_to_top_switch);
        mTopSwitchView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setTopConversation(isChecked);
            }
        });
        //俱乐部名称
        mGroupJlbName = findViewById(R.id.group_info);
        mGroupJlbName.setOnClickListener(this);
        mGroupJlbName.setCanNav(true);
        //每日礼物数量
        mGiftDayView = findViewById(R.id.group_jlb_day_gift);
        mGiftDayView.setOnClickListener(this);
        mGiftDayView.setEnabled(false);
        //礼物发放时间
        mGroupDate = findViewById(R.id.group_jlb_gift_date);
        mGroupDate.setOnClickListener(this);
        mGroupDate.setCanNav(true);
        //超级俱乐部
        mGroupSuperClub = findViewById(R.id.group_jlb_super_club);
        mGroupSuperClub.setOnClickListener(this);
        mGroupSuperClub.setCanNav(true);
        //消息免打扰
        mGroupMessageNo = findViewById(R.id.group_message_no);

        /**隐藏俱乐部成员*/
        mGroupHideClubMember = findViewById(R.id.group_hide_member);

        String groupid = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.ISDISTURB + Utils.DISTURB);
        String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + groupid + Utils.NODISTURB, "closenodisturb");
        if (!closenodisturb.equals("closenodisturb")) {
//            mSwitchView.setBackgroundResource(R.mipmap.switch_on_green);
            mGroupMessageNo.setChecked(true);
        } else {
            mGroupMessageNo.setChecked(false);
//            mSwitchView.setBackgroundResource(R.mipmap.switch_off_white);
        }

        mGroupMessageNo.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String groupid = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + Utils.ISDISTURB + Utils.DISTURB);
                SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + Utils.ISDISTURB + Utils.DISTURB + groupid, isChecked);
//                mPresenter.setNoMessage(isChecked, new IUIKitCallBack() {
//                    @Override
//                    public void onSuccess(Object data) {
//                        ToastUtils.showShortToast("修改成功");
//                    }
//
//                    @Override
//                    public void onError(String module, int errCode, String errMsg) {
//
//                    }
//                });

                String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + groupid + Utils.NODISTURB, "closenodisturb");
                if (closenodisturb.equals("closenodisturb")) {
//                    buttonView.setBackgroundResource(R.mipmap.switch_on_green);
                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + groupid + Utils.NODISTURB, "opennodisturb");
//开启免打扰模式之后  消息数量去更新
                    EventBus.getDefault().postSticky(new GroupUnreadMessageNumEvent(0, groupid));
                } else {
//                    buttonView.setBackgroundResource(R.mipmap.switch_off_white);
                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + groupid + Utils.NODISTURB, "closenodisturb");
                }
            }
        });

        long vip = Long.parseLong(UserInfoManger.getUserVipLevel());

        if (vip < 70) {
            mGroupHideClubMember.setChecked(false);
        } else {
            mGroupHideClubMember.setChecked(true);
        }

        mGroupHideClubMember.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!buttonView.isPressed()){
                    return;
                }

                if (vip < 70) {
                    EventBus.getDefault().post(new SeventyEvent(true));
                    mGroupHideClubMember.setChecked(false);
                    return;
                }
                mGroupHideClubMember.setChecked(isChecked);
                String hide = isChecked ? "1" : "0";
                GroupInfoSetUtils.setGroupInfoHide(getContext(), groupId, hide, new TIMValueCallBack<Boolean>() {
                    @Override
                    public void onError(int i, String s) {
                        ToastUtil.toastShortMessage(s);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
//                            ToastUtil.toastShortMessage("设置成功");
                        }
                    }
                });
            }
        });


        // 退群
        mDissolveBtn = findViewById(R.id.group_dissolve_button);
        mDissolveBtn.setOnClickListener(this);
        groupMemberInfoBeans = new ArrayList<>();

        mPresenter = new GroupInfoPresenter(this);
        loadingDialog = new LoadingDialog(getContext(), R.style.dialog);

        /**查看更多群成员*/
        tv_look_more = findViewById(R.id.tv_look_more);

        tv_look_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupInforEven groupInforEven = new GroupInforEven(groupMemberInfoBeans, mGroupInfo.isOwner(), groupInfoBean, groupId);
                EventBus.getDefault().postSticky(groupInforEven);
                Intent intent = new Intent(getContext(), GroupMemberActivity.class);
                intent.putExtra("data",groupMemberInfoBeans);
                intent.putExtra("isGroupOwner",mGroupInfo.isOwner());
                intent.putExtra("groupId",groupId);
                getContext().startActivity(intent);
            }
        });

        memberInfoBeanArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        adapter = new CommonRecyclerAdapter<GroupMemberInfoBean>(getContext(), memberInfoBeanArrayList, R.layout.group_member_adpater) {
            @Override
            public void convert(CommonRecyclerHolder holder, GroupMemberInfoBean groupMemberInfo, int position) {
                ImageView memberIcon = holder.getView(R.id.group_member_icon);
                TextView memberName = holder.getView(R.id.group_member_name);
                TextView tv_group_z = holder.getView(R.id.tv_group_z);

                memberName.setText(groupMemberInfo.getNickName());
                if (groupMemberInfo.getIsOwner() == 1) {
                    tv_group_z.setVisibility(View.VISIBLE);
                } else {
                    tv_group_z.setVisibility(View.GONE);
                }
                if (groupMemberInfo.getType() == ADD_TYPE) {
                    memberIcon.setImageResource(R.mipmap.club_icon_groupadd_gray);
                    memberIcon.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContext().startActivity(new Intent(getContext(), GroupAddOrDelMeberActivity.class)
                                    .putExtra("add", true)
                                    .putExtra("groupId", groupId));
                        }
                    });
                } else if (groupMemberInfo.getType() == DEL_TYPE) {
                    memberIcon.setImageResource(R.mipmap.club_icon_groupdelete_gray);
                    memberIcon.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContext().startActivity(new Intent(getContext(), GroupAddOrDelMeberActivity.class)
                                    .putExtra("add", false)
                                    .putExtra("groupId", groupId));
                        }
                    });
                } else {
                    GlideEngine.loadImage(memberIcon, groupMemberInfo.getHead(), null);
                    memberIcon.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), UserDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("userId", groupMemberInfo.getUserId());
                            getContext().startActivity(intent);
                        }
                    });
                }

            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (mGroupInfo == null) {
            TUIKitLog.e(TAG, "mGroupInfo is NULL");
            return;
        }
        if (v.getId() == R.id.group_member_bar) {
            if (mMemberPreviewListener != null) {
                mMemberPreviewListener.forwardListMember(mGroupInfo);
            }
        } else if (v.getId() == R.id.group_name) {
//            Bundle bundle = new Bundle();
//            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.fix_group_info));
//            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mGroupNameView.getContent());
//            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
//            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
//                @Override
//                public void onReturn(final Object text) {
//                    mPresenter.modifyGroupName(text.toString());
//                    mGroupNameView.setContent(text.toString());
//                }
//            });

            GroupInforEven groupInforEven = new GroupInforEven(groupMemberInfoBeans, mGroupInfo.isOwner(), groupInfoBean, groupId);
            EventBus.getDefault().postSticky(groupInforEven);
            Intent intent = new Intent(getContext(), GroupInformationActivity.class);
            intent.putExtra("type", true);
            intent.putExtra(Utils.GROUPID, groupId);
            getContext().startActivity(intent);

        } else if (v.getId() == R.id.group_icon) {
            String groupUrl = String.format("https://picsum.photos/id/%d/200/200", new Random().nextInt(1000));
            TIMGroupManager.ModifyGroupInfoParam param = new TIMGroupManager.ModifyGroupInfoParam(mGroupInfo.getId());
            param.setFaceUrl(groupUrl);
            TIMGroupManager.getInstance().modifyGroupInfo(param, new TIMCallBack() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(TAG, "modify group icon failed, code:" + code + "|desc:" + desc);
                    ToastUtil.toastLongMessage("修改俱乐部头像失败, code = " + code + ", info = " + desc);
                }

                @Override
                public void onSuccess() {
                    ToastUtil.toastLongMessage("修改俱乐部头像成功");
                }
            });

        } else if (v.getId() == R.id.group_notice) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.fix_group_notice_jlb));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mGroupNotice.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 200);
            bundle.putString("type", "group_notice");
//            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
//                @Override
//                public void onReturn(final Object text) {
//                    mPresenter.modifyGroupNotice(text.toString());
//                    mGroupNotice.setContent(text.toString());
//                }
//            });
            ClubnoticeActivity.startTextSelection(getContext(), bundle, new ClubnoticeActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object res) {
                    mPresenter.modifyGroupNotice(res.toString());
                    mGroupNotice.setContent(res.toString());
                    setData(groupId);
                }
            });
        } else if (v.getId() == R.id.self_nickname_bar) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.fix_my_name));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mNickView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(final Object text) {
                    mPresenter.modifyMyGroupNickname(text.toString());
                    mNickView.setContent(text.toString());
                }
            });
        } else if (v.getId() == R.id.join_type_bar) {
            if (mGroupTypeView.getContent().equals("聊天室")) {
                ToastUtil.toastLongMessage("加入聊天室为自动审批，暂不支持修改");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.group_join_type));
            bundle.putStringArrayList(TUIKitConstants.Selection.LIST, mJoinTypes);
            bundle.putInt(TUIKitConstants.Selection.DEFAULT_SELECT_ITEM_INDEX, mGroupInfo.getJoinType());
            SelectionActivity.startListSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(final Object text) {
                    mPresenter.modifyGroupInfo((Integer) text, TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE);
                    mJoinTypeView.setContent(mJoinTypes.get((Integer) text));


                }
            });
        } else if (v.getId() == R.id.group_dissolve_button) {
            if (mGroupInfo.isOwner() && !mGroupInfo.getGroupType().equals("Private")) {
//                new TUIKitDialog(getContext())
//                        .builder()
//                        .setCancelable(true)
//                        .setCancelOutside(true)
//                        .setTitle("您确认解散该群?")
//                        .setDialogWidth(0.75f)
//                        .setPositiveButton("确定", new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mPresenter.deleteGroup();
//                            }
//                        })
//                        .setNegativeButton("取消", new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        })
//                        .show();
                TipsBaseDialogFragment tipsBaseDialogFragment
                        = new TipsBaseDialogFragment(true, Gravity.CENTER, "是否确定解散俱乐部？", "取消", "退出", true);
                tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            mPresenter.deleteGroup();
                        }
                    }
                });
                tipsBaseDialogFragment.show(fragment, "");
            } else {
//                new TUIKitDialog(getContext())
//                        .builder()
//                        .setCancelable(true)
//                        .setCancelOutside(true)
//                        .setTitle("您确认退出该群？")
//                        .setDialogWidth(0.75f)
//                        .setPositiveButton("确定", new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mPresenter.quitGroup();
//                            }
//                        })
//                        .setNegativeButton("取消", new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        })
//                        .show();
                TipsBaseDialogFragment tipsBaseDialogFragment
                        = new TipsBaseDialogFragment(true, Gravity.CENTER, "是否确定退出俱乐部？", "取消", "退出", true);
                tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s) {
                            mPresenter.quitGroup();
                        }
                    }
                });
                tipsBaseDialogFragment.show(fragment, "");
            }
        } else if (v.getId() == R.id.group_info) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.fix_group_jlb_name));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mGroupNameView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            bundle.putString("type", "group_set");
//            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
//                @Override
//                public void onReturn(final Object text) {
//                    mPresenter.modifyGroupName(text.toString());
//                    mGroupNameView.setContent(text.toString());
//                }
//            });
            ClubnameActivity.startTextSelection(getContext(), bundle, mPresenter, new ClubnameActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object res) {
//                    mPresenter.modifyGroupName(res.toString());
                    mGroupNameView.setContent(res.toString());
                    mGroupJlbName.setContent(res.toString());
                    setData(groupId);
                }
            });
        } else if (v.getId() == R.id.group_jlb_day_gift) {
            getContext().startActivity(new Intent(getContext(), SelectpacketsActivity.class)
                    .putExtra("group", true)
                    .putExtra(Utils.GROUPID, groupId));
        } else if (v.getId() == R.id.group_jlb_gift_date) {
            getContext().startActivity(new Intent(getContext(), SelectpackettimeActivity.class)
                    .putExtra("group", true)
                    .putExtra(Utils.GROUPID, groupId));
        } else if (v.getId() == R.id.group_jlb_super_club) {
            Intent intent = new Intent(getContext(), UpGradeSuperGroupActivity.class);
            intent.putExtra(Utils.GROUPID, groupId);
            getContext().startActivity(intent);

        }
    }


    public void setFragment(FragmentManager fragment) {
        this.fragment = fragment;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        mPresenter.loadGroupInfo(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                setGroupInfo((GroupInfo) data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
        //自定义消息
        ////设置自定义消息
        setData(groupId);
    }

    private void setGroupInfo(GroupInfo info) {
        if (info == null) {
            return;
        }
        this.mGroupInfo = info;
        mGroupNameView.setContent(info.getGroupName());
        mGroupJlbName.setContent(info.getGroupName());
        mGroupIDView.setContent(info.getId());
        mGroupNotice.setContent(info.getNotice());
        mMemberView.setContent(info.getMemberCount() + "人");
        mGroupTypeView.setContent(convertGroupText(info.getGroupType()));
        mJoinTypeView.setContent(mJoinTypes.get(info.getJoinType()));
        mNickView.setContent(mPresenter.getNickName());
        mTopSwitchView.setChecked(mGroupInfo.isTopChat());

        mDissolveBtn.setText(R.string.dissolve);
        if (mGroupInfo.isOwner()) {
            mGroupNotice.setVisibility(VISIBLE);
            mJoinTypeView.setVisibility(GONE);
            //俱乐部名称
            mGroupJlbName.setVisibility(VISIBLE);
            //公告
            mGroupNotice.setVisibility(VISIBLE);
            //礼物数量
            mGiftDayView.setCanNav(true);
            mGiftDayView.setEnabled(true);
            //礼物发放时间
            mGroupDate.setEnabled(true);
            mGroupDate.setCanNav(true);
            //超级
            mGroupSuperClub.setVisibility(VISIBLE);
            if (mGroupInfo.getGroupType().equals("Private")) {
                mDissolveBtn.setText(R.string.exit_group);
            }
        } else {
            mGroupNotice.setVisibility(GONE);
            mJoinTypeView.setVisibility(GONE);
            //俱乐部名称
            mGroupJlbName.setVisibility(GONE);
            //公告
            mGroupNotice.setVisibility(GONE);
            //礼物数量
            mGiftDayView.setCanNav(false);
            mGiftDayView.setEnabled(false);
            //礼物发放时间
            mGroupDate.setEnabled(false);
            mGroupDate.setCanNav(false);
            //超级
            mGroupSuperClub.setVisibility(GONE);
            mDissolveBtn.setText(R.string.exit_group);
        }

        setAdapterData(info);
    }


    private void setAdapterData(GroupInfo info) {
        if (loadingDialog != null && !this.loadingDialog.isShowing()) {
            try {
                loadingDialog.show();
            } catch (Exception e) {

            }
        }

        GroupInfoSetUtils.loadGroupMemberList(getContext(),groupId, "200", "10", new IUIKitCallsBack<GroupMemberInfoBean>() {
            @Override
            public void onSuccess(List<GroupMemberInfoBean> data) {
                loadingDialog.dismiss();
                groupMemberInfoBeans.clear();
                groupMemberInfoBeans.addAll(data);
                memberInfoBeanArrayList.clear();

                if (info.isOwner()) {
                    if (groupMemberInfoBeans.size() > 18) {
                        ArrayList<GroupMemberInfoBean> groupMemberInfoBeanResult = new ArrayList<>(groupMemberInfoBeans.subList(0, 18));
                        GroupMemberInfoBean add = new GroupMemberInfoBean();
                        add.setType(ADD_TYPE);
                        GroupMemberInfoBean del = new GroupMemberInfoBean();
                        del.setType(DEL_TYPE);
                        groupMemberInfoBeanResult.add(add);
                        groupMemberInfoBeanResult.add(del);
                        memberInfoBeanArrayList.addAll(groupMemberInfoBeanResult);
                        tv_look_more.setVisibility(VISIBLE);
                    } else {
                        tv_look_more.setVisibility(GONE);
                        GroupMemberInfoBean add = new GroupMemberInfoBean();
                        add.setType(ADD_TYPE);
                        GroupMemberInfoBean del = new GroupMemberInfoBean();
                        del.setType(DEL_TYPE);
                        groupMemberInfoBeans.add(add);
                        groupMemberInfoBeans.add(del);
                        memberInfoBeanArrayList.addAll(groupMemberInfoBeans);
                    }
                } else {
                    if (groupMemberInfoBeans.size() > 20) {
                        ArrayList<GroupMemberInfoBean> groupMemberInfoBeanResult = new ArrayList<>(groupMemberInfoBeans.subList(0, 20));
                        memberInfoBeanArrayList.addAll(groupMemberInfoBeanResult);
                        tv_look_more.setVisibility(VISIBLE);
                    } else {
                        tv_look_more.setVisibility(GONE);
                        memberInfoBeanArrayList.addAll(groupMemberInfoBeans);
                    }
                }




                adapter.notifyDataSetChanged();
//                mMemberAdapter.setDataSource(info, data);
//                mMemberAdapter.setGroupId(groupId);

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                loadingDialog.dismiss();
            }
        });
    }

    //本地信息 mGiftDayView,mGroupDate,mGroupSuperClub,mGroupMessageNo
    private void setData(String id) {
        if (mHandler == null) {
            mHandler = new Handler();
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    loadingDialog.show();
                } catch (Exception e) {

                }

                mPresenter.loadGroupInfo(id, getContext(), new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        loadingDialog.dismiss();
                        if (groupInfoBean != null) {
                            groupInfoBean = null;
                        }
                        groupInfoBean = (GroupInfoBean) data;
                        mGiftDayView.setPicContent("x" + groupInfoBean.getRedPacketNum() + "/人");
                        mGroupDate.setContent("每天" + groupInfoBean.getSendHour() + ":00");
                        if (TextUtils.equals(groupInfoBean.getHide(),"1")){
                            mGroupHideClubMember.setChecked(true);
                        } else {
                            mGroupHideClubMember.setChecked(false);
                        }

                        if (groupInfoBean.getIsOwner() == 1) {
                            mGroupHideClubMember.setVisibility(VISIBLE);
                        }

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        loadingDialog.dismiss();
                    }
                });
            }
        };
        mHandler.postDelayed(runnable, 500);


    }

    private String convertGroupText(String groupType) {
        String groupText = "";
        if (TextUtils.isEmpty(groupType)) {
            return groupText;
        }
        if (TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_PRIVATE)) {
            groupText = "讨论组";
        } else if (TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_PUBLIC)) {
            groupText = "公开群";
        } else if (TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_CHAT_ROOM)) {
            groupText = "聊天室";
        }
        return groupText;
    }

    public void onGroupInfoModified(Object value, int type) {
        switch (type) {
            case TUIKitConstants.Group.MODIFY_GROUP_NAME:
                ToastUtil.toastLongMessage(getResources().getString(R.string.modify_group_name_success));
                mGroupNameView.setContent(value.toString());
                break;
            case TUIKitConstants.Group.MODIFY_GROUP_NOTICE:
                mGroupNotice.setContent(value.toString());
                ToastUtil.toastLongMessage(getResources().getString(R.string.modify_group_notice_success));
                break;
            case TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE:
                mJoinTypeView.setContent(mJoinTypes.get((Integer) value));
                break;
            case TUIKitConstants.Group.MODIFY_MEMBER_NAME:
                ToastUtil.toastLongMessage(getResources().getString(R.string.modify_nickname_success));
                mNickView.setContent(value.toString());
                break;
        }
    }

    public void setRouter(IGroupMemberRouter listener) {
        mMemberPreviewListener = listener;
//        mMemberAdapter.setManagerCallBack(listener);
    }

    @Override
    public void setDataSource(GroupInfo dataSource) {

    }

    @Override
    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

}
