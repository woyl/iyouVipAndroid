package com.wanglu.photoviewerlibrary

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils{
    fun dp2px(context: Context, dipValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue.toFloat(), context.resources.displayMetrics).toInt()
    }

    private val sDM = Resources.getSystem().displayMetrics

    fun getScreenWidth(): Int {
        return sDM.widthPixels
    }

    fun getScreenHeight(): Int {
        return sDM.heightPixels
    }

    fun getDensitys(): Float {
        return sDM.density
    }

    fun dpToPx(dp: Int): Int {
        return dpToPx(dp.toFloat())
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, sDM).toInt()
    }

    fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, sDM).toInt()
    }

    var density = 0f

    fun pxToDp(px: Int): Int {
        return (px / getDensitys()).roundToInt()
    }

    fun dip2px(dipValue: Float): Int {
        return (dipValue * density + 0.5f).toInt()
    }


    fun getTodayDateTime3(): String? {
        val format = SimpleDateFormat("a",
                Locale.CHINESE)
        return format.format(Date())
    }

    fun getTodayDateTime2(): String? {
        val format = SimpleDateFormat("hh:mm",
                Locale.CHINESE)
        return format.format(Date())
    }

}
