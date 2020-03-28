package com.flj.latte.web.event

/**
 * @author 傅令杰
 */
interface IEvent {

    fun execute(params: String?): String?
}
