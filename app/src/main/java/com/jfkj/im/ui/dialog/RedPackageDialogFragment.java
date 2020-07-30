package com.jfkj.im.ui.dialog;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.redpack.DismantleRedPackageBean;
import com.jfkj.im.TIM.redpack.TestingBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.JumpRedPackageDetailsEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class RedPackageDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private ResponListener<Boolean> responListener;
    private DismantleRedPackageBean bean;
    private TestingBean testingBean;
    private Context mContext;
    private String state, code;
    private String redId, content, headUrl, nickName, mChatInfoId, redType;
    private LoadingDialog loadingDialog;
    private LinearLayout ll_details;
    private String type;

    public void setResponListener(ResponListener<Boolean> responListener) {
        this.responListener = responListener;
    }

    public RedPackageDialogFragment(boolean isWidth, int ori, Context context) {
        super(isWidth, ori);
        this.mContext = context;
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_red_package, null);
        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog);
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
        OkLogger.e(content);


        type = getArguments().getString("type");
        testingBean = getArguments().getParcelable("TestingBean");
        code = getArguments().getString("code");
        redType = getArguments().getString("redType");

        ConstraintLayout constraint_head = view.findViewById(R.id.constraint_head);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) constraint_head.getLayoutParams();
        layoutParams.width = (int) (ScreenUtil.getScreenWidth() * 0.8);
        layoutParams.height = (int) (ScreenUtil.getScreenHeight() * 0.5);

        ImageView rounded_img = view.findViewById(R.id.rounded_img);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_content = view.findViewById(R.id.tv_content);
        ImageView img_open = view.findViewById(R.id.img_open);
        TextView tv_details = view.findViewById(R.id.tv_details);
        Glide.with(mContext).load(headUrl).into(rounded_img);
        tv_name.setText(nickName);
        ll_details = view.findViewById(R.id.ll_details);
        if (code.equals("200")) {
            if (redType.equals("2")) {
                tv_details.setText("手气最佳用户需做指定任务");
                tv_details.setVisibility(View.VISIBLE);
            } else {
                tv_details.setVisibility(View.INVISIBLE);
            }
            img_open.setVisibility(View.VISIBLE);
            tv_content.setText(content);
         //   tv_content.setText("冒险游戏");
        } else if (code.equals("60015")) {
            tv_content.setText(state);
            img_open.setVisibility(View.INVISIBLE);
            tv_details.setVisibility(View.VISIBLE);
//            tv_details.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//            tv_details.getPaint().setAntiAlias(true);//抗锯齿
            // 使用此方式设置下滑线为了适配华为手机有双下划线
//            SpannableString spannableString = new SpannableString("查看领取情况 >");
//            UnderlineSpan underlineSpan = new UnderlineSpan();
//            spannableString.setSpan(underlineSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_details.setText("查看领取情况 >");
        } else {
            tv_content.setText(state);
            img_open.setVisibility(View.INVISIBLE);
            tv_details.setVisibility(View.VISIBLE);
//            SpannableString spannableString = new SpannableString("查看领取情况 >");
//            UnderlineSpan underlineSpan = new UnderlineSpan();
//            spannableString.setSpan(underlineSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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
                EventBus.getDefault().post(new JumpRedPackageDetailsEvent(redId, mChatInfoId, state));
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
        map.put(Utils.SERIALNO, testingBean.getSerialNo());
        map.put("type",type);
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/group/receiveRedPacket")
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
                        DismantleRedPackageBean dismantleRedPackageBean = JSON.parseObject(jsonObject.getString("data"), DismantleRedPackageBean.class);
                        EventBus.getDefault().post(new JumpRedPackageDetailsEvent(redId, mChatInfoId, state));
                        SPUtils.getInstance(getActivity()).put(Utils.APPID + redId, RED_PAGE_TYPE_CODE_FOUR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
