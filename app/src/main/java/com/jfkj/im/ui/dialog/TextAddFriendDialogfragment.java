package com.jfkj.im.ui.dialog;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.interfaces.ResponListener;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMFriendStatus;

public class TextAddFriendDialogfragment extends BaseDialogFragment implements View.OnClickListener {

    private EditText user_id, add_wording;
    private ResponListener<String> responListener;

    public void setResponListener (ResponListener<String> responListener){
        this.responListener = responListener;
    }

    public TextAddFriendDialogfragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_add_friend_text,null);
        user_id = view.findViewById(R.id.user_id);
        add_wording = view.findViewById(R.id.add_wording);
        view.findViewById(R.id.add).setOnClickListener(this);
    }

    private void add() {
        TIMFriendRequest timFriendRequest = new TIMFriendRequest(user_id.getText().toString().trim());
        timFriendRequest.setAddWording(add_wording.getText().toString().trim());
        timFriendRequest.setAddSource("android");
        TIMFriendshipManager.getInstance().addFriend(timFriendRequest, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                Log.e("TAG", "addFriend err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                Log.e("TAG", "addFriend success result = " + timFriendResult.toString());
                switch (timFriendResult.getResultCode()) {
                    case TIMFriendStatus.TIM_FRIEND_STATUS_SUCC:
                        ToastUtil.toastShortMessage("成功");
                        break;
                    case TIMFriendStatus.TIM_FRIEND_PARAM_INVALID:
                        if (TextUtils.equals(timFriendResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
                            ToastUtil.toastShortMessage("对方已是您的好友");
                            break;
                        }
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_SELF_FRIEND_FULL:
                        ToastUtil.toastShortMessage("您的好友数已达系统上限");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_THEIR_FRIEND_FULL:
                        ToastUtil.toastShortMessage("对方的好友数已达系统上限");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                        ToastUtil.toastShortMessage("被加好友在自己的黑名单中");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                        ToastUtil.toastShortMessage("对方已禁止加好友");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                        ToastUtil.toastShortMessage("您已被被对方设置为黑名单");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_PENDING:
                        ToastUtil.toastShortMessage("等待好友审核同意");
                        break;
                    default:
                        ToastUtil.toastLongMessage(timFriendResult.getResultCode() + " * " + timFriendResult.getResultInfo());
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                add();
                responListener.Rsp(user_id.getText().toString().trim());
                dismiss();
                break;
        }
    }
}
