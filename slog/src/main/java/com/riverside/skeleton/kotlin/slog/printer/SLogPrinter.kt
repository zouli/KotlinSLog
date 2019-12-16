package com.riverside.skeleton.kotlin.slog.printer

import android.util.Log
import com.riverside.skeleton.kotlin.slog.SLogLevel

/**
 * Logcat输出器 1.0
 * b_e  2019/12/13
 */
class SLogPrinter(override var sLevel: SLogLevel = SLogLevel.LEVEL_VERBOSE) :
    Printer {
    override fun print(level: SLogLevel, tag: String, msg: String, throwable: Throwable?) {
        if (level < sLevel) return
        when (level) {
            SLogLevel.LEVEL_VERBOSE -> throwable?.let { Log.v(tag, msg, throwable) } ?: Log.v(
                tag,
                msg
            )
            SLogLevel.LEVEL_DEBUG -> throwable?.let { Log.d(tag, msg, throwable) } ?: Log.d(
                tag,
                msg
            )
            SLogLevel.LEVEL_INFO -> throwable?.let { Log.i(tag, msg, throwable) } ?: Log.i(tag, msg)
            SLogLevel.LEVEL_WARNING -> throwable?.let { Log.w(tag, msg, throwable) } ?: Log.w(
                tag,
                msg
            )
            SLogLevel.LEVEL_ERROR -> throwable?.let { Log.e(tag, msg, throwable) } ?: Log.e(
                tag,
                msg
            )
            SLogLevel.LEVEL_FATAL -> throwable?.let { Log.wtf(tag, msg, throwable) } ?: Log.wtf(
                tag,
                msg
            )
        }
    }
}