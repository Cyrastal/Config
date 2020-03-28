package com.flj.latte.web.sonic

import android.os.Bundle
import android.webkit.WebView
import com.tencent.sonic.sdk.SonicSessionClient
import java.util.*

/**
 * @author 傅令杰
 */
class SonicSessionClientImpl(private val webView: WebView) : SonicSessionClient() {

    override fun loadUrl(url: String?, extraData: Bundle?) {
        webView.loadUrl(url)
    }

    override fun loadDataWithBaseUrlAndHeader(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?,
        headers: HashMap<String, String>?
    ) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
    }

    override fun loadDataWithBaseUrl(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?
    ) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
    }
}
