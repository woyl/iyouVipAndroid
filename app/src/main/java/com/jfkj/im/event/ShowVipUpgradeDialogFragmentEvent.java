package com.jfkj.im.event;

public class ShowVipUpgradeDialogFragmentEvent {
    private String content;

    public ShowVipUpgradeDialogFragmentEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
