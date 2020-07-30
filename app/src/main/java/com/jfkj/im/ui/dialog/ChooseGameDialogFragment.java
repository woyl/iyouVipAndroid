package com.jfkj.im.ui.dialog;

import com.jfkj.im.R;

public class ChooseGameDialogFragment extends BaseDialogFragment {

    public ChooseGameDialogFragment(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_f_choose_game,null);
    }
}
