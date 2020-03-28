package com.flj.mall.fragments.detail

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flj.latte.ui.recycler.adapter.LatteRecyclerAdapter
import com.flj.latte.ui.recycler.adapter.LatteViewHolder
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R

class DetailImageAdapter
constructor(data: List<LatteItemEntity>) :
    LatteRecyclerAdapter(data) {

    companion object {
        private val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
    }

    init {
        addItemType(R.id.type_goods_detail_image, R.layout.item_image)
    }

    override fun convert(holder: LatteViewHolder, entity: LatteItemEntity) {
        when (holder.itemViewType) {
            R.id.type_goods_detail_image -> {
                val imageView = holder.getView<AppCompatImageView>(R.id.image_rv_item)
                val url = entity.getField<String>(R.id.field_image_url)
                Glide.with(mContext)
                    .load(url)
                    .apply(options)
                    .into(imageView)
            }
        }
    }

}