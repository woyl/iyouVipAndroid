package com.jfkj.im.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.widget.EditText;

import com.jfkj.im.R;

public  class AtGroupUtils {


    public static String setAtImageSpan(final Context context, EditText mEditText, String nameStr) {


        String str = "";


        String content = String.valueOf(mEditText.getText());
        if (content.endsWith("@") || content.endsWith("＠")) {
            content = content.substring(0, content.length() - 1);
        }
        String tmp = content;
        SpannableString ss = new SpannableString(tmp);
        if (nameStr != null) {
            //@aaa 111 @bbb 111 111
            String[] names = nameStr.split(" ");
            if (names.length > 0) {
                for (String name : names) {
                    if (name != null && name.trim().length() > 0) {
                        //把获取到的名字转为bitmap对象
                        final Bitmap bmp = getNameBitmap(context, name);
                        // 这里会出现删除过的用户，需要做判断，过滤掉
                        if (tmp.contains(name) && (tmp.indexOf(name) + name.length()) <= tmp.length()) {

                            // 把取到的要@的人名，用DynamicDrawableSpan代替
                            ss.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
                                           @Override
                                           public Drawable getDrawable() {
                                               BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bmp);
                                               drawable.setBounds(0, 0, bmp.getWidth(), bmp.getHeight());
                                               return drawable;
                                           }
                                       }, tmp.indexOf(name),
                                    tmp.indexOf(name) + name.length(),
                                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

                        }else{
                            str += name;
                        }
                    }
                }
            }
        }
        mEditText.setTextKeepState(ss);

        return str;
    }



    /**
     * 把返回的人名，转换成bitmap
     *
     * @param name 人名
     * @return 图片
     */
    private static Bitmap getNameBitmap(Context context, String name) {
        /* 把@相关的字符串转换成bitmap 然后使用DynamicDrawableSpan加入输入框中 */
        name = "" + name;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 设置字体画笔的颜色
         paint.setColor(context.getResources().getColor(R.color.white));
        paint.setTextSize(40);
        Rect rect = new Rect();
        paint.getTextBounds(name, 0, name.length(), rect);
        // 获取字符串在屏幕上的长度
        int width = (int) (paint.measureText(name));
        final Bitmap bmp = Bitmap.createBitmap(width, rect.height(), Bitmap.Config.ARGB_8888);// 长宽+5，可调
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(context.getResources().getColor(R.color.transparent));
        canvas.drawText(name, rect.left, rect.height() - rect.bottom, paint);
        return bmp;
    }




}
