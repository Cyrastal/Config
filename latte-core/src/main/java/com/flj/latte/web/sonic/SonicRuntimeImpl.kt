package com.flj.latte.web.sonic

import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebResourceResponse
import com.flj.latte.util.log.LogUtil
import com.tencent.sonic.sdk.SonicRuntime
import com.tencent.sonic.sdk.SonicSessionClient
import java.io.InputStream

/**
 * @author 傅令杰
 */
class SonicRuntimeImpl(context: Context?) : SonicRuntime(context) {

    override fun showToast(text: CharSequence?, duration: Int) {
    }

    override fun log(tag: String?, level: Int, message: String?) {
    }

    override fun getUserAgent(): String? {
        return null
    }

    override fun isNetworkValid(): Boolean {
        //默认判断网络有效，其实应该先判断网络环境，然后返回判断值
        return true
    }

    override fun postTaskToThread(task: Runnable?, delayMillis: Long) {
        val thread = Thread(task, "SonicThread")
        thread.start()
    }

    override fun isSonicUrl(url: String?): Boolean {
        //默认是支持的，其实应该判断
        return true
    }

    override fun setCookie(url: String?, cookies: MutableList<String>): Boolean {
        if (!url.isNullOrEmpty() && cookies.isNotEmpty()) {
            val cookieManager = CookieManager.getInstance()
            cookies.forEach {
                val cookie = it
                cookieManager.setCookie(url, cookie)
            }
            return true
        }
        return false
    }

    override fun getCookie(url: String?): String? {
        val cookieManager = CookieManager.getInstance()
        return cookieManager.getCookie(url)
    }

    override fun createWebResourceResponse(
        mimeType: String?,
        encoding: String?,
        data: InputStream?,
        headers: MutableMap<String, String>?
    ): Any {
        val resourceResponse = WebResourceResponse(mimeType, encoding, data)
        resourceResponse.responseHeaders = headers
        return resourceResponse
    }

    override fun getCurrentUserAccount(): String? {
        return null
    }

    override fun notifyError(client: SonicSessionClient?, url: String?, errorCode: Int) {
        LogUtil.e("SonicRuntimeImpl", "$client? $url? $errorCode")
    }
}
