package com.jfkj.im.ui.dialog;

import com.jfkj.im.R;
import com.jfkj.im.interfaces.ResponListener;

public class ReadPackageDialog extends BaseDialogFragment{

    private ResponListener<Boolean> responListener;

    public void setResponListener(ResponListener<Boolean> responListener){
        this.responListener = responListener;
    }

    public ReadPackageDialog(boolean isWidth, int ori) {
        super(isWidth, ori);
    }

    @Override
    protected void initViews() {
        view = inflater.inflate(R.layout.dialog_red_package,null);
    }
}
