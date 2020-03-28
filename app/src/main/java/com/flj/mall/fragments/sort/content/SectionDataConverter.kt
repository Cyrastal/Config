package com.flj.mall.fragments.sort.content

import com.alibaba.fastjson.JSON

class SectionDataConverter {

    internal fun convert(json: String): List<SectionBean> {
        val dataList = ArrayList<SectionBean>()
        val dataArray =
            JSON.parseObject(json).getJSONArray("data")

        val size = dataArray.size
        for (i in 0 until size) {
            val data = dataArray.getJSONObject(i)
            val id = data.getInteger("id")
            val title = data.getString("section")

            //具体点初始化title(section)
            val sectionTitleBean = SectionBean(true, title)
            sectionTitleBean.id = id
            sectionTitleBean.isMore = true
            dataList.add(sectionTitleBean)

            val goods = data.getJSONArray("goods")
            val goodsSize = goods.size
            for (j in 0 until goodsSize) {
                val contentItem = goods.getJSONObject(j)
                val goodsId = contentItem.getInteger("goods_id")
                val goodsName = contentItem.getString("goods_name")
                val goodsThumb = contentItem.getString("goods_thumb")
                //初始化内容部分
                val itemEntity = SectionContentItemEntity()
                itemEntity.goodsId = goodsId
                itemEntity.goodsName = goodsName
                itemEntity.goodsThumb = goodsThumb

                dataList.add(SectionBean(itemEntity))
            }
        }

        return dataList
    }
}