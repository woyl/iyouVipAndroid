package com.jfkj.im.event;

public class ShowDialogEvent {
    private boolean isDialog;
    private String redPackageId;
    private String redRec;


    public ShowDialogEvent(boolean isDialog, String redPackageId, String redRec) {
        this.isDialog = isDialog;
        this.redPackageId = redPackageId;
        this.redRec = redRec;
    }

    public String getRedRec() {
        return redRec;
    }

    public void setRedRec(String redRec) {
        this.redRec = redRec;
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }

    public String getRedPackageId() {
        return redPackageId;
    }

    public void setRedPackageId(String redPackageId) {
        this.redPackageId = redPackageId;
    }
}
