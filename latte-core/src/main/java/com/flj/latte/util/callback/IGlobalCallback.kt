package com.flj.latte.util.callback

/**
 * @author 傅令杰
 */
interface IGlobalCallback<T> {
    fun executeCallback(args: T?)
}
