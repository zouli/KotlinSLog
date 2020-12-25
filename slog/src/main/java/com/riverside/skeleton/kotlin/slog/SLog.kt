package com.riverside.skeleton.kotlin.slog

import com.riverside.skeleton.kotlin.slog.printer.Printer
import com.riverside.skeleton.kotlin.slog.printer.SLogPrinter
import com.riverside.skeleton.kotlin.slog.template.TemplateAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * 通用Log输出类 1.1
 * b_e  2019/12/13
 * 1.1  简化参数    2020/12/25
 * 1.1  添加类型判断  2020/12/25
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

    fun v(msg: Any?, tag: String = getTag()) = print(SLogLevel.LEVEL_VERBOSE, tag, msg)
    fun d(msg: Any?, tag: String = getTag()) = print(SLogLevel.LEVEL_DEBUG, tag, msg)
    fun i(msg: Any?, tag: String = getTag()) = print(SLogLevel.LEVEL_INFO, tag, msg)
    fun w(msg: Any?, tag: String = getTag()) = print(SLogLevel.LEVEL_WARNING, tag, msg)
    fun e(msg: Any?, tag: String = getTag()) = print(SLogLevel.LEVEL_ERROR, tag, msg)
    fun f(msg: Any?, tag: String = getTag()) = print(SLogLevel.LEVEL_FATAL, tag, msg)

    fun addPrinter(printer: Printer) {
        if (printer !in printers) printers.add(printer)
    }

    private fun print(level: SLogLevel, tag: String, msg: Any?) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            when (msg) {
                is Throwable -> channelPrint.send(PrintInfo(level, tag, "", msg))
                is Array<*> -> channelPrint.send(PrintInfo(level, tag, Arrays.toString(msg), null))
                else -> channelPrint.send(PrintInfo(level, tag, msg.toString(), null))
            }

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