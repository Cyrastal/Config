package com.flj.mall.fragments.sort.list

import com.alibaba.fastjson.JSON
import com.flj.latte.ui.recycler.data.DataConverter
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import java.util.*

class VerticalListDataConverter(response: String) : DataConverter(response) {

    override fun convert(): LinkedList<LatteItemEntity> {
        val dataList = LinkedList<LatteItemEntity>()
        val dataArray = JSON
            .parseObject(response)
            .getJSONObject("data")
            .getJSONArray("list")

        val size = dataArray.size
        for (i in 0 until size) {
            val data = dataArray.getJSONObject(i)
            val id = data.getInteger("id")
            val name = data.getString("name")

            val entity = LatteItemEntity
                .builder()
                .setField(R.id.field_item_type, R.id.type_vertical_menu_list)
                .setField(R.id.field_id, id)
                .setField(R.id.field_text, name)
                .setField(R.id.field_tag, false)
                .build()

            dataList.add(entity)

            //默认设置第一个类型被选中
            dataList[0].setField(R.id.field_tag, true)
        }
        return dataList
    }
}