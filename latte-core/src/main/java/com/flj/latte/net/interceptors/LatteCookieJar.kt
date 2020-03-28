package com.flj.latte.net.interceptors

import com.flj.latte.util.callback.CallbackManager
import com.flj.latte.util.callback.CallbackType
import com.flj.latte.util.callback.IGlobalCallback
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * @author 傅令杰
 */
class LatteCookieJar(catch: CookieCache, persistor: CookiePersistor) :
    PersistentCookieJar(catch, persistor) {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = super.loadForRequest(url)
        if (cookies.isEmpty()) {
            val callback = CallbackManager
                .instance
                .getCallback(CallbackType.ON_NO_COOKIE)
            if (callback != null) {
                @Suppress("UNCHECKED_CAST")
                val targetCallback = callback as IGlobalCallback<LatteCookieJar>
                targetCallback.executeCallback(this)
            }
        }
        return cookies
    }
}