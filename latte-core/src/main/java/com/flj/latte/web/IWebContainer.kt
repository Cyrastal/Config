package com.flj.latte.web

import android.content.Context
import android.webkit.WebView
import com.flj.latte.web.route.IRouter

/**
 * @author 傅令杰
 */
interface IWebContainer {

    val webView: WebView

    val loadedUrl: String

    val preLoadUrl: String

    val containerContext: Context?

    //根据需要重写开启方法，如果是fragment就实现start，是Activity就调用Activity的逻辑
    fun startNewContainer(next: IWebContainer)

    val router: IRouter
}