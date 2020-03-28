package com.flj.latte.web.event

import android.content.Context
import android.webkit.WebView
import com.flj.latte.web.IWebContainer

/**
 * @author 傅令杰
 */
abstract class Event : IEvent {

    private lateinit var mContainer: IWebContainer

    val context: Context?
        get() = mContainer.containerContext

    val webView: WebView
        get() = mContainer.webView

    val preLoadUrl: String
        get() = mContainer.preLoadUrl

    val loadedUrl: String
        get() = mContainer.loadedUrl

    val container: IWebContainer
        get() = mContainer

    fun setContainer(container: IWebContainer) {
        this.mContainer = container
    }
}