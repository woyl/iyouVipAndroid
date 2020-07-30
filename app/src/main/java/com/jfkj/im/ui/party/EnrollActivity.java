package com.jfkj.im.ui.party;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.party.EnrollPresenter;
import com.jfkj.im.mvp.party.EnrollView;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.utils.GsonUtil;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;


/**
 * 報名參賽
 */
public class EnrollActivity extends BaseActivity<EnrollPresenter> implements OnItemSelectedListener, EnrollView {

    @BindView(R.id.iv_title_back)
    AppCompatImageView ivTitleBack;


    @BindView(R.id.tv_title)
    TextView tvTile;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;



    private EnrollPresenter enrollPresenter ;




    private String partyId;
    private String title,nickName;
    private String cadddate;
    private String place;
    private WheelView wheelView;

    private List<String> list = new ArrayList<>();
    private BottomSheetDialog dialog;


    String selectDate = "";
    private CommonDialog exitDialog;
    private String user_id;
    private String detailsphoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
      //  Glide.with(this).load(R.drawable.iv_back_black).into(ivTitleBack);

        list.add("我会提前30分钟到");
        list.add("我会准时到");
        list.add("我会晚到30分钟");

        enrollPresenter = new EnrollPresenter(this);

        initDate();

    }

    private void initDate() {

        Bundle mBundle = getIntent().getExtras();
        partyId = mBundle.getString("partyId","");
        title = mBundle.getString("title","");
        cadddate = mBundle.getString("cadddate","");
        place = mBundle.getString("place","");
        user_id = mBundle.getString("user_id", "");
        detailsphoto = mBundle.getString("detailsphoto", "");
        nickName = mBundle.getString("nick_name","");


        tvTile.setText(title);
        tvAddress.setText(place);
        tvTime.setText(cadddate);


    }


    private void showExitDialog() {
        exitDialog = new CommonDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        exitDialog.dismiss();
                        break;
                    case R.id.tv_confirm:

                        if(!"".equals(selectDate)){
                            enrollPresenter.joinParty(selectDate,partyId);
                            exitDialog.dismiss();
                        }
                        break;
                }
            }
        });
        //
        String htmlStr = "<font>出现放鸽子，迟到等严重失信行为的用户将会遭到</font>" + "<font color=\"#F04F70\" >" + "永久封号"  + "</font>" + "<font>处理</font>";
        exitDialog.setTitleText("温馨提示").setContentText(htmlStr).show();
    }



    private void showButtomDialog(){
        dialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_select_time,null);
        dialog.setContentView(view);


        view.findViewById(R.id.tv_cancel).setOnClickListener(this::onClick);
        view.findViewById(R.id.tv_submit).setOnClickListener(this::onClick);

        wheelView = view.findViewById(R.id.wheelView);
        wheelView.setCyclic(false);
        wheelView.setAdapter(new ArrayWheelAdapter(list));
        wheelView.setOnItemSelectedListener(this);


        dialog.show();
    }


    /**
     *      R.id.tv_cancel,R.id.tv_submit
     * @param view
     */
    @OnClick({R.id.tv_enroll,R.id.rl_select_time,R.id.iv_title_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_enroll:


                if(!"".equals(selectDate)){
                    showExitDialog();
                }

                break;
            case R.id.rl_select_time:
                selectDate  = "我会提前30分钟到";
                showButtomDialog();
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                dialog.cancel();
                break;
            case R.id.tv_submit:
                dialog.dismiss();
                dialog.cancel();
                tvSelectTime.setTextColor(ContextCompat.getColor(mActivity,R.color.white));
                tvSelectTime.setText(selectDate);
                break;
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_enroll;
    }

    @Override
    public EnrollPresenter createPresenter() {
        return enrollPresenter;
    }

    @Override
    public void onItemSelected(int index) {
        selectDate = list.get(index);
    }

    @Override
    public void joinPartySuccess(BaseBean baseBean) {
            ToastUtils.showShortToast(baseBean.getMessage());
            //确认报名成功
            if(baseBean.getCode().equals("200")){
                    MessageInfo messageInfo = buildSystemMessageInfo();

                    TIMManager.getInstance().getConversation(TIMConversationType.C2C,user_id).sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int code, String desc) {
                            OkLogger.e(code + ""  + desc);
                        }
                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            OkLogger.e("" + timMessage);
                            setResult(10);
                            Intent intent = new Intent(mActivity, ChatActivity.class);
                            ChatInfo chatInfo = new ChatInfo();
                            chatInfo.setType(TIMConversationType.C2C);
                            chatInfo.setId(user_id);
                            chatInfo.setChatName(nickName);
                            intent.putExtra(Constants.CHAT_INFO, chatInfo);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
            }
    }

    //这里构建系统自定义消息
    public  MessageInfo buildSystemMessageInfo() {
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage TIMMsg = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();

        JSONObject object = new JSONObject();

        try {
            object.put("sendType","1");
            object.put("type","12");
            object.put("partyId",partyId);
            object.put("receiverId",user_id);
            object.put("joinTime",selectDate);
            object.put("senderId", UserInfoManger.getUserId());
            object.put("senderId", UserInfoManger.getUserId());
            object.put("pagePhoto", detailsphoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ele.setData(object.toString().getBytes());
        messageInfo.setMsgTime(System.currentTimeMillis() / 1000);
        TIMMsg.addElement(ele);
        messageInfo.setElement(ele);
        messageInfo.setSelf(true);
        messageInfo.setTIMMessage(TIMMsg);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        return messageInfo;
    }

    @Override
    public void joinPartyError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
