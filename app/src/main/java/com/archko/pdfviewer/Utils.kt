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
}