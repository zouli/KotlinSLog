package com.riverside.skeleton.kotlin.slog

import com.riverside.skeleton.kotlin.slog.printer.Printer
import com.riverside.skeleton.kotlin.slog.printer.SLogPrinter
import com.riverside.skeleton.kotlin.slog.template.TemplateAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * 通用Log输出类 1.0
 * b_e  2019/12/13
 */
object SLog {
    private const val TAG = "SLog"
    private val printers: MutableList<Printer> = ArrayList()
    /**
     * TAG格式
     * 模板字段格式为 {{关键字}}
     * 关键字：
     *      thread_name 线程名称
     *      thread_id   线程ID
     *      filename    执行文件名
     *      line_number 执行行数
     *      method_name 执行方法名
     *      now|format  当前时间，format使用SimpleDateFormat语法设置
     */
    var TAG_FORMAT =
        "[{{thread_name}}({{thread_id}}):{{filename}}({{line_number}}):{{method_name}}]"

    private data class PrintInfo(
        val level: SLogLevel,
        val tag: String,
        val msg: String,
        val throwable: Throwable?
    )

    private val channelPrint = Channel<PrintInfo>()

    init {
        GlobalScope.launch {
            channelPrint.consumeEach {
                if (printers.size < 1) addPrinter(SLogPrinter())
                for (printer in printers) {
                    printer.print(it.level, it.tag, it.msg, it.throwable)
                }
            }
        }
    }

    fun v(msg: Any?, throwable: Throwable? = null, tag: String = getTag()) =
        print(SLogLevel.LEVEL_VERBOSE, tag, msg.toString(), throwable)

    fun d(msg: Any?, throwable: Throwable? = null, tag: String = getTag()) =
        print(SLogLevel.LEVEL_DEBUG, tag, msg.toString(), throwable)

    fun i(msg: Any?, throwable: Throwable? = null, tag: String = getTag()) =
        print(SLogLevel.LEVEL_INFO, tag, msg.toString(), throwable)

    fun w(msg: Any?, throwable: Throwable? = null, tag: String = getTag()) =
        print(SLogLevel.LEVEL_WARNING, tag, msg.toString(), throwable)

    fun e(msg: Any?, throwable: Throwable? = null, tag: String = getTag()) =
        print(SLogLevel.LEVEL_ERROR, tag, msg.toString(), throwable)

    fun f(msg: Any?, throwable: Throwable? = null, tag: String = getTag()) =
        print(SLogLevel.LEVEL_FATAL, tag, msg.toString(), throwable)

    fun addPrinter(printer: Printer) {
        if (printer !in printers) printers.add(printer)
    }

    private fun print(level: SLogLevel, tag: String, msg: String, throwable: Throwable?) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            channelPrint.send(PrintInfo(level, tag, msg, throwable))
        }
    }

    private fun getTag(): String {
        val sts = Thread.currentThread().stackTrace ?: return TAG

        loop@ for (st in sts) {
            when {
                st.isNativeMethod -> continue@loop
                st.className == Thread::class.java.name -> continue@loop
                st.className == SLog::class.java.name -> continue@loop
                else -> {
                    return TemplateAdapter.parse(
                        TAG_FORMAT,
                        mapOf(
                            "thread_name" to Thread.currentThread().name,
                            "thread_id" to Thread.currentThread().id.toString(),
                            "filename" to st.fileName,
                            "line_number" to st.lineNumber.toString(),
                            "method_name" to st.methodName
                        )
                    )
                }
            }
        }

        return TAG
    }
}