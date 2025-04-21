package com.archko.pdfviewer

import android.content.Context

/**
 * @author: archko 2025/1/6 :09:19
 */
object Utils {
    fun dp2px(context: Context, dip: Float): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (dip * scale + 0.5f).toInt()
    }

    /**
     * 获取屏幕高度(px)
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度(px)
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
}