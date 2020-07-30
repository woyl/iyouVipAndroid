package com.jfkj.im.ui.dialog;

import android.view.View;

import com.jfkj.im.R;


public class ChooseLikeFemaleDialogFragment extends BaseBgDialogFragment implements View.OnClickListener {

    public ChooseLikeFemaleDialogFragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_frag_choose_like_m,null);
        view.findViewById(R.id.tv_go).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_go:
                dismiss();
                break;
        }
    }
}
