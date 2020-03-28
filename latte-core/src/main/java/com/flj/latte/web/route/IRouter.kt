package com.flj.latte.web.route

import com.flj.latte.web.IWebContainer

/**
 * @author 傅令杰
 */
interface IRouter {

    fun start(next: IWebContainer): Boolean

    fun start(next: IWebContainer, url: String): Boolean

    fun load(url: String): Boolean
}