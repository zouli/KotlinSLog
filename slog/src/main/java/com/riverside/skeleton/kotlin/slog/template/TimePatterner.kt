package com.riverside.skeleton.kotlin.slog.template

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间模式器 1.0
 * b_e  2019/12/13
 */
object TimePatterner : Patterner<Date> {
    override fun parse(value: Date, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(value)
    }
}