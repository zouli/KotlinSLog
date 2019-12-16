package com.riverside.skeleton.kotlin.slog.printer

import com.riverside.skeleton.kotlin.slog.SLogLevel

/**
 * 输出器 1.0
 * b_e  2019/12/13
 */
interface Printer {
    var sLevel: SLogLevel

    fun print(level: SLogLevel, tag: String, msg: String, throwable: Throwable?)
}