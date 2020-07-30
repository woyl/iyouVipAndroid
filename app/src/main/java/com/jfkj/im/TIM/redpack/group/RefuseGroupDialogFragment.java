package com.jfkj.im.TIM.redpack.group;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.dialog.BaseDialogFragment;
import com.jfkj.im.utils.ToastUtils;

public class RefuseGroupDialogFragment extends BaseDialogFragment implements View.OnClickListener, TextWatcher {

    private ResponListener<String> responListener;
    private EditText et_refuse;
    private TextView tv_send;

    public void setResponListener(ResponListener<String> responListener){
        this.responListener = responListener;
    }

    public RefuseGroupDialogFragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_f_refuse_group,null);
        et_refuse = view.findViewById(R.id.et_refuse);
        view.findViewById(R.id.tv_refuse).setOnClickListener(this);
        tv_send = view.findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        et_refuse.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_refuse:
                responListener.Rsp("");
                dismiss();
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(et_refuse.getText().toString().trim())){
                    ToastUtils.showShortToast("请输入拒绝理由!");
                    return;
                }
                responListener.Rsp(et_refuse.getText().toString().trim());
                dismiss();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())){
            tv_send.setAlpha(0.5f);
            tv_send.setEnabled(false);
        }else {
            tv_send.setAlpha(1f);
            tv_send.setEnabled(true);
        }
    }
}
