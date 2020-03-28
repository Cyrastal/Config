package com.flj.latte.ui.recycler.data

import androidx.annotation.IdRes
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.flj.latte.R
import java.util.*

/**
 * @author 傅令杰
 */
class LatteItemEntity internal constructor(private val fields: LinkedHashMap<Int, Any>) :
    MultiItemEntity {

    companion object {
        fun builder(): LatteEntityBuilder {
            return LatteEntityBuilder()
        }
    }

    override fun getItemType(): Int {
        return fields[R.id.field_item_type] as Int
    }

    fun getSpanSize(): Int {
        return getField(R.id.field_span_size)
    }

    fun <T> getField(@IdRes key: Int): T {
        @Suppress("UNCHECKED_CAST")
        return fields[key] as T
    }

    fun setField(@IdRes key: Int, value: Any): LatteItemEntity {
        fields[key] = value
        return this
    }
}
