package com.flj.latte.util.log

import com.orhanobut.logger.Logger

/**
 * Created by 傅令杰 on 2017/4/22
 */

object LogUtil {

    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val NOTHING = 6

    //控制log等级
    private const val LEVEL = VERBOSE

    fun v(tag: String, message: String) {
        if (LEVEL <= VERBOSE) {
            Logger.t(tag).v(message)
        }
    }

    fun d(tag: String, message: Any?) {
        if (LEVEL <= DEBUG) {
            Logger.t(tag).d(message)
        }
    }

    fun d(message: Any) {
        if (LEVEL <= DEBUG) {
            Logger.d(message)
        }
    }

    fun i(tag: String, message: String) {
        if (LEVEL <= INFO) {
            Logger.t(tag).i(message)
        }
    }

    fun w(tag: String, message: String) {
        if (LEVEL <= WARN) {
            Logger.t(tag).w(message)
        }
    }

    fun json(tag: String, message: String?) {
        if (LEVEL <= WARN) {
            Logger.t(tag).json(message)
        }
    }

    fun e(tag: String, message: String?) {
        if (LEVEL <= ERROR) {
            message?.let { Logger.t(tag).e(it) }
        }
    }
}
