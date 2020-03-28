package com.flj.latte.pay

import android.content.Context
import androidx.annotation.NonNull
import com.flj.latte.R
import com.flj.latte.util.storage.MemoryStore

/**
 * @author 傅令杰
 */
class LattePayBuilder internal constructor() {

    private val mParams = LinkedHashMap<String, Any>()
    private lateinit var mAlPayResultListener: IAlPayResultListener
    private lateinit var mContext: Context
    private lateinit var mAlPayUrl: String

    fun context(@NonNull context: Context): LattePayBuilder {
        this.mContext = context
        return this
    }

    fun alPayUrl(@NonNull url: String): LattePayBuilder {
        this.mAlPayUrl = url
        return this
    }

    fun params(key: String, value: Any): LattePayBuilder {
        this.mParams[key] = value
        return this
    }

    fun alPayResultListener(@NonNull listener: IAlPayResultListener): LattePayBuilder {
        this.mAlPayResultListener = listener
        return this
    }

    //是否显示回调的Activity
    fun showWXPayEntry(show: Boolean): LattePayBuilder {
        MemoryStore.instance
            .addData(R.id.key_we_chat_show_pay_entry, show)
        return this
    }

    fun build(): LattePay {
        return LattePay(
            mContext,
            mParams,
            mAlPayUrl,
            mAlPayResultListener
        )
    }
}
