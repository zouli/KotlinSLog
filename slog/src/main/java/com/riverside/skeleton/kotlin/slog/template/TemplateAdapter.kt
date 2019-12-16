package com.riverside.skeleton.kotlin.slog.template

import java.util.*

/**
 * 模板适配器 1.0
 * b_e  2019/12/13
 */

private data class FormatItem(
    val start: Int,
    val end: Int,
    val key: String,
    val pattern: String = ""
)

object TemplateAdapter {
    fun parse(format: String, param: Map<String, String> = mapOf()): String {
        val formatMap = parseFormat(format).reversed()
        var result = format
        for (formatItem in formatMap) {
            result = result.replaceRange(
                formatItem.start, formatItem.end,
                when (formatItem.key) {
                    "now" -> TimePatterner.parse(Date(), formatItem.pattern)
                    else -> if (formatItem.key in param) param[formatItem.key]
                        ?: formatItem.key else formatItem.key
                }
            )
        }

        return result
    }

    private fun parseFormat(format: String): List<FormatItem> {
        val formatItemList = mutableListOf<FormatItem>()
        var leftover = format
        var indexNext = 0

        while (leftover.isNotEmpty()) {
            val indexStart = leftover.indexOf("{{")
            if (indexStart >= 0) {
                var indexEnd = leftover.indexOf("}}")
                var item = leftover.substring(indexStart + 2, indexEnd)
                var pattern = ""
                if (item.indexOf("|") > 0) {
                    pattern = item.substring(item.indexOf("|") + 1)
                    item = item.substring(0, item.indexOf("|"))
                }

                indexEnd += 2

                formatItemList.add(
                    FormatItem(
                        indexNext + indexStart,
                        indexNext + indexEnd,
                        item,
                        pattern
                    )
                )
                indexNext += indexEnd
                leftover = leftover.substring(indexEnd)
            } else {
                break
            }
        }

        if (indexNext == 0) formatItemList.add(FormatItem(0, format.lastIndex, format))

        return formatItemList
    }
}