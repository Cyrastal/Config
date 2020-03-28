package com.flj.latte.pay

/**
 * @author 傅令杰
 */
interface IAlPayResultListener {

    fun onAlPaySuccess()

    fun onAlPaying()

    fun onAlPayFail()

    fun onAlPayCancel()

    fun onAlPayConnectError()
}
