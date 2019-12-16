package com.riverside.skeleton.kotlin.slog.printer

import com.riverside.skeleton.kotlin.slog.SLogLevel
import com.riverside.skeleton.kotlin.slog.template.TemplateAdapter
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文件输出器 1.0
 * b_e  2019/12/13
 */
class FilePrinter() : Printer {
    override var sLevel: SLogLevel = SLogLevel.LEVEL_VERBOSE

    companion object {
        /**
         * 文件名格式
         * 模板字段格式为 {{关键字}}
         * 关键字：
         *      now|format  当前时间，format使用SimpleDateFormat语法设置
         */
        var FILENAME_FORMAT = "/{{now|yyyyMMdd}}.log"

        /**
         * 信息格式
         * 模板字段格式为 {{关键字}}
         * 关键字：
         *      level_long  LOG基本名
         *      level_short LOG基本名（短）
         *      tag         TAG名
         *      msg         信息
         *      now|format  当前时间，format使用SimpleDateFormat语法设置
         */
        var MSG_FORMAT = "{{level_short}}/{{tag}}: {{msg}}"

        var path: String = ""

        private val targetStream: PrintStream by lazy {
            val fos = FileOutputStream(path + TemplateAdapter.parse(FILENAME_FORMAT), true)
            PrintStream(fos)
        }
    }

    constructor(path: String) : this() {
        Companion.path = path
    }

    override fun print(level: SLogLevel, tag: String, msg: String, throwable: Throwable?) {
        write(
            TemplateAdapter.parse(
                MSG_FORMAT,
                mapOf(
                    "level_short" to level.shortName,
                    "level_long" to level.longName,
                    "tag" to tag,
                    "msg" to msg
                )
            ),
            throwable
        )
    }

    private fun write(buf: String, throwable: Throwable?) {
        targetStream.println(buf)
        throwable?.printStackTrace(targetStream)
        targetStream.flush()
    }
}