package com.flj.latte.net.callback

/**
 * @author 傅令杰
 */

interface IError {

    fun onError(code: Int, message: String)
}
