package com.jfkj.im.TIM.redpack.group;

import android.view.Gravity;
import android.view.View;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.RedPackageUtil;
import com.jfkj.im.TIM.redpack.SendRedPackageBean;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.dialog.BaseDialogFragment;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.TipNoBaseDialogFragment;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class SubmitGroupEverydayTaskDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private TIMConversation conversation;
    private ChatInfo chatInfo;
    private String taskId;
    private LoadingDialog loadingDialog;
    private ResponListener<Boolean> responListener;

    public void Rsp(ResponListener<Boolean> responListener) {
        this.responListener = responListener;
    }

    public SubmitGroupEverydayTaskDialogFragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_subm_task,null);
        view.findViewById(R.id.tv_open_album).setOnClickListener(this);
        view.findViewById(R.id.tv_send_red_pack).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);

        if (getArguments() != null) {
            chatInfo = (ChatInfo) getArguments().getSerializable("chat");
        }
        if (getArguments() != null) {
            taskId = getArguments().getString("taskId");
        }
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatInfo.getId());
        loadingDialog = new LoadingDialog(getActivity(),R.style.dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_open_album:
                responListener.Rsp(true);
                dismiss();
                break;
            case R.id.tv_send_red_pack:
                choiceSendRedpackage();
                break;

        }
    }

    private void choiceSendRedpackage() {
        TipNoBaseDialogFragment dialogFragment = new TipNoBaseDialogFragment(true,
                Gravity.CENTER,"发1000钻石红包在群里，才能代替完成日常任务。","取消","确认发红包",false);
        dialogFragment.setResponListener(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s){
                    loadingDialog.show();
                    new RedPackageUtil().taskGroupEverydayRedpackage(chatInfo.getId(), taskId, new TIMValueCallBack<SendRedPackageBean>() {
                        @Override
                        public void onError(int code, String desc) {
                            loadingDialog.dismiss();
                            dismiss();
                        }

                        @Override
                        public void onSuccess(SendRedPackageBean sendRedPackageBean) {
                            loadingDialog.dismiss();
                            dismiss();
                        }
                    }, new TIMValueCallBack<Boolean>() {
                        @Override
                        public void onError(int code, String desc) {
                            dismiss();
                            loadingDialog.dismiss();
                        }

                        @Override

                        public void onSuccess(Boolean aBoolean) {
                            loadingDialog.dismiss();
                            dismiss();
                            if (aBoolean){
                                PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                                        "您的余额不足，请及时充值！", "立即充值",true);
                                dialog.setRsp(new ResponListener<Boolean>() {
                                    @Override
                                    public void Rsp(Boolean s) {
                                        if (s){
                                            ChargeDialog mChargeDialog = new ChargeDialog(Objects.requireNonNull(getContext()),getActivity());
                                            mChargeDialog.show();
                                        }
                                    }
                                });
                                dialog.show(getChildFragmentManager(), "");
                            }

                        }
                    });
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(),"");
    }
}
