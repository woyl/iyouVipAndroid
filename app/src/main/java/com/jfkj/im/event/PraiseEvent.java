package com.jfkj.im.event;

import android.view.View;

public class PraiseEvent {
    private int position;
    private View view;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public PraiseEvent(int position, View view) {
        this.position = position;
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
