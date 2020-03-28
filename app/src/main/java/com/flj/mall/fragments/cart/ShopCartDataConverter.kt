package com.flj.mall.fragments.cart

import com.alibaba.fastjson.JSON
import com.flj.latte.ui.recycler.data.DataConverter
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import java.util.*

class ShopCartDataConverter(response: String) : DataConverter(response) {

    override fun convert(): LinkedList<LatteItemEntity> {
        val dataList = LinkedList<LatteItemEntity>()
        val dataArray = JSON.parseObject(response).getJSONArray("data")
        val size = dataArray.size
        for (i in 0 until size) {
            val data = dataArray.getJSONObject(i)
            val thumb = data.getString("thumb")
            val desc = data.getString("desc")
            val title = data.getString("title")
            val id = data.getInteger("id")
            val count = data.getInteger("count")
            val price = data.getDouble("price")

            val entity = LatteItemEntity
                .builder()
                .setField(R.id.field_item_type, R.id.type_shop_cart)
                .setField(R.id.field_id, id)
                .setField(R.id.field_image_url, thumb)
                .setField(R.id.field_title, title)
                .setField(R.id.field_description, desc)
                .setField(R.id.field_count, count)
                .setField(R.id.field_price, price)
                .setField(R.id.field_is_selected, false)
                .setField(R.id.field_position, i)
                .build()

            dataList.add(entity)
        }
        return dataList
    }
}