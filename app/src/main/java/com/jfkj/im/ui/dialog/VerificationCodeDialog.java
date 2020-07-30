package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jfkj.im.R;
import com.jfkj.im.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/6
 * </pre>
 */
public class VerificationCodeDialog extends Dialog implements TextWatcher {
    private Context mContext;
    private EditText mEtCode;
    private ImageView ivCode;
    private Button btnEnter;
    private String code ;
    private onEnterConfirm mConfirm;
    private Bitmap mBitmap;

    public VerificationCodeDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public VerificationCodeDialog(@NonNull Context context, int themeResId,onEnterConfirm click) {
        super(context, themeResId);
        this.mContext = context;
        this.mConfirm = click;
    }

    public void setBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_verifi_code);
        mEtCode = findViewById(R.id.et_code);
        ivCode = findViewById(R.id.iv_code);
        btnEnter = findViewById(R.id.btn_enter);
        ivCode.setImageBitmap(mBitmap);
        ImageView img_cancel = findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        mEtCode.addTextChangedListener(this);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtCode.getText().toString().trim())){
                    ToastUtils.showShortToast("请输入图形验证码");
                }else {
                    code = mEtCode.getText().toString().trim();
                    mConfirm.getCode(code);
                }
            }
        });
        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirm.again();
            }
        });

    }

    public void setImage(){
        ivCode.setImageBitmap(mBitmap);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(mEtCode.getText().toString().trim().length() == 4){
            btnEnter.setEnabled(true);
            btnEnter.setAlpha(1);
        }else{
            btnEnter.setEnabled(false);
            btnEnter.setAlpha(0.5f);
        }
        btnEnter.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shap_rejuse_bg));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface onEnterConfirm{
        void getCode(String code);

        void again();
    }
}
