package com.jfkj.im.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jfkj.im.R;


/**
 * @author gmf
 * @description
 * @date 2018/2/11.
 */
public class SendView extends RelativeLayout {

    public ImageView img_cancel, image_complete;

    public SendView(Context context) {
        this(context, null);
    }

    public SendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_view_send_btn, this);
        img_cancel = findViewById(R.id.img_cancel);
        image_complete = findViewById(R.id.image_complete);
        setVisibility(GONE);
    }

    public void startAnim() {
        setVisibility(VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(img_cancel, "translationX", 0, -220),
                ObjectAnimator.ofFloat(image_complete, "translationX", 0, 220)
        );
        set.setDuration(300).start();
    }

    public void stopAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(img_cancel, "translationX", -220, 0),
                ObjectAnimator.ofFloat(image_complete, "translationX", 220, 0)
        );
        set.setDuration(300).start();
        setVisibility(GONE);
    }

}
