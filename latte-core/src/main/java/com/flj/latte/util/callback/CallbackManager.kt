package com.flj.latte.util.callback

/**
 * @author 傅令杰
 */
class CallbackManager {

    companion object {
        private val callbacks = LinkedHashMap<Any, IGlobalCallback<*>>()
        val instance: CallbackManager
            get() = Holder.instance
    }

    private object Holder {
        internal val instance = CallbackManager()
    }

    fun addCallback(tag: Any, callback: IGlobalCallback<*>): CallbackManager {
        callbacks[tag] = callback
        return this
    }

    fun getCallback(tag: Any): IGlobalCallback<*>? {
        return callbacks[tag]
    }
}
