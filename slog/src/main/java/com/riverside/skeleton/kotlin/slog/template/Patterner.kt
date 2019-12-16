package com.riverside.skeleton.kotlin.slog.template

/**
 * 模式器 1.0
 * b_e  2019/12/13
 */
interface Patterner<T> {
    fun parse(value: T, pattern: String): String
}