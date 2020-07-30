package com.jfkj.im.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.DismantleRedPackageBean;
import com.jfkj.im.TIM.redpack.TestingBean;
import com.jfkj.im.TIM.redpack.ice.ReceiveRedpacketIceBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.JumpRedPackageDetailsEvent;
import com.jfkj.im.event.UpdateMessageEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ELEVENT;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_CUS_TYPE_TWELVE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class RedPackageIceDialogFragment  extends BaseDialogFragment implements View.OnClickListener {

    private ResponListener<Boolean> responListener;
    private Context mContext;
    private String state,code;
    private String redId, content, headUrl, nickName, mChatInfoId,redType;
    private LoadingDialog loadingDialog;
    private TIMConversation conversation;

    public void setResponListener(ResponListener<Boolean> responListener) {
        this.responListener = responListener;
    }

    public RedPackageIceDialogFragment(boolean isWidth, int ori, Context context) {
        super(isWidth, ori);
        this.mContext = context;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_red_package, null);
        loadingDialog = new LoadingDialog(getActivity(),R.style.dialog);
        assert getArguments() != null;
        state = getArguments().getString("state");
        nickName = getArguments().getString("nickName");
        headUrl = getArguments().getString("headUrl");
        mChatInfoId = getArguments().getString("id");
        /**
         * 红包状态 1 正在领取 2 已领取完 3过期退款
         * */
        redId = getArguments().getString("redPackId");
        content = getArguments().getString("content");
        code= getArguments().getString("code");

        ConstraintLayout ll_1 = view.findViewById(R.id.constraint_head);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ll_1.getLayoutParams();
        layoutParams.width = (int) (ScreenUtil.getScreenWidth(Objects.requireNonNull(getActivity()))*0.8);

        ImageView rounded_img = view.findViewById(R.id.rounded_img);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_content = view.findViewById(R.id.tv_content);
        ImageView img_open = view.findViewById(R.id.img_open);
        TextView tv_details = view.findViewById(R.id.tv_details);
        Glide.with(mContext).load(headUrl).into(rounded_img);
        tv_name.setText(nickName);
        tv_content.setText("谢谢你完成我的任务");
        if (code.equals("200")) {
            img_open.setVisibility(View.VISIBLE);
            tv_details.setVisibility(View.VISIBLE);
            tv_details.setText("点击领取");

        }else if (code.equals("60015")){
            img_open.setVisibility(View.INVISIBLE);
            tv_details.setVisibility(View.VISIBLE);
        } else  {
            img_open.setVisibility(View.INVISIBLE);
            tv_details.setVisibility(View.VISIBLE);
            tv_details.setText("查看领取情况 >");
        }

        view.findViewById(R.id.img_cancel).setOnClickListener(this);
        tv_details.setOnClickListener(this);
        img_open.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                dismiss();
                break;
            case R.id.tv_details:
                EventBus.getDefault().post(new JumpRedPackageDetailsEvent(redId,mChatInfoId,state));
                dismiss();
                break;
            case R.id.img_open:
                chaiRedPackage();
                break;
        }
    }

    /**
     * 拆红包
     */
    private void chaiRedPackage() {
        dismiss();
        loadingDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.REDID, redId);
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/task/receiveTaskRedPacket")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(getActivity()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("200")) {
                        ReceiveRedpacketIceBean receiveRedpacketIceBean = JSON.parseObject(jsonObject.getString("data"),ReceiveRedpacketIceBean.class);
                        sendMessage(receiveRedpacketIceBean);
                        EventBus.getDefault().post(new JumpRedPackageDetailsEvent(redId,mChatInfoId,state));
                        SPUtils.getInstance(getActivity()).put(Utils.APPID+redId,RED_PAGE_TYPE_CODE_FOUR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage(ReceiveRedpacketIceBean receiveRedpacketIceBean) {
        Log.e("msg","...........slef.............."+TIMManager.getInstance().getLoginUser());
        Log.e("msg","........................." + receiveRedpacketIceBean.getReceivedId());
        Log.e("msg","........................."+receiveRedpacketIceBean.getSendId());
        Log.e("msg","............................."+mChatInfoId);
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mChatInfoId);
        JSONObject jsonObject = new JSONObject();
        try {
//          jsonObject.put(Utils.SENDID,TIMManager.getInstance().getLoginUser());
            jsonObject.put("sendType",READ_PACKAGE_MSG_TYPE_ONE);
            jsonObject.put("sendId",TIMManager.getInstance().getLoginUser());
            jsonObject.put("type",READ_PACKAGE_CUS_TYPE_ELEVENT);
            jsonObject.put("sendName", receiveRedpacketIceBean.getSendName());
            jsonObject.put("receivedId", receiveRedpacketIceBean.getReceivedId());
            jsonObject.put("receiveName", receiveRedpacketIceBean.getReceiveName());
            jsonObject.put("redId",redId);
            MessageInfo messageInfo = MessageInfoUtil.buildRedPackCustomMessage(jsonObject.toString());
            conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e("msg", ",,,,,,code,,,,,,,,," + code);
                    Log.e("msg", ",,,,,,desc,,,,,,,,," + desc);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    Log.e("msg", ",,,,,,,,timMessage,,,,,,," + timMessage.getSender());
                    EventBus.getDefault().post(new UpdateMessageEvent(true));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
