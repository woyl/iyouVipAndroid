package com.jfkj.im.ui.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;

import java.util.Objects;

public class InvitationDialogFragment extends BaseCustomHeightWithDialogFragment {

    private ResponListener<Boolean> responListener;

    public void setResponListener(ResponListener<Boolean> responListener){
        this.responListener = responListener;
    }


    public InvitationDialogFragment( int ori, int height, int with) {
        super(ori, height, with);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_f_invitation,null);

        Objects.requireNonNull(this.getDialog()).setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    responListener.Rsp(true);
                    dismiss();
                    return true;
                }else {
                    return false;
                }

            }
        });

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                responListener.Rsp(true);
                dismiss();
                return true;
            }
        });
    }


}
