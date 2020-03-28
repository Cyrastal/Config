package com.flj.latte.web.route

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.flj.latte.web.IWebContainer

/**
 * @author 傅令杰
 */
class Router constructor(private val current: IWebContainer) : IRouter {

    override fun start(next: IWebContainer, url: String): Boolean {
        //如果是电话协议
        if (url.contains("tel:")) {
            callPhone(current.containerContext, url)
            return true
        }
        current.startNewContainer(next)
        return true
    }

    override fun start(next: IWebContainer): Boolean {
        current.startNewContainer(next)
        return true
    }

    override fun load(url: String): Boolean {
        //如果是电话协议
        if (url.contains("tel:")) {
            callPhone(current.containerContext, url)
            return true
        }
        val webView = current.webView
        webView.post {
            webView.loadUrl(url)
        }
        return true
    }

    private fun callPhone(context: Context?, uri: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse(uri)
        intent.data = data
        context?.let { ContextCompat.startActivity(it, intent, null) }
    }
}