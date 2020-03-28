package com.flj.latte.net.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author 傅令杰
 */
class EmptyInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
