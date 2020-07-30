package com.jfkj.im.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MyConstraintLayout  extends ConstraintLayout {


    private float round = 20f;//圆角半径像素值

    public MyConstraintLayout(Context context) {
        super(context);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRound(float round) {
        this.round = round;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (round > 0) {
            Path path = new Path();
            RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            path.addRoundRect(rectF, round, round, Path.Direction.CW);
            // 先对canvas进行裁剪
            canvas.clipPath(path, Region.Op.INTERSECT);
        }
        super.dispatchDraw(canvas);
    }


}
