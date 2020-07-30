package com.jfkj.im.TIM.redpack.group;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.ApplyJoinGroupEvent;
import com.jfkj.im.interfaces.ResponListtenerThrees;
import com.jfkj.im.litepal.GroupMessCenterLitepal;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.tencent.imsdk.ext.group.TIMGroupPendencyGetParam;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.group.TIMGroupPendencyListGetSucc;
import com.tencent.imsdk.ext.group.TIMGroupPendencyMeta;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_EIGHT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_NINE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TEN;

public class GroupMessageCenterActivity extends BaseActivity {

    @BindView(R.id.recycler)
    SwipeRecyclerView recycler;
    @BindView(R.id.constraint_head)
    ConstraintLayout constraint_head;

    @BindView(R.id.iv_img_no_news)
    ImageView ivImgNoNews;

    private List<RedPackageCustom> messages = new ArrayList<>();
    private GroupMessCenterAdapter adapter;
    private Map<Integer, List<RedPackageCustom>> map = new HashMap<>();

    //获取自己的信息
    private String selfName;
    private String selfUrlHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this, false);
        setStaturBar(constraint_head);
        int num = SPUtils.getInstance(getApplicationContext()).getInt("numCenter");
        SPUtils.getInstance(getApplicationContext()).put(Utils.GROUP_CENTER_MESSAGE_ISSHOW, false);
        getSlef();

        for (int i = 0; i <= num; i++) {
            map.put(i, JSON.parseArray(SPUtils.getInstance(getApplicationContext()).getString("data" + i), RedPackageCustom.class));
        }

        //     EventBus.getDefault().postSticky(new ApplyJoinGroupEvent(false));

        SPUtils.getInstance(App.getAppContext()).put(UserInfoManger.getUserId()+Utils.UN_READ_NUM,-1L);


        recycler.setLayoutManager(new LinearLayoutManager(this));


        adapter = new GroupMessCenterAdapter(messages, getSupportFragmentManager());
        recycler.setAdapter(adapter);
        adapter.setResponListeners(new ResponListtenerThrees<Integer, String, String, String>() {
            @Override
            public void Rsps(Integer data, String view, String content, String id, int position) {
                switch (data) {
                    case 0://拒绝
                        refuse(view, content, id, position);

                        break;
                    case 1://同意
//                        agreeEntry(view);
                        accept(view, content, id, position);

                        break;
                }
            }
        });

        //获取本地消息
        getLocalMessage();
        //获取未决绝信息
        getMessage();



    }

    private void getLocalMessage() {
        List<GroupMessCenterLitepal> groupMessCenterLitepals = LitePal.findAll(GroupMessCenterLitepal.class);
        for (GroupMessCenterLitepal groupMessCenterLitepal : groupMessCenterLitepals) {
            if (TextUtils.equals(groupMessCenterLitepal.getUserId(),UserInfoManger.getUserId())){
                RedPackageCustom redPackageCustom = new RedPackageCustom();
                redPackageCustom.setAvatarUrl(groupMessCenterLitepal.getUserHeadUrl());
                redPackageCustom.setSendId(groupMessCenterLitepal.getUserId());
                redPackageCustom.setSendName(groupMessCenterLitepal.getUserName());
                redPackageCustom.setSendContent(groupMessCenterLitepal.getApplyStr());
                redPackageCustom.setIntHandledStatus(Integer.parseInt(groupMessCenterLitepal.getState()));
                redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
                redPackageCustom.setGroupName(groupMessCenterLitepal.getGroupName());
                redPackageCustom.setGroupId(groupMessCenterLitepal.getGroupId());
                redPackageCustom.setReceiveContent(groupMessCenterLitepal.getRefuseStr());
                redPackageCustom.setReceiveName(groupMessCenterLitepal.getHandlerName());
                messages.add(redPackageCustom);
                adapter.notifyDataSetChanged();
            }
        }
    }


    public void processMessage() {
        String gSize = SPUtils.getInstance(App.getAppContext()).getString(UserInfoManger.getUserId() + "GROUP_JOIN_USER_PROCESSING", "");
        if (!gSize.equals("")) {

            SPUtils.getInstance(App.getAppContext()).put(UserInfoManger.getUserId() + "GROUP_JOIN_USER_PROCESSING", "" + (Integer.parseInt(gSize) - 1));

            if (gSize.equals("1")) {
                EventBus.getDefault().postSticky(new ApplyJoinGroupEvent(false));
            }
        }


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_message_center;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.back_iv})
    void Onclick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }


    private void getSlef() {
        TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().querySelfProfile();
        if (timUserProfile == null) {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    selfName = timUserProfile.getNickName();
                    selfUrlHead = timUserProfile.getFaceUrl();
                }
            });
        } else {
            selfName = timUserProfile.getNickName();
            selfUrlHead = timUserProfile.getFaceUrl();
        }
    }

    private void setAdapter() {
        if (map == null) {
            return;
        }
        if (map.size() > 0) {
            for (Map.Entry<Integer, List<RedPackageCustom>> entry : map.entrySet()) {
                messages.addAll(entry.getValue());
            }
        }

        //没有消息时
        if (messages.size() > 0) {
            recycler.setVisibility(View.VISIBLE);
            ivImgNoNews.setVisibility(View.GONE);
        } else {
            recycler.setVisibility(View.GONE);
            ivImgNoNews.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();


    }

    private void accept(String view, String content, String id, int position) {
        progressDialog.show();
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setTimestamp(0);//首次获取填 0
        param.setNumPerPage(10);

        /**
         * 获取群组未处理请求列表
         *
         * <p>1. 群未处理请求泛指所有需要审批的群相关的操作（例如：加群待审批，拉人入群待审批等等）。即便审核通过或者拒绝后，该条信息也可通过此接口拉回，拉回的信息中有已处理标志
         * <br>2. 审批人：有权限拉取相关信息,如果 UserA 申请加入群 GroupA，则群管理员可获取此未处理相关信息，UserA 因为没有审批权限，不需要拉取未处理信息。如果 AdminA 拉 UserA 进去 GroupA，则 UserA 可以拉取此未处理相关信息，因为该未处理信息待 UserA 审批。
         *
         * @param param 获取群未决请求列表参数类，详见{@link TIMGroupPendencyGetParam}
         * @param cb    回调，在onSuccess的参数中返回群未决的列表及元数据，详见{@link com.tencent.imsdk.ext.group.TIMGroupPendencyItem}
         */

        TIMGroupManager.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int code, String desc) {
                toastShow(desc);
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                progressDialog.dismiss();
                //meta中的nextStartTimestamp如果不为 0,可以先保存起来，
                // 作为获取下一页数据的参数设置到 TIMGroupPendencyGetParam 中
                TIMGroupPendencyMeta meta = timGroupPendencyListGetSucc.getPendencyMeta();
                Log.e("msg", meta.getNextStartTimestamp()
                        + "|" + meta.getReportedTimestamp() + "|" + meta.getUnReadCount());

                List<TIMGroupPendencyItem> pendencyItems = timGroupPendencyListGetSucc.getPendencies();
                for (TIMGroupPendencyItem item : pendencyItems) {
                    //对群未决进行相应操作，例如查看/通过/拒绝等
                    if (TextUtils.equals(item.getGroupId(), view)) {
                        item.accept(content, new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                Log.i("msg", "....accept..code...." + code + ",,,,,,,," + desc);
                            }

                            @Override
                            public void onSuccess() {
                                SPUtils.getInstance(mActivity).put(Utils.APPID+Utils.CHAT_GROUP_OLD_DAY, "");
                                processMessage();
                                toastShow("已同意");
//                                if (messages != null){
//                                    messages.clear();
//                                }

                                messages.get(position).setIntHandledStatus(2);
                                messages.get(position).setIntOperationType(1);
                                messages.get(position).setIntPendencyType(0);
//                                adapter.notifyDataSetChanged();
                                messages.get(position).setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
                                adapter.notifyItemChanged(position);

                                //  getMessage();
                            }
                        });
                    }
                }
            }
        });
    }

    private void refuse(String id, String content, String fromId, int position) {
        progressDialog.show();
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setTimestamp(0);//首次获取填 0
        param.setNumPerPage(10);

        TIMGroupManager.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int code, String desc) {
                toastShow(desc);
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                progressDialog.dismiss();
                //meta中的nextStartTimestamp如果不为 0,可以先保存起来，
                // 作为获取下一页数据的参数设置到 TIMGroupPendencyGetParam 中
                TIMGroupPendencyMeta meta = timGroupPendencyListGetSucc.getPendencyMeta();
                Log.e("msg", meta.getNextStartTimestamp()
                        + "|" + meta.getReportedTimestamp() + "|" + meta.getUnReadCount());

                List<TIMGroupPendencyItem> pendencyItems = timGroupPendencyListGetSucc.getPendencies();
                for (TIMGroupPendencyItem item : pendencyItems) {
                    //对群未决进行相应操作，例如查看/通过/拒绝等
                    Log.i("msg", ".....fromId........." + fromId);
                    Log.i("msg", ".....TIMGroupPendencyItem........." + item.getFromUser());
                    if (TextUtils.equals(item.getGroupId(), id)) {
                        if (TextUtils.equals(item.getFromUser(), fromId)) {
                            item.refuse(content, new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    Log.e("msg", "...refuse...code...." + code + ",,,,,,,," + desc);
                                }

                                @Override
                                public void onSuccess() {
                                    processMessage();
                                    toastShow("已拒绝");
//                                    if (messages != null){
//                                        messages.clear();
//                                    }
                                    messages.get(position).setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
                                    messages.get(position).setIntHandledStatus(2);
                                    messages.get(position).setIntOperationType(0);
                                    messages.get(position).setIntPendencyType(0);

                                    adapter.notifyItemChanged(position);
//                                    adapter.notifyDataSetChanged();

                                    //   getMessage();
//                                finish();
                                }
                            });
                            return;
                        }
                    }
                }
            }
        });
    }


    private void agreeEntry(String groupId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mActivity)
                    .url(ApiStores.base_url + "/group/handlerInviteGroup")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                toastShow(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
//                                    refuse(groupId, "agree","");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            toastShow(R.string.nonetwork);
        }
    }

    private void getMessage() {
        /**
         * 获取群组未处理请求列表
         *
         * <p>1. 群未处理请求泛指所有需要审批的群相关的操作（例如：加群待审批，拉人入群待审批等等）。即便审核通过或者拒绝后，该条信息也可通过此接口拉回，拉回的信息中有已处理标志
         * <br>2. 审批人：有权限拉取相关信息,如果 UserA 申请加入群 GroupA，则群管理员可获取此未处理相关信息，UserA 因为没有审批权限，不需要拉取未处理信息。如果 AdminA 拉 UserA 进去 GroupA，则 UserA 可以拉取此未处理相关信息，因为该未处理信息待 UserA 审批。
         *
         * @param param 获取群未决请求列表参数类，详见{@link TIMGroupPendencyGetParam}
         * @param cb    回调，在onSuccess的参数中返回群未决的列表及元数据，详见{@link com.tencent.imsdk.ext.group.TIMGroupPendencyItem}
         */
        TIMGroupPendencyGetParam pendencyGetParam = new TIMGroupPendencyGetParam();
        pendencyGetParam.setTimestamp(0);
        pendencyGetParam.setNumPerPage(100);
        TIMGroupManager.getInstance().getGroupPendencyList(pendencyGetParam, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                //meta中的nextStartTimestamp如果不为 0,可以先保存起来，
                // 作为获取下一页数据的参数设置到 TIMGroupPendencyGetParam 中
                TIMGroupPendencyMeta meta = timGroupPendencyListGetSucc.getPendencyMeta();
                Log.e("msg", meta.getNextStartTimestamp()
                        + "|" + meta.getReportedTimestamp() + "|" + meta.getUnReadCount());

                List<TIMGroupPendencyItem> pendencyItems = timGroupPendencyListGetSucc.getPendencies();
                if (pendencyItems.size() > 0) {
                    for (TIMGroupPendencyItem item : pendencyItems) {
                        //对群未决进行相应操作，例如查看/通过/拒绝等
                        getOther(item);
                    }
                } else {
                    setAdapter();
                }
            }
        });
    }

    private void getOther(TIMGroupPendencyItem item) {

        FriendsUtils.getUsersProfileLoaclService(item.getFromUser(), new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfileFrom) {
                FriendsUtils.getUsersProfileLoaclService(item.getToUser(), new TIMValueCallBack<TIMUserProfile>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(TIMUserProfile timUserProfileTo) {
                        getGroupInfo(timUserProfileFrom,timUserProfileTo, item);
                    }
                });
            }
        });
    }

    private void getGroupInfo(TIMUserProfile timUserProfileFrom, TIMUserProfile timUserProfileTo,TIMGroupPendencyItem item) {
        /**
         * 获取本地存储的群组信息
         * @param groupId 需要拉取详细信息的群组 ID
         * @return 群组信息，本地没有返回 null
         */
        TIMGroupDetailInfo groupDetailInfo = TIMGroupManager.getInstance().queryGroupInfo(item.getGroupId());
        if (groupDetailInfo == null) {
            List<String> ids = new ArrayList<>();
            ids.add(item.getGroupId());
            TIMGroupManager.getInstance().getGroupInfo(ids, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                    for (TIMGroupDetailInfoResult result : timGroupDetailInfoResults) {
                        RedPackageCustom redPackageCustom = new RedPackageCustom();
                        redPackageCustom.setReceiveHeadUrl(timUserProfileFrom.getFaceUrl());
//                        redPackageCustom.setReceiveId(timUserProfile.getIdentifier());
                        redPackageCustom.setReceiveName(timUserProfileFrom.getNickName());
                        redPackageCustom.setReceiveContent(item.getHandledMsg());
                        redPackageCustom.setReceive(true);
                        redPackageCustom.setIntHandledStatus(item.getHandledStatus().getValue());
                        redPackageCustom.setIntOperationType(item.getOperationType().getValue());
                        redPackageCustom.setIntPendencyType(item.getPendencyType().getValue());
                        if (2 == item.getHandledStatus().getValue()
                                && 1 == item.getOperationType().getValue()
                                && 0 == item.getPendencyType().getValue()) {
                            redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
                        } else if (2 == item.getHandledStatus().getValue()
                                && 0 == item.getOperationType().getValue()
                                && 0 == item.getPendencyType().getValue()) {
                            redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
                        } else if (0 == item.getHandledStatus().getValue()
                                && 0 == item.getOperationType().getValue()
                                && 0 == item.getPendencyType().getValue()) {
                            redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_NINE);
                        }
                        redPackageCustom.setGroupName(groupDetailInfo.getGroupName());
                        redPackageCustom.setGroupId(groupDetailInfo.getGroupId());
                        redPackageCustom.setSendId(item.getFromUser());
                        redPackageCustom.setSendName(timUserProfileTo.getNickName());
                        messages.add(redPackageCustom);
                    }
                }
            });
        } else {
            RedPackageCustom redPackageCustom = new RedPackageCustom();
            redPackageCustom.setIntHandledStatus(item.getHandledStatus().getValue());
            redPackageCustom.setIntOperationType(item.getOperationType().getValue());
            redPackageCustom.setIntPendencyType(item.getPendencyType().getValue());
            if (2 == item.getHandledStatus().getValue()
                    && 1 == item.getOperationType().getValue()
                    && 0 == item.getPendencyType().getValue()) {
                redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
            } else if (2 == item.getHandledStatus().getValue()
                    && 0 == item.getOperationType().getValue()
                    && 0 == item.getPendencyType().getValue()) {
                redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_EIGHT);
            } else if (0 == item.getHandledStatus().getValue()
                    && 0 == item.getOperationType().getValue()
                    && 0 == item.getPendencyType().getValue()) {
                redPackageCustom.setCusType(READ_PACKAGE_CUS_TYPE_NINE);
            }
            redPackageCustom.setSendContent(item.getRequestMsg());
            redPackageCustom.setReceiveHeadUrl(timUserProfileFrom.getFaceUrl());
//            redPackageCustom.setReceiveId(timUserProfile.getIdentifier());
            redPackageCustom.setReceiveName(timUserProfileFrom.getNickName());
            redPackageCustom.setReceiveContent(item.getHandledMsg());
            redPackageCustom.setReceive(true);
            redPackageCustom.setGroupName(groupDetailInfo.getGroupName());
            redPackageCustom.setGroupId(groupDetailInfo.getGroupId());
            redPackageCustom.setSendId(item.getFromUser());
            redPackageCustom.setSendName(timUserProfileTo.getNickName());
            messages.add(redPackageCustom);
        }
        SPUtils.getInstance(getApplicationContext()).put(Utils.GROUP_CENTER_MESSAGE, messages.size());
        setAdapter();
    }


}
