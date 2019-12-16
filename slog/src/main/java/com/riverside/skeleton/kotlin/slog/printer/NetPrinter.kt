package com.riverside.skeleton.kotlin.slog.printer

import com.riverside.skeleton.kotlin.slog.SLogLevel

/**
 * 网络输出器 1.0
 * b_e  2019/12/13
 */
class NetPrinter() : Printer {
    override var sLevel: SLogLevel =
        SLogLevel.LEVEL_VERBOSE
    lateinit var callback: (level: SLogLevel, tag: String, msg: String, throwable: Throwable?) -> Unit

    constructor(callback: (level: SLogLevel, tag: String, msg: String, throwable: Throwable?) -> Unit) : this() {
        this.callback = callback
    }

    override fun print(level: SLogLevel, tag: String, msg: String, throwable: Throwable?) {
        this.callback(level, tag, msg, throwable)
    }
}