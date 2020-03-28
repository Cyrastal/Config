package com.flj.mall.fragments.index

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.SimpleClickListener
import com.flj.latte.ui.recycler.data.LatteItemEntity
import com.flj.mall.R
import com.flj.mall.fragments.MainFragment
import com.flj.mall.fragments.detail.GoodsDetailFragment

class IndexItemClickListener private constructor
    (private val fragment: MainFragment) : SimpleClickListener() {

    companion object {
        fun create(fragment: MainFragment): SimpleClickListener {
            return IndexItemClickListener(fragment)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildLongClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val entity = baseQuickAdapter.data[position] as LatteItemEntity
        val goodsId = entity.getField<Int>(R.id.field_id)
        val goodsDetailFragment = GoodsDetailFragment.create(goodsId)
        fragment.start(goodsDetailFragment)
    }
}