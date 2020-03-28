package com.flj.latte.web.fragments

import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import com.flj.latte.R
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.global.Latte
import com.flj.latte.util.log.LogUtil
import com.flj.latte.web.IWebContainer
import com.flj.latte.web.IWebViewInitializer
import com.flj.latte.web.LatteWebInterface
import com.flj.latte.web.route.IRouter
import com.flj.latte.web.route.RouteKeys
import com.flj.latte.web.route.Router
import com.flj.latte.web.sonic.SonicSessionClientImpl
import com.tencent.sonic.sdk.SonicConstants
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig

/**
 * @author 傅令杰
 */
abstract class WebFragment : LatteFragment(), IWebContainer {

    private lateinit var mWebView: WebView
    private var mIsWebViewAvailable = false
    private lateinit var mPreLoadUrl: String

    override val webView: WebView
        get() = mWebView

    override val loadedUrl: String
        get() = webView.url

    override val preLoadUrl: String
        get() = mPreLoadUrl

    override val router: IRouter
        get() = Router(this)

    override val containerContext: Context?
        get() = context

    abstract fun setInitializer(): IWebViewInitializer

    private fun initVasSonic() {

        val sessionConfig = SonicSessionConfig.Builder()
            .setSupportLocalServer(true)
            .setAcceptDiff(true)
            .setAutoStartWhenCreate(true)
            .setReloadInBadNetwork(true)
            .setSupportCacheControl(true)
            .setSessionMode(SonicConstants.SESSION_MODE_QUICK)
            .build()

        if (SonicEngine.isGetInstanceAllowed()) {
            val engine: SonicEngine = SonicEngine.getInstance()
            engine.preCreateSession(preLoadUrl, sessionConfig)
            val sonicSession: SonicSession? = engine.createSession(preLoadUrl, sessionConfig)
            if (sonicSession !== null) {
                val client = SonicSessionClientImpl(webView)
                sonicSession.bindClient(client)
                client.clientReady()
                LogUtil.d("initVasSonic", "sonicSession初始化成功")
            } else {
                mWebView.loadUrl(preLoadUrl)
                LogUtil.d("initVasSonic", "sonicSession初始化失败")
            }
        }
    }

    private fun initWebView() {
        val initializer = setInitializer()
        mWebView = initializer.createWebView()
        mWebView.webViewClient = initializer.createWebViewClient()
        mWebView.webChromeClient = initializer.createWebChromeClient()
        val name = Latte.instance.getConfiguration<String>(R.id.key_javascript_interface)
        mWebView.addJavascriptInterface(LatteWebInterface.create(this), name)
        mIsWebViewAvailable = true
    }

    private fun checkWebEngine() {
        val isEnableVasSonic = Latte.instance.getConfiguration<Boolean>(R.id.key_enable_vas_sonic)
        if (isEnableVasSonic) {
            initVasSonic()
            LogUtil.d("checkWebEngine", "使用VasSonic引擎")
        } else {
            mWebView.loadUrl(preLoadUrl)
            LogUtil.d("checkWebEngine", "使用原生WebView引擎")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreLoadUrl =
            arguments?.getString(RouteKeys.URL.name) ?: throw NullPointerException("Null url")
        initWebView()
        checkWebEngine()
    }

    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    override fun onPause() {
        mWebView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mIsWebViewAvailable = false
        super.onDestroyView()
    }

    override fun onDestroy() {
        mWebView.removeAllViews()
        mWebView.destroy()
        super.onDestroy()
    }
}