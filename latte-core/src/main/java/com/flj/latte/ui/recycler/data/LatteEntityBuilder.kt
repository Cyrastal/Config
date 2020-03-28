package com.flj.latte.ui.recycler.data

import androidx.annotation.IdRes
import com.flj.latte.R
import java.util.*

/**
 * @author 傅令杰
 */
class LatteEntityBuilder {

    private val mFields = LinkedHashMap<Int, Any>()

    fun setItemType(@IdRes itemType: Int): LatteEntityBuilder {
        return setField(R.id.field_item_type, itemType)
    }

    fun setSpanSize(spanSize: Int): LatteEntityBuilder {
        return setField(R.id.field_span_size, spanSize)
    }

    fun setField(@IdRes key: Int, value: Any?): LatteEntityBuilder {
        if (value != null) {
            mFields[key] = value
        }
        return this
    }

    fun setFields(map: LinkedHashMap<Int, *>): LatteEntityBuilder {
        mFields.putAll(map)
        return this
    }

    fun build(): LatteItemEntity {
        return LatteItemEntity(mFields)
    }
}
