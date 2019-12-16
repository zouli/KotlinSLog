package com.riverside.skeleton.kotlin.slog

/**
 * LOG级别类 1.0
 * b_e  2019/12/13
 */
enum class SLogLevel {
    LEVEL_VERBOSE {
        override val longName = "verbose"
        override val shortName = "v"
    },
    LEVEL_DEBUG {
        override val longName = "debug"
        override val shortName = "d"
    },
    LEVEL_INFO {
        override val longName = "info"
        override val shortName = "i"
    },
    LEVEL_WARNING {
        override val longName = "warning"
        override val shortName = "w"
    },
    LEVEL_ERROR {
        override val longName = "error"
        override val shortName = "e"
    },
    LEVEL_FATAL {
        override val longName = "fatal"
        override val shortName = "f"
    };

    abstract val longName: String
    abstract val shortName: String
}