package com.flj.latte.util.storage

import androidx.annotation.IdRes
import java.util.*

/**
 * @author 傅令杰
 */
class MemoryStore {

    private val mDataMap = HashMap<Any, Any>()

    private object Holder {
        internal val INSTANCE = MemoryStore()
    }

    companion object {
        val instance: MemoryStore
            get() = Holder.INSTANCE
    }

    fun addData(key: String, value: Any): MemoryStore {
        mDataMap[key] = value
        return this
    }

    fun addData(key: Enum<*>, value: Any): MemoryStore {
        mDataMap[key] = value
        return this
    }

    fun addData(@IdRes key: Int, value: Any): MemoryStore {
        mDataMap[key] = value
        return this
    }

    fun <T> getData(key: String): T {
        @Suppress("UNCHECKED_CAST")
        return mDataMap[key] as T
    }

    fun <T> getData(key: Enum<*>): T {
        @Suppress("UNCHECKED_CAST")
        return mDataMap[key] as T
    }

    fun <T> getData(@IdRes key: Int): T {
        @Suppress("UNCHECKED_CAST")
        return mDataMap[key] as T
    }

    fun removeData(key: String): MemoryStore {
        mDataMap.remove(key)
        return this
    }

    fun removeData(key: Enum<*>): MemoryStore {
        mDataMap.remove(key)
        return this
    }

    fun removeData(@IdRes key: Int): MemoryStore {
        mDataMap.remove(key)
        return this
    }
}
