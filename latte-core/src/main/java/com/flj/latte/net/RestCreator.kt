package com.flj.latte.net


import com.flj.latte.R
import com.flj.latte.global.Latte
import com.flj.latte.net.interceptors.LatteCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @author 傅令杰
 */

object RestCreator {

    val restService: RestService
        get() = RestServiceHolder.mRestService

    /**
     * 构建OkHttp
     */
    private object OKHttpHolder {
        private const val mTimeout = 60
        private val mBuilder = OkHttpClient.Builder()
        private val mInterceptor =
            Latte.instance.getConfiguration<ArrayList<Interceptor>>(R.id.key_interceptor)
        //保持cookie

        internal val mOkHttpClient = initClient()
            .connectTimeout(mTimeout.toLong(), TimeUnit.SECONDS)
            .build()

        private fun initClient(): OkHttpClient.Builder {
            if (mInterceptor.isNotEmpty()) {
                for (interceptor in mInterceptor) {
                    mBuilder.addInterceptor(interceptor)
                }
            }
            val cookieJar =
                LatteCookieJar(
                    SetCookieCache(),
                    SharedPrefsCookiePersistor(Latte.instance.applicationContext)
                )
            mBuilder.cookieJar(cookieJar)
            return mBuilder
        }
    }

    /**
     * 构建全局Retrofit客户端
     */
    private object RetrofitHolder {
        private val mBaseUrl = Latte.instance.getConfiguration<String>(R.id.key_api_host)
        internal val mRetrofitClient = Retrofit.Builder()
            .baseUrl(mBaseUrl)
//            .client(OKHttpHolder.mOkHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    /**
     * Service接口
     */
    private object RestServiceHolder {
        internal val mRestService = RetrofitHolder.mRetrofitClient.create(RestService::class.java)
    }
}
