package com.flj.latte.global

import android.app.Activity
import android.content.Context
import android.os.Handler
import androidx.annotation.IdRes
import com.blankj.utilcode.util.Utils
import com.flj.latte.R
import com.flj.latte.ui.scanner.ScannerEngines
import com.flj.latte.util.storage.MemoryStore
import com.flj.latte.web.event.Event
import com.flj.latte.web.event.EventManager
import com.flj.latte.web.sonic.SonicRuntimeImpl
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.ITypeface
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import okhttp3.Interceptor
import java.util.*

/**
 * @author 傅令杰
 */

class Configurator private constructor() {
    private val mInterceptors = LinkedList<Interceptor>()

    companion object {
        private val mStore = MemoryStore.instance
        private val handler = Handler()

        internal val instance: Configurator
            get() = Holder.instance
    }

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
        mStore.addData(R.id.key_handler, handler)
        Utils.init(Latte.instance.applicationContext)
        Iconics.init(Latte.instance.applicationContext)
        //默认不开启VasSonic引擎
        enableVasSonic(false)
    }

    private object Holder {
        val instance = Configurator()
    }

    fun withCustomConfiguration(@IdRes key: Int, value: Any): Configurator {
        mStore.addData(key, value)
        return this
    }

    fun withLoaderDelayed(delayed: Long): Configurator {
        mStore.addData(R.id.key_loader_delayed, delayed)
        return this
    }

    fun withWeChatAppId(appId: String): Configurator {
        mStore.addData(R.id.key_we_chat_app_id, appId)
        return this
    }

    fun withWeChatAppSecret(secret: String): Configurator {
        mStore.addData(R.id.key_we_chat_app_secret, secret)
        return this
    }

    /**
     * 访问服务器端API设置
     */
    fun withApiHost(host: String): Configurator {
        mStore.addData(R.id.key_api_host, host)
        return this
    }

    fun withInterceptor(interceptor: Interceptor): Configurator {
        mInterceptors.add(interceptor)
        mStore.addData(R.id.key_interceptor, mInterceptors)
        return this
    }

    fun enableVasSonic(enable: Boolean): Configurator {
        mStore.addData(R.id.key_enable_vas_sonic, enable)
        if (enable) {
            val context = MemoryStore.instance.getData<Context>(R.id.key_application_context)
            if (!SonicEngine.isGetInstanceAllowed()) {
                SonicEngine
                    .createInstance(
                        SonicRuntimeImpl(context),
                        SonicConfig.Builder().build()
                    )
            }
        }
        return this
    }

    fun withWebEvent(name: String, event: Event): Configurator {
        val manager = EventManager.instance
        manager.addEvent(name, event)
        return this
    }

    fun withJavascriptInterface(name: String): Configurator {
        mStore.addData(R.id.key_javascript_interface, name)
        return this
    }

    fun withUserAgents(userAgents: String): Configurator {
        mStore.addData(R.id.key_user_agent, userAgents)
        return this
    }

    fun withActivity(activity: Activity): Configurator {
        mStore.addData(R.id.key_activity, activity)
        return this
    }

    fun withScannerEngine(engine: Enum<ScannerEngines>): Configurator {
        mStore.addData(R.id.key_scanner_engine, engine)
        return this
    }

    fun withIconTypeface(typeface: ITypeface): Configurator {
        Iconics.registerFont(typeface)
        return this
    }

    internal fun <T> getConfiguration(@IdRes key: Int): T {
        @Suppress("UNCHECKED_CAST")
        return mStore.getData<Any>(key) as T
    }
}