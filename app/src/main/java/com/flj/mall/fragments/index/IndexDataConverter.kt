package com.flj.mall.fragments.index

import com.alibaba.fastjson.JSON
import com.flj.latte.ui.recycler.data.DataConverter
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import java.util.*
import kotlin.collections.ArrayList

class IndexDataConverter(response: String) : DataConverter(response) {

    override fun convert(): LinkedList<LatteItemEntity> {
        val dataList = LinkedList<LatteItemEntity>()
        val dataArray =
            JSON.parseObject(response).getJSONArray("data")
        val size = dataArray.size
        for (i in 0 until size) {
            //循环出了每一个商品的json信息
            val data = dataArray.getJSONObject(i)

            val imageUrl = data.getString("imageUrl")
            val text = data.getString("text")
            val spanSize = data.getInteger("spanSize")
            val id = data.getInteger("goodsId")
            val banners = data.getJSONArray("banners")

            val bannerImages = ArrayList<String>()
            var type = 0
            //具体的判断怎么去渲染数据
            if (imageUrl == null && text != null) {
                type = R.id.type_text
            } else if (imageUrl != null && text == null) {
                type = R.id.type_image
            } else if (imageUrl != null) {
                type = R.id.type_text_image
            } else if (banners != null) {
                type = R.id.type_banner
                //初始化轮播图
                val bannerSize = banners.size
                for (j in 0 until bannerSize) {
                    val banner = banners.getString(j)
                    bannerImages.add(banner)
                }
            }

            //构造entity
            val entity = LatteItemEntity.builder()
                .setField(R.id.field_item_type, type)
                .setField(R.id.field_span_size, spanSize)
                .setField(R.id.field_id, id)
                .setField(R.id.field_text, text)
                .setField(R.id.field_image_url, imageUrl)
                .setField(R.id.field_banners, bannerImages)
                .build()

            dataList.add(entity)
        }
        return dataList
    }
}








