package com.flj.latte.ui.recycler.data

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.util.*

/**
 * @author 傅令杰
 */
abstract class DataConverter(protected val response: String?) {

    private val mEntities = LinkedList<LatteItemEntity>()

    protected val entities: LinkedList<LatteItemEntity>
        get() = mEntities

    protected val jsonObject: JSONObject
        get() = JSON.parseObject(response)

    abstract fun convert(): LinkedList<LatteItemEntity>

    fun clearData() {
        mEntities.clear()
    }
}
