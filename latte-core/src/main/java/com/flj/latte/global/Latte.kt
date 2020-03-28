package com.flj.latte.global

import android.app.Application
import android.content.Context
import android.os.Handler
import androidx.annotation.IdRes
import com.flj.latte.R
import com.flj.latte.util.storage.MemoryStore


/**
 * @author 傅令杰
 */

class Latte private constructor() {

    object Holder {
        val instance: Latte = Latte()
    }

    companion object {
        val instance: Latte
            get() = Holder.instance
    }

    val configurator: Configurator
        get() = Configurator.instance

    val applicationContext: Application
        //这里不直接从getConfiguration里取避免configure检查
        get() = MemoryStore
            .instance
            .getData(R.id.key_application_context)

    val handler: Handler
        get() = getConfiguration(R.id.key_handler)

    fun init(context: Context): Configurator {
        MemoryStore
            .instance
            .addData(R.id.key_application_context, context.applicationContext)
        return Configurator.instance
    }

    fun <T> getConfiguration(@IdRes key: Int): T {
        return configurator.getConfiguration(key)
    }
}