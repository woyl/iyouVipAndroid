package com.jfkj.im.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.math.BigDecimal;

/**
 *
 * =
 *         //3. 单张图片，如果0.5 < = 宽 / 高 <= 2 时，被限定在1—4格子的范围大小（包括间距），也就是凡是宽高比在这个范围时，最长的那边暂两个格子加间距；
 *
 *         //4. 单张图片，宽 / 高 > 2的图片（如全景图），最多占三栏，高最多占一栏（包括间距大小）；
 *
 *         //5. 单张图片，宽 / 高 < 0.5（如微博长图），高最多占二栏，宽度最小占二栏1/3（包括间距）；
 *
 *         Iyou单图展示
 */
public class MyScaleImageView extends ImageView {
    private static final String TAG = "MyImageView";
    private  Context context;

    int intrinsicHeight  ;
    int intrinsicWidth ;
    float width, height;

    public MyScaleImageView(Context context) {
        super(context);

    }

    public MyScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void test(int w,int h , Context context){
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        BitmapFactory.decodeResource(getResources(),id,options);
        this.context = context;


        //获取图片的宽高
        intrinsicHeight = h;
        intrinsicWidth = w;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();

        double twoThirds = new BigDecimal((float) 2/3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double oneThird = new BigDecimal((float) 1/3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();



        if(d!=null){

            if (intrinsicWidth == 0 && intrinsicHeight == 0) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            double WHScale = new BigDecimal((float) intrinsicWidth/intrinsicHeight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            //屏幕宽度
            int screenWidth = getScreenWidth();



            if(WHScale>=0.5 && WHScale<=2){
                //3. 单张图片，如果0.5 < = 宽 / 高 <= 2 时，被限定在1—4格子的范围大小（包括间距），也就是凡是宽高比在这个范围时，最长的那边暂两个格子加间距；
                //被限定在1—4格子的范围大小（包括间距）   相当于 宽高最大在屏幕宽度的 2/3


                double screen  = screenWidth* twoThirds;

                //1. 判断图片宽度是否大于 屏幕 2/3
                if(intrinsicWidth > screen){
                    //缩放图片,  缩放比例为   图片宽度/屏幕2/3

                    double scalingRatio =new BigDecimal((float) intrinsicWidth/screen).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //给图片宽高同时 * 缩放比例

                    double viewHeight = intrinsicHeight / scalingRatio;
                    double viewWidth =  intrinsicWidth / scalingRatio;
                    setMeasuredDimension((int)viewWidth, (int)viewHeight);
                }else if(intrinsicHeight > screen){
                    //2.不大于屏幕宽度 2/3 ,判断高度是否大于 屏幕宽度 2/3
                    double scalingRatio = new BigDecimal((float)  intrinsicHeight/screen).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double viewHeight = intrinsicHeight / scalingRatio;
                    double viewWidth =  intrinsicWidth / scalingRatio;
                    setMeasuredDimension((int)viewWidth, (int)viewHeight);
                }else{
                    setMeasuredDimension(intrinsicWidth,intrinsicHeight);
                }
            }else if (WHScale >= 2){

                double screenH = screenWidth* oneThird;

                //4. 单张图片，宽 / 高 > 2的图片（如全景图），最多占三栏，高最多占一栏（包括间距大小）；
                if(intrinsicWidth > screenWidth){
                    double scalingRatio =new BigDecimal((float)intrinsicWidth/screenWidth).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
                    double viewHeight =    intrinsicHeight / scalingRatio;
                    double viewWidth =    intrinsicWidth / scalingRatio;
                    setMeasuredDimension((int)viewWidth, (int)viewHeight);
                }else if(intrinsicHeight > screenH ){
                    //高度如果大于屏幕 1/3
                    double scalingRatio = new  BigDecimal((float) intrinsicHeight / screenH).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double viewHeight =    intrinsicHeight / scalingRatio;
                    double viewWidth =    intrinsicWidth / scalingRatio;
                    setMeasuredDimension((int)viewWidth, (int)viewHeight);
                }else{
                    setMeasuredDimension(intrinsicWidth,intrinsicHeight);
                }

            }else if(WHScale < 0.5){
                //5. 单张图片，宽 / 高 < 0.5（如微博长图），高最多占二栏，宽度最小占二栏1/3（包括间距）；
                //高最多占二栏，宽度最小占二栏1/3
                double screenH  = screenWidth* twoThirds;
                double screenW  = screenWidth* oneThird;
                if(intrinsicHeight > screenH ){
                    double scalingRatio =new BigDecimal((float)intrinsicHeight/screenH).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()   ;
                    double viewHeight =    intrinsicHeight/scalingRatio;
                    double viewWidth =     intrinsicWidth / scalingRatio;
                    setMeasuredDimension((int)viewWidth, (int)viewHeight);
                }else if (intrinsicWidth<screenW){
                    double scalingRatio =new BigDecimal((float)  intrinsicWidth/screenW).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double viewHeight = intrinsicHeight/scalingRatio;
                    double viewWidth = intrinsicWidth/scalingRatio;
                    setMeasuredDimension((int)viewWidth, (int)viewHeight);
                }else{
                    setMeasuredDimension(intrinsicWidth,intrinsicHeight);
                }
            }else{
                setMeasuredDimension(intrinsicWidth,intrinsicHeight);
            }



        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= 12 && height > 12) {
            Path path = new Path();
            //四个圆角
            path.moveTo(12, 0);
            path.lineTo(width - 12, 0);
            path.quadTo(width, 0, width, 12);
            path.lineTo(width, height - 12);
            path.quadTo(width, height, width - 12, height);
            path.lineTo(12, height);
            path.quadTo(0, height, 0, height - 12);
            path.lineTo(0, 12);
            path.quadTo(0, 0, 12, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }


    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        int i = dip2px(context, 110);

        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels - i;
    }
}
