package com.flj.mall.fragments.cart

import android.graphics.Color
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.ISuccess
import com.flj.latte.ui.recycler.adapter.LatteRecyclerAdapter
import com.flj.latte.ui.recycler.adapter.LatteViewHolder
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import com.mikepenz.iconics.view.IconicsTextView

class ShopCartAdapter internal constructor(data: List<LatteItemEntity>) :
    LatteRecyclerAdapter(data) {

    private var mIsSelectAll = false
    var totPrice = 0.00
        private set

    private lateinit var mICartItemPriceListener: ICartItemPriceListener

    fun setCartItemPriceListener(listener: ICartItemPriceListener) {
        this.mICartItemPriceListener = listener
    }

    fun setIsSelectAll(isSelectAll: Boolean) {
        this.mIsSelectAll = isSelectAll
    }

    init {
        addItemType(R.id.type_shop_cart, R.layout.item_shop_cart)
        data.forEach {
            val entity: LatteItemEntity = it
            val price = entity.getField<Double>(R.id.field_count)
            val count = entity.getField<Int>(R.id.field_count)
            val total = price * count
            totPrice += total
        }
    }

    companion object {
        private val OPTIONS = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
    }

    override fun convert(holder: LatteViewHolder, entity: LatteItemEntity) {
        when (holder.itemViewType) {
            R.id.type_shop_cart -> {
                val thumb = entity.getField<String>(R.id.field_image_url)
                val title = entity.getField<String>(R.id.field_title)
                val desc = entity.getField<String>(R.id.field_description)
                val count = entity.getField<Int>(R.id.field_count)
                val price = entity.getField<Double>(R.id.field_price)

                //取出所以控件
                val imgThumb = holder.getView<AppCompatImageView>(R.id.image_item_shop_cart)
                val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_item_shop_cart_title)
                val tvDesc = holder.getView<AppCompatTextView>(R.id.tv_item_shop_cart_desc)
                val tvPrice = holder.getView<AppCompatTextView>(R.id.tv_item_shop_cart_price)
                val iconMinus = holder.getView<IconicsTextView>(R.id.icon_item_minus)
                val iconPlus = holder.getView<IconicsTextView>(R.id.icon_item_plus)
                val tvCount = holder.getView<AppCompatTextView>(R.id.tv_shop_cart_count)
                val iconIsSelected = holder.getView<IconicsTextView>(R.id.icon_item_shop_cart)

                tvTitle.text = title
                tvDesc.text = desc
                tvPrice.text = price.toString()
                tvCount.text = count.toString()

                Glide.with(mContext)
                    .load(thumb)
                    .apply(OPTIONS)
                    .into(imgThumb)

                entity.setField(R.id.field_is_selected, mIsSelectAll)

                val isSelected = entity.getField<Boolean>(R.id.field_is_selected)
                if (isSelected) {
                    iconIsSelected.setTextColor(ContextCompat.getColor(mContext, R.color.app_main))
                } else {
                    iconIsSelected.setTextColor(Color.GRAY)
                }
                //设置左侧勾选的点击事件
                iconIsSelected.setOnClickListener {
                    val currentSelected = entity.getField<Boolean>(R.id.field_is_selected)
                    if (currentSelected) {
                        iconIsSelected.setTextColor(Color.GRAY)
                        entity.setField(R.id.field_is_selected, false)
                    } else {
                        iconIsSelected.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.app_main
                            )
                        )
                        entity.setField(R.id.field_is_selected, true)
                    }
                }

                iconMinus.setOnClickListener {
                    //添加加减事件
                    if (Integer.parseInt(tvCount.text.toString()) > 1) {
                        val currentCount = entity.getField<Int>(R.id.field_count)
                        RestClient.builder()
                            .url("shop_cart_count.php")
//                            .loader(mContext)
                            .params("count", currentCount)
                            .success(object : ISuccess {
                                override fun onSuccess(response: String) {
                                    var countNum = Integer.parseInt(tvCount.text.toString())
                                    countNum--
                                    tvCount.text = countNum.toString()
                                    totPrice -= price
                                    val itemTotal = countNum * price
                                    mICartItemPriceListener.onItemPriceChanged(itemTotal)
                                }
                            })
                            .build()
                            .post()
                    }
                }

                iconPlus.setOnClickListener {
                    val currentCount = entity.getField<Int>(R.id.field_count)
                    //加减货物之后，会立刻调用一次API
                    RestClient
                        .builder()
                        .url("shop_cart_count.php")
//                        .loader(mContext)
                        //真实项目中可能会用到类似的参数
                        //.params("id", "")
                        .params("count", currentCount)
                        .success(object : ISuccess {
                            override fun onSuccess(response: String) {
                                var countNum = Integer.parseInt(tvCount.text.toString())
                                countNum++
                                tvCount.text = countNum.toString()
                                totPrice += price
                                val itemTotal = countNum * price
                                mICartItemPriceListener.onItemPriceChanged(itemTotal)
                            }
                        })
                        .build()
                        .post()
                }
            }
        }
    }
}