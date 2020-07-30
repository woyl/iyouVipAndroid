package com.jfkj.im.ui.dialog;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import java.util.Objects;

public class SendMessageDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    private ResponListener<Boolean> responListener;
    private boolean isSelect;

    public SendMessageDialogFragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    public void setResponListener(ResponListener<Boolean> responListener) {
        this.responListener = responListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                if (isSelect){
                    SPUtils.getInstance(App.getAppContext()).put(Utils.APPID+Utils.IS_TIPS,true);
                }
                responListener.Rsp(true);
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_f_send_message, null);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        CheckBox checkbox = view.findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkbox.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()),R.mipmap.pop_radio_btn_sel));
                    isSelect = true;

                }else {
                    checkbox.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()),R.mipmap.pop_radio_btn_unsel));
                    isSelect = false;
                }
            }
        });
    }
}
