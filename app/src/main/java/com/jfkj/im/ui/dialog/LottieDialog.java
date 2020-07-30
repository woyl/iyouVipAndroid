package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;


import com.jfkj.im.R;

public class LottieDialog extends Dialog {
    public Context context;

    public LottieDialog(Context context) {
        super(context, R.style.picture_alert_dialog);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialogstyle);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottie_loading_dialog);
    }

}
