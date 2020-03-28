package com.flj.latte.web

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.flj.latte.R
import com.flj.latte.global.Latte

/**
 * @author 傅令杰
 */
class WebViewCreator(private val container: IWebContainer) {

    companion object {
        fun create(container: IWebContainer): WebViewCreator {
            return WebViewCreator(container)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun buildDefault(): WebView {
        val context = container.containerContext
        val webView = WebView(context)
        WebView.setWebContentsDebuggingEnabled(true)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        }
        CookieManager.setAcceptFileSchemeCookies(true)
        //不能横向滚动
        webView.isHorizontalScrollBarEnabled = false
        //不能纵向滚动
        webView.isVerticalScrollBarEnabled = false
        //允许截图
//        webView.isDrawingCacheEnabled = true
        //屏蔽长按事件
        webView.setOnLongClickListener { true }
        //初始化WebSettings
        val settings = webView.settings
        settings.javaScriptEnabled = true
        val ua = settings.userAgentString
        val agents = Latte.instance.getConfiguration<String>(R.id.key_user_agent)
        settings.userAgentString = ua + agents
        //解决HTTPS网站图片不显示问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        //隐藏缩放控件
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        //禁止缩放
        settings.setSupportZoom(false)
        //禁止保存密码
//        settings.savePassword = false
//        settings.saveFormData = false
        //强制全屏
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        //文件权限
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowContentAccess = true
        //缓存相关
        settings.setAppCacheEnabled(true)
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        val interfaceName =
            Latte.instance.configurator.getConfiguration<String>(R.id.key_javascript_interface)
        webView.addJavascriptInterface(LatteWebInterface.create(container), interfaceName)

        return webView
    }

}
