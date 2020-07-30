package com.jfkj.im.TIM.component.gatherimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.jfkj.im.TIM.utils.ScreenUtil;


@SuppressLint("AppCompatCustomView")
public class ShadeImageView extends ImageView {

    private static SparseArray<Bitmap> sRoundBitmapArray = new SparseArray();
    private Paint mShadePaint = new Paint();
    private Bitmap mRoundBitmap;
    private int radius = ScreenUtil.getPxByDp(50);

    public ShadeImageView(Context context) {
        super(context);
    }

    public ShadeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShadeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mShadePaint.setColor(Color.RED);
        mShadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mRoundBitmap = sRoundBitmapArray.get(getMeasuredWidth() + radius);

        if (mRoundBitmap == null) {
            mRoundBitmap = getRoundBitmap();
            sRoundBitmapArray.put(getMeasuredWidth() + radius, mRoundBitmap);
        }
        canvas.drawBitmap(mRoundBitmap, 0, 0, mShadePaint);
    }


    /**
     * 获取圆角矩形图片方法
     *
     * @return Bitmap
     */
    private Bitmap getRoundBitmap() {
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = Color.parseColor("#111111");
        final Rect rect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        final RectF rectF = new RectF(rect);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        Log.e("msg","........getMeasuredWidth()..........."+getMeasuredWidth());
        Log.e("msg","........getMeasuredHeight()..........."+getMeasuredHeight());
//        canvas.drawRoundRect(rectF, radius, radius, paint);
        float size = (float)getMeasuredHeight()/2;
        canvas.drawCircle(size,size,size,paint);
        return output;

    }


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


}