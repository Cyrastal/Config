package com.flj.mall.fragments.index

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flj.latte.ui.banner.BannerCreator
import com.flj.latte.ui.recycler.adapter.LatteRecyclerAdapter
import com.flj.latte.ui.recycler.adapter.LatteViewHolder
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import com.youth.banner.Banner

class IndexRecyclerAdapter(data: List<LatteItemEntity>) : LatteRecyclerAdapter(data) {

    //因为每次加载RecyclerView都会初始化变量(Banner实例),所以会出现重复加载
    //确保Banner初始化一次
    private var mIsInitBanner = false

    init {
        addItemType(R.id.type_text, R.layout.item_index_text)
        addItemType(R.id.type_image, R.layout.item_index_image)
        addItemType(R.id.type_text_image, R.layout.item_index_image_text)
        addItemType(R.id.type_banner, R.layout.item_index_banner)
    }

    //初始化静态的参数和对象
    companion object {
        private val RECYCLER_OPTIONS = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
    }

    override fun convert(holder: LatteViewHolder, entity: LatteItemEntity) {
        val text: String
        val imageUrl: String
        val bannerImages: ArrayList<String>
        when (holder.itemViewType) {
            R.id.type_text -> {
                text = entity.getField(R.id.field_text)
                holder.setText(R.id.text_single, text)
            }

            R.id.type_image -> {
                imageUrl = entity.getField(R.id.field_image_url)
                Glide.with(mContext)
                    .load(imageUrl)
                    .apply(RECYCLER_OPTIONS)
                    .into(holder.getView<View>(R.id.img_single) as ImageView)
            }

            R.id.type_text_image -> {
                text = entity.getField(R.id.field_text)
                imageUrl = entity.getField(R.id.field_image_url)
                holder.setText(R.id.tv_multiple, text)
                Glide.with(mContext)
                    .load(imageUrl)
                    .apply(RECYCLER_OPTIONS)
                    .into(holder.getView<View>(R.id.img_multiple) as ImageView)
            }

            R.id.type_banner -> if (!mIsInitBanner) {
                bannerImages = entity.getField(R.id.field_banners)
                val banner =
                    holder.getView<Banner>(R.id.banner_recycler_item)
                //先设置成默认的轮播样式
                BannerCreator.setDefault(banner, bannerImages)
                mIsInitBanner = true
            }
        }
    }
}